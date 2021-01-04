package games.Wolfenstein3D;

import engine.core.*;
import engine.core.math.*;
import engine.rendering.*;
import games.entities.*;

import java.util.ArrayList;

public class Level
{
    private static final float SPOT_WIDTH  = 1.0f;
    private static final float SPOT_LENGTH = 1.0f;
    private static final float SPOT_HEIGHT = 1.0f;

    private static final int   NUM_TEX_EXP   = 4;
    private static final int   NUM_TEXTURES  = (int)Math.pow(2, NUM_TEX_EXP);
    private static final float OPEN_DISTANCE = 1.0f;
    private static final float DOOR_OPEN_MOVEMENT_AMOUNT = 0.9f;

    private Mesh         mesh;
    private Material     material;
    private Bitmap       level;
    private WolfPlayer   player;

    //TEMP
    private Monster monster;

    private ArrayList<Door> doors;

    private ArrayList<Vector2f> collisionPositionStart;
    private ArrayList<Vector2f> collisionPositionEnd;

    public Level(String levelName, String textureName, WolfPlayer player)
    {
        this.player = player;
        level = new Bitmap(levelName).flipY();

        material = new Material();
        material.setTexture("texture", new Texture("Wolfenstein3D/" + textureName, false));

        doors = new ArrayList<>();
        collisionPositionStart = new ArrayList<>();
        collisionPositionEnd   = new ArrayList<>();

        generateLevel();
        init();
    }

    private void init()
    {
        Transformation tempTransform = new Transformation();
        tempTransform.setPosition(11, 0, 12);
        this.player.setLevel(this);
        this.monster = new Monster(tempTransform, player, this);
    }

    public void openDoors(Vector3f position)
    {
        for(Door door : doors)
        {
            if(door.getTransformation().getPosition().subtract(position).length() < OPEN_DISTANCE)
            {
                door.open();
            }
        }
    }

    public void input(float delta)
    {
        if(Input.getKey(Input.KEY_E))
        {
            openDoors(player.getTransformation().getPosition());
            monster.damage(30);
        }
    }

    public Vector3f checkCollision(Vector3f oldPosition, Vector3f newPosition, Vector2f objectSize)
    {
        Vector2f collisionVector = new Vector2f(1, 1);
        Vector3f movementVector = newPosition.subtract(oldPosition);

        if(movementVector.length() > 0)
        {
            Vector2f blockSize = new Vector2f(SPOT_WIDTH, SPOT_LENGTH);

            Vector2f oldPos2 = oldPosition.getXZ();
            Vector2f newPos2 = newPosition.getXZ();

            for (int i = 0; i < level.getWidth(); i++)
                for (int j = 0; j < level.getHeight(); j++)
                    if ((level.getPixel(i, j) & 0xFFFFFF) == 0)
                        collisionVector = collisionVector.multiply(rectCollide(oldPos2, newPos2, objectSize, blockSize.multiply(new Vector2f(i, j)), blockSize));

              for(Door door : doors)
              {
                  Vector2f doorSize = door.getDoorSize();
                  collisionVector = collisionVector.multiply(rectCollide(oldPos2, newPos2, objectSize, door.getTransformation().getPosition().getXZ(), doorSize));
              }
        }

        return new Vector3f(collisionVector.getX(), 0, collisionVector.getY());
    }

    public Vector2f checkIntersections(Vector2f lineStart, Vector2f lineEnd)
    {
        Vector2f nearestIntersection = null;

        for(int i = 0; i < collisionPositionStart.size(); i++)
        {
            Vector2f collisionVector = lineIntersect(lineStart, lineEnd, collisionPositionStart.get(i), collisionPositionEnd.get(i));
            nearestIntersection = findNearestVector2f(nearestIntersection, collisionVector, lineStart);
        }

        for(Door door : doors)
        {
            Vector2f collisionVector = lineIntersectRect(lineStart, lineEnd, door.getTransformation().getPosition().getXZ(), door.getDoorSize());
            nearestIntersection = findNearestVector2f(nearestIntersection, collisionVector, lineStart);
        }

        return nearestIntersection;
    }

    private Vector2f findNearestVector2f(Vector2f a, Vector2f b, Vector2f positionRelativeTo)
    {
        if(b != null && (a == null ||
                a.subtract(positionRelativeTo).length() > b.subtract(positionRelativeTo).length()))
            return b;

        return a;
    }

