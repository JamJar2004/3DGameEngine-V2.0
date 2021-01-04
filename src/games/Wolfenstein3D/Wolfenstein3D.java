package games.Wolfenstein3D;

import engine.core.*;
import engine.core.math.*;


public class Wolfenstein3D extends Game
{
    private Level level;

    @Override
    public void init()
    {
        turnOffHighQuality();

        WolfPlayer player;
        player = new WolfPlayer(getMainCamera(), new Vector3f(6, 0.4375f, 6));
        level = new Level("level1.png", "WolfCollection.png", player);
        addEntity(level.getMesh());
        addEntity(player);

        addEntity(level.getMonster());

        for(Door door : level.getDoors())
            addEntity(door);
    }

    @Override
    public void gameLoop(float delta)
    {
        level.input(delta);
    }
}
