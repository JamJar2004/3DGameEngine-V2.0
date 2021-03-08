/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/*
 * This code has been brought from thebennybox 3D Game Engine tutorial series.
 * I added a clone method and some inverting methods.
 */

package engine.core.math;

public class Quaternion
{
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion()
    {
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vector3f eulerRotation)
    {
        float pitch = eulerRotation.getX();
        float yaw   = eulerRotation.getY();
        float roll  = eulerRotation.getZ();

        x = (float) (Math.sin(roll/2) * Math.cos(pitch/2) * Math.cos(yaw/2) - Math.cos(roll/2) * Math.sin(pitch/2) * Math.sin(yaw/2));
        y = (float) (Math.cos(roll/2) * Math.sin(pitch/2) * Math.cos(yaw/2) + Math.sin(roll/2) * Math.cos(pitch/2) * Math.sin(yaw/2));
        z = (float) (Math.cos(roll/2) * Math.cos(pitch/2) * Math.sin(yaw/2) - Math.sin(roll/2) * Math.sin(pitch/2) * Math.cos(yaw/2));
        w = (float) (Math.cos(roll/2) * Math.cos(pitch/2) * Math.cos(yaw/2) + Math.sin(roll/2) * Math.sin(pitch/2) * Math.sin(yaw/2));
    }

    public Quaternion(Vector3f axis, float angle)
    {
        float rad = (float)Math.toRadians(angle);
        float sinHalfAngle = (float)Math.sin(rad / 2);
        float cosHalfAngle = (float)Math.cos(rad / 2);

        this.x = axis.getX() * sinHalfAngle;
        this.y = axis.getY() * sinHalfAngle;
        this.z = axis.getZ() * sinHalfAngle;
        this.w = cosHalfAngle;
    }

    //From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
    public Quaternion(Matrix4f rotation)
    {
        float trace = rotation.get(0, 0) + rotation.get(1, 1) + rotation.get(2, 2);

        if(trace > 0)
        {
            float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
            w = 0.25f / s;
            x = (rotation.get(1, 2) - rotation.get(2, 1)) * s;
            y = (rotation.get(2, 0) - rotation.get(0, 2)) * s;
            z = (rotation.get(0, 1) - rotation.get(1, 0)) * s;
        }
        else
        {
            if(rotation.get(0, 0) > rotation.get(1, 1) && rotation.get(0, 0) > rotation.get(2, 2))
            {
                float s = 2.0f * (float)Math.sqrt(1.0f + rotation.get(0, 0) - rotation.get(1, 1) - rotation.get(2, 2));
                w = (rotation.get(1, 2) - rotation.get(2, 1)) / s;
                x = 0.25f * s;
                y = (rotation.get(1, 0) + rotation.get(0, 1)) / s;
                z = (rotation.get(2, 0) + rotation.get(0, 2)) / s;
            }
            else if(rotation.get(1, 1) > rotation.get(2, 2))
            {
                float s = 2.0f * (float)Math.sqrt(1.0f + rotation.get(1, 1) - rotation.get(0, 0) - rotation.get(2, 2));
                w = (rotation.get(2, 0) - rotation.get(0, 2)) / s;
                x = (rotation.get(1, 0) + rotation.get(0, 1)) / s;
                y = 0.25f * s;
                z = (rotation.get(2, 1) + rotation.get(1, 2)) / s;
            }
            else
            {
                float s = 2.0f * (float)Math.sqrt(1.0f + rotation.get(2, 2) - rotation.get(0, 0) - rotation.get(1, 1));
                w = (rotation.get(0, 1) - rotation.get(1, 0) ) / s;
                x = (rotation.get(2, 0) + rotation.get(0, 2) ) / s;
                y = (rotation.get(1, 2) + rotation.get(2, 1) ) / s;
                z = 0.25f * s;
            }
        }

        float length = (float)Math.sqrt(x * x + y * y + z * z + w * w);
        x /= length;
        y /= length;
        z /= length;
        w /= length;
    }

    public float length() { return (float)Math.sqrt(x * x + y * y + z * z + w * w); }

    public Quaternion normalize()
    {
        float length = length();
        return new Quaternion(x / length, y / length, z / length, w / length);
    }

    public Quaternion conjugate() { return new Quaternion(-x, -y, -z, w); }

    public Quaternion add(Quaternion other) { return new Quaternion(x + other.getX(), y + other.getY(), z + other.getZ(), w + other.getW()); }

    public Quaternion subtract(Quaternion other) { return new Quaternion(x - other.getX(), y - other.getY(), z - other.getZ(), w - other.getW()); }

