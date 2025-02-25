/*
 * This class has been brought from ThinMatrix's OpenAL tutorial.
 * Link: https://www.youtube.com/redirect?event=video_description&redir_token=QUFFLUhqbl9lY3lHSjhMRUdmSW5QcWRCRVRrVXl1UFZIQXxBQ3Jtc0ttdU1OakNWS1IyWmlXRERXREpNTmY1UFhLS0JCZEdaSjFzNUl4OXcwTVhIb0FXVFRVUWhtZlUzNUV5TVdSX3ktZ1o4Si1NdUZKeUYxc2lBT1kzalFaakR0NWMwNVFMeGtqRkpaMjEzbUVaRENLaE1SOA&q=https%3A%2F%2Fwww.dropbox.com%2Fs%2Fhkbfk2z1qngsg2y%2FWaveData.java%3Fdl%3D0
 */

package engine.audio;

import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL10.*;

public class WaveData
{

    final int format;
    final int samplerate;
    final int totalBytes;
    final int bytesPerFrame;
    final ByteBuffer data;

    private final AudioInputStream audioStream;
    private final byte[] dataArray;

    private WaveData(AudioInputStream stream)
    {
        this.audioStream = stream;
        AudioFormat audioFormat = stream.getFormat();
        format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
        this.samplerate = (int) audioFormat.getSampleRate();
        this.bytesPerFrame = audioFormat.getFrameSize();
        this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
        this.data = BufferUtils.createByteBuffer(totalBytes);
        this.dataArray = new byte[totalBytes];
        loadData();
    }

    protected void dispose()
    {
        try
        {
            audioStream.close();
            data.clear();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private ByteBuffer loadData()
    {
        try
        {
            int bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.clear();
            data.put(dataArray, 0, bytesRead);
            data.flip();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Couldn't read bytes from audio stream!");
        }
        return data;
    }


    public static WaveData create(String file)
    {
        InputStream stream = Class.class.getResourceAsStream("/" + file);
        if(stream == null)
        {
            System.err.println("Couldn't find file: "+file);
            new FileNotFoundException().printStackTrace();
            System.exit(1);
        }
        InputStream bufferedInput = new BufferedInputStream(stream);
        AudioInputStream audioStream = null;
        try
        {
            audioStream = AudioSystem.getAudioInputStream(bufferedInput);
        }
        catch (UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WaveData wavStream = new WaveData(audioStream);
        return wavStream;
    }


    private static int getOpenAlFormat(int channels, int bitsPerSample)
    {
        if(channels == 1)
        {
            return bitsPerSample == 8 ? AL_FORMAT_MONO8 : AL_FORMAT_MONO16;
        }
        else
        {
            return bitsPerSample == 8 ? AL_FORMAT_STEREO8 : AL_FORMAT_STEREO16;
        }
    }

}