package games.Wolfenstein3D;

import engine.core.Input;
import engine.core.math.Vector3f;
import games.components.*;
import games.entities.Entity;
import games.entities.Player;

public class WolfPlayer extends Player
{
    public static float PLAYER_SIZE = 0.2f;

    private Level level;
    private WolfFreeMove freeMove;

    public WolfPlayer(Entity parent, Vector3f position)
    {
        super(parent, position);
        getTransformation().setPosition(position);
        freeMove = new WolfFreeMove();
        freeMove.setLevel(level);
        getParent().getComponents().clear();
        getComponents().clear();
        setFreeLook(new FreeLook(Input.KEY_ESCAPE, 4.0f));


        addComponent(freeMove).addComponent(getFreeLook());
        //this.parent.addComponent(freeMove).addComponent(freeLook);
        getParent().addComponent(new Follow(this));
        //getParent().addComponent(freeMove).addComponent(getFreeLook());
//        addComponent(new Follow(getParent()));
//        //addComponent(freeMove).addComponent(getFreeLook());
//        getParent().addComponent(new Follow(this));
    }

    public Level getLevel() { return level; }

    @Override
    public WolfFreeMove getFreeMove() { return freeMove; }

    public void setLevel(Level level)
    {
        this.level = level;
        freeMove.setLevel(level);
    }
    public void setFreeMove(WolfFreeMove freeMove) { this.freeMove = freeMove; }
}
