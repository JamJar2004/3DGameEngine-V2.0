package games.components;

import engine.core.Input;
import engine.core.Window;
import engine.core.math.Vector2f;
import engine.core.math.Vector3f;

public class FreeLook extends EntityComponent
{
    private float   sensitivity;
    private boolean mouseLocked;
    private int     unlockMouseKey;
    private boolean canRotateX;
    private boolean canRotateY;

    public FreeLook(int unlockMouseKey) { this(unlockMouseKey, 0.5f); }

    public FreeLook(int unlockMouseKey, float sensitivity)
    {
        this.sensitivity = sensitivity;
        this.mouseLocked = false;
        this.unlockMouseKey = unlockMouseKey;
        this.canRotateX = true;
        this.canRotateY = true;
    }

    @Override
    public void input(float delta)
    {
        if(Input.getKey(unlockMouseKey))
        {
            Input.setCursor(true);
            mouseLocked = false;
        }
        if(Input.getMouseDown(0))
        {
            Input.setMousePosition(Window.getCenterPosition());
            Input.setCursor(false);
            mouseLocked = true;
        }

        if(mouseLocked)
        {
            Vector2f deltaPos = Input.getMousePosition().subtract(Window.getCenterPosition());

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if(rotY && canRotateY)
                getParent().getTransformation().turn(Vector3f.UP, (float)Math.toRadians(deltaPos.getX() * sensitivity));
            if(rotX && canRotateX)
                getParent().getTransformation().turn(getParent().getTransformation().getRotation().getRight(), (float)Math.toRadians(-deltaPos.getY() * sensitivity));

            if(rotY || rotX)
                Input.setMousePosition(Window.getCenterPosition());
        }
    }

    @Override
    public void update(float delta)
    {

    }

    public boolean getCanPitch() { return canRotateX; }
    public boolean getCanYaw()   { return canRotateY; }

    public float getSensitivity()    { return sensitivity;    }
    public int   getUnlockMouseKey() { return unlockMouseKey; }


    public void setCanRotateX(boolean canRotateX) { this.canRotateX = canRotateX; }
    public void setCanRotateY(boolean canRotateY) { this.canRotateY = canRotateY; }

    public void  setSensitivity(float sensitivity)     { this.sensitivity = sensitivity;       }
    public void  setUnlockMouseKey(int unlockMouseKey) { this.unlockMouseKey = unlockMouseKey; }
}