    public Vector2f lineIntersectRect(Vector2f lineStart, Vector2f lineEnd, Vector2f rectPosition, Vector2f rectSize)
    {
        Vector2f result = null;

        Vector2f collisionVector = lineIntersect(lineStart, lineEnd, rectPosition, new Vector2f(rectPosition.getX() + rectSize.getX(), rectPosition.getY()));
        result = findNearestVector2f(result, collisionVector, lineStart);

        collisionVector = lineIntersect(lineStart, lineEnd, rectPosition, new Vector2f(rectPosition.getX(), rectPosition.getY() + rectSize.getY()));
        result = findNearestVector2f(result, collisionVector, lineStart);

        collisionVector = lineIntersect(lineStart, lineEnd, new Vector2f(rectPosition.getX(), rectPosition.getY() + rectSize.getY()), rectPosition.add(rectSize));
        result = findNearestVector2f(result, collisionVector, lineStart);

        collisionVector = lineIntersect(lineStart, lineEnd, new Vector2f(rectPosition.getX() + rectSize.getX(), rectPosition.getY()), rectPosition.add(rectSize));
        result = findNearestVector2f(result, collisionVector, lineStart);

        return result;
    }

    private Vector2f lineIntersect(Vector2f lineStart1, Vector2f lineEnd1, Vector2f lineStart2, Vector2f lineEnd2)
    {
        Vector2f line1 = lineEnd1.subtract(lineStart1);
        Vector2f line2 = lineEnd2.subtract(lineStart2);

        //lineStart1 + line1 * a == lineStart2 + line2 * b

        float cross = line1.cross(line2);

        if(cross == 0)
            return null;

        Vector2f distanceBetweenLineStarts = lineStart2.subtract(lineStart1);

        float a = distanceBetweenLineStarts.cross(line2) / cross;
        float b = distanceBetweenLineStarts.cross(line1) / cross;

        if(0.0f < a && a < 1.0f && 0.0f < b && b < 1.0f)
            return lineStart1.add(line1.multiply(a));

        return null;
    }

    private Vector2f rectCollide(Vector2f oldPosition, Vector2f newPosition, Vector2f size1, Vector2f position2, Vector2f size2)
    {
        Vector2f result = new Vector2f(0,0);

        if(newPosition.getX() + size1.getX() < position2.getX() ||
           newPosition.getX() - size1.getX() > position2.getX() + size2.getX() * size2.getX() ||
           oldPosition.getY() + size1.getY() < position2.getY() ||
           oldPosition.getY() - size1.getY() > position2.getY() + size2.getY() * size2.getY())
                result.setX(1);

        if(oldPosition.getX() + size1.getX() < position2.getX() ||
           oldPosition.getX() - size1.getX() > position2.getX() + size2.getX() * size2.getX() ||
           newPosition.getY() + size1.getY() < position2.getY() ||
           newPosition.getY() - size1.getY() > position2.getY() + size2.getY() * size2.getY())
                result.setY(1);

        return result;
    }

    private void addFace(ArrayList<Integer> indices, int startLocation, boolean direction)
    {
        if(direction)
        {
            indices.add(startLocation + 2);
            indices.add(startLocation + 1);
            indices.add(startLocation + 0);
            indices.add(startLocation + 3);
            indices.add(startLocation + 2);
            indices.add(startLocation + 0);
        }
        else
        {
            indices.add(startLocation + 0);
            indices.add(startLocation + 1);
            indices.add(startLocation + 2);
            indices.add(startLocation + 0);
            indices.add(startLocation + 2);
            indices.add(startLocation + 3);
        }
    }

    private float[] calcTexCoords(int value)
    {
        int texX = value / NUM_TEXTURES;
        int texY = texX % NUM_TEX_EXP;
        texX /= NUM_TEX_EXP;

        float[] result = new float[4];

        result[0] = 1f - (float)texX / (float)NUM_TEX_EXP;
        result[1] = result[0]  - 1f / (float)NUM_TEX_EXP;
        result[3] = 1f - (float)texY / (float)NUM_TEX_EXP;
        result[2] = result[3] - 1f / (float)NUM_TEX_EXP;

        return result;
    }

