package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.Map;

public class CollidingOrbit extends Entity {
    public static final String TAG = CollidingOrbit.class.getSimpleName();

    private Entity entityToOrbit;
    private boolean clockwise;
    private int angleOfRotationInDegree;

    public CollidingOrbit(Map<Direction, AnimationDrawable> animationsByDirection,
                          Entity entityToOrbit,
                          CollisionListener collisionListener,
                          MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);
        this.entityToOrbit = entityToOrbit;
        clockwise = true;
        angleOfRotationInDegree = 2;
    }

    @Override
    public void collided(Entity collider) {
        Log.e(TAG, "collided()");
    }

    @Override
    public void update(Handler handler) {
        if (clockwise) {
            rotateClockwise();
        } else {
            rotateCounterClockwise();
        }

        boolean colliding = checkEntityCollision(0f, 0f);
        updateStateOfEntityCollision(colliding);
    }

    private void rotateClockwise() {
        double xToRotate = xPos;
        double yToRotate = yPos;
        double xCenterOfRotation = entityToOrbit.getXPos() + (Tile.WIDTH / 2);
        double yCenterOfRotation = entityToOrbit.getYPos() + (Tile.HEIGHT / 2);
        double angleOfRotationInRadian = Math.toRadians(angleOfRotationInDegree);

        double cos = Math.cos(angleOfRotationInRadian);
        double sin = Math.sin(angleOfRotationInRadian);
        double temp = ((xToRotate - xCenterOfRotation) * cos) - ((yToRotate - yCenterOfRotation) * sin) + xCenterOfRotation;
        double yRotated = ((xToRotate - xCenterOfRotation) * sin) + ((yToRotate - yCenterOfRotation) * cos) + yCenterOfRotation;
        double xRotated = temp;

        xPos = (float) xRotated;
        yPos = (float) yRotated;
    }

    private void rotateCounterClockwise() {
        double xToRotate = xPos;
        double yToRotate = yPos;
        double xCenterOfRotation = entityToOrbit.getXPos() + (Tile.WIDTH / 2);
        double yCenterOfRotation = entityToOrbit.getYPos() + (Tile.HEIGHT / 2);
        double angleOfRotationInRadian = Math.toRadians(-angleOfRotationInDegree);

        double cos = Math.cos(angleOfRotationInRadian);
        double sin = Math.sin(angleOfRotationInRadian);
        double temp = ((xToRotate - xCenterOfRotation) * cos) - ((yToRotate - yCenterOfRotation) * sin) + xCenterOfRotation;
        double yRotated = ((xToRotate - xCenterOfRotation) * sin) + ((yToRotate - yCenterOfRotation) * cos) + yCenterOfRotation;
        double xRotated = temp;

        xPos = (float) xRotated;
        yPos = (float) yRotated;
    }

    public void toggleClockwise() {
        clockwise = !clockwise;
    }

    public boolean isClockwise() {
        return clockwise;
    }
}
