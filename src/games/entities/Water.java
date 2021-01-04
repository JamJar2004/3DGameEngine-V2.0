package games.entities;

import engine.rendering.Material;
import engine.rendering.Texture;
import engine.rendering.Vertex;
import engine.rendering.renderers.WaterRenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Water extends Mesh
{
    private float moveFactor;
    private float waveSpeed;

    public Water(Texture distortionMap, Texture normalMap)
    {
        super(Primitive.QUAD);
        setRenderer(new WaterRenderer(this));
        setCullType(CullType.INSIDE);
        entityTypeName = "Water";
        Material waterMat = new Material();
        waterMat.setTexture("distortionMap", distortionMap);
        waterMat.setTexture("normalMap", normalMap);
        setMaterial(waterMat);
        moveFactor = 0;
        waveSpeed  = 0.08f;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        moveFactor += waveSpeed * delta;
    }

    @Override
    public void render()
    {
        switch(getCullType())
        {
            case NONE:
                glDisable(GL_CULL_FACE);
                break;
            case INSIDE:
                glEnable(GL_CULL_FACE);
                glCullFace(GL_BACK);
                break;
            case OUTSIDE:
                glEnable(GL_CULL_FACE);
                glCullFace(GL_FRONT);
                break;
        }

        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, getIboID());
        glDrawElements(GL_TRIANGLES, getSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
    }

    public float getWaveSpeed()  { return waveSpeed;  }
    public float getMoveFactor() { return moveFactor; }

    public void setWaveSpeed(float waveSpeed) { this.waveSpeed = waveSpeed; }
}
