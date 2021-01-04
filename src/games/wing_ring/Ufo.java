package games.wing_ring;

import engine.core.math.Vector3f;
import games.entities.Mesh;

public class Ufo extends Mesh
{
    private static float maxRange    = 100f;
    private static float floorHeight = 100f;

    public enum UfoType
    {
        ORANGE("orange"), YELLOW("yellow"), GREEN("green"), CYAN("cyan"), PINK("pink");
        public String value;
        UfoType(String value) { this.value = value; }
    }

    public Ufo(UfoType type, WingPlayer player)
    {
        loadMesh("wing_ring/" + type.value + "_ufo.x");
        getMaterial().setFloat("reflectivity", 0.5f);
        getMaterial().setFloat("damping", 3.0f);

        UfoBehaviour behaviour = new UfoBehaviour(player, getMinPosition(), getMaxPosition(), type.value, type);
        behaviour.setMaxRange(maxRange);
        behaviour.setFloorHeight(floorHeight);
        addComponent(behaviour);
        //addComponent(new LookAt(player));
    }

    public static float getMaxRange()
    {
        return maxRange;
    }

    public static float getFloorHeight()
    {
        return floorHeight;
    }

    public static void setMaxRange(float maxRange)
    {
        Ufo.maxRange = maxRange;
    }

    public static void setFloorHeight(float floorHeight)
    {
        Ufo.floorHeight = floorHeight;
    }
}
