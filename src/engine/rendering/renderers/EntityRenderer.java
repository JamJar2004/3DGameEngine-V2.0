package engine.rendering.renderers;

import engine.core.math.Matrix4f;
import engine.core.math.Vector2f;
import engine.core.math.Vector3f;
import engine.core.math.Vector4f;
import engine.rendering.*;
import games.entities.lights.BaseLight;
import games.entities.Camera;
import games.entities.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public abstract class EntityRenderer
{
    private Entity            entity;
    private RenderVariables   variables;
    private RenderingEngine   renderingEngine;

    public EntityRenderer(Entity entity)
    {
        this.entity = entity;
        variables   = new RenderVariables();
    }

    public void render(Camera camera) {}

    public Entity getEntity()                    { return entity; }
    public RenderVariables getVariables()        { return variables; }
    public Shader getShader(String shaderName)   { return renderingEngine.getShader(shaderName); }
    public RenderingEngine getRenderingEngine()  { return renderingEngine; }


    public void bindShader(String shaderName)           { getShader(shaderName).bind(); }
    public void updateShaderUniforms(String shaderName) { getShader(shaderName).updateUniforms(this); }

    public void setEntity(Entity entity)                { this.entity = entity; }
    public void setRenderingEngine(RenderingEngine renderingEngine)
    {
        this.renderingEngine = renderingEngine;
        addRenderingEngineVariables();
    }

    public void addRenderingEngineVariables() {}

    public void loadRenderingEngineVector2f(String name) { setVariable(name, renderingEngine.getRenderingVariables().getVector2f(name)); }
    public void loadRenderingEngineVector3f(String name) { setVariable(name, renderingEngine.getRenderingVariables().getVector3f(name)); }
    public void loadRenderingEngineVector4f(String name) { setVariable(name, renderingEngine.getRenderingVariables().getVector4f(name)); }
    public void loadRenderingEngineMatrix4f(String name) { setVariable(name, renderingEngine.getRenderingVariables().getMatrix4f(name)); }
    public void loadRenderingEngineFloat(String name)    { setVariable(name, renderingEngine.getRenderingVariables().getFloat(name));    }
    public void loadRenderingEngineInteger(String name)  { setVariable(name, renderingEngine.getRenderingVariables().getInteger(name));  }
    public void loadRenderingEngineBoolean(String name)  { setVariable(name, renderingEngine.getRenderingVariables().getBoolean(name));  }
    public void loadRenderingEngineTexture(String name)  { setVariable(name, renderingEngine.getRenderingVariables().getTexture(name));  }
    public void loadRenderingEngineCubeMap(String name)  { setVariable(name, renderingEngine.getRenderingVariables().getCubeMap(name));  }
    public void loadRenderingEngineLight(String name)    { setVariable(name, renderingEngine.getRenderingVariables().getLight(name));    }

    public Entity getRenderingEngineEntity(String entityTypeName, int index)          { return renderingEngine.getEntity(entityTypeName, index); }
    public HashMap<Integer, Entity> getRenderingEngineEntities(String entityTypeName) { return renderingEngine.getEntities(entityTypeName);      }
    public Collection<HashMap<Integer, Entity>> getAllRenderingEngineEntities()       { return renderingEngine.getAllEntities();                 }

    public void setVariables(RenderVariables variables)   { this.variables.setVariables(variables); }

    public void setVariable(String name, Vector2f value)  { variables.setVector2f(name, value); }
    public void setVariable(String name, Vector3f value)  { variables.setVector3f(name, value); }
    public void setVariable(String name, Vector4f value)  { variables.setVector4f(name, value); }
    public void setVariable(String name, Matrix4f value)  { variables.setMatrix4f(name, value); }
    public void setVariable(String name, float value)     { variables.setFloat(name, value); }
    public void setVariable(String name, int value)       { variables.setInteger(name, value); }
    public void setVariable(String name, boolean value)   { variables.setBoolean(name, value); }
    public void setVariable(String name, Texture value)   { variables.setTexture(name, value); }
    public void setVariable(String name, CubeMap value)   { variables.setCubeMap(name, value); }
    public void setVariable(String name, BaseLight value) { variables.setLight(name, value); }
}
