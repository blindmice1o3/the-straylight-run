package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.SignPost;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Milk;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneWorldMapPart01 extends Scene {
    public static final int X_SPAWN_INDEX_DEFAULT = 69;
    public static final int Y_SPAWN_INDEX_DEFAULT = 103;

    private static SceneWorldMapPart01 uniqueInstance;

    private SceneWorldMapPart01() {
        super();
        List<Entity> entitiesForWorldMapPart01 = createEntitiesForWorldMapPart01();
        entityManager.loadEntities(entitiesForWorldMapPart01);
        List<Item> itemsForWorldMapPart01 = createItemsForWorldMapPart01();
        itemManager.loadItems(itemsForWorldMapPart01);
    }

    public static SceneWorldMapPart01 getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneWorldMapPart01();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForWorldMapPart01 = createAndInitTilesForWorldMapPart01(game);
        tileManager.loadTiles(tilesForWorldMapPart01);
        Map<String, Rect> transferPointsForWorldMapPart01 = createTransferPointsForWorldMapPart01();
        tileManager.loadTransferPoints(transferPointsForWorldMapPart01); // transferPoints are transient and should be reloaded everytime.
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

    private int xStartTileIndex = 0;
    private int yStartTileIndex = 104;
    private int xEndTileIndex = 80;
    private int yEndTileIndex = 223;

    private Tile[][] createAndInitTilesForWorldMapPart01(Game game) {
        // Crop [Part01] portion of tiles from full map tiles.
        Tile[][] worldMapPart01 = TileManagerLoader.cropTilesFromFullWorldMap(game.getContext().getResources(),
                xStartTileIndex, yStartTileIndex, xEndTileIndex, yEndTileIndex);
        // Crop [Part01] portion of image from full map image.
        Bitmap imageWorldMapPart01 = TileManagerLoader.cropImageFromFullWorldMap(game.getContext().getResources(),
                xStartTileIndex, yStartTileIndex, xEndTileIndex, yEndTileIndex);

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in worldMapPart01.
        for (int y = 0; y < worldMapPart01.length; y++) {
            for (int x = 0; x < worldMapPart01[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = worldMapPart01[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageWorldMapPart01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageWorldMapPart01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //TallGrassTile
                else if (tile.getId().equals("2")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageWorldMapPart01, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
//                //TelevisionTile
//                else if (tokens[((y * columns) + x) + 2].equals("3")) {
//                    tiles[y][x] = new TelevisionTile(gameCartridge, x, y);
//                }
//                //ComputerTile
//                else if (tokens[((y * columns) + x) + 2].equals("4")) {
//                    tiles[y][x] = new ComputerTile(gameCartridge, x, y);
//                }
//                //GameConsoleTile
//                else if (tokens[((y * columns) + x) + 2].equals("5")) {
//                    tiles[y][x] = new GameConsoleTile(gameCartridge, x, y);
//                }
                //NullTile (blank tile)
                else if (tile.getId().equals("9")) {
                    tile.init(game, x, y, null);
                    tile.setWalkable(false);
                } else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return worldMapPart01;
    }

    private Map<String, Rect> createTransferPointsForWorldMapPart01() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("HOME_01", new Rect((65 * Tile.WIDTH), (99 * Tile.HEIGHT),
                (65 * Tile.WIDTH) + Tile.WIDTH, (99 * Tile.HEIGHT) + Tile.HEIGHT));
        transferPoints.put("HOME_RIVAL", new Rect((73 * Tile.WIDTH), (99 * Tile.HEIGHT),
                (73 * Tile.WIDTH) + Tile.WIDTH, (99 * Tile.HEIGHT) + Tile.HEIGHT));
        transferPoints.put("LAB", new Rect((72 * Tile.WIDTH), (105 * Tile.HEIGHT),
                (72 * Tile.WIDTH) + Tile.WIDTH, (105 * Tile.HEIGHT) + Tile.HEIGHT));
        return transferPoints;
    }

    @Override
    public void drawCurrentFrame(Canvas canvas) {
        super.drawCurrentFrame(canvas);
//        Rect screenRectOfTransferPointHome02 = GameCamera.getInstance().convertInGameRectToScreenRect(tileManager.getTransferPointBounds("HOME_01"));
//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(screenRectOfTransferPointHome02, paint);
//
//        Rect screenRectOfTransferPointHomeRival = GameCamera.getInstance().convertInGameRectToScreenRect(tileManager.getTransferPointBounds("HOME_RIVAL"));
//        paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(screenRectOfTransferPointHomeRival, paint);
//
//        Rect screenRectOfTransferPointLab = GameCamera.getInstance().convertInGameRectToScreenRect(tileManager.getTransferPointBounds("LAB"));
//        paint = new Paint();
//        paint.setColor(Color.RED);
//        canvas.drawRect(screenRectOfTransferPointLab, paint);
//
//        Rect screenRectOfPlayer = GameCamera.getInstance().convertInGameRectToScreenRect(Player.getInstance().getCollisionBounds(0, 0));
//        paint.setColor(Color.GREEN);
//        canvas.drawRect(screenRectOfPlayer, paint);
    }

    private List<Entity> createEntitiesForWorldMapPart01() {
        List<Entity> entities = new ArrayList<Entity>();
        ///////////////////////////////////
        entities.add(Player.getInstance());
        ///////////////////////////////////
        entities.add(new SignPost((X_SPAWN_INDEX_DEFAULT * 16 - 16 - 16), (Y_SPAWN_INDEX_DEFAULT * 16 - 16)));
        entities.add(new SignPost((X_SPAWN_INDEX_DEFAULT * 16 - 16 - 16), (Y_SPAWN_INDEX_DEFAULT * 16 + 16)));
        return entities;
    }

    private List<Item> createItemsForWorldMapPart01() {
        List<Item> items = new ArrayList<Item>();
        for (int i = 0; i < 5; i++) {
            Item milk = new Milk();
            milk.setPosition((X_SPAWN_INDEX_DEFAULT * Tile.WIDTH - Tile.WIDTH), (i * Tile.HEIGHT + (Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT - Tile.HEIGHT)));
            items.add(milk);
        }
        HoneyPot honeyPot = new HoneyPot();
        honeyPot.setPosition((X_SPAWN_INDEX_DEFAULT * Tile.WIDTH - Tile.WIDTH), (Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT - Tile.HEIGHT - Tile.HEIGHT));
        items.add(honeyPot);
        honeyPot = new HoneyPot();
        honeyPot.setPosition((X_SPAWN_INDEX_DEFAULT * Tile.WIDTH), (Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT - Tile.HEIGHT - Tile.HEIGHT));
        items.add(honeyPot);
        honeyPot = new HoneyPot();
        honeyPot.setPosition((X_SPAWN_INDEX_DEFAULT * Tile.WIDTH + Tile.WIDTH), (Y_SPAWN_INDEX_DEFAULT * Tile.HEIGHT - Tile.HEIGHT - Tile.HEIGHT));
        items.add(honeyPot);
        return items;
    }
}