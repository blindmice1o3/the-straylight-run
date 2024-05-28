package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.graphics.Canvas;

import java.util.List;

public class WorldScene extends Scene {
    public static final String TAG = WorldScene.class.getSimpleName();

    private static WorldScene worldScene;

    private WorldScene() {
    }

    public static WorldScene getInstance() {
        if (worldScene == null) {
            worldScene = new WorldScene();
        }
        return worldScene;
    }

    public void init() {

    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public List<Object> exit() {
        return null;
    }

    @Override
    public void enter(List<Object> args) {

    }
}
