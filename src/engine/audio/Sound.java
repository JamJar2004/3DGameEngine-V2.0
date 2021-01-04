package engine.audio;

import static org.lwjgl.openal.AL10.*;

public class Sound
{
    private int bufferID;

    public Sound(String fileName)
    {
        bufferID = alGenBuffers();
        WaveData waveFile = WaveData.create("sounds/" + fileName);
        alBufferData(bufferID, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
    }

    public int getBufferID() { return bufferID; }
}
