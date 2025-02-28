package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta.Monsta;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class GrowingState
        implements State {
    public static final String TAG = GrowingState.class.getSimpleName();

    private Game game;
    private Bubble bubble;

    private int counterFrameTarget;
    private int counterFrame;

    private Bitmap[] framesBubble;
    private int indexFramesBubble;

    @Override
    public void enter() {

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
        counterFrameTarget = 10;

        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexFramesBubble = 0;
        framesBubble = new Bitmap[6];
        for (int i = 0; i < framesBubble.length; i++) {
            int y = 1050;
            int x = 5 + (i * (16 + 2));

            framesBubble[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }
        bubble.setImage(framesBubble[0]);
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexFramesBubble++;
            if (indexFramesBubble >= framesBubble.length) {
                indexFramesBubble = framesBubble.length - 1;
                bubble.becomeFullyInflated();
                return;
            }

            bubble.setImage(framesBubble[indexFramesBubble]);

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        if (e instanceof Monsta) {
            boolean didBecomeBubbled = ((Monsta) e).becomeBubbled();
            if (didBecomeBubbled) {
                bubble.setActive(false);
            }
        }
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
