package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.Assets;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishStateManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class MenuItemConfirmation
        implements MenuStateImplEvo.MenuItem {
    public static final String TAG = MenuItemConfirmation.class.getSimpleName();

    private enum MenuItem {YES, NO;}

    private static final int BORDER = 2 * Tile.WIDTH;
    private static MenuItemConfirmation uniqueInstance;
    transient private Game game;
    private String name;
    private int xCenterViewport;
    private int yCenterViewport;

    private TextPaint textPaint;
    private StaticLayout staticLayout;
    private int xStaticLayout;
    private int yStaticLayout;
    transient private Paint paintBackground;
    transient private Rect rectBackground;
    private int xTextYes;
    private int yTextYes;
    private int xTextNo;
    private int yTextNo;
    private int xCursorYes;
    private int yCursorYes;
    private int xCursorNo;
    private int yCursorNo;

    private MenuItem menuItem;
    transient private Bitmap imageCursor;
    private int xCursor;
    private int yCursor;

    private MenuItemConfirmation() {
        name = "Confirmation";
        menuItem = MenuItem.YES;
    }

    public static MenuItemConfirmation getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuItemConfirmation();
        }
        return uniqueInstance;
    }

    private void updateConfirmationMessage() {
        int indexBodyPartCategory = MenuItemEvolution.getInstance().getIndexBodyPartCategory();
        int indexBodyPartSelected = MenuItemEvolution.getInstance().getIndexBodyPartSelected();
        String confirmationMessage = null;
        switch (indexBodyPartCategory) {
            case 0:
                confirmationMessage = "Spend " + FishStateManager.Jaws.values()[indexBodyPartSelected].getCost() +
                        " experience point(s) for " + FishStateManager.Jaws.values()[indexBodyPartSelected] + " Jaws?";
                break;
            case 1:
                if (indexBodyPartSelected <= (FishStateManager.BodyTexture.values().length - 1)) {
                    confirmationMessage = "Spend " + FishStateManager.BodyTexture.values()[indexBodyPartSelected].getCost() +
                            " experience point(s) for " + FishStateManager.BodyTexture.values()[indexBodyPartSelected] + " BodyTexture?";
                } else if (indexBodyPartSelected > (FishStateManager.BodyTexture.values().length - 1)) {
                    confirmationMessage = "Spend " + FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getCost() +
                            " experience point(s) for " + FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)] + " BodySize?";
                }
                break;
            case 2:
                confirmationMessage = "Spend " + FishStateManager.FinPectoral.values()[indexBodyPartSelected].getCost() +
                        " experience point(s) for " + FishStateManager.FinPectoral.values()[indexBodyPartSelected] + " FinPectoral?";
                break;
            case 3:
                confirmationMessage = "Spend " + FishStateManager.Tail.values()[indexBodyPartSelected].getCost() +
                        " experience point(s) for " + FishStateManager.Tail.values()[indexBodyPartSelected] + " Tail?";
                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".render(Canvas canvas) switch(indexBodyPartCategory)'s default.");
                break;
        }

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);

        // STATIC_LAYOUT (size)
        int widthStaticLayout = game.getWidthViewport() / 2;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        float spacingMultiplier = 1;
        float spacingAddition = 0;
        boolean includePadding = false;
        staticLayout = new StaticLayout(confirmationMessage, textPaint, widthStaticLayout,
                alignment, spacingMultiplier, spacingAddition, includePadding);
        int heightStaticLayout = staticLayout.getHeight();

        // STATIC_LAYOUT (position)
        xCenterViewport = game.getWidthViewport() / 2;
        yCenterViewport = game.getHeightViewport() / 2;
        xStaticLayout = xCenterViewport - (widthStaticLayout / 2);
        yStaticLayout = yCenterViewport - (heightStaticLayout / 2) - ((heightLine + heightLine) / 2);

        // BACKGROUND PANEL
        paintBackground = new Paint();
        paintBackground.setColor(0xBB0000FF);
        rectBackground = new Rect(xStaticLayout - BORDER, yStaticLayout - BORDER,
                xStaticLayout + widthStaticLayout + BORDER,
                yStaticLayout + heightStaticLayout + (2 * heightLine) + BORDER);

        // TEXT/MENU_ITEMS (position)
        xTextYes = xStaticLayout + imageCursor.getWidth() + 3 + 3;
        yTextYes = yCenterViewport + (heightStaticLayout / 2);
        xTextNo = xTextYes;
        yTextNo = yTextYes + heightLine;

        // CURSOR (position)
        xCursorYes = xStaticLayout;
        yCursorYes = yTextYes - (heightLine / 2) + imageCursor.getHeight();
        xCursorNo = xStaticLayout;
        yCursorNo = yCursorYes + heightLine;

        updateCursorPosition();
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // CURSOR (Bitmap)
        imageCursor = Assets.leftOverworld0;

        // TEXT_PAINT (heightStaticLayout and heightLine)
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * game.getContext().getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFFFFFF);
    }

    @Override
    public void enter(Object[] args) {
        updateConfirmationMessage();
    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        interpretInput();
    }

    private void updateCursorPosition() {
        if (menuItem == MenuItem.YES) {
            xCursor = xCursorYes;
            yCursor = yCursorYes;
        } else if (menuItem == MenuItem.NO) {
            xCursor = xCursorNo;
            yCursor = yCursorNo;
        }
    }

    private void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.DOWN) ||
                game.getInputManager().isJustPressed(InputManager.Button.UP)) {
            if (menuItem == MenuItem.YES) {
                menuItem = MenuItem.NO;
            } else if (menuItem == MenuItem.NO) {
                menuItem = MenuItem.YES;
            }
            updateCursorPosition();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            if (menuItem == MenuItem.YES) {
                Form form = Player.getInstance().getForm();
                if (form instanceof FishForm) {
                    FishForm fishForm = (FishForm) form;

                    //upgrade body part (buying feature).
                    int expCost = 0;
                    int indexBodyPartCategory = MenuItemEvolution.getInstance().getIndexBodyPartCategory();
                    int indexBodyPartSelected = MenuItemEvolution.getInstance().getIndexBodyPartSelected();
                    switch (indexBodyPartCategory) {
                        case 0:
                            //upgrade jaws.
                            fishForm.getFishStateManager().setCurrentJaws((FishStateManager.Jaws.values()[indexBodyPartSelected]));
                            Log.d(TAG, "setJaws: " + (FishStateManager.Jaws.values()[indexBodyPartSelected]).toString());
                            expCost = FishStateManager.Jaws.values()[indexBodyPartSelected].getCost();
                            break;
                        //BodyTexture !!!AND!!! BodySize
                        case 1:
                            if (indexBodyPartSelected <= (FishStateManager.BodyTexture.values().length - 1)) {
                                fishForm.getFishStateManager().setCurrentBodyTexture((FishStateManager.BodyTexture.values()[indexBodyPartSelected]));
                                Log.d(TAG, "setBodyTexture: " + (FishStateManager.BodyTexture.values()[indexBodyPartSelected]).toString());
                                expCost = FishStateManager.BodyTexture.values()[indexBodyPartSelected].getCost();
                            } else if (indexBodyPartSelected > (FishStateManager.BodyTexture.values().length - 1)) {
                                fishForm.getFishStateManager().setCurrentBodySize((FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)]));
                                Log.d(TAG, "setBodySize: " + (FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)]).toString());
                                expCost = FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getCost();
                            }
                            break;
                        case 2:
                            fishForm.getFishStateManager().setCurrentFinPectoral((FishStateManager.FinPectoral.values()[indexBodyPartSelected]));
                            Log.d(TAG, "setFinPectoral: " + (FishStateManager.FinPectoral.values()[indexBodyPartSelected]).toString());
                            expCost = FishStateManager.FinPectoral.values()[indexBodyPartSelected].getCost();
                            break;
                        case 3:
                            fishForm.getFishStateManager().setCurrentTail((FishStateManager.Tail.values()[indexBodyPartSelected]));
                            Log.d(TAG, "setCurrentTail: " + (FishStateManager.Tail.values()[indexBodyPartSelected]).toString());
                            expCost = FishStateManager.Tail.values()[indexBodyPartSelected].getCost();
                            break;
                        case 4:
                            break;
                        default:
                            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() switch(indexBodyPartCategory)'s default.");
                            break;
                    }

                    /////////////////////////////////////
                    fishForm.updateHeadAndTailAnimations();
                    fishForm.updatePlayerStats();
                    /////////////////////////////////////

                    //deduct experience points
                    fishForm.setExperiencePoints((fishForm.getExperiencePoints() - expCost));

                    Log.d(TAG, getClass().getSimpleName() + ".interpretInput() " + expCost + " experience points DEDUCTED.");

                    // Pop twice to go back to MenuItemInitial.
                    MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
                    MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
                } else {
                    Log.d(TAG, "PLAYER IS NOT A FISH INSTANCE!!!");
                }
            } else if (menuItem == MenuItem.NO) {
                MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        // RE-DRAW MenuItemEvolution
        MenuItemEvolution.getInstance().render(canvas);

        // BACKGROUND PANEL
        canvas.drawRect(rectBackground, paintBackground);

        // TEXT (confirmation message)
        canvas.save();
        canvas.translate(xStaticLayout, yStaticLayout);
        staticLayout.draw(canvas);
        canvas.restore();

        // TEXT (menu items)
        canvas.drawText("yes", xTextYes, yTextYes, textPaint);
        canvas.drawText("no", xTextNo, yTextNo, textPaint);

        // CURSOR
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