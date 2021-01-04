package games;

import engine.core.CoreEngine;
import games.wing_ring.*;

public class Main
{
    public static void main(String[] args)
    {
        CoreEngine engine = new CoreEngine(new WingRing(), 60);
        engine.createWindow(1024, 768, "3D Game Engine");
        engine.start();
    }
}
