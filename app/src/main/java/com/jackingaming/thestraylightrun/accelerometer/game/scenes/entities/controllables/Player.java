package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.Map;

public class Player extends Entity {

    private Direction directionHorizontal = Direction.RIGHT;
    private Direction directionVertical = Direction.DOWN;

    private ObjectAnimator animatorMovementRight;
    private ObjectAnimator animatorMovementLeft;
    private ObjectAnimator animatorMovementDown;
    private ObjectAnimator animatorMovementUp;

    public Player(Map<Direction, AnimationDrawable> animationsByDirection) {
        super(animationsByDirection, null, null);

        animatorMovementRight =
                ObjectAnimator.ofFloat(this, "xPos", xPos + widthSpriteDst);
        animatorMovementLeft =
                ObjectAnimator.ofFloat(this, "xPos", xPos - widthSpriteDst);
        animatorMovementDown =
                ObjectAnimator.ofFloat(this, "yPos", yPos + heightSpriteDst);
        animatorMovementUp =
                ObjectAnimator.ofFloat(this, "yPos", yPos - heightSpriteDst);

        animatorMovementRight.setDuration(1000L);
        animatorMovementLeft.setDuration(1000L);
        animatorMovementDown.setDuration(1000L);
        animatorMovementUp.setDuration(1000L);

        animatorMovementRight.setInterpolator(new LinearInterpolator());
        animatorMovementLeft.setInterpolator(new LinearInterpolator());
        animatorMovementDown.setInterpolator(new LinearInterpolator());
        animatorMovementUp.setInterpolator(new LinearInterpolator());

        startAnimations();
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update() {

    }

    public void increaseMovementSpeed() {
        animatorMovementRight.setDuration(250L);
        animatorMovementLeft.setDuration(250L);
        animatorMovementDown.setDuration(250L);
        animatorMovementUp.setDuration(250L);
    }

    public boolean isMovementSpeedIncreased() {
        return (animatorMovementRight.getDuration() < 1000L) &&
                (animatorMovementLeft.getDuration() < 1000L) &&
                (animatorMovementDown.getDuration() < 1000L) &&
                (animatorMovementUp.getDuration() < 1000L);
    }

    public void updateViaSensorEvent(Handler handler, float xDelta, float yDelta) {
        if (animatorMovementRight.isRunning() || animatorMovementLeft.isRunning() ||
                animatorMovementDown.isRunning() || animatorMovementUp.isRunning()) {
            return;
        }

        // DIRECTION
        if (Math.abs(yDelta) >= Math.abs(xDelta)) {
            // more vertical than horizontal (or equal)
            direction = (yDelta < 0) ? Direction.UP : Direction.DOWN;
        } else {
            // more horizontal than vertical
            direction = (xDelta < 0) ? Direction.LEFT : Direction.RIGHT;
        }

        int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
        int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
        int[] topLeft = new int[2];
        int[] bottomLeft = new int[2];
        int[] topRight = new int[2];
        int[] bottomRight = new int[2];
        boolean isTileWalkable = false;
        switch (direction) {
            case UP:
                // TILE-COLLISION
                xTopLeft = (int) (xPos);
                yTopLeft = (int) (yPos + yDelta);
                xTopRight = (int) (xPos + Entity.getWidthSpriteDst());
                yTopRight = (int) (yPos + yDelta);

                topLeft[0] = xTopLeft + 1;
                topLeft[1] = yTopLeft;
                topRight[0] = xTopRight - 1;
                topRight[1] = yTopRight;

                isTileWalkable = movementListener.onMove(topLeft, topRight);

                // ENTITY-COLLISION
                colliding = checkEntityCollision(0f, -(heightSpriteDst / 2));
                if (cantCollide && !colliding) {
                    cantCollide = false;
                } else if (justCollided) {
                    cantCollide = true;
                    justCollided = false;
                }
                if (!cantCollide && colliding) {
                    justCollided = true;
                }

                if (!colliding && isTileWalkable) {
                    animatorMovementUp.setFloatValues(yPos, yPos - heightSpriteDst);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            animatorMovementUp.start();
                        }
                    });
                }
                break;
            case DOWN:
                // TILE-COLLISION
                xBottomLeft = (int) (xPos);
                yBottomLeft = (int) (yPos + yDelta + Entity.getHeightSpriteDst());
                xBottomRight = (int) (xPos + Entity.getWidthSpriteDst());
                yBottomRight = (int) (yPos + yDelta + Entity.getHeightSpriteDst());

                bottomLeft[0] = xBottomLeft + 1;
                bottomLeft[1] = yBottomLeft;
                bottomRight[0] = xBottomRight - 1;
                bottomRight[1] = yBottomRight;

                isTileWalkable = movementListener.onMove(bottomLeft, bottomRight);

                // ENTITY-COLLISION
                colliding = checkEntityCollision(0f, (heightSpriteDst / 2));
                if (cantCollide && !colliding) {
                    cantCollide = false;
                } else if (justCollided) {
                    cantCollide = true;
                    justCollided = false;
                }
                if (!cantCollide && colliding) {
                    justCollided = true;
                }

