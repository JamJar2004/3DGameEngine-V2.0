/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This code has been copied from thebennybox 3D Game Engine tutorial series.
 * In this code I modified the updateUniforms() method to work with RenderVariables.
 * I also made GLSLStruct have information about it's fields which I called "GLSLVariable".
 * The shader creation and uniform parsing have been left untouched.
 */

package engine.rendering;

import engine.core.Util;
import engine.core.math.Matrix4f;
import engine.core.math.Vector2f;
import engine.core.math.Vector3f;
import engine.core.math.Vector4f;
import engine.rendering.renderers.EntityRenderer;
import games.entities.lights.BaseLight;
import games.entities.lights.DirectionalLight;
import games.entities.lights.PointLight;
import games.entities.lights.SpotLight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

public class Shader
{
    private int                           program;
    private HashMap<String, Integer>      uniformLocations;
    private HashMap<String, GLSLStruct>   structs;
    private ArrayList<GLSLVariable>       uniforms;

    public Shader(String fileName)
    {
        program          = glCreateProgram();
        uniformLocations = new HashMap<>();
        structs          = new HashMap<>();
        uniforms         = new ArrayList<>();

        if(program == 0)
        {
            System.err.println("Shader creation failed: Could not find valid memory location in constructor");
            System.exit(1);
        }

        String vertexShaderText = loadShader(fileName + ".vs");
        String fragmentShaderText = loadShader(fileName + ".fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        addAllAttributes(vertexShaderText);

        compileShader();

        addAllUniforms(vertexShaderText);
        addAllUniforms(fragmentShaderText);
    }

    private static String loadShader(String fileName)
    {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;
        final String INCLUDE_DIRECTIVE = "#include";

        try
        {
            shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
            String line;

            while((line = shaderReader.readLine()) != null)
            {
                if(line.startsWith(INCLUDE_DIRECTIVE))
                {
                    shaderSource.append(loadShader(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))));
                }
                else
                    shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }

    private void addVertexShader(String text)   { addProgram(text, GL_VERTEX_SHADER); }

    private void addGeometryShader(String text) { addProgram(text, GL_GEOMETRY_SHADER); }

    private void addFragmentShader(String text) { addProgram(text, GL_FRAGMENT_SHADER); }

    public void updateUniforms(EntityRenderer renderer)
    {
        RenderVariables variables = renderer.getVariables();

        for(GLSLVariable uniform : uniforms)
        {
            if(uniform.getType().equals("vec2"))
                setUniformVec2(uniform.getName(), variables.getVector2f(uniform.getName()));
            else if(uniform.getType().equals("vec3"))
                setUniformVec3(uniform.getName(), variables.getVector3f(uniform.getName()));
            else if(uniform.getType().equals("vec4"))
                setUniformVec4(uniform.getName(), variables.getVector4f(uniform.getName()));
            else if(uniform.getType().equals("mat4"))
                setUniformMat4(uniform.getName(), variables.getMatrix4f(uniform.getName()));
            else if(uniform.getType().equals("float"))
                setUniformFloat(uniform.getName(), variables.getFloat(uniform.getName()));
            else if(uniform.getType().equals("int"))
                setUniformInt(uniform.getName(), variables.getInteger(uniform.getName()));
            else if(uniform.getType().equals("BaseLight"))
                setUniformBaseLight(uniform.getName(), variables.getLight(uniform.getName()));
            else if(uniform.getType().equals("DirectionalLight"))
                setUniformDirectionalLight(uniform.getName(), (DirectionalLight)variables.getLight(uniform.getName()));
            else if(uniform.getType().equals("PointLight"))
                setUniformPointLight(uniform.getName(), (PointLight)variables.getLight(uniform.getName()));
            else if(uniform.getType().equals("SpotLight"))
                setUniformSpotLight(uniform.getName(), (SpotLight)variables.getLight(uniform.getName()));
            else if(uniform.getType().equals("sampler2D"))
            {
                Texture tex = variables.getTexture(uniform.getName());

                if(tex != null)
                {
                    tex.bind();
                    setUniformInt(uniform.getName(), tex.getSamplerSlot());
                }
            }
            else if(uniform.getType().equals("samplerCube"))
            {
                CubeMap cubeMap = variables.getCubeMap(uniform.getName());

                if(cubeMap != null)
                {
                    cubeMap.bind();
                    setUniformInt(uniform.getName(), cubeMap.getSamplerSlot());
                }
            }
        }
    }

    private void loadStructs(String shaderText)
    {
        final String STRUCT_KEYWORD = "struct";
        int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);

        while(structStartLocation != -1)
        {
            int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
            int nameEnd   = shaderText.indexOf("{", nameBegin);
            int braceEnd = shaderText.indexOf("}", nameEnd);

            String structName = shaderText.substring(nameBegin, nameEnd);
            structName = Util.removeWhiteSpaces(structName);

            GLSLStruct struct = new GLSLStruct();

            int semicolonPosition = shaderText.indexOf(";", nameEnd);
            while(semicolonPosition != -1 && semicolonPosition < braceEnd)
            {
                int varNameEnd = semicolonPosition + 1;

                while(Character.isWhitespace(shaderText.charAt(varNameEnd - 1)) || shaderText.charAt(varNameEnd - 1) == ';')
                    varNameEnd--;

                int varNameStart = semicolonPosition;

                while(!Character.isWhitespace(shaderText.charAt(varNameStart - 1)))
                    varNameStart--;

                int varTypeEnd = varNameStart;

                while(Character.isWhitespace(shaderText.charAt(varTypeEnd - 1)))
                    varTypeEnd--;

                int varTypeStart = varTypeEnd;

                while(!Character.isWhitespace(shaderText.charAt(varTypeStart - 1)))
                    varTypeStart--;

                String varName = shaderText.substring(varNameStart, varNameEnd);
                String varType = shaderText.substring(varTypeStart, varTypeEnd);

                struct.addVariable(new GLSLVariable(varName, varType));

                semicolonPosition = shaderText.indexOf(";", semicolonPosition + 1);
            }

            structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
            structs.put(structName, struct);
        }
    }

