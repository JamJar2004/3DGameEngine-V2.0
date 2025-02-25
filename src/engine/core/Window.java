package engine.core;

import engine.core.math.Vector2f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window
{
    public static void create(int width, int height, String title, boolean fullScreen)
    {
        Display.setTitle(title);
        try
        {
            for(DisplayMode mode : Display.getAvailableDisplayModes())
            {
                if(mode.getWidth() == width && mode.getHeight() == height)
                {
                    if(fullScreen && !mode.isFullscreenCapable())
                        continue;

                    Display.setDisplayMode(mode);
                    Display.setFullscreen(fullScreen);
                }
            }

            Display.create();
            Keyboard.create();
            Mouse.create();
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }
    }

    public static void update()
    {
        Display.update();
    }

    public static void cleanUp()
    {
        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    public static int getWidth()
    {
        return Display.getDisplayMode().getWidth();
    }

    public static int getHeight()
    {
        return Display.getDisplayMode().getHeight();
    }

    public static String getTitle()
    {
        return Display.getTitle();
    }

    public static Vector2f getSize() { return new Vector2f(getWidth(), getHeight()); }

    public static Vector2f getCenterPosition() { return new Vector2f(getWidth() / 2, getHeight() / 2); }

    public static float getAspectRatio()
    {
        float aspect = (float)getWidth() / (float)getHeight();
        return aspect;
    }

    public static boolean isCloseRequested()
    {
        return Display.isCloseRequested();
    }
}