                if (!colliding && isTileWalkable) {
                    animatorMovementDown.setFloatValues(yPos, yPos + heightSpriteDst);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            animatorMovementDown.start();
                        }
                    });
                }
                break;
            case LEFT:
                // TILE-COLLISION
                xTopLeft = (int) (xPos + xDelta);
                yTopLeft = (int) (yPos);
                xBottomLeft = (int) (xPos + xDelta);
                yBottomLeft = (int) (yPos + Entity.getHeightSpriteDst());

                topLeft[0] = xTopLeft;
                topLeft[1] = yTopLeft + 1;
                bottomLeft[0] = xBottomLeft;
                bottomLeft[1] = yBottomLeft - 1;

                isTileWalkable = movementListener.onMove(topLeft, bottomLeft);

                // ENTITY-COLLISION
                colliding = checkEntityCollision(-(widthSpriteDst / 2), 0f);
                if (cantCollide && !colliding) {
                    cantCollide = false;
                } else if (justCollided) {
                    cantCollide = true;
                    justCollided = false;
                }
                if (!cantCollide && colliding) {
                    justCollided = true;
                }

                if (!colliding && isTileWalkable) {
                    animatorMovementLeft.setFloatValues(xPos, xPos - widthSpriteDst);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            animatorMovementLeft.start();
                        }
                    });
                }
                break;
            case RIGHT:
                // TILE-COLLISION
                xTopRight = (int) (xPos + xDelta + Entity.getWidthSpriteDst());
                yTopRight = (int) (yPos);
                xBottomRight = (int) (xPos + xDelta + Entity.getWidthSpriteDst());
                yBottomRight = (int) (yPos + Entity.getHeightSpriteDst());

                topRight[0] = xTopRight;
                topRight[1] = yTopRight + 1;
                bottomRight[0] = xBottomRight;
                bottomRight[1] = yBottomRight - 1;

                isTileWalkable = movementListener.onMove(topRight, bottomRight);

                // ENTITY-COLLISION
                colliding = checkEntityCollision((widthSpriteDst / 2), 0f);
                if (cantCollide && !colliding) {
                    cantCollide = false;
                } else if (justCollided) {
                    cantCollide = true;
                    justCollided = false;
                }
                if (!cantCollide && colliding) {
                    justCollided = true;
                }

                if (!colliding && isTileWalkable) {
                    animatorMovementRight.setFloatValues(xPos, xPos + widthSpriteDst);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            animatorMovementRight.start();
                        }
                    });
                }
                break;
        }

//        directionHorizontal = (xDelta < 0) ? Direction.LEFT : Direction.RIGHT;
//        directionVertical = (yDelta < 0) ? Direction.UP : Direction.DOWN;
//
//        float xDeltaWithBonus = xDelta * speedBonus;
//        float yDeltaWithBonus = yDelta * speedBonus;
//
//        // TILE-COLLISION
//        int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
//        int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
//        int[] topLeft = new int[2];
//        int[] bottomLeft = new int[2];
//        int[] topRight = new int[2];
//        int[] bottomRight = new int[2];
//        boolean isTileWalkableHorizontal = false;
//        boolean isTileWalkableVertical = false;
//        if (directionHorizontal == Direction.LEFT) {
//            xTopLeft = (int) (xPos + xDeltaWithBonus);
//            yTopLeft = (int) (yPos);
//            xBottomLeft = (int) (xPos + xDeltaWithBonus);
//            yBottomLeft = (int) (yPos + Entity.getHeightSpriteDst());
//
//            topLeft[0] = xTopLeft;
//            topLeft[1] = yTopLeft + 1;
//            bottomLeft[0] = xBottomLeft;
//            bottomLeft[1] = yBottomLeft - 1;
//
//            isTileWalkableHorizontal = movementListener.onMove(topLeft, bottomLeft);
//        } else {
//            xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSpriteDst());
//            yTopRight = (int) (yPos);
//            xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSpriteDst());
//            yBottomRight = (int) (yPos + Entity.getHeightSpriteDst());
//
//            topRight[0] = xTopRight;
//            topRight[1] = yTopRight + 1;
//            bottomRight[0] = xBottomRight;
//            bottomRight[1] = yBottomRight - 1;
//
//            isTileWalkableHorizontal = movementListener.onMove(topRight, bottomRight);
//        }
//
//        if (directionVertical == Direction.UP) {
//            xTopLeft = (int) (xPos);
//            yTopLeft = (int) (yPos + yDeltaWithBonus);
//            xTopRight = (int) (xPos + Entity.getWidthSpriteDst());
//            yTopRight = (int) (yPos + yDeltaWithBonus);
//
//            topLeft[0] = xTopLeft + 1;
//            topLeft[1] = yTopLeft;
//            topRight[0] = xTopRight - 1;
//            topRight[1] = yTopRight;
//
//            isTileWalkableVertical = movementListener.onMove(topLeft, topRight);
//        } else {
//            xBottomLeft = (int) (xPos);
//            yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSpriteDst());
//            xBottomRight = (int) (xPos + Entity.getWidthSpriteDst());
//            yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSpriteDst());
//
//            bottomLeft[0] = xBottomLeft + 1;
//            bottomLeft[1] = yBottomLeft;
//            bottomRight[0] = xBottomRight - 1;
//            bottomRight[1] = yBottomRight;
//
//            isTileWalkableVertical = movementListener.onMove(bottomLeft, bottomRight);
//        }
//
//        // ENTITY-COLLISION
//        boolean collidingHorizontal = checkEntityCollision(xDeltaWithBonus, 0f);
//        boolean collidingVertical = checkEntityCollision(0f, yDeltaWithBonus);
//        colliding = (collidingHorizontal || collidingVertical);
//        if (cantCollide && !colliding) {
//            cantCollide = false;
//        } else if (justCollided) {
//            cantCollide = true;
//            justCollided = false;
//        }
//        if (!cantCollide && colliding) {
//            justCollided = true;
//        }
//
//        // POSITION
//        if (!collidingHorizontal && isTileWalkableHorizontal) {
//            xPos += xDeltaWithBonus;
//        }
//        if (!collidingVertical && isTileWalkableVertical) {
//            yPos += yDeltaWithBonus;
//        }
    }
}
