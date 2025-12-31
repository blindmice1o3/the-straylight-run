package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.BounceInterpolator;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Caterpillar;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.PoohForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class CollidingOrbit extends Creature {
    public static final String TAG = CollidingOrbit.class.getSimpleName();
    public static final int ANGLE_OF_ROTATION_IN_DEGREE = 4;

    public Entity entityToOrbit;
    private boolean clockwise;
    private double cosCW, sinCW;
    private double cosCCW, sinCCW;

    public CollidingOrbit(int xSpawn, int ySpawn, Entity entityToOrbit) {
        super(xSpawn, ySpawn);
        this.entityToOrbit = entityToOrbit;
        clockwise = true;

        double angleOfRotationInRadianCW = Math.toRadians(ANGLE_OF_ROTATION_IN_DEGREE);
        cosCW = Math.cos(angleOfRotationInRadianCW);
        sinCW = Math.sin(angleOfRotationInRadianCW);
        double angleOfRotationInRadianCCW = Math.toRadians(-ANGLE_OF_ROTATION_IN_DEGREE);
        cosCCW = Math.cos(angleOfRotationInRadianCCW);
        sinCCW = Math.sin(angleOfRotationInRadianCCW);
    }

    @Override
    protected boolean skipEntityCollisionCheck(Entity e) {
        return super.skipEntityCollisionCheck(e) ||
                (e instanceof Player);
    }

    @Override
    public void init(Game game) {
        super.init(game);
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 93, 669, 244, 261);

        if (entityToOrbit instanceof Player) {
            Form form = ((Player) entityToOrbit).getForm();
            if (form instanceof PoohForm) {
                ((PoohForm) form).setMovementListener(new PoohForm.MovementListener() {
                    @Override
                    public void onMove(float xMove, float yMove) {
                        x += xMove;
                        y += yMove;
                    }
                });
            }
        }
    }

    @Override
    public void update(long elapsed) {
        if (clockwise) {
            rotateClockwise();
        } else {
            rotateCounterClockwise();
        }
        checkEntityCollision(0f, 0f);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        if (e instanceof Caterpillar) {
            Log.e(TAG, "respondToEntityCollision(): e instanceof Eel");
            // bounce Eel based on CollidingOrbit's cw/ccw.
            float xStartEel = e.getX();
            float yStartEel = e.getY();
            float xEndEel = xStartEel;
            float yEndEel = yStartEel;
            float numberOfTile = 2f;

            Player player = Player.getInstance();
            // RIGHT of player
            if (xStartEel > player.getX()) {
                // BELOW player
                if (yStartEel > player.getY()) {
                    xEndEel = xStartEel + (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel + (numberOfTile * Tile.HEIGHT);
                }
                // ABOVE PLAYER
                else {
                    xEndEel = xStartEel + (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel - (numberOfTile * Tile.HEIGHT);
                }
            }
            // LEFT of player
            else {
                // BELOW player
                if (yStartEel > player.getY()) {
                    xEndEel = xStartEel - (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel + (numberOfTile * Tile.HEIGHT);
                }
                // ABOVE PLAYER
                else {
                    xEndEel = xStartEel - (numberOfTile * Tile.WIDTH);
                    yEndEel = yStartEel - (numberOfTile * Tile.HEIGHT);
                }
            }

            String propertyName = null;
            float endValue = -1f;
            if (player.getDirection() == Direction.LEFT || player.getDirection() == Direction.RIGHT) {
                propertyName = "x";
                endValue = xEndEel;
            } else {
                propertyName = "y";
                endValue = yEndEel;
            }

            ObjectAnimator animatorPosition = ObjectAnimator.ofFloat(e,
                    propertyName, endValue);
            animatorPosition.setInterpolator(new BounceInterpolator());
            animatorPosition.setDuration(1000L);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    animatorPosition.start();
                }
            });

            toggleClockwise();

            return true;
        }

        return false;
    }

    @Override
    public boolean respondToItemCollisionViaClick(Item item) {
        Log.e(TAG, "respondToItemCollisionViaClick()");
        return false;
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        Log.e(TAG, "respondToItemCollisionViaMove()");
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        Log.e(TAG, "respondToTransferPointCollision()");
    }

    private void rotateClockwise() {
        double xToRotate = x;
        double yToRotate = y;
        double xCenterOfRotation = entityToOrbit.getX();
        double yCenterOfRotation = entityToOrbit.getY();

        double temp = ((xToRotate - xCenterOfRotation) * cosCW) - ((yToRotate - yCenterOfRotation) * sinCW) + xCenterOfRotation;
        double yRotated = ((xToRotate - xCenterOfRotation) * sinCW) + ((yToRotate - yCenterOfRotation) * cosCW) + yCenterOfRotation;
        double xRotated = temp;

        x = (float) xRotated;
        y = (float) yRotated;
    }

    private void rotateCounterClockwise() {
        double xToRotate = x;
        double yToRotate = y;
        double xCenterOfRotation = entityToOrbit.getX();
        double yCenterOfRotation = entityToOrbit.getY();

        double temp = ((xToRotate - xCenterOfRotation) * cosCCW) - ((yToRotate - yCenterOfRotation) * sinCCW) + xCenterOfRotation;
        double yRotated = ((xToRotate - xCenterOfRotation) * sinCCW) + ((yToRotate - yCenterOfRotation) * cosCCW) + yCenterOfRotation;
        double xRotated = temp;

        x = (float) xRotated;
        y = (float) yRotated;
    }

    public void toggleClockwise() {
        clockwise = !clockwise;
    }

    public void setEntityToOrbit(Entity entityToOrbit) {
        this.entityToOrbit = entityToOrbit;
    }
}
