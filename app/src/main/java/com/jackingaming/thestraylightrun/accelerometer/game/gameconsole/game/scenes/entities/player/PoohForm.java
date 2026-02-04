package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.PoohAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.timer.CountdownTimer;

import java.io.Serializable;

public class PoohForm
        implements Form {
    public static final String TAG = PoohForm.class.getSimpleName();
    private static final long TARGET_TRANSFER_POINT_COOLDOWN = 500L;

    public interface MovementListener extends Serializable {
        void onMove(float xMove, float yMove);
    }

    private MovementListener movementListener;

    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    transient private Game game;

    private Player player;
    private PoohAnimationManager poohAnimationManager;

    private boolean isTransferPointCooldownComplete;
    private CountdownTimer countdownTimer;

    public PoohForm() {
        poohAnimationManager = new PoohAnimationManager();
    }

    @Override
    public void init(Game game) {
        this.game = game;
        player = Player.getInstance();
        poohAnimationManager.init(game);

        isTransferPointCooldownComplete = true;
        countdownTimer = new CountdownTimer(new CountdownTimer.CountdownListener() {
            @Override
            public void onCountdownEnd() {
                isTransferPointCooldownComplete = true;
            }
        }, TARGET_TRANSFER_POINT_COOLDOWN);
        countdownTimer.init();
    }

    @Override
    public void update(long elapsed) {
        // ANIMATION
        poohAnimationManager.update(elapsed);

        // ATTACK_COOLDOWN
        // TODO: placeholder for AttackCooldown


        // USER_INPUT (determine values of [offset-of-next-step]... potential movement)
        interpretInput();

        // MOVEMENT (check tile, item, entity, and transfer point collisions... actual movement)
        boolean successfulMove = player.move();
        if (successfulMove && movementListener != null) {
            movementListener.onMove(player.getxMove(), player.getyMove());
        }

        // PREPARE_FOR_RENDER
        determineNextImage();

        // CARRYABLE
        if (player.hasCarryable()) {
            player.moveCarryable();

            if (player.getCarryable() instanceof Creature) {
                Creature creatureBeingCarried = ((Creature) player.getCarryable());
                if (creatureBeingCarried.hasCarryable()) {
                    creatureBeingCarried.moveCarryable();
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        // Intentionally blank.
    }

    @Override
    public void interpretInput() {
        Log.d(TAG, "interpretInput()");
    }

    @Override
    public void determineNextImage() {
        Bitmap imageNext = poohAnimationManager.getCurrentFrame(player.getDirection(),
                player.getxMove(), player.getyMove());
        player.setImage(imageNext);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        Log.e(TAG, getClass().getSimpleName() + ".respondToTransferPointCollision(String key) key: " + key);

        //////////////////////////
        if (!isTransferPointCooldownComplete) {
            return;
        }

        isTransferPointCooldownComplete = false;
        countdownTimer.start();
        //////////////////////////

        game.getSceneManager().changeScene(key);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        // TODO: if (e instanceof SignPost) { readSignPost(); }

        if (e instanceof com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.frogger.Log) {
            return false;
        }

//        ((PassingThroughActivity)game.getContext()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(game.getContext(), getClass().getSimpleName() + ".respondToEntityCollision(Entity e) testing Toast-message from non-UI thread.", Toast.LENGTH_SHORT).show();
//            }
//        });

//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(game.getContext(), getClass().getSimpleName() + ".respondToEntityCollision(Entity e) testing Toast-message from non-UI thread.", Toast.LENGTH_SHORT).show();
//            }
//        });
        return true;
    }

    @Override
    public boolean respondToItemCollisionViaClick(Item item) {
        boolean successfullyAddedToBackpack = game.addItemToBackpack(item);
        game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
        return successfullyAddedToBackpack;
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        if (item instanceof HoneyPot) {
            game.incrementCurrency(
                    item.getPrice()
            );

            game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
        }
    }
}