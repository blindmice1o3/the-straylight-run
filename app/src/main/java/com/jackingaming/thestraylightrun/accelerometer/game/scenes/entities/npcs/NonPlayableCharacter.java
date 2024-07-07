package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.Map;
import java.util.Random;

public class NonPlayableCharacter extends Entity {
    public static final String TAG = NonPlayableCharacter.class.getSimpleName();

    private Random random = new Random();

    private String id;
    private Bitmap portrait;
    private boolean stationary = false;

    public NonPlayableCharacter(String id, Bitmap portrait,
                                Map<Direction, AnimationDrawable> animationsByDirection, Direction directionFacing,
                                CollisionListener collisionListener, MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);

        this.id = id;
        this.portrait = portrait;
        this.direction = directionFacing;

        startAnimations();
    }

    public void turnStationaryOn() {
        stationary = true;
        updateAnimationsBasedOnStationary();
    }

    public void turnStationaryOff() {
        stationary = false;
        updateAnimationsBasedOnStationary();
    }

    public void toggleStationary() {
        Log.e(TAG, "toggleStationary()");
        stationary = !stationary;
        updateAnimationsBasedOnStationary();
    }

    public void updateAnimationsBasedOnStationary() {
        if (stationary) {
            stopAnimations();
        } else {
            startAnimations();
        }
    }

    @Override
    public void collided(Entity collider) {

    }

    private Direction directionHorizontal = Direction.RIGHT;
    private Direction directionVertical = Direction.DOWN;

    @Override
    public void update(Handler handler) {
        if (stationary) {
            // do nothing.
        } else {
            if (animatorMovementRight.isRunning() || animatorMovementLeft.isRunning() ||
                    animatorMovementDown.isRunning() || animatorMovementUp.isRunning()) {
                return;
            }

            // DIRECTION (changes 20% of the time)
            if (random.nextInt(5) < 1) {
                // determine direction
                int directionSpecified = random.nextInt(4);
                if (directionSpecified == 0) {
                    direction = Direction.LEFT;
                } else if (directionSpecified == 1) {
                    direction = Direction.RIGHT;
                } else if (directionSpecified == 2) {
                    direction = Direction.UP;
                } else if (directionSpecified == 3) {
                    direction = Direction.DOWN;
                }
            } else {
                // do nothing.
            }

            int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
            int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
            int[] topLeft = new int[2];
            int[] bottomLeft = new int[2];
            int[] topRight = new int[2];
            int[] bottomRight = new int[2];
            boolean isTileWalkable = false;
            int xDelta = 0;
            int yDelta = 0;
            switch (direction) {
                case UP:
                    xDelta = 0;
                    yDelta = -speedMovement;

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
                    xDelta = 0;
                    yDelta = speedMovement;

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
                    xDelta = -speedMovement;
                    yDelta = 0;

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
                    xDelta = speedMovement;
                    yDelta = 0;

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

    public String getId() {
        return id;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public boolean isStationary() {
        return stationary;
    }
}
