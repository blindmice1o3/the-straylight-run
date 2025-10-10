package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.attacks.AttackState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements.FallState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements.JumpState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements.WalkState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta.BubbledState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta.Monsta;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class Bubblun extends Creature {
    public static final String TAG = Bubblun.class.getSimpleName();

    private BubblunStateManager bubblunStateManager;
    private boolean movingLeft = false;

    public Bubblun(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        bubblunStateManager = new BubblunStateManager();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        bubblunStateManager.init(game, this);
    }

    @Override
    protected boolean skipEntityCollisionCheck(Entity e) {
        boolean collisionFromSelf = super.skipEntityCollisionCheck(e);
        boolean collisionFromMonstaBubbled = (e instanceof Monsta) && (((Monsta) e).getCurrentState() instanceof BubbledState);
        return collisionFromSelf || collisionFromMonstaBubbled;
    }

    public State getCurrentMovementState() {
        return bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.MOVEMENT);
    }

    public void changeToAttackState() {
        boolean isCurrentlyAttackState = bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.ATTACK) instanceof AttackState;
        if (!isCurrentlyAttackState) {
            bubblunStateManager.changeState(BubblunStateManager.StateLayer.ATTACK, BubblunStateManager.ATTACK_STATE);
        } else {
            Log.d(TAG, "ALREADY ATTACK STATE!!!");
        }
    }

    public void changeToJumpState() {
        Log.e(TAG, "changeToJumpState()");

        boolean isCurrentlyJumpState = bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.MOVEMENT) instanceof JumpState;
        if (!isCurrentlyJumpState) {
            bubblunStateManager.changeState(BubblunStateManager.StateLayer.MOVEMENT, BubblunStateManager.JUMP_STATE);
        } else {
            Log.d(TAG, "ALREADY JUMP STATE!!!");
        }
    }

    public void changeToFallState() {
        Log.e(TAG, "changeToFallState()");

        boolean isCurrentlyFallState = bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.MOVEMENT) instanceof FallState;
        if (!isCurrentlyFallState) {
            bubblunStateManager.changeState(BubblunStateManager.StateLayer.MOVEMENT, BubblunStateManager.FALL_STATE);
        } else {
            Log.d(TAG, "ALREADY FALL STATE!!!");
        }
    }

    public void changeToWalkState() {
        Log.e(TAG, "changeToWalkState()");

        boolean isCurrentlyWalkState = bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.MOVEMENT) instanceof WalkState;
        if (!isCurrentlyWalkState) {
            bubblunStateManager.changeState(BubblunStateManager.StateLayer.MOVEMENT, BubblunStateManager.WALK_STATE);
        } else {
            Log.d(TAG, "ALREADY WALK STATE!!!");
        }
    }

    public void changeToBaseState() {
        Log.e(TAG, "changeToBaseState()");

        boolean isCurrentlyBaseState = bubblunStateManager.getSizeOfStateStackMovement() == 1;
        if (!isCurrentlyBaseState) {
            int sizeOfStateStackMovement = bubblunStateManager.getSizeOfStateStackMovement();
            int numberOfStatesToPopFromStack = sizeOfStateStackMovement - 1;
            for (int i = 0; i < numberOfStatesToPopFromStack; i++) {
                bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.MOVEMENT).exit();
                bubblunStateManager.popState(BubblunStateManager.StateLayer.MOVEMENT);
            }
        } else {
            Log.d(TAG, "ALREADY IN BASE STATE!!!");
        }
    }

    public void popAttackState() {
        bubblunStateManager.popState(BubblunStateManager.StateLayer.ATTACK);
    }

    public void popMovementState() {
        bubblunStateManager.popState(BubblunStateManager.StateLayer.MOVEMENT);
    }

    @Override
    public void update(long elapsed) {
        bubblunStateManager.update(elapsed);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        return bubblunStateManager.respondToEntityCollision(e);
    }

    @Override
    public boolean respondToItemCollisionViaClick(Item item) {
        bubblunStateManager.respondToItemCollisionViaClick(item);
        return true;
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        bubblunStateManager.respondToItemCollisionViaMove(item);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        bubblunStateManager.respondToTransferPointCollision(key);
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }
}
