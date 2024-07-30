package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters;

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

public class SceneHomeRival extends Scene {
    public static final String TAG = SceneHomeRival.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 2;
    public static final int Y_SPAWN_INDEX_DEFAULT = 6;

    private static SceneHomeRival uniqueInstance;

    private SceneHomeRival() {
        super();
        List<Entity> entitiesForHomeRival = createEntitiesForHomeRival();
        entityManager.loadEntities(entitiesForHomeRival);
        List<Item> itemsForHomeRival = createItemsForHomeRival();
        itemManager.loadItems(itemsForHomeRival);
    }

    public static SceneHomeRival getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneHomeRival();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForHomeRival = createAndInitTilesForHomeRival(game);
        tileManager.loadTiles(tilesForHomeRival);
        Map<String, Rect> transferPointsForHomeRival = createTransferPointsForHomeRival();
        tileManager.loadTransferPoints(transferPointsForHomeRival); // transferPoints are transient and should be reloaded everytime.
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

    private Tile[][] createAndInitTilesForHomeRival(Game game) {
        String homeRivalLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_home_rival);
        Tile[][] homeRival = TileManagerLoader.convertStringToTiles(homeRivalLoadedAsString);
        Bitmap imageHomeRival = cropImageHomeRival(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in worldMapPart01.
        for (int y = 0; y < homeRival.length; y++) {
            for (int x = 0; x < homeRival[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = homeRival[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHomeRival, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHomeRival, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return homeRival;
    }

    private Bitmap cropImageHomeRival(Resources resources) {
        Log.d(TAG, "SceneHomeRival.cropImageHomeRival(Resources resources)");

        Bitmap indoorsHomeAndRoom = BitmapFactory.decodeResource(resources, R.drawable.indoors_home_and_room);
        Bitmap homeRival = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        homeRival = Bitmap.createBitmap(indoorsHomeAndRoom, 304, 16, 128, 128);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap homeRival's (width, height): " + homeRival.getWidth() + ", " + homeRival.getHeight());

        return homeRival;
    }

    private Map<String, Rect> createTransferPointsForHomeRival() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("PART_01", new Rect((2 * Tile.WIDTH), (7 * Tile.HEIGHT),
                (2 * Tile.WIDTH) + (2 * Tile.WIDTH), (7 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForHomeRival() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForHomeRival() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}