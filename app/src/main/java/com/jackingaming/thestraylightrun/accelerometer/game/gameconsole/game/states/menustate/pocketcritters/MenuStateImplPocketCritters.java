package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.pocketcritters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.MenuStateImpl;

import java.util.concurrent.ExecutionException;

public class MenuStateImplPocketCritters extends MenuStateImpl {
    public static final String TAG = MenuStateImplPocketCritters.class.getSimpleName();

    public enum MenuItem {CRITTER_DEX, BELT_LIST, BACKPACK_LIST, LOAD, SAVE, OPTION, EXIT;}

    private Game game;
    private int indexMenu;

    private Bitmap menuBackgroundImage;
    private Bitmap menuCursorImage;

    private int scaleFactor;

    public MenuStateImplPocketCritters() {
        indexMenu = 0;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        initImage(game.getContext().getResources());
        float xScaleFactor = (game.getWidthViewport() / (float) menuBackgroundImage.getWidth());
        float yScaleFactor = (game.getHeightViewport() / (float) menuBackgroundImage.getHeight());
        Log.d(TAG, getClass().getSimpleName() + ".init(Game game) xScaleFactor, yScaleFactor: " + xScaleFactor + ", " + yScaleFactor);
        scaleFactor = (int) Math.min(xScaleFactor, yScaleFactor);
        Log.d(TAG, getClass().getSimpleName() + ".init(Game game) Math.min(xScaleFactor, yScaleFactor) -> scaleFactor: " + scaleFactor);

        initSourceBoundsOfMenuBackgroundImage();
        initDestinationBoundsOfMenuBackgroundImage();

        initSourceBoundsOfMenuCursorImage();
        initOrUpdateDestinationBoundsOfMenuCursorImage();

        initTextPaint();
        initPositionOfLoad();
    }

