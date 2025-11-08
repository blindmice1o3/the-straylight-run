package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Cheese;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Milk;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

public class CheeseMakerTile extends Tile {
    public static final String TAG = CheeseMakerTile.class.getSimpleName();
    public static final int DAYS_REQUIRED_TO_PROCESS = 3;

    private static int daysProcessed;
    private static boolean availableToProcess = true;
    private static Milk milkToProcessIntoCheese;
    private Bitmap imageWithoutMilk;

    public CheeseMakerTile(String id) {
        super(id);
    }

    @Override
    public void init(Game game, int xIndex, int yIndex, Bitmap image) {
        super.init(game, xIndex, yIndex, image);

        imageWithoutMilk = image;
    }

    public static Cheese startNewDay(TileManager tileManager,
                                     CheeseMakerTile tileTopLeft,
                                     CheeseMakerTile tileTopRight) {
        Log.d(TAG, "startNewDay()");

        if (!availableToProcess) {
            Log.d(TAG, "!availableToProcess");

            daysProcessed++;

            if (daysProcessed >= DAYS_REQUIRED_TO_PROCESS) {
                return processMilkIntoCheese(
                        tileManager,
                        tileTopLeft,
                        tileTopRight
                );
            }
        }

        Log.e(TAG, "availableToProcess: returning null.");

        return null;
    }

    private static Cheese processMilkIntoCheese(TileManager tileManager,
                                                CheeseMakerTile tileTopLeft,
                                                CheeseMakerTile tileTopRight) {
        Tile[][] tiles = tileManager.getTiles();
        boolean lookingForRandomWalkableTile = true;

        while (lookingForRandomWalkableTile) {
            int xRandom = (int) (Math.random() * tiles[0].length);
            int yRandom = (int) (Math.random() * tiles.length);

            Log.e(TAG, "xRandom: " + xRandom);
            Log.e(TAG, "yRandom: " + yRandom);

            if (tiles[yRandom][xRandom].isWalkable()) {
                ///////////////////////////////
                lookingForRandomWalkableTile = false;
                ///////////////////////////////

                Cheese cheeseJustProcessed = milkToProcessIntoCheese.process(
                        (xRandom * Tile.WIDTH),
                        (yRandom * Tile.HEIGHT)
                );

                daysProcessed = 0;
                availableToProcess = true;
                milkToProcessIntoCheese = null;
                tileTopLeft.setImage(
                        tileTopLeft.getImageWithoutMilk()
                );
                tileTopRight.setImage(
                        tileTopRight.getImageWithoutMilk()
                );

                return cheeseJustProcessed;
            }
        }

        return null;
    }

    public void startProcessingMilkIntoCheese(Milk milkToProcessIntoCheese, CheeseMakerTile tileTopLeft, CheeseMakerTile tileTopRight) {
        Log.d(TAG, "startProcessingMilkIntoCheese()");

        availableToProcess = false;
        this.milkToProcessIntoCheese = milkToProcessIntoCheese;

        Bitmap imageMilk = milkToProcessIntoCheese.getImage();

        Bitmap tileTopLeftNoMilk = tileTopLeft.getImage();
        Bitmap tileTopLeftYesMilk = Bitmap.createBitmap(tileTopLeftNoMilk.getWidth(), tileTopLeftNoMilk.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasTopLeft = new Canvas(tileTopLeftYesMilk);
        Rect srcRectTopLeft = new Rect(0, 0, (imageMilk.getWidth() / 2), imageMilk.getHeight());
        Rect dstRectTopLeft = new Rect((tileTopLeftNoMilk.getWidth() / 2), 0, tileTopLeftNoMilk.getWidth(), tileTopLeftNoMilk.getHeight());
        canvasTopLeft.drawBitmap(tileTopLeftNoMilk, 0, 0, null);
        canvasTopLeft.drawBitmap(imageMilk, srcRectTopLeft, dstRectTopLeft, null);
        tileTopLeft.setImage(tileTopLeftYesMilk);

        Bitmap tileTopRightNoMilk = tileTopRight.getImage();
        Bitmap tileTopRightYesMilk = Bitmap.createBitmap(tileTopRightNoMilk.getWidth(), tileTopRightNoMilk.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasTopRight = new Canvas(tileTopRightYesMilk);
        Rect srcRectTopRight = new Rect((imageMilk.getWidth() / 2), 0, imageMilk.getWidth(), imageMilk.getHeight());
        Rect dstRectTopRight = new Rect(0, 0, (tileTopRightNoMilk.getWidth() / 2), tileTopRightNoMilk.getHeight());
        canvasTopRight.drawBitmap(tileTopRightNoMilk, 0, 0, null);
        canvasTopRight.drawBitmap(imageMilk, srcRectTopRight, dstRectTopRight, null);
        tileTopRight.setImage(tileTopRightYesMilk);
    }

    public static boolean isAvailableToProcess() {
        return availableToProcess;
    }

    public Bitmap getImageWithoutMilk() {
        return imageWithoutMilk;
    }
}
