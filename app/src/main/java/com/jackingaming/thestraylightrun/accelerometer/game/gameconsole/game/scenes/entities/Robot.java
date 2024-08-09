package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs.RobotDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs.TileSelectorDialogFragment;
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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.WaterGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Robot extends Creature {
    public static final String TAG = Robot.class.getSimpleName();
    public static final long DEFAULT_MOVEMENT_DURATION = 1000L;
    public static final long RUNNING_MOVEMENT_DURATION = 500L;

    public enum State {OFF, WALK, RUN, TILE_SELECTED;}

    private RobotAnimationManager robotAnimationManager;
    private ObjectAnimator movementAnimator;

    private State state;
    private Random random;
    private Command walkLeftCommand, walkUpCommand, walkRightCommand, walkDownCommand,
            faceLeftCommand, faceUpCommand, faceRightCommand, faceDownCommand,
            tillTileCommand, seedTileCommand, waterTileCommand;


    public Robot(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        direction = Direction.DOWN;
        robotAnimationManager = new RobotAnimationManager();
        movementAnimator =
                ObjectAnimator.ofFloat(this, "x", x - Tile.WIDTH);
        movementAnimator.setDuration(DEFAULT_MOVEMENT_DURATION);
        movementAnimator.setInterpolator(new LinearInterpolator());

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
        commands = new ArrayList<>();
//        commands.add(new WalkDownCommand(this));
//        commands.add(new FaceRightCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkRightCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkRightCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkRightCommand(this));
//        commands.add(new FaceDownCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkDownCommand(this));
//        commands.add(new FaceLeftCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkLeftCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkLeftCommand(this));
//        commands.add(new FaceDownCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkDownCommand(this));
//        commands.add(new FaceRightCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));
//        commands.add(new WalkRightCommand(this));
//        commands.add(new TillGrowableTileCommand(null));
//        commands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
//        commands.add(new WaterGrowableTileCommand(null));


        state = State.OFF;
        random = new Random();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        robotAnimationManager.init(game);
    }

    private List<Command> commands;
    private int counterCommands = 0;
    private int counterTileSelected = 0;

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
                if (!commands.isEmpty()) {
                    counterCommands++;
                    if (counterCommands == 50) {
                        counterCommands = 0;

                        Command command = commands.get(0);
                        if (command instanceof TileCommand) {
                            Tile tileCurrentlyFacing = checkTileCurrentlyFacing();

                            ((TileCommand) command).setTile(tileCurrentlyFacing);
                        }

                        command.execute();
                        commands.remove(command);

                        if (commands.isEmpty()) {
                            tilesToTillSeedWater.remove(0);
                        }
                    }
                } else {
                    if (!tilesToTillSeedWater.isEmpty()) {
                        xIndexSrc = ((int) x / Tile.WIDTH);
                        yIndexSrc = ((int) y / Tile.HEIGHT);
                        xIndexDest = tilesToTillSeedWater.get(0).getxIndex();
                        yIndexDest = tilesToTillSeedWater.get(0).getyIndex();

                        TileManager tileManager = game.getSceneManager().getCurrentScene().getTileManager();
                        Tile[][] tilesScene = tileManager.getTiles();
                        List<Tile> pathToTravel = tileManager.doesExistPath(
                                tilesScene[yIndexSrc][xIndexSrc],
                                tilesScene[yIndexDest][xIndexDest]
                        );

                        convertPathToTravelAndAppendToCommands(pathToTravel);
                    } else {
                        counterCommands++;
                        if (counterCommands == 50) {
                            counterCommands = 0;

                            state = State.OFF;
                        }
                    }

                }
                break;
        }

        determineNextImage();
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
    public void performMove() {
        movementAnimator.setPropertyName(propertyName);
        movementAnimator.setFloatValues(valueStart, valueEnd);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                movementAnimator.start();
            }
        });
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
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }

    public void changeToOff() {
        state = State.OFF;
    }

    public void changeToWalk() {
        state = State.WALK;
        movementAnimator.setDuration(DEFAULT_MOVEMENT_DURATION);
    }

    public void changeToRun() {
        state = State.RUN;
        movementAnimator.setDuration(RUNNING_MOVEMENT_DURATION);
    }

    private int xIndexSrc, yIndexSrc = -1;
    private int xIndexDest, yIndexDest = -1;

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
            public void onTileSelectorButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                TileSelectorDialogFragment tileSelectorDialogFragment =
                        TileSelectorDialogFragment.newInstance(
                                game.getSceneManager().getCurrentScene().getTileManager(),
                                new TileSelectorDialogFragment.TileSelectorListener() {
                                    @Override
                                    public void selected(List<Tile> tiles) {
                                        tilesToTillSeedWater.addAll(tiles);

                                        state = State.TILE_SELECTED;
                                    }
                                }, new TileSelectorDialogFragment.DismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        robotDialogFragment.dismiss();
                                    }
                                });

                tileSelectorDialogFragment.show(
                        ((MainActivity) game.getContext()).getSupportFragmentManager(),
                        TileSelectorDialogFragment.TAG
                );
            }
        }, new RobotDialogFragment.DismissListener() {
            @Override
            public void onDismiss() {
                game.setPaused(false);
            }
        });

        return robotDialogFragment;
    }

    private void convertPathToTravelAndAppendToCommands(List<Tile> pathToTravel) {
        Log.e(TAG, "begin conversion");
        Tile tileStepPrevious = null;
        for (Tile tileStep : pathToTravel) {
            int xIndexStart = (tileStepPrevious == null) ?
                    ((int) x / Tile.WIDTH) : (tileStepPrevious.getxIndex());
            int yIndexStart = (tileStepPrevious == null) ?
                    ((int) y / Tile.HEIGHT) : (tileStepPrevious.getyIndex());
            int xIndexEnd = tileStep.getxIndex();
            int yIndexEnd = tileStep.getyIndex();

            // move right
            if (xIndexStart < xIndexEnd) {
                Log.e(TAG, "adding walk-right command");
                commands.add(walkRightCommand);
            }
            // move left
            else if (xIndexStart > xIndexEnd) {
                Log.e(TAG, "adding walk-left command");
                commands.add(walkLeftCommand);
            }
            // move down
            else if (yIndexStart < yIndexEnd) {
                Log.e(TAG, "adding walk-down command");
                commands.add(walkDownCommand);
            }
            // move up
            else if (yIndexStart > yIndexEnd) {
                Log.e(TAG, "adding walk-up command");
                commands.add(walkUpCommand);
            }

            tileStepPrevious = tileStep;
        }
        Log.e(TAG, "end conversion");

        Log.e(TAG, "removing LAST walk command");
        int indexEnd = commands.size() - 1;
        commands.remove(indexEnd);

        // determine tile to face
        Tile tileBeforeEnd = null;
        Tile tileEnd = null;
        Command faceCorrectDirectionCommand = null;
        if (pathToTravel.size() > 1) {
            int indexEndPathToTravel = pathToTravel.size() - 1;
            tileEnd = pathToTravel.get(indexEndPathToTravel);
            int indexBeforeEndPathToTravel = pathToTravel.size() - 2;
            tileBeforeEnd = pathToTravel.get(indexBeforeEndPathToTravel);

            faceCorrectDirectionCommand = generateCommandToFaceCorrectDirection(tileBeforeEnd, tileEnd);
        } else {
            int indexEndPathToTravel = pathToTravel.size() - 1;
            tileEnd = pathToTravel.get(indexEndPathToTravel);

            faceCorrectDirectionCommand = generateCommandToFaceCorrectDirection(null, tileEnd);
        }
        Log.e(TAG, "adding face-correct-direction command");
        commands.add(faceCorrectDirectionCommand);

        Log.e(TAG, "adding till/seed/water commands");
        commands.add(tillTileCommand);
        commands.add(seedTileCommand);
        commands.add(waterTileCommand);
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

    private List<Tile> tilesToTillSeedWater = new ArrayList<>();
}
