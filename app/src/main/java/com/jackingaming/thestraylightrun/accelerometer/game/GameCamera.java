package com.jackingaming.thestraylightrun.accelerometer.game;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

public class GameCamera {
    private float xOffset, yOffset;
    private int widthDeviceScreen, heightDeviceScreen;
    private int widthWorldInPixels, heightWorldInPixels;

    public GameCamera(float xOffset, float yOffset,
                      int widthDeviceScreen, int heightDeviceScreen) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.widthDeviceScreen = widthDeviceScreen;
        this.heightDeviceScreen = heightDeviceScreen;
    }

    public void init(int widthWorldInPixels, int heightWorldInPixels) {
        this.widthWorldInPixels = widthWorldInPixels;
        this.heightWorldInPixels = heightWorldInPixels;
    }

    public void checkBlankSpace() {
        if (xOffset < 0) {
            xOffset = 0;
        } else if (xOffset > widthWorldInPixels - widthDeviceScreen) {
            xOffset = widthWorldInPixels - widthDeviceScreen;
        }

        if (yOffset < 0) {
            yOffset = 0;
        } else if (yOffset > heightWorldInPixels - heightDeviceScreen) {
            yOffset = heightWorldInPixels - heightDeviceScreen;
        }
    }

    public void centerOnEntity(Entity e) {
        xOffset = e.getxPos() - (widthDeviceScreen / 2) + (e.getWidthSpriteDst() / 2);
        yOffset = e.getyPos() - (heightDeviceScreen / 2) + (e.getHeightSpriteDst() / 2);

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
