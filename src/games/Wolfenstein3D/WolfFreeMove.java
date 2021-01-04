package games.Wolfenstein3D;

import engine.core.Input;
import engine.core.math.*;
import games.components.FreeMove;

import static games.Wolfenstein3D.WolfPlayer.*;

public class WolfFreeMove extends FreeMove
{
    private Level level;

    public WolfFreeMove()
    {
        addMovementControl(Input.KEY_W, new FreeMove.Movement(Vector3f.FORWARD, 7f));
        addMovementControl(Input.KEY_S, new FreeMove.Movement(Vector3f.BACK,    7f));
        addMovementControl(Input.KEY_A, new FreeMove.Movement(Vector3f.LEFT,    7f));
        addMovementControl(Input.KEY_D, new FreeMove.Movement(Vector3f.RIGHT,   7f));
    }

    @Override
    public void input(float delta)
    {
        for(int keyCode : getMovementControls().keySet())
        {
            if(Input.getKey(keyCode))
            {
                Movement movement = getMovementControls().get(keyCode);

                Vector3f movementVector = movement.getDirection().rotate(getParent().getTransformation().getRotation());

                if(!getIsMovementXAllowed())
                    movementVector.setX(0);

                if(!getIsMovementYAllowed())
                    movementVector.setY(0);

                if(!getIsMovementZAllowed())
                    movementVector.setZ(0);

                float movAmt = (float)(movement.getSpeed() * delta);

                movementVector.setY(0);

                if(movementVector.length() > 0)
                    movementVector = movementVector.normalize();

                Vector3f oldPos = getParent().getTransformation().getPosition();
                Vector3f newPos = oldPos.add(movementVector.multiply(movAmt));

                Vector3f collisionVector = level.checkCollision(oldPos, newPos, new Vector2f(PLAYER_SIZE, PLAYER_SIZE));
                movementVector = movementVector.multiply(collisionVector);

                getParent().getTransformation().move(movementVector, movAmt);
            }
        }
    }

    public void setLevel(Level level) { this.level = level; }
}
