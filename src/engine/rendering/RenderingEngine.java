package engine.rendering;

import engine.core.math.Matrix4f;
import engine.core.math.Vector2f;
import engine.core.math.Vector4f;
import games.entities.lights.BaseLight;
import games.entities.Camera;
import games.entities.Entity;
import engine.core.math.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

public class RenderingEngine
{
    private HashMap<String, Shader>                   shaders;
    private RenderVariables                           renderingVariables;
    private HashMap<String, HashMap<Integer, Entity>> entities;
    private HashMap<Integer, Entity>                  transparentEntities;

    public RenderingEngine()
    {
        shaders             = new HashMap<>();
        renderingVariables  = new RenderVariables();
        entities            = new HashMap<>();
        transparentEntities = new HashMap<>();

        setClearColor(new Vector3f(0, 0, 0));
        clearScreen();

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_CLAMP);

        glEnable(GL_TEXTURE_2D);
    }

    public void render(Camera camera)
    {
        transparentEntities.clear();

        clearScreen();
        for(HashMap<Integer, Entity> entityList : entities.values())
        {
            for(Entity entity : entityList.values())
            {
                if (entity.hasRenderer() && !entity.getIsHidden() && !entity.getContainsTransparency())
                {
                    entity.getRenderer().setRenderingEngine(this);
                    entity.getRenderer().render(camera);
                }

                if(entity.getContainsTransparency())
                {
                    transparentEntities.put(entity.getID(), entity);
                }
            }
        }

        for(Integer index : transparentEntities.keySet())
        {
            Entity entity =  transparentEntities.get(index);
            if (entity.hasRenderer() && !entity.getIsHidden())
            {
                entity.getRenderer().setRenderingEngine(this);
                entity.getRenderer().render(camera);
            }
        }
    }

    public Shader getShader(String shaderName)
    {
        if(!shaders.containsKey(shaderName))
            shaders.put(shaderName, new Shader(shaderName));

        return shaders.get(shaderName);
    }

    public RenderVariables getRenderingVariables()
    {
        return renderingVariables;
    }

    public boolean hasEntityType(String entityTypeName) { return entities.containsKey(entityTypeName); }

    public Entity getEntity(String entityTypeName, int index)               { return entities.get(entityTypeName).get(0);   }
    public HashMap<Integer, Entity> getEntities(String entityTypeName)      { return entities.get(entityTypeName);          }
    public Collection<HashMap<Integer, Entity>> getAllEntities()            { return entities.values(); }
    public Set<String> getEntityTypes()                                     { return entities.keySet(); }


    public void clearScreen()      { glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); }
    public void clearColorBuffer() { glClear(GL_COLOR_BUFFER_BIT); }
    public void clearDepthBuffer() { glClear(GL_DEPTH_BUFFER_BIT); }

    public void setClearColor(Vector3f color) { glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f); }

    public void setVariable(String name, Vector2f value)  { renderingVariables.setVector2f(name, value); }
    public void setVariable(String name, Vector3f value)  { renderingVariables.setVector3f(name, value); }
    public void setVariable(String name, Vector4f value)  { renderingVariables.setVector4f(name, value); }
    public void setVariable(String name, Matrix4f value)  { renderingVariables.setMatrix4f(name, value); }
    public void setVariable(String name, float value)     { renderingVariables.setFloat(name, value);    }
    public void setVariable(String name, int value)       { renderingVariables.setInteger(name, value);  }
    public void setVariable(String name, boolean value)   { renderingVariables.setBoolean(name, value);  }
    public void setVariable(String name, Texture value)   { renderingVariables.setTexture(name, value);  }
    public void setVariable(String name, CubeMap value)   { renderingVariables.setCubeMap(name, value);  }
    public void setVariable(String name, BaseLight value) { renderingVariables.setLight(name, value);    }

    public void addEntityType(String entityTypeName)            { entities.put(entityTypeName, new HashMap<>()); }
    public void addEntity(String entityTypeName, Entity entity) { entities.get(entityTypeName).put(entity.getID(), entity); }

    public void clearEntities()                                 { entities.clear(); }

    public void freeEntity(Entity entity)
    {
        entities.get(entity.getTypeName()).remove(entity.getID());
    }

    public void showEntity(String entityTypeName, Entity entity)
    {
        entities.get(entity.getTypeName()).get(entity.getID()).show();
    }

    public void hideEntity(String entityTypeName, Entity entity)
    {
        entities.get(entity.getTypeName()).get(entity.getID()).hide();
    }

    public static String getOpenGLVersion() { return glGetString(GL_VERSION); }
}
