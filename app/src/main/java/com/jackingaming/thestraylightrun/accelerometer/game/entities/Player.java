package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import java.util.Map;

public class Player extends Entity
        implements Controllable {

    private ObjectAnimator objectAnimatorUp = ObjectAnimator.ofInt(sprites.get(Direction.UP), "index", 0, sprites.get(Direction.UP).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorDown = ObjectAnimator.ofInt(sprites.get(Direction.DOWN), "index", 0, sprites.get(Direction.DOWN).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofInt(sprites.get(Direction.LEFT), "index", 0, sprites.get(Direction.LEFT).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorRight = ObjectAnimator.ofInt(sprites.get(Direction.RIGHT), "index", 0, sprites.get(Direction.RIGHT).getNumberOfFrames() - 1);

    public Player(Map<Direction, Animation> sprites, CollisionListener collisionListener) {
        super(sprites, collisionListener);

        objectAnimatorUp.setDuration(500);
        objectAnimatorUp.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorUp.start();

        objectAnimatorDown.setDuration(500);
        objectAnimatorDown.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorDown.start();

        objectAnimatorLeft.setDuration(500);
        objectAnimatorLeft.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorLeft.start();

        objectAnimatorRight.setDuration(500);
        objectAnimatorRight.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorRight.start();
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update() {

    }

    @Override
    public void updateViaSensorEvent(float xDelta, float yDelta) {
        // DIRECTION
        if (Math.abs(yDelta) >= Math.abs(xDelta)) {
            if (yDelta < 0) {
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        } else {
            if (xDelta < 0) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        }

        // ENTITY-COLLISION
        float xDeltaWithBonus = xDelta * speedBonus;
        float yDeltaWithBonus = yDelta * speedBonus;

        colliding = checkEntityCollision(xDeltaWithBonus, yDeltaWithBonus);
        if (cantCollide && !colliding) {
            cantCollide = false;
        } else if (justCollided) {
            cantCollide = true;
            justCollided = false;
        }
        if (!cantCollide && colliding) {
            justCollided = true;
        }

        // POSITION
        if (!colliding) {
            xPos += xDeltaWithBonus;
            yPos += yDeltaWithBonus;

            // Validate
            if (xPos < xMin) {
                xPos = xMin;
            }
            if (xPos > xMax) {
                xPos = xMax;
            }
            if (yPos < yMin) {
                yPos = yMin;
            }
            if (yPos > yMax) {
                yPos = yMax;
            }
        }
    }
}
