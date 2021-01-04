package games;

import engine.audio.*;
import engine.core.*;
import engine.core.math.*;
import engine.rendering.*;
import games.components.*;
import games.entities.*;
import games.entities.lights.*;

public class TestGame extends Game
{
    private Mesh             land;
    private Mesh             mesh2;
    private DirectionalLight directionalLight;
    private PointLight       pointLight;

    private Sound       thunder;
    private SoundSource source2;

    @Override
    public void init()
    {
        //mesh = new Mesh(Mesh.Primitive.QUAD);

        setAmbientLight(0f, 0f, 0f);

        Sound sound = new Sound("Under Water Tube Cavern.wav");
        SoundSource source = new SoundSource();
        source.setPitch(1f);
        source.setLooping(true);
        source.setSound(sound);
        source.play();

        thunder = new Sound("thunder.wav");
        source2 = new SoundSource();
        source2.setSound(thunder);

        land = new Terrain(new HeightMap("heightMap.png"));

        land.getTransformation().setScale(40f);

        Texture grass    = new Texture("grassy2.png");
        Texture flowers  = new Texture("grassFlowers.png");
        Texture path     = new Texture("path.png");
        Texture mud      = new Texture("mud.png");
        Texture blendMap = new Texture("blendMap.png");

        Material material = new Material();
        material.setTexture("texture", grass);
        material.setTexture("redTexture", flowers);
        material.setTexture("greenTexture", mud);
        material.setTexture("blueTexture", path);
        material.setTexture("blendMap", blendMap);

        //material.setTexture("normalMap", new Texture("bricks2_normal.png"));
        //material.setFloat("reflectivity", 0.5f);
        //material.setFloat("damping", 3);
        material.setVector2f("tilingFactor", new Vector2f(100));
        material.setVector3f("color", new Vector3f(1, 1, 1));

        land.getTransformation().setPosition(0, -1, 5);
        land.setMaterial(material);

        addEntity(land);



        mesh2 = new Mesh("dragon.obj");
        Material material2 = new Material();
        material2.setVector3f("color", new Vector3f(0.25f, 0, 0.5f));
        material2.setFloat("reflectivity", 1f);
        material2.setFloat("damping", 64f);
        mesh2.setMaterial(material2);
        mesh2.getTransformation().setPosition(0, 1, 0);
        mesh2.getTransformation().setRotation(new Vector3f(0, 1, 0), 180f);
        mesh2.setCullType(Mesh.CullType.INSIDE);
        addEntity(mesh2);

        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 2f);
        directionalLight.getTransformation().turn(new Vector3f(1, 0, 0), -45f);
        directionalLight.getTransformation().turn(new Vector3f(0, 1, 0), 90f);
        directionalLight.setIntensity(0);

//        pointLight = new PointLight(new Vector3f(0, 1, 0), 1f, new Attenuation(0, 0, 0.1f));
//        pointLight.getTransformation().setPosition(10, -0.9f, 10);

        addEntity(directionalLight);
        //addEntity(pointLight);

        //pointLight.addComponent(new Follow(getMainCamera()));

        SpotLight torch = new SpotLight(new Vector3f(1, 1, 0.5f), 1f, new Attenuation(0, 0, 0.1f), 0.5f);
        torch.getTransformation().turn(new Vector3f(1, 0, 0), 90f);
        torch.addComponent(new Follow(getMainCamera()));
        addEntity(torch);

//        SkyBox skyBox = new SkyBox(new CubeMap("front.png", "back.png", "left.png", "right.png", "top.png", "bottom.png"));
//        skyBox.addComponent(new Follow(getMainCamera(), new Vector3f(0, 0, 0), false));
//        skyBox.getTransformation().setScale(100f);
//        addEntity(skyBox);

        Water water = new Water(new Texture("dudvMap.png"), new Texture("normalMap.png"));
        water.getMaterial().setFloat("reflectivity", 0.5f);
        water.getMaterial().setFloat("damping", 16);
        water.getTransformation().setScale(30, 1, 30);
        addEntity(water);

        FreeMove moveComponent = new FreeMove();
        moveComponent.addMovementControl(Input.KEY_W, new FreeMove.Movement(new Vector3f(0, 0, 1), 10f));
        moveComponent.addMovementControl(Input.KEY_S, new FreeMove.Movement(new Vector3f(0, 0, -1), 10f));

        FreeLook lookComponent = new FreeLook(Input.KEY_ESCAPE, 4f);

        getMainCamera().addComponent(moveComponent);
        getMainCamera().addComponent(lookComponent);

        Mesh tree = new Mesh("pine.obj");
        tree.getTransformation().setScale(0.1f);
        Material pineMat = new Material();
        pineMat.setTexture("texture", new Texture("pine.png"));
        tree.setMaterial(pineMat);
        tree.setCullType(Mesh.CullType.NONE);
        placeEntities(new EntityPlaceMap(tree, "placeMap.png", new HeightMap("heightmap.png")), land.getTransformation().getScale(), land.getTransformation().getPosition());

        Mesh fern = new Mesh("fern.obj");
        fern.getTransformation().setScale(0.1f);
        Material fernMat = new Material();
        fernMat.setTexture("texture", new Texture("fernTex.png"));
        fern.setMaterial(fernMat);
        fern.setCullType(Mesh.CullType.NONE);
        placeEntities(new EntityPlaceMap(fern, "placeMap2.png", new HeightMap("heightmap.png")), land.getTransformation().getScale(), land.getTransformation().getPosition());

        //turnOffHighQuality();
    }

    float    rand;
    float    time = 0;
    float    flashTime = 0;
    float    brightness = 0;
    Vector3f lightColor = new Vector3f(1, 1, 1);

    @Override
    public void gameLoop(float delta)
    {
        time += delta;

        rand = (float) Math.random();

        if (time > (Math.random() * 20 + 5))
        {
            if (rand < 0.5f)
            {
                float purpleDecision = (float)Math.random();

                if(purpleDecision > 0.5f)
                    lightColor = new Vector3f(0.65f, 0.55f, 0.75f);
                else
                    lightColor = new Vector3f(1, 1, 1);

                directionalLight.getTransformation().turn(new Vector3f(0, 1, 0), (float)Math.random() * 360f);
                flashTime = 0;
                brightness = 0.75f;
                source2.play();
            }
        }

        if(flashTime > 0.2f && brightness > 0)
        {
            brightness -= 1f * delta;
        }

        setClearColor(new Vector3f(brightness).multiply(lightColor));
        directionalLight.setIntensity(brightness);
        directionalLight.setColor(lightColor);

        if(source2.getIsPlaying())
        {
            flashTime += delta;
            time = 0;
        }

    }
}
