package engine.rendering;

import engine.core.math.*;

public class Material
{
    private RenderVariables variables;

    public Material()
    {
        variables = new RenderVariables();
    }

    public RenderVariables getVariables()
    {
        return variables;
    }

    public void setVariables(RenderVariables variables)
    {
        this.variables = variables;
    }

    public void setTexture(String name, Texture texture)    { variables.setTexture(name, texture);   }
    public void setCubeMap(String name, CubeMap cubeMap)    { variables.setCubeMap(name, cubeMap);   }
    public void setVector2f(String name, Vector2f vector2f) { variables.setVector2f(name, vector2f); }
    public void setVector3f(String name, Vector3f vector3f) { variables.setVector3f(name, vector3f); }
    public void setFloat(String name, float value)          { variables.setFloat(name, value);       }
    public void setBoolean(String name, boolean value)      { variables.setBoolean(name, value);     }

    public Texture getTexture(String name)
    {
        if(variables.getTexture(name) != null)
            return variables.getTexture(name);
        else
            return Texture.WHITE_TEXTURE;
    }

    public CubeMap getCubeMap(String name)
    {
        if(variables.getCubeMap(name) != null)
            return variables.getCubeMap(name);
        else
            return (CubeMap) Texture.WHITE_TEXTURE;
    }

    public Vector2f getVector2f(String name)
    {
        if(variables.getVector3f(name) != null)
            return variables.getVector2f(name);
        else
            return new Vector2f(1, 1);
    }

    public Vector3f getVector3f(String name)
    {
        if(variables.getVector3f(name) != null)
            return variables.getVector3f(name);
        else
            return new Vector3f(1, 1, 1);
    }
    public float    getFloat(String name)    { return variables.getFloat(name);   }
    public boolean  getBoolean(String name)  { return variables.getBoolean(name); }

    public Material clone()
    {
        Material result = new Material();
        result.setVariables(getVariables().clone());
        return result;
    }
}
