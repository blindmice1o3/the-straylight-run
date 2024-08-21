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

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs.EditTextDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TransferPointTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabScene extends Scene {
    public static final String TAG = LabScene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_lab;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.indoors_home_and_room;
    private static final int X_SPAWN_INDEX_PLAYER = 4;
    private static final int Y_SPAWN_INDEX_PLAYER = 10;
    public static final String ID_SCIENTIST = "scientist";
    private static final int X_INDEX_PORTRAIT_SCIENTIST = 0;
    private static final int Y_INDEX_PORTRAIT_SCIENTIST = 1;
    private static final int X_SPAWN_INDEX_SCIENTIST = 4;
    private static final int Y_SPAWN_INDEX_SCIENTIST = 3;
    private static final int X_TRANSFER_POINT_INDEX_WORLD = X_SPAWN_INDEX_PLAYER;
    private static final int Y_TRANSFER_POINT_INDEX_WORLD = Y_SPAWN_INDEX_PLAYER + 1;
    public static int durationOfFrameInMilli = 420;

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

    private Bitmap[][] spritesCharactersBattle;
    private Bitmap[][] sprites;
    private List<Entity> entities;

    private LabScene() {
    }

    public static LabScene getInstance() {
        if (instance == null) {
            instance = new LabScene();
        }
        return instance;
    }

    private void initTiles() {
        // [IMAGES]
        Bitmap bitmapIndoorsHomeAndRoom = BitmapFactory.decodeResource(resources,
                RES_ID_TILE_COLLISION_BACKGROUND);
        Bitmap bitmapLab = Bitmap.createBitmap(bitmapIndoorsHomeAndRoom, 23, 544, 160, 192);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);

        // [TILES]
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, bitmapLab);
        // transfer point: world
        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD];
        tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD] = new TransferPointTile(
                tileBeforeBecomingTransferPoint.getImage(), WorldScene.TAG
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
                Log.e(TAG, "LabScene: player's  CollisionListener.onJustCollided(Entity)");

                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals(ID_SCIENTIST)) {
                        pause();

                        Bitmap portrait = ((NonPlayableCharacter) collided).getPortrait();
                        startDialogueWithScientist(portrait);
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
                            if (idSceneDestination.equals(WorldScene.TAG)) {
                                if (dismissCompletedDialog04) {
                                    Log.e(TAG, "transfer point: HOME PLAYER ROOM 02");
                                    gameListener.onChangeScene(HomePlayerRoom02Scene.getInstance());
                                } else {
                                    Log.e(TAG, "transfer point: WORLD");
                                    gameListener.onChangeScene(WorldScene.getInstance());
                                }

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
        argsSceneTransfer.add(LabScene.TAG);
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
            if (idScenePrevious.equals(WorldScene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER * heightSpriteDst);
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

        Entity.CollisionListener collisionListenerNPC = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof Player) {
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
        // ENTITIES
        NonPlayableCharacter npcScientist = generateNonPlayableCharacter(ID_SCIENTIST,
                X_INDEX_PORTRAIT_SCIENTIST, Y_INDEX_PORTRAIT_SCIENTIST,
                4,
                X_SPAWN_INDEX_SCIENTIST, Y_SPAWN_INDEX_SCIENTIST,
                true, DOWN,
                collisionListenerNPC,
                movementListenerNPC);

        entities.add(npcScientist);
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

    private void onDismissScientistDialogue04(Bitmap portraitScientist) {
        String messageScientistDialogue05 = resources.getString(R.string.scientist_dialogue05);

        TypeWriterDialogFragment dialogFragmentScientistDialogue05 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue05,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");

                                if (completedDialogue04) {
                                    // Used in exit() (determines: xBeforeTransfer or X_SPAWN_HOMETOWN)
                                    dismissCompletedDialog04 = true;
                                }

                                unpause();
                            }
                        },
                        new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                                completedDialogue04 = true;
                            }
                        }
                );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue05,
                "ScientistDialogue05"
        );
    }

    private void onDismissScientistDialogue03(Bitmap portraitScientist) {
        String messageTemplate04 = resources.getString(R.string.scientist_dialogue04_template);
        String messageScientistDialogue04 = String.format(messageTemplate04, nameUser);

        dialogFragmentScientistDialogue04 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue04,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                                onDismissScientistDialogue04(portraitScientist);
                            }
                        },
                        new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue04,
                "ScientistDialogue04"
        );
    }

    private void onEnterKeyPressedScientistInput01(Bitmap portraitScientist, String nameRival) {
        pause();

        String messageTemplate03 = resources.getString(R.string.scientist_dialogue03_template);
        String messageScientistDialogue03 = String.format(messageTemplate03, nameRival);

        TypeWriterDialogFragment dialogFragmentScientistDialogue03 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue03,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                                completedDialogue02 = true;

                                onDismissScientistDialogue03(portraitScientist);
                            }
                        }, new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue03,
                "ScientistDialogue03"
        );
    }

    private void onAnimationFinishScientistDialogue02(TypeWriterDialogFragment typeWriterDialogFragmentScientistDialogue02,
                                                      Bitmap portraitScientist) {
        EditTextDialogFragment dialogFragmentScientistInput01 = EditTextDialogFragment.newInstance(
                new EditTextDialogFragment.EnterListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "onDismiss()");
                        typeWriterDialogFragmentScientistDialogue02.dismiss();
                    }

                    @Override
                    public void onEnterKeyPressed(String name) {
                        Log.e(TAG, "onEnterKeyPressed()");
                        nameRival = name;
                        onEnterKeyPressedScientistInput01(portraitScientist, name);
                    }
                }
        );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistInput01,
                "ScientistInput00"
        );
    }

    private void onDismissScientistDialogue01(Bitmap portraitScientist) {
        String messageScientistDialogue02 = resources.getString(R.string.scientist_dialogue02);

        dialogFragmentScientistDialogue02 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue02,
                        new TypeWriterDialogFragment.DismissListener() {
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
                                onAnimationFinishScientistDialogue02(
                                        dialogFragmentScientistDialogue02,
                                        portraitScientist
                                );
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue02,
                "ScientistDialogue02"
        );
    }

    private void onEnterKeyPressedScientistInput00(Bitmap portraitScientist, String nameUser) {
        pause();

        String messageTemplate01 = resources.getString(R.string.scientist_dialogue01_template);
        String messageScientistDialogue01 = String.format(messageTemplate01, nameUser);

        TypeWriterDialogFragment dialogFragmentScientistDialogue01 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue01,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                                completedDialogue00 = true;

                                onDismissScientistDialogue01(portraitScientist);
                            }
                        }, new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue01,
                "ScientistDialogue01"
        );
    }

    private void onAnimationFinishScientistDialogue00(TypeWriterDialogFragment typeWriterDialogFragmentScientistDialogue00,
                                                      Bitmap portraitScientist) {
        EditTextDialogFragment dialogFragmentScientistInput00 = EditTextDialogFragment.newInstance(
                new EditTextDialogFragment.EnterListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "onDismiss()");
                        typeWriterDialogFragmentScientistDialogue00.dismiss();
                    }

                    @Override
                    public void onEnterKeyPressed(String name) {
                        Log.e(TAG, "onEnterKeyPressed()");
                        nameUser = name;
                        onEnterKeyPressedScientistInput00(portraitScientist, name);
                    }
                }
        );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistInput00,
                "ScientistInput00"
        );
    }

    private void onShowScientistDialogue00(Bitmap portraitScientist) {
        String messageScientistDialogue00 = resources.getString(R.string.scientist_dialogue00);

        dialogFragmentScientistDialogue00 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue00,
                        new TypeWriterDialogFragment.DismissListener() {
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
                                onAnimationFinishScientistDialogue00(
                                        dialogFragmentScientistDialogue00,
                                        portraitScientist
                                );
                            }
                        }
                );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue00,
                "ScientistDialogue00"
        );
    }

    private String nameRival;
    private String nameUser;
    TypeWriterDialogFragment dialogFragmentScientistDialogue00;
    TypeWriterDialogFragment dialogFragmentScientistDialogue02;
    TypeWriterDialogFragment dialogFragmentScientistDialogue04;
    private boolean completedDialogue00 = false;
    private boolean completedDialogue02 = false;
    private boolean completedDialogue04 = false;
    private boolean dismissCompletedDialog04 = false;

    // 00 (START)
    private void startDialogueWithScientist(Bitmap portraitScientist) {
        if (completedDialogue02) {
            onDismissScientistDialogue03(portraitScientist);
        } else if (completedDialogue00) {
            onDismissScientistDialogue01(portraitScientist);
        } else {
            onShowScientistDialogue00(portraitScientist);
        }
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
    }
}
