package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

import java.util.concurrent.ExecutionException;

public class NewOrContinueState
        implements State {
    public static final String TAG = NewOrContinueState.class.getSimpleName();
    private static final int TEXT_PADDING = 16;

    private Game game;

    private String textNew;
    private String textLoad;
    private String textCursor;

    private int index;
    private Paint textPaint;
    private int textNewWidth, textNewHeight;
    private int textLoadWidth, textLoadHeight;
    private int textCursorWidth, textCursorHeight;
    private int screenWidth, screenHeight;

    private int xTextNew, yTextNew;
    private int xTextLoad, yTextLoad;
    private int xTextCursor, yTextCursor;

    public NewOrContinueState() {

    }

    @Override
    public void init(Game game) {
        this.game = game;
        index = 0;
        initTextPaint();

        textNew = game.getContext().getResources().getString(R.string.text_new);
        textLoad = game.getContext().getResources().getString(R.string.text_load);
        textCursor = game.getContext().getResources().getString(R.string.text_cursor);

        Rect boundsNew = new Rect();
        textPaint.getTextBounds(textNew, 0, textNew.length(), boundsNew);
        textNewWidth = boundsNew.width();
        textNewHeight = boundsNew.height();

        Rect boundsLoad = new Rect();
        textPaint.getTextBounds(textLoad, 0, textLoad.length(), boundsLoad);
        textLoadWidth = boundsLoad.width();
        textLoadHeight = boundsLoad.height();

        Rect boundsCursor = new Rect();
        textPaint.getTextBounds(textCursor, 0, textCursor.length(), boundsCursor);
        textCursorWidth = boundsCursor.width();
        textCursorHeight = boundsCursor.height();

        screenWidth = game.getWidthViewport();
        screenHeight = game.getHeightViewport();

        xTextNew = (screenWidth / 2) - (textNewWidth / 2);
        yTextNew = (screenHeight / 2) - (textNewHeight) - (TEXT_PADDING / 2);

        xTextLoad = (screenWidth / 2) - (textLoadWidth / 2);
        yTextLoad = (screenHeight / 2) + (TEXT_PADDING / 2);

        updateCursorPosition();
    }

    private void updateCursorPosition() {
        if (index == 0) {
            xTextCursor = xTextNew - textCursorWidth - TEXT_PADDING;
            yTextCursor = yTextNew;
        } else if (index == 1) {
            xTextCursor = xTextLoad - textCursorWidth - TEXT_PADDING;
            yTextCursor = yTextLoad;
        }
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(64);
    }

    @Override
    public void enter(Object[] args) {
        index = 0;
        updateCursorPosition();
    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        interpretInput();
    }

    private void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            if (index == 0) {
                game.getStateManager().pop();
            } else if (index == 1) {
                try {
                    ////////////////////////
                    game.loadViaUserInput();
                    ////////////////////////
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                game.getStateManager().pop();
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            // intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.MENU)) {
            // intentionally blank.
        }

        if (game.getInputManager().isJustPressed(InputManager.Button.UP)) {
            index--;

            if (index < 0) {
                index = 1;
            }

            updateCursorPosition();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.DOWN)) {
            index++;

            if (index > 1) {
                index = 0;
            }

            updateCursorPosition();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.LEFT)) {
            // intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.RIGHT)) {
            // intentionally blank.
        }
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawColor(Color.GREEN);

        canvas.drawText(textNew, xTextNew, yTextNew, textPaint);
        canvas.drawText(textLoad, xTextLoad, yTextLoad, textPaint);
        canvas.drawText(textCursor, xTextCursor, yTextCursor, textPaint);
    }
}
