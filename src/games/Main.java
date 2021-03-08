package games;

import engine.core.CoreEngine;

public class Main
{
    public static void main(String[] args)
    {
        CoreEngine engine = new CoreEngine(new TutorialGame(), 60);
        engine.createWindow(800, 600, "3D Game Engine", false);
        engine.start();
    }
}
