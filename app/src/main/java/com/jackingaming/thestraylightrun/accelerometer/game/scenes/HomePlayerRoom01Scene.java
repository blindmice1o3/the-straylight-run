package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.IDEDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.FCVDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.GameConsoleFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.notes.topics.NotesViewerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles.UniqueSolidTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.nextweektonight.NextWeekTonightEpisodesGeneratorFragment;
import com.jackingaming.thestraylightrun.nextweektonight.OnCompletionListenerDTO;
import com.jackingaming.thestraylightrun.nextweektonight.VideoViewFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePlayerRoom01Scene extends Scene {
    public static final String TAG = HomePlayerRoom01Scene.class.getSimpleName();

    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_house_player;
    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.scene_house_player;
    //    private static final int RES_ID_TILE_COLLISION_SOURCE = R.raw.tiles_home_player_01;
//    private static final int RES_ID_TILE_COLLISION_BACKGROUND = R.drawable.indoors_home_and_room;
    private static final int X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM02 = 6;
    private static final int Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM02 = 1;
    private static final int X_SPAWN_INDEX_PLAYER_WORLD = 2;
    private static final int Y_SPAWN_INDEX_PLAYER_WORLD = 6;
    private static final int X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM_02 = X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM02 + 1;
    private static final int Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM_02 = Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM02;
    private static final int X_TRANSFER_POINT_INDEX_WORLD = X_SPAWN_INDEX_PLAYER_WORLD;
    private static final int Y_TRANSFER_POINT_INDEX_WORLD = Y_SPAWN_INDEX_PLAYER_WORLD + 1;
    private static final String VIDEO_RUN_ONE_PART_1 = "vid_20250826_045602759_run_one_part_1a_post_compressed_rotated_90";
    private static final String VIDEO_RUN_ONE_COMMERCIAL = "vid_not_bad_not_bad_burger_2025_06_10_compressed_rotated_90";
    private static final String VIDEO_RUN_ONE_PART_2 = "vid_20251125_141730_run_one_part_2a_post_compressed_rotated_90";
    private static final String VIDEO_RUN_TWO = "vid_20250913_161759550_run_two_post_compressed_rotated_90";
    private static final String VIDEO_RUN_THREE = "vid_20250917_180111542_run_three_post_compressed_rotated_90";
    private static final String VIDEO_RUN_FOUR = "vid_20250918_173333392_run_four_post_compressed_rotated_90";
    private static final String VIDEO_RUN_FIVE = "pxl_20250429_193429506";

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

    // TODO: change to arrays.
    private List<UniqueSolidTile> tilesTelevision = new ArrayList<>();
    private List<UniqueSolidTile> tilesTables = new ArrayList<>();
    private List<UniqueSolidTile> tilesGameConsole = new ArrayList<>();
    private List<UniqueSolidTile> tilesComputer = new ArrayList<>();
    private List<UniqueSolidTile> tilesBed = new ArrayList<>();
    private UniqueSolidTile tileTelevision, tileComputer, tileGameConsole,
            tileTableLeft, tileTableRight, tileBedTop, tileBedBottom;

    private FCVDialogFragment dialogFragment;

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
        Bitmap bitmapHomePlayerRoom01 = bitmapIndoorsHomeAndRoom;
//        Bitmap bitmapHomePlayerRoom01 = Bitmap.createBitmap(bitmapIndoorsHomeAndRoom, 160, 16, 128, 128);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(resources,
                RES_ID_TILE_COLLISION_SOURCE);

        // [TILES]
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, bitmapHomePlayerRoom01, true);
//        // transfer point: world
//        Tile tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD];
//        tiles[X_TRANSFER_POINT_INDEX_WORLD][Y_TRANSFER_POINT_INDEX_WORLD] = new TransferPointTile(
//                tileBeforeBecomingTransferPoint.getImage(), WorldScene.TAG
//        );
//        // transfer point: home player room02
//        tileBeforeBecomingTransferPoint = tiles[X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM_02][Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM_02];
//        tiles[X_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM_02][Y_TRANSFER_POINT_INDEX_HOME_PLAYER_ROOM_02] = new TransferPointTile(
//                tileBeforeBecomingTransferPoint.getImage(), HomePlayerRoom02Scene.TAG
//        );

        widthWorldInTiles = tiles.length;
        heightWorldInTiles = tiles[0].length;
        widthWorldInPixels = widthWorldInTiles * widthSpriteDst;
        heightWorldInPixels = heightWorldInTiles * heightSpriteDst;

        for (Tile[] row : tiles) {
            for (Tile column : row) {
                if (column instanceof UniqueSolidTile) {
                    /////////////////////////////////////////////////////
                    ((UniqueSolidTile) column).setGameCamera(gameCamera);
                    /////////////////////////////////////////////////////

                    if (((UniqueSolidTile) column).getId().equals(UniqueSolidTile.TELEVISION)) {
                        tilesTelevision.add((UniqueSolidTile) column);
//                        tileTelevision = (UniqueSolidTile) column;
                    } else if (((UniqueSolidTile) column).getId().equals(UniqueSolidTile.COMPUTER)) {
                        tilesComputer.add((UniqueSolidTile) column);
//                        tileComputer = (UniqueSolidTile) column;
                    } else if (((UniqueSolidTile) column).getId().equals(UniqueSolidTile.GAME_CONSOLE)) {
                        tilesGameConsole.add((UniqueSolidTile) column);
//                        tileGameConsole = (UniqueSolidTile) column;
                    } else if (((UniqueSolidTile) column).getId().equals(UniqueSolidTile.TABLE)) {
                        tilesTables.add((UniqueSolidTile) column);
//                        if (tileTableLeft == null) {
//                            tileTableLeft = (UniqueSolidTile) column;
//                        } else {
//                            tileTableRight = (UniqueSolidTile) column;
//                        }
                    } else if (((UniqueSolidTile) column).getId().equals(UniqueSolidTile.BED)) {
                        tilesBed.add((UniqueSolidTile) column);
//                        if (tileBedTop == null) {
//                            tileBedTop = (UniqueSolidTile) column;
//                        } else {
//                            tileBedBottom = (UniqueSolidTile) column;
//                        }
                    }
                }
            }
        }
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
//                    if (tiles[xIndex1][yIndex1] instanceof TransferPointTile &&
//                            tiles[xIndex2][yIndex2] instanceof TransferPointTile) {
//                        if (canUseTransferPoint) {
//                            String idSceneDestination = ((TransferPointTile) tiles[xIndex1][yIndex1]).getIdSceneDestination();
//                            if (idSceneDestination.equals(HomePlayerRoom02Scene.TAG)) {
//                                Log.e(TAG, "transfer point: HomePlayerRoom02Scene");
//                                gameListener.onChangeScene(HomePlayerRoom02Scene.getInstance());
//                                return true;
//                            } else if (idSceneDestination.equals(WorldScene.TAG)) {
//                                Log.e(TAG, "transfer point: WorldScene");
//                                gameListener.onChangeScene(WorldScene.getInstance());
//                                return true;
//                            }
//                        }
//                    } else {
//                        Log.e(TAG, "tile is NOT TransferPointTile");
//                    }
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
                        if (id.equals(UniqueSolidTile.TELEVISION)) {
                            Log.e(TAG, "unique solid tile: TELEVISION");
                            if (gameListener.getDailyLoop() != Game.DailyLoop.TELEVISION) {
                                return false;
                            }

                            pause();

                            String videoByRun = null;
                            switch (gameListener.getRun()) {
                                case ONE:
                                    videoByRun = VIDEO_RUN_ONE_PART_1;
                                    break;
                                case TWO:
                                    videoByRun = VIDEO_RUN_TWO;
                                    break;
                                case THREE:
                                    videoByRun = VIDEO_RUN_THREE;
                                    break;
                                case FOUR:
                                    videoByRun = VIDEO_RUN_FOUR;
                                    break;
                                case FIVE:
                                    videoByRun = VIDEO_RUN_FIVE;
                                    break;
                            }

                            boolean showToolbarOnDismiss = false;
                            VideoViewFragment videoViewFragmentPart1 =
//                                    NextWeekTonightEpisodesGeneratorFragment.newInstance(showToolbarOnDismiss);
                                    VideoViewFragment.newInstance(videoByRun, new OnCompletionListenerDTO(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            Log.i(TAG, "finished playback part1!");

                                            if (gameListener.getRun() == Game.Run.ONE) {
                                                Log.i(TAG, "gameListener.getRun() == Game.Run.ONE - showing second video (commercial) and then third video (part2)");

                                                VideoViewFragment videoViewFragmentCommercial = VideoViewFragment.newInstance(VIDEO_RUN_ONE_COMMERCIAL, new OnCompletionListenerDTO(new MediaPlayer.OnCompletionListener() {
                                                    @Override
                                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                                        Log.i(TAG, "finished playback commercial!");

                                                        VideoViewFragment videoViewFragmentPart2 = VideoViewFragment.newInstance(VIDEO_RUN_ONE_PART_2, new OnCompletionListenerDTO(new MediaPlayer.OnCompletionListener() {
                                                            @Override
                                                            public void onCompletion(MediaPlayer mediaPlayer) {
                                                                Log.i(TAG, "finished playback part2!");
                                                            }
                                                        }));

                                                        dialogFragment.replaceFragment(videoViewFragmentPart2);
                                                    }
                                                }));

                                                dialogFragment.replaceFragment(videoViewFragmentCommercial);
                                            } else {
                                                Log.i(TAG, "gameListener.getRun() != Game.Run.ONE - doing nothing.");
                                            }
                                        }
                                    }));
                            String tag = NextWeekTonightEpisodesGeneratorFragment.TAG;
                            boolean canceledOnTouchOutside = true;
                            dialogFragment =
                                    FCVDialogFragment.newInstance(videoViewFragmentPart1, tag,
                                            canceledOnTouchOutside, FCVDialogFragment.DEFAULT_WIDTH_IN_DECIMAL, FCVDialogFragment.DEFAULT_HEIGHT_IN_DECIMAL,
                                            new FCVDialogFragment.LifecycleListener() {
                                                @Override
                                                public void onResume() {
                                                    // Intentionally blank.
                                                }

                                                @Override
                                                public void onDismiss() {
                                                    //////////////////////////////////
                                                    gameListener.incrementDailyLoop();
                                                    //////////////////////////////////

                                                    unpause();
                                                }
                                            });

                            gameListener.onShowDialogFragment(
                                    dialogFragment, tag
                            );

                            return false;
                        } else if (id.equals(UniqueSolidTile.COMPUTER)) {
                            Log.e(TAG, "unique solid tile: COMPUTER");
                            if (gameListener.getDailyLoop() != Game.DailyLoop.COMPUTER) {
                                return false;
                            }

                            pause();

                            DialogFragment dialogFragment = IDEDialogFragment.newInstance(
                                    new IDEDialogFragment.ButtonListener() {
                                        @Override
                                        public void onCloseButtonClicked(View view, IDEDialogFragment ideDialogFragment) {
                                            ideDialogFragment.dismiss();
                                        }
                                    }, new IDEDialogFragment.DismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            Log.e(TAG, "onDismiss()");

                                            //////////////////////////////////
                                            gameListener.incrementDailyLoop();
                                            //////////////////////////////////

                                            unpause();
                                        }
                                    }, IDEDialogFragment.Mode.LONG_PRESS_REVEALS,
                                    gameListener.getRun());

                            gameListener.onShowDialogFragment(dialogFragment, IDEDialogFragment.TAG);

                            return false;
                        } else if (id.equals(UniqueSolidTile.GAME_CONSOLE)) {
                            Log.e(TAG, "unique solid tile: GAME CONSOLE");
                            if (gameListener.getDailyLoop() != Game.DailyLoop.GAME_CONSOLE) {
                                return false;
                            }

                            pause();

                            // Other options: Pocket Critters, Pooh Farmer, Evo, Pong, Frogger
                            String gameTitle = "Pooh Farmer";
                            Fragment fragment = GameConsoleFragment.newInstance(gameTitle,
                                    gameListener.getRun());
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
                                                    //////////////////////////////////
                                                    gameListener.incrementDailyLoop();
                                                    //////////////////////////////////

                                                    unpause();
                                                }
                                            });

                            gameListener.onShowDialogFragment(
                                    dialogFragment, tag
                            );

                            return false;
                        } else if (id.equals(UniqueSolidTile.TABLE)) {
                            Log.e(TAG, "unique solid tile: TABLE");
                            if (gameListener.getDailyLoop() != Game.DailyLoop.NOTES_TOPIC) {
                                return false;
                            }

                            pause();

                            NotesViewerFragment.NoteType noteType =
                                    (gameListener.getDailyLoop() == Game.DailyLoop.NOTES_TOPIC) ?
                                            NotesViewerFragment.NoteType.TOPICS :
                                            NotesViewerFragment.NoteType.LEARNERS;

                            Fragment fragment = NotesViewerFragment.newInstance(noteType,
                                    gameListener.getRun());
                            String tag = NotesViewerFragment.TAG;
                            boolean canceledOnTouchOutside = false;
                            DialogFragment dialogFragment = FCVDialogFragment.newInstance(fragment, tag,
                                    canceledOnTouchOutside, 1.0f, 0.7f,
                                    new FCVDialogFragment.LifecycleListener() {
                                        @Override
                                        public void onResume() {
                                            // Intentionally blank.
                                        }

                                        @Override
                                        public void onDismiss() {
                                            //////////////////////////////////
                                            gameListener.incrementDailyLoop();
                                            //////////////////////////////////

                                            unpause();
                                        }
                                    });

                            gameListener.onShowDialogFragment(
                                    dialogFragment, tag
                            );

                            return false;
                        } else if (id.equals(UniqueSolidTile.BED)) {
                            Log.e(TAG, "unique solid tile: BED");
                            if (gameListener.getDailyLoop() != Game.DailyLoop.SLEEP_SAVE) {
                                return false;
                            }

                            //////////////////////////////////
                            gameListener.incrementDailyLoop();
                            //////////////////////////////////
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

    void highlightTelevisionTile() {
        if (!tilesTelevision.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileTelevision : tilesTelevision) {
                tileTelevision.startCirleAnimation();
            }
        }
//        if (!tileTelevision.isAnimationRunning()) {
//            tileTelevision.startCirleAnimation();
//        }
    }

    void highlightTableTile() {
        if (!tilesTables.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileTable : tilesTables) {
                tileTable.startCirleAnimation();
            }
        }
