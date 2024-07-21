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

    public void moveLeft(Handler handler) {
        String propertyName = "xPos";
        float valueStart = xPos;
        float valueEnd = (xPos - widthSpriteDst);
        float xDeltaFullStep = -(widthSpriteDst - 1);
        float yDeltaFullStep = 0;

        validateAndMove(handler, propertyName,
                valueStart, valueEnd,
                xDeltaFullStep, yDeltaFullStep);
    }

    public void moveRight(Handler handler) {
        String propertyName = "xPos";
        float valueStart = xPos;
        float valueEnd = (xPos + widthSpriteDst);
        float xDeltaFullStep = (widthSpriteDst - 1);
        float yDeltaFullStep = 0;

        validateAndMove(handler, propertyName,
                valueStart, valueEnd,
                xDeltaFullStep, yDeltaFullStep);
    }

    public void moveUp(Handler handler) {
        String propertyName = "yPos";
        float valueStart = yPos;
        float valueEnd = (yPos - heightSpriteDst);
        float xDeltaFullStep = 0;
        float yDeltaFullStep = -(heightSpriteDst - 1);

        validateAndMove(handler, propertyName,
                valueStart, valueEnd,
                xDeltaFullStep, yDeltaFullStep);
    }

    private void validateAndMove(Handler handler, String propertyName,
                                 float valueStart, float valueEnd,
                                 float xDeltaFullStep, float yDeltaFullStep) {
        // TILE-COLLISION
        boolean isTileWalkable = false;
        switch (direction) {
            case LEFT:
                isTileWalkable = checkTileCollisionLeft(xDeltaFullStep, yDeltaFullStep);
                break;
            case UP:
                isTileWalkable = checkTileCollisionUp(xDeltaFullStep, yDeltaFullStep);
                break;
            case RIGHT:
                isTileWalkable = checkTileCollisionRight(xDeltaFullStep, yDeltaFullStep);
                break;
            case DOWN:
                isTileWalkable = checkTileCollisionDown(xDeltaFullStep, yDeltaFullStep);
                break;
        }

        // ENTITY-COLLISION
        boolean colliding = checkEntityCollision(xDeltaFullStep, yDeltaFullStep);
        updateStateOfEntityCollision(colliding);

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

    public void moveDown(Handler handler) {
        String propertyName = "yPos";
        float valueStart = yPos;
        float valueEnd = (yPos + heightSpriteDst);
        float xDeltaFullStep = 0;
        float yDeltaFullStep = (heightSpriteDst - 1);

        validateAndMove(handler, propertyName,
                valueStart, valueEnd,
                xDeltaFullStep, yDeltaFullStep);
    }

    private boolean checkTileCollisionLeft(float xDeltaFullStep, float yDeltaFullStep) {
        int xTopLeft = (int) (xPos + xDeltaFullStep);
        int yTopLeft = (int) (yPos);
        int xBottomLeft = (int) (xPos + xDeltaFullStep);
        int yBottomLeft = (int) (yPos + heightSpriteDst);

        int[] topLeft = new int[2];
        topLeft[0] = xTopLeft;
        topLeft[1] = yTopLeft + 1;
        int[] bottomLeft = new int[2];
        bottomLeft[0] = xBottomLeft;
        bottomLeft[1] = yBottomLeft - 1;

        return movementListener.onMove(topLeft, bottomLeft);
    }

    private boolean checkTileCollisionRight(float xDeltaFullStep, float yDeltaFullStep) {
        int xTopRight = (int) (xPos + xDeltaFullStep + widthSpriteDst);
        int yTopRight = (int) (yPos);
        int xBottomRight = (int) (xPos + xDeltaFullStep + widthSpriteDst);
        int yBottomRight = (int) (yPos + heightSpriteDst);

        int[] topRight = new int[2];
        topRight[0] = xTopRight;
        topRight[1] = yTopRight + 1;
        int[] bottomRight = new int[2];
        bottomRight[0] = xBottomRight;
        bottomRight[1] = yBottomRight - 1;

        return movementListener.onMove(topRight, bottomRight);
    }

    private boolean checkTileCollisionUp(float xDeltaFullStep, float yDeltaFullStep) {
        int xTopLeft = (int) (xPos);
        int yTopLeft = (int) (yPos + yDeltaFullStep);
        int xTopRight = (int) (xPos + widthSpriteDst);
        int yTopRight = (int) (yPos + yDeltaFullStep);

        int[] topLeft = new int[2];
        topLeft[0] = xTopLeft + 1;
        topLeft[1] = yTopLeft;
        int[] topRight = new int[2];
        topRight[0] = xTopRight - 1;
        topRight[1] = yTopRight;

        return movementListener.onMove(topLeft, topRight);
    }

    private boolean checkTileCollisionDown(float xDeltaFullStep, float yDeltaFullStep) {
        int xBottomLeft = (int) (xPos);
        int yBottomLeft = (int) (yPos + yDeltaFullStep + heightSpriteDst);
        int xBottomRight = (int) (xPos + widthSpriteDst);
        int yBottomRight = (int) (yPos + yDeltaFullStep + heightSpriteDst);

        int[] bottomLeft = new int[2];
        bottomLeft[0] = xBottomLeft + 1;
        bottomLeft[1] = yBottomLeft;
        int[] bottomRight = new int[2];
        bottomRight[0] = xBottomRight - 1;
        bottomRight[1] = yBottomRight;

        return movementListener.onMove(bottomLeft, bottomRight);
    }

    private void updateStateOfEntityCollision(boolean colliding) {
        if (cantCollide && !colliding) {
            cantCollide = false;
        } else if (justCollided) {
            cantCollide = true;
            justCollided = false;
        }
        if (!cantCollide && colliding) {
            justCollided = true;
        }
    }

    protected void doMoveBasedOnDirection(Handler handler) {
        switch (direction) {
            case UP:
                moveUp(handler);
                break;
            case DOWN:
                moveDown(handler);
                break;
            case LEFT:
                moveLeft(handler);
                break;
            case RIGHT:
                moveRight(handler);
                break;
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

    public boolean isJustCollided() {
        return justCollided;
    }

    public boolean isCantCollide() {
        return cantCollide;
    }
}
