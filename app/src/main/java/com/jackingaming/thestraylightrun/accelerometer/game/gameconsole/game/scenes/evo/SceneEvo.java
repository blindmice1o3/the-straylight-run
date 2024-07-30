package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Damageable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Eel;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Kelp;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.SeaJelly;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.hud.HeadUpDisplay;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneEvo extends Scene {
    public static final String TAG = SceneEvo.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 4;
    public static final int Y_SPAWN_INDEX_DEFAULT = 4;

    private static SceneEvo uniqueInstance;

    private HeadUpDisplay headUpDisplay;

    private SceneEvo() {
        super();
        List<Entity> entitiesForEvo = createEntitiesForEvo();
        entityManager.loadEntities(entitiesForEvo);
        List<Item> itemsForEvo = createItemsForEvo();
        itemManager.loadItems(itemsForEvo);
    }

    public static SceneEvo getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneEvo();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForEvo = createAndInitTilesForEvo(game);
        tileManager.loadTiles(tilesForEvo);
        Map<String, Rect> transferPointsForEvo = createTransferPointsForEvo();
        tileManager.loadTransferPoints(transferPointsForEvo); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);

        headUpDisplay = new HeadUpDisplay(game);
    }

    @Override
    public void update(long elapsed) {
        super.update(elapsed);

        interpretViewportInput();

        headUpDisplay.update(elapsed);
    }

    private static final int TOUCH_POINT_RADIUS = 30;

    private void interpretViewportInput() {
        if (game.getInputManager().isJustPressedViewport()) {
            Log.d(TAG, getClass().getSimpleName() + ".interpretViewportInput() isJustPressedViewport is true");

            // DETERMINE coordinates for touch event.
            float xEventCenter = game.getInputManager().getxViewport();
            float yEventCenter = game.getInputManager().getyViewport();
            Log.d(TAG, "(xViewport, yViewport): " + xEventCenter + ", " + yEventCenter);

//            float xEventCenter = event.getX();
//            float yEventCenter = event.getY();

//            // DETERMINE heightActionBar.
//            TypedValue tv = new TypedValue();
//            game.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
//            int heightActionBar = game.getContext().getResources().getDimensionPixelSize(tv.resourceId);
//            Log.d(MainActivity.DEBUG_TAG, getClass().getSimpleName() + ".interpretViewportInput() heightActionBar: " + heightActionBar);
//            // DETERMINE heightStatusBar.
//            int idStatusBarHeight = game.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
//            int heightStatusBar = game.getContext().getResources().getDimensionPixelSize(idStatusBarHeight);
//            Log.d(MainActivity.DEBUG_TAG, "heightStatusBar = " + heightStatusBar);

            // DEFINE Rect representing collision bounds of user touch (FACTOR IN action bar and status bar).
            float x0Event = xEventCenter - TOUCH_POINT_RADIUS;
            float y0Event = yEventCenter - TOUCH_POINT_RADIUS/* - (heightActionBar + heightStatusBar)*/;
            float x1Event = xEventCenter + TOUCH_POINT_RADIUS;
            float y1Event = yEventCenter + TOUCH_POINT_RADIUS/* - (heightActionBar + heightStatusBar)*/;
            Log.d(TAG, getClass().getSimpleName() + ".interpretViewportInput() x0Event, y0Event, x1Event, y1Event: " + x0Event + ", " + y0Event + ", " + x1Event + ", " + y1Event);

            Rect rectOfTouchPointOnScreen = new Rect((int) x0Event, (int) y0Event, (int) x1Event, (int) y1Event);
            Rect rectOfTouchPointInGame = GameCamera.getInstance().convertScreenRectToInGameRect(rectOfTouchPointOnScreen);
            for (Entity e : entityManager.getEntities()) {
                if (rectOfTouchPointInGame.intersect(e.getCollisionBounds(0f, 0f))) {
                    if (e instanceof Kelp) {
                        GameCamera.getInstance().startShaking();

//                        ComponentHUD textHUD = new ComponentHUD(game, ComponentHUD.ComponentType.TEXT,
//                                "The caterpillar went for a walk.", e);
//                        SceneEvo sceneEvo = ((SceneEvo)game.getSceneManager().getCurrentScene());
//                        sceneEvo.getHeadUpDisplay().addTimedNumericIndicator(textHUD);

//                        e.setActive(false);
//                        ((Kelp)e).die();

                        // TODO: pass text and location (x, y, width, height) to textbox.
//                        String text = "Two four six eight ... eight six four two";
//                        float xPlayerInGame = Player.getInstance().getX();
//                        float yPlayerInGame = Player.getInstance().getY();
//                        float xPlayerOnScreen = (xPlayerInGame - GameCamera.getInstance().getX()) * GameCamera.getInstance().getWidthPixelToViewportRatio();
//                        float yPlayerOnScreen = (yPlayerInGame - GameCamera.getInstance().getY()) * GameCamera.getInstance().getHeightPixelToViewportRatio();
//                        game.getStateManager().pushTextboxState(text, xPlayerOnScreen, yPlayerOnScreen);
                    } else if (e instanceof Damageable) {
                        e.setActive(false);
                        ((Damageable) e).die();
                    }
                }
            }
        }
    }

    @Override
    public void drawCurrentFrame(Canvas canvas) {
        super.drawCurrentFrame(canvas);
        headUpDisplay.render(canvas);
    }

    private Form formBeforeThisScene;
    private Form formForThisScene;

    @Override
    public void enter() {
        super.enter();

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_SPAWN_INDEX_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT);
            formForThisScene = new FishForm();
            formForThisScene.init(game);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }

        formBeforeThisScene = Player.getInstance().getForm();
        Player.getInstance().setForm(formForThisScene);

        GameCamera.getInstance().update(0L);
    }

    @Override
    public void exit() {
        super.exit();

        Player.getInstance().setForm(formBeforeThisScene);
    }

    private boolean compareTwoSprites(Bitmap sprite1, Bitmap sprite2, int x, int y) {
        //if width or height are not the same, the two sprites are NOT the same.
        if ((sprite1.getWidth() != sprite1.getWidth()) || (sprite1.getHeight() != sprite2.getHeight())) {
            return false;
        }

        //if any pixels are not the same, the two sprite are NOT the same.
        //for (int y = 0; y < Tile.HEIGHT; y++) {
        //for (int x = 0; x < Tile.WIDTH; x++) {
        int pixelSprite1 = sprite1.getPixel(x, y);
        int redSprite1 = (pixelSprite1 >> 16) & 0xff;
        int greenSprite1 = (pixelSprite1 >> 8) & 0xff;
        int blueSprite1 = (pixelSprite1) & 0xff;

        int pixelSprite2 = sprite2.getPixel(x, y);
        int redSprite2 = (pixelSprite2 >> 16) & 0xff;
        int greenSprite2 = (pixelSprite2 >> 8) & 0xff;
        int blueSprite2 = (pixelSprite2) & 0xff;

        if (((redSprite1 == redSprite2) && (greenSprite1 == greenSprite2) && (blueSprite1 == blueSprite2))) {
            return true;
        }
        //}
        //}

        return false;
    }

    private Tile[][] createAndInitTilesForEvo(Game game) {
        // entire scene
        Bitmap imageEvo = cropImageEvo(game.getContext().getResources());
        int columns = imageEvo.getWidth() / Tile.WIDTH;
        int rows = imageEvo.getHeight() / Tile.HEIGHT;
        Tile[][] evo = new Tile[rows][columns];

        // tile-image targets
        Bitmap coin = Bitmap.createBitmap(imageEvo, 224, 184, Tile.WIDTH, Tile.HEIGHT);
        Bitmap brickGreen = Bitmap.createBitmap(imageEvo, 0, 200, Tile.WIDTH, Tile.HEIGHT);
        Bitmap coralPink1 = Bitmap.createBitmap(imageEvo, 176, 184, Tile.WIDTH, Tile.HEIGHT);
        Bitmap coralPink2 = Bitmap.createBitmap(imageEvo, 1632, 120, Tile.WIDTH, Tile.HEIGHT);
        Bitmap coralPink3 = Bitmap.createBitmap(imageEvo, 2384, 184, Tile.WIDTH, Tile.HEIGHT);

        ArrayList<Bitmap> solidTileSearchTargets = new ArrayList<Bitmap>();
        solidTileSearchTargets.add(coin);
        solidTileSearchTargets.add(brickGreen);
        solidTileSearchTargets.add(coralPink1);
        solidTileSearchTargets.add(coralPink2);
        solidTileSearchTargets.add(coralPink3);

        //check each pixels in the tile (16x16) within the 192tiles by 14tiles map.
        for (int y = 0; y < rows - 1; y++) {
            for (int x = 0; x < columns; x++) {

                int xOffset = (x * Tile.WIDTH);
                int yOffset = (y * Tile.HEIGHT) + 8;
                Bitmap currentTile = Bitmap.createBitmap(imageEvo,
                        xOffset, yOffset, Tile.WIDTH, Tile.HEIGHT);

                //for each tile, check if it's one of the solidTileSearchTargets.
                for (Bitmap solidTileTarget : solidTileSearchTargets) {

                    int xx = 0;
                    int yy = 0;

                    if (solidTileTarget == brickGreen) {
                        xx = 0;
                        yy = 0;
                    } else {
                        xx = 9;
                        yy = 2;
                    }

                    //if it's the same, we have a SOLID tile.
                    if (compareTwoSprites(solidTileTarget, currentTile, xx, yy)) {
                        Tile tile = new Tile("x");
                        tile.setWalkable(false);
                        tile.init(game, x, y, currentTile);
                        evo[y][x] = tile;
                        break;
                    }

                    if ((evo[y][x] == null) && (solidTileTarget == coralPink1)) {
                        xx = 8;
                        yy = 14;
                        if (compareTwoSprites(solidTileTarget, currentTile, xx, yy)) {
                            Tile tile = new Tile("x");
                            tile.setWalkable(false);
                            tile.init(game, x, y, currentTile);
                            evo[y][x] = tile;
                            break;
                        }
                    }
                    if ((evo[y][x] == null) && (solidTileTarget == coralPink1)) {
                        xx = 8;
                        yy = 15;
                        if (compareTwoSprites(solidTileTarget, currentTile, xx, yy)) {
                            Tile tile = new Tile("x");
                            tile.setWalkable(false);
                            tile.init(game, x, y, currentTile);
                            evo[y][x] = tile;
                            break;
                        }
                    }

                }

                if (evo[y][x] == null) {
                    // walkable
                    Tile tile = new Tile("o");
                    tile.init(game, x, y, currentTile);
                    evo[y][x] = tile;
                }
            }
        }

        // bottom row should be all solid brick tiles.
        for (int x = 0; x < columns; x++) {
            Tile tile = new Tile("x");
            tile.setWalkable(false);
            tile.init(game, x, rows - 1, brickGreen);
            evo[rows - 1][x] = tile;
        }

        return evo;
    }

    public static Bitmap cropImageEvo(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.mario_7_2);
    }

    private Map<String, Rect> createTransferPointsForEvo() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        return transferPoints;
    }

    private List<Entity> createEntitiesForEvo() {
        List<Entity> entities = new ArrayList<Entity>();

        Entity kelp1 = new Kelp(5 * Tile.WIDTH, 3 * Tile.HEIGHT);
        Entity kelp2 = new Kelp((int) (5.5f * Tile.WIDTH), 3 * Tile.HEIGHT);
        Entity kelp3 = new Kelp(6 * Tile.WIDTH, 3 * Tile.HEIGHT);
        entities.add(kelp1);
        entities.add(kelp2);
        entities.add(kelp3);

        Entity seaJelly1 = new SeaJelly(8 * Tile.WIDTH, 2 * Tile.HEIGHT,
                Creature.Direction.DOWN, 5 * Tile.HEIGHT);
        entities.add(seaJelly1);

        Entity eel1 = new Eel(8 * Tile.WIDTH, 6 * Tile.WIDTH,
                Eel.DirectionFacing.LEFT, 5 * Tile.WIDTH);
        entities.add(eel1);

        return entities;
    }

    private List<Item> createItemsForEvo() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }

    public HeadUpDisplay getHeadUpDisplay() {
        return headUpDisplay;
    }
}