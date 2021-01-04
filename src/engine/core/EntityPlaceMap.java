package engine.core;

import engine.core.math.*;
import games.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EntityPlaceMap
{
    private Entity              entity;
    private ArrayList<Vector3f> positions;

    public EntityPlaceMap(Entity entity)
    {
        this.entity = entity;
        positions = new ArrayList<>();
    }

    public EntityPlaceMap (Entity entity, String fileName, HeightMap heightMap)
    {
        this.entity = entity;
        positions = new ArrayList<>();

        try
        {
            BufferedImage image = ImageIO.read(new File("res/textures/" + fileName));

            for(int x = 0; x < image.getWidth(); x++)
            {
                for(int z = 0; z < image.getHeight(); z++)
                {
                    if(((image.getRGB(x, z) >> 0) & 0xFF) > 127)
                    {
                        positions.add(new Vector3f(((float)x / image.getWidth()) * 2 - 1, heightMap.getHeight(x, z), ((float)z / image.getHeight()) * 2 - 1));
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public ArrayList<Vector3f> getPositions()
    {
        return positions;
    }

    public void addEntityPosition(Vector3f position)
    {
        positions.add(position);
    }

    public void setPositions(ArrayList<Vector3f> positions)
    {
        this.positions = positions;
    }
}
