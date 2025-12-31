package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class TitleScreenState
        implements State {
    public static final String TAG = TitleScreenState.class.getSimpleName();

    private Game game;
    private Bitmap imageTitleScreen;

    public TitleScreenState() {

    }

    @Override
    public void reload(Game game) {
        this.game = game;

        imageTitleScreen = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.title_screen_cultivate);
    }

    @Override
    public void init(Game game) {
        this.game = game;

        imageTitleScreen = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.title_screen_cultivate);
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        interpretInput();
    }

    private void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.A) ||
                game.getInputManager().isJustPressed(InputManager.Button.B) ||
                game.getInputManager().isJustPressed(InputManager.Button.MENU) ||
                game.getInputManager().isJustPressed(InputManager.Button.UP) ||
                game.getInputManager().isJustPressed(InputManager.Button.DOWN) ||
                game.getInputManager().isJustPressed(InputManager.Button.LEFT) ||
                game.getInputManager().isJustPressed(InputManager.Button.RIGHT)) {
            game.getStateManager().pop();
        }
    }

    @Override
    public void render(Canvas canvas) {
        Rect rectOfImage = new Rect(0, 0, imageTitleScreen.getWidth(), imageTitleScreen.getHeight());
        Rect rectOnScreen = new Rect(0, 0, game.getWidthViewport(), game.getHeightViewport());

        canvas.drawBitmap(imageTitleScreen, rectOfImage, rectOnScreen, null);
    }
}
