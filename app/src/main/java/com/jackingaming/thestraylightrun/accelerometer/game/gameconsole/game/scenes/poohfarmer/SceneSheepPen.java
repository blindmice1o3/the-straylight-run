package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneSheepPen extends Scene {
    public static final String TAG = SceneSheepPen.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 12;

    private static SceneSheepPen uniqueInstance;

    private SceneSheepPen() {
        super();
        List<Entity> entitiesForSheepPen = createEntitiesForSheepPen();
        entityManager.loadEntities(entitiesForSheepPen);
        List<Item> itemsForSheepPen = createItemsForSheepPen();
        itemManager.loadItems(itemsForSheepPen);
    }

    public static SceneSheepPen getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneSheepPen();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForSheepPen = createAndInitTilesForSheepPen(game);
        tileManager.loadTiles(tilesForSheepPen);
        Map<String, Rect> transferPointsForSheepPen = createTransferPointsForSheepPen();
        tileManager.loadTransferPoints(transferPointsForSheepPen); // transferPoints are transient and should be reloaded everytime.
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

    private Tile[][] createAndInitTilesForSheepPen(Game game) {
        String sheepPenLoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_sheep_pen);
        Tile[][] sheepPen = TileManagerLoader.convertStringToTiles(sheepPenLoadedAsString);
        Bitmap imageSheepPen = cropImageSheepPen(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in sheepPen.
        for (int y = 0; y < sheepPen.length; y++) {
            for (int x = 0; x < sheepPen[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = sheepPen[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageSheepPen, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageSheepPen, xInPixel, yInPixel, widthInPixel, heightInPixel);
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

        return sheepPen;
    }

    private Bitmap cropImageSheepPen(Resources resources) {
        Log.d(TAG, "SceneSheepPen.cropImageSheepPen(Resources resources)");

        Bitmap indoorsFarmHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_farm_indoors);
        Bitmap sheepPenEmpty = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        sheepPenEmpty = Bitmap.createBitmap(indoorsFarmHM2, 8, 902, 240, 256);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap sheepPenEmpty's (width, height): " + sheepPenEmpty.getWidth() + ", " + sheepPenEmpty.getHeight());

        return sheepPenEmpty;
    }

    private Map<String, Rect> createTransferPointsForSheepPen() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("FARM", new Rect((6 * Tile.WIDTH), (13 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (3 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    @Override
    public void drawCurrentFrame(Canvas canvas) {
        super.drawCurrentFrame(canvas);
//        Rect screenRectOfTransferPoint = GameCamera.getInstance().convertInGameRectToScreenRect(tileManager.getTransferPointBounds("FARM"));
//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(screenRectOfTransferPoint, paint);
//
//        Rect screenRectOfPlayer = GameCamera.getInstance().convertInGameRectToScreenRect(Player.getInstance().getCollisionBounds(0, 0));
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(screenRectOfPlayer, paint);
    }

    private List<Entity> createEntitiesForSheepPen() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    private List<Item> createItemsForSheepPen() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}