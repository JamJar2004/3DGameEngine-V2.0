package games.wing_ring;

import engine.core.math.Vector3f;
import games.components.EntityComponent;

public class BulletBehaviour extends EntityComponent
{
    public static final float SPEED = 100f;
    public static final float LIFE  = 2000f;

    private float distanceTravelled;
    private Vector3f direction;

    public BulletBehaviour(Vector3f direction)
    {
        distanceTravelled = 0;
        this.direction = direction;
    }

    @Override
    public void update(float delta)
    {
        distanceTravelled += SPEED * delta;
        getParent().getTransformation().move(direction, SPEED * delta);

        //getParent().getTransformation().move(getParent().getTransformation().getRotation().getForward(), SPEED * delta);

        if(distanceTravelled > LIFE)
        {
            getParent().free();
        }
    }
}
