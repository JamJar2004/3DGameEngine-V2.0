package engine.rendering.renderers;

import engine.core.Window;
import engine.core.math.Matrix4f;
import engine.core.math.Quaternion;
import engine.core.math.Vector4f;
import engine.rendering.WaterFrameBuffers;
import games.entities.*;
import games.entities.lights.BaseLight;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class WaterRenderer extends EntityRenderer
{
    //private FrameBufferObject waterBuffer;
    private WaterFrameBuffers waterBuffer;
    private WaterFrameBuffers lowPolyWaterBuffer;

    public WaterRenderer(Water water)
    {
        super(water);
        waterBuffer        = new WaterFrameBuffers((int)((float)Window.getWidth() / 2.0f), (int)((float)Window.getHeight() / 2.0f));
        lowPolyWaterBuffer = new WaterFrameBuffers((int)((float)Window.getWidth() / 4.0f), (int)((float)Window.getHeight() / 4.0f));
    }

    @Override
    public void addRenderingEngineVariables() {}

    @Override
    public void render(Camera camera)
    {
        Water water = (Water) getEntity();

        glEnable(GL_CLIP_DISTANCE0);

        WaterFrameBuffers finalBuffer;

        loadRenderingEngineBoolean("highQuality");
        boolean highQuality = getVariables().getBoolean("highQuality");

        if(highQuality)
            finalBuffer = waterBuffer;
        else
            finalBuffer = lowPolyWaterBuffer;

        finalBuffer.bindReflectionFrameBuffer();

        float waterHeight = getEntity().getTransformation().getPosition().getY();

        float distance = 2 * (camera.getTransformation().getPosition().getY() - waterHeight);

        Camera reflectCamera = camera.clone();

        reflectCamera.getTransformation().move(0, -distance, 0);
        Quaternion cameraRotation = reflectCamera.getTransformation().getRotation();
        reflectCamera.getTransformation().setRotation(cameraRotation.invertZ().invertX());

        renderScene(reflectCamera, new Vector4f(0, 1, 0, -waterHeight + 1));

        finalBuffer.bindRefractionFrameBuffer();

        renderScene(camera, new Vector4f(0, -1, 0, waterHeight + 1));

        finalBuffer.unbindCurrentFrameBuffer();

        glDisable(GL_CLIP_DISTANCE0);

        Matrix4f worldMatrix = getEntity().getTransformation().getMatrix();
        Matrix4f MVP = camera.getViewProjection().multiply(worldMatrix);

        water.getMaterial().setTexture("reflection", finalBuffer.getReflectionTexture());
        water.getMaterial().setTexture("refraction", finalBuffer.getRefractionTexture());
        water.getMaterial().setTexture("depthMap",   finalBuffer.getRefractionDepthTexture());

        renderAmbient(worldMatrix, MVP, camera);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDepthMask(false);
        glDepthFunc(GL_EQUAL);

        if(getRenderingEngineEntities("BaseLight") != null)
        {
            for (Entity lightEntity : getRenderingEngineEntities("BaseLight").values())
            {
                renderLight(MVP, worldMatrix, (BaseLight) lightEntity, camera);
            }
        }

        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);

    }

    private void renderAmbient(Matrix4f worldMatrix, Matrix4f MVP, Camera camera)
    {
        Water water = (Water) getEntity();

        bindShader("water-ambient");
        setVariable("MVP", MVP);
        setVariable("modelMatrix", worldMatrix);
        setVariables(water.getMaterial().getVariables());
        setVariable("moveFactor", water.getMoveFactor());
        setVariable("camZNear", camera.getZNear());
        setVariable("camZFar", camera.getZFar());
        setVariable("cameraPosition", camera.getTransformation().getPosition());
        updateShaderUniforms("water-ambient");
        water.render();
    }

    private void renderLight(Matrix4f MVP, Matrix4f worldMatrix, BaseLight baseLight, Camera camera)
    {
        Water water = (Water) getEntity();

        bindShader("water-" + baseLight.getLightName());
        setVariable("modelMatrix", worldMatrix);
        setVariable("MVP", MVP);
        setVariable("moveFactor", water.getMoveFactor());
        setVariable("cameraPosition", camera.getTransformation().getPosition());
        setVariable("eyePosition", camera.getTransformation().getPosition());
        setVariables(water.getMaterial().getVariables());
        setVariable(baseLight.getLightName() + "Light", baseLight);
        updateShaderUniforms("water-" + baseLight.getLightName());
        water.render();
    }

    private void renderScene(Camera camera, Vector4f clippingPlane)
    {
        getRenderingEngine().clearScreen();

        if (getRenderingEngineEntities("Mesh") != null)
        {
            for (Entity entity : getRenderingEngineEntities("Mesh").values())
            {
                if (entity.hasRenderer())
                {
                    entity.getRenderer().setRenderingEngine(getRenderingEngine());
                    entity.getRenderer().setVariable("clippingPlane", clippingPlane);
                    entity.getRenderer().render(camera);
                }
            }
        }

        if (getRenderingEngineEntities("SkyBox") != null)
        {
            for (Entity entity : getRenderingEngineEntities("SkyBox").values())
            {
                if (entity.hasRenderer())
                {
                    entity.getRenderer().setRenderingEngine(getRenderingEngine());
                    entity.getRenderer().render(camera);
                }
            }
        }
    }
}
