package engine.rendering.renderers;

import engine.core.math.Matrix4f;
import games.entities.Camera;
import games.entities.SkyBox;


public class SkyBoxRenderer extends EntityRenderer
{
    public SkyBoxRenderer(SkyBox skyBox)
    {
        super(skyBox);
    }

    @Override
    public void addRenderingEngineVariables() {}

    @Override
    public void render(Camera camera)
    {
        Matrix4f worldMatrix = getEntity().getTransformation().getMatrix();
        Matrix4f MVP = camera.getViewProjection().multiply(worldMatrix);

        SkyBox skyBox = (SkyBox) getEntity();

        bindShader("skyBox");
        setVariable("MVP", MVP);
        setVariables(skyBox.getMaterial().getVariables());
        updateShaderUniforms("skyBox");
        skyBox.render();
    }
}