    private void addVertices(ArrayList<Vertex> vertices, int i, int j, float offset, boolean x, boolean y, boolean z, float[] texCoords)
    {
        if(x && z)
        {
            vertices.add(new Vertex(new Vector3f( i      * SPOT_WIDTH, offset * SPOT_HEIGHT,  j      * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, offset * SPOT_HEIGHT,  j      * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, offset * SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[2])));
            vertices.add(new Vertex(new Vector3f( i      * SPOT_WIDTH, offset * SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[2])));
        }
        else if(x && y)
        {
            vertices.add(new Vertex(new Vector3f( i      * SPOT_WIDTH,  j      * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH,  j      * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[3])));
            vertices.add(new Vertex(new Vector3f((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[2])));
            vertices.add(new Vertex(new Vector3f( i      * SPOT_WIDTH, (j + 1) * SPOT_HEIGHT, offset * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[2])));
        }
        else if(y && z)
        {
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH,  i      * SPOT_HEIGHT,  j      * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[3])));
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH,  i      * SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[3])));
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH, (i + 1) * SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2f(texCoords[0], texCoords[2])));
            vertices.add(new Vertex(new Vector3f(offset * SPOT_WIDTH, (i + 1) * SPOT_HEIGHT,  j      * SPOT_LENGTH), new Vector2f(texCoords[1], texCoords[2])));
        }
        else
        {
            System.err.println("Error: Invalid plane used in level generator");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    private void addDoor(int x, int y)
    {
        Transformation doorTransform = new Transformation();

        boolean xDoor = (level.getPixel(x, y - 1) & 0xFFFFFF) == 0 && (level.getPixel(x, y + 1) & 0xFFFFFF) == 0;
        boolean yDoor = (level.getPixel(x - 1, y) & 0xFFFFFF) == 0 && (level.getPixel(x + 1, y) & 0xFFFFFF) == 0;

        if(!(xDoor ^ yDoor))
        {
            System.err.println("Error: Level generation has failed. You placed a door in the invalid location at " + x + ", " + y);
            new Exception().printStackTrace();
            System.exit(1);
        }

        Vector3f openPosition = null;

        if(yDoor)
        {
            doorTransform.setPosition(x, 0, y + SPOT_LENGTH / 2);
            openPosition = doorTransform.getPosition().subtract(new Vector3f(DOOR_OPEN_MOVEMENT_AMOUNT, 0, 0));
        }

        if(xDoor)
        {
            doorTransform.setPosition(x + SPOT_WIDTH / 2, 0, y);
            doorTransform.turn(Vector3f.UP, -90);
            openPosition = doorTransform.getPosition().subtract(new Vector3f(0, 0, DOOR_OPEN_MOVEMENT_AMOUNT));
        }

        Door result = new Door(doorTransform, openPosition);
        result.setMaterial(material);
        doors.add(result);
    }

    private void addSpecial(int blueVal, int x, int y)
    {
        if(blueVal == 16)
            addDoor(x, y);
    }

    private void generateLevel()
    {
        ArrayList<Vertex> vertices  = new ArrayList<>();
        ArrayList<Integer> indices  = new ArrayList<>();

        for(int i = 0; i < level.getWidth(); i++)
        {
            for(int j = 0; j < level.getHeight(); j++)
            {
                if((level.getPixel(i, j) & 0xFFFFFF) == 0)
                    continue;

                float[] texCoords = calcTexCoords((level.getPixel(i, j) & 0x00FF00) >> 8);

                addSpecial((level.getPixel(i, j) & 0x0000FF), i, j);

                //Generate Floor
                addFace(indices, vertices.size(), true);
                addVertices(vertices, i, j, 0, true, false, true, texCoords);

                //Generate Ceiling
                addFace(indices, vertices.size(), false);
                addVertices(vertices, i, j, 1, true, false, true, texCoords);

                //Generate Walls
                texCoords = calcTexCoords((level.getPixel(i, j) & 0xFF0000) >> 16);

                if((level.getPixel(i, j - 1) & 0xFFFFFF) == 0)
                {
                    collisionPositionStart.add(new Vector2f(i * SPOT_WIDTH, j * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2f((i + 1) * SPOT_WIDTH, j * SPOT_LENGTH));
                    addFace(indices, vertices.size(), false);
                    addVertices(vertices, i, 0, j, true, true, false, texCoords);
                }

                if((level.getPixel(i, j + 1) & 0xFFFFFF) == 0)
                {
                    collisionPositionStart.add(new Vector2f(i * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2f((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));
                    addFace(indices, vertices.size(), true);
                    addVertices(vertices, i, 0, (j + 1), true, true, false, texCoords);
                }

                if((level.getPixel(i - 1, j) & 0xFFFFFF) == 0)
                {
                    collisionPositionStart.add(new Vector2f(i * SPOT_WIDTH, j * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2f(i * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));
                    addFace(indices, vertices.size(), true);
                    addVertices(vertices, 0, j, i, false, true, true, texCoords);
                }

                if((level.getPixel(i + 1, j) & 0xFFFFFF) == 0)
                {
                    collisionPositionStart.add(new Vector2f((i + 1) * SPOT_WIDTH, j * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2f((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));
                    addFace(indices, vertices.size(), false);
                    addVertices(vertices, 0, j, (i + 1), false, true, true, texCoords);
                }
            }
        }

        Vertex[]  vertArray = new Vertex[vertices.size()];
        Integer[] intArray  = new Integer[indices.size()];
        vertices.toArray(vertArray);
        indices.toArray(intArray);

        mesh = new Mesh(vertArray, Util.toIntArray(intArray));
        mesh.setMaterial(material);
    }

    public Material getMaterial()
    {
        return material;
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public ArrayList<Door> getDoors()
    {
        return doors;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Monster getMonster()
    {
        return monster;
    }
}
