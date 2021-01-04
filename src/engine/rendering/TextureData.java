package engine.rendering;

import org.newdawn.slick.opengl.*;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class TextureData
{
    private int        width;
    private int        height;
    private ByteBuffer buffer;

    public TextureData(String fileName)
    {
        try
        {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);

            width  = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);

            decoder.decode(buffer, width * 4, PNGDecoder.RGBA);
            buffer.flip();
            in.close();
        }
        catch(Exception e)
        {
            System.err.println("Failed to load texture " + fileName + ".");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public TextureData(int width, int height, ByteBuffer buffer)
    {
        this.width  = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public ByteBuffer getBuffer()
    {
        return buffer;
    }
}

