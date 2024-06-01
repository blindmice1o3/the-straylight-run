package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.DOWN;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.LEFT;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.RIGHT;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.UP;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.choiceboxes.ChoiceDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TransferPointTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.sequencetrainer.SequenceTrainerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldScene extends Scene {
    public static final String TAG = WorldScene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_world_map;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.pokemon_gsc_kanto;
    private static final int X_SPAWN_INDEX_PLAYER = 200;
    private static final int Y_SPAWN_INDEX_PLAYER = 34;
    private static final int X_SPAWN_INDEX_RIVAL = 200;
    private static final int Y_SPAWN_INDEX_RIVAL = 12;
    private static final int X_SPAWN_INDEX_COIN = 201;
    private static final int Y_SPAWN_INDEX_COIN = 37;
    private static final int X_SPAWN_INDEX_RIVAL_LEADER = 201;
    private static final int Y_SPAWN_INDEX_RIVAL_LEADER = 15;
    private static final int X_SPAWN_INDEX_JR_TRAINER = 201;
    private static final int Y_SPAWN_INDEX_JR_TRAINER = 19;
    private static final int X_SPAWN_INDEX_LASS_02 = 200;
    private static final int Y_SPAWN_INDEX_LASS_02 = 22;
    private static final int X_SPAWN_INDEX_YOUNGSTER = 201;
    private static final int Y_SPAWN_INDEX_YOUNGSTER = 25;
    private static final int X_SPAWN_INDEX_LASS_01 = 200;
    private static final int Y_SPAWN_INDEX_LASS_01 = 28;
    private static final int X_SPAWN_INDEX_BUG_CATCHER = 201;
    private static final int Y_SPAWN_INDEX_BUG_CATCHER = 31;
    private static final int X_TRANSFER_POINT_INDEX_LAB = 239;
    private static final int Y_TRANSFER_POINT_INDEX_LAB = 5;
    public static int durationOfFrameInMilli = 420;

    private static WorldScene instance;

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

    private Bitmap[][] sprites;
    private Bitmap spriteCoin;
    private Bitmap spriteTileSolid, spriteTileWalkable, spriteTileBoulder;
    private List<Entity> entities;

    private WorldScene() {
    }

    public static WorldScene getInstance() {
        if (instance == null) {
            instance = new WorldScene();
        }
        return instance;
    }

    private void initTiles() {
        Bitmap fullWorldMap = BitmapFactory.decodeResource(resources,
                RES_ID_TILE_COLLISION_BACKGROUND);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, fullWorldMap);
        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_LAB][Y_TRANSFER_POINT_INDEX_LAB];
        tiles[X_TRANSFER_POINT_INDEX_LAB][Y_TRANSFER_POINT_INDEX_LAB] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), LabScene.TAG
        );
        widthWorldInTiles = tiles.length;
        heightWorldInTiles = tiles[0].length;
        widthWorldInPixels = widthWorldInTiles * widthSpriteDst;
        heightWorldInPixels = heightWorldInTiles * heightSpriteDst;
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

        // TODO: calling init() in Game.changeScene() between the exit() and enter() calls
        //  brought the tile images back and game camera was moving, but entities weren't.

        // TILES
        // [IMAGES]
        initTiles();

        // ENTITIES
        // [IMAGES]
        sprites = SpriteInitializer.initSprites(resources, widthSpriteDst, heightSpriteDst);
        spriteCoin = SpriteInitializer.initCoinSprite(resources);
        spriteTileSolid = SpriteInitializer.initSolidTileSprite(resources);
        spriteTileWalkable = SpriteInitializer.initWalkableTileSprite(resources);
        spriteTileBoulder = SpriteInitializer.initBoulderTileSprite(resources);
        List<Entity> entities = generateEntities();
        // TODO:
        Entity.init(
                entities,
                widthSpriteDst, heightSpriteDst
        )
        ;

        collisionListenerPlayer = generateCollisionListenerForPlayer();
        movementListenerPlayer = generateMovementListenerForPlayer();

        player.setCollisionListener(collisionListenerPlayer);
        player.setMovementListener(movementListenerPlayer);

        player.setxPos(X_SPAWN_INDEX_PLAYER * widthSpriteDst);
        player.setyPos(Y_SPAWN_INDEX_PLAYER * heightSpriteDst);

        // GAME CAMERA
        gameCamera.init(widthWorldInPixels, heightWorldInPixels);
        gameCamera.centerOnEntity(player);
    }

