package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneChickenCoop extends Scene {
    public static final String TAG = SceneChickenCoop.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneChickenCoop uniqueInstance;

    private SceneChickenCoop() {
        super();
        List<Entity> entitiesForChickenCoop = createEntitiesForChickenCoop();
        entityManager.loadEntities(entitiesForChickenCoop);
        List<Item> itemsForChickenCoop = createItemsForChickenCoop();
        itemManager.loadItems(itemsForChickenCoop);
    }

    public static SceneChickenCoop getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneChickenCoop();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForChickenCoop = createAndInitTilesForChickenCoop(game);
        tileManager.loadTiles(tilesForChickenCoop);
        Map<String, Rect> transferPointsForChickenCoop = createTransferPointsForChickenCoop();
        tileManager.loadTransferPoints(transferPointsForChickenCoop); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    public void enter(List<Object> args) {
        super.enter(args);
        game.getTimeManager().setIsPaused(true);

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_SPAWN_INDEX_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    @Override
    protected void doJustPressedButtonA() {
        super.doJustPressedButtonA();

        Player player = Player.getInstance();
        Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();

        if (player.hasCarryable() && entityCurrentlyFacing == null) {
            Log.e(TAG, "has carryable and entityFacing is null");
            Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();

            if (tileCurrentlyFacing instanceof ShippingBinTile) {
                Log.e(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");
                if (player.getCarryable() instanceof Sellable) {
                    Log.e(TAG, "carryable is Sellable");
                    player.placeInShippingBin();
                }
            } else if (tileCurrentlyFacing.isWalkable()) {
                Log.e(TAG, "tileCurrentlyFacing.isWalkable()");
                if (player.getCarryable() instanceof AimlessWalker) {
                    ((AimlessWalker) player.getCarryable()).changeToWalk();
                }

                player.placeDown();
            }
        } else if (entityCurrentlyFacing != null &&
                entityCurrentlyFacing instanceof AimlessWalker) {
            ((AimlessWalker) entityCurrentlyFacing).changeToOff();

            player.pickUp(entityCurrentlyFacing);
        }
    }

    private Tile[][] createAndInitTilesForChickenCoop(Game game) {
        String chickenCoopLoadedAsString = TileManagerLoader.loadFileAsString(
                game.getContext().getResources(), R.raw.tile_chicken_coop
        );
        Tile[][] chickenCoop = TileManagerLoader.convertStringToTiles(chickenCoopLoadedAsString);
        Bitmap imageChickenCoop = cropImageChickenCoop(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in chickenCoop.
        for (int y = 0; y < chickenCoop.length; y++) {
            for (int x = 0; x < chickenCoop[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = chickenCoop[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //SignPostTile
                else if (tile.getId().equals("a")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //EggIncubatorTile
                else if (tile.getId().equals("g")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //FodderStashTile
                else if (tile.getId().equals("h")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //FeedingStallTile
                else if (tile.getId().equals("i")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //ShippingBinTile
                else if (tile.getId().equals("c")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else if (tile.getId().equals("d")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else if (tile.getId().equals("e")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else if (tile.getId().equals("f")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageChickenCoop, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //default
                else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return chickenCoop;
    }

    private Bitmap cropImageChickenCoop(Resources resources) {
        Log.d(TAG, "SceneChickenCoop.cropImageChickenCoop(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap chickenCoopEmpty = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        chickenCoopEmpty = Bitmap.createBitmap(indoorsFarmHM2, 78, 603, 240, 256);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap chickenCoopEmpty's (width, height): " + chickenCoopEmpty.getWidth() + ", " + chickenCoopEmpty.getHeight());

        return chickenCoopEmpty;
    }

    private Map<String, Rect> createTransferPointsForChickenCoop() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (13 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (3 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForChickenCoop() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.

        return entities;
    }

    private List<Item> createItemsForChickenCoop() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}