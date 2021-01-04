package games.wing_ring;

import engine.audio.Sound;
import engine.audio.SoundSource;
import engine.core.math.Quaternion;
import engine.core.math.Vector3f;
import games.components.EntityComponent;
import games.components.LookAt;
import games.components.ParticleEffect;
import games.components.Rotater;
import games.entities.Mesh;
import games.entities.Sprite;

import java.util.ArrayList;
import java.util.HashMap;

public class UfoBehaviour extends EntityComponent
{
    private static final float SPEED             = 10f;
    private static final float TURN_SPEED        = 90f;
    private static final Vector3f CENTER         = new Vector3f();
    private static final float INSIDE_RANGE      = 0.9f;
    private static final String EXPLOSION        = "explode2.wav";
    private static final int   PARTICLE_COUNT    = 100;
    private static final float PARTICLE_LIFE     = 1f;
    private static final float PARTICLE_SPEED    = 100f;
    private static final float PARTICLE_GRAVITY  = 10f;

    private float maxRange;

    private Vector3f    direction;
    private double      timeCounter;
    private double      shootTime;
    private float       turnDirection;
    private float       moveDirection;
    private float       floorHeight;
    private Vector3f    boundingMin;
    private Vector3f    boundingMax;
    private Sound       explosionSound;
    private SoundSource explosionSource;
    private Sprite      particleSprite;
    private Ufo.UfoType ufoType;

    private String sparkName;

    private Vector3f destination;
    private int      counter;

    private Bullet defaultBullet;

    private WingPlayer player;

    public UfoBehaviour(WingPlayer player, Vector3f boundingMin, Vector3f boundingMax, String particleFileName, Ufo.UfoType ufoType)
    {
        this.sparkName        = particleFileName;
        this.timeCounter      = 0;
        this.maxRange         = 50f;
        this.floorHeight      = 0f;
        this.turnDirection    = 1;
        this.direction        = new Vector3f(0, 0, 1);
        this.player           = player;
        this.boundingMin      = boundingMin;
        this.boundingMax      = boundingMax;
        this.explosionSound   = new Sound("wing_ring/" + EXPLOSION);
        this.explosionSource  = new SoundSource();
        this.particleSprite   = new Sprite("wing_ring/" + particleFileName + "_spark.bmp");
        this.defaultBullet    = new Bullet(new Vector3f(), new Vector3f(), particleFileName);
        this.ufoType          = ufoType;

        explosionSource.setVolume(10f);
        explosionSource.setSound(explosionSound);

        destination = new Vector3f();
        counter     = 180;
    }

    @Override
    public void update(float delta)
    {
        Vector3f ufoPosition = getParent().getTransformation().getPosition();

        counter += 1;
        //TODO: Delta calculation
        getParent().getTransformation().setPosition(ufoPosition.add(destination.subtract(ufoPosition).divide(100)));

        lookAtPlayer(delta);

        defaultBullet.getTransformation().setRotation(player.getTransformation().getRotation());//getParent().getTransformation().getRotation());

        if((int)(Math.random() * 900) == 450)
            shoot(getParent().getTransformation().getRotation().getBack());

        if(counter > 180)
        {
            reset();
        }

        if(hasBeenHit())
        {
            createExplosion();

            if(player.getCurrentRing() != null)
            {
                if (player.getCurrentRing().getRingColor() == ufoType)
                {
                    createCamel();
                }
            }

            getParent().hide();
            getParent().free();
            explosionSource.setPosition(ufoPosition);
            explosionSource.play();
        }
    }

    private void createCamel()
    {
        Vector3f color;

        switch(ufoType)
        {
            case ORANGE:
                color = new Vector3f(1f, 0.5f, 0f);
                break;
            case YELLOW:
                color = new Vector3f(1f, 1f, 0f);
                break;
            case GREEN:
                color = new Vector3f(0f, 1f, 0f);
                break;
            case CYAN:
                color = new Vector3f(0f, 1f, 1f);
                break;
            case PINK:
                color = new Vector3f(1f, 0f, 1f);
                break;
            default:
                color = new Vector3f(1f, 0f, 0f);
        }

        Mesh camel = new Mesh("wing_ring/camel.x");
        camel.getMaterial().setBoolean("ignoreVertexColor", true);
        camel.getMaterial().setVector3f("color", color);
        camel.setTransformation(getParent().getTransformation());
        camel.addComponent(new Rotater(Vector3f.UP, -100f));
        getParent().getGame().addEntity(camel);
    }

