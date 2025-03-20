package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.DOWN;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.LEFT;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.RIGHT;
import static com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction.UP;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ChoiceDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.FCVDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.GameConsoleFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveLeftCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveRightCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveUpCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MovementCommand;
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
    private static final int X_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE = 200;
    private static final int Y_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE = 34;
    private static final int X_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA = 239;
    private static final int Y_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA = 6;
    private static final int X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 = 65;
    private static final int Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 = 204;
    private static final int X_SPAWN_INDEX_PLAYER_HOME_RIVAL = 73;
    private static final int Y_SPAWN_INDEX_PLAYER_HOME_RIVAL = 204;
    private static final int X_SPAWN_INDEX_PLAYER_LAB_HOMETOWN = 72;
    private static final int Y_SPAWN_INDEX_PLAYER_LAB_HOMETOWN = 210;
    public static final String ID_RIVAL = "rival";
    private static final int X_INDEX_PORTRAIT_RIVAL = 4;
    private static final int Y_INDEX_PORTRAIT_RIVAL = 5;
    private static final int X_SPAWN_INDEX_RIVAL = 200;
    private static final int Y_SPAWN_INDEX_RIVAL = 12;
    public static final String ID_COIN = "coin";
    private static final int X_INDEX_PORTRAIT_COIN = 7;
    private static final int Y_INDEX_PORTRAIT_COIN = 3;
    private static final int X_SPAWN_INDEX_COIN = 201;
    private static final int Y_SPAWN_INDEX_COIN = 37;
    public static final String ID_RIVAL_LEADER = "rival leader";
    private static final int X_INDEX_PORTRAIT_RIVAL_LEADER = 6;
    private static final int Y_INDEX_PORTRAIT_RIVAL_LEADER = 4;
    private static final int X_SPAWN_INDEX_RIVAL_LEADER = 201;
    private static final int Y_SPAWN_INDEX_RIVAL_LEADER = 15;
    public static final String ID_JR_TRAINER = "jr trainer";
    private static final int X_INDEX_PORTRAIT_JR_TRAINER = 5;
    private static final int Y_INDEX_PORTRAIT_JR_TRAINER = 1;
    private static final int X_SPAWN_INDEX_JR_TRAINER = 201;
    private static final int Y_SPAWN_INDEX_JR_TRAINER = 19;
    public static final String ID_LASS02 = "lass02";
    private static final int X_INDEX_PORTRAIT_LASS_02 = 4;
    private static final int Y_INDEX_PORTRAIT_LASS_02 = 1;
    private static final int X_SPAWN_INDEX_LASS_02 = 200;
    private static final int Y_SPAWN_INDEX_LASS_02 = 22;
    public static final String ID_YOUNGSTER = "youngster";
    private static final int X_INDEX_PORTRAIT_YOUNGSTER = 3;
    private static final int Y_INDEX_PORTRAIT_YOUNGSTER = 1;
    private static final int X_SPAWN_INDEX_YOUNGSTER = 201;
    private static final int Y_SPAWN_INDEX_YOUNGSTER = 25;
    public static final String ID_LASS01 = "lass01";
    private static final int X_INDEX_PORTRAIT_LASS_01 = 4;
    private static final int Y_INDEX_PORTRAIT_LASS_01 = 1;
    private static final int X_SPAWN_INDEX_LASS_01 = 200;
    private static final int Y_SPAWN_INDEX_LASS_01 = 28;
    public static final String ID_BUG_CATCH = "bug catcher";
    private static final int X_INDEX_PORTRAIT_BUG_CATCHER = 2;
    private static final int Y_INDEX_PORTRAIT_BUG_CATCHER = 1;
    private static final int X_SPAWN_INDEX_BUG_CATCHER = 201;
    private static final int Y_SPAWN_INDEX_BUG_CATCHER = 31;
    private static final int X_TRANSFER_POINT_INDEX_LAB_PROF_JAVA = 239;
    private static final int Y_TRANSFER_POINT_INDEX_LAB_PROF_JAVA = 5;
    private static final int X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01 = 65;
    private static final int Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01 = 203;
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

    private Bitmap[][] spritesCharactersBattle;
    private Bitmap[][] sprites;
    private Bitmap spriteCoin;
    private Bitmap spriteTileSolid, spriteTileWalkable, spriteTileBoulder;
    private List<Entity> entities;
    private NonPlayableCharacter npcJrTrainer;
    private NonPlayableCharacter npcLass02;
    private NonPlayableCharacter npcYoungster;
    private NonPlayableCharacter npcLass01;
    private NonPlayableCharacter npcBugCatcher;
    private Paint paintText;

    private WorldScene() {
    }

    public static WorldScene getInstance() {
        if (instance == null) {
            instance = new WorldScene();
        }
        return instance;
    }

    private void initTiles() {
        // [IMAGES]
        Bitmap fullWorldMap = BitmapFactory.decodeResource(resources,
                RES_ID_TILE_COLLISION_BACKGROUND);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);

        // [TILES]
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, fullWorldMap);
        // transfer point: lab prof java
        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_LAB_PROF_JAVA][Y_TRANSFER_POINT_INDEX_LAB_PROF_JAVA];
        tiles[X_TRANSFER_POINT_INDEX_LAB_PROF_JAVA][Y_TRANSFER_POINT_INDEX_LAB_PROF_JAVA] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), LabScene.TAG
        );
        // transfer point: home player room01
        tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01][Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01];
        tiles[X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01][Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM01] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), HomePlayerRoom01Scene.TAG
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

        initTiles();
        initEntities();

        collisionListenerPlayer = generateCollisionListenerForPlayer();
        movementListenerPlayer = generateMovementListenerForPlayer();

        paintText = new Paint();
        paintText.setColor(Color.MAGENTA);
        paintText.setTextSize(20);
        paintText.setStyle(Paint.Style.FILL);
    }

