package engine.core.math;

public class Vector3f
{
    public static Vector3f FORWARD = new Vector3f( 0,  0,  1);
    public static Vector3f BACK    = new Vector3f( 0,  0, -1);
    public static Vector3f LEFT    = new Vector3f(-1,  0,  0);
    public static Vector3f RIGHT   = new Vector3f( 1,  0,  0);
    public static Vector3f UP      = new Vector3f( 0,  1,  0);
    public static Vector3f DOWN    = new Vector3f( 0, -1,  0);

    private float x;
    private float y;
    private float z;

    public Vector3f() { this(0, 0, 0); }

    public Vector3f(Vector3f position)
    {
        set(position);
    }

    public Vector3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(float value) { this(value, value, value); }

    public float length() { return (float)Math.sqrt(x * x + y * y + z * z); }

    public Vector3f normalize()
    {
        float length = length();
        return new Vector3f(x / length, y / length, z / length);
    }

    public float dot(Vector3f other) { return x * other.getX() + y * other.getY() + z * other.getZ(); }

    public Vector3f cross(Vector3f other)
    {
        float nx = y * other.getZ() - z * other.getY();
        float ny = z * other.getX() - x * other.getZ();
        float nz = x * other.getY() - y * other.getX();

        return new Vector3f(nx, ny, nz);
    }

    public Vector3f invert() { return new Vector3f(-x, -y, -z); }

    public Vector3f invertX() { return new Vector3f(-x,  y,  z); }
    public Vector3f invertY() { return new Vector3f( x, -y,  z); }
    public Vector3f invertZ() { return new Vector3f( x,  y, -z); }

    public Vector3f rotate(Vector3f axis, float angle)
    {
        float rad = (float)Math.toRadians(angle);
        float sinAngle = (float)Math.sin(-rad);
        float cosAngle = (float)Math.cos(-rad);

        return this.cross(axis.multiply(sinAngle)).add(           //Rotation on local X
                (this.multiply(cosAngle)).add(                     //Rotation on local Z
                        axis.multiply(this.dot(axis.multiply(1 - cosAngle))))); //Rotation on local Y
    }

    public Vector3f rotate(Quaternion rotation)
    {
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.multiply(this).multiply(conjugate);
        return w.getXYZ();
    }

    public Vector3f toRadians()
    {
        return new Vector3f((float)Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
    }

    public Vector3f toDegrees()
    {
        return new Vector3f((float)Math.toDegrees(x), (float)Math.toDegrees(y), (float)Math.toDegrees(z));
    }

    public Vector3f abs() { return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z)); }

    public Vector3f pow(float power) { return new Vector3f((float)Math.pow(x, power), (float)Math.pow(y, power), (float)Math.pow(z, power)); }

    public float maxValue() { return Math.max(x, Math.max(y, z)); }

    public float minValue() { return Math.min(x, Math.min(y, z)); }

    public Vector3f lerp(Vector3f dest, float lerpFactor) { return dest.subtract(this).multiply(lerpFactor).add(this); }

    public Vector3f add(Vector3f other) { return new Vector3f(x + other.getX(), y + other.getY(), z + other.getZ()); }

    public Vector3f add(float value) { return new Vector3f(x + value, y + value, z + value); }

    public Vector3f subtract(Vector3f other) { return new Vector3f(x - other.getX(), y - other.getY(), z - other.getZ()); }

    public Vector3f subtract(float value) { return new Vector3f(x - value, y - value, z - value); }

    public Vector3f multiply(Vector3f other) { return new Vector3f(x * other.getX(), y * other.getY(), z * other.getZ()); }

    public Vector3f multiply(float value) { return new Vector3f(x * value, y * value, z * value); }

    public Vector3f divide(Vector3f other) { return new Vector3f(x / other.getX(), y / other.getY(), z / other.getZ()); }

    public Vector3f divide(float value) { return new Vector3f(x / value, y / value, z / value); }

    public String toString()
    {
        return "(" + x + ", " + y + ", " + z +  ")";
    }

    public Vector2f getXY() { return new Vector2f(x, y); }
    public Vector2f getXZ() { return new Vector2f(x, z); }
    public Vector2f getYZ() { return new Vector2f(y, z); }

    public Vector2f getYX() { return new Vector2f(y, x); }
    public Vector2f getZX() { return new Vector2f(z, x); }
    public Vector2f getZY() { return new Vector2f(z, y); }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public Vector3f set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(Vector3f other)
    {
        set(other.getX(), other.getY(), other.getZ());
        return this;
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }

    public Vector3f flipXY() { return new Vector3f(y, x, z); }
    public Vector3f flipXZ() { return new Vector3f(z, y, x); }
    public Vector3f flipYZ() { return new Vector3f(x, z, y); }

    public boolean equals(Object obj)
    {
        Vector3f other = (Vector3f)obj;
        return x == other.getX() && y == other.getY() && z == other.getZ();
    }

    public Vector3f clone() { return new Vector3f(x, y, z); }
}
