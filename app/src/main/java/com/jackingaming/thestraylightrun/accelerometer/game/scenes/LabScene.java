package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.List;

public class LabScene extends Scene {
    public static final String TAG = LabScene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_lab;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.indoors_home_and_room;
    private static final int X_SPAWN_INDEX = 4;
    private static final int Y_SPAWN_INDEX = 10;

    private static LabScene instance;

    private Resources resources;
    private Handler handler;
    private SoundManager soundManager;
    private Game.GameListener gameListener;
    private Player player;
    private GameCamera gameCamera;
    private int widthSurfaceView, heightSurfaceView;
    private int widthSpriteDst, heightSpriteDst;

    private int widthWorldInTiles, heightWorldInTiles;
    private int widthWorldInPixels, heightWorldInPixels;
    private Tile[][] tiles;

    private LabScene() {
    }

    public static LabScene getInstance() {
        if (instance == null) {
            instance = new LabScene();
        }
        return instance;
    }

    public void init(Resources resources, Handler handler, SoundManager soundManager,
                     Game.GameListener gameListener, GameCamera gameCamera,
                     int widthSurfaceView, int heightSurfaceView,
                     int widthSpriteDst, int heightSpriteDst) {
        this.resources = resources;
        this.handler = handler;
        this.soundManager = soundManager;
        this.gameListener = gameListener;
        this.gameCamera = gameCamera;
        this.widthSurfaceView = widthSurfaceView;
        this.heightSurfaceView = heightSurfaceView;
        this.widthSpriteDst = widthSpriteDst;
        this.heightSpriteDst = heightSpriteDst;

        // TILES
        // [IMAGES]
        Bitmap bitmapIndoorsHomeAndRoom = BitmapFactory.decodeResource(resources,
                RES_ID_TILE_COLLISION_BACKGROUND);
        Bitmap bitmapLab = Bitmap.createBitmap(bitmapIndoorsHomeAndRoom, 23, 544, 160, 192);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, bitmapLab);
        widthWorldInTiles = tiles.length;
        heightWorldInTiles = tiles[0].length;
        widthWorldInPixels = widthWorldInTiles * widthSpriteDst;
        heightWorldInPixels = heightWorldInTiles * heightSpriteDst;

        // GAME CAMERA
        gameCamera.init(widthWorldInPixels, heightWorldInPixels);
    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public void draw(Canvas canvas) {
        // TILES
        int xStart = (int) Math.max(0, gameCamera.getxOffset() / widthSpriteDst);
        int xEnd = (int) Math.min(widthWorldInTiles, ((gameCamera.getxOffset() + widthSurfaceView) / widthSpriteDst) + 1);
        int yStart = (int) Math.max(0, gameCamera.getyOffset() / heightSpriteDst);
        int yEnd = (int) Math.min(heightWorldInTiles, ((gameCamera.getyOffset() + heightSurfaceView) / heightSpriteDst) + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                tiles[x][y].render(canvas,
                        (int) (x * widthSpriteDst - gameCamera.getxOffset()),
                        (int) (y * heightSpriteDst - gameCamera.getyOffset()),
                        widthSpriteDst, heightSpriteDst);
            }
        }
    }

    @Override
    public List<Object> exit() {
        return null;
    }

    @Override
    public void enter(Player player, List<Object> args) {
        this.player = player;

        player.setxPos(X_SPAWN_INDEX * widthSpriteDst);
        player.setyPos(Y_SPAWN_INDEX * widthSpriteDst);
        gameCamera.centerOnEntity(player);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }
}
