package games.wing_ring;

import engine.audio.*;
import engine.core.*;
import engine.core.math.*;
import engine.rendering.*;
import games.components.*;
import games.entities.*;
import games.entities.lights.*;

public class WingRing extends Game
{
    private static final int UFO_COUNT = 50;

    private Sound       music;
    private SoundSource source;

    private WingPlayer player;
    private Mesh       sand;

    @Override
    public void init()
    {
        sand = new Mesh(Mesh.Primitive.ELLIPSE);
        sand.getMaterial().setTexture("texture", new Texture("wing_ring/sand.bmp"));
        sand.getMaterial().setVector2f("tilingFactor", new Vector2f(10000f));
        sand.getMaterial().setVector3f("color", new Vector3f(0.66f, 0.52f, 0.22f));
        sand.getTransformation().setScale(10000f);
        sand.getTransformation().setPosition(0, -20f, 0);
        Follow follow = new Follow(getMainCamera(), new Vector3f(0, -20, 0));
        follow.setFollowRotation(false);
        follow.setFollowY(false);
        sand.addComponent(follow);
        addEntity(sand);

        Sprite rings = new Sprite("wing_ring/ring.bmp");
        rings.getTransformation().turn(Vector3f.RIGHT, 90f);
        rings.getTransformation().setPosition(0, sand.getTransformation().getPosition().getY() + 0.1f, 0);
        rings.getTransformation().setScale(1000f);
        addEntity(rings);

        Mesh sphinx = new Mesh("wing_ring/sphinx.x");
        //sphinx.getMaterial().setFloat("reflectivity", 0.5f);
        //sphinx.getMaterial().setFloat("damping", 3f);
        sphinx.getTransformation().turn(Vector3f.UP, 180f);
        sphinx.getTransformation().setScale(0.1f);
        sphinx.getTransformation().move(0, -sphinx.getMaxPosition().getY() * sphinx.getTransformation().getScale().getY() - 0.9f, 0);
        addEntity(sphinx);

        Mesh plane = new Mesh("wing_ring/plane.obj");
        plane.getMaterial().setTexture("texture", new Texture("wing_ring/F15.BMP"));
        addEntity(plane);

        Mesh circle = new Mesh(Mesh.Primitive.ELLIPSE);
        circle.getMaterial().setVector3f("color", new Vector3f(1, 0, 0));
        circle.getTransformation().setScale(150);
        circle.getTransformation().setPosition(new Vector3f(515, sand.getTransformation().getPosition().getY() + 1, -150));
        addEntity(circle);

        player = new WingPlayer(getMainCamera(), plane, plane.getMinPosition().multiply(0.3f), plane.getMaxPosition().multiply(0.3f));
        player.getTransformation().setScale(0.3f);
        player.getTransformation().setPosition(0, 5f, 0);
        addEntity(player);

        player.addRing(new Ring(new Vector3f(507, 0, 176), 240, Ufo.UfoType.YELLOW));
        player.addRing(new Ring(new Vector3f(0, 0, 550), 275, Ufo.UfoType.ORANGE));
        player.addRing(new Ring(new Vector3f(-515, 0, 176), 210, Ufo.UfoType.GREEN));

        Ufo.setMaxRange(UFO_COUNT * 2);
        Ufo.setFloorHeight(sand.getTransformation().getPosition().getY() + 10f);

        for(int i = 0; i < UFO_COUNT; i++)
        {
            int rand = (int)(Math.random() * 5);
            Ufo ufo;

            //ufo = new Ufo(Ufo.UfoType.values()[rand], player);
            ufo = new Ufo(Ufo.UfoType.GREEN, player);
            Quaternion offset = new Quaternion();
            offset.turn(Vector3f.UP, 180);
            offset.turn(Vector3f.RIGHT, 180);
            offset.turn(Vector3f.FORWARD, 180);

            float x = (float)Math.random() * 100;
            float y = -(float)Math.random() * 10;
            float z = (float)Math.random() * 100;

            float randAngle = (float)Math.random() * 360f;

            ufo.getTransformation().setPosition(x - 50, y, z - 50);
            ufo.getTransformation().turn(Vector3f.UP, randAngle);
            addEntity(ufo);
        }

        Mesh mother = new Mesh("wing_ring/mother.x");
        mother.getTransformation().setPosition(20, 0, 0);
        mother.getTransformation().setScale(0.01f);
        //addEntity(mother);

        Mesh sky = new Mesh(Mesh.Primitive.SPHERE);
        sky.setCullType(Mesh.CullType.OUTSIDE);
        sky.getTransformation().setScale(10000f);
        sky.getTransformation().turn(new Vector3f(1, 0, 0), 90f);
        sky.getMaterial().setBoolean("ignoreLighting", true);
        sky.getMaterial().setTexture("texture", new Texture("wing_ring/clouds.bmp"));
        sky.getMaterial().setVector2f("tilingFactor", new Vector2f(2f, 1f));
        sky.addComponent(follow);
        addEntity(sky);

        DirectionalLight light = new DirectionalLight(new Vector3f(1, 1, 1), 1f);
        light.getTransformation().turn(new Vector3f(1, 0, 0), -45);
        addEntity(light);

        FreeMove moveComponent = new FreeMove();
        moveComponent.addMovementControl(Input.KEY_W, new FreeMove.Movement(new Vector3f(0, 0, 1), 20f));
        moveComponent.addMovementControl(Input.KEY_S, new FreeMove.Movement(new Vector3f(0, 0, -1), 20f));

//        FreeLook lookComponent = new FreeLook(Input.KEY_ESCAPE, 4f);
//        getMainCamera().addComponent(moveComponent).addComponent(lookComponent);
        getMainCamera().setPerspectiveProjection(70.0f, Window.getAspectRatio(), 1f, 100000f);

        setAmbientLight(0.5f, 0.5f, 0.5f);

        music = new Sound("wing_ring/tune2.mid");
        source = new SoundSource();
        source.setPitch(1f);
        source.setLooping(true);
        source.setSound(music);
        source.play();
    }

    @Override
    public void gameLoop(float delta)
    {
//        if(!source.getIsPlaying())
//            source.play();

//        if(player.getHasBeenKilled())
//        {
//            source.stop();
//        }

        setListenerPosition(getMainCamera().getTransformation().getTransformedPosition());

        if(player.getTransformation().getPosition().getY() < sand.getTransformation().getPosition().getY() + 1)
        {
            player.getTransformation().getPosition().setY(sand.getTransformation().getPosition().getY() + 1);
        }

        for(Integer bulletIndex : player.getBulletsForGame().keySet())
        {
            if(player.getBullets().get(bulletIndex) != null)
                addEntity(player.getBullets().get(bulletIndex));
        }
        player.getBulletsForGame().clear();
    }

    public class Ring
    {
        private Vector3f    centerPosition;
        private float       radius;
        private Ufo.UfoType ringColor;

        public Ring(Vector3f centerPosition, float radius, Ufo.UfoType ringColor)
        {
            this.centerPosition = centerPosition;
            this.radius         = radius;
            this.ringColor      = ringColor;
        }

        public Vector3f    getCenterPosition() { return centerPosition; }
        public float       getRadius() { return radius; }
        public Ufo.UfoType getRingColor() { return ringColor; }

        public void setCenterPosition(Vector3f centerPosition) { this.centerPosition = centerPosition; }
        public void setRadius(float radius) { this.radius = radius; }
        public void setRingColor(Ufo.UfoType ringColor) { this.ringColor = ringColor; }
    }
}
