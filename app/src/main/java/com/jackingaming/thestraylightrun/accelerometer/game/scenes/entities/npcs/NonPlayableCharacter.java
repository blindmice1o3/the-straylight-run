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
        implements Player.LeaderListener {
    public static final String TAG = NonPlayableCharacter.class.getSimpleName();

    private Random random = new Random();

    private String id;
    private Bitmap portrait;
    private boolean stationary = false;

    public NonPlayableCharacter(String id, Bitmap portrait, String[] dialogueArray,
                                Map<Direction, AnimationDrawable> animationsByDirection, Direction directionFacing,
                                CollisionListener collisionListener, MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);

        this.id = id;
        this.portrait = portrait;
        this.dialogueArray = dialogueArray;
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

    @Override
    protected void moveAfterValidation(Handler handler, String propertyName, float valueStart, float valueEnd) {
        if (leaderListener != null) {
            leaderListener.onLeaderMove(this, handler);
        }
        super.moveAfterValidation(handler, propertyName, valueStart, valueEnd);
    }

    @Override
    public void onLeaderMove(Entity leader, Handler handler) {
        MovementCommand movementCommand = null;
        switch (leader.getDirection()) {
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