//        if (!tileTableLeft.isAnimationRunning()) {
//            tileTableLeft.startCirleAnimation();
//        }
//        if (!tileTableRight.isAnimationRunning()) {
//            tileTableRight.startCirleAnimation();
//        }
    }

    void highlightGroupChatDrawer() {
        if (isGroupChatDrawerClosed) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    isGroupChatDrawerClosed = false;
                    gameListener.highlightGroupChatDrawer();
                }
            });
        }
    }

    void highlightGameConsoleTile() {
        if (!tilesGameConsole.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileGameConsole : tilesGameConsole) {
                tileGameConsole.startCirleAnimation();
            }
        }
//        if (!tileGameConsole.isAnimationRunning()) {
//            tileGameConsole.startCirleAnimation();
//        }
    }

    void highlightComputerTile() {
        if (!tilesComputer.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileComputer : tilesComputer) {
                tileComputer.startCirleAnimation();
            }
        }
//        if (!tileComputer.isAnimationRunning()) {
//            tileComputer.startCirleAnimation();
//        }
    }

    void highlightJournalDrawer() {
        if (isJournalDrawerClosed) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    isJournalDrawerClosed = false;
                    gameListener.highlightJournalDrawer();
                }
            });
        }
    }

    void highlightBedTile() {
        if (!tilesBed.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileBed : tilesBed) {
                tileBed.startCirleAnimation();
            }
        }
