package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.computer.ComputerDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManagerLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHome02 extends Scene {
    public static final String TAG = SceneHome02.class.getSimpleName();

    public static final int X_SPAWN_INDEX_DEFAULT = 7;
    public static final int Y_SPAWN_INDEX_DEFAULT = 2;
    private static SceneHome02 uniqueInstance;

    private ComputerDialogFragment computerDialogFragment;

    private SceneHome02() {
        super();
        List<Entity> entitiesForHome02 = createEntitiesForHome02();
        entityManager.loadEntities(entitiesForHome02);
        List<Item> itemsForHome02 = createItemsForHome02();
        itemManager.loadItems(itemsForHome02);

        computerDialogFragment = new ComputerDialogFragment();
    }

    public ComputerDialogFragment getComputerDialogFragment() {
        return computerDialogFragment;
    }

    public void setComputerDialogFragment(ComputerDialogFragment computerDialogFragment) {
        this.computerDialogFragment = computerDialogFragment;
    }

    public static SceneHome02 getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneHome02();
        }
        return uniqueInstance;
    }

    public static void setInstance(SceneHome02 sceneHome02) {
        uniqueInstance = sceneHome02;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForHome02 = createAndInitTilesForHome02(game);
        tileManager.loadTiles(tilesForHome02);
        Map<String, Rect> transferPointsForHome02 = createTransferPointsForHome02();
        tileManager.loadTransferPoints(transferPointsForHome02); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        entityManager.init(game);
        itemManager.init(game);

        computerDialogFragment.init(game);
    }

    public void showComputerDialog() {
        game.setPaused(true);
        computerDialogFragment.show(((MainActivity) game.getContext()).getSupportFragmentManager(), ComputerDialogFragment.TAG);
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

    private Tile[][] createAndInitTilesForHome02(Game game) {
        String home02LoadedAsString = TileManagerLoader.loadFileAsString(game.getContext().getResources(), R.raw.tile_home02);
        Tile[][] home02 = TileManagerLoader.convertStringToTiles(home02LoadedAsString);
        Bitmap imageHome02 = cropImageHome02(game.getContext().getResources());

        // Initialize the tiles (provide image and define walkable)
        // Assign image and init() all the tiles in worldMapPart01.
        for (int y = 0; y < home02.length; y++) {
            for (int x = 0; x < home02[0].length; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;

                Tile tile = home02[y][x];
                //GenericWalkableTile
                if (tile.getId().equals("0")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHome02, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                }
                //GenericSolidTile
                else if (tile.getId().equals("1")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHome02, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //ComputerTile
                else if (tile.getId().equals("4")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHome02, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                }
                //GameConsoleTile
                else if (tile.getId().equals("5")) {
                    Bitmap tileSprite = Bitmap.createBitmap(imageHome02, xInPixel, yInPixel, widthInPixel, heightInPixel);
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                } else {
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                }
            }
        }

        return home02;
    }

    private Bitmap cropImageHome02(Resources resources) {
        Log.d(TAG, "SceneHome02.cropImageHome02(Resources)");

        Bitmap indoorsHomeAndRoom = BitmapFactory.decodeResource(resources, R.drawable.indoors_home_and_room);
        Bitmap home02 = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        home02 = Bitmap.createBitmap(indoorsHomeAndRoom, 16, 16, 128, 128);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap home02's (width, height): " + home02.getWidth() + ", " + home02.getHeight());

        return home02;
    }

    public Map<String, Rect> createTransferPointsForHome02() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("HOME_01", new Rect((7 * Tile.WIDTH), (1 * Tile.HEIGHT),
                (7 * Tile.WIDTH) + (1 * Tile.WIDTH), (1 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    @Override
    public void drawCurrentFrame(Canvas canvas) {
        super.drawCurrentFrame(canvas);
//        Rect screenRectOfTransferPoint = GameCamera.getInstance().convertInGameRectToScreenRect(tileManager.getTransferPointBounds("HOME_01"));
//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(screenRectOfTransferPoint, paint);
//
//        Rect screenRectOfPlayer = GameCamera.getInstance().convertInGameRectToScreenRect(Player.getInstance().getCollisionBounds(0, 0));
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(screenRectOfPlayer, paint);
    }

    public List<Entity> createEntitiesForHome02() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        return entities;
    }

    public List<Item> createItemsForHome02() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        return items;
    }
}