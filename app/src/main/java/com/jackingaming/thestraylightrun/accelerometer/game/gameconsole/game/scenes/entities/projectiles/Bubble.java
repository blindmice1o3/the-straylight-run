package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.projectiles;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Bubble extends Entity {
    public static final String TAG = Bubble.class.getSimpleName();

    private BubbleStateManager bubbleStateManager;

    public Bubble(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        bubbleStateManager = new BubbleStateManager();
    }

    @Override
    public void init(Game game) {
        super.init(game);

        bubbleStateManager.init(game, this);
    }

    public void bounceToRight() {
        float xEnd = x + (2 * Tile.WIDTH);
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(this, "x", xEnd);
        positionAnimator.setInterpolator(new LinearInterpolator());
        positionAnimator.setDuration(1000L);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                checkEntityCollision(0, 0);
            }
        });

        Handler handler = new Handler(game.getContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                positionAnimator.start();
            }
        });
    }

    public boolean becomeFullyInflated() {
        boolean isFullyInflated = bubbleStateManager.getCurrentState() instanceof InflatedState;
        if (!isFullyInflated) {
            bubbleStateManager.changeState(BubbleStateManager.INFLATED_STATE);
            return true;
        }
        return false;
    }

    @Override
    public void update(long elapsed) {
        bubbleStateManager.update(elapsed);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        return bubbleStateManager.respondToEntityCollision(e);
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {
        bubbleStateManager.respondToItemCollisionViaClick(item);
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        bubbleStateManager.respondToItemCollisionViaMove(item);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        bubbleStateManager.respondToTransferPointCollision(key);
    }
}
