package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class Monsta extends Entity {
    public static final String TAG = Monsta.class.getSimpleName();

    private MonstaStateManager monstaStateManager;
    private boolean bubbled = false;

    public Monsta(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        monstaStateManager = new MonstaStateManager();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        monstaStateManager.init(game, this);
    }

    public State getCurrentState() {
        return monstaStateManager.getCurrentState();
    }

    public boolean becomeBubbled() {
        boolean isCurrentlyBubbled = monstaStateManager.getCurrentState() instanceof BubbledState;
        if (!isCurrentlyBubbled) {
            monstaStateManager.changeState(MonstaStateManager.BUBBLED_STATE);
            return true;
        }
        return false;
    }

    public void becomeUnbubbled() {
        boolean isCurrentlyBubbled = monstaStateManager.getCurrentState() instanceof BubbledState;
        if (isCurrentlyBubbled) {
            monstaStateManager.popState();
        }
    }

    @Override
    public void update(long elapsed) {
        monstaStateManager.update(elapsed);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        return monstaStateManager.respondToEntityCollision(e);
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {
        monstaStateManager.respondToItemCollisionViaClick(item);
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        monstaStateManager.respondToItemCollisionViaMove(item);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        monstaStateManager.respondToTransferPointCollision(key);
    }

    public void setBubbled(boolean bubbled) {
        this.bubbled = bubbled;
    }

    public boolean isBubbled() {
        return bubbled;
    }
}
