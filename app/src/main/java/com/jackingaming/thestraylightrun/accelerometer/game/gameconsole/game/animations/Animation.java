package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class Animation {
    public static final String TAG = Animation.class.getSimpleName();

    private int index;
    private Bitmap[] frames;

    private int speed;
    private long timer;

    public Animation(Bitmap[] frames, int speed) {
        this.frames = frames;
        this.speed = speed;
        index = 0;
        timer = 0;
    }

    public void update(long elapsed) {
        timer += elapsed;

        if (timer > speed) {
            index++;
            if (index >= frames.length) {
                index = 0;
            }

            timer = 0;
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Bitmap getCurrentFrame() {
        return frames[index];
    }

    public static Bitmap flipImageHorizontally(Bitmap source) {
        Log.d(TAG, "Animation.flipImageHorizontally(Bitmap source)");
        int xCenter = source.getWidth() / 2;
        int yCenter = source.getHeight() / 2;

        Matrix matrix = new Matrix();
        //////////////////////////////////////////////////
        matrix.postScale(-1, 1, xCenter, yCenter);
        //////////////////////////////////////////////////

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap[] flipImageArrayHorizontally(Bitmap[] imagesUnflipped) {
        Log.d(TAG, "Animation.flipImageArrayHorizontally(Bitmap[] imagesUnflipped)");
        Bitmap[] imagesFlipped = new Bitmap[imagesUnflipped.length];
        for (int i = 0; i < imagesUnflipped.length; i++) {
            imagesFlipped[i] = Animation.flipImageHorizontally(imagesUnflipped[i]);
        }
        return imagesFlipped;
    }

    public int getIndex() {
        return index;
    }

    public void resetIndex() {
        index = 0;
    }

    public boolean isLastFrame() {
        return index == (frames.length - 1);
    }
}