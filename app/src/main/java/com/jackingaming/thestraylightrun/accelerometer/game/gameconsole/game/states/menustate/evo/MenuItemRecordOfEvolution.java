package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.Assets;

import java.util.concurrent.ExecutionException;

public class MenuItemRecordOfEvolution
        implements MenuStateImplEvo.MenuItem {
    public static final String TAG = MenuItemRecordOfEvolution.class.getSimpleName();

    private enum MenuItem {SAVE, LOAD;}

    private static MenuItemRecordOfEvolution uniqueInstance;
    transient private Game game;
    private String name;
    private MenuItem menuItem;

    transient private Bitmap imageCursor;
    private int xCursorSave;
    private int yCursorSave;
    private int xCursorLoad;
    private int yCursorLoad;
    transient private Paint paintFont;
    private int heightLine;
    private int xTextSave;
    private int yTextSave;
    private int xTextLoad;
    private int yTextLoad;
    transient private Paint paintBackground;
    transient private Rect rectBackground;

    private MenuItemRecordOfEvolution() {
        name = "Record of Evolution";
        menuItem = MenuItem.SAVE;
    }

    public static MenuItemRecordOfEvolution getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuItemRecordOfEvolution();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        imageCursor = Assets.leftOverworld0;

        paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(40f);
        paintFont.setColor(Color.WHITE);
        String textSave = "save";
        String textLoad = "load";
        Rect rectTextBounds = new Rect();
        paintFont.getTextBounds(textSave + textLoad, 0,
                textSave.length() + textLoad.length(), rectTextBounds);
        int widthTextSaveLoad = rectTextBounds.width();
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        heightLine = (int) (fm.bottom - fm.top + fm.leading);
        int margin = heightLine;


        paintBackground = new Paint();
        paintBackground.setAntiAlias(true);
        paintBackground.setColor(Color.LTGRAY);
        paintBackground.setAlpha(175);

        int x0RectBackground = MenuItemInitial.getInstance().calculateBackgroundPanelX0();
        int y0RectBackground = MenuItemInitial.getInstance().calculateBackgroundPanelY1();
        int widthRectBackground = (2 * (margin / 2)) + widthTextSaveLoad + (2 * imageCursor.getWidth()) + 4 + 4 + 4;
        int heightRectBackground = heightLine + (heightLine / 2);
        rectBackground = new Rect(x0RectBackground, y0RectBackground,
                x0RectBackground + widthRectBackground, y0RectBackground + heightRectBackground);

        xCursorSave = x0RectBackground + (margin / 2) - (imageCursor.getWidth() / 2);
        yCursorSave = y0RectBackground + (heightRectBackground / 2) - (imageCursor.getHeight() / 2);

        xTextSave = xCursorSave + imageCursor.getWidth() + 4;
        yTextSave = y0RectBackground + heightLine;

        xCursorLoad = xTextSave + widthTextSaveLoad / 2 + 4 + 4 + 4;
        yCursorLoad = yCursorSave;

        xTextLoad = xTextSave + widthTextSaveLoad / 2 + 4 + 4 + 4 + 4 + imageCursor.getWidth();
        yTextLoad = yTextSave;
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        if (game.getInputManager().isJustPressed(InputManager.Button.RIGHT) ||
                game.getInputManager().isJustPressed(InputManager.Button.LEFT)) {
            if (menuItem == MenuItem.SAVE) {
                menuItem = MenuItem.LOAD;
            } else if (menuItem == MenuItem.LOAD) {
                menuItem = MenuItem.SAVE;
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            if (menuItem == MenuItem.SAVE) {
                Log.d(TAG, getClass().getSimpleName() + ".update(long elapsed) a-button-justPressed SAVE");
                ////////////////////////
                game.saveViaUserInput();
                ////////////////////////
                MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
            } else if (menuItem == MenuItem.LOAD) {
                try {
                    Log.d(TAG, getClass().getSimpleName() + ".update(long elapsed) a-button-justPressed LOAD");
                    ////////////////////////
                    game.loadViaUserInput();
                    ////////////////////////
                    MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        // RE-DRAW MenuItemInitial
        MenuItemInitial.getInstance().render(canvas);

        // BACKGROUND_PANEL
        canvas.drawRect(rectBackground, paintBackground);

        // TEXT
        canvas.drawText("save", xTextSave, yTextSave, paintFont);
        canvas.drawText("load", xTextLoad, yTextLoad, paintFont);

        // CURSOR
        if (menuItem == MenuItem.SAVE) {
            canvas.drawBitmap(imageCursor, xCursorSave, yCursorSave, null);
        } else if (menuItem == MenuItem.LOAD) {
            canvas.drawBitmap(imageCursor, xCursorLoad, yCursorLoad, null);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Game getGame() {
        return game;
    }
}