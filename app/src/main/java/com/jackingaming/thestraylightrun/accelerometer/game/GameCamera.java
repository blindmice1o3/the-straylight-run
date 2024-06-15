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
        if (widthWorldInPixels > widthDeviceScreen) {
            if (heightWorldInPixels > heightDeviceScreen) {
                // Scene is LARGER than viewport (BOTH HORIZONTALLY AND VERTICALLY)
                // Horizontal (LEFT)
                if (xOffset < 0) {
                    xOffset = 0;
                }
                // Horizontal (RIGHT)
                else if (xOffset > widthWorldInPixels - widthDeviceScreen) {
                    xOffset = widthWorldInPixels - widthDeviceScreen;
                }

                // Vertical (TOP)
                if (yOffset < 0) {
                    yOffset = 0;
                }
                // Vertical (BOTTOM)
                else if (yOffset > heightWorldInPixels - heightDeviceScreen) {
                    yOffset = heightWorldInPixels - heightDeviceScreen;
                }
            } else {
                // Scene is LARGER than viewport (ONLY HORIZONTALLY)
                // Horizontal (LEFT)
                if (xOffset < 0) {
                    xOffset = 0;
                }
                // Horizontal (RIGHT)
                else if (xOffset > widthWorldInPixels - widthDeviceScreen) {
                    xOffset = widthWorldInPixels - widthDeviceScreen;
                }

                // Vertical: always centered
                yOffset = (heightWorldInPixels - heightDeviceScreen) / 2;
            }
        } else {
            if (heightWorldInPixels > heightDeviceScreen) {
                // Scene is LARGER than viewport (ONLY VERTICALLY)
                // Horizontal: always centered
                xOffset = (widthWorldInPixels - widthDeviceScreen) / 2;

                // Vertical (TOP)
                if (yOffset < 0) {
                    yOffset = 0;
                }
                // Vertical (BOTTOM)
                else if (yOffset > heightWorldInPixels - heightDeviceScreen) {
                    yOffset = heightWorldInPixels - heightDeviceScreen;
                }
            } else {
                // Scene is SMALLER than viewport (BOTH HORIZONTALLY AND VERTICALLY)
                // Vertical: always centered
                xOffset = (widthWorldInPixels - widthDeviceScreen) / 2;

                // Horizontal: always centered
                yOffset = (heightWorldInPixels - heightDeviceScreen) / 2;
            }
        }
    }

    public void centerOnEntity(Entity e) {
        xOffset = e.getXPos() - (widthDeviceScreen / 2) + (e.getWidthSpriteDst() / 2);
        yOffset = e.getYPos() - (heightDeviceScreen / 2) + (e.getHeightSpriteDst() / 2);

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
