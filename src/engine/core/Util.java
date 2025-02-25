package engine.core;

import engine.core.math.Matrix4f;
import engine.rendering.Vertex;
import org.lwjgl.BufferUtils;

import java.nio.*;
import java.util.ArrayList;

public class Util
{
    public static FloatBuffer createFloatBuffer(int size)
    {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size)
    {
        return BufferUtils.createIntBuffer(size);
    }

    public static ByteBuffer createByteBuffer(int size)
    {
        return BufferUtils.createByteBuffer(size);
    }

    public static IntBuffer createFlippedBuffer(int... values)
    {
        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vertex[] vertices)
    {
        FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);

        for (int i = 0; i < vertices.length; i++)
        {
            buffer.put(vertices[i].getPosition().getX());
            buffer.put(vertices[i].getPosition().getY());
            buffer.put(vertices[i].getPosition().getZ());

            buffer.put(vertices[i].getTexCoord().getX());
            buffer.put(vertices[i].getTexCoord().getY());

            buffer.put(vertices[i].getNormal().getX());
            buffer.put(vertices[i].getNormal().getY());
            buffer.put(vertices[i].getNormal().getZ());

            buffer.put(vertices[i].getTangent().getX());
            buffer.put(vertices[i].getTangent().getY());
            buffer.put(vertices[i].getTangent().getZ());

            buffer.put(vertices[i].getColor().getX());
            buffer.put(vertices[i].getColor().getY());
            buffer.put(vertices[i].getColor().getZ());
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Matrix4f value)
    {
        FloatBuffer buffer = createFloatBuffer(4 * 4);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                buffer.put(value.get(i, j));

        buffer.flip();

        return buffer;
    }

    public static String[] removeEmptyStrings(String[] data)
    {
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < data.length; i++)
            if (!data[i].equals(""))
                result.add(data[i]);

        String[] res = new String[result.size()];
        result.toArray(res);

        return res;
    }

    public static String[] removeWhiteSpaces(String[] data)
    {
        String[] result = new String[data.length];
        for(int i = 0; i < data.length; i++)
        {
            result[i] = removeWhiteSpaces(data[i]);
        }
        return result;
    }

    public static String removeWhiteSpaces(String str)
    {
        String result = "";
        for(int i = 0; i < str.length(); i++)
        {
            char currChar = str.charAt(i);
            if(!Character.isWhitespace(currChar))
                result = result + currChar;
        }
        return result;
    }

    public static int[] toIntArray(Integer[] data)
    {
        int[] result = new int[data.length];

        for (int i = 0; i < data.length; i++)
            result[i] = data[i].intValue();

        return result;
    }
}
