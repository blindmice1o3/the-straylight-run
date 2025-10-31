package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.RobotDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.TileSelectorDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.IDEDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TileSelectorView;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.RobotAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.FaceDownCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.FaceLeftCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.FaceRightCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.FaceUpCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.WalkDownCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.WalkLeftCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.WalkRightCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement.WalkUpCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.HarvestGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.PlaceInShippingBinTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.WaterGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.TileWorkRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Robot extends Creature {
    public static final String TAG = Robot.class.getSimpleName();
    private static final int COUNTER_COMMANDS_TARGET = 25;
    private static final long DEFAULT_MOVEMENT_DURATION = 1000L;
    private static final long RUNNING_MOVEMENT_DURATION = 500L;
    private static final int X_INDEX_SHIPPING_BIN_DROP_TILE_TL = 7;
    private static final int Y_INDEX_SHIPPING_BIN_DROP_TILE_TL = 8;
    private static final int X_INDEX_SHIPPING_BIN_DROP_TILE_TR = 8;
    private static final int Y_INDEX_SHIPPING_BIN_DROP_TILE_TR = 8;
    private static final int X_INDEX_SHIPPING_BIN_DROP_TILE_BL = 7;
    private static final int Y_INDEX_SHIPPING_BIN_DROP_TILE_BL = 9;
    private static final int X_INDEX_SHIPPING_BIN_DROP_TILE_BR = 8;
    private static final int Y_INDEX_SHIPPING_BIN_DROP_TILE_BR = 9;

    public interface DialogListener {
        void onOpenIDEDialogFragment();
    }

    private DialogListener listener;

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    public enum State {OFF, WALK, RUN, TILE_SELECTED;}

    private RobotAnimationManager robotAnimationManager;
    transient private ObjectAnimator movementAnimator;

    private State state;
    private Random random;
    private Command walkLeftCommand, walkUpCommand, walkRightCommand, walkDownCommand,
            faceLeftCommand, faceUpCommand, faceRightCommand, faceDownCommand,
            tillTileCommand, seedTileCommand, waterTileCommand,
            harvestTileCommand, placeInShippingBinTileCommand;

    private List<Command> commands;
    //    private int counterCommands = 0;
    private List<Tile> tilesShippingBinDrop;

    private boolean isFirstTimeShowingRobotDialogFragment = true;

    public Robot(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        direction = Direction.DOWN;
        robotAnimationManager = new RobotAnimationManager();

        walkLeftCommand = new WalkLeftCommand(this);
        walkUpCommand = new WalkUpCommand(this);
        walkRightCommand = new WalkRightCommand(this);
        walkDownCommand = new WalkDownCommand(this);
        faceLeftCommand = new FaceLeftCommand(this);
        faceUpCommand = new FaceUpCommand(this);
        faceRightCommand = new FaceRightCommand(this);
        faceDownCommand = new FaceDownCommand(this);
        tillTileCommand = new TillGrowableTileCommand(null);
        seedTileCommand = new SeedGrowableTileCommand(null, MysterySeed.TAG);
        waterTileCommand = new WaterGrowableTileCommand(null);
        harvestTileCommand = new HarvestGrowableTileCommand(null, this);
        placeInShippingBinTileCommand = new PlaceInShippingBinTileCommand(null, this);
        commands = new ArrayList<>();

        state = State.OFF;
        random = new Random();
    }

    public void checkIfFirstTimeShowingRobotDialogFragment() {
        if (isFirstTimeShowingRobotDialogFragment) {
            Log.d(TAG, "isFirstTimeShowingRobotDialogFragment");
            isFirstTimeShowingRobotDialogFragment = false;

            game.getViewportListener().addAndShowParticleExplosionView();

            Bitmap portrait = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.dialogue_image_nwt_host);
            String text = game.getContext().getResources().getString(R.string.use_robot_reprogrammer_4000);
            TypeWriterDialogFragment typeWriterDialogFragment = TypeWriterDialogFragment.newInstance(
                    50L, portrait, text,
                    new TypeWriterDialogFragment.DismissListener() {
                        @Override
                        public void onDismiss() {
                            Log.e(TAG, "TextboxListener.showTextbox( TypeWriterDialogFragment.onDismiss() )");
                        }
                    }, new TypeWriterTextView.TextCompletionListener() {
                        @Override
                        public void onAnimationFinish() {
                            Log.e(TAG, "TextboxListener.showTextbox( TypeWriterDialogFragment.onAnimationFinish() )");
                        }
                    }
            );

            SceneFarm.getInstance().setInTutorialUseRobotReprogrammer4000(
                    true
            );
            game.getTextboxListener().showTextbox(typeWriterDialogFragment);
        } else {
            Log.d(TAG, "!isFirstTimeShowingRobotDialogFragment");

            // do nothing;
        }
    }

    public void showRobotDialogFragment() {
        game.setPaused(true);

        // used for RunOne's "tutorial"
        checkIfFirstTimeShowingRobotDialogFragment();

        RobotDialogFragment robotDialogFragment = instantiateRobotDialogFragment();
        robotDialogFragment.show(
                ((MainActivity) game.getContext()).getSupportFragmentManager(),
                RobotDialogFragment.TAG
        );
    }

    @Override
    public void init(Game game) {
        super.init(game);

        robotAnimationManager.init(game);

        movementAnimator =
                ObjectAnimator.ofFloat(this, "x", x - Tile.WIDTH);
        movementAnimator.setDuration(RUNNING_MOVEMENT_DURATION);
        movementAnimator.setInterpolator(new LinearInterpolator());
        movementAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "movementAnimator.onAnimationEnd()");

                doNextCommand();
            }
        });

        TileManager tileManager = game.getSceneManager().getCurrentScene().getTileManager();
        Tile[][] tilesScene = tileManager.getTiles();
        Tile tileShippingBinTL = tilesScene[Y_INDEX_SHIPPING_BIN_DROP_TILE_TL][X_INDEX_SHIPPING_BIN_DROP_TILE_TL];
        Tile tileShippingBinTR = tilesScene[Y_INDEX_SHIPPING_BIN_DROP_TILE_TR][X_INDEX_SHIPPING_BIN_DROP_TILE_TR];
        Tile tileShippingBinBL = tilesScene[Y_INDEX_SHIPPING_BIN_DROP_TILE_BL][X_INDEX_SHIPPING_BIN_DROP_TILE_BL];
        Tile tileShippingBinBR = tilesScene[Y_INDEX_SHIPPING_BIN_DROP_TILE_BR][X_INDEX_SHIPPING_BIN_DROP_TILE_BR];

        tilesShippingBinDrop = new ArrayList<>();
        tilesShippingBinDrop.add(tileShippingBinTL);
        tilesShippingBinDrop.add(tileShippingBinTR);
        tilesShippingBinDrop.add(tileShippingBinBL);
        tilesShippingBinDrop.add(tileShippingBinBR);
    }

    @Override
    public void update(long elapsed) {
        robotAnimationManager.update(elapsed);

        switch (state) {
            case OFF:
                break;
            case WALK:
            case RUN:
                if (!movementAnimator.isRunning()) {
                    determineNextMove();
                    move();
                }
                break;
            case TILE_SELECTED:
                // Intentionally blank.
                break;
        }

        determineNextImage();

        // CARRYABLE
        if (carryable != null) {
            moveCarryable();
        }
    }

    private String propertyName = null;
    private float valueStart = 0;
    private float valueEnd = 0;

    public void prepareMoveDown() {
        yMove = (1 * Tile.HEIGHT);
        propertyName = "y";
        valueStart = y;
        valueEnd = y + yMove;
    }

    public void prepareMoveLeft() {
        xMove = -(1 * Tile.WIDTH);
        propertyName = "x";
        valueStart = x;
        valueEnd = x + xMove;
    }

    public void prepareMoveUp() {
        yMove = -(1 * Tile.HEIGHT);
        propertyName = "y";
        valueStart = y;
        valueEnd = y + yMove;
    }

    public void prepareMoveRight() {
        xMove = (1 * Tile.WIDTH);
        propertyName = "x";
        valueStart = x;
        valueEnd = x + xMove;
    }

    @Override
    public boolean checkItemCollision(float xOffset, float yOffset, boolean viaClick) {
        return false;
    }

    @Override
    public boolean checkEntityCollision(float xOffset, float yOffset) {
        return false;
    }

    @Override
    public boolean performMove() {
        boolean collision = false;
        if (propertyName.equals("x")) {
            collision = checkEntityCollision(xMove, 0);
        } else if (propertyName.equals("y")) {
            collision = checkEntityCollision(0, yMove);
        }

        if (!collision) {
            movementAnimator.setPropertyName(propertyName);
            movementAnimator.setFloatValues(valueStart, valueEnd);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    movementAnimator.start();
                }
            });

            return true;
        }

        return false;
    }

    private void determineNextMove() {
        if (state == State.OFF) {
            direction = Direction.DOWN;
        } else {
            // change direction 10% of time.
            if (random.nextInt(10) == 1) {
                // randomly determine direction.
                int moveDir = random.nextInt(4);

                // DOWN
                if (moveDir == 0) {
                    direction = Direction.DOWN;
                    prepareMoveDown();
                }
                // LEFT
                else if (moveDir == 1) {
                    direction = Direction.LEFT;
                    prepareMoveLeft();
                }
                // UP
                else if (moveDir == 2) {
                    direction = Direction.UP;
                    prepareMoveUp();
                }
                // RIGHT
                else if (moveDir == 3) {
                    direction = Direction.RIGHT;
                    prepareMoveRight();
                } else {
                    Log.e(TAG, "determineNextMove() else-clause moveDir.");
                }
            }
            // do NOT change direction 90% of time.
            else {
                switch (direction) {
                    case UP:
                        prepareMoveUp();
                        break;
                    case DOWN:
                        prepareMoveDown();
                        break;
                    case LEFT:
                        prepareMoveLeft();
                        break;
                    case RIGHT:
                        prepareMoveRight();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void determineNextImage() {
        image = robotAnimationManager.getCurrentFrame(direction, state, xMove, yMove);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        if (e instanceof Plant || e instanceof CollidingOrbit) {
            return false;
        }
        return true;
    }

    @Override
    public boolean respondToItemCollisionViaClick(Item item) {
        return false;
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }

    public void changeToOff() {
        state = State.OFF;
        commands.clear();
        tileWorkRequests.clear();
    }

    public void changeToWalk() {
        state = State.WALK;
        movementAnimator.setDuration(DEFAULT_MOVEMENT_DURATION);
    }

    public void changeToRun() {
        state = State.RUN;
        movementAnimator.setDuration(RUNNING_MOVEMENT_DURATION);
    }

    public RobotDialogFragment instantiateRobotDialogFragment() {
        RobotDialogFragment robotDialogFragment = RobotDialogFragment.newInstance(new RobotDialogFragment.ButtonListener() {
            @Override
            public void onOffButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                Log.e(TAG, "OFF");
                changeToOff();
                game.setPaused(false);
            }

            @Override
            public void onWalkButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                Log.e(TAG, "WALK");
                changeToWalk();
                game.setPaused(false);
            }

            @Override
            public void onRunButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                Log.e(TAG, "RUN");
                changeToRun();
                game.setPaused(false);
            }

            @Override
            public void onTillSeedWaterButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                showSelectionsOfRobotDialogFragment(
                        TileSelectorView.Mode.TILL_SEED_WATER,
                        robotDialogFragment
                );
            }

            @Override
            public void onWaterButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                showSelectionsOfRobotDialogFragment(
                        TileSelectorView.Mode.ONLY_WATER,
                        robotDialogFragment
                );
            }

            @Override
            public void onHarvestButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                showSelectionsOfRobotDialogFragment(
                        TileSelectorView.Mode.HARVEST,
                        robotDialogFragment
                );
            }

            @Override
            public void onIDEButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                showIDEDialogFragment(robotDialogFragment);
            }
        }, new RobotDialogFragment.DismissListener() {
            @Override
            public void onDismiss() {
                game.setPaused(false);
            }
        });

        return robotDialogFragment;
    }

    private void showIDEDialogFragment(RobotDialogFragment robotDialogFragment) {
        IDEDialogFragment ideDialogFragment = IDEDialogFragment.newInstance(new IDEDialogFragment.ButtonListener() {
            @Override
            public void onCloseButtonClicked(View view, IDEDialogFragment ideDialogFragment) {
                ideDialogFragment.dismiss();
            }
        }, new IDEDialogFragment.DismissListener() {
            @Override
            public void onDismiss() {
                Log.e(TAG, "onDismiss()");
            }
        }, IDEDialogFragment.Mode.KEYBOARD_TRAINER, com.jackingaming.thestraylightrun.accelerometer.game.Game.Run.ONE);

        if (listener != null) {
            listener.onOpenIDEDialogFragment();
        }

        ideDialogFragment.show(
                ((MainActivity) game.getContext()).getSupportFragmentManager(),
                IDEDialogFragment.TAG
        );
    }

    private void doNextCommand() {
        Log.e(TAG, "doNextCommand" +
                "()");
        if (!commands.isEmpty()) {
            Log.e(TAG, "!commands.isEmpty()");
//            counterCommands++;
//            if (counterCommands == COUNTER_COMMANDS_TARGET) {
//                counterCommands = 0;

            Command command = commands.get(0);
            Log.e(TAG, "command: " + command.getClass());
            if (command instanceof TileCommand) {
                Tile tileCurrentlyFacing = checkTileCurrentlyFacing();

                ((TileCommand) command).setTile(tileCurrentlyFacing);
            }

            boolean success = command.execute();
            if (success) {
                Log.e(TAG, "command successfully executed... removing command from front of queue. command: " + command.getClass().getSimpleName());
                commands.remove(command);

                if (command instanceof HarvestGrowableTileCommand) {
                    List<Command> pathToShippingBinAsCommands =
                            handleFindShortestPathToShippingBin();
                    commands.addAll(pathToShippingBinAsCommands);

                    commands.add(placeInShippingBinTileCommand);
                } else if (command instanceof PlaceInShippingBinTileCommand) {
                    List<Command> pathFromShippingBinAsCommands =
                            handleFindPathBackFromShippingBin();
                    commands.addAll(pathFromShippingBinAsCommands);
                }

                if (commands.isEmpty()) {
                    tileWorkRequests.remove(0);
                }

                if (command instanceof WalkRightCommand || command instanceof WalkDownCommand || command instanceof WalkLeftCommand || command instanceof WalkUpCommand) {
                    // do nothing.
                } else {
                    doNextCommand();
                }
            } else {
                Log.e(TAG, "command NOT successfully executed... keep queue the same. command: " + command.getClass().getSimpleName());
                // TODO: 2025_10_23. do nothing. (maybe).
//                            if (command instanceof WalkUpCommand || command instanceof WalkDownCommand ||
//                                    command instanceof WalkLeftCommand || command instanceof WalkRightCommand) {
//                                commands.clear();
//                            } else {
//                                Log.e(TAG, "command NOT instanceof WalkUpCommand, WalkDownCommand, WalkLeftCommand, nor WalkRightCommand.");
//                            }

//                            // TODO: 2025_10_23 experimenting (start off aligned).
//                            if (x % Tile.WIDTH != 0) {
//                                int xRemainder = (int) (x % Tile.WIDTH);
//                                x -= xRemainder;
//                            }
//                            if (x % Tile.HEIGHT != 0) {
//                                int yRemainder = (int) (y % Tile.HEIGHT);
//                                y -= yRemainder;
//                            }
            }
//            }
        } else {
            Log.e(TAG, "commands.isEmpty()");

            if (!tileWorkRequests.isEmpty()) {
                Log.e(TAG, "!tileWorkRequests.isEmpty()");
                TileWorkRequest tileWorkRequestHead = tileWorkRequests.get(0);

                int xIndexSrc = ((int) x / Tile.WIDTH);
                int yIndexSrc = ((int) y / Tile.HEIGHT);
                int xIndexDest = tileWorkRequestHead.getTile().getxIndex();
                int yIndexDest = tileWorkRequestHead.getTile().getyIndex();

                TileManager tileManager = game.getSceneManager().getCurrentScene().getTileManager();
                Tile[][] tilesScene = tileManager.getTiles();
                Tile tileSrc = tilesScene[yIndexSrc][xIndexSrc];
                Tile tileDest = tilesScene[yIndexDest][xIndexDest];
                List<Tile> pathToTravel = tileManager.doesExistPath(tileSrc, tileDest);

                List<Command> pathToTravelAsCommands = convertPathToTravelToCommands(pathToTravel,
                        tileWorkRequestHead.getModeForTileSelectorView());
                commands.addAll(pathToTravelAsCommands);

                doNextCommand();
            } else {
                Log.e(TAG, "tileWorkRequests.isEmpty()");
//                counterCommands++;
//                if (counterCommands == COUNTER_COMMANDS_TARGET) {
//                    counterCommands = 0;

                state = State.OFF;
//                }
            }

        }
    }

    private void showSelectionsOfRobotDialogFragment(TileSelectorView.Mode modeForTileSelectorView,
                                                     RobotDialogFragment robotDialogFragment) {
        TileSelectorDialogFragment tileSelectorDialogFragment =
                TileSelectorDialogFragment.newInstance(
                        game.getSceneManager().getCurrentScene().getTileManager(),
                        modeForTileSelectorView,
                        new TileSelectorDialogFragment.TileSelectorListener() {
                            @Override
                            public void selected(List<Tile> tiles,
                                                 TileSelectorView.Mode modeForTileSelectorView) {
                                for (Tile tile : tiles) {
                                    tileWorkRequests.add(
                                            new TileWorkRequest(tile, modeForTileSelectorView)
                                    );
                                }

                                state = State.TILE_SELECTED;
                            }
                        }, new TileSelectorDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                robotDialogFragment.dismiss();

                                Log.e(TAG, "TileSelectorDialogFragment.DismissListener.onDismiss()");
                                doNextCommand();
                            }
                        });

        tileSelectorDialogFragment.show(
                ((MainActivity) game.getContext()).getSupportFragmentManager(),
                TileSelectorDialogFragment.TAG
        );
    }

    private List<Command> handleFindPathBackFromShippingBin() {
        TileManager tileManager = game.getSceneManager().getCurrentScene().getTileManager();
        Tile[][] tilesScene = tileManager.getTiles();
        int xIndexSrc = ((int) x / Tile.WIDTH);
        int yIndexSrc = ((int) y / Tile.HEIGHT);
        Tile tileSrc = tilesScene[yIndexSrc][xIndexSrc];
        Tile tileDest = tileBeforeWalkingToShippingBin;

        List<Tile> pathToTileBeforeWalkingToShippingBin = tileManager.doesExistPath(tileSrc, tileDest);

        tileShippingBin = null;
        tileBeforeWalkingToShippingBin = null;

        List<Command> commandsBackFromShippingBin =
                convertToMovementCommands(pathToTileBeforeWalkingToShippingBin);

        return commandsBackFromShippingBin;
    }

    Tile tileBeforeWalkingToShippingBin;
    Tile tileShippingBin;

    private List<Command> handleFindShortestPathToShippingBin() {
        Map<List<Tile>, Integer> optionsPathToTravel = new HashMap<>();

        TileManager tileManager = game.getSceneManager().getCurrentScene().getTileManager();
        Tile[][] tilesScene = tileManager.getTiles();
        int xIndexSrc = ((int) x / Tile.WIDTH);
        int yIndexSrc = ((int) y / Tile.HEIGHT);
        Tile tileSrc = tilesScene[yIndexSrc][xIndexSrc];
        for (Tile tileShippingBinDrop : tilesShippingBinDrop) {
            int xIndexDest = tileShippingBinDrop.getxIndex();
            int yIndexDest = tileShippingBinDrop.getyIndex();
            Tile tileDest = tilesScene[yIndexDest][xIndexDest];

            List<Tile> pathToTravel = tileManager.doesExistPath(tileSrc, tileDest);

            optionsPathToTravel.put(pathToTravel, pathToTravel.size());
        }

        // find shortest path, record tileDest/tileSrc.
        List<Tile> pathToShippingBinShortest = null;
        int min = Integer.MAX_VALUE;
        for (List<Tile> key : optionsPathToTravel.keySet()) {
            Integer numberOfTiles = optionsPathToTravel.get(key);
            if (numberOfTiles < min) {
                min = numberOfTiles;
                pathToShippingBinShortest = key;
            }
        }

        Tile tileDest = pathToShippingBinShortest.get(
                pathToShippingBinShortest.size() - 1
        );

        tileShippingBin = tileDest;
        tileBeforeWalkingToShippingBin = tileSrc;

        return convertToMovementCommands(pathToShippingBinShortest);
    }

    private List<Command> convertToMovementCommands(List<Tile> pathToTravel) {
        Log.e(TAG, "begin conversion");
        Log.e(TAG, "pathToTravel.size(): " + pathToTravel.size());
        List<Command> pathToTravelAsCommands = new ArrayList<>();

        // selected tile is tile-standing-on
        if (pathToTravel.size() == 1) {
            Log.e(TAG, "selected tile is tile-standing-on");
            handleMoveAsideThenFaceTargetTile(pathToTravelAsCommands);
        }
        // selected tile is direct-neighbor
        else if (pathToTravel.size() == 2) {
            Log.e(TAG, "selected tile is direct-neighbor");
            int indexLast = pathToTravel.size() - 1;
            Tile tileTarget = pathToTravel.get(indexLast);
            Command commandFaceTargetTile = generateCommandToFaceCorrectDirection(null, tileTarget);
            pathToTravelAsCommands.add(commandFaceTargetTile);
        }
        // selected tile is one-step-or-more-away
        else if (pathToTravel.size() > 2) {
            Log.e(TAG, "selected tile is one-step-or-more-away");
            Tile tileStepPrevious = null;
            int indexLast = pathToTravel.size() - 1; // do NOT include last element.
            for (int i = 0; i < indexLast; i++) {
                Tile tileStep = pathToTravel.get(i);

                int xIndexStart = (tileStepPrevious == null) ?
                        ((int) x / Tile.WIDTH) : (tileStepPrevious.getxIndex());
                int yIndexStart = (tileStepPrevious == null) ?
                        ((int) y / Tile.HEIGHT) : (tileStepPrevious.getyIndex());
                int xIndexEnd = tileStep.getxIndex();
                int yIndexEnd = tileStep.getyIndex();

                // move right
                if (xIndexStart < xIndexEnd) {
                    Log.e(TAG, "adding walk-right command");
                    pathToTravelAsCommands.add(walkRightCommand);
                }
                // move left
                else if (xIndexStart > xIndexEnd) {
                    Log.e(TAG, "adding walk-left command");
                    pathToTravelAsCommands.add(walkLeftCommand);
                }
                // move down
                else if (yIndexStart < yIndexEnd) {
                    Log.e(TAG, "adding walk-down command");
                    pathToTravelAsCommands.add(walkDownCommand);
                }
                // move up
                else if (yIndexStart > yIndexEnd) {
                    Log.e(TAG, "adding walk-up command");
                    pathToTravelAsCommands.add(walkUpCommand);
                } else {
                    Log.e(TAG, "else-clause... standing on same tile??? no command added.");
                }

                tileStepPrevious = tileStep;
            }

            Tile tileTarget = pathToTravel.get(indexLast);
            Command commandFaceTargetTile = generateCommandToFaceCorrectDirection(tileStepPrevious, tileTarget);
            pathToTravelAsCommands.add(commandFaceTargetTile);
        }
        Log.e(TAG, "end conversion");

        return pathToTravelAsCommands;
    }

    private List<Command> appendWorkCommands(List<Command> pathToTravelAsCommands,
                                             TileSelectorView.Mode modeForTileSelectorView) {
        switch (modeForTileSelectorView) {
            case TILL_SEED_WATER:
                Log.e(TAG, "adding till/seed/water commands");
                pathToTravelAsCommands.add(tillTileCommand);
                pathToTravelAsCommands.add(seedTileCommand);
                pathToTravelAsCommands.add(waterTileCommand);
                break;
            case ONLY_WATER:
                Log.e(TAG, "adding water command");
                pathToTravelAsCommands.add(waterTileCommand);
                break;
            case HARVEST:
                Log.e(TAG, "adding harvest command");
                pathToTravelAsCommands.add(harvestTileCommand);
                break;
            default:
                Log.e(TAG, "modeForTileSelectorView is undefined.");
        }

        return pathToTravelAsCommands;
    }

    private List<Command> convertPathToTravelToCommands(List<Tile> pathToTravel,
                                                        TileSelectorView.Mode modeForTileSelectorView) {
        List<Command> pathToTravelAsCommands = convertToMovementCommands(pathToTravel);
        appendWorkCommands(pathToTravelAsCommands, modeForTileSelectorView);

        return pathToTravelAsCommands;
    }

    private void handleMoveAsideThenFaceTargetTile(List<Command> pathToTravelAsCommands) {
        direction = Direction.LEFT;
        if (checkTileCurrentlyFacing().isWalkable()) {
            pathToTravelAsCommands.add(walkLeftCommand);
            pathToTravelAsCommands.add(faceRightCommand);
        } else {
            direction = Direction.RIGHT;
            if (checkTileCurrentlyFacing().isWalkable()) {
                pathToTravelAsCommands.add(walkRightCommand);
                pathToTravelAsCommands.add(faceLeftCommand);
            } else {
                direction = Direction.UP;
                if (checkTileCurrentlyFacing().isWalkable()) {
                    pathToTravelAsCommands.add(walkUpCommand);
                    pathToTravelAsCommands.add(faceDownCommand);
                } else {
                    direction = Direction.DOWN;
                    if (checkTileCurrentlyFacing().isWalkable()) {
                        pathToTravelAsCommands.add(walkDownCommand);
                        pathToTravelAsCommands.add(faceUpCommand);
                    } else {
                        Log.e(TAG, "CHECKED ALL 4 directions.... all NOT WALKABLE!!!");
                    }
                }
            }
        }
    }

    private Command generateCommandToFaceCorrectDirection(Tile tileBeforeTarget, Tile tileTarget) {
        int xIndexStart = (tileBeforeTarget != null) ?
                (tileBeforeTarget.getxIndex()) : ((int) (x / Tile.WIDTH));
        int yIndexStart = (tileBeforeTarget != null) ?
                (tileBeforeTarget.getyIndex()) : ((int) (y / Tile.HEIGHT));
        int xIndexEnd = tileTarget.getxIndex();
        int yIndexEnd = tileTarget.getyIndex();

        // face right
        if (xIndexStart < xIndexEnd) {
            return faceRightCommand;
        }
        // face left
        else if (xIndexStart > xIndexEnd) {
            return faceLeftCommand;
        }
        // face down
        else if (yIndexStart < yIndexEnd) {
            return faceDownCommand;
        }
        // face up
        else if (yIndexStart > yIndexEnd) {
            return faceUpCommand;
        } else {
            Log.e(TAG, "generateCommandToFaceCorrectDirection() else-clause returning null");
            return null;
        }
    }

    private List<TileWorkRequest> tileWorkRequests = new ArrayList<>();
}
