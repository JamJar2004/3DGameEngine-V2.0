package engine.core.math;

public class Vector2f
{
    private float x;
    private float y;

    public Vector2f() { this(0, 0); }

    public Vector2f(Vector2f position)
    {
        set(position);
    }

    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2f(float value) { this(value, value); }

    public float length() { return (float)Math.sqrt(x * x + y * y); }

    public Vector2f normalize()
    {
        float length = length();
        return new Vector2f(x / length, y / length);
    }

    public float cross(Vector2f other) { return x * other.getY() + y * other.getX(); }

    public float dot(Vector2f other) { return x * other.getX() + y * other.getY(); }

    public Vector2f rotate(float angle)
    {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vector2f((float)(x * cos - y * sin), (float)(x * sin + y * cos));
    }

    public Vector2f invert() { return new Vector2f(-x, -y); }

    public Vector2f abs() { return new Vector2f(Math.abs(x), Math.abs(y)); }

    public float maxValue() { return Math.max(x, y); }

    public float minValue() { return Math.min(x, y); }

    public Vector2f add(Vector2f other) { return new Vector2f(x + other.getX(), y + other.getY()); }

    public Vector2f lerp(Vector2f dest, float lerpFactor) { return dest.subtract(this).multiply(lerpFactor).add(this); }

    public Vector2f add(float value) { return new Vector2f(x + value, y + value); }

    public Vector2f subtract(Vector2f other) { return new Vector2f(x - other.getX(), y - other.getY()); }

    public Vector2f subtract(float value) { return new Vector2f(x - value, y - value); }

    public Vector2f multiply(Vector2f other) { return new Vector2f(x * other.getX(), y * other.getY()); }

    public Vector2f multiply(float value) { return new Vector2f(x * value, y * value); }

    public Vector2f divide(Vector2f other) { return new Vector2f(x / other.getX(), y / other.getY()); }

    public Vector2f divide(float value) { return new Vector2f(x / value, y / value); }

    public String toString() { return "(" + x + ", " + y + ")"; }

    public float getX() { return x; }
    public float getY() { return y; }

    public Vector2f set(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f other)
    {
        set(other.getX(), other.getY());
        return this;
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public Vector2f flipXY() { return new Vector2f(y, x); }

    public boolean equals(Object obj)
    {
        Vector2f other = (Vector2f)obj;
        return x == other.getX() && y == other.getY();
    }

    public Vector2f clone()
    {
        return new Vector2f(x, y);
    }
}
