package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class Bubblun extends Entity {
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

    public void changeToAttackState() {
        boolean isCurrentlyAttackState = bubblunStateManager.getCurrentState(BubblunStateManager.StateLayer.ATTACK) instanceof AttackState;
        if (!isCurrentlyAttackState) {
            bubblunStateManager.changeState(BubblunStateManager.StateLayer.ATTACK, BubblunStateManager.ATTACK_STATE);
        } else {
            Log.d(TAG, "ALREADY ATTACK STATE!!!");
        }
    }

    public void popAttackState() {
        bubblunStateManager.popState(BubblunStateManager.StateLayer.ATTACK);
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
    public void respondToItemCollisionViaClick(Item item) {
        bubblunStateManager.respondToItemCollisionViaClick(item);
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
