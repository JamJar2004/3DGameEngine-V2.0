package engine.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeightMap
{
    private float[][] heights;

    public HeightMap(float[][] heights)
    {
        this.heights = heights;
    }

    public HeightMap(String fileName)
    {
        loadHeightMap("res/textures/" + fileName);
    }

    private void loadHeightMap(String fileName)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File(fileName));
            heights = new float[image.getWidth()][image.getHeight()];

            for(int x = 0; x < image.getWidth(); x++)
            {
                for(int y = 0; y < image.getHeight(); y++)
                {
                    int value = (image.getRGB(x, y)) & 0xFF;
                    heights[x][y] = ((float)value / 255) * 2 - 1;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public float[][] getHeights()            { return heights;           }
    public float     getHeight(int x, int z) { return heights[x][z];     }
    public int       getColumns()            { return heights.length;    }
    public int       getRows()               { return heights[0].length; }

    public void setHeight(int x, int z, float height) { heights[x][z] = height; }
}
