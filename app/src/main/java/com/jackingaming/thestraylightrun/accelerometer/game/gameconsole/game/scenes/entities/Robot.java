package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.RobotAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.TillGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.WalkDownTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.WaterGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Robot extends Creature {
    public static final String TAG = Robot.class.getSimpleName();
    public static final long DEFAULT_MOVEMENT_DURATION = 1000L;
    public static final long RUNNING_MOVEMENT_DURATION = 500L;

    public enum State {OFF, WALK, RUN;}

    private RobotAnimationManager robotAnimationManager;
    private ObjectAnimator movementAnimator;

    private State state;
    private Random random;

    public Robot(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        direction = Direction.DOWN;
        robotAnimationManager = new RobotAnimationManager();
        // TODO: change movement from update() to ObjectAnimator.
        movementAnimator =
                ObjectAnimator.ofFloat(this, "x", x - Tile.WIDTH);
        movementAnimator.setDuration(DEFAULT_MOVEMENT_DURATION);
        movementAnimator.setInterpolator(new LinearInterpolator());
        tileCommands = new ArrayList<>();
        tileCommands.add(new TillGrowableTileCommand(null));
        tileCommands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
        tileCommands.add(new WaterGrowableTileCommand(null));
        tileCommands.add(new WalkDownTileCommand(this));
        tileCommands.add(new TillGrowableTileCommand(null));
        tileCommands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
        tileCommands.add(new WaterGrowableTileCommand(null));
        tileCommands.add(new WalkDownTileCommand(this));
        tileCommands.add(new TillGrowableTileCommand(null));
        tileCommands.add(new SeedGrowableTileCommand(null, MysterySeed.TAG));
        tileCommands.add(new WaterGrowableTileCommand(null));
        tileCommands.add(new WalkDownTileCommand(this));

        state = State.OFF;
        random = new Random();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        robotAnimationManager.init(game);
    }

    private List<TileCommand> tileCommands;
    private int counter = 0;

    @Override
    public void update(long elapsed) {
        robotAnimationManager.update(elapsed);

        if (state != State.OFF) {
            if (!tileCommands.isEmpty()) {
                counter++;
                if (counter == 50) {
                    TileCommand tileCommand = tileCommands.get(0);
                    Tile tileCurrentlyFacing = checkTileCurrentlyFacing();

                    tileCommand.setTile(tileCurrentlyFacing);
                    tileCommand.execute();
                    tileCommands.remove(tileCommand);

                    counter = 0;
                }
            } else {
                if (!movementAnimator.isRunning()) {
                    determineNextMove();
                    move();
                }
            }
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

    public void toggleState() {
        int indexStateCurrent = state.ordinal();

        indexStateCurrent++;

        if (indexStateCurrent >= State.values().length) {
            indexStateCurrent = 0;
        }

        state = State.values()[indexStateCurrent];
    }
}
