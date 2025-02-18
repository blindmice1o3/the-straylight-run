package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class PatrolState
        implements State {
    public static final String TAG = PatrolState.class.getSimpleName();

    private Game game;
    private Monsta monsta;

    private Bitmap[] monstaPatrolLeft;
    private Bitmap[] monstaPatrolRight;
    private int indexMonstaPatrol;

    private ObjectAnimator movementLeftAnimator;
    private ObjectAnimator movementRightAnimator;

    private boolean movingLeft;
    private int counterFrame;
    private int counterFrameTarget;

    private boolean movingLeftPriorToStateChange;

    @Override
    public void enter() {
        Log.e(TAG, "enter()");
        int xCurrent = (int) monsta.getX();
        movingLeft = movingLeftPriorToStateChange;

        if (movingLeft) {
            monsta.setImage(monstaPatrolLeft[indexMonstaPatrol]);
            movementLeftAnimator.setFloatValues(xCurrent, (xCurrent - (2 * Tile.WIDTH)));

            Handler handler = new Handler(game.getContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    movementLeftAnimator.start();
                }
            });
        } else {
            monsta.setImage(monstaPatrolRight[indexMonstaPatrol]);
            movementRightAnimator.setFloatValues(xCurrent, (xCurrent + (2 * Tile.WIDTH)));

            Handler handler = new Handler(game.getContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    movementRightAnimator.start();
                }
            });
        }
    }

    @Override
    public void exit() {
        Log.e(TAG, "exit()");
        movingLeftPriorToStateChange = movingLeft;

        Handler handler = new Handler(game.getContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                movementLeftAnimator.cancel();
                movementRightAnimator.cancel();
            }
        });
    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Monsta) {
            monsta = (Monsta) e;
        }

        counterFrame = 0;
        counterFrameTarget = 30;

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


        int xCurrent = (int) monsta.getX();
        movementLeftAnimator = ObjectAnimator.ofFloat(monsta, "x",
                (xCurrent - (2 * Tile.WIDTH)));
        movementLeftAnimator.setDuration(1000L);
        movementLeftAnimator.setInterpolator(new LinearInterpolator());
        movementRightAnimator = ObjectAnimator.ofFloat(monsta, "x",
                (xCurrent + (2 * Tile.WIDTH)));
        movementRightAnimator.setDuration(1000L);
        movementRightAnimator.setInterpolator(new LinearInterpolator());

        movementLeftAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!monsta.isBubbled()) {
                    int xCurrent = (int) monsta.getX();

                    movingLeft = false;
                    monsta.setImage(monstaPatrolRight[indexMonstaPatrol]);
                    movementRightAnimator.setFloatValues(xCurrent, (xCurrent + (2 * Tile.WIDTH)));
                    movementRightAnimator.start();
                }
            }
        });
        movementRightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!monsta.isBubbled()) {
                    int xCurrent = (int) monsta.getX();

                    movingLeft = true;
                    monsta.setImage(monstaPatrolLeft[indexMonstaPatrol]);
                    movementLeftAnimator.setFloatValues(xCurrent, (xCurrent - (2 * Tile.WIDTH)));
                    movementLeftAnimator.start();
                }
            }
        });

        movingLeft = true;
        monsta.setImage(monstaPatrolLeft[0]);
        movementLeftAnimator.start();
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexMonstaPatrol++;
            if (indexMonstaPatrol >= monstaPatrolLeft.length) {
                indexMonstaPatrol = 0;
            }

            Bitmap imageForFrame = (movingLeft) ?
                    monstaPatrolLeft[indexMonstaPatrol] :
                    monstaPatrolRight[indexMonstaPatrol];
            monsta.setImage(imageForFrame);

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
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
