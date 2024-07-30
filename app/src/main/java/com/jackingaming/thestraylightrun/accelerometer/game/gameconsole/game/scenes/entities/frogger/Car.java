package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.frogger;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class Car extends Creature {
    public static final String TAG = Car.class.getSimpleName();
    
    public enum Type {PINK, WHITE, YELLOW, BIG_RIG;}

    private Type type;

    public Car(int xSpawn, int ySpawn, Type type, Direction direction, Bitmap image) {
        super(xSpawn, ySpawn);

        this.type = type;
        if (type == Type.BIG_RIG) {
            int widthDoubled = 2 * width;
            bounds = new Rect(0, 0, widthDoubled, height);
        }

        //Do NOT allow UP, DOWN, or any of the diagonals (default is RIGHT).
        if ((direction == Direction.LEFT) || (direction == Direction.RIGHT)) {
            this.direction = direction;
        } else {
            this.direction = Direction.RIGHT;
        }

        this.image = image;
    }

    @Override
    public void update(long elapsed) {
        xMove = 0;
        yMove = 0;

        switch (direction) {
            case LEFT:
                if ((x - moveSpeed) > 0) {
                    xMove = -moveSpeed;
                } else {
                    active = false;
                }

                break;
            case RIGHT:
                int widthSceneMax = game.getSceneManager().getCurrentScene().getTileManager().getWidthScene();
                if ((x + width + moveSpeed) < widthSceneMax) {
                    xMove = moveSpeed;
                } else {
                    active = false;
                }

                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".update(long elapsed) switch (direction)'s default.");
                break;
        }

        move();
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
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