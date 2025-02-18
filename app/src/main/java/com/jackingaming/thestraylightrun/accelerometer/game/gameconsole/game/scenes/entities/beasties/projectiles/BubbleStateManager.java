package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.StateManager;

public class BubbleStateManager extends StateManager {
    public static final String GROWING_STATE = "GrowingState";
    public static final String INFLATED_STATE = "InflatedState";

    private Bubble bubble;

    @Override
    public void init(Game game, Entity e) {
        states.put(GROWING_STATE, new GrowingState());
        states.put(INFLATED_STATE, new InflatedState());

        State growingState = states.get(GROWING_STATE);
        stateStack.add(growingState);

        if (e instanceof Bubble) {
            bubble = (Bubble) e;

            for (State state : states.values()) {
                state.init(game, bubble);
            }
        }
    }
}
