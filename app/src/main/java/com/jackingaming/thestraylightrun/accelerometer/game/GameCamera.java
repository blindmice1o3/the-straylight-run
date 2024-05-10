package com.jackingaming.thestraylightrun.accelerometer.game;

import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;

public class GameCamera {
    private float xOffset, yOffset;
    private int xScreenSize, yScreenSize;
    private int widthWorld, heightWorld;

    public GameCamera(float xOffset, float yOffset,
                      int xScreenSize, int yScreenSize,
                      int widthWorld, int heightWorld) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xScreenSize = xScreenSize;
        this.yScreenSize = yScreenSize;
        this.widthWorld = widthWorld;
        this.heightWorld = heightWorld;
    }

    public void checkBlankSpace() {
        if (xOffset < 0) {
            xOffset = 0;
        } else if (xOffset > widthWorld - xScreenSize) {
            xOffset = widthWorld - xScreenSize;
        }

        if (yOffset < 0) {
            yOffset = 0;
        } else if (yOffset > heightWorld - yScreenSize) {
            yOffset = heightWorld - yScreenSize;
        }
    }

    public void centerOnEntity(Entity e) {
        xOffset = e.getxPos() - (xScreenSize / 2) + (e.getWidthSprite() / 2);
        yOffset = e.getyPos() - (yScreenSize / 2) + (e.getHeightSprite() / 2);

        checkBlankSpace();
    }

    public void move(int xAmount, int yAmount) {
        xOffset += xAmount;
        yOffset += yAmount;
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }
}
