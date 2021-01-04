package games.entities;

import engine.core.Util;
import engine.core.math.Vector2f;
import engine.core.math.Vector3f;
import engine.rendering.Material;
import engine.rendering.Vertex;
import engine.rendering.meshLoading.IndexedModel;
import engine.rendering.meshLoading.OBJModel;
import engine.rendering.meshLoading.XModel;
import engine.rendering.renderers.MeshRenderer;
import games.components.EntityComponent;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh extends Entity
{
    private static HashMap<String, Mesh> loadedModels = new HashMap<>();
    private static HashMap<String, Mesh> primitives   = new HashMap<>();

    private int vboID;
    private int iboID;
    private int size;

    private Vector3f minPosition;
    private Vector3f maxPosition;

    private Material material;
    private CullType cullType;

    public enum CullType
    {
        NONE, INSIDE, OUTSIDE
    }

    protected Mesh()
    {
        super();
        entityTypeName = "Mesh";
        init();
    }

    public Mesh(Primitive primitive)
    {
        this(primitive, 60);
    }

    public Mesh(Primitive primitive, int quality)
    {
        super();
        quality = Math.max(3, quality);
        entityTypeName = "Mesh";
        init();
        switch(primitive)
        {
            case QUAD:
                createQuad();
                break;
            case ELLIPSE:
                createEllipse(quality);
                break;
            case CUBE:
                createCube();
                break;
            case SPHERE:
                createSphere(quality);
                break;
            case PYRAMID:
                createPyramid();
                break;
            case CONE:
                createCone(quality);
                break;
            case CYLINDER:
                createCylinder(quality);
                break;
            default:
                System.err.println("This mesh primitive does not exist.");
                new Exception().printStackTrace();
                System.exit(1);
        }
    }

    public Mesh(String fileName)
    {
        super();
        entityTypeName = "Mesh";
        init();
        loadMesh(fileName);
    }

    public Mesh(Vertex[] vertices, int[] indices)
    {
        super();
        entityTypeName = "Mesh";
        init();
        addVertices(vertices, indices, true, true);
    }

    private void init()
    {
        minPosition = new Vector3f(Float.MAX_VALUE);
        maxPosition = new Vector3f(Float.MIN_VALUE);

        vboID = glGenBuffers();
        iboID = glGenBuffers();
        size  = 0;

        cullType = CullType.INSIDE;

        material = new Material();
        setRenderer(new MeshRenderer(this));
    }

    public enum Primitive
    {
        QUAD, ELLIPSE, CUBE, SPHERE, PYRAMID, CONE, CYLINDER
    }

    private void createQuad()
    {
        if(primitives.containsKey("quad"))
        {
            Mesh primitive = primitives.get("quad");
            vboID    = primitive.getVboID();
            iboID    = primitive.getIboID();
            size     = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {
            Vertex[] vertices = new Vertex[]{new Vertex(new Vector3f(-1, 0, -1), new Vector2f(0, 0)),
                                             new Vertex(new Vector3f( 1, 0, -1), new Vector2f(1, 0)),
                                             new Vertex(new Vector3f(-1, 0,  1), new Vector2f(0, 1)),
                                             new Vertex(new Vector3f( 1, 0,  1), new Vector2f(1, 1))};

            int[] indices = new int[]{2, 1, 0,
                    1, 2, 3};

            addVertices(vertices, indices, true, true);
        }
    }

    private void createEllipse(int quality)
    {
        if(primitives.containsKey("circle"))
        {
            Mesh primitive = primitives.get("circle");
            vboID       = primitive.getVboID();
            iboID       = primitive.getIboID();
            size        = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {
            Vertex[] vertices = new Vertex[quality + 1];

            vertices[0] = new Vertex(new Vector3f(0, 0, 0));

            int i = 1;
            for (int a = 0; a < 360; a += 360 / quality)
            {
                float x = (float) Math.cos(Math.toRadians(a));
                float z = (float) Math.sin(Math.toRadians(a));

                vertices[i] = new Vertex(new Vector3f(x, 0, z), new Vector2f(x, z));
                i++;
            }

            int[] indices = new int[vertices.length * 3];

            int k = 0;
            for (int j = 2; j < vertices.length; j++)
            {
                int v0 = j;
                int v1 = j + 1;

                if (v1 >= vertices.length)
                {
                    v1 = v1 - (vertices.length - 2);
                }

                indices[k + 0] = v1;
                indices[k + 1] = v0;
                indices[k + 2] = 0;

                k += 3;
            }

            addVertices(vertices, indices, true, true);
        }
    }

    private void createCube()
    {
        if(primitives.containsKey("cube"))
        {
            Mesh primitive = primitives.get("cube");
            vboID    = primitive.getVboID();
            iboID    = primitive.getIboID();
            size     = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {
            Vertex[] vertices = new Vertex[]{ new Vertex(new Vector3f(-1, -1, -1), new Vector2f(0, 0)),
                                              new Vertex(new Vector3f( 1, -1, -1), new Vector2f(1, 0)),
                                              new Vertex(new Vector3f(-1,  1, -1), new Vector2f(0, 1)),
                                              new Vertex(new Vector3f( 1,  1, -1), new Vector2f(1, 1)),
                                              new Vertex(new Vector3f(-1, -1,  1), new Vector2f(1, 0)),
                                              new Vertex(new Vector3f( 1, -1,  1), new Vector2f(0, 0)),
                                              new Vertex(new Vector3f(-1,  1,  1), new Vector2f(1, 1)),
                                              new Vertex(new Vector3f( 1,  1,  1), new Vector2f(0, 1)) };


            int[] indices = new int[]{ 0, 2, 3,
                                       3, 1, 0,
                                       6, 4, 5,
                                       7, 6, 5,
                                       1, 3, 7,
                                       7, 5, 1,
                                       4, 6, 2,
                                       0, 4, 2,
                                       3, 2, 6,
                                       6, 7, 3,
                                       5, 4, 0,
                                       0, 1, 5 };

            addVertices(vertices, indices, true, true);
        }
    }

    private void createPyramid()
    {
        if(primitives.containsKey("pyramid"))
        {
            Mesh primitive = primitives.get("pyramid");
            vboID    = primitive.getVboID();
            iboID    = primitive.getIboID();
            size     = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {
            Vertex[] vertices = new Vertex[]{ new Vertex(new Vector3f( 0,  1,  0), new Vector2f(0.5f, 1)),
                                              new Vertex(new Vector3f(-1, -1, -1), new Vector2f(0   , 0)),
                                              new Vertex(new Vector3f( 1, -1, -1), new Vector2f(1   , 0)),
                                              new Vertex(new Vector3f(-1, -1,  1), new Vector2f(1   , 0)),
                                              new Vertex(new Vector3f( 1, -1,  1), new Vector2f(0   , 0)) };

            int[] indices = new int[]{ 0, 2, 1,
                                       0, 1, 3,
                                       3, 4, 0,
                                       0, 4, 2,
                                       4, 3, 1,
                                       1, 2, 4 };

            addVertices(vertices, indices, true, true);
        }
    }

    private void createSphere(int quality)
    {
        if(primitives.containsKey("sphere"))
        {
            Mesh primitive = primitives.get("sphere");
            vboID    = primitive.getVboID();
            iboID    = primitive.getIboID();
            size     = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {

            int stackCount = quality;
            int sectorCount = quality;

            ArrayList<Vertex> vertexList = new ArrayList<>();

            float PI = (float) Math.PI;

            float x, y, z, xy;                     // vertex position
            float nx, ny, nz, lengthInv = 1.0f;    // vertex normal
            float s, t;                            // vertex texCoord

            float sectorStep = 2 * PI / sectorCount;
            float stackStep = PI / stackCount;
            float sectorAngle, stackAngle;

            for (int i = 0; i <= stackCount; ++i)
            {
                stackAngle = PI / 2 - i * stackStep;        // starting from pi/2 to -pi/2
                xy = (float) Math.cos(stackAngle);             // r * cos(u)
                z = (float) Math.sin(stackAngle);              // r * sin(u)

                // add (sectorCount+1) vertices per stack
                // the first and last vertices have same position and normal, but different tex coords
                for (int j = 0; j <= sectorCount; ++j)
                {
                    sectorAngle = j * sectorStep;           // starting from 0 to 2pi

                    // vertex position (x, y, z)
                    x = xy * (float) Math.cos(sectorAngle);             // r * cos(u) * cos(v)
                    y = xy * (float) Math.sin(sectorAngle);             // r * cos(u) * sin(v)

                    // normalized vertex normal (nx, ny, nz)
                    nx = x * lengthInv;
                    ny = y * lengthInv;
                    nz = z * lengthInv;


                    // vertex tex coord (s, t) range between [0, 1]
                    s = (float) j / sectorCount;
                    t = (float) i / stackCount;
                    //					texCoords.push_back(s);
                    //					texCoords.push_back(t);

                    vertexList.add(new Vertex(new Vector3f(x, y, z), new Vector2f(s, t), new Vector3f(nx, ny, nz)));
                }
            }

            ArrayList<Integer> indexList = new ArrayList<Integer>();

            int k1, k2;
            for (int i = 0; i < stackCount; ++i)
            {
                k1 = i * (sectorCount + 1);     // beginning of current stack
                k2 = k1 + sectorCount + 1;      // beginning of next stack

                for (int j = 0; j < sectorCount; ++j, ++k1, ++k2)
                {
                    // 2 triangles per sector excluding first and last stacks
                    // k1 => k2 => k1+1
                    if (i != 0)
                    {
                        indexList.add(k1);
                        indexList.add(k2);
                        indexList.add(k1 + 1);
                    }

                    // k1+1 => k2 => k2+1
                    if (i != (stackCount - 1))
                    {
                        indexList.add(k1 + 1);
                        indexList.add(k2);
                        indexList.add(k2 + 1);
                    }
                }
            }

            Vertex[] vertices = new Vertex[vertexList.size()];
            vertexList.toArray(vertices);

            Integer[] indices = new Integer[indexList.size()];
            indexList.toArray(indices);

            addVertices(vertices, Util.toIntArray(indices), false, true);
        }
    }

    private void createCone(int quality)
    {
        if(primitives.containsKey("cone"))
        {
            Mesh primitive = primitives.get("cone");
            vboID    = primitive.getVboID();
            iboID    = primitive.getIboID();
            size     = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {
            Vertex[] vertices = new Vertex[quality + 2];

            vertices[0] = new Vertex(new Vector3f(0, 1, 0));
            vertices[1] = new Vertex(new Vector3f(0, -1, 0));

            int i = 2;
            for (int a = 0; a < 360; a += 360 / quality)
            {
                float x = (float) Math.cos(Math.toRadians(a));
                float z = (float) Math.sin(Math.toRadians(a));

                vertices[i] = new Vertex(new Vector3f(x, -1, z));
                i++;
            }

            int[] indices = new int[quality * 6];

            int k = 0;
            for (int j = 2; j < vertices.length; j++)
            {
                int v0 = j;
                int v1 = j + 1;

                if (v1 >= vertices.length)
                {
                    v1 = v1 - (vertices.length - 2);
                }

                indices[k + 0] = 1;
                indices[k + 1] = v0;
                indices[k + 2] = v1;

                indices[k + 3] = v1;
                indices[k + 4] = v0;
                indices[k + 5] = 0;

                k += 6;
            }

            addVertices(vertices, indices, true, true);
        }
    }

    private void createCylinder(int quality)
    {
        if(primitives.containsKey("cylinder"))
        {
            Mesh primitive = primitives.get("cylinder");
            vboID    = primitive.getVboID();
            iboID    = primitive.getIboID();
            size     = primitive.getSize();
            minPosition = primitive.getMinPosition();
            maxPosition = primitive.getMaxPosition();
        }
        else
        {
            Vertex[] vertices = new Vertex[quality * 2 + 2];

            vertices[0] = new Vertex(new Vector3f(0, 1, 0));
            vertices[1] = new Vertex(new Vector3f(0, -1, 0));

            int i = 2;
            for (int a = 0; a < 360; a += 360 / quality)
            {
                float x = (float) Math.cos(Math.toRadians(a));
                float z = (float) Math.sin(Math.toRadians(a));

                vertices[i] = new Vertex(new Vector3f(x, 1, z));
                vertices[i + 1] = new Vertex(new Vector3f(x, -1, z));
                i += 2;
            }

            int[] indices = new int[quality * 12];

            int k = 0;
            for (int j = 2; j < vertices.length; j += 2)
            {
                int v0 = j;
                int v1 = j + 1;
                int v2 = j + 2;
                int v3 = j + 3;

                if (v3 >= vertices.length)
                {
                    v3 = v3 - (vertices.length - 2);
                }

                if (v2 >= vertices.length)
                {
                    v2 = v2 - (vertices.length - 2);
                }

                if (v1 >= vertices.length)
                {
                    v1 = v1 - (vertices.length - 3);
                }

                indices[k] = v2;
                indices[k + 1] = v0;
                indices[k + 2] = 0;

                indices[k + 3] = 1;
                indices[k + 4] = v1;
                indices[k + 5] = v3;

                indices[k + 6] = v2;
                indices[k + 7] = v1;
                indices[k + 8] = v0;

                indices[k + 9] = v2;
                indices[k + 10] = v3;
                indices[k + 11] = v1;

                k += 12;
            }

            addVertices(vertices, indices, true, true);
        }
    }

    protected void loadMesh(String fileName)
    {
        if(loadedModels.containsKey(fileName))
        {
            Mesh loadedModel = loadedModels.get(fileName);
            vboID       = loadedModel.getVboID();
            iboID       = loadedModel.getIboID();
            size        = loadedModel.getSize();
            cullType    = loadedModel.getCullType();
            material    = loadedModel.getMaterial();
            minPosition = loadedModel.getMinPosition();
            maxPosition = loadedModel.getMaxPosition();
        }
        else
        {
            if(fileName.endsWith(".obj"))
            {
                OBJModel loadedModel = new OBJModel("res/models/" + fileName);
                IndexedModel indexedModel = loadedModel.toIndexedModel();

                addVerticesFromIndexedModel(indexedModel);

                loadedModels.put(fileName, this);
            }
            else if(fileName.endsWith(".x"))
            {
                XModel loadedModel = new XModel("res/models/" + fileName);
                IndexedModel indexedModel = loadedModel.toIndexedModel();

                addVerticesFromIndexedModel(indexedModel);

                loadedModels.put(fileName, this);
            }
            else
            {
                System.err.println("The format" + fileName.substring(fileName.indexOf(".")) + "is not supported.");
                new Exception().printStackTrace();
                System.exit(1);
            }
        }
    }

    private void addVerticesFromIndexedModel(IndexedModel indexedModel)
    {
        Vertex[] vertices = new Vertex[indexedModel.getPositions().size()];

        Integer[] indices = new Integer[indexedModel.getIndices().size()];
        indexedModel.getIndices().toArray(indices);

        for (int i = 0; i < indexedModel.getPositions().size(); i++)
        {
            vertices[i] = new Vertex(indexedModel.getPositions().get(i),
                                     indexedModel.getTexCoords().get(i),
                                     indexedModel.getNormals().get(i),
                                     indexedModel.getTangents().get(i),
                                     indexedModel.getColors().get(i));
        }

        addVertices(vertices, Util.toIntArray(indices), false, false);
    }

    protected void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents)
    {
        if(calcNormals)
            calcNormals(vertices, indices, calcTangents);

        for(Vertex vertex : vertices)
        {
            minPosition.setX(Math.min(vertex.getPosition().getX(), minPosition.getX()));
            minPosition.setY(Math.min(vertex.getPosition().getY(), minPosition.getY()));
            minPosition.setZ(Math.min(vertex.getPosition().getZ(), minPosition.getZ()));

            maxPosition.setX(Math.max(vertex.getPosition().getX(), maxPosition.getX()));
            maxPosition.setY(Math.max(vertex.getPosition().getY(), maxPosition.getY()));
            maxPosition.setZ(Math.max(vertex.getPosition().getZ(), maxPosition.getZ()));
        }

        size  = indices.length;

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
    }

    public void calcNormals(Vertex[] vertices, int[] indices, boolean calcTangents)
    {
        for(int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i    ];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3f v1 = vertices[i1].getPosition().subtract(vertices[i0].getPosition());
            Vector3f v2 = vertices[i2].getPosition().subtract(vertices[i0].getPosition());

            Vector3f normal = v1.cross(v2);

            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }

        for(int i = 0; i < vertices.length; i++)
            vertices[i].setNormal(vertices[i].getNormal().normalize());

        if(calcTangents)
            calcTangents(vertices, indices);
    }

    public void calcTangents(Vertex[] vertices, int[] indices)
    {
        for (int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3f edge1 = vertices[i1].getPosition().subtract(vertices[i0].getPosition());
            Vector3f edge2 = vertices[i2].getPosition().subtract(vertices[i0].getPosition());

            float deltaU1 = vertices[i1].getTexCoord().getX() - vertices[i0].getTexCoord().getX();
            float deltaV1 = vertices[i1].getTexCoord().getY() - vertices[i0].getTexCoord().getY();
            float deltaU2 = vertices[i2].getTexCoord().getX() - vertices[i0].getTexCoord().getX();
            float deltaV2 = vertices[i2].getTexCoord().getY() - vertices[i0].getTexCoord().getY();

            float dividend = (deltaU1 * deltaV2 - deltaU2 * deltaV1);
            //TODO: The first 0.0f may need to be changed to 1.0f here.
            float f = dividend == 0 ? 0.0f : 1.0f / dividend;

            Vector3f tangent = new Vector3f(0, 0, 0);
            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

            vertices[i0].getTangent().set(vertices[i0].getTangent().add(tangent));
            vertices[i1].getTangent().set(vertices[i1].getTangent().add(tangent));
            vertices[i2].getTangent().set(vertices[i2].getTangent().add(tangent));
        }
    }

    public void render()
    {
        switch(cullType)
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
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
        glVertexAttribPointer(4, 3, GL_FLOAT, false, Vertex.SIZE * 4, 44);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
    }

    @Override
    public Mesh clone()
    {
        Mesh result = new Mesh();
        result.setTransformation(getTransformation().clone());
        result.vboID = vboID;
        result.iboID = iboID;
        result.size  = size;
        result.setMaterial(material.clone());
        result.setCullType(cullType);
        result.entityTypeName = entityTypeName;

        for(EntityComponent component : getComponents())
        {
            result.addComponent(component.clone());
        }

        return result;
    }

    public int      getVboID()       { return vboID;    }
    public int      getIboID()       { return iboID;    }
    public int      getSize()        { return size;     }
    public Material getMaterial()    { return material; }
    public CullType getCullType()    { return cullType; }
    public Vector3f getMinPosition() { return minPosition; }
    public Vector3f getMaxPosition() { return maxPosition; }

    protected void setVboID(int vboID)
    {
        this.vboID = vboID;
    }

    protected void setIboID(int iboID)
    {
        this.iboID = iboID;
    }

    protected void setSize(int size)
    {
        this.size = size;
    }

    public void setCullType(CullType cullType) { this.cullType = cullType; }
    public void setMaterial(Material material) { this.material = material; }
}
