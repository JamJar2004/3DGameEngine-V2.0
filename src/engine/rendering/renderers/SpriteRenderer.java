package engine.rendering.renderers;

import engine.core.math.Matrix4f;
import games.entities.*;

import static org.lwjgl.opengl.GL11.*;

public class SpriteRenderer extends EntityRenderer
{
    public SpriteRenderer(Sprite sprite)
    {
        super(sprite);
    }

    @Override
    public void render(Camera camera)
    {
        Matrix4f worldMatrix = getEntity().getTransformation().getMatrix();
        Matrix4f MVP = camera.getViewProjection().multiply(worldMatrix);

        Sprite sprite = (Sprite) getEntity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        bindShader("sprite");
        setVariable("MVP", MVP);
        setVariables(sprite.getMaterial().getVariables());
        updateShaderUniforms("sprite");
        sprite.render();

        glDepthMask(true);
        glDisable(GL_BLEND);
    }
}
