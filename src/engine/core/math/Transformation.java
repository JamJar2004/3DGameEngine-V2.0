package engine.core.math;

public class Transformation
{
    private Transformation parent;
    private Matrix4f       parentMatrix;

    private Vector3f   position;
    private Quaternion rotation;
    private Vector3f   scale;

    private Vector3f oldPosition;
    private Quaternion oldRotation;
    private Vector3f oldScale;

    public Transformation(Vector3f position, Quaternion rotation, Vector3f scale)
    {
        this.position = position;
        this.rotation = rotation;
        this.scale    = scale;

        parentMatrix = new Matrix4f().initIdentity();
    }

    public void update()
    {
        if(oldPosition != null)
        {
            oldPosition.set(position);
            oldRotation.set(rotation);
            oldScale.set(scale);
        }
        else
        {
            oldPosition = new Vector3f(0,0,0).set(position).add(1.0f);
            oldRotation = new Quaternion(0,0,0,0).set(rotation).multiply(0.5f);
            oldScale    = new Vector3f(0,0,0).set(scale).add(1.0f);
        }
    }

    public Transformation()
    {
        this(new Vector3f(), new Quaternion(), new Vector3f(1, 1, 1));
    }

    public Matrix4f getMatrix()
    {
        Matrix4f translationMatrix = new Matrix4f().initTranslation(position);
        Matrix4f rotationMatrix    = rotation.toRotationMatrix();
        Matrix4f scaleMatrix       = new Matrix4f().initScale(scale);

        return translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));
    }

    public void lookAt(Vector3f point, Vector3f up)
    {
        lookAt(point, up, new Quaternion());
    }

    public void lookAt(Vector3f point, Vector3f up, Quaternion rotationOffset)
    {
        rotation = getLookAtRotation(point, up).multiply(rotationOffset);
    }

    public Quaternion getLookAtRotation(Vector3f point, Vector3f up)
    {
        return new Quaternion(new Matrix4f().initRotation(point.subtract(position).normalize(), up));
    }

    public boolean hasChanged()
    {
        if(parent != null && parent.hasChanged())
            return true;

        if(!position.equals(oldPosition))
            return true;

        if(!rotation.equals(oldRotation))
            return true;

        if(!scale.equals(oldScale))
            return true;

        return false;
    }

    private Matrix4f getParentMatrix()
    {
        if(parent != null && parent.hasChanged())
            parentMatrix = parent.getMatrix();

        return parentMatrix;
    }

    public void setParent(Transformation parent)
    {
        this.parent = parent;
    }

    public Vector3f getTransformedPosition()
    {
        return getParentMatrix().transform(position);
    }

    public Quaternion getTransformedRotation()
    {
        Quaternion parentRotation = new Quaternion(0,0,0,1);

        if(parent != null)
            parentRotation = parent.getTransformedRotation();

        return parentRotation.multiply(rotation);
    }

    public Transformation invert()
    {
        Vector3f   invertPosition = position.invert();
        Quaternion invertRotation = rotation.conjugate();
        Vector3f   invertScale    = scale.pow(-1f);

        return new Transformation(invertPosition, invertRotation, invertScale);
    }

    public Vector3f   getPosition() { return position; }
    public Quaternion getRotation() { return rotation; }
    public Vector3f   getScale()    { return scale; }

    public void setPosition(Vector3f position) { this.position = position; }
    public void setPosition(float x, float y, float z) { this.position = new Vector3f(x, y, z); }

    public void move(Vector3f position) { position = position.add(position); }
    public void move(float x, float y, float z) { position = position.add(new Vector3f(x, y, z)); }

    public void move(Vector3f direction, float amount) { position = position.add(direction.multiply(amount)); }

    public void setRotation(Quaternion rotation) { this.rotation = rotation; }
    public void setRotation(Vector3f axis, float angle) { this.rotation = new Quaternion(axis, angle); }
    public void turn(Vector3f axis, float angle) { rotation = new Quaternion(axis, angle).multiply(rotation).normalize(); }

    public void setScale(Vector3f scale) { this.scale = scale; }
    public void setScale(float width, float height, float depth) { this.scale = new Vector3f(width, height, depth); }
    public void setScale(float size) { this.scale = new Vector3f(size); }

    public Transformation clone()
    {
        Transformation result = new Transformation(position.clone(), rotation.clone(), scale.clone());

        if(parent != null)
            result.setParent(parent.clone());

        result.update();
        return result;
    }
}
