package engine.audio;

import engine.core.math.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;

public class AudioEngine
{
    public AudioEngine()
    {
        try
        {
            AL.create();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }

        setListenerPosition(0, 0, 0);
        setListenerVelocity(0, 0, 0);
    }

    public void setListenerPosition(Vector3f position)
    {
        setListenerPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setListenerPosition(float x, float y, float z)
    {
        alListener3f(AL_POSITION, x, y, z);
    }

    public void setListenerVelocity(Vector3f velocity)
    {
        setListenerVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    public void setListenerVelocity(float x, float y, float z)
    {
        alListener3f(AL_VELOCITY, x, y, z);
    }

    public void cleanUp()
    {
        AL.destroy();
    }
}
