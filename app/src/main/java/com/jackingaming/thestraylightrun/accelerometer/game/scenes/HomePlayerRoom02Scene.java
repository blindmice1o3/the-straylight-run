package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.FCVDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.GameConsoleFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TransferPointTile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.UniqueSolidTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class HomePlayerRoom02Scene extends Scene {
    public static final String TAG = HomePlayerRoom02Scene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_home_player_02;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.indoors_home_and_room;
    private static final int X_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA = 3;
    private static final int Y_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA = 5;
    private static final int X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 = 6;
    private static final int Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 = 1;
    private static final int X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01 = X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 + 1;
    private static final int Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01 = Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01;

    private static HomePlayerRoom02Scene instance;

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

    private List<Entity> entities;

    private HomePlayerRoom02Scene() {
    }

    public static HomePlayerRoom02Scene getInstance() {
        if (instance == null) {
            instance = new HomePlayerRoom02Scene();
        }
        return instance;
    }

    private void initTiles() {
        // [IMAGES]
        Bitmap bitmapIndoorsHomeAndRoom = BitmapFactory.decodeResource(resources,
                RES_ID_TILE_COLLISION_BACKGROUND);
        Bitmap bitmapHomePlayerRoom02 = Bitmap.createBitmap(bitmapIndoorsHomeAndRoom, 16, 16, 128, 128);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);

        // [TILES]
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, bitmapHomePlayerRoom02);
        // transfer point: home player room01
        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01][Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01];
        tiles[X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01][Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), HomePlayerRoom01Scene.TAG
        );

        widthWorldInTiles = tiles.length;
        heightWorldInTiles = tiles[0].length;
        widthWorldInPixels = widthWorldInTiles * widthSpriteDst;
        heightWorldInPixels = heightWorldInTiles * heightSpriteDst;
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

        initTiles();
        initEntities();

        collisionListenerPlayer = generateCollisionListenerForPlayer();
        movementListenerPlayer = generateMovementListenerForPlayer();
    }

    private Entity.CollisionListener generateCollisionListenerForPlayer() {
        return new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                Log.e(TAG, "player's CollisionListener.onJustCollided(Entity)");
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

                // BOTH WALKABLE
                if (isWalkableCorner1 && isWalkableCorner2) {
                    if (tiles[xIndex1][yIndex1] instanceof TransferPointTile &&
                            tiles[xIndex2][yIndex2] instanceof TransferPointTile) {
                        if (canUseTransferPoint) {
                            String idSceneDestination = ((TransferPointTile) tiles[xIndex1][yIndex1]).getIdSceneDestination();
                            if (idSceneDestination.equals(HomePlayerRoom01Scene.TAG)) {
                                Log.e(TAG, "transfer point: HomePlayerRoom01Scene");
                                gameListener.onChangeScene(HomePlayerRoom01Scene.getInstance());
                                return true;
                            }
                        }
                    } else {
                        Log.e(TAG, "tile is NOT TransferPointTile");
                    }
                }
                // BOTH SOLID
                else if (!isWalkableCorner1 && !isWalkableCorner2 &&
                        xIndex1 >= 0 && xIndex1 < tiles.length &&
                        yIndex1 >= 0 && yIndex1 < tiles[xIndex1].length &&
                        xIndex2 >= 0 && xIndex2 < tiles.length &&
                        yIndex2 >= 0 && yIndex2 < tiles[xIndex1].length) {
                    if (tiles[xIndex1][yIndex1] instanceof UniqueSolidTile &&
                            tiles[xIndex2][yIndex2] instanceof UniqueSolidTile) {
                        String id = ((UniqueSolidTile) tiles[xIndex2][yIndex2]).getId();
                        if (id.equals(UniqueSolidTile.COMPUTER)) {
                            Log.e(TAG, "unique solid tile: COMPUTER");
                            // TODO:
                            return false;
                        } else if (id.equals(UniqueSolidTile.GAME_CONSOLE)) {
                            Log.e(TAG, "unique solid tile: GAME CONSOLE");
                            pause();

                            // Other options: Pocket Critters, Pooh Farmer, Evo, Pong, Frogger
                            String gameTitle = "Pooh Farmer";
                            Fragment fragment = GameConsoleFragment.newInstance(gameTitle);
                            String tag = GameConsoleFragment.TAG;
                            boolean canceledOnTouchOutside = false;
                            DialogFragment dialogFragment =
                                    FCVDialogFragment.newInstance(fragment, tag,
                                            canceledOnTouchOutside, FCVDialogFragment.DEFAULT_WIDTH_IN_DECIMAL, FCVDialogFragment.DEFAULT_HEIGHT_IN_DECIMAL,
                                            new FCVDialogFragment.LifecycleListener() {
                                                @Override
                                                public void onResume() {
                                                    // Intentionally blank.
                                                }

                                                @Override
                                                public void onDismiss() {
                                                    unpause();
                                                }
                                            });

                            gameListener.onShowDialogFragment(
                                    dialogFragment, tag
                            );

                            return false;
                        }
                    } else {
                        Log.e(TAG, "tile is NOT UniqueSolidTile");
                    }
                }

                return (isWalkableCorner1 && isWalkableCorner2);
            }
        };
    }

    @Override
    public void update(long elapsed) {
        if (paused) {
            return;
        }

        // INPUTS
        float[] dataAccelerometer = gameListener.onCheckAccelerometer();
        float xDelta = dataAccelerometer[0];
        float yDelta = dataAccelerometer[1];

        // ENTITIES
        updateGameEntities(xDelta, yDelta);
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
        Log.e(TAG, "exit()");

        stopEntityAnimations();

        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.removeImageViewOfEntityFromFrameLayout();
            }
        });

        List<Object> argsSceneTransfer = new ArrayList<>();
        argsSceneTransfer.add(HomePlayerRoom02Scene.TAG);
        return argsSceneTransfer;
    }

    private Entity.CollisionListener collisionListenerPlayer;
    private Entity.MovementListener movementListenerPlayer;
    private String idScenePrevious;

    @Override
    public void enter(List<Object> args) {
        Log.e(TAG, "enter()");
        transferPointCoolDownElapsedInMillis = 0L;
        canUseTransferPoint = false;

        Entity.replaceEntitiesForNewScene(entities);
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.instantiateImageViewForEntities(entities);
                gameListener.addImageViewOfEntityToFrameLayout(widthSpriteDst, heightSpriteDst);
            }
        });

        player.setCollisionListener(collisionListenerPlayer);
        player.setMovementListener(movementListenerPlayer);

        if (args != null) {
            Log.e(TAG, "args != null");
            idScenePrevious = (String) args.get(0);
            if (idScenePrevious.equals(LabScene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA * heightSpriteDst);
            } else if (idScenePrevious.equals(HomePlayerRoom01Scene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 * heightSpriteDst);
            }
        } else {
            Log.e(TAG, "args == null");
            player.setXPos(0);
            player.setYPos(0);
        }

        // GAME CAMERA
        gameCamera.init(widthWorldInPixels, heightWorldInPixels);

        startEntityAnimations();
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

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    private void updateGameEntities(float xDelta, float yDelta) {
        for (Entity e : entities) {
            if (e instanceof Player) {
//                Player player = (Player) e;
                player.updateViaSensorEvent(handler, xDelta, yDelta);
                validatePosition(player);
                gameCamera.centerOnEntity(player);
            } else {
                e.update(handler);
                validatePosition(e);
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameListener.onUpdateEntity(e);
                }
            });
        }
    }

    private void validatePosition(Entity e) {
        if (e.getXPos() < 0) {
            e.setXPos(0);
        }
        if (e.getXPos() > widthWorldInPixels) {
            e.setXPos(widthWorldInPixels);
        }
        if (e.getYPos() < 0) {
            e.setYPos(0);
        }
        if (e.getYPos() > heightWorldInPixels) {
            e.setYPos(heightWorldInPixels);
        }
    }

    private void initEntities() {
        entities = new ArrayList<>();
        entities.add(player);
    }

    private void pause() {
        Log.e(TAG, "pause()");

        paused = true;
        stopEntityAnimations();
    }

    private void unpause() {
        Log.e(TAG, "unpause()");

        paused = false;
        startEntityAnimations();
    }

    private void stopEntityAnimations() {
        for (Entity e : entities) {
            e.stopAnimations();
        }
    }

    private void startEntityAnimations() {
        for (Entity e : entities) {
            e.startAnimations();
        }
    }
}
