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

    public Player(Map<Direction, Animation> sprites,
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

        float xDeltaWithBonus = xDelta * speedBonus;
        float yDeltaWithBonus = yDelta * speedBonus;

        // TILE-COLLISION
        int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
        int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
        int[] topLeft = new int[2];
        int[] bottomLeft = new int[2];
        int[] topRight = new int[2];
        int[] bottomRight = new int[2];
        boolean isTileWalkableHorizontal = false;
        boolean isTileWalkableVertical = false;
        switch (direction) {
            case LEFT:
                xTopLeft = (int) (xPos + xDeltaWithBonus);
                yTopLeft = (int) (yPos + yDeltaWithBonus);
                xBottomLeft = (int) (xPos + xDeltaWithBonus);
                yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

                topLeft[0] = xTopLeft;
                topLeft[1] = yTopLeft + 1;
                bottomLeft[0] = xBottomLeft;
                bottomLeft[1] = yBottomLeft - 1;

                isTileWalkableHorizontal = movementListener.onMove(topLeft, bottomLeft);
                break;
            case UP:
                xTopLeft = (int) (xPos + xDeltaWithBonus);
                yTopLeft = (int) (yPos + yDeltaWithBonus);
                xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yTopRight = (int) (yPos + yDeltaWithBonus);

                topLeft[0] = xTopLeft + 1;
                topLeft[1] = yTopLeft;
                topRight[0] = xTopRight - 1;
                topRight[1] = yTopRight;

                isTileWalkableVertical = movementListener.onMove(topLeft, topRight);
                break;
            case RIGHT:
                xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yTopRight = (int) (yPos + yDeltaWithBonus);
                xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

                topRight[0] = xTopRight;
                topRight[1] = yTopRight + 1;
                bottomRight[0] = xBottomRight;
                bottomRight[1] = yBottomRight - 1;

                isTileWalkableHorizontal = movementListener.onMove(topRight, bottomRight);
                break;
            case DOWN:
                xBottomLeft = (int) (xPos + xDeltaWithBonus);
                yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());
                xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
                yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

                bottomLeft[0] = xBottomLeft + 1;
                bottomLeft[1] = yBottomLeft;
                bottomRight[0] = xBottomRight - 1;
                bottomRight[1] = yBottomRight;

                isTileWalkableVertical = movementListener.onMove(bottomLeft, bottomRight);
                break;
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
        // @@@ ENTITY-COLLISION @@@
        if (!colliding) {
            // @@@ TILE-COLLISION @@@
            if (isTileWalkableHorizontal) {
                xPos += xDeltaWithBonus;
            }
            if (isTileWalkableVertical) {
                yPos += yDeltaWithBonus;
            }

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
