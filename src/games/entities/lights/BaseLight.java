package games.entities.lights;

import engine.core.math.Vector3f;
import games.entities.Entity;

public class BaseLight extends Entity
{
    private Vector3f color;
    private float    intensity;
    private String   lightName;

    public BaseLight(Vector3f color, float intensity)
    {
        super();
        entityTypeName = "BaseLight";
        this.color = color;
        this.intensity = intensity;
        this.lightName = "base";
    }

    public Vector3f getColor()      { return color; }
    public float    getIntensity()  { return intensity; }
    public String   getLightName()  { return lightName; }

    public void setColor(Vector3f color)         { this.color = color; }
    public void setIntensity(float intensity)    { this.intensity = intensity; }
    public void setLightName(String lightName)  { this.lightName = lightName; }
}
