package engine.rendering;

import engine.core.math.Vector2f;
import engine.core.math.Vector3f;

public class Vertex
{
    public static final int SIZE = 14;

    private Vector3f position;
    private Vector2f texCoord;
    private Vector3f normal;
    private Vector3f tangent;
    private Vector3f color;

    public Vertex(Vector3f position) { this(position, new Vector2f()); }

    public Vertex(Vector3f position, Vector2f texCoord) { this(position, texCoord, new Vector3f()); }

    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) { this(position, texCoord, normal, new Vector3f()); }

    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal, Vector3f tangent) { this(position, texCoord, normal, tangent, new Vector3f(1, 1, 1)); }

    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal, Vector3f tangent, Vector3f color)
    {
        this.position = position;
        this.texCoord = texCoord;
        this.normal   = normal;
        this.tangent  = tangent;
        this.color    = color;
    }

    public Vector3f getPosition() { return position; }
    public Vector2f getTexCoord() { return texCoord; }
    public Vector3f getNormal()   { return normal;   }
    public Vector3f getTangent()  { return tangent;  }
    public Vector3f getColor()    { return color;    }

    public void setPosition(Vector3f position) { this.position = position; }
    public void setTexCoord(Vector2f texCoord) { this.texCoord = texCoord; }
    public void setNormal(Vector3f normal)     { this.normal   = normal;   }
    public void setTangent(Vector3f tangent)   { this.tangent  = tangent;  }
    public void setColor(Vector3f color)       { this.color    = color;    }
}
