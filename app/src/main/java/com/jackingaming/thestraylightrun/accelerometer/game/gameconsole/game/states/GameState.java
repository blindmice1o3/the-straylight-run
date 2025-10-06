package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;

public class GameState
        implements State {

    private Game game;

    public GameState() {

    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        game.getTimeManager().update(elapsed);
        game.getSceneManager().update(elapsed);
        GameCamera.getInstance().update(elapsed);
    }

    @Override
    public void render(Canvas canvas) {
        game.getSceneManager().drawCurrentFrame(canvas);
    }
}