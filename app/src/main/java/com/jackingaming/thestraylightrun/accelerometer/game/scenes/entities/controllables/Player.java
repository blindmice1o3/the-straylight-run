package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.Map;

public class Player extends Entity {

    public Player(Map<Direction, AnimationDrawable> animationsByDirection) {
        super(animationsByDirection, null, null);

        startAnimations();
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update(Handler handler) {

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
                xTopRight = (int) (xPos + widthSpriteDst);
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
                yBottomLeft = (int) (yPos + yDelta + heightSpriteDst);
                xBottomRight = (int) (xPos + widthSpriteDst);
                yBottomRight = (int) (yPos + yDelta + heightSpriteDst);

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
                yBottomLeft = (int) (yPos + heightSpriteDst);

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
                xTopRight = (int) (xPos + xDelta + widthSpriteDst);
                yTopRight = (int) (yPos);
                xBottomRight = (int) (xPos + xDelta + widthSpriteDst);
                yBottomRight = (int) (yPos + heightSpriteDst);

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
    }
}
