package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveDownCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveLeftCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveRightCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveUpCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MovementCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;

import java.util.Map;
import java.util.Random;

public class NonPlayableCharacter extends Entity
        implements Player.PartyMovementListener {
    public static final String TAG = NonPlayableCharacter.class.getSimpleName();

    private Random random = new Random();

    private String id;
    private Bitmap portrait;
    private boolean stationary = false;

    public NonPlayableCharacter(String id, Bitmap portrait,
                                Map<Direction, AnimationDrawable> animationsByDirection, Direction directionFacing,
                                CollisionListener collisionListener, MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);

        this.id = id;
        this.portrait = portrait;
        this.direction = directionFacing;

        startAnimations();
    }

    @Override
    public void resetMovementCommands() {
        super.resetMovementCommands();
        turnStationaryOn();
    }

    public void turnStationaryOn() {
        stationary = true;
        updateAnimationsBasedOnStationary();
    }

    public void turnStationaryOff() {
        stationary = false;
        updateAnimationsBasedOnStationary();
    }

    public void toggleStationary() {
        Log.e(TAG, "toggleStationary()");
        stationary = !stationary;
        updateAnimationsBasedOnStationary();
    }

    public void updateAnimationsBasedOnStationary() {
        if (stationary) {
            stopAnimations();
        } else {
            startAnimations();
        }
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update(Handler handler) {
        if (stationary) {
            // do nothing.
        } else {
            if (animatorMovement.isRunning()) {
                return;
            }

            runMovementCommands();

//            if (animatorMovement.isRunning()) {
//                return;
//            }
//
//            if (getMovementCommands().isEmpty()) {
//                // DIRECTION (changes 20% of the time)
//                if (random.nextInt(5) < 1) {
//                    // determine direction
//                    int directionSpecified = random.nextInt(4);
//                    if (directionSpecified == 0) {
//                        direction = Direction.LEFT;
//                    } else if (directionSpecified == 1) {
//                        direction = Direction.RIGHT;
//                    } else if (directionSpecified == 2) {
//                        direction = Direction.UP;
//                    } else if (directionSpecified == 3) {
//                        direction = Direction.DOWN;
//                    }
//                } else {
//                    // do nothing.
//                }
//
//                doMoveBasedOnDirection(handler);
//            } else {
//                runMovementCommands();
//            }
        }
    }

    public String getId() {
        return id;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public boolean isStationary() {
        return stationary;
    }

    private int joinPartyWaitCounter = 0;
    private int joinPartyWaitCounterTarget = 0;

    public void setJoinPartyWaitCounterTarget(int joinPartyWaitCounterTarget) {
        this.joinPartyWaitCounterTarget = joinPartyWaitCounterTarget;
    }

    @Override
    public void onPartyLeaderMove(Player player, Handler handler) {
        joinPartyWaitCounter++;
        if (joinPartyWaitCounter >= joinPartyWaitCounterTarget) {
            if (stationary == true) {
                turnStationaryOff();
            }
        }

        MovementCommand movementCommand = null;
        switch (player.getDirection()) {
            case LEFT:
                movementCommand = new MoveLeftCommand(this, handler);
                break;
            case UP:
                movementCommand = new MoveUpCommand(this, handler);
                break;
            case RIGHT:
                movementCommand = new MoveRightCommand(this, handler);
                break;
            case DOWN:
                movementCommand = new MoveDownCommand(this, handler);
                break;
        }
        appendMovementCommand(movementCommand);
    }
}
