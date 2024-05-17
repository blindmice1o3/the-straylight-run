package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

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

    public static final int DEFAULT_SPEED_MOVEMENT = 2;
    public static final float DEFAULT_SPEED_BONUS = 1f;

    protected static float xMax, yMax;
    protected static float xMin, yMin;
    protected static float widthSprite, heightSprite;
    protected static List<Entity> entities;

    protected float xPos, yPos = 0f;
    protected int speedMovement = DEFAULT_SPEED_MOVEMENT;
    protected float speedBonus = DEFAULT_SPEED_BONUS;
    protected Direction direction = Direction.DOWN;
    protected Map<Direction, AnimationDrawable> animationsByDirection;

    protected boolean colliding;
    protected boolean justCollided;
    protected boolean cantCollide;

    public Entity(Map<Direction, AnimationDrawable> animationsByDirection, CollisionListener collisionListener, MovementListener movementListener) {
        this.animationsByDirection = animationsByDirection;
        this.collisionListener = collisionListener;
        this.movementListener = movementListener;
    }

    public static void init(List<Entity> entities,
                            float widthSprite, float heightSprite,
                            float xMin, float xMax, float yMin, float yMax) {
        Entity.entities = entities;
        Entity.widthSprite = widthSprite;
        Entity.heightSprite = heightSprite;
        Entity.xMin = xMin;
        Entity.xMax = xMax;
        Entity.yMin = yMin;
        Entity.yMax = yMax;
    }

    public abstract void collided(Entity collider);

    public abstract void update();

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

    protected boolean checkEntityCollision(float xDelta, float yDelta) {
        for (Entity e : entities) {
            if (e.equals(this)) {
                continue;
            }

            if (xPos + xDelta + 1 < e.getxPos() + Entity.getWidthSprite() &&
                    xPos + xDelta + Entity.getWidthSprite() - 1 > e.getxPos() &&
                    yPos + yDelta < e.getyPos() + 1 + Entity.getHeightSprite() &&
                    yPos + yDelta + Entity.getHeightSprite() - 1 > e.getyPos()) {

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

    public static float getWidthSprite() {
        return widthSprite;
    }

    public static float getHeightSprite() {
        return heightSprite;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getSpeedBonus() {
        return speedBonus;
    }

    public void setSpeedBonus(float speedBonus) {
        this.speedBonus = speedBonus;
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
