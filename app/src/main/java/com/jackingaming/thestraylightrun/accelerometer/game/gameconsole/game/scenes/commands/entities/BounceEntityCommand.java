package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.BounceInterpolator;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Caterpillar;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class BounceEntityCommand
        implements EntityCommand {
    public static final String TAG = BounceEntityCommand.class.getSimpleName();

    private Entity entity;

    public BounceEntityCommand(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean execute() {
        if (entity instanceof Caterpillar) {
            Log.e(TAG, "BounceEntityCommand.execute(): entity instanceof Eel");
            float xStartEel = entity.getX();
            float yStartEel = entity.getY();
            float xEndEel = xStartEel;
            float yEndEel = yStartEel;
            float numberOfTile = 2f;

            Player player = Player.getInstance();
            switch (player.getDirection()) {
                case UP:
                    yEndEel = yStartEel - (numberOfTile * Tile.HEIGHT);
                    break;
                case DOWN:
                case CENTER:
                    yEndEel = yStartEel + (numberOfTile * Tile.HEIGHT);
                    break;
                case LEFT:
                    xEndEel = xStartEel - (numberOfTile * Tile.WIDTH);
                    break;
                case RIGHT:
                    xEndEel = xStartEel + (numberOfTile * Tile.WIDTH);
                    break;
                case UP_LEFT:
                    xEndEel = xStartEel - (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel - (numberOfTile * Tile.HEIGHT);
                    break;
                case UP_RIGHT:
                    xEndEel = xStartEel + (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel - (numberOfTile * Tile.HEIGHT);
                    break;
                case DOWN_LEFT:
                    xEndEel = xStartEel - (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel + (numberOfTile * Tile.HEIGHT);
                    break;
                case DOWN_RIGHT:
                    xEndEel = xStartEel + (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel + (numberOfTile * Tile.HEIGHT);
                    break;
            }

            String propertyName = null;
            float endValue = -1f;
            // NON-diagonal directions
            if (player.getDirection() == Creature.Direction.LEFT ||
                    player.getDirection() == Creature.Direction.RIGHT ||
                    player.getDirection() == Creature.Direction.UP ||
                    player.getDirection() == Creature.Direction.DOWN) {
                // Determine horizontal or vertical direction of movement.
                if (player.getDirection() == Creature.Direction.LEFT ||
                        player.getDirection() == Creature.Direction.RIGHT) {
                    propertyName = "x";
                    endValue = xEndEel;
                } else if (player.getDirection() == Creature.Direction.UP ||
                        player.getDirection() == Creature.Direction.DOWN) {
                    propertyName = "y";
                    endValue = yEndEel;
                }

                ObjectAnimator animatorPosition = ObjectAnimator.ofFloat(entity,
                        propertyName, endValue);
                animatorPosition.setInterpolator(new BounceInterpolator());
                animatorPosition.setDuration(1000L);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        animatorPosition.start();
                    }
                });

                return true;
            }
            // Diagonal directions.
            else {
                ObjectAnimator animatorPositionHorizontal = ObjectAnimator.ofFloat(entity,
                        "x", xEndEel);
                ObjectAnimator animatorPositionVertical = ObjectAnimator.ofFloat(entity,
                        "y", yEndEel);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorPositionHorizontal, animatorPositionVertical);
                animatorSet.setInterpolator(new BounceInterpolator());
                animatorSet.setDuration(1000L);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        animatorSet.start();
                    }
                });

                return true;
            }
        }
        Log.e(TAG, "entity is NOT Eel... entity's class: " + entity.getClass().getSimpleName());
        return false;
    }
}
