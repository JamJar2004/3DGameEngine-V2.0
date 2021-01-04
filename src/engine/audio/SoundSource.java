package engine.audio;

import com.sun.prism.shader.AlphaOne_ImagePattern_Loader;
import engine.core.math.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundSource
{
    private int     sourceID;
    private Sound   sound;

    public SoundSource()
    {
        sourceID = alGenSources();
        alSourcef(sourceID, AL_GAIN, 1);
        alSourcef(sourceID, AL_PITCH, 1);
        alSource3f(sourceID, AL_POSITION, 0, 0, 0);
    }

    public void play()
    {
        stop();
        alSourcePlay(sourceID);
    }

    public void pause()
    {
        alSourcePause(sourceID);
    }

    public void stop()
    {
        alSourceStop(sourceID);
    }

    public Sound getSound()
    {
        return sound;
    }

    public void setPosition(Vector3f position)
    {
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(float x, float y, float z)
    {
        alSource3f(sourceID, AL_POSITION, x, y, z);
    }

    public void setPitch(float pitch)
    {
        alSourcef(sourceID, AL_PITCH, pitch);
    }

    public void setVolume(float volume)
    {
        alSourcef(sourceID, AL_GAIN, volume);
    }

    public void setVelocity(Vector3f velocity)
    {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    public void setVelocity(float x, float y, float z)
    {
        alSource3f(sourceID, AL_VELOCITY, x, y, z);
    }

    public void setLooping(boolean loop)
    {
        alSourcei(sourceID, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public boolean getIsPlaying()
    {
        return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void setSound(Sound sound)
    {
        this.sound = sound;
        alSourcei(sourceID, AL_BUFFER, sound.getBufferID());
    }

    @Override
    public void finalize()
    {
        stop();
        alDeleteSources(sourceID);
    }
}
