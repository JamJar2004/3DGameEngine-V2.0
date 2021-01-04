package games.entities.lights;

import engine.core.math.Vector3f;
import engine.rendering.Attenuation;

public class SpotLight extends PointLight
{
    private float cutoff;

    public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float cutoff)
    {
        super(color, intensity, attenuation);
        setLightName("spot");
        this.cutoff = cutoff;
    }

    public float getCutoff() { return cutoff; }

    public void setCutoff(float cutoff) { this.cutoff = cutoff; }
}
