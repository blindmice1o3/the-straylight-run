package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.bubblepop.SceneBubblePop;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class WalkState
        implements State {

    private Game game;
    private Bubblun bubblun;
    private ObjectAnimator walkAnimator;

    private boolean walking;
    private boolean facingLeft;

    @Override
    public void enter() {
        facingLeft = bubblun.isMovingLeft();
        walking = true;
    }

    @Override
    public void exit() {
        walking = false;
    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Bubblun) {
            bubblun = (Bubblun) e;
        }

        float xStart = bubblun.getX();
        float xEnd = (facingLeft) ?
                (xStart - Tile.WIDTH) :
                (xStart + Tile.WIDTH);
        walkAnimator = ObjectAnimator.ofFloat(bubblun, "x", xStart, xEnd);
        walkAnimator.setDuration(1000L);
        walkAnimator.setInterpolator(new LinearInterpolator());
        walkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                if (game.getSceneManager().getCurrentScene() instanceof SceneBubblePop) {
                    // check left/right move for tile/entity collision.
                    if (facingLeft) {
                        int xLeftTop = (int) bubblun.getX();
                        int yLeftTop = (int) bubblun.getY();
                        int xLeftBottom = (int) bubblun.getX();
                        int yLeftBottom = (int) (bubblun.getY() + bubblun.getHeight() - 2);

                        // CHECKING tile collision: LEFT-TOP and LEFT-BOTTOM
                        boolean isLeftTopSolid =
                                ((SceneBubblePop) game.getSceneManager().getCurrentScene()).isSolidTile(
                                        xLeftTop, yLeftTop
                                );
                        boolean isLeftBottomSolid =
                                ((SceneBubblePop) game.getSceneManager().getCurrentScene()).isSolidTile(
                                        xLeftBottom, yLeftBottom
                                );
                        // CHECKING entity collision AND item collision
                        boolean isEntityCollision = bubblun.checkEntityCollision(0f, 0f);
                        boolean isItemCollision = bubblun.checkItemCollision(0f, 0f, false);
                        if (isLeftTopSolid || isLeftBottomSolid ||
                                isEntityCollision || isItemCollision) {
                            walkAnimator.cancel();
                            bubblun.changeToBaseState();
                        }
                    } else {
                        int xRightTop = (int) (bubblun.getX() + bubblun.getWidth());
                        int yRightTop = (int) bubblun.getY();
                        int xRightBottom = (int) (bubblun.getX() + bubblun.getWidth());
                        int yRightBottom = (int) (bubblun.getY() + bubblun.getHeight() - 2);

                        // CHECKING tile collision: RIGHT-TOP and RIGHT-BOTTOM
                        boolean isRightTopSolid =
                                ((SceneBubblePop) game.getSceneManager().getCurrentScene()).isSolidTile(
                                        xRightTop, yRightTop
                                );
                        boolean isRightBottomSolid =
                                ((SceneBubblePop) game.getSceneManager().getCurrentScene()).isSolidTile(
                                        xRightBottom, yRightBottom
                                );
                        // CHECKING entity collision AND item collision
                        boolean isEntityCollision = bubblun.checkEntityCollision(0f, 0f);
                        boolean isItemCollision = bubblun.checkItemCollision(0f, 0f, false);
                        if (isRightTopSolid || isRightBottomSolid ||
                                isEntityCollision || isItemCollision) {
                            walkAnimator.cancel();
                            bubblun.changeToBaseState();
                        }
                    }

                    // after successful left/right move... check if walked off platform.
                    int xLeftBottom = (int) bubblun.getX();
                    int yLeftBottom = (int) (bubblun.getY() + bubblun.getHeight());
                    boolean isLeftBottomSolid =
                            ((SceneBubblePop) game.getSceneManager().getCurrentScene()).isSolidTile(
                                    xLeftBottom, yLeftBottom
                            );

                    int xRightBottom = (int) (bubblun.getX() + bubblun.getWidth());
                    int yRightBottom = (int) (bubblun.getY() + bubblun.getHeight());
                    boolean isRightBottomSolid =
                            ((SceneBubblePop) game.getSceneManager().getCurrentScene()).isSolidTile(
                                    xRightBottom, yRightBottom
                            );

                    if (!isLeftBottomSolid && !isRightBottomSolid) {
                        walkAnimator.cancel();

                        bubblun.changeToBaseState();
                        bubblun.changeToFallState();
                    }
                }
            }
        });
        walkAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (bubblun.getCurrentMovementState() instanceof WalkState) {
                    float xStart = bubblun.getX();
                    float xEnd = (facingLeft) ?
                            (xStart - Tile.WIDTH) :
                            (xStart + Tile.WIDTH);
                    walkAnimator.setFloatValues(xStart, xEnd);

                    Handler handler = new Handler(game.getContext().getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            walkAnimator.start();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void update(long elapsed) {
        if (walking) {
            float xStart = bubblun.getX();
            float xEnd = (facingLeft) ?
                    (xStart - Tile.WIDTH) :
                    (xStart + Tile.WIDTH);
            walkAnimator.setFloatValues(xStart, xEnd);

            Handler handler = new Handler(game.getContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    walkAnimator.start();
                }
            });

            walking = false;
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
