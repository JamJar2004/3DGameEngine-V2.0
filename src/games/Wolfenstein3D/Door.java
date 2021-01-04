package games.Wolfenstein3D;

import engine.core.Time;
import engine.core.math.*;
import engine.rendering.*;
import games.entities.*;

public class Door extends Mesh
{
    public static final float  LENGTH       = 1;
    public static final float  HEIGHT       = 1;
    public static final float  WIDTH        = 0.125f;
    public static final float  START        = 0;
    public static final double TIME_TO_OPEN = 0.25;
    public static final double CLOSE_DELAY = 2.0;

    private Vector3f openPosition;
    private Vector3f closePosition;

    private boolean isOpening;
    private double  openingStartTime;
    private double  openTime;
    private double  closingStartTime;
    private double  closeTime;

    public Door(Transformation transformation, Vector3f openPosition)
    {
        super();

        setTransformation(transformation);
        this.closePosition = getTransformation().getPosition().clone();
        this.openPosition  = openPosition;
        this.isOpening     = false;

        Vertex[] vertices = new Vertex[] {new Vertex(new Vector3f(START , START , START), new Vector2f(0.5f , 1.0f )),
                                          new Vertex(new Vector3f(START , HEIGHT, START), new Vector2f(0.5f , 0.75f)),
                                          new Vertex(new Vector3f(LENGTH, HEIGHT, START), new Vector2f(0.75f, 0.75f)),
                                          new Vertex(new Vector3f(LENGTH, START , START), new Vector2f(0.75f, 1.0f )),

                                          new Vertex(new Vector3f(START, START , START),  new Vector2f(0.73f, 1.0f )),
                                          new Vertex(new Vector3f(START, HEIGHT, START),  new Vector2f(0.73f, 0.75f)),
                                          new Vertex(new Vector3f(START, HEIGHT, WIDTH),  new Vector2f(0.75f, 0.75f)),
                                          new Vertex(new Vector3f(START, START , WIDTH),  new Vector2f(0.75f, 1.0f )),

                                          new Vertex(new Vector3f(START , START , WIDTH), new Vector2f(0.5f , 1.0f )),
                                          new Vertex(new Vector3f(START , HEIGHT, WIDTH), new Vector2f(0.5f , 0.75f)),
                                          new Vertex(new Vector3f(LENGTH, HEIGHT, WIDTH), new Vector2f(0.75f, 0.75f)),
                                          new Vertex(new Vector3f(LENGTH, START , WIDTH), new Vector2f(0.75f, 1.0f )),

                                          new Vertex(new Vector3f(LENGTH, START , START), new Vector2f(0.73f, 1.0f )),
                                          new Vertex(new Vector3f(LENGTH, HEIGHT, START), new Vector2f(0.73f, 0.75f)),
                                          new Vertex(new Vector3f(LENGTH, HEIGHT, WIDTH), new Vector2f(0.75f, 0.75f)),
                                          new Vertex(new Vector3f(LENGTH, START , WIDTH), new Vector2f(0.75f, 1.0f ))};

        int[] indices = new int[] {0,  1,  2,
                                   0,  2,  3,

                                   6,  5,  4,
                                   7,  6,  4,

                                   10, 9,  8,
                                   11, 10, 8,

                                   12, 13, 14,
                                   12, 14, 15};

        addVertices(vertices, indices, false, false);
    }

    public void open()
    {
        if(isOpening)
            return;

        openingStartTime = Time.getTime();
        openTime         = openingStartTime + TIME_TO_OPEN;
        closingStartTime = openTime + CLOSE_DELAY;
        closeTime        = closingStartTime + TIME_TO_OPEN;

        isOpening = true;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        if(isOpening)
        {
            double time = Time.getTime();
            if(time < openTime)
            {
                getTransformation().setPosition(closePosition.lerp(openPosition, (float)((time - openingStartTime) / TIME_TO_OPEN)));
            }
            else if(time < closingStartTime)
            {
                getTransformation().setPosition(openPosition);
            }
            else if(time < closeTime)
            {
                getTransformation().setPosition(openPosition.lerp(closePosition, (float)((time - closingStartTime) / TIME_TO_OPEN)));
            }
            else
            {
                getTransformation().setPosition(closePosition);
                isOpening = false;
            }
        }
    }

    public Vector2f getDoorSize()
    {
        float angleY = (float)Math.floor(getTransformation().getRotation().toEuler().getY());
        if(angleY == -91)
            return new Vector2f(Door.WIDTH, Door.LENGTH);
        else
            return new Vector2f(Door.LENGTH, Door.WIDTH);
    }

    public void setClosePosition(Vector3f closePosition)
    {
        this.closePosition = closePosition;
    }
}
