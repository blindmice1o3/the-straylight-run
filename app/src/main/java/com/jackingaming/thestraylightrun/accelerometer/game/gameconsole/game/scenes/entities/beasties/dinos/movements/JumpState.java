package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class JumpState
        implements State {

    private Game game;
    private Bubblun bubblun;
    private ObjectAnimator jumpAnimator;

    private boolean jumping = false;

    @Override
    public void enter() {
        jumping = true;
    }

    @Override
    public void exit() {
        jumping = false;
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
        if (jumping) {
            float yCurrent = bubblun.getY();
            float yEnd = yCurrent - Tile.HEIGHT;
            jumpAnimator = ObjectAnimator.ofFloat(bubblun, "y", yCurrent, yEnd);
            jumpAnimator.setDuration(500L);
            jumpAnimator.setInterpolator(new LinearInterpolator());
            jumpAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    bubblun.changeToFallState();
                }
            });

            Handler handler = new Handler(game.getContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    jumpAnimator.start();
                }
            });

            jumping = false;
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
