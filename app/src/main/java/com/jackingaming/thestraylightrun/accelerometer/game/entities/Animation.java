package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.graphics.Bitmap;

public class Animation {
    private int index;
    private Bitmap[] frames;

    public Animation(Bitmap[] frames) {
        this.frames = frames;
    }

    public Bitmap getFrame() {
        return frames[index];
    }

    public int getNumberOfFrames() {
        return frames.length;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
