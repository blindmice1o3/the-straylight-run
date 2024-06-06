package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.List;

public abstract class Scene {
    public static final long DEFAULT_TRANSFER_POINT_COOL_DOWN_THRESHOLD_IN_MILLI = 420L;

    protected boolean paused = false;

    public abstract void update(long elapsed);

    public abstract void draw(Canvas canvas);

    public abstract List<Object> exit();

    public abstract void enter(List<Object> args);

    public abstract boolean isPaused();

    public abstract boolean checkIsWalkableTile(int x, int y);

    public abstract List<Entity> getEntities();
}
