package games.components;

import engine.core.math.Vector3f;

public class ParticleEffect extends EntityComponent
{
    private float    gravity;
    private float    speed;
    private Vector3f direction;
    private float    lifeTime;

    private float    time;
    private Vector3f size;

    public ParticleEffect(Vector3f direction, float speed, float gravity, float lifeTime)
    {
        this.direction = direction;
        this.speed     = speed;
        this.gravity   = gravity;
        this.lifeTime  = lifeTime;
        this.time      = 0;
        this.size      = new Vector3f(1, 1, 1);
    }

    @Override
    public void update(float delta)
    {
        time += delta;
        getParent().getTransformation().move(direction, speed * delta);
        getParent().getTransformation().move(Vector3f.DOWN, gravity * delta);

//        size.setX(size.getX() - (delta * 0.001f));
//        size.setY(size.getY() - (delta * 0.001f));
//        size.setZ(size.getZ() - (delta * 0.001f));

        getParent().getTransformation().setScale(size.multiply((lifeTime - time) / lifeTime));

        if(time > lifeTime)
        {
            getParent().free();
        }
    }
}
