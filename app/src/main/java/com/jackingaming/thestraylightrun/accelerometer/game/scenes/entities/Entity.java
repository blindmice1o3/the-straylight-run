package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import java.util.List;
import java.util.Map;

public abstract class Entity {
    public interface MovementListener {
        boolean onMove(int[] futureCorner1, int[] futureCorner2);
    }

    public interface CollisionListener {
        void onJustCollided(Entity collided);
    }

    protected MovementListener movementListener;
    private CollisionListener collisionListener;

    public static final long DEFAULT_MOVEMENT_DURATION = 1000L;

    protected static float widthSpriteDst, heightSpriteDst;
    protected static List<Entity> entities;

    protected float xPos, yPos = 0f;
    protected Direction direction = Direction.DOWN;
    protected Map<Direction, AnimationDrawable> animationsByDirection;

    protected ObjectAnimator animatorMovement;

    protected boolean colliding;
    protected boolean justCollided;
    protected boolean cantCollide;

    public Entity(Map<Direction, AnimationDrawable> animationsByDirection,
                  CollisionListener collisionListener, MovementListener movementListener) {
        this.animationsByDirection = animationsByDirection;
        this.collisionListener = collisionListener;
        this.movementListener = movementListener;

        animatorMovement =
                ObjectAnimator.ofFloat(this, "yPos", yPos - heightSpriteDst);
        animatorMovement.setDuration(DEFAULT_MOVEMENT_DURATION);
        animatorMovement.setInterpolator(new LinearInterpolator());
    }

    public void increaseMovementSpeed() {
        long quarterOfDefaultMovementDuration = DEFAULT_MOVEMENT_DURATION / 4;

        animatorMovement.setDuration(quarterOfDefaultMovementDuration);
    }

    public boolean isMovementSpeedIncreased() {
        return (animatorMovement.getDuration() < DEFAULT_MOVEMENT_DURATION);
    }

    public static void init(float widthSpriteDst, float heightSpriteDst) {
        Entity.widthSpriteDst = widthSpriteDst;
        Entity.heightSpriteDst = heightSpriteDst;
    }

    public static void replaceEntitiesForNewScene(List<Entity> entities) {
        Entity.entities = entities;
    }

    public abstract void collided(Entity collider);

    public abstract void update(Handler handler);

    public void startAnimations() {
        for (AnimationDrawable animationDrawable : animationsByDirection.values()) {
            animationDrawable.start();
        }
    }

    public void stopAnimations() {
        for (AnimationDrawable animationDrawable : animationsByDirection.values()) {
            animationDrawable.stop();
        }
    }

    public AnimationDrawable getAnimationDrawableBasedOnDirection() {
        return animationsByDirection.get(direction);
    }

