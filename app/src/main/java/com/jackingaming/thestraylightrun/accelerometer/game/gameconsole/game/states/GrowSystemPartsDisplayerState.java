package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Canvas;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.GrowSystemPartsDisplayerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.GrowSystemPartsDataCarrier;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class GrowSystemPartsDisplayerState
        implements State {
    public static final String TAG = GrowSystemPartsDisplayerState.class.getSimpleName();

    private Game game;
    private GrowSystemPartsDisplayerFragment growSystemPartsDisplayerFragment;
    private GrowSystemPartsDataCarrier growSystemPartsDataCarrier;
    private boolean isAnimationStart;
    private boolean isAnimationFinish;

    public GrowSystemPartsDisplayerState() {
    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    @Override
    public void enter(Object[] args) {
        Log.d(TAG, "enter()");

        isAnimationStart = false;
        isAnimationFinish = false;

        if (args != null) {
            Log.d(TAG, "args != null");

            growSystemPartsDataCarrier = (GrowSystemPartsDataCarrier) args[0];
        } else {
            Log.d(TAG, "args == null");

            // Do nothing.
        }
    }

    @Override
    public void exit() {
        Log.d(TAG, "exit()");

        // Do nothing.
    }

    @Override
    public void update(long elapsed) {
        if (!isAnimationStart) {
            isAnimationStart = true;

            growSystemPartsDisplayerFragment = GrowSystemPartsDisplayerFragment.newInstance(
                    growSystemPartsDataCarrier,
                    new GrowSystemPartsDisplayerFragment.AnimationListener() {
                        @Override
                        public void onAnimationFinish() {
                            Log.e(TAG, "GrowSystemPartsDisplayerFragment onAnimationFinish()");

                            isAnimationFinish = true;
                        }
                    });
            game.getTextboxListener().showGrowSystemPartsDisplayerFragment(
                    growSystemPartsDisplayerFragment
            );
        }

        growSystemPartsDisplayerFragment.update(elapsed);

        interpretInput();
    }

    private void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            if (isAnimationFinish) {
                game.getTextboxListener().showStatsDisplayer();
                game.getStateManager().pop();
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        game.getSceneManager().drawCurrentFrame(canvas);
    }
}
