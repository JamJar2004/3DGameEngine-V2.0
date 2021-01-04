package games.entities;

import engine.core.Window;
import engine.core.math.Matrix4f;
import engine.core.math.Vector3f;

public class Camera extends Entity
{
    private Matrix4f projection;

    private float zNear;
    private float zFar;

    public Camera()
    {
        this(new Vector3f());
    }

    public Camera(Vector3f position)
    {
        super();
        getTransformation().setPosition(position);
        entityTypeName = "Camera";
        zNear = 0.01f;
        zFar  = 1000f;
        setPerspectiveProjection(70.0f, Window.getAspectRatio(), zNear, zFar);
    }

    public Matrix4f getViewProjection()
    {
        Matrix4f cameraRotation = getTransformation().getTransformedRotation().conjugate().toRotationMatrix();
        Vector3f cameraPosition = getTransformation().getTransformedPosition().multiply(-1);

        Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPosition);

        return projection.multiply(cameraRotation.multiply(cameraTranslation));
    }

    public Matrix4f getViewMatrix()
    {
        Matrix4f cameraRotation = getTransformation().getTransformedRotation().conjugate().toRotationMatrix();
        Vector3f cameraPos      = getTransformation().getTransformedPosition().multiply(-1);

        Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos);

        return cameraRotation.multiply(cameraTranslation);
    }

    public Matrix4f getProjection() { return projection; }
    public void setProjection(Matrix4f projection) { this.projection = projection; }

    public void setPerspectiveProjection(float fieldOfView, float aspectRatio, float zNear, float zFar)
    {
        this.zNear = zNear;
        this.zFar  = zFar;
        this.projection = new Matrix4f().initPerspective(fieldOfView, aspectRatio, zNear, zFar);
    }

    public void setOrthographicProjection(float left, float right, float bottom, float top, float near, float far)
    {
        this.zNear = near;
        this.zFar  = far;
        this.projection = new Matrix4f().initOrthographic(left, right, bottom, top, near, far);
    }

    public float getZNear()
    {
        return zNear;
    }

    public void setZNear(float zNear)
    {
        this.zNear = zNear;
    }

    public float getZFar()
    {
        return zFar;
    }

    public void setZFar(float zFar)
    {
        this.zFar = zFar;
    }

    public Camera clone()
    {
        Camera result = new Camera();
        result.setProjection(projection.clone());
        result.setTransformation(getTransformation().clone());
        return result;
    }
}
