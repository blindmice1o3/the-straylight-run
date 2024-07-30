package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public interface State {
    void init(Game game);

    void enter(Object[] args);

    void exit();

    void update(long elapsed);

    void render(Canvas canvas);
}