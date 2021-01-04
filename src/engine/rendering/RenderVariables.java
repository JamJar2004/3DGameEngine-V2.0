package engine.rendering;

import engine.core.math.*;
import games.entities.lights.*;

import java.util.HashMap;

public class RenderVariables
{
    private HashMap<String, Vector2f>  vector2fHashMap;
    private HashMap<String, Vector3f>  vector3fHashMap;
    private HashMap<String, Vector4f>  vector4fHashMap;
    private HashMap<String, Matrix4f>  matrix4fHashMap;
    private HashMap<String, Float>     floatHashMap;
    private HashMap<String, Integer>   intHashMap;
    private HashMap<String, Integer>   boolHashMap;
    private HashMap<String, Texture>   textureHashMap;
    private HashMap<String, CubeMap>   cubeMapHashMap;
    private HashMap<String, BaseLight> lightHashMap;

    public RenderVariables()
    {
        vector2fHashMap = new HashMap<>();
        vector3fHashMap = new HashMap<>();
        vector4fHashMap = new HashMap<>();
        matrix4fHashMap = new HashMap<>();
        floatHashMap    = new HashMap<>();
        intHashMap      = new HashMap<>();
        boolHashMap     = new HashMap<>();
        textureHashMap  = new HashMap<>();
        cubeMapHashMap  = new HashMap<>();
        lightHashMap    = new HashMap<>();
    }

