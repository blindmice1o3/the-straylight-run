package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.GrowSystemPartsDataCarrier;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.MenuState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.MenuStateImpl;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo.MenuStateImplEvo;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
    transient private Game game;

    private NewOrContinueState newOrContinueState;
    private TextboxState textboxState;
    private GrowSystemPartsDisplayerState growSystemPartsDisplayerState;
    private GameState gameState;
    private MenuStateImpl menuState;

    private List<State> stateStack;

    public StateManager() {
        newOrContinueState = new NewOrContinueState();
        textboxState = new TextboxState();
        gameState = new GameState();

        stateStack = new ArrayList<State>();
        stateStack.add(gameState);
        stateStack.add(textboxState);
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

    public State getCurrentState() {
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
            menuState = new MenuState();
            menuState.init(game);
        }

        stateStack.add(menuState);

        getCurrentState().enter(null);
    }

    public void pushTextboxState(Bitmap imageForDialogue, String textToShow,
                                 TypeWriterDialogFragment.DismissListener dismissListener,
                                 TypeWriterTextView.TextCompletionListener textCompletionListener) {
        getCurrentState().exit();

        if (textboxState == null) {
            textboxState = new TextboxState();
            textboxState.init(game);
        }

        stateStack.add(textboxState);

        Object[] args = {imageForDialogue,
                textToShow,
                dismissListener,
                textCompletionListener
        };
        textboxState.enter(args);
    }

    public void pushGrowSystemPartsDisplayerState(GrowSystemPartsDataCarrier growSystemPartsDataCarrier) {
        getCurrentState().exit();

        if (growSystemPartsDisplayerState == null) {
            growSystemPartsDisplayerState = new GrowSystemPartsDisplayerState();
            growSystemPartsDisplayerState.init(game);
        }

        stateStack.add(growSystemPartsDisplayerState);

        Object[] args = {growSystemPartsDataCarrier};
        growSystemPartsDisplayerState.enter(args);
    }

    public TextboxState getTextboxState() {
        return textboxState;
    }
}
