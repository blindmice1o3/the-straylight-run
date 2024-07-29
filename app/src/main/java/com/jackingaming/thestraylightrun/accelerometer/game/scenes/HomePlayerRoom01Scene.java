package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.DOWN;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.LEFT;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.RIGHT;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.UP;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.outputs.FCVDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.inanimates.Inanimate;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TransferPointTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.nextweektonight.NextWeekTonightFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePlayerRoom01Scene extends Scene {
    public static final String TAG = HomePlayerRoom01Scene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_home_player_01;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.indoors_home_and_room;
    private static final int X_SPAWN_INDEX_PLAYER_FROM_STAIRS = 6;
    private static final int Y_SPAWN_INDEX_PLAYER_FROM_STAIRS = 1;
    private static final int X_SPAWN_INDEX_PLAYER_FROM_DOOR = 2;
    private static final int Y_SPAWN_INDEX_PLAYER_FROM_DOOR = 6;
    private static final int X_TRANSFER_POINT_INDEX_PLAYER_ROOM_02 = X_SPAWN_INDEX_PLAYER_FROM_STAIRS + 1;
    private static final int Y_TRANSFER_POINT_INDEX_PLAYER_ROOM_02 = Y_SPAWN_INDEX_PLAYER_FROM_STAIRS;
    private static final int X_TRANSFER_POINT_INDEX_WORLD = X_SPAWN_INDEX_PLAYER_FROM_DOOR;
    private static final int Y_TRANSFER_POINT_INDEX_WORLD = Y_SPAWN_INDEX_PLAYER_FROM_DOOR + 1;
    private static final int X_SPAWN_INDEX_TELEVISION = 3;
    private static final int Y_SPAWN_INDEX_TELEVISION = 1;
    public static final String ID_TELEVISION = "television";

    private static HomePlayerRoom01Scene instance;

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

    private HomePlayerRoom01Scene() {
    }

    public static HomePlayerRoom01Scene getInstance() {
        if (instance == null) {
            instance = new HomePlayerRoom01Scene();
        }
        return instance;
    }

    private void initTiles() {
        // [IMAGES]
        Bitmap bitmapIndoorsHomeAndRoom = BitmapFactory.decodeResource(resources,
                RES_ID_TILE_COLLISION_BACKGROUND);
        Bitmap bitmapHomePlayerRoom01 = Bitmap.createBitmap(bitmapIndoorsHomeAndRoom, 160, 16, 128, 128);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);
        // TILES
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, bitmapHomePlayerRoom01);
        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD];
        tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), WorldScene.TAG
        );
        tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_PLAYER_ROOM_02][Y_TRANSFER_POINT_INDEX_PLAYER_ROOM_02];
        tiles[X_TRANSFER_POINT_INDEX_PLAYER_ROOM_02][Y_TRANSFER_POINT_INDEX_PLAYER_ROOM_02] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), HomePlayerRoom02Scene.TAG
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
                Log.e(TAG, "HomePlayerRoom01Scene: player's  CollisionListener.onJustCollided(Entity)");

                if (collided instanceof Inanimate) {
                    if (((Inanimate) collided).getId().equals(ID_TELEVISION)) {
                        Log.e(TAG, "TELEVISION inanimate entity collision!!!");
                        // TODO: show dialog fragment TELEVISION.
                        pause();

                        boolean showToolbarOnDismiss = false;
                        Fragment fragment = NextWeekTonightFragment.newInstance(showToolbarOnDismiss);
                        String tag = NextWeekTonightFragment.TAG;
                        DialogFragment dialogFragment =
                                FCVDialogFragment.newInstance(fragment, tag, new FCVDialogFragment.DismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        unpause();
                                    }
                                });

                        gameListener.onShowDialogFragment(
                                dialogFragment, tag
                        );
                    }
                }
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

                if (isWalkableCorner1 && isWalkableCorner2) {
                    if (tiles[xIndex1][yIndex1] instanceof TransferPointTile &&
                            tiles[xIndex2][yIndex2] instanceof TransferPointTile) {
                        if (canUseTransferPoint) {
                            String idSceneDestination = ((TransferPointTile) tiles[xIndex1][yIndex1]).getIdSceneDestination();
                            if (idSceneDestination.equals(HomePlayerRoom02Scene.TAG)) {
                                Log.e(TAG, "transfer point: HomePlayerRoom02Scene");
                                gameListener.onChangeScene(HomePlayerRoom02Scene.getInstance());
                                return true;
                            } else if (idSceneDestination.equals(WorldScene.TAG)) {
                                Log.e(TAG, "transfer point: WorldScene");
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

    private float xBeforeTransfer = -1f;
    private float yBeforeTransfer = -1f;

    @Override
    public List<Object> exit() {
        Log.e(TAG, "exit()");

        xBeforeTransfer = player.getXPos();
        yBeforeTransfer = player.getYPos();

        stopEntityAnimations();

        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.removeImageViewOfEntityFromFrameLayout();
            }
        });

        List<Object> argsSceneTransfer = new ArrayList<>();
        argsSceneTransfer.add(HomePlayerRoom01Scene.TAG);
        return argsSceneTransfer;
    }

    private Entity.CollisionListener collisionListenerPlayer;
    private Entity.MovementListener movementListenerPlayer;

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

        if (xBeforeTransfer < 0) {
            player.setXPos(X_SPAWN_INDEX_PLAYER_FROM_STAIRS * widthSpriteDst);
            player.setYPos(Y_SPAWN_INDEX_PLAYER_FROM_STAIRS * heightSpriteDst);
        } else {
            Log.e(TAG, "xBeforeTransfer >= 0");
            if (args != null) {
                Log.e(TAG, "args != null");
                String idScene = (String) args.get(0);

                if (idScene.equals(HomePlayerRoom02Scene.TAG)) {
                    player.setXPos(X_SPAWN_INDEX_PLAYER_FROM_STAIRS * widthSpriteDst);
                    player.setYPos(Y_SPAWN_INDEX_PLAYER_FROM_STAIRS * heightSpriteDst);
                } else if (idScene.equals(WorldScene.TAG)) {
                    player.setXPos(X_SPAWN_INDEX_PLAYER_FROM_DOOR * widthSpriteDst);
                    player.setYPos(Y_SPAWN_INDEX_PLAYER_FROM_DOOR * heightSpriteDst);
                }
            } else {
                Log.e(TAG, "args == null");
            }
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
            // DO MOVE.
            if (e instanceof Player) {
//                Player player = (Player) e;
                player.updateViaSensorEvent(handler, xDelta, yDelta);
                validatePosition(player);
                gameCamera.centerOnEntity(player);
            }
//            else {
//                e.update(handler);
//                validatePosition(e);
//            }

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
        Entity.CollisionListener collisionListenerInanimate = null;
        Entity.MovementListener movementListenerInanimate = null;

        // [IMAGES]
        Bitmap image = tiles[X_SPAWN_INDEX_TELEVISION][Y_SPAWN_INDEX_TELEVISION].getImage();
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.addFrame(new BitmapDrawable(resources, image), 420);
        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawable);
        animationsByDirection.put(DOWN, animationDrawable);
        animationsByDirection.put(LEFT, animationDrawable);
        animationsByDirection.put(RIGHT, animationDrawable);

        // ENTITIES
        Inanimate television = new Inanimate(ID_TELEVISION, animationsByDirection,
                collisionListenerInanimate, movementListenerInanimate);
        television.setXPos(X_SPAWN_INDEX_TELEVISION * widthSpriteDst);
        television.setYPos(Y_SPAWN_INDEX_TELEVISION * heightSpriteDst);

        entities = new ArrayList<>();
        entities.add(television);
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
