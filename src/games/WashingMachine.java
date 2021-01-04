package games;

import engine.core.Game;
import engine.core.Input;
import engine.core.math.*;
import engine.rendering.*;
import games.components.*;
import games.entities.*;
import games.entities.lights.*;

public class WashingMachine extends Game
{
    @Override
    public void init()
    {
        float width  = 5.95f;
        float height = 8.1f;
        float depth  = 4.2f;

        Mesh base = new Mesh(Mesh.Primitive.QUAD);
        base.setCullType(Mesh.CullType.NONE);
        base.getTransformation().setScale(width, 1, depth);
        Material baseMaterial = new Material();
        baseMaterial.setVector3f("color", new Vector3f(1, 1, 0));
        base.setMaterial(baseMaterial);

        Mesh rightSide = new Mesh(Mesh.Primitive.QUAD);
        rightSide.getMaterial().setTexture("normalMap", new Texture("sidePanel.png", Texture.FlipType.VERTICAL));
        rightSide.setCullType(Mesh.CullType.NONE);
        rightSide.getTransformation().turn(new Vector3f(0, 0, 1), 90f);
        rightSide.getTransformation().setScale(height, 1, depth);
        rightSide.getTransformation().setPosition(width, height, 0);

        Mesh leftSide = new Mesh(Mesh.Primitive.QUAD);
        leftSide.getMaterial().setTexture("normalMap", new Texture("sidePanel.png"));
        leftSide.setCullType(Mesh.CullType.NONE);
        leftSide.getTransformation().turn(new Vector3f(0, 0, 1), 90f);
        leftSide.getTransformation().turn(new Vector3f(0, 1, 0), 180f);
        leftSide.getTransformation().setScale(height, 1, depth);
        leftSide.getTransformation().setPosition(-width, height, 0);

        Mesh back = new Mesh(Mesh.Primitive.QUAD);
        back.setCullType(Mesh.CullType.NONE);
        back.getTransformation().turn(new Vector3f(0, 0, 1), 90f);
        back.getTransformation().turn(new Vector3f(0, 1, 0), 270f);
        back.getTransformation().setScale(height, 1, width);
        back.getTransformation().setPosition(0, height, depth);

        Mesh front = new Mesh("frontcorrect.obj");
        front.getMaterial().setFloat("reflectivity", 1f);
        front.getMaterial().setFloat("damping", 32f);
        front.getTransformation().turn(new Vector3f(0, 1, 0), 180f);
        front.getTransformation().setScale(1.925f);
        front.getTransformation().setPosition(0, 1.95f, -depth - 0.75f);
        front.setCullType(Mesh.CullType.NONE);

        Mesh plinth = new Mesh("plinth.obj");
        plinth.getTransformation().turn(new Vector3f(0, 1, 0), 180f);
        plinth.getTransformation().setPosition(0, 0, -depth - 0.75f);
        plinth.getTransformation().setScale(1.925f);
        plinth.setCullType(Mesh.CullType.NONE);

//        PointLight bulb = new PointLight(new Vector3f(1, 1, 1), 1000f, new Attenuation(0, 100f, 0.0001f));
//        bulb.getTransformation().setPosition(0, height * 2, -20f);
//        addEntity(bulb);

        DirectionalLight light = new DirectionalLight(new Vector3f(1, 1, 1), 0.7f);
        light.getTransformation().turn(new Vector3f(0, 1, 0), -45f);
        //light.getTransformation().turn(new Vector3f(1, 0, 0), -45f);
        addEntity(light);

        Mesh doorFrame = new Mesh("door frame.obj");
        doorFrame.getMaterial().setFloat("reflectivity", 1f);
        doorFrame.getMaterial().setFloat("damping", 32f);
        //doorFrame.getMaterial().setBoolean("ignoreVertexColor", true);
        doorFrame.getTransformation().turn(new Vector3f(0, 1, 0), 180f);
        doorFrame.getTransformation().setScale(1.925f);
        doorFrame.getTransformation().setPosition(0, 1.95f, -depth - 0.75f);

        Mesh controlPanel = new Mesh("control panel without drawer.obj");
        controlPanel.getMaterial().setFloat("reflectivity", 1f);
        controlPanel.getMaterial().setFloat("damping", 32f);
        controlPanel.getTransformation().turn(new Vector3f(0, 1, 0), 180f);
        controlPanel.getTransformation().setScale(1.925f);
        controlPanel.getTransformation().setPosition(1.175f, 13.7f, depth + 2.5f);

        Mesh drum = new Mesh("drum.obj");
        drum.setCullType(Mesh.CullType.NONE);
        drum.getMaterial().setFloat("reflectivity", 0.5f);
        drum.getMaterial().setFloat("damping", 100f);
        drum.getTransformation().turn(new Vector3f(0, 1, 0), 180f);
        drum.getTransformation().setScale(1.925f);
        drum.getTransformation().setPosition(0, 9.7f, 0.5f);

        Water water = new Water(new Texture("dudvMap.png"), new Texture("normalMap.png"));
        addEntity(water);

        water.getTransformation().setPosition(0, drum.getTransformation().getPosition().getY() - 3, -1);
        water.getTransformation().setScale(4f, 0, 3.5f);
        water.getMaterial().setFloat("waveStrength", 10f);
        water.getMaterial().setFloat("tilingFactor", 1f);

        float rpm = 60;
        drum.addComponent(new Rotater(new Vector3f(0, 0, 1), rpm * 6));

        addEntity(base);
        addEntity(rightSide);
        addEntity(leftSide);
        addEntity(back);
        addEntity(front);
        addEntity(plinth);
        addEntity(doorFrame);
        addEntity(controlPanel);
        addEntity(drum);

        //addEntity(bulb);

        FreeMove moveComponent = new FreeMove();
        moveComponent.addMovementControl(Input.KEY_W, new FreeMove.Movement(new Vector3f(0, 0, 1), 10f));
        moveComponent.addMovementControl(Input.KEY_S, new FreeMove.Movement(new Vector3f(0, 0, -1), 10f));

        FreeLook lookComponent = new FreeLook(Input.KEY_ESCAPE, 4f);

        getMainCamera().addComponent(moveComponent);
        getMainCamera().addComponent(lookComponent);

    }

    @Override
    public void gameLoop(float delta)
    {

    }
}