    public Quaternion turn(Vector3f axis, float angle) { return new Quaternion(axis, angle).multiply(this).normalize(); }

    public Matrix4f toRotationMatrix()
    {
        Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
        Vector3f up      = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
        Vector3f right   = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

        return new Matrix4f().initRotation(forward, up, right);
    }

    public Quaternion invertX() { return new Quaternion(-x, y, z, w); }
    public Quaternion invertY() { return new Quaternion(x, -y, z, w); }
    public Quaternion invertZ() { return new Quaternion(x, y, -z, w); }
    public Quaternion invertW() { return new Quaternion(x, y, z, -w); }

    public Vector3f toEuler()
    {
        float pitch = (float)Math.atan2(2 * x * w - 2 * y * z, 1 - 2 * x * x - 2 * z * z);
        float yaw   = (float)Math.atan2(2 * y * w - 2 * x * z, 1 - 2 * y * y - 2 * z * z);
        float roll  = (float)Math.asin( 2 * x * y + 2 * z * w);

        return new Vector3f(pitch, yaw, roll).toDegrees();
    }

    public Vector3f getFront() { return new Vector3f( 0,  0,  1).rotate(this); }

    public Vector3f getBack()    { return new Vector3f( 0,  0, -1).rotate(this); }

    public Vector3f getUp()      { return new Vector3f( 0,  1,  0).rotate(this); }

    public Vector3f getDown()    { return new Vector3f( 0, -1,  0).rotate(this); }

    public Vector3f getRight()   { return new Vector3f( 1,  0,  0).rotate(this); }

    public Vector3f getLeft()    { return new Vector3f(-1,  0,  0).rotate(this); }

    public float dot(Quaternion other) { return x * other.getX() + y * other.getY() + z * other.getZ() + w * other.getW(); }

    public Quaternion multiply(float value) { return new Quaternion(x * value, y * value, z * value, w * value); }

    public Quaternion multiply(Quaternion other)
    {
        float nw = w * other.getW() - x * other.getX() - y * other.getY() - z * other.getZ();
        float nx = x * other.getW() + w * other.getX() + y * other.getZ() - z * other.getY();
        float ny = y * other.getW() + w * other.getY() + z * other.getX() - x * other.getZ();
        float nz = z * other.getW() + w * other.getZ() + x * other.getY() - y * other.getX();

        return new Quaternion(nx, ny, nz, nw);
    }

    public Quaternion multiply(Vector3f vector)
    {
        float nw = -x * vector.getX() - y * vector.getY() - z * vector.getZ();
        float nx =  w * vector.getX() + y * vector.getZ() - z * vector.getY();
        float ny =  w * vector.getY() + z * vector.getX() - x * vector.getZ();
        float nz =  w * vector.getZ() + x * vector.getY() - y * vector.getX();

        return new Quaternion(nx, ny, nz, nw);
    }

    public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest)
    {
        Quaternion correctedDest = dest;

        if(shortest && this.dot(dest) < 0)
            correctedDest = new Quaternion(-dest.getX(), - dest.getY(), -dest.getZ(), -dest.getW());

        return correctedDest.subtract(this).multiply(lerpFactor).add(this).normalize();
    }

    public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest)
    {
        float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctDest = dest;

        if(shortest && cos < 0)
        {
            cos = -cos;
            correctDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), - dest.getW());
        }

        if(Math.abs(cos) >= 1 - EPSILON)
            return nlerp(correctDest, lerpFactor, false);

        float sin = (float)Math.sqrt(1.0f - cos * cos);
        float angle = (float)Math.atan2(sin, cos);
        float invSin = 1.0f / sin;

        float srcFactor = (float)Math.sin((1.0f - lerpFactor) * angle) * invSin;
        float destFactor = (float)Math.sin((lerpFactor) * angle) * invSin;

        return this.multiply(srcFactor).add(correctDest.multiply(destFactor));
    }

    public Vector3f getXYZ() { return new Vector3f(x, y, z); }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getW() { return w; }

    public Quaternion set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternion set(Quaternion other)
    {
        set(other.getX(), other.getY(), other.getZ(), other.getW());
        return this;
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }
    public void setW(float w) { this.w = w; }

    public boolean equals(Object obj)
    {
        Quaternion other = (Quaternion)obj;
        return x == other.getX() && y == other.getY() && z == other.getZ() && w == other.getW();
    }

    public Quaternion clone() { return new Quaternion(x, y, z, w); }
}
