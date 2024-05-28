package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.graphics.Canvas;

import java.util.List;

public abstract class Scene {
    public abstract void update(long elapsed);

    public abstract void draw(Canvas canvas);

    public abstract List<Object> exit();

    public abstract void enter(List<Object> args);
}
