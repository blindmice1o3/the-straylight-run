package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs.RobotDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.PoohAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.bubblepop.SceneBubblePop;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles.Bubble;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.EntityCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneWorldMapPart01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHouseLevel01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.BedTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

import java.io.Serializable;

public class PoohForm
        implements Form {
    public static final String TAG = PoohForm.class.getSimpleName();

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

    public PoohForm() {
        poohAnimationManager = new PoohAnimationManager();
    }

    @Override
    public void init(Game game) {
        this.game = game;
        player = Player.getInstance();
        poohAnimationManager.init(game);
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
        Bitmap imageNext = poohAnimationManager.getCurrentFrame(player.getDirection(),
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