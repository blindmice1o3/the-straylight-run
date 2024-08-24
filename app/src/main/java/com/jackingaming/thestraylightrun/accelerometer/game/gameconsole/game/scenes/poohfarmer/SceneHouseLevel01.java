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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.BedTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHouseLevel01 extends Scene {
    public static final String TAG = SceneHouseLevel01.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 4;
    public static final int Y_SPAWN_INDEX_DEFAULT = 8;

    private boolean leftHouseToday; // initialize to false if game starts if game starts in SceneHouseLevel01.
    private boolean slept;

    private static SceneHouseLevel01 uniqueInstance;

    private SceneHouseLevel01() {
        super();
        List<Entity> entitiesForHouseLevel01 = createEntitiesForHouseLevel01();
        entityManager.loadEntities(entitiesForHouseLevel01);
        List<Item> itemsForHouseLevel01 = createItemsForHouseLevel01();
        itemManager.loadItems(itemsForHouseLevel01);
        leftHouseToday = true; // started game in SceneFarm.
        slept = false;
    }

    public void onExitToFarm() {
        leftHouseToday = true;

        if (slept) {
            ///////////////////
            game.startNewDay();
            ///////////////////

            slept = false;
        }
    }

    public void onBedTileClicked() {
        if (leftHouseToday) {
            slept = true;
            leftHouseToday = false;
        }
    }

    public static SceneHouseLevel01 getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneHouseLevel01();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForHouseLevel01 = createAndInitTilesForHouseLevel01(game);
        tileManager.loadTiles(tilesForHouseLevel01);
        Map<String, Rect> transferPointsForHouseLevel01 = createTransferPointsForHouseLevel01();
        tileManager.loadTransferPoints(transferPointsForHouseLevel01); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    public List<Object> exit() {
        List<Object> args = super.exit();
        args.add(1, TAG);
        return args;
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

    private Tile[][] createAndInitTilesForHouseLevel01(Game game) {
        String houseLevel01LoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_house_level_01);
        Tile[][] houseLevel01 = TileManagerLoader.convertStringToTiles(houseLevel01LoadedAsString);
        Bitmap imageHouseLevel01 = cropImageHouseLevel01(game.getContext().getResources());

        // TODO: init all tiles (assign image) in houseLevel01.
        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in houseLevel01.
        for (int y = 0; y < houseLevel01.length; y++) {
            for (int x = 0; x < houseLevel01[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = houseLevel01[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHouseLevel01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHouseLevel01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //BedTile
                else if (tile.getId().equals("b")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHouseLevel01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    houseLevel01[y][x] = new BedTile(BedTile.TAG);
                    houseLevel01[y][x].init(game, x, y, tileSprite);
                    houseLevel01[y][x].setWalkable(false);
                } else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return houseLevel01;
    }

    private Bitmap cropImageHouseLevel01(Resources resources) {
        Log.d(TAG, "SceneHouseLevel01.cropImageHouseLevel01(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap houseLevel01 = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        houseLevel01 = Bitmap.createBitmap(indoorsFarmHM2, 6, 6, 160, 192);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap houseLevel01's (width, height): " + houseLevel01.getWidth() + ", " + houseLevel01.getHeight());

        return houseLevel01;
    }

    private Map<String, Rect> createTransferPointsForHouseLevel01() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((4 * Tile.WIDTH), (9 * Tile.HEIGHT),
                (4 * Tile.WIDTH) + (2 * Tile.WIDTH), (9 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForHouseLevel01() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForHouseLevel01() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}