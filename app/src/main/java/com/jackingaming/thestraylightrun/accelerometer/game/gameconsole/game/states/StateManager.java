package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.MenuStateImpl;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo.MenuStateImplEvo;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.pocketcritters.MenuStateImplPocketCritters;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
    private Game game;

    private NewOrContinueState newOrContinueState;
    private GameStateImpl gameState;
    private MenuStateImpl menuState;
    private TextboxStateImpl textboxState;

    private List<State> stateStack;

    public StateManager() {
        newOrContinueState = new NewOrContinueState();
        gameState = new GameStateImpl();

        stateStack = new ArrayList<State>();
        stateStack.add(gameState);
        stateStack.add(newOrContinueState);
    }

    public void init(Game game) {
        this.game = game;

        // TODO: possibly temporary... just testing different menu system for different games.
        if (game.getGameTitle().equals("Evo")) {
            menuState = MenuStateImplEvo.getInstance();
            menuState.init(game);
        }

        for (State state : stateStack) {
            state.init(game);
        }
    }

    public void update(long elapsed) {
        State stateCurrent = stateStack.get(getIndexOfTop());
        stateCurrent.update(elapsed);
    }

    public void render(Canvas canvas) {
        State stateCurrent = stateStack.get(getIndexOfTop());
        stateCurrent.render(canvas);
    }

    private int getIndexOfTop() {
        return stateStack.size() - 1;
    }

    private State getCurrentState() {
        return stateStack.get(getIndexOfTop());
    }

    public void pop() {
        State stateCurrent = getCurrentState();
        stateCurrent.exit();

        stateStack.remove(stateCurrent);

        getCurrentState().enter(null);
    }

    public void toggleMenuState() {
        if (getCurrentState() instanceof MenuStateImpl) {
            pop();
        } else {
            pushMenuState();
        }
    }

    private void pushMenuState() {
        getCurrentState().exit();

        if (menuState == null) {
            // TODO: possible temporary... this should work as a default.
            menuState = new MenuStateImplPocketCritters();
            menuState.init(game);
        }

        stateStack.add(menuState);

        getCurrentState().enter(null);
    }

    public void popTextboxState() {
        pop();
    }

    public void pushTextboxState(String text, float x, float y) {
        getCurrentState().exit();

        if (textboxState == null) {
            textboxState = new TextboxStateImpl();
            textboxState.init(game);
        }

        stateStack.add(textboxState);

        Object[] args = {text, x, y};
        textboxState.enter(args);
    }
}
