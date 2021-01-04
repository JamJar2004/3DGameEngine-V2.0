package games.components;

import engine.core.Input;
import engine.core.math.Vector3f;

import java.util.HashMap;

public class FreeMove extends EntityComponent
{
    private HashMap<Integer, Movement> movementControls;
    private boolean allowMovementX;
    private boolean allowMovementY;
    private boolean allowMovementZ;

    public FreeMove()
    {
        super();
        movementControls = new HashMap<>();
        allowMovementX   = true;
        allowMovementY   = true;
        allowMovementZ   = true;
    }

    public static class Movement
    {
        private Vector3f direction;
        private float    speed;

        public Movement(Vector3f direction, float speed)
        {
            this.direction = direction;
            this.speed = speed;
        }

        public Vector3f getDirection() { return direction; }
        public float    getSpeed()     { return speed;     }

        public void setDirection(Vector3f direction) { this.direction = direction; }
        public void setSpeed(float speed)            { this.speed = speed;         }
    }

    @Override
    public void input(float delta)
    {
        for(int keyCode : movementControls.keySet())
        {
            if(Input.getKey(keyCode))
            {
                Movement movement = movementControls.get(keyCode);

                Vector3f direction = movement.getDirection().rotate(getParent().getTransformation().getRotation());

                if(!allowMovementX)
                    direction.setX(0);

                if(!allowMovementY)
                    direction.setY(0);

                if(!allowMovementZ)
                    direction.setZ(0);

                getParent().getTransformation().move(direction, movement.getSpeed() * delta);
            }
        }
    }

    public boolean getIsMovementXAllowed() { return allowMovementX; }
    public boolean getIsMovementYAllowed() { return allowMovementY; }
    public boolean getIsMovementZAllowed() { return allowMovementZ; }

    public HashMap<Integer, Movement> getMovementControls() { return movementControls; }

    public Movement getMovementControl(int keyCode)  { return movementControls.get(keyCode);         }
    public boolean  getDoesControlExist(int keyCode) { return movementControls.containsKey(keyCode); }


    public void clearMovementControls() { movementControls.clear(); }

    public void removeMovementControl(int keyCode)
    {
        if(movementControls.containsKey(keyCode))
            movementControls.remove(keyCode);
        else
        {
            System.err.println("Error: The key code " + keyCode + " does not exist.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    public void addMovementControl(int keyCode, Movement movement)
    {
        if(!movementControls.containsKey(keyCode))
            movementControls.put(keyCode, movement);
        else
        {
            System.err.println("Error: The key code " + keyCode + " has already been used.");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    public void setAllowMovementX(boolean allowMovementX) { this.allowMovementX = allowMovementX; }
    public void setAllowMovementY(boolean allowMovementY) { this.allowMovementY = allowMovementY; }
    public void setAllowMovementZ(boolean allowMovementZ) { this.allowMovementZ = allowMovementZ; }
}
