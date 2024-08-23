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

public class SceneLab extends Scene {
    public static final String TAG = SceneLab.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 4;
    public static final int Y_SPAWN_INDEX_DEFAULT = 10;

    private static SceneLab uniqueInstance;

    private SceneLab() {
        super();
        List<Entity> entitiesForLab = createEntitiesForLab();
        entityManager.loadEntities(entitiesForLab);
        List<Item> itemsForLab = createItemsForLab();
        itemManager.loadItems(itemsForLab);
    }

    public static SceneLab getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneLab();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForLab = createAndInitTilesForLab(game);
        tileManager.loadTiles(tilesForLab);
        Map<String, Rect> transferPointsForLab = createTransferPointsForLab();
        tileManager.loadTransferPoints(transferPointsForLab); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);
    }

    @Override
    public void enter(List<Object> args) {
        super.enter(args);

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_SPAWN_INDEX_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    private Tile[][] createAndInitTilesForLab(Game game) {
        String labLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_lab);
        Tile[][] lab = TileManagerLoader.convertStringToTiles(labLoadedAsString);
        Bitmap imageLab = cropImageLab(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in worldMapPart01.
        for (int y = 0; y < lab.length; y++) {
            for (int x = 0; x < lab[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = lab[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageLab, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageLab, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return lab;
    }

    private Bitmap cropImageLab(Resources resources) {
        Log.d(TAG, "SceneLab.cropImageLab(Resources resources)");

        Bitmap indoorsHomeAndRoom = BitmapFactory.decodeResource(resources, R.drawable.indoors_home_and_room);
        Bitmap lab = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        lab = Bitmap.createBitmap(indoorsHomeAndRoom, 23, 544, 160, 192);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap lab's (width, height): " + lab.getWidth() + ", " + lab.getHeight());

        return lab;
    }

    private Map<String, Rect> createTransferPointsForLab() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("PART_01", new Rect((4 * Tile.WIDTH), (11 * Tile.HEIGHT),
                (4 * Tile.WIDTH) + (2 * Tile.WIDTH), (11 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private List<Entity> createEntitiesForLab() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForLab() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}