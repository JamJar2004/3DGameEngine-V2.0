package games.components;

import engine.core.math.Quaternion;
import engine.core.math.Vector3f;
import games.entities.Entity;

public class Follow extends EntityComponent
{
    private Entity     follower;
    private Vector3f   distance;
    private Quaternion rotationDifference;
    private boolean    followRotation;

    private boolean followX;
    private boolean followY;
    private boolean followZ;

    public Follow(Entity follower) { this(follower, new Vector3f()); }

    public Follow(Entity follower, Vector3f distance) { this(follower, distance, true); }

    public Follow(Entity follower, Vector3f distance, boolean followRotation) { this(follower, distance, followRotation, new Quaternion()); }

    public Follow(Entity follower, Vector3f distance, boolean followRotation, Quaternion rotationDifference)
    {
        this.follower           = follower;
        this.distance           = distance;
        this.followRotation     = followRotation;
        this.rotationDifference = rotationDifference;
        this.followX            = true;
        this.followY            = true;
        this.followZ            = true;
    }

    @Override
    public void input(float delta) {}

    @Override
    public void update(float delta)
    {
        Vector3f oldPosition = getParent().getTransformation().getPosition();
        Vector3f movement = distance.add(follower.getTransformation().getPosition());

        if(!followX)
            movement.setX(oldPosition.getX());

        if(!followY)
            movement.setY(oldPosition.getY());

        if(!followZ)
            movement.setZ(oldPosition.getZ());

        getParent().getTransformation().setPosition(movement);

        if(followRotation)
            getParent().getTransformation().setRotation(follower.getTransformation().getRotation());
    }

    public Entity     getFollower()           { return follower;           }
    public Vector3f   getDistance()           { return distance;           }
    public boolean    getFollowRotation()     { return followRotation;     }
    public Quaternion getRotationDifference() { return rotationDifference; }

    public void setRotationDifference(Quaternion rotationDifference) { this.rotationDifference = rotationDifference; }
    public void setFollowRotation(boolean followRotation)            { this.followRotation = followRotation;         }
    public void setFollower(Entity follower)                         { this.follower = follower;                     }
    public void setDistance(Vector3f distance)                       { this.distance = distance;                     }

    public boolean getCanFollowX() { return followX; }
    public boolean getCanFollowY() { return followY; }
    public boolean getCanFollowZ() { return followZ; }

    public void setFollowX(boolean followX) { this.followX = followX; }
    public void setFollowY(boolean followY) { this.followY = followY; }
    public void setFollowZ(boolean followZ) { this.followZ = followZ; }
}
