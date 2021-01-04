package engine.rendering.renderers;

import engine.core.math.Matrix4f;
import engine.core.math.Vector3f;
import games.entities.Camera;
import games.entities.Entity;
import games.entities.Mesh;
import games.entities.lights.BaseLight;

import static org.lwjgl.opengl.GL11.*;

public class MeshRenderer extends EntityRenderer
{
    public MeshRenderer(Mesh mesh)
    {
        super(mesh);
    }

    @Override
    public void addRenderingEngineVariables()
    {
        loadRenderingEngineVector3f("ambientLight");
        loadRenderingEngineBoolean("shading");
    }

    @Override
    public void render(Camera camera)
    {
        Matrix4f worldMatrix = getEntity().getTransformation().getMatrix();
        Matrix4f MVP = camera.getViewProjection().multiply(worldMatrix);

        Mesh mesh = (Mesh) getEntity();
        setVariables(mesh.getMaterial().getVariables());

        boolean shading = getVariables().getBoolean("shading") && !getVariables().getBoolean("ignoreLighting");

        renderAmbient(MVP, shading);

        if(shading)
        {
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glDepthMask(false);
            glDepthFunc(GL_EQUAL);

            if (getRenderingEngineEntities("BaseLight") != null)
            {
                for (Entity lightEntity : getRenderingEngineEntities("BaseLight").values())
                {
                    renderLight(MVP, worldMatrix, (BaseLight)lightEntity, camera.getTransformation().getPosition());
                }
            }

            glDepthFunc(GL_LESS);
            glDepthMask(true);
            glDisable(GL_BLEND);
        }
    }

    private void renderAmbient(Matrix4f MVP, boolean hasLighting)
    {
        Mesh mesh = (Mesh) getEntity();

        bindShader("forward-ambient");

        if(!hasLighting)
            setVariable("ambientLight", new Vector3f(1, 1, 1));

        setVariable("MVP", MVP);

        updateShaderUniforms("forward-ambient");
        mesh.render();
    }

    private void renderLight(Matrix4f MVP, Matrix4f worldMatrix, BaseLight baseLight, Vector3f eyePosition)
    {
        Mesh mesh = (Mesh) getEntity();

        bindShader("forward-" + baseLight.getLightName());
        setVariable("modelMatrix", worldMatrix);
        setVariable("MVP", MVP);
        setVariable("eyePosition", eyePosition);
        setVariable(baseLight.getLightName() + "Light", baseLight);
        updateShaderUniforms("forward-" + baseLight.getLightName());
        mesh.render();
    }
}
