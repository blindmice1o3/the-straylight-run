package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.Random;

public class BubbledState
        implements State {
    public static final String TAG = BubbledState.class.getSimpleName();

    private Game game;
    private Monsta monsta;

    private Bitmap[] monstaBubbledGreen;
    private Bitmap[] monstaBubbledBlue;
    private Bitmap[] monstaBubbledOrange;
    private Bitmap[] monstaBubbledRed;
    private int indexMonstaBubbled;

    private int counterFrame;
    private int counterFrameTarget;
    private float timerEscape;
    private float timerEscapeTarget;

    @Override
    public void enter() {
        Log.e(TAG, "enter()");
        monsta.setBubbled(true);
    }

    @Override
    public void exit() {
        Log.e(TAG, "exit()");
        monsta.setBubbled(false);
    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Monsta) {
            monsta = (Monsta) e;
        }

        counterFrame = 0;
        counterFrameTarget = 30;
        timerEscape = 0;
        timerEscapeTarget = 6000;

        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexMonstaBubbled = 0;
        int numberOfFrames = 3;
        int numberOfColors = 4;
        monstaBubbledGreen = new Bitmap[numberOfFrames];
        monstaBubbledBlue = new Bitmap[numberOfFrames];
        monstaBubbledOrange = new Bitmap[numberOfFrames];
        monstaBubbledRed = new Bitmap[numberOfFrames];
        for (int i = 0; i < (numberOfFrames * numberOfColors); i++) {
            int y = 333;
            int x = 267 + (i * (16 + 2));

            if (i >= 0 && i <= 2) {
                int index = i % 3;
                monstaBubbledGreen[index] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            } else if (i >= 3 && i <= 5) {
                int index = i % 3;
                monstaBubbledBlue[index] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            } else if (i >= 6 && i <= 8) {
                int index = i % 3;
                monstaBubbledOrange[index] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            } else if (i >= 9 && i <= 11) {
                int index = i % 3;
                monstaBubbledRed[index] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            }
        }
    }

    @Override
    public void update(long elapsed) {
        // check escape timer
        timerEscape += elapsed;
        Log.e(TAG, "timerEscape: " + timerEscape);

        if (timerEscape >= timerEscapeTarget) {
            timerEscape = 0;

            monsta.becomeUnbubbled();
        }

        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexMonstaBubbled++;
            if (indexMonstaBubbled >= monstaBubbledGreen.length) {
                indexMonstaBubbled = 0;
            }

            if (timerEscape >= 0 && timerEscape < (0.50 * timerEscapeTarget)) {
                monsta.setImage(monstaBubbledGreen[indexMonstaBubbled]);
            } else {
                Random random = new Random();
                int randomColor = random.nextInt(3);
                if (randomColor == 0) {
                    monsta.setImage(monstaBubbledBlue[indexMonstaBubbled]);
                } else if (randomColor == 1) {
                    monsta.setImage(monstaBubbledOrange[indexMonstaBubbled]);
                } else if (randomColor == 2) {
                    monsta.setImage(monstaBubbledRed[indexMonstaBubbled]);
                }
            }

            counterFrame = 0;
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
