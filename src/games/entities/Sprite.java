package games.entities;

import engine.core.math.*;
import engine.rendering.Texture;
import engine.rendering.Vertex;
import engine.rendering.renderers.SpriteRenderer;
import games.components.EntityComponent;

public class Sprite extends Mesh
{
    private Sprite() {}

    public Sprite(String fileName)
    {
        Vertex[] vertices = new Vertex[] {new Vertex(new Vector3f(-1, -1, 0), new Vector2f(0, 0)),
                                          new Vertex(new Vector3f( 1, -1, 0), new Vector2f(1, 0)),
                                          new Vertex(new Vector3f(-1,  1, 0), new Vector2f(0, 1)),
                                          new Vertex(new Vector3f( 1,  1, 0), new Vector2f(1, 1))};

        int[] indices = new int[] {2, 1, 0,
                                   1, 2, 3};

        addVertices(vertices, indices, false, false);

        entityTypeName = "Sprite";
        setRenderer(new SpriteRenderer(this));
        setContainsTransparency(true);
        setCullType(CullType.NONE);

        Texture texture = new Texture(fileName, true, false);
        float maxSide = Math.max((float)texture.getWidth(), (float)texture.getHeight());
        float width  = (float)texture.getWidth() / maxSide;
        float height = (float)texture.getHeight() / maxSide;
        getTransformation().setScale(width, height, 0);
        getMaterial().setTexture("texture", texture);
    }

    public void setTexture(Texture texture)
    {
        getMaterial().setTexture("texture", texture);
    }

    @Override
    public Sprite clone()
    {
        Sprite result = new Sprite();
        result.setMaterial(getMaterial().clone());
        result.setTransformation(getTransformation().clone());
        result.setCullType(getCullType());
        result.setVboID(getVboID());
        result.setIboID(getIboID());
        result.setSize(getSize());
        result.setContainsTransparency(getContainsTransparency());
        result.setRenderer(new SpriteRenderer(result));
        result.entityTypeName = entityTypeName;

        for(EntityComponent component : getComponents())
        {
            result.addComponent(component.clone());
        }

        return result;
    }
}
