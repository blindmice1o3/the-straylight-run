package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.graphics.Bitmap;

import java.util.Map;
import java.util.Random;

public class Rival extends Entity {

    private Random random = new Random();

    public Rival(Map<Direction, Bitmap> sprites, CollisionListener collisionListener) {
        super(sprites, collisionListener);

        xPos = 400f;
        yPos = 400f;
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update() {
        // DIRECTION (changes 10% of the time)
        if (random.nextInt(10) < 1) {
            // determine direction
            switch (random.nextInt(4)) {
                case 0:
                    direction = Direction.UP;
                    break;
                case 1:
                    direction = Direction.DOWN;
                    break;
                case 2:
                    direction = Direction.LEFT;
                    break;
                case 3:
                    direction = Direction.RIGHT;
                    break;
            }
        }
        // don't change direction (90% of the time)
        else {
            // do nothing
        }

        // POSITION
        int xDelta = 0;
        int yDelta = 0;
        switch (direction) {
            case UP:
                yDelta = -speedMovement;
                break;
            case DOWN:
                yDelta = speedMovement;
                break;
            case LEFT:
                xDelta = -speedMovement;
                break;
            case RIGHT:
                xDelta = speedMovement;
                break;
        }

        // ENTITY-COLLISION
        float xDeltaWithBonus = (xDelta * speedBonus);
        float yDeltaWithBonus = (yDelta * speedBonus);

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
