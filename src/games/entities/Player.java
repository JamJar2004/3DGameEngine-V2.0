package games.entities;

import engine.core.Input;
import engine.core.math.Vector3f;
import games.components.Follow;
import games.components.FreeLook;
import games.components.FreeMove;

public class Player extends Entity
{
    private Entity   parent;
    private FreeMove freeMove;
    private FreeLook freeLook;

    public Player(Entity parent)
    {
        this(parent, new Vector3f());
    }

    public Player(Entity parent, Vector3f position)
    {
        this.parent = parent;
        this.parent.getTransformation().setPosition(position);
        freeMove = new FreeMove();
        freeMove.addMovementControl(Input.KEY_W, new FreeMove.Movement(Vector3f.FORWARD, 10f));
        freeMove.addMovementControl(Input.KEY_S, new FreeMove.Movement(Vector3f.BACK,    10f));
        freeMove.addMovementControl(Input.KEY_A, new FreeMove.Movement(Vector3f.LEFT,    10f));
        freeMove.addMovementControl(Input.KEY_D, new FreeMove.Movement(Vector3f.RIGHT,   10f));
        freeLook = new FreeLook(Input.KEY_ESCAPE, 4.0f);

        addComponent(freeMove).addComponent(freeLook);
        //this.parent.addComponent(freeMove).addComponent(freeLook);
        this.parent.addComponent(new Follow(this));
    }

    public Entity   getParent()   { return parent;   }
    public FreeMove getFreeMove() { return freeMove; }
    public FreeLook getFreeLook() { return freeLook; }

    public void setParent(Entity parent)       { this.parent = parent;     }
    public void setFreeMove(FreeMove freeMove) { this.freeMove = freeMove; }
    public void setFreeLook(FreeLook freeLook) { this.freeLook = freeLook; }

}