    private void lookAtPlayer(float delta)
    {
        Quaternion newRotation = getParent().getTransformation().getLookAtRotation(player.getTransformation().getTransformedPosition(), Vector3f.UP);
        newRotation = newRotation.multiply(new Quaternion(Vector3f.UP, 180f));
        float yaw = newRotation.toEuler().getY();
        newRotation = new Quaternion(Vector3f.UP, yaw);
        getParent().getTransformation().setRotation(getParent().getTransformation().getRotation().slerp(newRotation, delta * 5.0f, true));
    }

    private void reset()
    {
        Vector3f playerPosition = player.getTransformation().getPosition();

        counter = 0;

        destination.setX(playerPosition.getX() + (float)Math.random() * maxRange - (maxRange / 2));
        destination.setZ(playerPosition.getZ() + (float)Math.random() * maxRange - (maxRange / 2));

        if(playerPosition.getY() > 15)
        {
            destination.setY(playerPosition.getY() + (float)Math.random() * 20 - 10);
        }
        else
        {
            destination.setY(playerPosition.getY() + (float)Math.random() * 10 + 5);
        }
    }

    private void shoot(Vector3f direction)
    {
        Bullet bullet = new Bullet(getParent().getTransformation().getPosition(), direction, sparkName);
        bullet.getTransformation().setRotation(defaultBullet.getTransformation().getRotation());
        bullet.getTransformation().turn(Vector3f.UP, 180f);
        bullet.getTransformation().move(0, -0.5f, 0);
        bullet.getTransformation().move(direction, 3f);
        getParent().getGame().addEntity(bullet);
        player.addBullet(bullet.getID());
    }

//    @Override
//    public void update(float delta)
//    {
//        timeCounter += delta;
//        shootTime   += delta;
//
//        Vector3f ufoPosition = getParent().getTransformation().getPosition();
//
//        if(timeCounter > 0.5f + Math.random() * 2)
//        {
//            timeCounter = 0;
//            turnDirection = (int)(Math.random() * 2) - 0.5f;
//            moveDirection = (int)(Math.random() * 2) - 0.5f;
//        }
//
////        if(!isInRange(ufoPosition) && dotToCenter() < INSIDE_RANGE)
////        {
////            float turnAmount = TURN_SPEED * delta;
////            getParent().getTransformation().turn(Vector3f.UP, turnAmount);
////            float rightDot = dotToCenter();
////            getParent().getTransformation().turn(Vector3f.UP, -turnAmount * 2);
////            float leftDot = dotToCenter();
////
////            if(leftDot < rightDot)
////            {
////                getParent().getTransformation().turn(Vector3f.UP, turnAmount * 2);
////            }
////
////            direction = new Vector3f();
////        }
////        else
////        {
////            getParent().getTransformation().turn(Vector3f.UP, TURN_SPEED * delta * turnDirection * 0.5f);
////            direction = getParent().getTransformation().getRotation().getBack();
////        }
//
//        if(dotToPlayer() < 0.99f)
//        {
//            float turnAmount = TURN_SPEED * delta;
//            getParent().getTransformation().turn(Vector3f.UP, turnAmount);
//            float rightDot = dotToPlayer();
//            getParent().getTransformation().turn(Vector3f.UP, -turnAmount * 2);
//            float leftDot = dotToPlayer();
//
//            if(leftDot < rightDot)
//            {
//                getParent().getTransformation().turn(Vector3f.UP, turnAmount * 2);
//            }
//
//            direction = new Vector3f();
//        }
//        else
//        {
////            if(shootTime > 5)
////            {
////                shootTime = 0;
////                shoot();
////            }
//        }
////        else
////        {
////            getParent().getTransformation().turn(Vector3f.UP, TURN_SPEED * delta * turnDirection * 0.5f);
////            direction = getParent().getTransformation().getRotation().getBack();
////        }
//
//        defaultBullet.getTransformation().setRotation(getParent().getTransformation().getRotation());
//
//        direction.setY(moveDirection * SPEED * delta);
//
//        if(ufoPosition.getY() + direction.getY() < floorHeight)
//            direction.setY(0);
//
//        if(ufoPosition.getY() < player.getTransformation().getPosition().getY() - 0.5f + 1f)
//            getParent().getTransformation().move(0, SPEED * delta * 0.1f, 0);
//        else if(ufoPosition.getY() > player.getTransformation().getPosition().getY() + 0.5f + 1f)
//            getParent().getTransformation().move(0, -SPEED * delta * 0.1f, 0);
//
//        direction = getParent().getTransformation().getRotation().getBack();
//
//        getParent().getTransformation().move(direction, SPEED * delta);
//
//        if(hasBeenHit())
//        {
//            createExplosion();
//            getParent().hide();
//            getParent().free();
//            explosionSource.setPosition(ufoPosition);
//            explosionSource.play();
//        }
//    }


