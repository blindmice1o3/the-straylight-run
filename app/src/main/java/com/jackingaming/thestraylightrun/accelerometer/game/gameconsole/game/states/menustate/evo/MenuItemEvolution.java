package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.Assets;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishStateManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.HashMap;
import java.util.Map;

public class MenuItemEvolution
        implements MenuStateImplEvo.MenuItem {
    public enum PlayerStatsKey {
        CLASSIFICATION, HIT_POINT_MAX, BITING, STRENGTH, KICK, STRIKE, HORN, DEFENSE_POWER, AGILITY, JUMPING_ABILITY;
    }

    private static MenuItemEvolution uniqueInstance;
    transient private Game game;
    private String name;

    transient private Bitmap imageBackground;
    transient private Bitmap imageCursor;
    private int y1ImageBackground;
    private float xScaleFactor;
    private float yScaleFactor;
    transient private Rect rectSource;
    transient private Rect rectDestination;

    transient private Paint paintYellowBorder;
    transient private Rect rectJaws;
    transient private Rect rectBody;
    transient private Rect rectHandsAndFeet;
    transient private Rect rectTail;
    transient private Rect rectDoNotEvolve;

    private int indexBodyPartCategory;
    private int indexBodyPartSelected;

    private MenuItemEvolution() {
        name = "Evolution";
        indexBodyPartCategory = 0;
        indexBodyPartSelected = 0;
    }

    public static MenuItemEvolution getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuItemEvolution();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        initImage(game.getContext().getResources());

        xScaleFactor = (float) game.getWidthViewport() / (float) imageBackground.getWidth();
        yScaleFactor = xScaleFactor;

        rectSource = new Rect(0, 0, imageBackground.getWidth(), imageBackground.getHeight());
        rectDestination = new Rect(0, 0, game.getWidthViewport(),
                (int) (imageBackground.getHeight() * yScaleFactor));
        y1ImageBackground = (int) (imageBackground.getHeight() * yScaleFactor);

        paintYellowBorder = new Paint();
        paintYellowBorder.setStyle(Paint.Style.STROKE);
        paintYellowBorder.setStrokeWidth(10f);
        paintYellowBorder.setColor(Color.YELLOW);

        rectJaws = new Rect((int) (12 * xScaleFactor), (int) (61 * yScaleFactor),
                (int) (106 * xScaleFactor), (int) (107 * yScaleFactor));
        rectBody = new Rect((int) (352 * xScaleFactor), (int) (61 * yScaleFactor),
                (int) (446 * xScaleFactor), (int) (107 * yScaleFactor));
        rectHandsAndFeet = new Rect((int) (452 * xScaleFactor), (int) (61 * yScaleFactor),
                (int) (586 * xScaleFactor), (int) (107 * yScaleFactor));
        rectTail = new Rect((int) (152 * xScaleFactor), (int) (113 * yScaleFactor),
                (int) (246 * xScaleFactor), (int) (159 * yScaleFactor));
        rectDoNotEvolve = new Rect((int) (432 * xScaleFactor), (int) (113 * yScaleFactor),
                (int) (586 * xScaleFactor), (int) (159 * yScaleFactor));
    }

    private void initImage(Resources resources) {
        imageBackground = Assets.mainMenuEVOLUTION;
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
        if (game.getInputManager().isJustPressed(InputManager.Button.RIGHT)) {
            indexBodyPartCategory++;
            if (indexBodyPartCategory > 4) {
                indexBodyPartCategory = 0;
            }
            indexBodyPartSelected = 0;
        } else if (game.getInputManager().isJustPressed(InputManager.Button.LEFT)) {
            indexBodyPartCategory--;
            if (indexBodyPartCategory < 0) {
                indexBodyPartCategory = 4;
            }
            indexBodyPartSelected = 0;
        } else if (game.getInputManager().isJustPressed(InputManager.Button.DOWN)) {
            indexBodyPartSelected++;

            switch (indexBodyPartCategory) {
                case 0:
                    if (indexBodyPartSelected > (FishStateManager.Jaws.values().length - 1)) {
                        indexBodyPartSelected = 0;
                    }
                    break;
                case 1:
                    if (indexBodyPartSelected >
                            (FishStateManager.BodyTexture.values().length - 1) +
                                    (FishStateManager.BodySize.values().length)) {
                        indexBodyPartSelected = 0;
                    }
                    break;
                case 2:
                    if (indexBodyPartSelected > (FishStateManager.FinPectoral.values().length - 1)) {
                        indexBodyPartSelected = 0;
                    }
                    break;
                case 3:
                    if (indexBodyPartSelected > (FishStateManager.Tail.values().length - 1)) {
                        indexBodyPartSelected = 0;
                    }
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.UP)) {
            indexBodyPartSelected--;

            if (indexBodyPartSelected < 0) {
                switch (indexBodyPartCategory) {
                    case 0:
                        indexBodyPartSelected = FishStateManager.Jaws.values().length - 1;
                        break;
                    case 1:
                        indexBodyPartSelected =
                                (FishStateManager.BodyTexture.values().length - 1) +
                                        (FishStateManager.BodySize.values().length);
                        break;
                    case 2:
                        indexBodyPartSelected = FishStateManager.FinPectoral.values().length - 1;
                        break;
                    case 3:
                        indexBodyPartSelected = FishStateManager.Tail.values().length - 1;
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            Form form = Player.getInstance().getForm();
            if (form instanceof FishForm) {
                FishForm fishForm = (FishForm) form;
                switch (indexBodyPartCategory) {
                    case 0:
                        //check if player already have this choice currently equipped.
                        if (fishForm.getFishStateManager().getCurrentJaws() == FishStateManager.Jaws.values()[indexBodyPartSelected]) {
                            String alreadyEquipped = "Already equipped, can NOT buy FishStateManager.Jaws." +
                                    FishStateManager.Jaws.values()[indexBodyPartSelected] + "!";
                            game.showToastMessageLong(alreadyEquipped);
                        }
                        //not enough experience points to buy element at current index.
                        else if (fishForm.getExperiencePoints() < FishStateManager.Jaws.values()[indexBodyPartSelected].getCost()) {
                            String notEnoughExpPoints = "Not enough experience points to buy FishStateManager.Jaws." +
                                    FishStateManager.Jaws.values()[indexBodyPartSelected] + ". You need " +
                                    (FishStateManager.Jaws.values()[indexBodyPartSelected].getCost() - fishForm.getExperiencePoints()) +
                                    " more experience points.";
                            game.showToastMessageLong(notEnoughExpPoints);
                        }
                        //check if player have enough experience points.
                        else if (fishForm.getExperiencePoints() >= FishStateManager.Jaws.values()[indexBodyPartSelected].getCost()) {
                            /////////////////////////////////////////////
                            MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemConfirmation();
                            /////////////////////////////////////////////
                        }
                        break;
                    case 1:
                        //BODY_TEXTURE
                        if (indexBodyPartSelected <= (FishStateManager.BodyTexture.values().length - 1)) {
                            //check if player already have this choice currently equipped.
                            if (fishForm.getFishStateManager().getCurrentBodyTexture() ==
                                    FishStateManager.BodyTexture.values()[indexBodyPartSelected]) {
                                String alreadyEquipped = "Already equipped, can NOT buy FishStateManager.BodyTexture." +
                                        FishStateManager.BodyTexture.values()[indexBodyPartSelected] + "!";
                                game.showToastMessageLong(alreadyEquipped);
                            }
                            //not enough experience points to buy element at current index.
                            else if (fishForm.getExperiencePoints() < FishStateManager.BodyTexture.values()[indexBodyPartSelected].getCost()) {
                                String notEnoughExpPoints = "Not enough experience points to buy FishStateManager.BodyTexture." +
                                        FishStateManager.BodyTexture.values()[indexBodyPartSelected] + ". You need " +
                                        (FishStateManager.BodyTexture.values()[indexBodyPartSelected].getCost() - fishForm.getExperiencePoints()) +
                                        " more experience points.";
                                game.showToastMessageLong(notEnoughExpPoints);
                            }
                            //check if player have enough experience points.
                            else if (fishForm.getExperiencePoints() >= FishStateManager.BodyTexture.values()[indexBodyPartSelected].getCost()) {
                                /////////////////////////////////////////////
                                MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemConfirmation();
                                /////////////////////////////////////////////
                            }
                        }
                        //BODY_SIZE
                        else if (indexBodyPartSelected > (FishStateManager.BodyTexture.values().length - 1)) {
                            //check if player already have this choice currently equipped.
                            if (fishForm.getFishStateManager().getCurrentBodySize() ==
                                    FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)]) {
                                String alreadyEquipped = "Already equipped, can NOT buy FishStateManager.BodySize." +
                                        FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)] +
                                        "!";
                                game.showToastMessageLong(alreadyEquipped);
                            }
                            //not enough experience points to buy element at current index.
                            else if (fishForm.getExperiencePoints() <
                                    FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getCost()) {
                                String notEnoughExpPoints = "Not enough experience points to buy FishStateManager.BodySize." +
                                        FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)] +
                                        ". You need " +
                                        (FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getCost() - fishForm.getExperiencePoints()) +
                                        " more experience points.";
                                game.showToastMessageLong(notEnoughExpPoints);
                            }
                            //check if player have enough experience points.
                            else if (fishForm.getExperiencePoints() >=
                                    FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getCost()) {
                                /////////////////////////////////////////////
                                MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemConfirmation();
                                /////////////////////////////////////////////
                            }
                        }
                        break;
                    case 2:
                        //check if player already have this choice currently equipped.
                        if (fishForm.getFishStateManager().getCurrentFinPectoral() == FishStateManager.FinPectoral.values()[indexBodyPartSelected]) {
                            String alreadyEquipped = "Already equipped, can NOT buy FishStateManager.FinPectoral. " +
                                    FishStateManager.FinPectoral.values()[indexBodyPartSelected] + "!";
                            game.showToastMessageLong(alreadyEquipped);
                        }
                        //not enough experience points to buy element at current index.
                        else if (fishForm.getExperiencePoints() < FishStateManager.FinPectoral.values()[indexBodyPartSelected].getCost()) {
                            String notEnoughExpPoints = "Not enough experience points to buy FishStateManager.FinPectoral." +
                                    FishStateManager.FinPectoral.values()[indexBodyPartSelected] + ". You need " +
                                    (FishStateManager.FinPectoral.values()[indexBodyPartSelected].getCost() - fishForm.getExperiencePoints()) +
                                    " more experience points.";
                            game.showToastMessageLong(notEnoughExpPoints);
                        }
                        //check if player have enough experience points.
                        else if (fishForm.getExperiencePoints() >= FishStateManager.FinPectoral.values()[indexBodyPartSelected].getCost()) {
                            /////////////////////////////////////////////
                            MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemConfirmation();
                            /////////////////////////////////////////////
                        }
                        break;
                    case 3:
                        //check if player already have this choice currently equipped.
                        if (fishForm.getFishStateManager().getCurrentTail() == FishStateManager.Tail.values()[indexBodyPartSelected]) {
                            String alreadyEquipped = "Already equipped, can NOT buy FishStateManager.Tail." +
                                    FishStateManager.Tail.values()[indexBodyPartSelected] + "!";
                            game.showToastMessageLong(alreadyEquipped);
                        }
                        //not enough experience points to buy element at current index.
                        else if (fishForm.getExperiencePoints() < FishStateManager.Tail.values()[indexBodyPartSelected].getCost()) {
                            String notEnoughExpPoints = "Not enough experience points to buy FishStateManager.Tail." +
                                    FishStateManager.Tail.values()[indexBodyPartSelected] + ". You need " +
                                    (FishStateManager.Tail.values()[indexBodyPartSelected].getCost() - fishForm.getExperiencePoints()) +
                                    " more experience points.";
                            game.showToastMessageLong(notEnoughExpPoints);
                        }
                        //check if player have enough experience points.
                        else if (fishForm.getExperiencePoints() >= FishStateManager.Tail.values()[indexBodyPartSelected].getCost()) {
                            /////////////////////////////////////////////
                            MenuStateImplEvo.getInstance().getMenuItemManager().pushMenuItemConfirmation();
                            /////////////////////////////////////////////
                        }
                        break;
                    case 4:
                        MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int padding = Tile.WIDTH;

    @Override
    public void render(Canvas canvas) {
        // BACKGROUND PANEL *** TOP ***
        canvas.drawBitmap(imageBackground, rectSource, rectDestination, null);
        // BACKGROUND PANEL *** BOTTOM-LEFT ***
        Paint paintPanelForBodyPartSelected = new Paint();
        paintPanelForBodyPartSelected.setAntiAlias(true);
        paintPanelForBodyPartSelected.setColor(Color.LTGRAY);
        paintPanelForBodyPartSelected.setAlpha(175);
        Rect rectPanelForBodyPartSelected = new Rect(0, y1ImageBackground,
                0 + (int) (0.5f * game.getWidthViewport()), y1ImageBackground + (int) (0.5f * game.getHeightViewport()));
        canvas.drawRect(rectPanelForBodyPartSelected, paintPanelForBodyPartSelected);

        // *** BOTTOM-RIGHT: CAPABILITY (BASE/BACKGROUND PANEL) ***
        renderCapability(canvas);

        // CURSOR AND TEXT
        Paint paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(16 * game.getContext().getResources().getDisplayMetrics().density);
        paintFont.setColor(Color.WHITE);
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);
        int xTextInitial = 0 + padding + imageCursor.getWidth() + 3 + 3;
        int yTextInitial = y1ImageBackground + heightLine;
        int xText = xTextInitial;
        int yText = yTextInitial;
        // FISH CURSOR
        switch (indexBodyPartCategory) {
            case 0:
                // *** TOP ***
                // YELLOW-BORDER CURSOR
                canvas.drawRect(rectJaws, paintYellowBorder);

                // *** BOTTOM-LEFT ***
                for (int i = 0; i < FishStateManager.Jaws.values().length; i++) {
                    FishStateManager.Jaws jaws = FishStateManager.Jaws.values()[i];

                    // *** SELECTED ***
                    if (i == indexBodyPartSelected) {
                        // FISH CURSOR
                        int xCursor = 0 + padding;
                        int yCursor = yText - (heightLine / 2) + imageCursor.getHeight();
                        canvas.drawBitmap(imageCursor, xCursor, yCursor, null);

                        // *** BOTTOM-RIGHT: CAPABILITY (BONUSES) ***
                        renderBonusesFromJaws(canvas);
                    }

                    // BODY-PART NAME AND COST
                    canvas.drawText(jaws.toString() + " " + jaws.getCost(),
                            xText, yText, paintFont);

                    // UPDATE TO NEXT LINE
                    yText += heightLine;
                }
                break;
            case 1:
                // *** TOP ***
                // YELLOW-BORDER CURSOR
                canvas.drawRect(rectBody, paintYellowBorder);

                // *** BOTTOM-LEFT (BODY_TEXTURE) ***
                for (int i = 0; i < FishStateManager.BodyTexture.values().length; i++) {
                    FishStateManager.BodyTexture bodyTexture = FishStateManager.BodyTexture.values()[i];

                    // *** SELECTED ***
                    if (indexBodyPartSelected <= (FishStateManager.BodyTexture.values().length - 1)) {
                        if (i == indexBodyPartSelected) {
                            // FISH CURSOR
                            int xCursor = 0 + padding;
                            int yCursor = yText - (heightLine / 2) + imageCursor.getHeight();
                            canvas.drawBitmap(imageCursor, xCursor, yCursor, null);

                            // *** BOTTOM-RIGHT: CAPABILITY (BONUSES) ***
                            renderBonusesFromBodyTexture(canvas);
                        }
                    }

                    // BODY-PART NAME AND COST
                    canvas.drawText(bodyTexture.toString() + " " + bodyTexture.getCost(),
                            xText, yText, paintFont);

                    // UPDATE TO NEXT LINE
                    yText += heightLine;
                }

                // *** BOTTOM-LEFT (BODY_SIZE) ***
                for (int i = 0; i < FishStateManager.BodySize.values().length; i++) {
                    FishStateManager.BodySize bodySize = FishStateManager.BodySize.values()[i];

                    // *** SELECTED ***
                    if (indexBodyPartSelected > (FishStateManager.BodyTexture.values().length - 1)) {
                        if (i == (indexBodyPartSelected - (FishStateManager.BodyTexture.values().length))) {
                            // FISH CURSOR
                            int xCursor = 0 + padding;
                            int yCursor = yText - (heightLine / 2) + imageCursor.getHeight();
                            canvas.drawBitmap(imageCursor, xCursor, yCursor, null);

                            // *** BOTTOM-RIGHT: CAPABILITY (BONUSES) ***
                            renderBonusesFromBodySize(canvas);
                        }
                    }

                    // BODY-PART NAME AND COST
                    canvas.drawText(bodySize.toString() + " " + bodySize.getCost(),
                            xText, yText, paintFont);

                    // UPDATE TO NEXT LINE
                    yText += heightLine;
                }
                break;
            case 2:
                // *** TOP ***
                // YELLOW-BORDER CURSOR
                canvas.drawRect(rectHandsAndFeet, paintYellowBorder);

                // *** BOTTOM-LEFT ***
                for (int i = 0; i < FishStateManager.FinPectoral.values().length; i++) {
                    FishStateManager.FinPectoral finPectoral = FishStateManager.FinPectoral.values()[i];

                    // *** SELECTED ***
                    if (i == indexBodyPartSelected) {
                        // FISH CURSOR
                        int xCursor = 0 + padding;
                        int yCursor = yText - (heightLine / 2) + imageCursor.getHeight();
                        canvas.drawBitmap(imageCursor, xCursor, yCursor, null);

                        // *** BOTTOM-RIGHT: CAPABILITY (BONUSES) ***
                        renderBonusesFromHandsAndFeet(canvas);
                    }

                    // BODY-PART NAME AND COST
                    canvas.drawText(finPectoral.toString() + " " + finPectoral.getCost(),
                            xText, yText, paintFont);

                    // UPDATE TO NEXT LINE
                    yText += heightLine;
                }
                break;
            case 3:
                // *** TOP ***
                // YELLOW-BORDER CURSOR
                canvas.drawRect(rectTail, paintYellowBorder);

                // *** BOTTOM-LEFT ***
                for (int i = 0; i < FishStateManager.Tail.values().length; i++) {
                    FishStateManager.Tail tail = FishStateManager.Tail.values()[i];

                    // *** SELECTED ***
                    if (i == indexBodyPartSelected) {
                        // FISH CURSOR
                        int xCursor = 0 + padding;
                        int yCursor = yText - (heightLine / 2) + imageCursor.getHeight();
                        canvas.drawBitmap(imageCursor, xCursor, yCursor, null);

                        // *** BOTTOM-RIGHT: CAPABILITY (BONUSES) ***
                        renderBonusesFromTail(canvas);
                    }

                    // BODY-PART NAME AND COST
                    canvas.drawText(tail.toString() + " " + tail.getCost(),
                            xText, yText, paintFont);

                    // UPDATE TO NEXT LINE
                    yText += heightLine;
                }
                break;
            case 4:
                // *** TOP ***
                // YELLOW-BORDER CURSOR
                canvas.drawRect(rectDoNotEvolve, paintYellowBorder);

                // *** BOTTOM-LEFT ***
                canvas.drawText("Do NoT eVoLvE", xTextInitial, yTextInitial, paintFont);

                // *** BOTTOM-RIGHT: CAPABILITY ***
                break;
            default:
                canvas.drawText("DeFaUlT", xTextInitial, yTextInitial, paintFont);
                break;
        }
    }

    public void renderCapability(Canvas canvas) {
        int x0PlayerStatsBox = (game.getWidthViewport() / 2) + 50;
        int y0PlayerStatsBox = y1ImageBackground;
        int x1PlayerStatsBox = game.getWidthViewport() - 10;
        int y1PlayerStatsBox = y1ImageBackground + (game.getHeightViewport() / 2);
        Rect rectPlayerStatsBox = new Rect(x0PlayerStatsBox, y0PlayerStatsBox, x1PlayerStatsBox, y1PlayerStatsBox);

        //background textbox.
        Paint paintPlayerStatsBox = new Paint();
        paintPlayerStatsBox.setAntiAlias(true);
        paintPlayerStatsBox.setColor(Color.LTGRAY);
        paintPlayerStatsBox.setAlpha(175);
        canvas.drawRect(rectPlayerStatsBox, paintPlayerStatsBox);

        Player player = Player.getInstance();
        Form form = player.getForm();
        if (form instanceof FishForm) {
            FishForm fishForm = (FishForm) form;
            //////////////////////////////////////
            Map<PlayerStatsKey, String> playerStats = new HashMap<PlayerStatsKey, String>();
            playerStats.put(PlayerStatsKey.CLASSIFICATION, form.getClass().getSimpleName());
            playerStats.put(PlayerStatsKey.HIT_POINT_MAX, Integer.toString(fishForm.getHealthMax()));
            playerStats.put(PlayerStatsKey.BITING, Integer.toString(fishForm.getFishStateManager().getDamageBite()));
            playerStats.put(PlayerStatsKey.STRENGTH, Integer.toString(fishForm.getFishStateManager().getDamageStrength()));
            playerStats.put(PlayerStatsKey.KICK, Integer.toString(fishForm.getFishStateManager().getDamageKick()));
            playerStats.put(PlayerStatsKey.STRIKE, Integer.toString(fishForm.getFishStateManager().getDamageStrike()));
            playerStats.put(PlayerStatsKey.HORN, Integer.toString(fishForm.getFishStateManager().getDamageHorn()));
            playerStats.put(PlayerStatsKey.DEFENSE_POWER, Integer.toString(fishForm.getFishStateManager().getDefense()));
            playerStats.put(PlayerStatsKey.AGILITY, Integer.toString(fishForm.getFishStateManager().getAgility()));
            playerStats.put(PlayerStatsKey.JUMPING_ABILITY, Integer.toString(fishForm.getFishStateManager().getJump()));
            //////////////////////////////////////

            //text.
            Paint paintFont = new Paint();
            paintFont.setAntiAlias(true);
            paintFont.setTextSize(20f);
            paintFont.setColor(Color.WHITE);
            Paint.FontMetrics fm = paintFont.getFontMetrics();
            int heightLine = (int) (fm.bottom - fm.top + fm.leading);

            int xStringKey = x0PlayerStatsBox + 10;
            int yString = y0PlayerStatsBox + heightLine;
            int xStringValue = game.getWidthViewport() - 100;
            for (int i = 0; i < PlayerStatsKey.values().length; i++) {
                //CLASSIFICATION (Fish.Form.FISH).
                if (i == 0) {
                    canvas.drawText(PlayerStatsKey.values()[i].toString() + ": ", xStringKey, yString, paintFont);
                    //do NOT add "P".
                    canvas.drawText(playerStats.get(PlayerStatsKey.values()[i]), xStringValue, yString, paintFont);
                    //one-regular-new-line AND one-EXTRA-new-line.
                    yString += (heightLine + heightLine);
                }
                //POINTS (NUMERIC VALUES).
                else {
                    canvas.drawText(PlayerStatsKey.values()[i].toString() + ": ", xStringKey, yString, paintFont);
                    //add "P".
                    canvas.drawText(playerStats.get(PlayerStatsKey.values()[i]) + "P", xStringValue, yString, paintFont);
                    //just one-regular-new-line.
                    yString += heightLine;
                }
            }
        }
    }

    private void renderBonusesFromJaws(Canvas canvas) {
        int x0PlayerStatsBox = (game.getWidthViewport() / 2) + 50;
        int y0PlayerStatsBox = y1ImageBackground;
        int x1PlayerStatsBox = game.getWidthViewport() - 10;
        int y1PlayerStatsBox = y1ImageBackground + (game.getHeightViewport() / 2);

        //text.
        Paint paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(20f);
        paintFont.setColor(Color.WHITE);
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);

        ////////////////////////////////////////////////////////////////////////////////////////////

        int xStringBonuses = game.getWidthViewport() - 50;
        int yStringBonuses = y0PlayerStatsBox + 24 + (2 * heightLine); //skip 2 lines for PlayerStatsKey.CLASSIFICATION.

        Form form = Player.getInstance().getForm();
        if (form instanceof FishForm) {
            FishForm fishForm = (FishForm) form;

            //i starting at 1 because PlayerStatsKey.CLASSIFICATION  was accounted for when initializing yStringBonuses.
            for (int i = 1; i < PlayerStatsKey.values().length; i++) {
                //PlayerStats beginning with PlayerStatsKey.Hit_Point_Max.
                if (i == PlayerStatsKey.BITING.ordinal()) {
                    int newDamage = FishStateManager.Jaws.values()[indexBodyPartSelected].getDamageBiteBonus() -
                            fishForm.getFishStateManager().getCurrentJaws().getDamageBiteBonus();
                    if (newDamage > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newDamage, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newDamage < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newDamage), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newDamage == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newDamage), xStringBonuses, yStringBonuses, paintFont);
                    }
                }

                //just one-regular-new-line.
                yStringBonuses += heightLine;
            }
        }
    }

    private void renderBonusesFromBodyTexture(Canvas canvas) {
        int x0PlayerStatsBox = (game.getWidthViewport() / 2) + 50;
        int y0PlayerStatsBox = y1ImageBackground;
        int x1PlayerStatsBox = game.getWidthViewport() - 10;
        int y1PlayerStatsBox = y1ImageBackground + (game.getHeightViewport() / 2);

        //text.
        Paint paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(20f);
        paintFont.setColor(Color.WHITE);
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);

        ////////////////////////////////////////////////////////////////////////////////////////////

        int xStringBonuses = game.getWidthViewport() - 50;
        int yStringBonuses = y0PlayerStatsBox + 24 + (2 * heightLine); //skip 2 lines for PlayerStatsKey.CLASSIFICATION.

        Form form = Player.getInstance().getForm();
        if (form instanceof FishForm) {
            FishForm fishForm = (FishForm) form;

            //i starting at 1 because PlayerStatsKey.CLASSIFICATION  was accounted for when initializing yStringBonuses.
            for (int i = 1; i < PlayerStatsKey.values().length; i++) {
                //PlayerStats beginning with PlayerStatsKey.Hit_Point_Max.
                if (i == PlayerStatsKey.DEFENSE_POWER.ordinal()) {
                    int newDefense = FishStateManager.BodyTexture.values()[indexBodyPartSelected].getDefenseBonus() -
                            fishForm.getFishStateManager().getCurrentBodyTexture().getDefenseBonus();
                    if (newDefense > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newDefense, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newDefense < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newDefense), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newDefense == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newDefense), xStringBonuses, yStringBonuses, paintFont);
                    }
                }

                //just one-regular-new-line.
                yStringBonuses += heightLine;
            }
        }
    }

    private void renderBonusesFromBodySize(Canvas canvas) {
        int x0PlayerStatsBox = (game.getWidthViewport() / 2) + 50;
        int y0PlayerStatsBox = y1ImageBackground;
        int x1PlayerStatsBox = game.getWidthViewport() - 10;
        int y1PlayerStatsBox = y1ImageBackground + (game.getHeightViewport() / 2);

        //text.
        Paint paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(20f);
        paintFont.setColor(Color.WHITE);
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);

        ////////////////////////////////////////////////////////////////////////////////////////////

        int xStringBonuses = game.getWidthViewport() - 50;
        int yStringBonuses = y0PlayerStatsBox + 24 + (2 * heightLine); //skip 2 lines for PlayerStatsKey.CLASSIFICATION.

        Form form = Player.getInstance().getForm();
        if (form instanceof FishForm) {
            FishForm fishForm = (FishForm) form;

            //i starting at 1 because PlayerStatsKey.CLASSIFICATION  was accounted for when initializing yStringBonuses.
            for (int i = 1; i < PlayerStatsKey.values().length; i++) {
                //PlayerStats beginning with PlayerStatsKey.Hit_Point_Max.
                if (i == PlayerStatsKey.HIT_POINT_MAX.ordinal()) {
                    int newHitPointMax = FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getHealthMaxBonus() -
                            fishForm.getFishStateManager().getCurrentBodySize().getHealthMaxBonus();
                    if (newHitPointMax > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newHitPointMax, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newHitPointMax < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newHitPointMax), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newHitPointMax == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newHitPointMax), xStringBonuses, yStringBonuses, paintFont);
                    }
                } else if (i == PlayerStatsKey.STRENGTH.ordinal()) {
                    int newStrength = FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getStrengthBonus() -
                            fishForm.getFishStateManager().getCurrentBodySize().getStrengthBonus();
                    if (newStrength > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newStrength, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newStrength < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newStrength), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newStrength == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newStrength), xStringBonuses, yStringBonuses, paintFont);
                    }
                } else if (i == PlayerStatsKey.DEFENSE_POWER.ordinal()) {
                    int newDefense = FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getDefenseBonus() -
                            fishForm.getFishStateManager().getCurrentBodySize().getDefenseBonus();
                    if (newDefense > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newDefense, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newDefense < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newDefense), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newDefense == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newDefense), xStringBonuses, yStringBonuses, paintFont);
                    }
                } else if (i == PlayerStatsKey.AGILITY.ordinal()) {
                    int newAgility = FishStateManager.BodySize.values()[indexBodyPartSelected - (FishStateManager.BodyTexture.values().length)].getAgilityBonus() -
                            fishForm.getFishStateManager().getCurrentBodySize().getAgilityBonus();
                    if (newAgility > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newAgility, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newAgility < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newAgility), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newAgility == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newAgility), xStringBonuses, yStringBonuses, paintFont);
                    }
                }

                //just one-regular-new-line.
                yStringBonuses += heightLine;
            }
        }
    }

    private void renderBonusesFromHandsAndFeet(Canvas canvas) {
        int x0PlayerStatsBox = (game.getWidthViewport() / 2) + 50;
        int y0PlayerStatsBox = y1ImageBackground;
        int x1PlayerStatsBox = game.getWidthViewport() - 10;
        int y1PlayerStatsBox = y1ImageBackground + (game.getHeightViewport() / 2);

        //text.
        Paint paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(20f);
        paintFont.setColor(Color.WHITE);
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);

        ////////////////////////////////////////////////////////////////////////////////////////////

        int xStringBonuses = game.getWidthViewport() - 50;
        int yStringBonuses = y0PlayerStatsBox + 24 + (2 * heightLine); //skip 2 lines for PlayerStatsKey.CLASSIFICATION.

        Form form = Player.getInstance().getForm();
        if (form instanceof FishForm) {
            FishForm fishForm = (FishForm) form;

            //i starting at 1 because PlayerStatsKey.CLASSIFICATION  was accounted for when initializing yStringBonuses.
            for (int i = 1; i < PlayerStatsKey.values().length; i++) {
                //PlayerStats beginning with PlayerStatsKey.Hit_Point_Max.
                if (i == PlayerStatsKey.STRENGTH.ordinal()) {
                    int newStrength = FishStateManager.FinPectoral.values()[indexBodyPartSelected].getStrengthBonus() -
                            fishForm.getFishStateManager().getCurrentFinPectoral().getStrengthBonus();
                    if (newStrength > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newStrength, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newStrength < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newStrength), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newStrength == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newStrength), xStringBonuses, yStringBonuses, paintFont);
                    }
                } else if (i == PlayerStatsKey.AGILITY.ordinal()) {
                    int newAgility = FishStateManager.FinPectoral.values()[indexBodyPartSelected].getAgilityBonus() -
                            fishForm.getFishStateManager().getCurrentFinPectoral().getAgilityBonus();
                    if (newAgility > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newAgility, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newAgility < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newAgility), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newAgility == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newAgility), xStringBonuses, yStringBonuses, paintFont);
                    }
                }

                //just one-regular-new-line.
                yStringBonuses += heightLine;
            }
        }
    }

    private void renderBonusesFromTail(Canvas canvas) {
        int x0PlayerStatsBox = (game.getWidthViewport() / 2) + 50;
        int y0PlayerStatsBox = y1ImageBackground;
        int x1PlayerStatsBox = game.getWidthViewport() - 10;
        int y1PlayerStatsBox = y1ImageBackground + (game.getHeightViewport() / 2);

        //text.
        Paint paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setTextSize(20f);
        paintFont.setColor(Color.WHITE);
        Paint.FontMetrics fm = paintFont.getFontMetrics();
        int heightLine = (int) (fm.bottom - fm.top + fm.leading);

        ////////////////////////////////////////////////////////////////////////////////////////////

        int xStringBonuses = game.getWidthViewport() - 50;
        int yStringBonuses = y0PlayerStatsBox + 24 + (2 * heightLine); //skip 2 lines for PlayerStatsKey.CLASSIFICATION.

        Form form = Player.getInstance().getForm();
        if (form instanceof FishForm) {
            FishForm fishForm = (FishForm) form;

            //i starting at 1 because PlayerStatsKey.CLASSIFICATION  was accounted for when initializing yStringBonuses.
            for (int i = 1; i < PlayerStatsKey.values().length; i++) {
                //PlayerStats beginning with PlayerStatsKey.Hit_Point_Max.
                if (i == PlayerStatsKey.HIT_POINT_MAX.ordinal()) {
                    int newHitPointMax = FishStateManager.Tail.values()[indexBodyPartSelected].getHealthMaxBonus() -
                            fishForm.getFishStateManager().getCurrentTail().getHealthMaxBonus();
                    if (newHitPointMax > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newHitPointMax, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newHitPointMax < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newHitPointMax), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newHitPointMax == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newHitPointMax), xStringBonuses, yStringBonuses, paintFont);
                    }
                } else if (i == PlayerStatsKey.AGILITY.ordinal()) {
                    int newAgility = FishStateManager.Tail.values()[indexBodyPartSelected].getAgilityBonus() -
                            fishForm.getFishStateManager().getCurrentTail().getAgilityBonus();
                    if (newAgility > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newAgility, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newAgility < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newAgility), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newAgility == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newAgility), xStringBonuses, yStringBonuses, paintFont);
                    }
                } else if (i == PlayerStatsKey.JUMPING_ABILITY.ordinal()) {
                    int newJump = FishStateManager.Tail.values()[indexBodyPartSelected].getJumpBonus() -
                            fishForm.getFishStateManager().getCurrentTail().getJumpBonus();
                    if (newJump > 0) {
                        paintFont.setColor(Color.GREEN);
                        canvas.drawText("+" + newJump, xStringBonuses, yStringBonuses, paintFont);
                    } else if (newJump < 0) {
                        paintFont.setColor(Color.RED);
                        canvas.drawText(Integer.toString(newJump), xStringBonuses, yStringBonuses, paintFont);
                    } else if (newJump == 0) {
                        paintFont.setColor(Color.YELLOW);
                        canvas.drawText(Integer.toString(newJump), xStringBonuses, yStringBonuses, paintFont);
                    }
                }

                //just one-regular-new-line.
                yStringBonuses += heightLine;
            }
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

    public int getIndexBodyPartCategory() {
        return indexBodyPartCategory;
    }

    public int getIndexBodyPartSelected() {
        return indexBodyPartSelected;
    }
}