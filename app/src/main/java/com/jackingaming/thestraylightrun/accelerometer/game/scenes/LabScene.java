package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TransferPointTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.List;

public class LabScene extends Scene {
    public static final String TAG = LabScene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_lab;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.indoors_home_and_room;
    private static final int X_SPAWN_INDEX_PLAYER = 4;
    private static final int Y_SPAWN_INDEX_PLAYER = 10;
    private static final int X_TRANSFER_POINT_INDEX_WORLD = X_SPAWN_INDEX_PLAYER;
    private static final int Y_TRANSFER_POINT_INDEX_WORLD = Y_SPAWN_INDEX_PLAYER + 1;

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
    private long transferPointCoolDownElapsedInMillis = 0L;
    private boolean canUseTransferPoint = true;

    private LabScene() {
    }

    public static LabScene getInstance() {
        if (instance == null) {
            instance = new LabScene();
        }
        return instance;
    }

    public void init(Player player, Resources resources, Handler handler, SoundManager soundManager,
                     Game.GameListener gameListener, GameCamera gameCamera,
                     int widthSurfaceView, int heightSurfaceView,
                     int widthSpriteDst, int heightSpriteDst) {
        this.player = player;
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
        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD];
        tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), WorldScene.TAG
        );
        widthWorldInTiles = tiles.length;
        heightWorldInTiles = tiles[0].length;
        widthWorldInPixels = widthWorldInTiles * widthSpriteDst;
        heightWorldInPixels = heightWorldInTiles * heightSpriteDst;

        collisionListenerPlayer = generateCollisionListenerForPlayer();
        movementListenerPlayer = generateMovementListenerForPlayer();
    }

    private Entity.CollisionListener generateCollisionListenerForPlayer() {
        return new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                Log.e(TAG, "LabScene: player's  CollisionListener.onJustCollided(Entity)");
            }
        };
    }

    private Entity.MovementListener generateMovementListenerForPlayer() {
        return new Entity.MovementListener() {

            private long timePrevious;

            @Override
            public boolean onMove(int[] futureCorner1, int[] futureCorner2) {
                int xFutureCorner1 = futureCorner1[0];
                int yFutureCorner1 = futureCorner1[1];
                int xFutureCorner2 = futureCorner2[0];
                int yFutureCorner2 = futureCorner2[1];

                int xIndex1 = xFutureCorner1 / widthSpriteDst;
                int yIndex1 = yFutureCorner1 / heightSpriteDst;
                int xIndex2 = xFutureCorner2 / widthSpriteDst;
                int yIndex2 = yFutureCorner2 / heightSpriteDst;

                boolean isWalkableCorner1 = checkIsWalkableTile(xIndex1, yIndex1);
                boolean isWalkableCorner2 = checkIsWalkableTile(xIndex2, yIndex2);

                // TRANSFER POINTS
                if (transferPointCoolDownElapsedInMillis < DEFAULT_TRANSFER_POINT_COOL_DOWN_THRESHOLD_IN_MILLI) {
                    long timeNow = System.currentTimeMillis();
                    if (transferPointCoolDownElapsedInMillis == 0) {
                        timePrevious = timeNow;
                        timeNow = System.currentTimeMillis() + 1;
                    }

                    long elapsed = timeNow - timePrevious;
                    transferPointCoolDownElapsedInMillis += elapsed;

                    if (transferPointCoolDownElapsedInMillis >= DEFAULT_TRANSFER_POINT_COOL_DOWN_THRESHOLD_IN_MILLI) {
                        canUseTransferPoint = true;
                    }

                    timePrevious = timeNow;
                }

                if (isWalkableCorner1) {
                    if (tiles[xIndex1][yIndex1] instanceof TransferPointTile) {
                        if (canUseTransferPoint) {
                            String idSceneDestination = ((TransferPointTile) tiles[xIndex1][yIndex1]).getIdSceneDestination();
                            if (idSceneDestination.equals(WorldScene.TAG)) {
                                Log.e(TAG, "transfer point: WORLD");
                                gameListener.onChangeScene(WorldScene.getInstance());
                                return true;
                            }
                        }
                    }
                } else if (isWalkableCorner2) {
                    if (tiles[xIndex2][yIndex2] instanceof TransferPointTile) {
                        if (canUseTransferPoint) {
                            String idSceneDestination = ((TransferPointTile) tiles[xIndex2][yIndex2]).getIdSceneDestination();
                            if (idSceneDestination.equals(WorldScene.TAG)) {
                                gameListener.onChangeScene(WorldScene.getInstance());
                                return true;
                            }
                        }
                    }
                }

                return (isWalkableCorner1 && isWalkableCorner2);
            }
        };
    }

    @Override
    public void update(long elapsed) {
        // INPUTS
        float[] dataAccelerometer = gameListener.onCheckAccelerometer();
        float xDelta = dataAccelerometer[0];
        float yDelta = dataAccelerometer[1];

        player.updateViaSensorEvent(xDelta, yDelta);
        validatePosition(player);
        gameCamera.centerOnEntity(player);

        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.onUpdateEntity(player);
            }
        });
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

    private float xBeforeTransfer = -1f;
    private float yBeforeTransfer = -1f;

    @Override
    public List<Object> exit() {
        Log.e(TAG, "exit()");

        xBeforeTransfer = player.getxPos();
        yBeforeTransfer = player.getyPos();
        return null;
    }

    private Entity.CollisionListener collisionListenerPlayer;
    private Entity.MovementListener movementListenerPlayer;

    @Override
    public void enter(List<Object> args) {
        Log.e(TAG, "enter() widthWorldInPixels: " + widthWorldInPixels);
        Log.e(TAG, "enter() heightWorldInPixels: " + heightWorldInPixels);
        transferPointCoolDownElapsedInMillis = 0L;
        canUseTransferPoint = false;

        player.startAnimations();
        player.setCollisionListener(collisionListenerPlayer);
        player.setMovementListener(movementListenerPlayer);

        if (xBeforeTransfer < 0) {
            player.setxPos(X_SPAWN_INDEX_PLAYER * widthSpriteDst);
            player.setyPos(Y_SPAWN_INDEX_PLAYER * heightSpriteDst);
        } else {
            player.setxPos(xBeforeTransfer);
            player.setyPos(yBeforeTransfer);
        }

        // GAME CAMERA
        gameCamera.init(widthWorldInPixels, heightWorldInPixels);
        gameCamera.centerOnEntity(player);

        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.onUpdateEntity(player);
            }
        });
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean checkIsWalkableTile(int x, int y) {
        if (x < 0 || x >= widthWorldInTiles || y < 0 || y >= heightWorldInTiles) {
            return false;
        }

        Tile tile = tiles[x][y];
        boolean isWalkable = !tile.isSolid();
        return isWalkable;
    }

    private void validatePosition(Entity e) {
        if (e.getxPos() < 0) {
            e.setxPos(0);
        }
        if (e.getxPos() > widthWorldInPixels) {
            e.setxPos(widthWorldInPixels);
        }
        if (e.getyPos() < 0) {
            e.setyPos(0);
        }
        if (e.getyPos() > heightWorldInPixels) {
            e.setyPos(heightWorldInPixels);
        }
    }
}
