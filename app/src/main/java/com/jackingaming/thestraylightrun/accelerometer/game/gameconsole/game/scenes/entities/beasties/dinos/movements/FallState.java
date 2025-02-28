package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class FallState
        implements State {

    private Game game;
    private Bubblun bubblun;
    private ObjectAnimator fallAnimator;

    private boolean falling = false;

    @Override
    public void enter() {
        falling = true;
        bubblun.setDirection(Creature.Direction.DOWN);
    }

    @Override
    public void exit() {
        falling = false;
    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Bubblun) {
            bubblun = (Bubblun) e;
        }
    }

    @Override
    public void update(long elapsed) {
        if (falling) {
            float yCurrent = bubblun.getY();
            float yEnd = yCurrent + Tile.HEIGHT;
            fallAnimator = ObjectAnimator.ofFloat(bubblun, "y", yCurrent, yEnd);
            fallAnimator.setDuration(500L);
            fallAnimator.setInterpolator(new LinearInterpolator());
            fallAnimator.setRepeatMode(ValueAnimator.RESTART);
            fallAnimator.setRepeatCount(ValueAnimator.INFINITE);
            fallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                    boolean isAbleToMove = bubblun.move();
                    if (!isAbleToMove) {
                        fallAnimator.cancel();

                        bubblun.changeToBaseState();
                    }
                }
            });
            fallAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);

                    float yCurrent = bubblun.getY();
                    float yEnd = yCurrent + Tile.HEIGHT;
                    fallAnimator.setFloatValues(yCurrent, yEnd);
                }
            });

            Handler handler = new Handler(game.getContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    fallAnimator.start();
                }
            });

            falling = false;
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
