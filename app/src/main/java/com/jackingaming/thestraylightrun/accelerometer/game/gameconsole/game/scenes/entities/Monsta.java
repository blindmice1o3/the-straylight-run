package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Monsta extends Entity {
    public static final String TAG = Monsta.class.getSimpleName();

    private boolean bubbled;

    private int counterFrame;
    private int counterFrameTarget;

    private Bitmap[] monstaPatrol;
    private int indexMonstaPatrol;
    private Bitmap[] monstaBubbled;
    private int indexMonstaBubbled;

    public Monsta(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        bubbled = false;
        counterFrame = 0;
        counterFrameTarget = 30;

        // Monsta [Entity]
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexMonstaPatrol = 0;
        monstaPatrol = new Bitmap[2];
        for (int i = 0; i < monstaPatrol.length; i++) {
            int y = 333;
            int x = 6 + (i * (16 + 5));

            monstaPatrol[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }

        indexMonstaBubbled = 0;
        monstaBubbled = new Bitmap[3];
        for (int i = 0; i < monstaBubbled.length; i++) {
            int y = 333;
            int x = 267 + (i * (16 + 2));

            monstaBubbled[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }

        image = monstaPatrol[0];
    }

    public void becomeBubbled() {
        bubbled = true;
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;

        if (counterFrame >= counterFrameTarget) {

            if (bubbled) {
                indexMonstaBubbled++;
                if (indexMonstaBubbled >= monstaBubbled.length) {
                    indexMonstaBubbled = 0;
                }

                image = monstaBubbled[indexMonstaBubbled];
            } else {
                indexMonstaPatrol++;
                if (indexMonstaPatrol >= monstaPatrol.length) {
                    indexMonstaPatrol = 0;
                }

                image = monstaPatrol[indexMonstaPatrol];
            }

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
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
