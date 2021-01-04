package games.entities;

import engine.core.HeightMap;
import engine.core.math.Vector2f;
import engine.core.math.Vector3f;
import engine.rendering.Vertex;

public class Terrain extends Mesh
{
    private HeightMap heightData;

    public Terrain(float heights[][])
    {
        super();
        heightData = new HeightMap(heights);
        generateVertices();
    }

    public Terrain(HeightMap heightMap)
    {
        super();
        heightData = heightMap;
        generateVertices();
    }

    private void generateVertices()
    {
        int width = heightData.getColumns();
        int depth = heightData.getRows();

        Vertex[] vertices = new Vertex[width * depth];

        int indexIndices[][] = new int[width][depth];

        int index = 0;
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < depth; j++)
            {
                float x = (((float)i / (float)(width - 1)) * 2) - 1;
                float z = (((float)j / (float)(depth - 1)) * 2) - 1;
                vertices[index] = new Vertex(new Vector3f(x, heightData.getHeight(i, j), z), new Vector2f((x + 1) / 2, (z + 1) / 2));
                indexIndices[i][j] = index;
                index++;
            }
        }

        int[] indices = new int[((width - 1) * (depth - 1)) * 6];

        int k = 0;
        for(int i = 0; i < indices.length; i++)
        {
            int x = i / depth;
            int z = i - (x * depth);

            if(z < depth - 1 && x < width - 1)
            {
                int v0 = indexIndices[x][z];
                int v1 = indexIndices[x][z + 1];
                int v2 = indexIndices[x + 1][z];
                int v3 = indexIndices[x + 1][z + 1];

                indices[k] = v0;
                indices[k + 1] = v1;
                indices[k + 2] = v2;

                indices[k + 3] = v3;
                indices[k + 4] = v2;
                indices[k + 5] = v1;
                k += 6;
            }
        }

        addVertices(vertices, indices, true, true);
    }

    public HeightMap getHeightMap()
    {
        return heightData;
    }

    public void setHeightMap(HeightMap heightMap)
    {
        heightData = heightMap;
    }

    public boolean isOnTerrain(float x, float z)
    {
        float heights[][] = heightData.getHeights();

        int width = heights.length;
        int depth = heights[0].length;

        float movedX = x - getTransformation().getPosition().getX();
        float movedZ = z - getTransformation().getPosition().getZ();

        float scaledX = (float)movedX / getTransformation().getScale().getX();
        float scaledZ = (float)movedZ / getTransformation().getScale().getZ();

        float convertedX = ((width - 1) * (scaledX + 1)) / 2;
        float convertedZ = ((depth - 1) * (scaledZ + 1)) / 2;

        int finalX = (int)Math.floor(convertedX);
        int finalZ = (int)Math.floor(convertedZ);

        return !(finalX >= heights.length - 1 || finalZ >= heights[0].length - 1 || finalX < 0 || finalZ < 0);
    }

    public boolean isOnTerrain(Vector2f position)
    {
        return isOnTerrain(position.getX(), position.getY());
    }

    public float getTerrainHeight(float x, float z)
    {
        float heights[][] = heightData.getHeights();

        int width = heights.length;
        int depth = heights[0].length;

        float movedX = x -  getTransformation().getPosition().getX();
        float movedZ = z -  getTransformation().getPosition().getZ();

        float scaledX = (float)movedX / getTransformation().getScale().getX();
        float scaledZ = (float)movedZ / getTransformation().getScale().getZ();

        float convertedX = ((width - 1) * (scaledX + 1)) / 2;
        float convertedZ = ((depth - 1) * (scaledZ + 1)) / 2;

        int finalX = (int)Math.floor(convertedX);
        int finalZ = (int)Math.floor(convertedZ);

        int endX = (int)Math.ceil(convertedX);
        int endZ = (int)Math.ceil(convertedZ);

        if(finalX >= heights.length - 1 || finalZ >= heights[0].length - 1 || finalX < 0 || finalZ < 0)
        {
            return 0;
        }

        float tileX = (convertedX - (float)finalX) / ((float)endX - (float)finalX);
        float tileZ = (convertedZ - (float)finalZ) / ((float)endZ - (float)finalZ);

        float answer;
        if (tileX <= (1 - tileZ))
        {
            answer = barryCentric(new Vector3f(0, heights[finalX][finalZ], 0), new Vector3f(1,
                    heights[finalX + 1][finalZ], 0), new Vector3f(0,
                    heights[finalX][finalZ + 1], 1), new Vector2f(tileX, tileZ));
        }
        else
        {
            answer = barryCentric(new Vector3f(1, heights[finalX + 1][finalZ], 0), new Vector3f(1,
                    heights[finalX + 1][finalZ + 1], 1), new Vector3f(0,
                    heights[finalX][finalZ + 1], 1), new Vector2f(tileX, tileZ));
        }

        return (answer * 2 - 1) * getTransformation().getScale().getY();
    }

    public float getTerrainHeight(Vector2f position)
    {
        return getTerrainHeight(position.getX(), position.getY());
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos)
    {
        float det = (p2.getZ() - p3.getZ()) * (p1.getX() - p3.getX()) + (p3.getX() - p2.getX()) * (p1.getZ() - p3.getZ());
        float l1 = ((p2.getZ() - p3.getZ()) * (pos.getX() - p3.getX()) + (p3.getX() - p2.getX()) * (pos.getY() - p3.getZ())) / det;
        float l2 = ((p3.getZ() - p1.getZ()) * (pos.getX() - p3.getX()) + (p1.getX() - p3.getX()) * (pos.getY() - p3.getZ())) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.getY() + l2 * p2.getY() + l3 * p3.getY();
    }
}
