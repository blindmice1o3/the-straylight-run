package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateManager {
    public static final String TAG = StateManager.class.getSimpleName();

    public static final String PATROL_STATE = "PatrolState";
    public static final String BUBBLED_STATE = "BubbledState";

    private Monsta monsta;
    private Map<String, State> states;
    private List<State> stateStack;

    public StateManager() {
        states = new HashMap<>();
        states.put(PATROL_STATE, new PatrolState());
        states.put(BUBBLED_STATE, new BubbledState());

        stateStack = new ArrayList<>();
        State patrolState = states.get(PATROL_STATE);
        stateStack.add(patrolState);
    }

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

    public void init(Game game, Monsta monsta) {
        for (State state : states.values()) {
            state.init(game, monsta);
        }
    }

    public void update(long elapsed) {
        getCurrentState().update(elapsed);
    }

    boolean respondToEntityCollision(Entity e) {
        return getCurrentState().respondToEntityCollision(e);
    }

    void respondToItemCollisionViaClick(Item i) {
        getCurrentState().respondToItemCollisionViaClick(i);
    }

    void respondToItemCollisionViaMove(Item i) {
        getCurrentState().respondToItemCollisionViaMove(i);
    }

    void respondToTransferPointCollision(String key) {
        getCurrentState().respondToTransferPointCollision(key);
    }
}
