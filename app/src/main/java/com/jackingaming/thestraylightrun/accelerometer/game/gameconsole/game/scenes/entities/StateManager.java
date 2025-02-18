package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StateManager {
    public static final String TAG = StateManager.class.getSimpleName();

    protected Map<String, State> states = new HashMap<>();
    protected List<State> stateStack = new ArrayList<>();

    public abstract void init(Game game, Entity e);

    public void changeState(String nextStateTag) {
        Log.e(TAG, "changeState(String)");
        getCurrentState().exit();

        State nextState = states.get(nextStateTag);
        stateStack.add(nextState);

        getCurrentState().enter();
    }

    public void popState() {
        Log.e(TAG, "popState()");
        getCurrentState().exit();

        int indexLast = stateStack.size() - 1;
        stateStack.remove(indexLast);

        getCurrentState().enter();
    }

    public State getCurrentState() {
        int indexLast = stateStack.size() - 1;
        return stateStack.get(indexLast);
    }

    public void update(long elapsed) {
        getCurrentState().update(elapsed);
    }

    public boolean respondToEntityCollision(Entity e) {
        return getCurrentState().respondToEntityCollision(e);
    }

    public void respondToItemCollisionViaClick(Item i) {
        getCurrentState().respondToItemCollisionViaClick(i);
    }

    public void respondToItemCollisionViaMove(Item i) {
        getCurrentState().respondToItemCollisionViaMove(i);
    }

    public void respondToTransferPointCollision(String key) {
        getCurrentState().respondToTransferPointCollision(key);
    }
}
