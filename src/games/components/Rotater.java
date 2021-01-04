package games.components;

import engine.core.math.Vector3f;

public class Rotater extends EntityComponent
{
    private Vector3f axis;
    private float    speed;

    public Rotater(Vector3f axis, float speed)
    {
        this.axis  = axis;
        this.speed = speed;
    }

    @Override
    public void update(float delta)
    {
        getParent().getTransformation().turn(axis, speed * delta);
    }

    public Vector3f getAxis()  { return axis;  }
    public float    getSpeed() { return speed; }

    public void setAxis(Vector3f axis) { this.axis = axis;   }
    public void setSpeed(float speed)  { this.speed = speed; }

    public Rotater clone()
    {
        Rotater result = new Rotater(axis, speed);
        return result;
    }
}
