package games;

import engine.core.*;
import engine.core.math.*;
import engine.rendering.*;
import games.components.*;
import games.entities.*;
import games.entities.lights.*;

public class TutorialGame extends Game
{
    @Override
    public void init()
    {
        Mesh mesh = new Mesh(Mesh.Primitive.SPHERE);
        mesh.getTransformation().setPosition(0, 0, 5);
        mesh.getMaterial().setVector3f("color", new Vector3f(1, 1, 0));
        mesh.getMaterial().setFloat("reflectivity", 1f);
        mesh.getMaterial().setFloat("damping", 8f);
        mesh.getMaterial().setVector2f("tilingFactor", new Vector2f(10, 10));
        mesh.getMaterial().setTexture("texture", new Texture("grassy2.png"));
        addEntity(mesh);

        getMainCamera().addComponent(new FreeLook(Input.KEY_ESCAPE, 5.0f));

        FreeMove freeMove = new FreeMove();
        freeMove.addMovementControl(Input.KEY_W, new FreeMove.Movement(new Vector3f( 0, 0,  1), 10f));
        freeMove.addMovementControl(Input.KEY_S, new FreeMove.Movement(new Vector3f( 0, 0, -1), 10f));
        freeMove.addMovementControl(Input.KEY_A, new FreeMove.Movement(new Vector3f(-1, 0,  0), 10f));
        freeMove.addMovementControl(Input.KEY_D, new FreeMove.Movement(new Vector3f( 1, 0,  0), 10f));
        getMainCamera().addComponent(freeMove);

        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.8f);
        directionalLight.getTransformation().turn(new Vector3f(1, 0, 0), 225f);

        DirectionalLight directionalLight2 = new DirectionalLight(new Vector3f(0, 0, 1), 0.8f);
        directionalLight2.getTransformation().turn(new Vector3f(1, 0, 0), 135f);
        addEntity(directionalLight);
        //addEntity(directionalLight2);

        Water water = new Water(new Texture("dudv.png"), new Texture("normalMap.png"));
        water.getTransformation().setPosition(0, -1, 5);
        water.getTransformation().setScale(10f);
        water.getMaterial().setFloat("reflectivity", 1f);
        water.getMaterial().setFloat("damping", 8f);
        addEntity(water);
    }

    @Override
    public void gameLoop(float delta)
    {

    }
}

















































