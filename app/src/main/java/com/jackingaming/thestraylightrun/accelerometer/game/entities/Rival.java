package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import java.util.Map;
import java.util.Random;

public class Rival extends Entity {
    private Random random = new Random();

    private ObjectAnimator objectAnimatorUp = ObjectAnimator.ofInt(sprites.get(Direction.UP), "index", 0, sprites.get(Direction.UP).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorDown = ObjectAnimator.ofInt(sprites.get(Direction.DOWN), "index", 0, sprites.get(Direction.DOWN).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofInt(sprites.get(Direction.LEFT), "index", 0, sprites.get(Direction.LEFT).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorRight = ObjectAnimator.ofInt(sprites.get(Direction.RIGHT), "index", 0, sprites.get(Direction.RIGHT).getNumberOfFrames() - 1);

    public Rival(Map<Direction, Animation> sprites,
                 CollisionListener collisionListener, MovementListener movementListener) {
        super(sprites, collisionListener, movementListener);

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

        float xDeltaWithBonus = (xDelta * speedBonus);
        float yDeltaWithBonus = (yDelta * speedBonus);

        // TILE-COLLISION
        // TODO: use [direction] to determine which two corner-coordinates (e.g. TopLeft,
        //  TopRight, BottomLeft, BottomRight) to check.
        int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
        int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
        int[] topLeft = new int[2];
        int[] bottomLeft = new int[2];
        int[] topRight = new int[2];
        int[] bottomRight = new int[2];
        boolean isTileWalkable = false;
        switch (direction) {
            case LEFT:
                xTopLeft = (int) (xPos + xDeltaWithBonus);
                yTopLeft = (int) (yPos + yDeltaWithBonus);
                xBottomLeft = (int) (xPos + xDeltaWithBonus);
                yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

                topLeft[0] = xTopLeft;
                topLeft[1] = yTopLeft;
                bottomLeft[0] = xBottomLeft;
                bottomLeft[1] = yBottomLeft;

                isTileWalkable = movementListener.onMove(topLeft, bottomLeft);
                break;
            case UP:
                xTopLeft = (int) (xPos + xDeltaWithBonus);
                yTopLeft = (int) (yPos + yDeltaWithBonus);
                xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yTopRight = (int) (yPos + yDeltaWithBonus);

                topLeft[0] = xTopLeft;
                topLeft[1] = yTopLeft;
                topRight[0] = xTopRight;
                topRight[1] = yTopRight;

                isTileWalkable = movementListener.onMove(topLeft, topRight);
                break;
            case RIGHT:
                xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yTopRight = (int) (yPos + yDeltaWithBonus);
                xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

                topRight[0] = xTopRight;
                topRight[1] = yTopRight;
                bottomRight[0] = xBottomRight;
                bottomRight[1] = yBottomRight;

                isTileWalkable = movementListener.onMove(topRight, bottomRight);
                break;
            case DOWN:
                xBottomLeft = (int) (xPos + xDeltaWithBonus);
                yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());
                xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

                bottomLeft[0] = xBottomLeft;
                bottomLeft[1] = yBottomLeft;
                bottomRight[0] = xBottomRight;
                bottomRight[1] = yBottomRight;

                isTileWalkable = movementListener.onMove(bottomLeft, bottomRight);
                break;
        }
        if (!isTileWalkable) {
            return;
        }

        // ENTITY-COLLISION
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
