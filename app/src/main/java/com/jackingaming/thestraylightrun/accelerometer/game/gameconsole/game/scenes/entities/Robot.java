package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.RobotAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.Random;

public class Robot extends Creature {
    public static final String TAG = Robot.class.getSimpleName();

    public enum State {OFF, WALK, RUN;}

    private RobotAnimationManager robotAnimationManager;

    private State state;
    private Random random;

    public Robot(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        direction = Direction.DOWN;
        robotAnimationManager = new RobotAnimationManager();

        state = State.OFF;
        random = new Random();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        robotAnimationManager.init(game);
    }

    @Override
    public void update(long elapsed) {
        robotAnimationManager.update(elapsed);

        xMove = 0f;
        yMove = 0f;

        determineNextMove();
        move(); // TODO: no entity collision... player probably not in EntityManager's list?

        determineNextImage();
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
                    yMove = moveSpeed;
                }
                // LEFT
                else if (moveDir == 1) {
                    direction = Direction.LEFT;
                    xMove = -moveSpeed;
                }
                // UP
                else if (moveDir == 2) {
                    direction = Direction.UP;
                    yMove = -moveSpeed;
                }
                // RIGHT
                else if (moveDir == 3) {
                    direction = Direction.RIGHT;
                    xMove = moveSpeed;
                } else {
                    Log.e(TAG, "determineNextMove() else-clause moveDir.");
                }
            }
        }
    }

    private void determineNextImage() {
        image = robotAnimationManager.getCurrentFrame(direction, state, xMove, yMove);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return false;
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
    }

    public void changeToRun() {
        state = State.RUN;
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
