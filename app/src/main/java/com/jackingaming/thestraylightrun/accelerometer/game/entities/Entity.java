package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

public abstract class Entity {
    public interface CollisionListener {
        void onJustCollided(Entity collided);
    }

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
    protected Map<Direction, Animation> sprites;

    protected boolean colliding;
    protected boolean justCollided;
    protected boolean cantCollide;

    public Entity(Map<Direction, Animation> sprites, CollisionListener collisionListener) {
        this.sprites = sprites;
        this.collisionListener = collisionListener;
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

    public Bitmap getFrame() {
        return sprites.get(direction).getFrame();
    }

    protected boolean checkEntityCollision(float xDelta, float yDelta) {
        for (Entity e : entities) {
            if (e.equals(this)) {
                continue;
            }

            if (xPos + xDelta < e.getxPos() + Entity.getWidthSprite() &&
                    xPos + xDelta + Entity.getWidthSprite() > e.getxPos() &&
                    yPos + yDelta < e.getyPos() + Entity.getHeightSprite() &&
                    yPos + yDelta + Entity.getHeightSprite() > e.getyPos()) {

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
