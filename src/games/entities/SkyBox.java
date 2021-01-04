package games.entities;

import engine.rendering.CubeMap;
import engine.rendering.Material;
import engine.rendering.Vertex;
import engine.rendering.renderers.SkyBoxRenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class SkyBox extends Mesh
{
    public SkyBox(CubeMap cubeMap)
    {
        super(Primitive.CUBE);
        setRenderer(new SkyBoxRenderer(this));
        setCullType(CullType.OUTSIDE);
        entityTypeName = "SkyBox";
        Material skyMat = new Material();
        skyMat.setCubeMap("cubeMap", cubeMap);
        setMaterial(skyMat);
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
}
