package engine.rendering;

import engine.core.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture
{
    private static HashMap<String, Texture> loadedTextures = new HashMap<>();

    private static BufferedImage image  = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
    public static Texture NULL_TEXTURE  = new Texture(image);
    public static Texture WHITE_TEXTURE = new Texture(generateWhiteImage(16, 16));

    protected int ID;
    protected int samplerSlot;

    private int width;
    private int height;

    public enum FlipType
    {
        HORIZONTAL, VERTICAL, NONE;
    }

    public Texture(String fileName)
    {
        this(fileName, FlipType.NONE, true);
    }

    public Texture(String fileName, boolean linearFiltering, boolean tiled)
    {
        this(fileName, FlipType.NONE, linearFiltering, tiled);
    }

    public Texture(String fileName, boolean linearFiltering)
    {
        this(fileName, FlipType.NONE, linearFiltering);
    }

    public Texture(String fileName, FlipType flipType)
    {
        this(fileName, flipType, false);
    }

    public Texture(String fileName, FlipType flipType, boolean linearFiltering)
    {
        this(fileName, flipType, linearFiltering, true);
    }

    public Texture(String fileName, FlipType flipType, boolean linearFiltering, boolean tiled)
    {
        if(loadedTextures.containsKey(fileName))
        {
            Texture loadedTexture = loadedTextures.get(fileName);
            ID = loadedTexture.getID();
        }
        else
        {
            loadTexture(fileName, flipType, linearFiltering, tiled);
            samplerSlot = 0;
        }
    }

    protected Texture(int ID)
    {
        this.ID = ID;
        samplerSlot = 0;
    }

    public Texture(BufferedImage image)
    {
        this(image, FlipType.NONE);
    }

    public Texture(BufferedImage image, FlipType flipType)
    {
        this(image, flipType, true);
    }

    public Texture(BufferedImage image, FlipType flipType, boolean linearFiltering)
    {
        this(image, flipType, linearFiltering, true);
    }

    public Texture(BufferedImage image, FlipType flipType, boolean linearFiltering, boolean tiled)
    {
        makeFromBufferedImage(image, linearFiltering, flipType, tiled);
        samplerSlot = 0;
    }

    private void loadTexture(String fileName, FlipType flipType, boolean linearFiltering, boolean tiled)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
            makeFromBufferedImage(image, linearFiltering, flipType, tiled);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void makeFromBufferedImage(BufferedImage image, boolean linearFiltering, FlipType flipType, boolean tiled)
    {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        width  = image.getWidth();
        height = image.getHeight();

        ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
        boolean hasAlpha = image.getColorModel().hasAlpha();

        int nx = 0;
        int ny = 0;

        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                switch(flipType)
                {
                    case NONE:
                        nx = x;
                        ny = y;
                        break;
                    case HORIZONTAL:
                        nx = image.getWidth() - (x + 1);
                        ny = y;
                        break;
                    case VERTICAL:
                        nx = x;
                        ny = image.getHeight() - (y + 1);
                        break;
                }

                int pixel = pixels[ny * image.getWidth() + nx];

                byte red   = (byte) ((pixel >> 16) & 0xFF);
                byte green = (byte) ((pixel >> 8) & 0xFF);
                byte blue  = (byte) ((pixel) & 0xFF);

                buffer.put(red);
                buffer.put(green);
                buffer.put(blue);

                if (hasAlpha)
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                else
                {
                    buffer.put((byte) (0xFF));
                }
            }
        }

        buffer.flip();

        int ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);

        if(tiled)
        {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        }
        else
        {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        if(linearFiltering)
        {
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }
        else
        {
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        }

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        if(linearFiltering)
        {
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
        }

        this.ID = ID;
    }

    public void bind()
    {
        assert(samplerSlot >= 0 && samplerSlot < 32);
        glActiveTexture(GL_TEXTURE0 + samplerSlot);
        glBindTexture(GL_TEXTURE_2D, ID);
    }

    public int getSamplerSlot()
    {
        return samplerSlot;
    }

    public void setSamplerSlot(int samplerSlot)
    {
        this.samplerSlot = samplerSlot;
    }

    private static BufferedImage generateWhiteImage(int width, int height)
    {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                result.setRGB(x, y, 255 + (255 << 8) + (255 << 16) + (255 << 24));
            }
        }
        return result;
    }

    public int getID()     { return ID;     }

    public int getWidth()  { return width;  }

    public int getHeight() { return height; }
}