//        if (!tileBedTop.isAnimationRunning()) {
//            tileBedTop.startCirleAnimation();
//        }
//        if (!tileBedBottom.isAnimationRunning()) {
//            tileBedBottom.startCirleAnimation();
//        }
    }

    void unhighlightTelevisionTile() {
        if (tilesTelevision.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileTelevision : tilesTelevision) {
                tileTelevision.stopCirleAnimation();
            }
        }
//        if (tileTelevision.isAnimationRunning()) {
//            tileTelevision.stopCirleAnimation();
//        }
    }

    void unhighlightTableTile() {
        if (tilesTables.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileTable : tilesTables) {
                tileTable.stopCirleAnimation();
            }
        }
//        if (tileTableLeft.isAnimationRunning()) {
//            tileTableLeft.stopCirleAnimation();
//        }
//        if (tileTableRight.isAnimationRunning()) {
//            tileTableRight.stopCirleAnimation();
//        }
    }

    void unhighlightGroupChatDrawer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.unhighlightGroupChatDrawer();
            }
        });
    }

    void unhighlightGameConsoleTile() {
        if (tilesGameConsole.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileGameConsole : tilesGameConsole) {
                tileGameConsole.stopCirleAnimation();
            }
        }
//        if (tileGameConsole.isAnimationRunning()) {
//            tileGameConsole.stopCirleAnimation();
//        }
    }

    void unhighlightComputerTile() {
        if (tilesComputer.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileComputer : tilesComputer) {
                tileComputer.stopCirleAnimation();
            }
        }
