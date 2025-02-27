package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class InflatedState
        implements State {
    public static final String TAG = InflatedState.class.getSimpleName();

    private Game game;
    private Bubble bubble;

    @Override
    public void enter() {
        bubble.bounceUpAndDown();
    }

    @Override
    public void exit() {

    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Bubble) {
            bubble = (Bubble) e;
        }
        // TODO:
    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        // intentionally empty (does NOT capture into bubble).
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item i) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item i) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }
}
