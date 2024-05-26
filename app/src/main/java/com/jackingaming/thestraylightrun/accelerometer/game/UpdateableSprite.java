package com.jackingaming.thestraylightrun.accelerometer.game;

import android.graphics.Rect;

public class UpdateableSprite extends Sprite {
    public static final String TAG = UpdateableSprite.class.getSimpleName();

    private final float xSpeed = 0.05f;
    private final float ySpeed = 0.05f;

    private int xDirection = 1;
    private int yDirection = 1;

    public UpdateableSprite(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    public void update(long elapsed) {
        float x = getX();
        float y = getY();

        Rect screenRect = getScreenRect();
        if (screenRect.left <= 0) {
            xDirection = 1;
        } else if (screenRect.right >= getScreenWidth()) {
            xDirection = -1;
        }
        if (screenRect.top <= 0) {
            yDirection = 1;
        } else if (screenRect.bottom >= getScreenHeight()) {
            yDirection = -1;
        }

        x += xDirection * xSpeed * elapsed;
        y += yDirection * ySpeed * elapsed;

        setX(x);
        setY(y);
    }
}