//    private boolean isFirstCollide = true;

    private Entity.CollisionListener generateCollisionListenerForPlayer() {
        return new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                Log.e(TAG, "WorldScene: player's  CollisionListener.onJustCollided(Entity)");

                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals(ID_COIN)) {
                        soundManager.sfxPlay(soundManager.sfxGetItem);
                        player.increaseMovementSpeed();

                        // bounce coin based on player's direction.
                        float xStartCoin = collided.getXPos();
                        float yStartCoin = collided.getYPos();
                        float xEndCoin = xStartCoin;
                        float yEndCoin = yStartCoin;
                        float numberOfTile = 3f;
                        switch (player.getDirection()) {
                            case LEFT:
                                xEndCoin = xStartCoin - (numberOfTile * widthSpriteDst);
                                break;
                            case UP:
                                yEndCoin = yStartCoin - (numberOfTile * heightSpriteDst);
                                break;
                            case RIGHT:
                                xEndCoin = xStartCoin + (numberOfTile * widthSpriteDst);
                                break;
                            case DOWN:
                                yEndCoin = yStartCoin + (numberOfTile * heightSpriteDst);
                                break;
                        }

                        String propertyName = null;
                        float endValue = -1f;
                        if (player.getDirection() == LEFT || player.getDirection() == RIGHT) {
                            propertyName = "xPos";
                            endValue = xEndCoin;
                        } else {
                            propertyName = "yPos";
                            endValue = yEndCoin;
                        }

                        ObjectAnimator animatorPosition = ObjectAnimator.ofFloat(collided,
                                propertyName, endValue);
                        animatorPosition.setInterpolator(new BounceInterpolator());
//                        animatorPosition.setRepeatCount(ValueAnimator.INFINITE);
//                        animatorPosition.setRepeatMode(ValueAnimator.REVERSE);
                        animatorPosition.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        gameListener.onUpdateEntity(collided);
                                    }
                                });
                            }
                        });
                        animatorPosition.setDuration(1000L);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                animatorPosition.start();
                            }
                        });
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
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_RIVAL)) {
//                                soundManager.sfxPlay(soundManager.sfxHorn);
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_RIVAL_LEADER)) {
                        pause();

                        Bitmap portrait = ((NonPlayableCharacter) collided).getPortrait();
                        gameListener.onShowDialogFragment(
                                instantiateRivalLeaderDialogFragment(portrait, gameListener),
                                "RivalLeaderDialogFragment"
                        );
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_BUG_CATCH)) {
//                        gameListener.onChangeScene(HomePlayerRoom01Scene.getInstance());;

//                        joinParty(collided);

                        pause();

                        // Other options: Pocket Critters, Pooh Farmer, Evo, Pong, Frogger
                        String gameTitle = "Bubble Pop";
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
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_LASS01)) {
//                        joinParty(collided);

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
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_YOUNGSTER)) {
//                        joinParty(collided);
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_LASS02)) {
//                        joinParty(collided);
                    } else if (((NonPlayableCharacter) collided).getId().equals(ID_JR_TRAINER)) {
//                        joinParty(collided);
                    }
                }
            }
        };
    }

    private void joinParty(Entity newPartyMember) {
        newPartyMember.resetMovementCommands();

        // first move
        MovementCommand movementCommand = null;
        int currentSizeOfParty = player.getSizeOfPartyMembers();
        if (currentSizeOfParty % 2 == 0) {
            movementCommand = new MoveLeftCommand(newPartyMember, handler);
        } else {
            movementCommand = new MoveRightCommand(newPartyMember, handler);
        }
        newPartyMember.appendMovementCommand(movementCommand);
        // second move
        newPartyMember.appendMovementCommand(new MoveUpCommand(newPartyMember, handler));

        // subsequent moves are same as Player's moves (done through listener).
        player.addPartyMember(((NonPlayableCharacter) newPartyMember));
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
                            } else if (idSceneDestination.equals(HomePlayerRoom01Scene.TAG)) {
                                Log.e(TAG, "transfer point: HomePlayerRoom01Scene");
                                gameListener.onChangeScene(HomePlayerRoom01Scene.getInstance());
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
        // TODO: without this... transferring back to world causes a blank background.
        if (hadBeenTransferred) {
            player.setCollisionListener(collisionListenerPlayer);
            player.setMovementListener(movementListenerPlayer);

            if (idScenePrevious.equals("init")) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE * heightSpriteDst);
            } else if (idScenePrevious.equals(LabScene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_LAB_PROF_JAVA * heightSpriteDst);
            } else if (idScenePrevious.equals(HomePlayerRoom01Scene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM01 * heightSpriteDst);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameListener.onUpdateEntity(player);
                }
            });
            gameCamera.centerOnEntity(player);

            hadBeenTransferred = false;
        }

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

        // ENTITIES are not on SURFACE_VIEW, they are IMAGE_VIEW(s) in the same FRAME_LAYOUT.
        // CONVERSATIONS belonging to ENTITY are different.
        if (npcYoungster.isTalking()) {
            float xYoungster = -1;
            float yYoungster = npcYoungster.getYPos() - gameCamera.getyOffset();
            if (npcYoungster.isTalkLeftSide()) {
                xYoungster = npcYoungster.getXPos() - gameCamera.getxOffset() - widthSpriteDst;
            } else {
                xYoungster = npcYoungster.getXPos() - gameCamera.getxOffset() + widthSpriteDst;
            }
            canvas.drawText("blah blah blah...", xYoungster, yYoungster, paintText);
        }
    }

    @Override
    public List<Object> exit() {
        Log.e(TAG, "exit()");
        hadBeenTransferred = true;

        stopEntityAnimations();

        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.removeImageViewOfEntityFromFrameLayout();
            }
        });

        List<Object> argsSceneTransfer = new ArrayList<>();
        argsSceneTransfer.add(WorldScene.TAG);
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
            if (idScenePrevious.equals("init")) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE * heightSpriteDst);
            } else if (idScenePrevious.equals(LabScene.TAG)) {
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

    private void checkForNewPartyMemberToJoin() {
        int yLastPartyMember = (int) player.getYPos();

        int currentPartySize = player.getSizeOfPartyMembers();
        Entity entityToJoinParty = null;
        if (currentPartySize == 0) {
            int yThresholdBugCatcher = Y_SPAWN_INDEX_BUG_CATCHER * heightSpriteDst;
            if (yLastPartyMember < yThresholdBugCatcher) {
                entityToJoinParty = npcBugCatcher;
                player.setLeaderListener(npcBugCatcher);
            }
        } else {
            NonPlayableCharacter lastPartyMember = player.getLastPartyMember();
            yLastPartyMember = (int) lastPartyMember.getYPos();

            if (currentPartySize == 1) {
                int yThresholdLass01 = Y_SPAWN_INDEX_LASS_01 * heightSpriteDst;
                if (yLastPartyMember < yThresholdLass01) {
                    entityToJoinParty = npcLass01;
                    lastPartyMember.setLeaderListener(npcLass01);
                }
            } else if (currentPartySize == 2) {
                int yThresholdYoungster = Y_SPAWN_INDEX_YOUNGSTER * heightSpriteDst;
                if (yLastPartyMember < yThresholdYoungster) {
                    entityToJoinParty = npcYoungster;
                    lastPartyMember.setLeaderListener(npcYoungster);
                }
            } else if (currentPartySize == 3) {
                int yThresholdLass02 = Y_SPAWN_INDEX_LASS_02 * heightSpriteDst;
                if (yLastPartyMember < yThresholdLass02) {
                    entityToJoinParty = npcLass02;
                    lastPartyMember.setLeaderListener(npcLass02);
                }
            } else if (currentPartySize == 4) {
                int yThresholdJrTrainer = Y_SPAWN_INDEX_JR_TRAINER * heightSpriteDst;
                if (yLastPartyMember < yThresholdJrTrainer) {
                    entityToJoinParty = npcJrTrainer;
                    lastPartyMember.setLeaderListener(npcJrTrainer);
                }
            }
        }

        if (entityToJoinParty != null) {
            joinParty(entityToJoinParty);
            ((NonPlayableCharacter) entityToJoinParty).turnStationaryOff();
        }
    }

    private void updateGameEntities(float xDelta, float yDelta) {
        for (Entity e : entities) {
            if (e instanceof Player) {
//                Player player = (Player) e;
                player.updateViaSensorEvent(handler, xDelta, yDelta);
                validatePosition(player);
                gameCamera.centerOnEntity(player);

                // limit new party member checks (horizontally)
                int xCurrent = (int) player.getXPos();
                if (xCurrent >= (X_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE * widthSpriteDst) &&
                        xCurrent < ((X_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE + 2) * widthSpriteDst)) {
                    // limit new party member checks (vertically)
                    int yCurrent = (int) player.getYPos();
                    if (yCurrent < (Y_SPAWN_INDEX_PLAYER_NUGGET_BRIDGE * widthSpriteDst)) {
                        checkForNewPartyMemberToJoin();
                    }
                }
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

    //////////////////////////////////////////////////////////////////////////////////////////

    private void initEntities() {
        entities = new ArrayList<>();

        Entity.init(widthSpriteDst, heightSpriteDst);

        Entity.CollisionListener collisionListenerNPC = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals(ID_COIN)) {
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
                    Log.e(TAG, "coin hit player!!!");

                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.MovementListener movementListenerNPC = new Entity.MovementListener() {
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

        // [IMAGES]
        spritesCharactersBattle = SpriteInitializer.initSpritesCharactersBattle(resources, widthSpriteDst, heightSpriteDst);
        sprites = SpriteInitializer.initSprites(resources, widthSpriteDst, heightSpriteDst);
        spriteCoin = SpriteInitializer.initCoinSprite(resources);
        spriteTileSolid = SpriteInitializer.initSolidTileSprite(resources);
        spriteTileWalkable = SpriteInitializer.initWalkableTileSprite(resources);
        spriteTileBoulder = SpriteInitializer.initBoulderTileSprite(resources);
        // ENTITIES
        NonPlayableCharacter npcRival = generateNonPlayableCharacter(ID_RIVAL,
                X_INDEX_PORTRAIT_RIVAL, Y_INDEX_PORTRAIT_RIVAL,
                3,
                X_SPAWN_INDEX_RIVAL, Y_SPAWN_INDEX_RIVAL,
                false, DOWN,
                collisionListenerNPC,
                movementListenerNPC);
        NonPlayableCharacter npcCoin = generateNonPlayableCharacter(ID_COIN,
                X_INDEX_PORTRAIT_COIN, Y_INDEX_PORTRAIT_COIN,
                -1,
                X_SPAWN_INDEX_COIN, Y_SPAWN_INDEX_COIN,
                true, DOWN,
                collisionListenerCoin,
                movementListenerNPC);
        NonPlayableCharacter npcRivalLeader = generateNonPlayableCharacter(ID_RIVAL_LEADER,
                X_INDEX_PORTRAIT_RIVAL_LEADER, Y_INDEX_PORTRAIT_RIVAL_LEADER,
                28,
                X_SPAWN_INDEX_RIVAL_LEADER, Y_SPAWN_INDEX_RIVAL_LEADER,
                true, LEFT,
                collisionListenerNPC,
                movementListenerNPC);
        npcJrTrainer = generateNonPlayableCharacter(ID_JR_TRAINER,
                X_INDEX_PORTRAIT_JR_TRAINER, Y_INDEX_PORTRAIT_JR_TRAINER,
                11,
                X_SPAWN_INDEX_JR_TRAINER, Y_SPAWN_INDEX_JR_TRAINER,
                true, LEFT,
                collisionListenerNPC,
                movementListenerNPC);
        npcLass02 = generateNonPlayableCharacter(ID_LASS02,
                X_INDEX_PORTRAIT_LASS_02, Y_INDEX_PORTRAIT_LASS_02,
                17,
                X_SPAWN_INDEX_LASS_02, Y_SPAWN_INDEX_LASS_02,
                true, RIGHT,
                collisionListenerNPC,
                movementListenerNPC);
        npcYoungster = generateNonPlayableCharacter(ID_YOUNGSTER,
                X_INDEX_PORTRAIT_YOUNGSTER, Y_INDEX_PORTRAIT_YOUNGSTER,
                10,
                X_SPAWN_INDEX_YOUNGSTER, Y_SPAWN_INDEX_YOUNGSTER,
                true, LEFT,
                collisionListenerNPC,
                movementListenerNPC);
        npcLass01 = generateNonPlayableCharacter(ID_LASS01,
                X_INDEX_PORTRAIT_LASS_01, Y_INDEX_PORTRAIT_LASS_01,
                17,
                X_SPAWN_INDEX_LASS_01, Y_SPAWN_INDEX_LASS_01,
                true, RIGHT,
                collisionListenerNPC,
                movementListenerNPC);
        npcBugCatcher = generateNonPlayableCharacter(ID_BUG_CATCH,
                X_INDEX_PORTRAIT_BUG_CATCHER, Y_INDEX_PORTRAIT_BUG_CATCHER,
                10,
                X_SPAWN_INDEX_BUG_CATCHER, Y_SPAWN_INDEX_BUG_CATCHER,
                true, LEFT,
                collisionListenerNPC,
                movementListenerNPC);
        player = generatePlayer();

        entities.add(npcRival);
        entities.add(npcCoin);
        entities.add(npcRivalLeader);
        entities.add(npcJrTrainer);
        entities.add(npcLass02);
        entities.add(npcYoungster);
        entities.add(npcLass01);
        entities.add(npcBugCatcher);
        entities.add(player);
    }

    private NonPlayableCharacter generateNonPlayableCharacter(String id,
                                                              int xIndexForPortrait, int yIndexForPortrait,
                                                              int yIndexForSprites,
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
                spritesCharactersBattle[xIndexForPortrait][yIndexForPortrait],
                animationsByDirection,
                directionFacing,
                entityCollisionListener,
                entityMovementListener);

        nonPlayableCharacter.setXPos(xIndexSpawn * widthSpriteDst);
        nonPlayableCharacter.setYPos(yIndexSpawn * heightSpriteDst);
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

    // TODO: bring in Neuromancer.
    /*
    private static void loadWintermute(Resources resources) {
        //LOAD SPRITESHEET
        Bitmap imageSource = BitmapFactory.decodeResource(resources, R.drawable.pc_ms_office_clippit);

        //CHECK IF IMAGE RESOURCE DID not LOAD PROPERLY
        if (imageSource == null) {
            Log.d(MainActivity.DEBUG_TAG, "Assets.loadWintermute(Resources): imageSource == null");

            Log.d(MainActivity.DEBUG_TAG, "BAILING EARLY!!!!!");
            return;
        }

        //IF WE'VE MADE IT THIS FAR, THE IMAGE RESOURCE LOADED AS INTENDED
        Log.d(MainActivity.DEBUG_TAG, "Assets.loadWintermute(Resources): imageSource != null");
        Log.d(MainActivity.DEBUG_TAG, "Assets.loadWintermute(Resources): imageSource.getWidth(), imageSource.getHeight() : " + imageSource.getWidth() + ", " + imageSource.getHeight());
        //UNPACKING THE SPRITESHEET (logic is specific to each spritesheet's layout)
        int column = 22;
        int row = 41;

        wintermute = new Bitmap[row][column];

        //TODO: everything in loadWintermute(Resources) was adapted from initItems(Resources) and has
        //not yet been tailored to this spritesheet (e.g. should NOT have margin at all).
        //
        //Which will also be different for the tiles spritesheet (which has margins, but not between
        //each sprites like the items spritesheet).
        int margin = 0;
        int tileWidth = 124;
        int tileHeight = 93;

        int xCurrent = margin;
        int yCurrent = margin;

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < column; x++) {
                wintermute[y][x] = Bitmap.createBitmap(imageSource, xCurrent, yCurrent, tileWidth, tileHeight);
                xCurrent += (tileWidth + margin);
            }
            xCurrent = margin;
            yCurrent += (tileHeight + margin);
        }
        Log.d(MainActivity.DEBUG_TAG, "Assets.loadWintermute(Resources): FINISHED!!!");
    }
     */

    private TypeWriterDialogFragment typeWriterDialogFragmentRivalLeader = null;

    private TypeWriterDialogFragment instantiateRivalLeaderDialogFragment(Bitmap portrait, Game.GameListener gameListener) {
        String message = resources.getString(R.string.rival_leader_dialogue00);

        typeWriterDialogFragmentRivalLeader =
                TypeWriterDialogFragment.newInstance(50L, portrait, message, new TypeWriterDialogFragment.DismissListener() {
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

    //////////////////////////////////////////////////////////////////////////////////////////

    static class SpriteInitializer {
        //  Battle
        private static final int WIDTH_SPRITE_SHEET_ACTUAL_BATTLE = 520;
        private static final int HEIGHT_SPRITE_SHEET_ACTUAL_BATTLE = 888;
        private static final int COLUMNS_BATTLE = 8;
        private static final int ROWS_BATTLE = 6;
        private static final int X_MARGIN_BATTLE = 8;
        private static final int Y_MARGIN_BATTLE = 8;
        private static final int WIDTH_SPRITE_BATTLE = 56;
        private static final int HEIGHT_SPRITE_BATTLE = 56;

        // Overworld
        private static final int WIDTH_SPRITE_SHEET_ACTUAL_OVERWORLD = 187;
        private static final int HEIGHT_SPRITE_SHEET_ACTUAL_OVERWORLD = 1188;
        private static final int COLUMNS_OVERWORLD = 10;
        private static final int ROWS_OVERWORLD = 56;
        private static final int X_OFFSET_INIT = 9;
        private static final int Y_OFFSET_INIT = 34;
        private static final int WIDTH_SPRITE_OVERWORLD = 16;
        private static final int HEIGHT_SPRITE_OVERWORLD = 16;
        private static final int WIDTH_DIVIDER = 1;
        private static final int HEIGHT_DIVIDER = 1;

        private static final int xSolidTile = 10;
        private static final int ySolidTile = 1147;
        private static final int xWalkableTile = 10;
        private static final int yWalkableTile = 1164;
        private static final int xBoulderTile = 112;
        private static final int yBoulderTile = 1088;

        private static Bitmap[][] initSpritesCharactersBattle(Resources resources, int widthSpriteDst, int heightSpriteDst) {
            Bitmap[][] spritesCharacterBattle = new Bitmap[8][6];

            Bitmap spriteSheetCharacterBattle = BitmapFactory.decodeResource(resources,
                    R.drawable.gbc_pokemon_red_blue_characters_battle);

            float ratioHorizontal = (float) spriteSheetCharacterBattle.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL_BATTLE;
            float ratioVertical = (float) spriteSheetCharacterBattle.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL_BATTLE;

            int widthSpriteConverted = (int) (WIDTH_SPRITE_BATTLE * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE_BATTLE * ratioVertical);

            int yOffset = Y_MARGIN_BATTLE;
            int xOffset = X_MARGIN_BATTLE;
            for (int i = 0; i < COLUMNS_BATTLE; i++) {
                for (int j = 0; j < ROWS_BATTLE; j++) {
                    int xOffsetConverted = (int) (xOffset * ratioHorizontal);
                    int yOffsetConverted = (int) (yOffset * ratioVertical);

                    Bitmap sprite = Bitmap.createBitmap(spriteSheetCharacterBattle,
                            xOffsetConverted, yOffsetConverted, widthSpriteConverted, heightSpriteConverted);
                    spritesCharacterBattle[i][j] = Bitmap.createScaledBitmap(sprite, widthSpriteDst, heightSpriteDst, true);

                    yOffset += (HEIGHT_SPRITE_BATTLE + Y_MARGIN_BATTLE);
                }
                yOffset = Y_MARGIN_BATTLE;
                xOffset += (WIDTH_SPRITE_BATTLE + X_MARGIN_BATTLE);
            }

            return spritesCharacterBattle;
        }

        public static Bitmap[][] initSprites(Resources resources, int widthSpriteDst, int heightSpriteDst) {
            Bitmap[][] sprites = new Bitmap[COLUMNS_OVERWORLD][ROWS_OVERWORLD];

            Bitmap spriteSheet = BitmapFactory.decodeResource(resources,
                    R.drawable.gbc_pokemon_red_blue_characters_overworld);

            float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL_OVERWORLD;
            float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL_OVERWORLD;

            int widthSpriteConverted = (int) (WIDTH_SPRITE_OVERWORLD * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE_OVERWORLD * ratioVertical);

            int yOffset = Y_OFFSET_INIT;
            int xOffset = X_OFFSET_INIT;
            for (int i = 0; i < COLUMNS_OVERWORLD; i++) {
                for (int j = 0; j < ROWS_OVERWORLD; j++) {
                    int xOffsetConverted = (int) (xOffset * ratioHorizontal);
                    int yOffsetConverted = (int) (yOffset * ratioVertical);

                    Bitmap sprite = Bitmap.createBitmap(spriteSheet,
                            xOffsetConverted, yOffsetConverted, widthSpriteConverted, heightSpriteConverted);
                    sprites[i][j] = Bitmap.createScaledBitmap(sprite, widthSpriteDst, heightSpriteDst, true);

                    yOffset += (HEIGHT_SPRITE_OVERWORLD + HEIGHT_DIVIDER);
                }
                yOffset = Y_OFFSET_INIT;
                xOffset += (WIDTH_SPRITE_OVERWORLD + WIDTH_DIVIDER);
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

            float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL_OVERWORLD;
            float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL_OVERWORLD;

            int widthSpriteConverted = (int) (WIDTH_SPRITE_OVERWORLD * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE_OVERWORLD * ratioVertical);

            Bitmap tileWalkable = Bitmap.createBitmap(spriteSheet,
                    (int) (xTile * ratioHorizontal), (int) (yTile * ratioVertical),
                    (int) (widthSpriteConverted - ratioHorizontal), (int) (heightSpriteConverted - ratioVertical));

            return tileWalkable;
        }
    }
}
