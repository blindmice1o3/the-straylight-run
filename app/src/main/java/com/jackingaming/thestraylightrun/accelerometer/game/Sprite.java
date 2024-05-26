package com.jackingaming.thestraylightrun.accelerometer.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;

public class Sprite {
    private float x, y;
    private int screenWidth, screenHeight;

    private Bitmap image;

    private Rect bounds;

    public Sprite(int screenWidth, int screenHeight) {
        this.x = 20;
        this.y = 30;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void init(Bitmap image) {
        this.image = image;
        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, getBounds(), getScreenRect(), null);
    }

    public Rect getBounds() {
        return bounds;
    }

    public Rect getScreenRect() {
        return new Rect((int) x, (int) y, (int) x + (int) Entity.getWidthSpriteDst(), (int) y + (int) Entity.getHeightSpriteDst());
//        return new Rect((int) x, (int) y, (int) x + getBounds().width(), (int) y + getBounds().height());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
