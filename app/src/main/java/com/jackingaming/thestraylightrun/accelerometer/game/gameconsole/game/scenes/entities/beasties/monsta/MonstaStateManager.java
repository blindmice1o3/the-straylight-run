package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.StateManager;

public class MonstaStateManager extends StateManager {
    public static final String PATROL_STATE = "PatrolState";
    public static final String BUBBLED_STATE = "BubbledState";

    private Monsta monsta;

    @Override
    public void init(Game game, Entity e) {
        states.put(PATROL_STATE, new PatrolState());
        states.put(BUBBLED_STATE, new BubbledState());

        State patrolState = states.get(PATROL_STATE);
        stateStack.add(patrolState);

        if (e instanceof Monsta) {
            monsta = (Monsta) e;

            for (State state : states.values()) {
                state.init(game, monsta);
            }
        }
    }
}
