package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.projectiles;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Monsta;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Bubble extends Entity {
    public static final String TAG = Bubble.class.getSimpleName();

    private boolean growing;

    private int counterFrameTarget;
    private int counterFrame;

    private Bitmap[] framesBubble;
    private int indexFramesBubble;

    public Bubble(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        growing = true;

        // Bubble [projectile]: all frames.
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);
        counterFrame = 0;
        counterFrameTarget = 10;
        indexFramesBubble = 0;
        framesBubble = new Bitmap[6];
        for (int i = 0; i < framesBubble.length; i++) {
            int y = 1050;
            int x = 5 + (i * (16 + 2));

            framesBubble[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }
        image = framesBubble[0];
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

    @Override
    public void update(long elapsed) {
        counterFrame++;

        if (counterFrame >= counterFrameTarget) {
            indexFramesBubble++;
            if (indexFramesBubble >= framesBubble.length) {
//                indexFramesBubble = 0;
                indexFramesBubble = framesBubble.length - 1;
                growing = false;
            }

            image = framesBubble[indexFramesBubble];

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
        if (growing) {
            if (e instanceof Monsta) {
                boolean isBubbled = ((Monsta) e).isBubbled();
                if (!isBubbled) {
                    ((Monsta) e).becomeBubbled();
                    active = false;
                }
            }
        }
        return true;
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
}
