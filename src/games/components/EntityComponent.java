package games.components;

import games.entities.Entity;

public class EntityComponent
{
    private Entity parent;

    public void input(float delta)  {}
    public void update(float delta) {}

    public Entity getParent() { return parent; }

    public void setParent(Entity parent) { this.parent = parent; }

    public EntityComponent clone()
    {
        EntityComponent result = new EntityComponent();
        return result;
    }
}
