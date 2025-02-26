package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.bubblepop;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta.Monsta;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles.Bubble;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneBubblePop extends Scene {
    public static final String TAG = SceneBubblePop.class.getSimpleName();

    private static SceneBubblePop uniqueInstance;

    private SceneBubblePop() {
        super();
        List<Entity> entitiesForFarm = createEntitiesForBubblePop();
        entityManager.loadEntities(entitiesForFarm);
        List<Item> itemsForFarm = createItemsForBubblePop();
        itemManager.loadItems(itemsForFarm);
    }

    public static SceneBubblePop getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneBubblePop();
        }
        return uniqueInstance;
    }

    // at this point, entityManager and itemManager are loaded, only need tileManager.
    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForBubblePop = createAndInitTilesForBubblePop(game);
        tileManager.loadTiles(tilesForBubblePop);
        Map<String, Rect> transferPointsForBubblePop = createTransferPointsForBubblePop();
        tileManager.loadTransferPoints(transferPointsForBubblePop); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    protected void doJustPressedButtonA() {
        super.doJustPressedButtonA();

        Entity entityCurrentlyFacing = Player.getInstance().getEntityCurrentlyFacing();

        if (entityCurrentlyFacing instanceof Bubblun) {
            Log.d(TAG, "SceneBubblePop entityCurrentlyFacing instanceof Bubblun.");

            ((Bubblun) entityCurrentlyFacing).changeToAttackState();
            ((Bubblun) entityCurrentlyFacing).changeToJumpState();
        } else if (entityCurrentlyFacing instanceof Bubble) {
            Log.d(TAG, "SceneBubblePop entityCurrentlyFacing instanceof Bubble.");

            Bubble bubble = (Bubble) entityCurrentlyFacing;
            bubble.bounceToRight();
        } else {
            Log.d(TAG, "SceneBubblePop entityCurrentlyFacing NOT instanceof Bubblun or Bubble.");
        }
    }

    private Tile[][] createAndInitTilesForBubblePop(Game game) {
        String bubblePopScene1LoadedAsString = TileManagerLoader.loadFileAsString(
                game.getContext().getResources(), R.raw.tile_bubble_pop_scene_1
        );
        Tile[][] bubblePopScene1 = TileManagerLoader.convertStringToTiles(bubblePopScene1LoadedAsString);
        Bitmap imageBubblePopScene1 = cropImageBubblePopScene1(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in bubblePopScene1.
        for (int y = 0; y < bubblePopScene1.length; y++) {
            for (int x = 0; x < bubblePopScene1[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = bubblePopScene1[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageBubblePopScene1, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageBubblePopScene1, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
//                //SignPostTile
//                else if (tile.getId().equals("a")) {
//                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
//                    tile.init(game, x, y, tileSprite);
//                    tile.setWalkable(false);
//                }
//                //EggIncubatorTile
//                else if (tile.getId().equals("g")) {
//                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
//                    tile.init(game, x, y, tileSprite);
//                    tile.setWalkable(false);
//                }
                //default
                else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return bubblePopScene1;
    }

    private Bitmap cropImageBubblePopScene1(Resources resources) {
        Log.d(TAG, "cropImageBubblePopScene1(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap chickenCoopEmpty = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        chickenCoopEmpty = Bitmap.createBitmap(indoorsFarmHM2, 78, 603, 240, 256);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap chickenCoopEmpty's (width, height): " + chickenCoopEmpty.getWidth() + ", " + chickenCoopEmpty.getHeight());

        return chickenCoopEmpty;
    }

    private Map<String, Rect> createTransferPointsForBubblePop() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        // TODO: Insert scene specific transfer points here.
//        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (13 * Tile.HEIGHT),
//                (6 * Tile.WIDTH) + (3 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForBubblePop() {
        List<Entity> entitiesForBubblePop = new ArrayList<>();

        // TODO: Insert scene specific entities here.
        Bubblun bubblun = new Bubblun(3 * Tile.WIDTH, 3 * Tile.HEIGHT);
        entitiesForBubblePop.add(bubblun);
        Monsta monsta = new Monsta(7 * Tile.WIDTH, 3 * Tile.HEIGHT);
        entitiesForBubblePop.add(monsta);

        return entitiesForBubblePop;
    }

    private List<Item> createItemsForBubblePop() {
        List<Item> itemsForBubblePop = new ArrayList<>();
        // TODO: Insert scene specific items here.
        return itemsForBubblePop;
    }
}
