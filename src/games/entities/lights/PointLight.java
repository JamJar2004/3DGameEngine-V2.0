package games.entities.lights;

import engine.core.math.Vector3f;
import engine.rendering.Attenuation;

public class PointLight extends BaseLight
{
    private static final int COLOR_DEPTH = 256;

    private Attenuation attenuation;
    private float       range;

    public PointLight(Vector3f color, float intensity, Attenuation attenuation)
    {
        super(color, intensity);
        setLightName("point");
        this.attenuation = attenuation;

        //I got this calculation from thebennybox's 3D Game Engine tutorial series.
        float a = attenuation.getExponent();
        float b = attenuation.getLinear();
        float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().maxValue();

        this.range = (float)(-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
    }

    public Attenuation getAttenuation() { return attenuation; }
    public float       getRange()       { return range; }

    public void setAttenuation(Attenuation attenuation) { this.attenuation = attenuation; }




}
