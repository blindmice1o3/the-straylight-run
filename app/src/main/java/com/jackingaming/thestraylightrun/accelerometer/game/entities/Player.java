package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.graphics.Bitmap;

import java.util.Map;

public class Player extends Entity
        implements Controllable {

    public Player(Map<Direction, Bitmap> sprites, CollisionListener collisionListener) {
        super(sprites, collisionListener);
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