    private void initImage(Resources resources) {
        Bitmap startMenuSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.start_menu_state);
        menuBackgroundImage = Bitmap.createBitmap(startMenuSpriteSheet, 239, 3, 75, 124);
        menuCursorImage = Bitmap.createBitmap(startMenuSpriteSheet, 3, 162, 7, 7);
    }

    @Override
    public void enter(Object[] args) {
        indexMenu = 0;
        initOrUpdateDestinationBoundsOfMenuCursorImage();
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
            MenuItem selectedMenuItem = MenuItem.values()[indexMenu];
            switch (selectedMenuItem) {
                case CRITTER_DEX:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed CRITTER_DEX");
                    //////////////////////////////////
                    Player.getInstance().toggleForm();
                    //////////////////////////////////
                    game.getStateManager().toggleMenuState();
                    break;
                case BELT_LIST:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed BELT_LIST");
                    game.getStateManager().toggleMenuState();
                    break;
                case BACKPACK_LIST:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed BACKPACK_LIST");
                    game.getStateManager().toggleMenuState();
                    break;
                case LOAD:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed LOAD");
                    try {
                        ////////////////////////
                        game.loadViaUserInput();
                        ////////////////////////
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    game.getStateManager().toggleMenuState();
                    break;
                case SAVE:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed SAVE");
                    ////////////////////////
                    game.saveViaUserInput();
                    ////////////////////////
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed SAVE BEFORE toggleMenuState().");
                    game.getStateManager().toggleMenuState();
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed SAVE AFTER toggleMenuState().");
                    break;
                case OPTION:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed OPTION");
                    game.getStateManager().toggleMenuState();
                    break;
                case EXIT:
                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() a-button-justPressed EXIT");
                    ///////////////////////////////////////////////////
                    game.getStateManager().toggleMenuState();
                    ///////////////////////////////////////////////////
                    break;
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            game.getStateManager().toggleMenuState();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.MENU)) {
            game.getStateManager().toggleMenuState();
        }

        if (game.getInputManager().isJustPressed(InputManager.Button.UP)) {
            indexMenu--;
            if (indexMenu < 0) {
                indexMenu = (MenuItem.values().length - 1);
            }
            /////////////////////////////////////////////////
            initOrUpdateDestinationBoundsOfMenuCursorImage();
            /////////////////////////////////////////////////
        } else if (game.getInputManager().isJustPressed(InputManager.Button.DOWN)) {
            indexMenu++;
            if (indexMenu > (MenuItem.values().length - 1)) {
                indexMenu = 0;
            }
            /////////////////////////////////////////////////
            initOrUpdateDestinationBoundsOfMenuCursorImage();
            /////////////////////////////////////////////////
        } else if (game.getInputManager().isJustPressed(InputManager.Button.LEFT)) {
            // Intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.RIGHT)) {
            // Intentionally blank.
        }

        Log.d(TAG, getClass().getSimpleName() + ".interpretInput() FINISHED.");
    }

    private Rect sourceBoundsOfMenuBackgroundImage;

    private void initSourceBoundsOfMenuBackgroundImage() {
        sourceBoundsOfMenuBackgroundImage = new Rect(0, 0,
                menuBackgroundImage.getWidth(), menuBackgroundImage.getHeight());
    }

    private Rect destinationBoundsOfMenuBackgroundImage;

    private void initDestinationBoundsOfMenuBackgroundImage() {
        int horizontalEndOfScreenMinusWidthOfMenuBackgroundImage =
                game.getWidthViewport() - (menuBackgroundImage.getWidth() * scaleFactor);
        int verticalHalfOfScreenMinusHalfHeightOfMenuBackgroundImage =
                (game.getHeightViewport() / 2) - ((menuBackgroundImage.getHeight() / 2) * scaleFactor);
        int horizontalEndOfScreen = game.getWidthViewport();
        int verticalHalfOfScreenPlusHalfHeightOfMenuBackgroundImage =
                (game.getHeightViewport() / 2) + ((menuBackgroundImage.getHeight() / 2) * scaleFactor);

        destinationBoundsOfMenuBackgroundImage = new Rect(
                horizontalEndOfScreenMinusWidthOfMenuBackgroundImage,
                verticalHalfOfScreenMinusHalfHeightOfMenuBackgroundImage,
                horizontalEndOfScreen,
                verticalHalfOfScreenPlusHalfHeightOfMenuBackgroundImage);
    }

    private Rect sourceBoundsOfMenuCursorImage;

    private void initSourceBoundsOfMenuCursorImage() {
        sourceBoundsOfMenuCursorImage = new Rect(0, 0,
                menuCursorImage.getWidth(), menuCursorImage.getHeight());
    }

    private Rect destinationBoundsOfMenuCursorImage;
    private static final int X_OFFSET_INITIAL_IN_PIXELS = 5;
    private static final int Y_OFFSET_INITIAL_IN_PIXELS = 13;
    private static final int HEIGHT_BETWEEN_MENU_ITEMS_IN_PIXELS = 16;

    private void initOrUpdateDestinationBoundsOfMenuCursorImage() {
        int xOffset = X_OFFSET_INITIAL_IN_PIXELS;
        int yOffset = Y_OFFSET_INITIAL_IN_PIXELS + (indexMenu * HEIGHT_BETWEEN_MENU_ITEMS_IN_PIXELS);

        int x0MenuBackgroundImagePlusXOffsetOfCursor =
                destinationBoundsOfMenuBackgroundImage.left + (xOffset * scaleFactor);
        int y0MenuBackgroundImagePlusYOffsetOfCursor =
                destinationBoundsOfMenuBackgroundImage.top + (yOffset * scaleFactor);
        int x0MenuBackgroundImagePlusXOffsetOfCursorPlusWidthOfMenuCursorImage =
                destinationBoundsOfMenuBackgroundImage.left + (xOffset * scaleFactor) + (menuCursorImage.getWidth() * scaleFactor);
        int y0MenuBackgroundImagePlusYOffsetOfCursorPlusHeightOfMenuCursorImage =
                destinationBoundsOfMenuBackgroundImage.top + (yOffset * scaleFactor) + (menuCursorImage.getHeight() * scaleFactor);

        destinationBoundsOfMenuCursorImage = new Rect(
                x0MenuBackgroundImagePlusXOffsetOfCursor,
                y0MenuBackgroundImagePlusYOffsetOfCursor,
                x0MenuBackgroundImagePlusXOffsetOfCursorPlusWidthOfMenuCursorImage,
                y0MenuBackgroundImagePlusYOffsetOfCursorPlusHeightOfMenuCursorImage);
    }

    private Paint textPaint;

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(64);
    }

    private int xOffsetLoad;
    private int yOffsetLoad;

    private void initPositionOfLoad() {
        xOffsetLoad = (X_OFFSET_INITIAL_IN_PIXELS * scaleFactor) +
                (menuCursorImage.getWidth() * scaleFactor) + (1 * scaleFactor);
        yOffsetLoad = (Y_OFFSET_INITIAL_IN_PIXELS * scaleFactor) +
                (menuCursorImage.getHeight() * scaleFactor) +
                (MenuItem.LOAD.ordinal() * (HEIGHT_BETWEEN_MENU_ITEMS_IN_PIXELS * scaleFactor));
    }

    @Override
    public void render(Canvas canvas) {
        //DRAW BACKGROUND (horizontally aligned towards end, vertically centered)
        canvas.drawBitmap(menuBackgroundImage,
                sourceBoundsOfMenuBackgroundImage, destinationBoundsOfMenuBackgroundImage, null);

        //DRAW CURSOR
        canvas.drawBitmap(menuCursorImage,
                sourceBoundsOfMenuCursorImage, destinationBoundsOfMenuCursorImage, null);

        //FILL IN BLANK OF BACKGROUND IMAGE
        canvas.drawText("LOAD",
                (destinationBoundsOfMenuBackgroundImage.left + xOffsetLoad),
                (destinationBoundsOfMenuBackgroundImage.top + yOffsetLoad),
                textPaint);
    }
}