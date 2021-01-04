package games.entities.lights;

import engine.core.math.Vector3f;

public class DirectionalLight extends BaseLight
{
    public DirectionalLight(Vector3f color, float intensity)
    {
        super(color, intensity);
        setLightName("directional");
    }
}
