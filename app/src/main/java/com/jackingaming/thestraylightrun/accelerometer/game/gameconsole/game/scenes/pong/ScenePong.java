package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pong;

import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.SignPost;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.pong.Ball;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.pong.Bat;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenePong extends Scene {
    public static final int X_SPAWN_INDEX_DEFAULT = 0;
    public static final int Y_SPAWN_INDEX_DEFAULT = 4;

    private static ScenePong uniqueInstance;

    private ScenePong() {
        super();
        List<Entity> entitiesForPong = createEntitiesForPong();
        entityManager.loadEntities(entitiesForPong);
        List<Item> itemsForPong = createItemsForPong();
        itemManager.loadItems(itemsForPong);
    }

    public static ScenePong getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScenePong();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForPong = createAndInitTilesForPong(game);
        tileManager.loadTiles(tilesForPong);
        Map<String, Rect> transferPointsForPong = createTransferPointsForPong();
        tileManager.loadTransferPoints(transferPointsForPong); // transferPoints are transient and should be reloaded everytime.
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

    private Tile[][] createAndInitTilesForPong(Game game) {
        int rows = GameCamera.CLIP_HEIGHT_IN_TILE_DEFAULT;
        int columns = GameCamera.CLIP_WIDTH_IN_TILE_DEFAULT;
        Tile[][] pong = new Tile[rows][columns];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Tile tile = new Tile("x");
                tile.init(game, x, y, null);
                pong[y][x] = tile;
            }
        }

        return pong;
    }

    private Map<String, Rect> createTransferPointsForPong() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        return transferPoints;
    }

    private List<Entity> createEntitiesForPong() {
        List<Entity> entities = new ArrayList<Entity>();

        Ball ball = new Ball(0, 0);
        entities.add(ball);
        Bat bat = new Bat(0, 0);
        entities.add(bat);
        Entity signPost = new SignPost(3 * Tile.WIDTH, 2 * Tile.HEIGHT);
        entities.add(signPost);
        signPost = new SignPost(4 * Tile.WIDTH, 5 * Tile.HEIGHT);
        entities.add(signPost);

        return entities;
    }

    private List<Item> createItemsForPong() {
        List<Item> items = new ArrayList<Item>();
        return items;
    }
}