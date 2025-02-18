package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.StateManager;

public class BubblunStateManager extends StateManager {

    public enum StateLayer {
        DEFAULT,
        MOVEMENT,
        ATTACK;
    }

    @Override
    public void init(Game game, Entity e) {

    }
}
