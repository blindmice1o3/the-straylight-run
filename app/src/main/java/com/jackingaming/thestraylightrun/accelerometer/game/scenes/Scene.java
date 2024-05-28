package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;

import java.util.List;

public abstract class Scene {
    protected boolean paused = false;

    public abstract void update(long elapsed);

    public abstract void draw(Canvas canvas);

    public abstract List<Object> exit();

    public abstract void enter(Player player, List<Object> args);

    public abstract boolean isPaused();
}
