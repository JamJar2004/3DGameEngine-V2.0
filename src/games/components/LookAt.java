package games.components;

import engine.core.math.Quaternion;
import engine.core.math.Vector3f;
import games.entities.Entity;

public class LookAt extends EntityComponent
{
    private static final float TURN_SPEED = 10f;

    private Entity     follower;
    private Quaternion rotationOffset;

    public LookAt(Entity follower)
    {
        this(follower, new Quaternion());
    }

    public LookAt(Entity follower, Quaternion rotationOffset)
    {
        this.follower       = follower;
        this.rotationOffset = rotationOffset;
    }

    @Override
    public void update(float delta)
    {
        Quaternion newRotation = getParent().getTransformation().getLookAtRotation(follower.getTransformation().getTransformedPosition(), Vector3f.UP);
        //getParent().getTransformation().setRotation(getParent().getTransformation().getRotation().slerp(newRotation, delta * 5.0f, true));
        getParent().getTransformation().lookAt(follower.getTransformation().getTransformedPosition(), newRotation.getUp(), rotationOffset);
    }

}