    private void addAllAttributes(String shaderText)
    {
        final String ATTRIBUTE_KEYWORD = "attribute";
        int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);

        int slot = 0;
        while(attributeStartLocation != -1)
        {
            int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
            int end   = shaderText.indexOf(";", begin);

            String attributeLine = shaderText.substring(begin, end);
            String attributeName = attributeLine.substring(attributeLine.indexOf(" ") + 1, attributeLine.length());
            attributeName = Util.removeWhiteSpaces(attributeName);

            setAttribLocation(attributeName, slot);
            slot++;

            attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
        }
    }

    public void setAttribLocation(String attributeName, int location)
    {
        glBindAttribLocation(program, location, attributeName);
    }

    private void addAllUniforms(String shaderText)
    {
        loadStructs(shaderText);

        final String UNIFORM_KEYWORD = "uniform";
        int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);

        while(uniformStartLocation != -1)
        {
            int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
            int end   = shaderText.indexOf(";", begin);

            String uniformLine      = shaderText.substring(begin, end).trim();
            int whiteSpacePosition  = uniformLine.indexOf(' ');
            String uniformName      = uniformLine.substring(whiteSpacePosition + 1, uniformLine.length()).trim();
            String uniformType      = uniformLine.substring(0, whiteSpacePosition).trim();

            addUniform(uniformName, uniformType);
            uniforms.add(new GLSLVariable(uniformName, uniformType));

            uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
        }
    }

    private void addUniform(String uniformName, String uniformType)
    {
        if(structs.containsKey(uniformType))
        {
            GLSLStruct foundStruct = structs.get(uniformType);
            for(GLSLVariable variable : foundStruct.getVariables())
            {
                addUniform(uniformName + "." + variable.getName(), variable.getType());
            }
        }
        else
        {
            addUniform(uniformName);
        }
    }

    private void addUniform(String uniformName)
    {
        int uniformLocation = glGetUniformLocation(program, uniformName);

        if(uniformLocation == 0xFFFFFFFF)
        {
            System.out.println("Warning: The uniform " + uniformName + " was either not found or not used.");
        }

        uniformLocations.put(uniformName, uniformLocation);
    }

    public void compileShader()
    {
        glLinkProgram(program);

        if(glGetProgrami(program, GL_LINK_STATUS) == 0)
        {
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);

        if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0)
        {
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }
    }

    private void addProgram(String text, int type)
    {
        int shader = glCreateShader(type);

        if(shader == 0)
        {
            System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
            System.exit(1);
        }

        glShaderSource(shader, text);
        glCompileShader(shader);

        if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0)
        {
            System.err.println(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
    }

    public void bind() { glUseProgram(program); }

    public void setUniformInt(String uniformName, int value) { glUniform1i(uniformLocations.get(uniformName), value); }

    public void setUniformBoolean(String uniformName, boolean value)
    {
        if(value)
            setUniformInt(uniformName, 1);
        else
            setUniformInt(uniformName, 0);
    }

    public void setUniformFloat(String uniformName, float value) { glUniform1f(uniformLocations.get(uniformName), value); }

    public void setUniformVec2(String uniformName, Vector2f value) { glUniform2f(uniformLocations.get(uniformName), value.getX(), value.getY()); }

    public void setUniformVec3(String uniformName, Vector3f value) { glUniform3f(uniformLocations.get(uniformName), value.getX(), value.getY(), value.getZ()); }

    public void setUniformVec4(String uniformName, Vector4f value) { glUniform4f(uniformLocations.get(uniformName), value.getX(), value.getY(), value.getZ(), value.getW()); }

    public void setUniformMat4(String uniformName, Matrix4f value) { glUniformMatrix4(uniformLocations.get(uniformName), true, Util.createFlippedBuffer(value)); }

    public void setUniformBaseLight(String uniformName, BaseLight baseLight)
    {
        setUniformVec3(uniformName + ".color", baseLight.getColor());
        setUniformFloat(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight)
    {
        setUniformBaseLight(uniformName + ".base", directionalLight);
        setUniformVec3(uniformName + ".direction", directionalLight.getTransformation().getRotation().getFront());
    }

    public void setUniformAttenuation(String uniformName, Attenuation attenuation)
    {
        setUniformFloat(uniformName + ".constant", attenuation.getConstant());
        setUniformFloat(uniformName + ".linear", attenuation.getLinear());
        setUniformFloat(uniformName + ".exponent", attenuation.getExponent());
    }

    public void setUniformPointLight(String uniformName, PointLight pointLight)
    {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformAttenuation(uniformName + ".attenuation", pointLight.getAttenuation());
        setUniformVec3(uniformName + ".position", pointLight.getTransformation().getPosition());
        setUniformFloat(uniformName + ".range", pointLight.getRange());
    }

    public void setUniformSpotLight(String uniformName, SpotLight spotLight)
    {
        setUniformPointLight(uniformName + ".pointLight", spotLight);
        setUniformVec3(uniformName + ".direction", spotLight.getTransformation().getRotation().getFront());
        setUniformFloat(uniformName + ".cutoff", spotLight.getCutoff());
    }

    private class GLSLStruct
    {
        private ArrayList<GLSLVariable> variables;

        public GLSLStruct()
        {
            variables = new ArrayList<>();
        }

        public void addVariable(GLSLVariable variable)
        {
            variables.add(variable);
        }

        public ArrayList<GLSLVariable> getVariables()
        {
            return variables;
        }
    }

    private class GLSLVariable
    {
        private String name;
        private String type;

        public GLSLVariable(String name, String type)
        {
            this.name = name;
            this.type = type;
        }
        public String getName() { return name; }
        public String getType() { return type; }
    }
}
