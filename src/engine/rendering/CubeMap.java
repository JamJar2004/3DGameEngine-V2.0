package engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

public class CubeMap extends Texture
{
    public CubeMap(String front, String back, String left, String right, String top, String bottom)
    {
        super(loadCubeMap(new String[]{right, left, top, bottom, back, front}));
        setSamplerSlot(0);
    }

    @Override
    public void bind()
    {
        assert(samplerSlot >= 0 && samplerSlot < 32);
        glActiveTexture(GL_TEXTURE0 + samplerSlot);
        glBindTexture(GL_TEXTURE_CUBE_MAP, ID);
    }

    private static int loadCubeMap(String[] textureFileNames)
    {
        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        for(int i = 0; i < textureFileNames.length; i++)
        {
            TextureData data = new TextureData("res/textures/" + textureFileNames[i]);
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return texID;
    }
}