    private void addVector2f(String name, Vector2f value)
    {
        if(!vector2fHashMap.containsKey(name))
            vector2fHashMap.put(name, value);
        else
        {
            System.err.println("This vector2f name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addVector3f(String name, Vector3f value)
    {
        if(!vector3fHashMap.containsKey(name))
            vector3fHashMap.put(name, value);
        else
        {
            System.err.println("This vector3f name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addVector4f(String name, Vector4f value)
    {
        if(!vector4fHashMap.containsKey(name))
            vector4fHashMap.put(name, value);
        else
        {
            System.err.println("This vector4f name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addMatrix4f(String name, Matrix4f value)
    {
        if(!matrix4fHashMap.containsKey(name))
            matrix4fHashMap.put(name, value);
        else
        {
            System.err.println("This matrix4f name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addFloat(String name, float value)
    {
        if(!floatHashMap.containsKey(name))
            floatHashMap.put(name, value);
        else
        {
            System.err.println("This float name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addInteger(String name, int value)
    {
        if(!intHashMap.containsKey(name))
            intHashMap.put(name, value);
        else
        {
            System.err.println("This integer name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addBoolean(String name, boolean value)
    {
        if(!boolHashMap.containsKey(name))
        {
            if(value)
                boolHashMap.put(name, 1);
            else
                boolHashMap.put(name, 0);
        }
        else
        {
            System.err.println("This boolean name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addTexture(String name, Texture value)
    {
        if(!textureHashMap.containsKey(name))
        {
            value.setSamplerSlot(textureHashMap.size());
            textureHashMap.put(name, value);
            setInteger(name + "_EXISTS", 1);
        }
        else
        {
            System.err.println("This texture name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addCubeMap(String name, CubeMap value)
    {
        if(!cubeMapHashMap.containsKey(name))
        {
            value.setSamplerSlot(cubeMapHashMap.size());
            cubeMapHashMap.put(name, value);
            setInteger(name + "_EXISTS", 1);
        }
        else
        {
            System.err.println("This cube map name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addLight(String name, BaseLight value)
    {
        if(!lightHashMap.containsKey(name))
            lightHashMap.put(name, value);
        else
        {
            System.err.println("This light name already exists.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    public void setVector2f(String name, Vector2f value)
    {
        if(!vector2fHashMap.containsKey(name))
        {
            addVector2f(name, value);
        }
        else
            vector2fHashMap.replace(name, value);
    }

    public void setVector3f(String name, Vector3f value)
    {
        if(!vector3fHashMap.containsKey(name))
        {
            addVector3f(name, value);
        }
        else
            vector3fHashMap.replace(name, value);
    }

    public void setVector4f(String name, Vector4f value)
    {
        if(!vector4fHashMap.containsKey(name))
        {
            addVector4f(name, value);
        }
        else
            vector4fHashMap.replace(name, value);
    }

    public void setMatrix4f(String name, Matrix4f value)
    {
        if(!matrix4fHashMap.containsKey(name))
        {
            addMatrix4f(name, value);
        }
        else
            matrix4fHashMap.replace(name, value);
    }

    public void setFloat(String name, float value)
    {
        if(!floatHashMap.containsKey(name))
        {
            addFloat(name, value);
        }
        else
            floatHashMap.replace(name, value);
    }

    public void setInteger(String name, int value)
    {
        if(!intHashMap.containsKey(name))
        {
            addInteger(name, value);
        }
        else
            intHashMap.replace(name, value);
    }

    public void setBoolean(String name, boolean value)
    {
        if(!boolHashMap.containsKey(name))
        {
            addBoolean(name, value);
        }
        else
            if(value)
                boolHashMap.replace(name, 1);
            else
                boolHashMap.replace(name, 0);
    }

    public void setTexture(String name, Texture value)
    {
        if(!textureHashMap.containsKey(name))
        {
            addTexture(name, value);
        }
        else
            textureHashMap.replace(name, value);
    }

    public void setCubeMap(String name, CubeMap value)
    {
        if(!cubeMapHashMap.containsKey(name))
        {
            addCubeMap(name, value);
        }
        else
            cubeMapHashMap.replace(name, value);
    }

    public void setLight(String name, BaseLight value)
    {
        if(!lightHashMap.containsKey(name))
        {
            addLight(name, value);
        }
        else
            lightHashMap.replace(name, value);
    }

    public Vector2f getVector2f(String name)
    {
        if(vector2fHashMap.containsKey(name))
            return vector2fHashMap.get(name);
        else
            return new Vector2f(1, 1);
    }

    public Vector3f getVector3f(String name)
    {
        if(vector3fHashMap.containsKey(name))
            return vector3fHashMap.get(name);
        else
            return new Vector3f(1, 1, 1);
    }

    public Vector4f getVector4f(String name)
    {
        if(vector4fHashMap.containsKey(name))
            return vector4fHashMap.get(name);
        else
            return new Vector4f(1, 1, 1, 1);
    }

    public Matrix4f getMatrix4f(String name)
    {
        if(matrix4fHashMap.containsKey(name))
            return matrix4fHashMap.get(name);
        else
            return new Matrix4f();
    }

    public float getFloat(String name)
    {
        if(floatHashMap.containsKey(name))
            return floatHashMap.get(name);
        else
            return 0;
    }

    public int getInteger(String name)
    {
        if(intHashMap.containsKey(name))
            return intHashMap.get(name);
        else if(boolHashMap.containsKey(name))
            return boolHashMap.get(name);
        else
            return 0;
    }

    public boolean getBoolean(String name)
    {
        if(boolHashMap.containsKey(name))
            return boolHashMap.get(name) == 1;
        else
            return false;
    }

    public Texture getTexture(String name)
    {
        if(textureHashMap.containsKey(name))
            return textureHashMap.get(name);
        else
            return null;
    }

    public CubeMap getCubeMap(String name)
    {
        if(cubeMapHashMap.containsKey(name))
            return cubeMapHashMap.get(name);
        else
            return null;
    }

    public BaseLight getLight(String name)
    {
        if(lightHashMap.containsKey(name))
            return lightHashMap.get(name);
        else
            return new BaseLight(new Vector3f(0, 0, 0), 0.0f);
    }

    public void setVariables(RenderVariables other)
    {
        for(String name : other.vector2fHashMap.keySet())
        {
            if(other.vector2fHashMap.containsKey(name))
                setVector2f(name, other.vector2fHashMap.get(name));
        }

        for(String name : other.vector3fHashMap.keySet())
        {
            if(other.vector3fHashMap.containsKey(name))
                setVector3f(name, other.vector3fHashMap.get(name));
        }

        for(String name : other.vector4fHashMap.keySet())
        {
            if(other.vector4fHashMap.containsKey(name))
                setVector4f(name, other.vector4fHashMap.get(name));
        }

        for(String name : other.matrix4fHashMap.keySet())
        {
            if(other.matrix4fHashMap.containsKey(name))
                setMatrix4f(name, other.matrix4fHashMap.get(name));
        }

        for(String name : other.floatHashMap.keySet())
        {
            if(other.floatHashMap.containsKey(name))
                setFloat(name, other.floatHashMap.get(name));
        }

        for(String name : other.intHashMap.keySet())
        {
            if(other.intHashMap.containsKey(name))
                setInteger(name, other.intHashMap.get(name));
        }

        for(String name : other.boolHashMap.keySet())
        {
            if(other.boolHashMap.containsKey(name))
                setBoolean(name, other.boolHashMap.get(name) == 1);
        }

        for(String name : other.textureHashMap.keySet())
        {
            if(other.textureHashMap.containsKey(name))
                setTexture(name, other.textureHashMap.get(name));
        }

        for(String name : other.cubeMapHashMap.keySet())
        {
            if(other.cubeMapHashMap.containsKey(name))
                setCubeMap(name, other.cubeMapHashMap.get(name));
        }

        for(String name : other.lightHashMap.keySet())
        {
            if(other.lightHashMap.containsKey(name))
                setLight(name, other.lightHashMap.get(name));
        }
    }

    public RenderVariables clone()
    {
        RenderVariables result = new RenderVariables();
        result.setVariables(this);
        return result;
    }
}