    public Drawable getPausedFrameBasedOnDirection() {
        int index = -1;
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            index = 0;
        } else if (direction == Direction.UP || direction == Direction.DOWN) {
            index = 1;
        }
        return animationsByDirection.get(direction).getFrame(index);
    }

    private void checkTileLeft() {

    }

    protected void doMoveBasedOnDirection(Handler handler) {
        int xTopLeft, xBottomLeft, xTopRight, xBottomRight = -1;
        int yTopLeft, yBottomLeft, yTopRight, yBottomRight = -1;
        int[] topLeft = new int[2];
        int[] bottomLeft = new int[2];
        int[] topRight = new int[2];
        int[] bottomRight = new int[2];
        boolean isTileWalkable = false;
        float xDeltaFullStep = 0;
        float yDeltaFullStep = 0;
        String propertyName = null;
        float valueStart = -1f;
        float valueEnd = -1f;
        switch (direction) {
            case UP:
                propertyName = "yPos";
                valueStart = yPos;
                valueEnd = (yPos - heightSpriteDst);
                xDeltaFullStep = 0;
                yDeltaFullStep = -(heightSpriteDst - 1);

                // TILE-COLLISION
                xTopLeft = (int) (xPos);
                yTopLeft = (int) (yPos + yDeltaFullStep);
                xTopRight = (int) (xPos + widthSpriteDst);
                yTopRight = (int) (yPos + yDeltaFullStep);

                topLeft[0] = xTopLeft + 1;
                topLeft[1] = yTopLeft;
                topRight[0] = xTopRight - 1;
                topRight[1] = yTopRight;

                isTileWalkable = movementListener.onMove(topLeft, topRight);
                break;
            case DOWN:
                propertyName = "yPos";
                valueStart = yPos;
                valueEnd = (yPos + heightSpriteDst);
                xDeltaFullStep = 0;
                yDeltaFullStep = (heightSpriteDst - 1);

                // TILE-COLLISION
                xBottomLeft = (int) (xPos);
                yBottomLeft = (int) (yPos + yDeltaFullStep + heightSpriteDst);
                xBottomRight = (int) (xPos + widthSpriteDst);
                yBottomRight = (int) (yPos + yDeltaFullStep + heightSpriteDst);

                bottomLeft[0] = xBottomLeft + 1;
                bottomLeft[1] = yBottomLeft;
                bottomRight[0] = xBottomRight - 1;
                bottomRight[1] = yBottomRight;

                isTileWalkable = movementListener.onMove(bottomLeft, bottomRight);
                break;
            case LEFT:
                propertyName = "xPos";
                valueStart = xPos;
                valueEnd = (xPos - widthSpriteDst);
                xDeltaFullStep = -(widthSpriteDst - 1);
                yDeltaFullStep = 0;

                // TILE-COLLISION
                xTopLeft = (int) (xPos + xDeltaFullStep);
                yTopLeft = (int) (yPos);
                xBottomLeft = (int) (xPos + xDeltaFullStep);
                yBottomLeft = (int) (yPos + heightSpriteDst);

                topLeft[0] = xTopLeft;
                topLeft[1] = yTopLeft + 1;
                bottomLeft[0] = xBottomLeft;
                bottomLeft[1] = yBottomLeft - 1;

                isTileWalkable = movementListener.onMove(topLeft, bottomLeft);
                break;
            case RIGHT:
                propertyName = "xPos";
                valueStart = xPos;
                valueEnd = (xPos + widthSpriteDst);
                xDeltaFullStep = (widthSpriteDst - 1);
                yDeltaFullStep = 0;

                // TILE-COLLISION
                xTopRight = (int) (xPos + xDeltaFullStep + widthSpriteDst);
                yTopRight = (int) (yPos);
                xBottomRight = (int) (xPos + xDeltaFullStep + widthSpriteDst);
                yBottomRight = (int) (yPos + heightSpriteDst);

                topRight[0] = xTopRight;
                topRight[1] = yTopRight + 1;
                bottomRight[0] = xBottomRight;
                bottomRight[1] = yBottomRight - 1;

                isTileWalkable = movementListener.onMove(topRight, bottomRight);
                break;
        }

        // ENTITY-COLLISION
        colliding = checkEntityCollision(xDeltaFullStep, yDeltaFullStep);
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
            animatorMovement.setPropertyName(propertyName);
            animatorMovement.setFloatValues(valueStart, valueEnd);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    animatorMovement.start();
                }
            });
        }
    }

    protected boolean checkEntityCollision(float xDelta, float yDelta) {
        for (Entity e : entities) {
            if (e.equals(this)) {
                continue;
            }

            if (xPos + xDelta + 1 < e.getXPos() + Entity.getWidthSpriteDst() &&
                    xPos + xDelta + Entity.getWidthSpriteDst() - 1 > e.getXPos() &&
                    yPos + yDelta + 1 < e.getYPos() + Entity.getHeightSpriteDst() &&
                    yPos + yDelta + Entity.getHeightSpriteDst() - 1 > e.getYPos()) {

                /////////////////////////////////////////////////
                e.collided(this);

                if (justCollided && collisionListener != null) {
                    collisionListener.onJustCollided(e);
                }
                /////////////////////////////////////////////////

                return true;
            }
        }
        return false;
    }

    public static float getWidthSpriteDst() {
        return widthSpriteDst;
    }

    public static float getHeightSpriteDst() {
        return heightSpriteDst;
    }

    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    public float getXPos() {
        return xPos;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isColliding() {
        return colliding;
    }

    public boolean isJustCollided() {
        return justCollided;
    }

    public boolean isCantCollide() {
        return cantCollide;
    }
}
