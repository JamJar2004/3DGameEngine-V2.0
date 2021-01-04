package games.Wolfenstein3D;

import engine.core.Time;
import engine.core.math.Transformation;
import engine.core.math.*;
import engine.rendering.*;
import games.entities.Mesh;

import java.util.Random;

import static games.Wolfenstein3D.WolfPlayer.PLAYER_SIZE;

public class Monster extends Mesh
{
    public static final int STATE_IDLE   = 0;
    public static final int STATE_CHASE  = 1;
    public static final int STATE_ATTACK = 2;
    public static final int STATE_DYING  = 3;
    public static final int STATE_DEAD   = 4;

    public static final float MOVE_SPEED = 3.0f;
    public static final float MOVEMENT_STOP_DISTANCE = 1.5f;

    public static final float MONSTER_WIDTH  = 0.2f;
    public static final float MONSTER_LENGTH = 0.2f;

    public static final float SHOOT_DISTANCE = 1000.0f;
    public static final float SHOT_ANGLE     = 10.0f;
    public static final float ATTACK_CHANCE  = 0.5f;
    public static final int   MAX_HEALTH     = 100;


    private int        state;
    private int        health;
    private WolfPlayer player;
    private Level      level;
    private Random     rand;
    private boolean    canLook;
    private boolean    canAttack;

    public Monster(Transformation transformation, WolfPlayer player, Level level)
    {
        super(Primitive.QUAD);

        transformation.turn(Vector3f.RIGHT, 90);
        transformation.setScale(0.190f, 0, 0.3375f);
        transformation.move(0, 0.3375f, 0);
        setTransformation(transformation);

        this.state   = STATE_IDLE;
        this.canAttack = false;
        this.canLook = false;
        this.health = MAX_HEALTH;
        this.rand    = new Random();
        this.player  = player;
        this.level   = level;

        Material material = new Material();
        material.setTexture("texture", new Texture("Wolfenstein3D/SSWVA1.png", false));
        setMaterial(material);
    }

    public void damage(int amount)
    {
        if(state == STATE_IDLE)
            state = STATE_CHASE;

        health -= amount;

        if(health <= 0)
            state = STATE_DYING;
    }

    private void idleUpdate(Vector3f orientation, float distance, float delta)
    {
        double time         = Time.getTime();
        double timeDecimals = time - (double)((int)time);

        if(timeDecimals < 0.5f)
        {
            canLook = true;
        }
        else if(canLook)
        {
            Vector2f lineStart = getTransformation().getPosition().getXZ();
            Vector2f castDirection = orientation.getXZ();
            Vector2f lineEnd = lineStart.add(castDirection.multiply(SHOOT_DISTANCE));

            Vector2f collisionVector = level.checkIntersections(lineStart, lineEnd);

            Vector2f playerIntersectVector = level.lineIntersectRect(lineStart, lineEnd,
                                             getTransformation().getPosition().getXZ(),
                                             new Vector2f(PLAYER_SIZE));

            if(playerIntersectVector != null && (collisionVector == null ||
                    playerIntersectVector.subtract(lineStart).length() < collisionVector.subtract(lineStart).length()))
            {
                System.out.println("We've seen the player!");
                state = STATE_CHASE;
            }

            canLook = false;
        }
    }

    private void chaseUpdate(Vector3f orientation, float distance, float delta)
    {
        if(rand.nextDouble() < ATTACK_CHANCE * delta)
            state = STATE_ATTACK;

        if(distance > MOVEMENT_STOP_DISTANCE)
        {
            float moveAmount = MOVE_SPEED * delta;

            Vector3f oldPosition = getTransformation().getPosition();
            Vector3f newPosition = getTransformation().getPosition().add(orientation.multiply(moveAmount));

            Vector3f collisionVector = level.checkCollision(oldPosition, newPosition, new Vector2f(MONSTER_WIDTH, MONSTER_LENGTH));

            Vector3f movementVector = collisionVector.multiply(orientation);

            if(movementVector.length() > 0)
                getTransformation().setPosition(getTransformation().getPosition().add(movementVector.multiply(moveAmount)));

            if(!movementVector.equals(orientation))
                level.openDoors(getTransformation().getPosition());
        }
        else
            state = STATE_ATTACK;
    }

    private void attackUpdate(Vector3f orientation, float distance, float delta)
    {
        double time         = Time.getTime();
        double timeDecimals = time - (double)((int)time);

        if(timeDecimals < 0.5f)
        {
            canAttack = true;
        }
        else if(canAttack)
        {
            Vector2f lineStart = new Vector2f(getTransformation().getPosition().getX(), getTransformation().getPosition().getZ());
            Vector2f castDirection = new Vector2f(orientation.getX(), orientation.getZ()).rotate((rand.nextFloat() - 0.5f) * SHOT_ANGLE);
            Vector2f lineEnd = lineStart.add(castDirection.multiply(SHOOT_DISTANCE));

            Vector2f collisionVector = level.checkIntersections(lineStart, lineEnd);

            Vector2f playerIntersectVector = level.lineIntersectRect(lineStart, lineEnd,
                    new Vector2f(getTransformation().getPosition().getX(), getTransformation().getPosition().getZ()),
                    new Vector2f(WolfPlayer.PLAYER_SIZE, WolfPlayer.PLAYER_SIZE));

            if (playerIntersectVector != null && (collisionVector == null ||
                    playerIntersectVector.subtract(lineStart).length() < collisionVector.subtract(lineStart).length()))
            {
                System.out.println("We've just shot the player!");
            }

            if (collisionVector == null)
                System.out.println("We've missed everything!");
            else
                System.out.println("We've hit something!");

            state = STATE_CHASE;
            canAttack = false;
        }
    }

    private void dyingUpdate(Vector3f orientation, float distance, float delta)
    {
        state = STATE_DEAD;
    }

    private void deadUpdate(Vector3f orientation, float distance, float delta)
    {
        System.out.println("We're dead!");
    }

    private void faceTheCamera(Vector3f directionToCamera)
    {
        float angleToCamera = (float)(-Math.toDegrees(Math.atan(directionToCamera.getZ() / directionToCamera.getX())));

        if(directionToCamera.getX() < 0)
            angleToCamera += 180;

        getTransformation().setRotation(Vector3f.RIGHT, 90);
        getTransformation().turn(Vector3f.UP, angleToCamera + 90);
    }

    @Override
    public void update(float delta)
    {
        Vector3f directionToCamera = player.getTransformation().getPosition().subtract(getTransformation().getPosition());

        float distance = directionToCamera.length();
        Vector3f orientation = directionToCamera.divide(distance);

        faceTheCamera(orientation);

        switch(state)
        {
            case STATE_IDLE:   idleUpdate(orientation, distance, delta);   break;
            case STATE_CHASE:  chaseUpdate(orientation, distance, delta);  break;
            case STATE_ATTACK: attackUpdate(orientation, distance, delta); break;
            case STATE_DYING:  dyingUpdate(orientation, distance, delta);  break;
            case STATE_DEAD:   deadUpdate(orientation, distance, delta);   break;
        }
    }
}
