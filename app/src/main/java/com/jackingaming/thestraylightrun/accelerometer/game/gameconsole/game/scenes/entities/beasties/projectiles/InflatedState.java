package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class InflatedState
        implements State {
    public static final String TAG = InflatedState.class.getSimpleName();

    private Game game;
    private Bubble bubble;

    private int counterFrameTarget;
    private int counterFrame;

    private Bitmap[] framesBubble;
    private int indexFramesBubble;

    private Bitmap frameBubbleBurst;
    private int timerBurstTargetRemoval = 5750;
    private int timerBurstTarget = 5000;
    private int timerBurst = 0;

    @Override
    public void enter() {
        bubble.setImage(framesBubble[indexFramesBubble]);
        bubble.bounceUpAndDownStart();
    }

    @Override
    public void exit() {

    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Bubble) {
            bubble = (Bubble) e;
        }

        counterFrame = 0;
        counterFrameTarget = 30;

        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexFramesBubble = 0;
        framesBubble = new Bitmap[3];
        for (int i = 0; i < framesBubble.length; i++) {
            int y = 1072;
            int x = 6 + (i * (16 + 2));

            framesBubble[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }

        frameBubbleBurst = Bitmap.createBitmap(spriteSheet, 589, 1050, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
    }

    @Override
    public void update(long elapsed) {
        timerBurst += elapsed;
        if (timerBurst >= timerBurstTarget) {
            bubble.bounceUpAndDownStop();
            bubble.setImage(frameBubbleBurst);

            if (timerBurst >= timerBurstTargetRemoval) {
                bubble.setActive(false);
            }

            return;
        }

        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexFramesBubble++;
            if (indexFramesBubble >= framesBubble.length) {
                indexFramesBubble = 0;
            }

            bubble.setImage(framesBubble[indexFramesBubble]);

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        // intentionally empty (does NOT capture into bubble).
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item i) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item i) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }
}
