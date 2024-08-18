package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;

import java.util.Map;

public class CollidingOrbit extends Entity {
    public static final String TAG = CollidingOrbit.class.getSimpleName();
    public static final int ANGLE_OF_ROTATION_IN_DEGREE = 2;

    private Entity entityToOrbit;
    private boolean clockwise;
    private double cosCW, sinCW;
    private double cosCCW, sinCCW;

    public CollidingOrbit(Map<Direction, AnimationDrawable> animationsByDirection,
                          Entity entityToOrbit,
                          CollisionListener collisionListener,
                          MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);
        this.entityToOrbit = entityToOrbit;
        clockwise = true;

        double angleOfRotationInRadianCW = Math.toRadians(ANGLE_OF_ROTATION_IN_DEGREE);
        cosCW = Math.cos(angleOfRotationInRadianCW);
        sinCW = Math.sin(angleOfRotationInRadianCW);
        double angleOfRotationInRadianCCW = Math.toRadians(-ANGLE_OF_ROTATION_IN_DEGREE);
        cosCCW = Math.cos(angleOfRotationInRadianCCW);
        sinCCW = Math.sin(angleOfRotationInRadianCCW);
    }

    @Override
    public void collided(Entity collider) {
        Log.e(TAG, "collided()");
    }

    @Override
    protected boolean skipEntityCollisionCheck(Entity e) {
        return super.skipEntityCollisionCheck(e) ||
                (e instanceof Player);
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

        double temp = ((xToRotate - xCenterOfRotation) * cosCW) - ((yToRotate - yCenterOfRotation) * sinCW) + xCenterOfRotation;
        double yRotated = ((xToRotate - xCenterOfRotation) * sinCW) + ((yToRotate - yCenterOfRotation) * cosCW) + yCenterOfRotation;
        double xRotated = temp;

        xPos = (float) xRotated;
        yPos = (float) yRotated;
    }

    private void rotateCounterClockwise() {
        double xToRotate = xPos;
        double yToRotate = yPos;
        double xCenterOfRotation = entityToOrbit.getXPos() + (Tile.WIDTH / 2);
        double yCenterOfRotation = entityToOrbit.getYPos() + (Tile.HEIGHT / 2);

        double temp = ((xToRotate - xCenterOfRotation) * cosCCW) - ((yToRotate - yCenterOfRotation) * sinCCW) + xCenterOfRotation;
        double yRotated = ((xToRotate - xCenterOfRotation) * sinCCW) + ((yToRotate - yCenterOfRotation) * cosCCW) + yCenterOfRotation;
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
