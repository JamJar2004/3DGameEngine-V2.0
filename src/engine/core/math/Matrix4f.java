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
 * I added the clone method myself.
 */

package engine.core.math;

public class Matrix4f
{
    private float[][] m;

    public Matrix4f() { m = new float[4][4]; }

    public Matrix4f initIdentity()
    {
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f initTranslation(Vector3f translation) { return initTranslation(translation.getX(), translation.getY(), translation.getZ()); }

    public Matrix4f initTranslation(float x, float y, float z)
    {
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = x;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = y;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = z;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f initScale(Vector3f scale) { return initScale(scale.getX(), scale.getY(), scale.getZ()); }

    public Matrix4f initScale(float width, float height, float depth)
    {
        m[0][0] = width; m[0][1] = 0;	   m[0][2] = 0;     m[0][3] = 0;
        m[1][0] = 0;     m[1][1] = height; m[1][2] = 0;	    m[1][3] = 0;
        m[2][0] = 0;     m[2][1] = 0;	   m[2][2] = depth;	m[2][3] = 0;
        m[3][0] = 0;     m[3][1] = 0;	   m[3][2] = 0;	    m[3][3] = 1;

        return this;
    }

    public Matrix4f initRotation(Vector3f rotation) { return initRotation(rotation.getX(), rotation.getY(), rotation.getZ()); }

    public Matrix4f initRotation(float x, float y, float z)
    {
        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();

        x = (float)Math.toRadians(x);
        y = (float)Math.toRadians(y);
        z = (float)Math.toRadians(z);

        rz.m[0][0] = (float)Math.cos(z); rz.m[0][1] = -(float)Math.sin(z); rz.m[0][2] = 0;				     rz.m[0][3] = 0;
        rz.m[1][0] = (float)Math.sin(z); rz.m[1][1] =  (float)Math.cos(z); rz.m[1][2] = 0;					 rz.m[1][3] = 0;
        rz.m[2][0] = 0;					 rz.m[2][1] = 0;				   rz.m[2][2] = 1;					 rz.m[2][3] = 0;
        rz.m[3][0] = 0;					 rz.m[3][1] = 0;				   rz.m[3][2] = 0;					 rz.m[3][3] = 1;

        rx.m[0][0] = 1;					 rx.m[0][1] = 0;				   rx.m[0][2] = 0;					 rx.m[0][3] = 0;
        rx.m[1][0] = 0;					 rx.m[1][1] = (float)Math.cos(x);  rx.m[1][2] = -(float)Math.sin(x); rx.m[1][3] = 0;
        rx.m[2][0] = 0;					 rx.m[2][1] = (float)Math.sin(x);  rx.m[2][2] =  (float)Math.cos(x); rx.m[2][3] = 0;
        rx.m[3][0] = 0;					 rx.m[3][1] = 0;				   rx.m[3][2] = 0;					 rx.m[3][3] = 1;

        ry.m[0][0] = (float)Math.cos(y); ry.m[0][1] = 0;				   ry.m[0][2] = -(float)Math.sin(y); ry.m[0][3] = 0;
        ry.m[1][0] = 0;					 ry.m[1][1] = 1;				   ry.m[1][2] = 0;					 ry.m[1][3] = 0;
        ry.m[2][0] = (float)Math.sin(y); ry.m[2][1] = 0;				   ry.m[2][2] =  (float)Math.cos(y); ry.m[2][3] = 0;
        ry.m[3][0] = 0;					 ry.m[3][1] = 0;				   ry.m[3][2] = 0;					 ry.m[3][3] = 1;

        m = rz.multiply(ry.multiply(rx)).getValues();

        return this;
    }

    public Matrix4f initRotation(Vector3f forward, Vector3f up)
    {
        Vector3f f = forward.normalize();

        Vector3f r = up.normalize();
        r = r.cross(f);

        Vector3f u = f.cross(r);

        return initRotation(f, u, r);
    }

    public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right)
    {
        Vector3f f = forward;
        Vector3f r = right;
        Vector3f u = up;

        m[0][0] = r.getX();	m[0][1] = r.getY();	m[0][2] = r.getZ();	m[0][3] = 0;
        m[1][0] = u.getX();	m[1][1] = u.getY();	m[1][2] = u.getZ();	m[1][3] = 0;
        m[2][0] = f.getX();	m[2][1] = f.getY();	m[2][2] = f.getZ();	m[2][3] = 0;
        m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

        return this;
    }

    public Vector3f transform(Vector3f other)
    {
        return new Vector3f(m[0][0] * other.getX() + m[0][1] * other.getY() + m[0][2] * other.getZ() + m[0][3],
                            m[1][0] * other.getX() + m[1][1] * other.getY() + m[1][2] * other.getZ() + m[1][3],
                            m[2][0] * other.getX() + m[2][1] * other.getY() + m[2][2] * other.getZ() + m[2][3]);
    }

    public Matrix4f initPerspective(float fieldOfView, float aspectRatio, float zNear, float zFar)
    {
        float tanHalfFOV = (float)Math.tan(Math.toRadians(fieldOfView / 2));
        float zRange = zNear - zFar;

        m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
        m[1][0] = 0;									m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
        m[2][0] = 0;									m[2][1] = 0;					m[2][2] = (-zNear -zFar) / zRange;	m[2][3] = 2 * zFar * zNear / zRange;
        m[3][0] = 0;									m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;


        return this;
    }

    public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far)
    {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        m[0][0] = 2 / width;	m[0][1] = 0;			m[0][2] = 0;			m[0][3] = -(right + left) / width;
        m[1][0] = 0;			m[1][1] = 2 / height;	m[1][2] = 0;			m[1][3] = -(top + bottom) / height;
        m[2][0] = 0;			m[2][1] = 0;			m[2][2] = -2 / depth;	m[2][3] = -(far + near) / depth;
        m[3][0] = 0;			m[3][1] = 0;			m[3][2] = 0;			m[3][3] = 1;

        return this;
    }

    public Matrix4f multiply(Matrix4f other)
    {
        Matrix4f result = new Matrix4f();
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                result.set(i, j, m[i][0] * other.get(0, j) +
                                 m[i][1] * other.get(1, j) +
                                 m[i][2] * other.get(2, j) +
                                 m[i][3] * other.get(3, j));
            }
        }
        return result;
    }

    public float get(int x, int y) { return m[x][y]; }

    public void set(int x, int y, float value) { m[x][y] = value; }

    public float[][] getValues() { return m; }

    public void setValues(float[][] values) { m = values; }

    public Matrix4f clone()
    {
        Matrix4f result = new Matrix4f();
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                result.set(i, j, m[i][j]);
            }
        }
        return result;
    }
}
