package games.wing_ring;

import engine.core.math.Vector3f;
import games.entities.Sprite;

public class Bullet extends Sprite
{
    public Bullet(Vector3f startPosition, Vector3f direction, String sparkName)
    {
        super("wing_ring/" + sparkName + "_spark.BMP");
        getTransformation().setPosition(startPosition);
        addComponent(new BulletBehaviour(direction));
    }
}
