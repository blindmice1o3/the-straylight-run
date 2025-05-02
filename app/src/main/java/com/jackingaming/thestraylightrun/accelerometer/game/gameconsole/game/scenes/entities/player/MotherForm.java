package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.MotherAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.io.Serializable;

public class MotherForm
        implements Form {
    public static final String TAG = MotherForm.class.getSimpleName();
    public static final int MOTHER = 1;
    public static final int DAUGHTER = 2;
    public static final int Y_INDEX_FOR_SPRITES_MOTHER = 30;
    public static final int Y_INDEX_FOR_SPRITES_DAUGHTER = 16;

    public interface MovementListener extends Serializable {
        void onMove(float xMove, float yMove);
    }

    private MovementListener movementListener;

    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    transient private Game game;

    private Player player;
    private int motherOrDaughter;
    private MotherAnimationManager motherAnimationManager;

    public MotherForm(int motherOrDaughter) {
        this.motherOrDaughter = motherOrDaughter;
        motherAnimationManager = new MotherAnimationManager();
    }

    @Override
    public void init(Game game) {
        this.game = game;
        player = Player.getInstance();

        int yIndexForSprites = -1;
        if (motherOrDaughter == 1) {
            yIndexForSprites = Y_INDEX_FOR_SPRITES_MOTHER;
        } else {
            yIndexForSprites = Y_INDEX_FOR_SPRITES_DAUGHTER;
        }
        motherAnimationManager.init(game, yIndexForSprites);
    }

    @Override
    public void update(long elapsed) {
        // ANIMATION
        motherAnimationManager.update(elapsed);

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
        player.moveCarryable();
    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        // Intentionally blank.
    }

    @Override
    public void interpretInput() {
        // TODO: testing.
    }

    @Override
    public void determineNextImage() {
        Bitmap imageNext = motherAnimationManager.getCurrentFrame(player.getDirection(),
                player.getxMove(), player.getyMove());
        player.setImage(imageNext);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        Log.e(TAG, getClass().getSimpleName() + ".respondToTransferPointCollision(String key) key: " + key);
        // TODO: change scene.
        game.getSceneManager().changeScene(key);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        // TODO: if (e instanceof SignPost) { readSignPost(); }

        if (e instanceof com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.frogger.Log) {
            return false;
        }

        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {
        game.addItemToBackpack(item);
        game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
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