//    private boolean isFirstCollide = true;

    private Entity.CollisionListener generateCollisionListenerForPlayer() {
        return new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                Log.e(TAG, "WorldScene: player's  CollisionListener.onJustCollided(Entity)");

                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals("coin")) {
                        soundManager.sfxPlay(soundManager.sfxGetItem);
                        player.setSpeedBonus(4.0f);

//                        if (isFirstCollide) {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    gameListener.switchVisibilityOfNPCsToGone();
//                                }
//                            });
//
//                            isFirstCollide = false;
//                        } else {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    gameListener.switchVisibilityOfNPCsToVisible();
//                                }
//                            });
//
//                            isFirstCollide = true;
//                        }
                    } else if (((NonPlayableCharacter) collided).getId().equals("rival")) {
//                                soundManager.sfxPlay(soundManager.sfxHorn);
                    } else if (((NonPlayableCharacter) collided).getId().equals("rival leader")) {
                        pause();

                        gameListener.onShowDialogFragment(
                                instantiateRivalLeaderDialogFragment(gameListener),
                                "RivalLeaderDialogFragment"
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
                            if (idSceneDestination.equals(LabScene.TAG)) {
                                Log.e(TAG, "transfer point: LAB");
                                gameListener.onChangeScene(LabScene.getInstance());
                                return true;
                            }
                        }
                    }
                }

                return (isWalkableCorner1 && isWalkableCorner2);
            }
        };
    }

    boolean hadBeenTransferred = false;

    @Override
    public void update(long elapsed) {
        // TODO: REMOVE
        if (hadBeenTransferred) {
            player.setCollisionListener(collisionListenerPlayer);
            player.setMovementListener(movementListenerPlayer);

            player.setxPos(xBeforeTransfer);
            player.setyPos(yBeforeTransfer);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameListener.onUpdateEntity(player);
                }
            });
            gameCamera.centerOnEntity(player);

            hadBeenTransferred = false;
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
//        canvas.drawColor(Color.MAGENTA);
        canvas.drawColor(Color.GREEN);

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
        Log.e(TAG, "xBeforeTransfer: " + xBeforeTransfer);
        yBeforeTransfer = player.getyPos();
        Log.e(TAG, "yBeforeTransfer: " + yBeforeTransfer);

        hadBeenTransferred = true;

        stopEntityAnimations();
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.switchVisibilityOfNPCsToGone();
            }
        });

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

        player.setCollisionListener(collisionListenerPlayer);
        player.setMovementListener(movementListenerPlayer);

        Log.e(TAG, "xBeforeTransfer < 0 == " + Boolean.toString(xBeforeTransfer < 0));
        if (xBeforeTransfer < 0) {
            player.setxPos(X_SPAWN_INDEX_PLAYER * widthSpriteDst);
            player.setyPos(Y_SPAWN_INDEX_PLAYER * heightSpriteDst);
        } else {
            player.setxPos(xBeforeTransfer);
            player.setyPos(yBeforeTransfer);
        }
        Log.e(TAG, "player.getxPos(): " + player.getxPos());
        Log.e(TAG, "player.getyPos(): " + player.getyPos());

        // GAME CAMERA
        gameCamera.init(widthWorldInPixels, heightWorldInPixels);
        gameCamera.centerOnEntity(player);

        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.onUpdateEntity(player);
            }
        });

        Log.e(TAG, "gameCamera.xoffset: " + gameCamera.getxOffset());
        Log.e(TAG, "gameCamera.yoffset: " + gameCamera.getyOffset());
        Log.e(TAG, "widthSpriteDst: " + widthSpriteDst);

        startEntityAnimations();
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.switchVisibilityOfNPCsToVisible();
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

    private void updateGameEntities(float xDelta, float yDelta) {
        for (Entity e : entities) {
            // DO MOVE.
            if (e instanceof Player) {
//                Player player = (Player) e;
                player.updateViaSensorEvent(xDelta, yDelta);
                validatePosition(player);
                gameCamera.centerOnEntity(player);
            } else {
                e.update();
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

    //////////////////////////////////////////////////////////////////////////////////////////

    private List<Entity> generateEntities() {
        Entity.CollisionListener collisionListener = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals("coin")) {
                        soundManager.sfxPlay(soundManager.sfxGetItem);
                    }
                } else if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.CollisionListener collisionListenerCoin = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.MovementListener movementListener = new Entity.MovementListener() {
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

                return (isWalkableCorner1 && isWalkableCorner2);
            }
        };

        NonPlayableCharacter npcRival = generateNonPlayableCharacter("rival", 3,
                X_SPAWN_INDEX_RIVAL, Y_SPAWN_INDEX_RIVAL,
                false, DOWN,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcCoin = generateNonPlayableCharacter("coin", -1,
                X_SPAWN_INDEX_COIN, Y_SPAWN_INDEX_COIN,
                true, DOWN,
                collisionListenerCoin,
                movementListener);
        NonPlayableCharacter npcRivalLeader = generateNonPlayableCharacter("rival leader", 28,
                X_SPAWN_INDEX_RIVAL_LEADER, Y_SPAWN_INDEX_RIVAL_LEADER,
                true, LEFT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcJrTrainer = generateNonPlayableCharacter("jr trainer", 11,
                X_SPAWN_INDEX_JR_TRAINER, Y_SPAWN_INDEX_JR_TRAINER,
                true, LEFT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcLass02 = generateNonPlayableCharacter("lass02", 17,
                X_SPAWN_INDEX_LASS_02, Y_SPAWN_INDEX_LASS_02,
                true, RIGHT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcYoungster = generateNonPlayableCharacter("youngster", 10,
                X_SPAWN_INDEX_YOUNGSTER, Y_SPAWN_INDEX_YOUNGSTER,
                true, LEFT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcLass01 = generateNonPlayableCharacter("lass01", 17,
                X_SPAWN_INDEX_LASS_01, Y_SPAWN_INDEX_LASS_01,
                true, RIGHT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcBugCatcher = generateNonPlayableCharacter("bug catcher", 10,
                X_SPAWN_INDEX_BUG_CATCHER, Y_SPAWN_INDEX_BUG_CATCHER,
                true, LEFT,
                collisionListener,
                movementListener);
        player = generatePlayer();

        entities = new ArrayList<>();
        entities.add(npcRival);
        entities.add(npcCoin);
        entities.add(npcRivalLeader);
        entities.add(npcJrTrainer);
        entities.add(npcLass02);
        entities.add(npcYoungster);
        entities.add(npcLass01);
        entities.add(npcBugCatcher);
        entities.add(player);

        return entities;
    }

    private NonPlayableCharacter generateNonPlayableCharacter(String id, int yIndexForSprites,
                                                              int xIndexSpawn, int yIndexSpawn,
                                                              boolean isStationary, Direction directionFacing,
                                                              Entity.CollisionListener entityCollisionListener,
                                                              Entity.MovementListener entityMovementListener) {
        AnimationDrawable animationDrawableUp = new AnimationDrawable();
        animationDrawableUp.setOneShot(false);
        AnimationDrawable animationDrawableDown = new AnimationDrawable();
        animationDrawableDown.setOneShot(false);
        AnimationDrawable animationDrawableLeft = new AnimationDrawable();
        animationDrawableLeft.setOneShot(false);
        AnimationDrawable animationDrawableRight = new AnimationDrawable();
        animationDrawableRight.setOneShot(false);
        if (yIndexForSprites >= 0) {
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[3][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[4][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[5][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[0][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[1][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[2][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, sprites[6][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, sprites[7][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, sprites[8][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, sprites[9][yIndexForSprites]), durationOfFrameInMilli);
        } else {
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
        }

        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawableUp);
        animationsByDirection.put(DOWN, animationDrawableDown);
        animationsByDirection.put(LEFT, animationDrawableLeft);
        animationsByDirection.put(RIGHT, animationDrawableRight);
        NonPlayableCharacter nonPlayableCharacter = new NonPlayableCharacter(id,
                animationsByDirection,
                directionFacing,
                entityCollisionListener,
                entityMovementListener);

        nonPlayableCharacter.setxPos(xIndexSpawn * widthSpriteDst);
        nonPlayableCharacter.setyPos(yIndexSpawn * heightSpriteDst);
        if (isStationary) {
            nonPlayableCharacter.turnStationaryOn();
        }

        return nonPlayableCharacter;
    }

    private Player generatePlayer() {
        AnimationDrawable animationDrawableUp = new AnimationDrawable();
        animationDrawableUp.setOneShot(false);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[3][1]), durationOfFrameInMilli);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[4][1]), durationOfFrameInMilli);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[5][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableDown = new AnimationDrawable();
        animationDrawableDown.setOneShot(false);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[0][1]), durationOfFrameInMilli);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[1][1]), durationOfFrameInMilli);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[2][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableLeft = new AnimationDrawable();
        animationDrawableLeft.setOneShot(false);
        animationDrawableLeft.addFrame(
                new BitmapDrawable(resources, sprites[6][1]), durationOfFrameInMilli);
        animationDrawableLeft.addFrame(
                new BitmapDrawable(resources, sprites[7][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableRight = new AnimationDrawable();
        animationDrawableRight.setOneShot(false);
        animationDrawableRight.addFrame(
                new BitmapDrawable(resources, sprites[8][1]), durationOfFrameInMilli);
        animationDrawableRight.addFrame(
                new BitmapDrawable(resources, sprites[9][1]), durationOfFrameInMilli);

        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawableUp);
        animationsByDirection.put(DOWN, animationDrawableDown);
        animationsByDirection.put(LEFT, animationDrawableLeft);
        animationsByDirection.put(RIGHT, animationDrawableRight);
        Player player = new Player(animationsByDirection);
        return player;
    }

    private TypeWriterDialogFragment typeWriterDialogFragmentRivalLeader = null;

    private TypeWriterDialogFragment instantiateRivalLeaderDialogFragment(Game.GameListener gameListener) {
        String message = "Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";

        typeWriterDialogFragmentRivalLeader =
                TypeWriterDialogFragment.newInstance(50L, message, new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");

                                unpause();
                            }
                        },
                        new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");

                                // TODO:
                                ChoiceDialogFragment choiceDialogFragmentYesOrNo = ChoiceDialogFragment.newInstance(
                                        new ChoiceDialogFragment.ChoiceListener() {
                                            @Override
                                            public void onChoiceYesSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                                Log.e(TAG, "YES selected");

                                                soundManager.sfxPlay(soundManager.sfxGetItem);

                                                choiceDialogFragment.dismiss();
                                                typeWriterDialogFragmentRivalLeader.dismiss();

                                                // TODO:
                                                gameListener.onReplaceFragmentInMainActivity(
                                                        SequenceTrainerFragment.newInstance(null, null)
                                                );
                                            }

                                            @Override
                                            public void onChoiceNoSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                                Log.e(TAG, "NO selected");

                                                soundManager.sfxPlay(soundManager.sfxCollision);

                                                choiceDialogFragment.dismiss();
                                                typeWriterDialogFragmentRivalLeader.dismiss();

                                                // TODO:
                                            }
                                        });

                                gameListener.onShowDialogFragment(
                                        choiceDialogFragmentYesOrNo,
                                        "ChoiceYesOrNoDialogFragment"
                                );
                            }
                        });
        return typeWriterDialogFragmentRivalLeader;
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

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    static class SpriteInitializer {
        private static final int WIDTH_SPRITE_SHEET_ACTUAL = 187;
        private static final int HEIGHT_SPRITE_SHEET_ACTUAL = 1188;
        private static final int COLUMNS = 10;
        private static final int ROWS = 56; // section: Characters
        private static final int X_OFFSET_INIT = 9;
        private static final int Y_OFFSET_INIT = 34;
        private static final int WIDTH_SPRITE = 16;
        private static final int HEIGHT_SPRITE = 16;
        private static final int WIDTH_DIVIDER = 1;
        private static final int HEIGHT_DIVIDER = 1;

        private static final int xSolidTile = 10;
        private static final int ySolidTile = 1147;
        private static final int xWalkableTile = 10;
        private static final int yWalkableTile = 1164;
        private static final int xBoulderTile = 112;
        private static final int yBoulderTile = 1088;

        public static Bitmap[][] initSprites(Resources resources, int widthSpriteDst, int heightSpriteDst) {
            Bitmap[][] sprites = new Bitmap[COLUMNS][ROWS];

            Bitmap spriteSheet = BitmapFactory.decodeResource(resources,
                    R.drawable.gbc_pokemon_red_blue_characters_overworld);

            float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL;
            float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL;

            int widthSpriteConverted = (int) (WIDTH_SPRITE * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE * ratioVertical);

            int yOffset = Y_OFFSET_INIT;
            int xOffset = X_OFFSET_INIT;
            for (int i = 0; i < COLUMNS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    int xOffsetConverted = (int) (xOffset * ratioHorizontal);
                    int yOffsetConverted = (int) (yOffset * ratioVertical);

                    Bitmap sprite = Bitmap.createBitmap(spriteSheet,
                            xOffsetConverted, yOffsetConverted, widthSpriteConverted, heightSpriteConverted);
                    sprites[i][j] = Bitmap.createScaledBitmap(sprite, widthSpriteDst, heightSpriteDst, true);

                    yOffset += (HEIGHT_SPRITE + HEIGHT_DIVIDER);
                }
                yOffset = Y_OFFSET_INIT;
                xOffset += (WIDTH_SPRITE + WIDTH_DIVIDER);
            }

            return sprites;
        }

        public static Bitmap initCoinSprite(Resources resources) {
            Bitmap spriteCoin = BitmapFactory.decodeResource(resources,
                    R.drawable.ic_coins_l);

            return spriteCoin;
        }

        public static Bitmap initSolidTileSprite(Resources resources) {
            return initTileSprite(resources, xSolidTile, ySolidTile);
        }

        public static Bitmap initWalkableTileSprite(Resources resources) {
            return initTileSprite(resources, xWalkableTile, yWalkableTile);
        }

        public static Bitmap initBoulderTileSprite(Resources resources) {
            return initTileSprite(resources, xBoulderTile, yBoulderTile);
        }

        private static Bitmap initTileSprite(Resources resources, int xTile, int yTile) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(resources,
                    R.drawable.gbc_pokemon_red_blue_characters_overworld);

            float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL;
            float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL;

            int widthSpriteConverted = (int) (WIDTH_SPRITE * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE * ratioVertical);

            Bitmap tileWalkable = Bitmap.createBitmap(spriteSheet,
                    (int) (xTile * ratioHorizontal), (int) (yTile * ratioVertical),
                    (int) (widthSpriteConverted - ratioHorizontal), (int) (heightSpriteConverted - ratioVertical));

            return tileWalkable;
        }
    }
}
