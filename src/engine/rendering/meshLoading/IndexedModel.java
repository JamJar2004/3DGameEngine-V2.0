package engine.rendering.meshLoading;


import engine.core.math.*;

import java.util.ArrayList;

public class IndexedModel
{
    private ArrayList<Vector3f> positions;
    private ArrayList<Vector2f> texCoords;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector3f> tangents;
    private ArrayList<Vector3f> colors;
    private ArrayList<Integer>  indices;

    public IndexedModel()
    {
        positions = new ArrayList<>();
        texCoords = new ArrayList<>();
        normals   = new ArrayList<>();
        tangents  = new ArrayList<>();
        colors    = new ArrayList<>();
        indices   = new ArrayList<>();
    }

    public void calcNormals()
    {
        for(int i = 0; i < indices.size(); i += 3)
        {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector3f v1 = positions.get(i0).subtract(positions.get(i1));
            Vector3f v2 = positions.get(i0).subtract(positions.get(i2));

            Vector3f normal = v1.cross(v2).normalize();

            normals.get(i0).set(normals.get(i0).add(normal));
            normals.get(i1).set(normals.get(i1).add(normal));
            normals.get(i2).set(normals.get(i2).add(normal));
        }

        for(int i = 0; i < normals.size(); i++)
            normals.get(i).set(normals.get(i).normalize());
    }

    public void calcTangents()
    {
        for(int i = 0; i < indices.size(); i += 3)
        {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector3f edge1 = positions.get(i1).subtract(positions.get(i0));
            Vector3f edge2 = positions.get(i2).subtract(positions.get(i0));

            float deltaU1 = texCoords.get(i1).getX() - texCoords.get(i0).getX();
            float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();
            float deltaU2 = texCoords.get(i2).getX() - texCoords.get(i0).getX();
            float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();

            float dividend = (deltaU1*deltaV2 - deltaU2*deltaV1);
            //TODO: The first 0.0f may need to be changed to 1.0f here.
            float f = dividend == 0 ? 0.0f : 1.0f/dividend;

            Vector3f tangent = new Vector3f(0,0,0);
            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

            tangents.get(i0).set(tangents.get(i0).add(tangent));
            tangents.get(i1).set(tangents.get(i1).add(tangent));
            tangents.get(i2).set(tangents.get(i2).add(tangent));
        }

        for(int i = 0; i < tangents.size(); i++)
            tangents.get(i).set(tangents.get(i).normalize());
    }

    public ArrayList<Vector3f> getPositions()
    {
        return positions;
    }

    public ArrayList<Vector2f> getTexCoords()
    {
        return texCoords;
    }

    public ArrayList<Vector3f> getNormals()
    {
        return normals;
    }

    public ArrayList<Vector3f> getTangents()
    {
        return tangents;
    }

    public ArrayList<Vector3f> getColors()
    {
        return colors;
    }

    public ArrayList<Integer>  getIndices()
    {
        return indices;
    }
}
