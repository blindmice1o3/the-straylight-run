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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneCowBarn extends Scene {
    public static final String TAG = SceneCowBarn.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneCowBarn uniqueInstance;

    private SceneCowBarn() {
        super();
        List<Entity> entitiesForCowBarn = createEntitiesForCowBarn();
        entityManager.loadEntities(entitiesForCowBarn);
        List<Item> itemsForCowBarn = createItemsForCowBarn();
        itemManager.loadItems(itemsForCowBarn);
    }

    public static SceneCowBarn getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneCowBarn();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForCowBarn = createAndInitTilesForCowBarn(game);
        tileManager.loadTiles(tilesForCowBarn);
        Map<String, Rect> transferPointsForCowBarn = createTransferPointsForCowBarn();
        tileManager.loadTransferPoints(transferPointsForCowBarn); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    public void enter() {
        super.enter();

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_SPAWN_INDEX_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    private Tile[][] createAndInitTilesForCowBarn(Game game) {
        String cowBarnLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_cow_barn);
        Tile[][] cowBarn = TileManagerLoader.convertStringToTiles(cowBarnLoadedAsString);
        Bitmap imageCowBarn = cropImageCowBarn(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in cowBarn.
        for (int y = 0; y < cowBarn.length; y++) {
            for (int x = 0; x < cowBarn[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = cowBarn[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageCowBarn, xInPixel, yInPixel, widthInPixel, heightInPixel);
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

        return cowBarn;
    }

    private Bitmap cropImageCowBarn(Resources resources) {
        Log.d(TAG, "SceneCowBarn.cropImageCowBarn(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap cowBarnEmpty = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        cowBarnEmpty = Bitmap.createBitmap(indoorsFarmHM2, 8, 304, 240, 256);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap cowBarnEmpty's (width, height): " + cowBarnEmpty.getWidth() + ", " + cowBarnEmpty.getHeight());

        return cowBarnEmpty;
    }

    private Map<String, Rect> createTransferPointsForCowBarn() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (13 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (3 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForCowBarn() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForCowBarn() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}