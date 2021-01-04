package games.wing_ring;

import engine.audio.Sound;
import engine.audio.SoundSource;
import engine.core.*;
import engine.core.math.*;
import games.components.*;
import games.entities.Camera;
import games.entities.Entity;
import games.entities.Sprite;

import java.util.ArrayList;
import java.util.HashMap;

public class WingPlayer extends Entity
{
    private static final int PARTICLE_COUNT      = 100;
    private static final float PARTICLE_LIFE     = 1f;
    private static final float PARTICLE_SPEED    = 100f;
    private static final float PARTICLE_GRAVITY  = 10f;
    private static final String EXPLOSION        = "explode2.wav";

    private Camera                    camera;
    private Entity                    parent;
    private HashMap<Integer, Bullet>  bullets;
    private HashMap<Integer, Bullet>  bulletsForGame;
    private ArrayList<Integer>        ufoBulletIDs;
    private ArrayList<WingRing.Ring>  rings;
    private int                       bulletsCreated;
    private Sprite                    particleSprite;
    private Sound                     explosionSound;
    private SoundSource               explosionSource;
    private WingRing.Ring             currRing;

    private Vector3f boundingMax;
    private Vector3f boundingMin;

    private boolean hasBeenKilled;

    private Bullet defaultBullet;

    public WingPlayer(Camera camera, Entity parent, Vector3f boundingMin, Vector3f boundingMax)
    {
        this.camera          = camera;
        this.parent          = parent;
        this.bullets         = new HashMap<>();
        this.bulletsForGame  = new HashMap<>();
        this.ufoBulletIDs    = new ArrayList<>();
        this.rings           = new ArrayList<>();
        this.bulletsCreated  = 0;
        this.defaultBullet   = new Bullet(new Vector3f(), new Vector3f(), "big");
        this.boundingMin     = boundingMin;
        this.boundingMax     = boundingMax;
        this.particleSprite  = new Sprite("wing_ring/big_spark.bmp");
        this.explosionSound  = new Sound("wing_ring/" + EXPLOSION);
        this.explosionSource = new SoundSource();
        this.hasBeenKilled   = false;
        explosionSource.setVolume(10f);
        explosionSource.setSound(explosionSound);
        FreeMove freeMove    = new FreeMove();
        freeMove.addMovementControl(Input.KEY_A, new FreeMove.Movement(Vector3f.FORWARD, 60f));
        freeMove.addMovementControl(Input.KEY_UP, new FreeMove.Movement(Vector3f.UP, 20f));
        freeMove.addMovementControl(Input.KEY_DOWN, new FreeMove.Movement(Vector3f.DOWN, 20f));
        addComponent(freeMove);
    }

    @Override
    public void input(float delta)
    {
        super.input(delta);

        if(Input.getKey(Input.KEY_LEFT))
        {
            getTransformation().turn(Vector3f.UP, -170f * delta);
            defaultBullet.getTransformation().turn(Vector3f.UP, -170f * delta);
        }
        else if(Input.getKey(Input.KEY_RIGHT))
        {
            getTransformation().turn(Vector3f.UP, 170f * delta);
            defaultBullet.getTransformation().turn(Vector3f.UP, 170f * delta);
        }

        if(Input.getKeyDown(Input.KEY_S))
        {
            Bullet bullet = new Bullet(getTransformation().getPosition(), defaultBullet.getTransformation().getRotation().getFront(), "big");
            bullet.getTransformation().setRotation(defaultBullet.getTransformation().getRotation());
            bullet.getTransformation().move(0, 0.2f, 0);

            bulletsCreated++;
            bullets.put(bulletsCreated - 1, bullet);
            bulletsForGame.put(bulletsCreated - 1, bullet);
        }
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        parent.setTransformation(getTransformation().clone());
        parent.getTransformation().turn(Vector3f.UP, 90f);
        parent.getTransformation().update();
        camera.getTransformation().setParent(getTransformation().clone());
        camera.getTransformation().setPosition(0, 0, -40);
        //camera.getTransformation().turn(Vector3f.UP, -90f);
        //camera.setTransformation(getTransformation());

        for(WingRing.Ring ring : rings)
        {
            currRing = null;
            if(isWithinRing(ring))
            {
                currRing = ring;
            }
        }

        if(hasBeenHit())
        {
            createExplosion();
            parent.free();
            free();
            explosionSource.setPosition(getTransformation().getPosition());
            explosionSource.play();
            hasBeenKilled = true;

            getGame().exit(2);
        }
    }

    private boolean isWithinRing(WingRing.Ring ring)
    {
        return isWithinRing(ring.getCenterPosition(), ring.getRadius());
    }

    private boolean isWithinRing(Vector3f centerPosition, float radius)
    {
        return FromCenterVectorToPlayer(centerPosition.getXZ()).length() < radius;
    }

    private Vector2f FromCenterVectorToPlayer(Vector2f centerPosition)
    {
        return getTransformation().getPosition().getXZ().subtract(centerPosition);
    }

    private void createExplosion()
    {
        for(int i = 0; i < PARTICLE_COUNT; i++)
        {
            Sprite particle = particleSprite.clone();//new Mesh(Mesh.Primitive.CUBE);
            particle.addComponent(new LookAt(getCamera()));
            particle.getTransformation().setPosition(getTransformation().getPosition());

            float x = (float)(Math.random() * 2 - 1);
            float y = (float)(Math.random() * 2 - 1);
            float z = (float)(Math.random() * 2 - 1);

            particle.addComponent(new ParticleEffect(new Vector3f(x, y, z), PARTICLE_SPEED, PARTICLE_GRAVITY, PARTICLE_LIFE));
            getGame().addEntity(particle);
        }
    }

    private boolean hasBeenHit()
    {
        for(Integer bulletID : ufoBulletIDs)
        {
            Vector3f bulletPosition;
            if(getGame().getEntity(bulletID) != null)
                 bulletPosition = getGame().getEntity(bulletID).getTransformation().getPosition();
            else
                return false;

            if(isIntersecting(bulletPosition))
            {
                ufoBulletIDs.remove(bulletID);
                getGame().getEntity(bulletID).free();
                return true;
            }
        }
        return false;
    }

    private boolean isIntersecting(Vector3f position)
    {
        Vector3f center   = getTransformation().getPosition();
        Vector3f distance = position.subtract(center);
        return distance.abs().getX() < boundingMax.getX() && distance.abs().getY() < boundingMax.getY() && distance.abs().getZ() < boundingMax.getZ() &&
               distance.abs().getX() > boundingMin.getX() && distance.abs().getY() > boundingMin.getY() && distance.abs().getZ() > boundingMin.getZ();
    }

    public WingRing.Ring getCurrentRing()
    {
        return currRing;
    }

    public HashMap<Integer, Bullet> getBullets()
    {
        return bullets;
    }

    public HashMap<Integer, Bullet> getBulletsForGame()
    {
        return bulletsForGame;
    }

    public boolean getHasBeenKilled()
    {
        return hasBeenKilled;
    }

    public void addBullet(int bulletID)
    {
        ufoBulletIDs.add(bulletID);
    }

    public void addRing(WingRing.Ring ring)
    {
        rings.add(ring);
    }
}
