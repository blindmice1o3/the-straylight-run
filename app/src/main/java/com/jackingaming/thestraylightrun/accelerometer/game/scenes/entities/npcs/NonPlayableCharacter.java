package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.Map;
import java.util.Random;

public class NonPlayableCharacter extends Entity {
    public static final String TAG = NonPlayableCharacter.class.getSimpleName();

    private Random random = new Random();

    private String id;
    private boolean stationary = false;

    public NonPlayableCharacter(String id, Map<Direction, AnimationDrawable> animationsByDirection, Direction directionFacing,
                                CollisionListener collisionListener, MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);

        this.id = id;
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
    public void update() {
        if (stationary) {
            // DIRECTION (changes 10% of the time)
//            if (random.nextInt(10) < 1) {
//                // determine direction
//                switch (random.nextInt(4)) {
//                    case 0:
//                        direction = Direction.UP;
//                        break;
//                    case 1:
//                        direction = Direction.DOWN;
//                        break;
//                    case 2:
//                        direction = Direction.LEFT;
//                        break;
//                    case 3:
//                        direction = Direction.RIGHT;
//                        break;
//                }
//            }
        } else {
            // DIRECTION (changes 5% of the time)
            if (random.nextInt(20) < 1) {
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
                yBottomLeft = (int) (yPos + Entity.getHeightSpriteDst());

                topLeft[0] = xTopLeft;
                topLeft[1] = yTopLeft + 1;
                bottomLeft[0] = xBottomLeft;
                bottomLeft[1] = yBottomLeft - 1;

                isTileWalkableHorizontal = movementListener.onMove(topLeft, bottomLeft);
            } else {
                xTopRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSpriteDst());
                yTopRight = (int) (yPos);
                xBottomRight = (int) (xPos + xDeltaWithBonus + Entity.getWidthSpriteDst());
                yBottomRight = (int) (yPos + Entity.getHeightSpriteDst());

                topRight[0] = xTopRight;
                topRight[1] = yTopRight + 1;
                bottomRight[0] = xBottomRight;
                bottomRight[1] = yBottomRight - 1;

                isTileWalkableHorizontal = movementListener.onMove(topRight, bottomRight);
            }

            if (directionVertical == Direction.UP) {
                xTopLeft = (int) (xPos);
                yTopLeft = (int) (yPos + yDeltaWithBonus);
                xTopRight = (int) (xPos + Entity.getWidthSpriteDst());
                yTopRight = (int) (yPos + yDeltaWithBonus);

                topLeft[0] = xTopLeft + 1;
                topLeft[1] = yTopLeft;
                topRight[0] = xTopRight - 1;
                topRight[1] = yTopRight;

                isTileWalkableVertical = movementListener.onMove(topLeft, topRight);
            } else {
                xBottomLeft = (int) (xPos);
                yBottomLeft = (int) (yPos + yDeltaWithBonus + Entity.getHeightSpriteDst());
                xBottomRight = (int) (xPos + Entity.getWidthSpriteDst());
                yBottomRight = (int) (yPos + yDeltaWithBonus + Entity.getHeightSpriteDst());

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
        }
    }

    public String getId() {
        return id;
    }

    public boolean isStationary() {
        return stationary;
    }
}
