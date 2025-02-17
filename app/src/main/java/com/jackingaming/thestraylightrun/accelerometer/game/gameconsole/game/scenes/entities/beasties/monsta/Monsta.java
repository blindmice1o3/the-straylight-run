package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class Monsta extends Entity {
    public static final String TAG = Monsta.class.getSimpleName();

    private StateManager stateManager;
    private boolean bubbled = false;

    public Monsta(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        stateManager = new StateManager();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        stateManager.init(game, this);
    }

    public State getCurrentState() {
        return stateManager.getCurrentState();
    }

    public boolean becomeBubbled() {
        boolean isCurrentlyBubbled = stateManager.getCurrentState() instanceof BubbledState;
        if (!isCurrentlyBubbled) {
            stateManager.changeState(StateManager.BUBBLED_STATE);
            return true;
        }
        return false;
    }

    public void becomeUnbubbled() {
        boolean isCurrentlyBubbled = stateManager.getCurrentState() instanceof BubbledState;
        if (isCurrentlyBubbled) {
            stateManager.popState();
        }
    }

    @Override
    public void update(long elapsed) {
        stateManager.update(elapsed);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        return stateManager.respondToEntityCollision(e);
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

    public void setBubbled(boolean bubbled) {
        this.bubbled = bubbled;
    }

    public boolean isBubbled() {
        return bubbled;
    }
}
