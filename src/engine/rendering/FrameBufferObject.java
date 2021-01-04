package engine.rendering;

import engine.core.Window;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class FrameBufferObject
{
    private int     frameBufferID;
    private int     renderBufferID;
    private int     depthBufferID;
    private Texture texture;
    private Texture depthTexture;

    private int width;
    private int height;

    public FrameBufferObject(int width, int height)
    {
        initialiseFrameBuffer();
    }

    public void cleanUp()
    {
        glDeleteFramebuffers(frameBufferID);
        glDeleteRenderbuffers(renderBufferID);
        glDeleteTextures(texture.getID());
        glDeleteTextures(depthTexture.getID());
    }

    public void bindFrameBuffer() //call before rendering to this FBO
    {
        bindFrameBuffer(frameBufferID, width, height);
    }

    public void unbindFrameBuffer() //call to switch to default frame buffer
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, Window.getWidth(), Window.getHeight());
    }

    public Texture getTexture() //get the resulting texture
    {
        return texture;
    }

    public Texture getDepthTexture() //get the resulting texture
    {
        return depthTexture;
    }

    private void initialiseFrameBuffer()
    {
        frameBufferID = createFrameBuffer();
        texture = createTextureAttachment(width, height);
        depthBufferID = createDepthBufferAttachment(width, height);
        depthTexture = createDepthTextureAttachment(width, height);
        unbindFrameBuffer();
    }

    private void bindFrameBuffer(int frameBuffer, int width, int height)
    {
        glBindTexture(GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }

    private int createFrameBuffer()
    {
        int frameBuffer = glGenFramebuffers();
        //generate name for frame buffer
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        //create the framebuffer
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        //indicate that we will always render to color attachment 0
        return frameBuffer;
    }

    private Texture createTextureAttachment(int width, int height)
    {
        Texture texture = new Texture(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, texture.getID());
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.getID(), 0);
        return texture;
    }

    private Texture createDepthTextureAttachment(int width, int height)
    {
        Texture texture = new Texture(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, texture.getID());
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture.getID(), 0);
        return texture;
    }

    private int createDepthBufferAttachment(int width, int height)
    {
        int depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
}
