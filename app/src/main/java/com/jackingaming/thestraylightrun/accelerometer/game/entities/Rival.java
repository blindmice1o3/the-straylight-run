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
    }

    @Override
    public void collided(Entity collider) {

    }

    private Direction directionHorizontal = Direction.RIGHT;
    private Direction directionVertical = Direction.DOWN;

    @Override
    public void update() {
        // DIRECTION (changes 10% of the time)
        if (random.nextInt(10) < 1) {
            // determine direction
            if (random.nextInt(2) == 0) {
                directionHorizontal = Direction.LEFT;
            } else {
                directionHorizontal = Direction.RIGHT;
            }
            if (random.nextInt(2) == 0) {
                directionVertical = Direction.UP;
            } else {
                directionVertical = Direction.DOWN;
            }

            boolean isMoreHorizontalThanVertical = (random.nextInt(2) == 0);
            if (isMoreHorizontalThanVertical) {
                direction = directionHorizontal;
            } else {
                direction = directionVertical;
            }
        }
        // don't change direction (90% of the time)
        else {
            // do nothing
        }

        // POSITION
        int xDelta = 0;
        int yDelta = 0;
        boolean isDiagonal = (random.nextInt(2) == 0);
        if (isDiagonal) {
            if (directionHorizontal == Direction.RIGHT) {
                xDelta = speedMovement;
            } else {
                xDelta = -speedMovement;
            }
            if (directionVertical == Direction.UP) {
                yDelta = -speedMovement;
            } else {
                yDelta = speedMovement;
            }
        } else {
            switch (direction) {
                case LEFT:
                    xDelta = -speedMovement;
                    yDelta = 0;
                    break;
                case UP:
                    xDelta = 0;
                    yDelta = -speedMovement;
                    break;
                case RIGHT:
                    xDelta = speedMovement;
                    yDelta = 0;
                    break;
                case DOWN:
                    xDelta = 0;
                    yDelta = speedMovement;
                    break;
            }
        }

        float xDeltaWithBonus = (xDelta * speedBonus);
        float yDeltaWithBonus = (yDelta * speedBonus);

        // TILE-COLLISION
        int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
        int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
        int[] topLeft = new int[2];
        int[] bottomLeft = new int[2];
        int[] topRight = new int[2];
        int[] bottomRight = new int[2];
        boolean isTileWalkableHorizontal = false;
        boolean isTileWalkableVertical = false;
        if (directionHorizontal == Direction.LEFT) {
            xTopLeft = (int) (xPos + xDeltaWithBonus);
            yTopLeft = (int) (yPos);
            xBottomLeft = (int) (xPos + xDeltaWithBonus);
            yBottomLeft = (int) (yPos + Entity.getHeightSprite());

            topLeft[0] = xTopLeft;
            topLeft[1] = yTopLeft + 1;
            bottomLeft[0] = xBottomLeft;
            bottomLeft[1] = yBottomLeft - 1;

            isTileWalkableHorizontal = movementListener.onMove(topLeft, bottomLeft);
        } else {
            xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
            yTopRight = (int) (yPos);
            xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSprite());
            yBottomRight = (int) (yPos + Entity.getHeightSprite());

            topRight[0] = xTopRight;
            topRight[1] = yTopRight + 1;
            bottomRight[0] = xBottomRight;
            bottomRight[1] = yBottomRight - 1;

            isTileWalkableHorizontal = movementListener.onMove(topRight, bottomRight);
        }

        if (directionVertical == Direction.UP) {
            xTopLeft = (int) (xPos);
            yTopLeft = (int) (yPos + yDeltaWithBonus);
            xTopRight = (int) (xPos + Entity.getWidthSprite());
            yTopRight = (int) (yPos + yDeltaWithBonus);

            topLeft[0] = xTopLeft + 1;
            topLeft[1] = yTopLeft;
            topRight[0] = xTopRight - 1;
            topRight[1] = yTopRight;

            isTileWalkableVertical = movementListener.onMove(topLeft, topRight);
        } else {
            xBottomLeft = (int) (xPos);
            yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());
            xBottomRight = (int) (xPos + Entity.getWidthSprite());
            yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSprite());

            bottomLeft[0] = xBottomLeft + 1;
            bottomLeft[1] = yBottomLeft;
            bottomRight[0] = xBottomRight - 1;
            bottomRight[1] = yBottomRight;

            isTileWalkableVertical = movementListener.onMove(bottomLeft, bottomRight);
        }

        // ENTITY-COLLISION
        boolean collidingHorizontal = checkEntityCollision(xDeltaWithBonus, 0f);
        boolean collidingVertical = checkEntityCollision(0f, yDeltaWithBonus);
        colliding = (collidingHorizontal || collidingVertical);
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
        if (!collidingHorizontal && isTileWalkableHorizontal) {
            xPos += xDeltaWithBonus;
        }
        if (!collidingVertical && isTileWalkableVertical) {
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
