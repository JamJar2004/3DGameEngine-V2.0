package engine.core;

import engine.audio.AudioEngine;
import engine.core.math.Vector3f;
import games.entities.Camera;
import engine.rendering.RenderingEngine;
import games.entities.Entity;

import java.util.HashMap;

public abstract class Game
{
    private Camera                   mainCamera;
    private HashMap<Integer, Entity> entities;
    private HashMap<Integer, Entity> entitiesToBeAdded;
    private HashMap<Integer, Entity> entitiesToBeFreed;
    private Vector3f                 ambientLight;
    private RenderingEngine          renderingEngine;
    private AudioEngine              audioEngine;
    private boolean                  lightingEffects;
    private boolean                  highQuality;

    private boolean hasBeenExited;
    private float   exitTime;
    private float   exitPassedTime;

    public Game()
    {
        lightingEffects   = true;
        entities          = new HashMap<>();
        entitiesToBeAdded = new HashMap<>();
        entitiesToBeFreed = new HashMap<>();
        mainCamera        = new Camera();
        ambientLight      = new Vector3f(0.1f);
        lightingEffects   = true;
        highQuality       = true;
        hasBeenExited     = false;
        exitTime          = 0;
        exitPassedTime    = 0;
    }

    public void init() {}

    public void input(float delta)
    {
        mainCamera.input(delta);

        for(Entity entity: entities.values())
            entity.input(delta);
    }

    public void update(float delta)
    {
        mainCamera.update(delta);

        for(Entity entity : entitiesToBeAdded.values())
        {
            entities.put(entity.getID(), entity);
        }

        entitiesToBeAdded.clear();

        for(Entity entity: entities.values())
        {
            if(entity.getIsDeleted())
            {
                entitiesToBeFreed.put(entity.getID(), entity);
            }
            else
                entity.update(delta);
        }

        for(Entity entity : entitiesToBeFreed.values())
        {
            freeEntity(entity);
        }
        entitiesToBeFreed.clear();

        if(hasBeenExited)
        {
            if(exitPassedTime < exitTime)
                exitPassedTime += delta;
            else
                exit();
        }
    }

    public void render()
    {
        renderingEngine.setVariable("ambientLight", ambientLight);
        renderingEngine.setVariable("highQuality", highQuality);

        if(highQuality)
            renderingEngine.setVariable("shading", lightingEffects);
        else
            renderingEngine.setVariable("shading", false);

        renderingEngine.render(mainCamera);
    }

    public void gameLoop(float delta) {}

    public Camera   getMainCamera()   { return mainCamera;   }
    public Vector3f getAmbientLight() { return ambientLight; }

    public Entity getEntity(int entityID)
    {
        return entities.get(entityID);
    }

    public void addEntity(Entity entity)
    {
        entity.setGame(this);
        //entities.put(entity.getID(), entity);
        entitiesToBeAdded.put(entity.getID(), entity);

        if(!renderingEngine.hasEntityType(entity.getTypeName()))
            renderingEngine.addEntityType(entity.getTypeName());

        renderingEngine.addEntity(entity.getTypeName(), entity);
    }

    public void showEntity(Entity entity)
    {
        entities.get(entity.getID()).show();
        renderingEngine.showEntity(entity.getTypeName(), entity);
    }

    public void hideEntity(Entity entity)
    {
        entities.get(entity.getID()).hide();
        renderingEngine.hideEntity(entity.getTypeName(), entity);
    }

    public void freeEntity(Entity entity)
    {
        entity.setGame(null);
        entities.remove(entity.getID());
        renderingEngine.freeEntity(entity);
    }

    public void placeEntities(EntityPlaceMap placeMap, Vector3f scale, Vector3f offset)
    {
        for(Vector3f position : placeMap.getPositions())
        {
            Entity entity = placeMap.getEntity().clone();
            entity.getTransformation().setPosition(position.multiply(scale).add(offset));
            addEntity(entity);
        }
    }

    public void setListenerPosition(Vector3f position)
    {
        audioEngine.setListenerPosition(position);
    }

    public void exit()
    {
        System.exit(0);
    }

    public void exit(float time)
    {
        hasBeenExited = true;
        exitTime = time;
    }

    public void turnOnLightingEffects()  { lightingEffects = true; }
    public void turnOffLightingEffects() { highQuality = false;    }

    public void turnOnHighQuality()  { lightingEffects = true; }
    public void turnOffHighQuality() { highQuality = false;    }

    public void setCamera(Camera camera) { mainCamera = camera; }
    public void setRenderingEngine(RenderingEngine renderingEngine) { this.renderingEngine = renderingEngine;    }
    public void setAudioEngine(AudioEngine audioEngine)             { this.audioEngine = audioEngine;            }
    public void setAmbientLight(Vector3f ambientLight)              { this.ambientLight = ambientLight;          }
    public void setAmbientLight(float r, float g, float b)          { this.ambientLight = new Vector3f(r, g, b); }
    public void setClearColor(Vector3f color) { renderingEngine.setClearColor(color); }

    public void clearEntities()
    {
        for(Entity entity : entities.values())
        {
            entity.hide();
            entity.free();
        }
    }
}
