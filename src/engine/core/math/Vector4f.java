package engine.core.math;

public class Vector4f
{
    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public Vector4f(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float Length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float max()
    {
        return Math.max(Math.max(x, y), Math.max(z, w));
    }

    public float min()
    {
        return Math.max(Math.max(x, y), Math.max(z, w));
    }

    public float dot(Vector4f other)
    {
        return x * other.getX() + y * other.getY() + z * other.getZ() + w * other.getW();
    }

    public Vector4f cross(Vector4f other)
    {
        float x_ = y * other.getZ() - z * other.getY();
        float y_ = z * other.getX() - x * other.getZ();
        float z_ = x * other.getY() - y * other.getX();

        return new Vector4f(x_, y_, z_, 0);
    }

    public Vector4f normalize()
    {
        float length = Length();

        return new Vector4f(x / length, y / length, z / length, w / length);
    }

    public Vector4f Rotate(Vector4f axis, float angle)
    {
        float sinAngle = (float)Math.sin(-angle);
        float cosAngle = (float)Math.cos(-angle);

        return this.cross(axis.multiply(sinAngle)).add(           //Rotation on local X
                (this.multiply(cosAngle)).add(                     //Rotation on local Z
                        axis.multiply(this.dot(axis.multiply(1 - cosAngle))))); //Rotation on local Y
    }


    public Vector4f lerp(Vector4f dest, float lerpFactor)
    {
        return dest.subtract(this).multiply(lerpFactor).add(this);
    }

    public Vector4f add(Vector4f other)
    {
        return new Vector4f(x + other.getX(), y + other.getY(), z + other.getZ(), w + other.getW());
    }

    public Vector4f add(float other)
    {
        return new Vector4f(x + other, y + other, z + other, w + other);
    }

    public Vector4f subtract(Vector4f other)
    {
        return new Vector4f(x - other.getX(), y - other.getY(), z - other.getZ(), w - other.getW());
    }

    public Vector4f subtract(float r)
    {
        return new Vector4f(x - r, y - r, z - r, w - r);
    }

    public Vector4f multiply(Vector4f other)
    {
        return new Vector4f(x * other.getX(), y * other.getY(), z * other.getZ(), w * other.getW());
    }

    public Vector4f multiply(float r)
    {
        return new Vector4f(x * r, y * r, z * r, w * r);
    }

    public Vector4f divide(Vector4f other)
    {
        return new Vector4f(x / other.getX(), y / other.getY(), z / other.getZ(), w / other.getW());
    }

    public Vector4f divide(float r)
    {
        return new Vector4f(x / r, y / r, z / r, w / r);
    }

    public Vector4f abs()
    {
        return new Vector4f(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
    }

    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getW()
    {
        return w;
    }

    public boolean equals(Vector4f other)
    {
        return x == other.getX() && y == other.getY() && z == other.getZ() && w == other.getW();
    }
}
