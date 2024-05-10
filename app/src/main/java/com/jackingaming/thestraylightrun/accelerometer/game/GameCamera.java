package com.jackingaming.thestraylightrun.accelerometer.game;

import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;

public class GameCamera {
    private float xOffset, yOffset;
    private int xScreenSize, yScreenSize;

    public GameCamera(float xOffset, float yOffset, int xScreenSize, int yScreenSize) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xScreenSize = xScreenSize;
        this.yScreenSize = yScreenSize;
    }

    public void centerOnEntity(Entity e) {
        xOffset = e.getxPos() - (xScreenSize / 2);
        yOffset = e.getyPos() - (yScreenSize / 2);
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
