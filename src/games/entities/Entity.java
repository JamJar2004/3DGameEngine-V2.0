package games.entities;

import engine.core.Game;
import engine.core.math.Transformation;
import engine.rendering.renderers.EntityRenderer;
import games.components.EntityComponent;

import java.util.ArrayList;

public class Entity
{
    private static int entitiesCreated = 0;

    protected String                     entityTypeName;
    private   EntityRenderer             renderer;
    private   Transformation             transformation;
    private   ArrayList<EntityComponent> components;
    private   int                        ID;
    private   boolean                    entityDeleted;
    private   boolean                    entityHidden;
    private   Game                       game;
    private   boolean                    containsTransparency;

    public Entity()
    {
        entityDeleted = false;
        entityTypeName = "Entity";
        renderer       = null;
        transformation = new Transformation();
        components     = new ArrayList<>();
        entitiesCreated++;
        ID = entitiesCreated - 1;
        game = null;
        containsTransparency = false;
    }

    public void input(float delta)
    {
        for(EntityComponent component : components)
            component.input(delta);
    }
    public void update(float delta)
    {
        for(EntityComponent component : components)
            component.update(delta);
    }

    public boolean hasRenderer() { return renderer != null; }

    public EntityRenderer getRenderer() { return renderer; }

    public void setRenderer(EntityRenderer renderer)
    {
        this.renderer = renderer;
    }

    public Transformation getTransformation()         { return transformation; }
    public String         getTypeName()               { return entityTypeName; }
    public ArrayList<EntityComponent> getComponents() { return components; }
    public int getID()                                { return ID; }

    public boolean getIsDeleted() { return entityDeleted; }
    public boolean getIsHidden()  { return entityHidden; }
    public Game getGame() { return game; }

    public void setGame(Game game)                                 { this.game = game; }
    public void setID(int ID)                                      { this.ID = ID; }
    public void   setTransformation(Transformation transformation) { this.transformation = transformation; }
    public Entity addComponent(EntityComponent component)
    {
        component.setParent(this);
        components.add(component);
        return this;
    }

    public boolean getContainsTransparency()
    {
        return containsTransparency;
    }

    public void free()
    {
        entityDeleted = true;
    }

    public void show()
    {
        entityHidden = false;
    }

    public void hide()
    {
        entityHidden = true;
    }

    public void setContainsTransparency(boolean containsTransparency)
    {
        this.containsTransparency = containsTransparency;
    }

    @Override
    public Entity clone()
    {
        Entity result = new Entity();
        result.setTransformation(getTransformation().clone());
        result.setRenderer(renderer);

        for(EntityComponent component : components)
        {
            result.addComponent(component.clone());
        }

        return result;
    }
}