//        if (tileComputer.isAnimationRunning()) {
//            tileComputer.stopCirleAnimation();
//        }
    }

    void unhighlightJournalDrawer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameListener.unhighlightJournalDrawer();
            }
        });
    }

    void unhighlightBedTile() {
        if (tilesBed.get(0).isAnimationRunning()) {
            for (UniqueSolidTile tileBed : tilesBed) {
                tileBed.stopCirleAnimation();
            }
        }
//        if (tileBedTop.isAnimationRunning()) {
//            tileBedTop.stopCirleAnimation();
//        }
//        if (tileBedBottom.isAnimationRunning()) {
//            tileBedBottom.stopCirleAnimation();
//        }
    }

    private boolean isGroupChatDrawerClosed = true;
    private boolean isJournalDrawerClosed = true;

    @Override
    public void update(long elapsed) {
        if (paused) {
            return;
        }

        // TODO: hyperinefficient approach,
        //  but fast way to see if it works (must re-do [later]).
        if (gameListener.getDailyLoop() == Game.DailyLoop.TELEVISION) {
            highlightTelevisionTile();
        } else {
            unhighlightTelevisionTile();
        }

        if (gameListener.getDailyLoop() == Game.DailyLoop.NOTES_TOPIC) {
            highlightTableTile();
        } else {
            unhighlightTableTile();
        }

        if (gameListener.getDailyLoop() == Game.DailyLoop.GROUP_CHAT) {
            highlightGroupChatDrawer();
        } else {
            unhighlightGroupChatDrawer();
        }

        if (gameListener.getDailyLoop() == Game.DailyLoop.COMPUTER) {
            highlightComputerTile();
        } else {
            unhighlightComputerTile();
        }

        if (gameListener.getDailyLoop() == Game.DailyLoop.GAME_CONSOLE) {
            highlightGameConsoleTile();
        } else {
            unhighlightGameConsoleTile();
        }

        if (gameListener.getDailyLoop() == Game.DailyLoop.SLEEP_SAVE) {
            highlightBedTile();
        } else {
            unhighlightBedTile();
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
        argsSceneTransfer.add(HomePlayerRoom01Scene.TAG);
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
            if (idScenePrevious.equals(HomePlayerRoom02Scene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM02 * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_HOME_PLAYER_ROOM02 * heightSpriteDst);
            } else if (idScenePrevious.equals(WorldScene.TAG)) {
                player.setXPos(X_SPAWN_INDEX_PLAYER_WORLD * widthSpriteDst);
                player.setYPos(Y_SPAWN_INDEX_PLAYER_WORLD * heightSpriteDst);
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

    public void setGroupChatDrawerClosed(boolean groupChatDrawerClosed) {
        isGroupChatDrawerClosed = groupChatDrawerClosed;
    }

    public void setJournalDrawerClosed(boolean journalDrawerClosed) {
        isJournalDrawerClosed = journalDrawerClosed;
    }
}
