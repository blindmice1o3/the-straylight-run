package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.Assets;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.SceneEvo;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class MenuItemInitial
        implements MenuStateImplEvo.MenuItem {
    private static MenuItemInitial uniqueInstance;
    transient private Game game;
    private String name;

    private List<MenuStateImplEvo.MenuItem> menuItems;
    private int index;

    transient private Bitmap imageCursor;
    private float xCursor;
    private float yCursor;

    private MenuItemInitial() {
        name = "Initial";

        menuItems = new ArrayList<MenuStateImplEvo.MenuItem>();
        menuItems.add(MenuItemEvolution.getInstance());
        menuItems.add(MenuItemCapability.getInstance());
        menuItems.add(MenuItemRecordOfEvolution.getInstance());
        index = 0;
    }

    public static MenuItemInitial getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuItemInitial();
        }
        return uniqueInstance;
    }

    transient private Paint paintBorder;
    transient private Paint paintBackgroundPanel;
    transient private Paint paintFont;
    private int x0BelowLabelHp;
    private int y0BelowLabelHp;
    private int widthBackgroundPanel;
    private int heightBackgroundPanel;
    private int heightLine;
    private int padding = Tile.WIDTH;
    private float xScaleFactorCursor;
    private float yScaleFactorCursor;
    private int xTextInitial;
    private int yTextInitial;

    @Override
    public void init(Game game) {
        this.game = game;

        initImage();

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(5f);
        paintBorder.setColor(Color.WHITE);

        paintBackgroundPanel = new Paint();
        paintBackgroundPanel.setAntiAlias(true);
        paintBackgroundPanel.setColor(Color.BLUE);

        paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setColor(Color.WHITE);
        paintFont.setTextSize(40f);

        int widthMaxText = 0;
        Rect boundsText = new Rect();
        for (MenuStateImplEvo.MenuItem menuItem : menuItems) {
            String nameMenuItem = menuItem.getName();
            paintFont.getTextBounds(nameMenuItem, 0, nameMenuItem.length(), boundsText);
            if (boundsText.width() > widthMaxText) {
                widthMaxText = boundsText.width();
            }
            if (boundsText.height() > heightLine) {
                heightLine = boundsText.height();
            }
        }

        widthBackgroundPanel = widthMaxText + (imageCursor.getWidth() + 3 + 3) + (2 * padding);
        heightBackgroundPanel = (menuItems.size() * heightLine) + (2 * padding);

        y0BelowLabelHp = ((SceneEvo) game.getSceneManager().getCurrentScene()).getHeadUpDisplay().calculateHpLabelY1();
        x0BelowLabelHp = y0BelowLabelHp;

        int widthHalfScreen = game.getWidthViewport() / 2;
        xScaleFactorCursor = (float) (widthHalfScreen - (2 * x0BelowLabelHp) - widthMaxText - (2 * padding)) / (float) (imageCursor.getWidth());
        yScaleFactorCursor = xScaleFactorCursor;

        xTextInitial = x0BelowLabelHp + padding + (imageCursor.getWidth() + 3 + 3);
        yTextInitial = y0BelowLabelHp + padding + heightLine;
    }

    public int calculateBackgroundPanelY1() {
        return y0BelowLabelHp + heightBackgroundPanel;
    }

    public int calculateBackgroundPanelX0() {
        return x0BelowLabelHp;
    }

    private void initImage() {
        imageCursor = Assets.leftOverworld0;
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        if (game.getInputManager().isJustPressed(InputManager.Button.DOWN)) {
            index++;
            if (index > (menuItems.size() - 1)) {
                index = 0;
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.UP)) {
            index--;
            if (index < 0) {
                index = (menuItems.size() - 1);
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            game.getStateManager().toggleMenuState();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            MenuStateImplEvo.MenuItem menuItem = menuItems.get(index);
            if (menuItem.getName().equals("Evolution")) {
                MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemEvolution();
            } else if (menuItem.getName().equals("Capability")) {
                MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemCapability();
            } else if (menuItem.getName().equals("Record of Evolution")) {
                MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemRecordOfEvolution();
            }
        }
    }

    private int sizeBorder = 3;
    private float roundnessBorder = 10f;

    @Override
    public void render(Canvas canvas) {
        // BACKGROUND PANEL
        RectF rectBackgroundPanel = new RectF(x0BelowLabelHp, y0BelowLabelHp,
                x0BelowLabelHp + widthBackgroundPanel,
                y0BelowLabelHp + heightBackgroundPanel);
        canvas.drawRoundRect(rectBackgroundPanel, roundnessBorder, roundnessBorder, paintBackgroundPanel);
        // BORDER
        canvas.drawRoundRect(rectBackgroundPanel, roundnessBorder, roundnessBorder, paintBorder);

        // TEXT
        int xText = xTextInitial;
        int yText = yTextInitial;
        for (MenuStateImplEvo.MenuItem menuItem : menuItems) {
            canvas.drawText(menuItem.getName(), xText, yText, paintFont);
            yText += heightLine;
        }

        // CURSOR
        xCursor = x0BelowLabelHp + padding;
        yCursor = (index * heightLine) + yTextInitial - (heightLine / 2) - (imageCursor.getHeight() / 2);
        canvas.drawBitmap(imageCursor, xCursor, yCursor, null);
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