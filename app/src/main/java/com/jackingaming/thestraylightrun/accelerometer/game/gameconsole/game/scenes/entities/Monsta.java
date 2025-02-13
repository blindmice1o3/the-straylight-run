package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Monsta extends Entity {
    public static final String TAG = Monsta.class.getSimpleName();

    private boolean bubbled;
    private boolean movingLeft;

    private int counterFrame;
    private int counterFrameTarget;

    private Bitmap[] monstaPatrolLeft;
    private Bitmap[] monstaPatrolRight;
    private int indexMonstaPatrol;
    private Bitmap[] monstaBubbled;
    private int indexMonstaBubbled;
    private ObjectAnimator movementLeftAnimator;
    private ObjectAnimator movementRightAnimator;

    public Monsta(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        bubbled = false;
        counterFrame = 0;
        counterFrameTarget = 30;

        // Monsta [Entity]
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexMonstaPatrol = 0;
        monstaPatrolLeft = new Bitmap[2];
        monstaPatrolRight = new Bitmap[2];
        for (int i = 0; i < monstaPatrolLeft.length; i++) {
            int y = 333;
            int x = 6 + (i * (16 + 5));

            monstaPatrolLeft[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            monstaPatrolRight[i] = Animation.flipImageHorizontally(monstaPatrolLeft[i]);
        }

        indexMonstaBubbled = 0;
        monstaBubbled = new Bitmap[3];
        for (int i = 0; i < monstaBubbled.length; i++) {
            int y = 333;
            int x = 267 + (i * (16 + 2));

            monstaBubbled[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }

        int xCurrent = (int) x;
        movementLeftAnimator = ObjectAnimator.ofFloat(this, "x",
                (xCurrent - (2 * Tile.WIDTH)));
        movementLeftAnimator.setDuration(1000L);
        movementLeftAnimator.setInterpolator(new LinearInterpolator());
        movementRightAnimator = ObjectAnimator.ofFloat(this, "x",
                (xCurrent + (2 * Tile.WIDTH)));
        movementRightAnimator.setDuration(1000L);
        movementRightAnimator.setInterpolator(new LinearInterpolator());

        movementLeftAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!bubbled) {
                    int xCurrent = (int) x;

                    movingLeft = false;
                    image = monstaPatrolRight[indexMonstaPatrol];
                    movementRightAnimator.setFloatValues(xCurrent, (xCurrent + (2 * Tile.WIDTH)));
                    movementRightAnimator.start();
                }
            }
        });
        movementRightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!bubbled) {
                    int xCurrent = (int) x;

                    movingLeft = true;
                    image = monstaPatrolLeft[indexMonstaPatrol];
                    movementLeftAnimator.setFloatValues(xCurrent, (xCurrent - (2 * Tile.WIDTH)));
                    movementLeftAnimator.start();
                }
            }
        });

        movingLeft = true;
        image = monstaPatrolLeft[0];
        movementLeftAnimator.start();
    }

    public void becomeBubbled() {
        bubbled = true;
        movementLeftAnimator.cancel();
        movementRightAnimator.cancel();
    }

    public boolean isBubbled() {
        return bubbled;
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;

        if (counterFrame >= counterFrameTarget) {

            if (bubbled) {
                indexMonstaBubbled++;
                if (indexMonstaBubbled >= monstaBubbled.length) {
                    indexMonstaBubbled = 0;
                }

                image = monstaBubbled[indexMonstaBubbled];
            } else {
                indexMonstaPatrol++;
                if (indexMonstaPatrol >= monstaPatrolLeft.length) {
                    indexMonstaPatrol = 0;
                }

                image = (movingLeft) ?
                        monstaPatrolLeft[indexMonstaPatrol] :
                        monstaPatrolRight[indexMonstaPatrol];
            }

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
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
