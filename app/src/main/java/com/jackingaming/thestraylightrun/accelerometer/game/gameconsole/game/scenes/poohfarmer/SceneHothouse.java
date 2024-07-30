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

public class SceneHothouse extends Scene {
    public static final String TAG = SceneHothouse.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 5;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneHothouse uniqueInstance;

    private SceneHothouse() {
        super();
        List<Entity> entitiesForHothouse = createEntitiesForHothouse();
        entityManager.loadEntities(entitiesForHothouse);
        List<Item> itemsForHothouse = createItemsForHothouse();
        itemManager.loadItems(itemsForHothouse);
    }

    public static SceneHothouse getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneHothouse();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForHothouse = createAndInitTilesForHothouse(game);
        tileManager.loadTiles(tilesForHothouse);
        Map<String, Rect> transferPointsForHothouse = createTransferPointsForHothouse();
        tileManager.loadTransferPoints(transferPointsForHothouse); // transferPoints are transient and should be reloaded everytime.
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

    private Tile[][] createAndInitTilesForHothouse(Game game) {
        String hothouseLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_hothouse);
        Tile[][] hothouse = TileManagerLoader.convertStringToTiles(hothouseLoadedAsString);
        Bitmap imageHothouse = cropImageHothouse(game.getContext().getResources());

        // InitiaClize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in hothouse.
        for (int y = 0; y < hothouse.length; y++) {
            for (int x = 0; x < hothouse[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = hothouse[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //SignPostTile
                else if (tile.getId().equals("a")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //GrowableTableTile
                else if (tile.getId().equals("p")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //ShippingBinTile
                else if (tile.getId().equals("c")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else if (tile.getId().equals("d")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else if (tile.getId().equals("e")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else if (tile.getId().equals("f")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHothouse, xInPixel, yInPixel, widthInPixel, heightInPixel);
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

        return hothouse;
    }

    private Bitmap cropImageHothouse(Resources resources) {
        Log.d(TAG, "SceneHothouse.cropImageHothouse(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap hothouseEmpty = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        hothouseEmpty = Bitmap.createBitmap(indoorsFarmHM2, 223, 1200, 192, 256);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap hothouseEmpty's (width, height): " + hothouseEmpty.getWidth() + ", " + hothouseEmpty.getHeight());

        return hothouseEmpty;
    }

    private Map<String, Rect> createTransferPointsForHothouse() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((5 * Tile.WIDTH), (13 * Tile.HEIGHT),
                (5 * Tile.WIDTH) + (2 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForHothouse() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForHothouse() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}