    private void createExplosion()
    {
        for(int i = 0; i < PARTICLE_COUNT; i++)
        {
            Sprite particle = particleSprite.clone();//new Mesh(Mesh.Primitive.CUBE);
            particle.addComponent(new LookAt(player.getCamera()));
            particle.getTransformation().setPosition(getParent().getTransformation().getPosition());

            float x = (float)(Math.random() * 2 - 1);
            float y = (float)(Math.random() * 2 - 1);
            float z = (float)(Math.random() * 2 - 1);

            particle.addComponent(new ParticleEffect(new Vector3f(x, y, z), PARTICLE_SPEED, PARTICLE_GRAVITY, PARTICLE_LIFE));
            getParent().getGame().addEntity(particle);
        }
    }

    private boolean hasBeenHit()
    {
        for(Integer bulletIndex : player.getBullets().keySet())
        {
            Vector3f bulletPosition = player.getBullets().get(bulletIndex).getTransformation().getPosition();
            if(isIntersecting(bulletPosition))
            {
                player.getBullets().get(bulletIndex).free();
                player.getBullets().remove(bulletIndex);
                return true;
            }
        }
        return false;
    }

    private boolean isIntersecting(Vector3f position)
    {
        Vector3f center    = getParent().getTransformation().getPosition();
        Vector3f distance  = position.subtract(center);
        return distance.abs().getX() < boundingMax.getX() && distance.abs().getY() < boundingMax.getY() && distance.abs().getZ() < boundingMax.getZ() &&
               distance.abs().getX() > boundingMin.getX() && distance.abs().getY() > boundingMin.getY() && distance.abs().getZ() > boundingMin.getZ();
    }

//    private boolean isSeenByPlayer()
//    {
//        Vector3f playerLookDirection = player.getTransformation().getRotation().getForward();
//        Vector3f toPlayerVector      = getParent().getTransformation().getPosition().subtract(player.getTransformation().getPosition());
//        float dot = playerLookDirection.dot(toPlayerVector.normalize());
//        return dot > 0.99f;
//    }

    private boolean isInRange(Vector3f position)
    {
        return toUfoVector(position).length() < maxRange;
    }

    private float dotToPlayer()
    {
        Vector3f ufoPosition = getParent().getTransformation().getPosition();
        Vector3f forward = getParent().getTransformation().getRotation().getBack();
        float dot = forward.dot(fromPlayerVector(ufoPosition).invert().normalize());
        return dot;
    }

    private float dotToCenter()
    {
        Vector3f ufoPosition = getParent().getTransformation().getPosition();
        Vector3f forward = getParent().getTransformation().getRotation().getBack();
        float dot = forward.dot(toUfoVector(ufoPosition).invert().normalize());
        return dot;
    }

    private Vector3f fromPlayerVector(Vector3f position)
    {
        return position.subtract(player.getTransformation().getPosition());
    }

    private Vector3f toUfoVector(Vector3f position)
    {
        return position.subtract(CENTER);
    }

    public float getMaxRange()
    {
        return maxRange;
    }

    public float getFloorHeight()
    {
        return floorHeight;
    }

    public Vector3f getBoundingMin()
    {
        return boundingMin;
    }

    public Vector3f getBoundingMax()
    {
        return boundingMax;
    }

    public void setMaxRange(float maxRange)
    {
        this.maxRange = maxRange;
    }

    public void setFloorHeight(float floorHeight)
    {
        this.floorHeight = floorHeight;
    }

    public void setBoundingMin(Vector3f boundingMin)
    {
        this.boundingMin = boundingMin;
    }

    public void setBoundingMax(Vector3f boundingMax)
    {
        this.boundingMax = boundingMax;
    }
}
