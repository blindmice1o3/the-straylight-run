package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Egg;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

public class EggIncubatorTile extends Tile {
    public static final String TAG = EggIncubatorTile.class.getSimpleName();
    public static final int DAYS_INCUBATED_REQUIRED_TO_HATCH = 3;

    private static int daysIncubated;
    private static boolean availableToIncubate = true;
    private static Egg eggToIncubateIntoChicken;
    private Bitmap imageWithoutEgg;

    public EggIncubatorTile(String id) {
        super(id);
    }

    @Override
    public void init(Game game, int xIndex, int yIndex, Bitmap image) {
        super.init(game, xIndex, yIndex, image);

        imageWithoutEgg = image;
    }

    public static AimlessWalker startNewDay(TileManager tileManager,
                                            EggIncubatorTile tileTopLeft,
                                            EggIncubatorTile tileTopRight) {
        Log.d(TAG, "startNewDay()");

        if (!availableToIncubate) {
            Log.d(TAG, "!availableToIncubate");

            daysIncubated++;

            if (daysIncubated >= DAYS_INCUBATED_REQUIRED_TO_HATCH) {
                return hatchEggToIncubateIntoChicken(
                        tileManager,
                        tileTopLeft,
                        tileTopRight
                );
            }
        }

        Log.e(TAG, "availableToIncubate: returning null.");

        return null;
    }

    private static AimlessWalker hatchEggToIncubateIntoChicken(TileManager tileManager,
                                                               EggIncubatorTile tileTopLeft,
                                                               EggIncubatorTile tileTopRight) {
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

                AimlessWalker chickJustHatched = eggToIncubateIntoChicken.hatch(
                        (xRandom * Tile.WIDTH),
                        (yRandom * Tile.HEIGHT)
                );

                daysIncubated = 0;
                availableToIncubate = true;
                eggToIncubateIntoChicken = null;
                tileTopLeft.setImage(
                        tileTopLeft.getImageWithoutEgg()
                );
                tileTopRight.setImage(
                        tileTopRight.getImageWithoutEgg()
                );

                return chickJustHatched;
            }
        }

        return null;
    }

    public void startIncubatingEggIntoChicken(Egg eggToIncubateIntoChicken, EggIncubatorTile tileTopLeft, EggIncubatorTile tileTopRight) {
        Log.d(TAG, "startIncubatingEggIntoChicken()");

        availableToIncubate = false;
        this.eggToIncubateIntoChicken = eggToIncubateIntoChicken;

        Bitmap imageEgg = eggToIncubateIntoChicken.getImage();

        Bitmap tileTopLeftNoEgg = tileTopLeft.getImage();
        Bitmap tileTopLeftYesEgg = Bitmap.createBitmap(tileTopLeftNoEgg.getWidth(), tileTopLeftNoEgg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasTopLeft = new Canvas(tileTopLeftYesEgg);
        Rect srcRectTopLeft = new Rect(0, 0, (imageEgg.getWidth() / 2), imageEgg.getHeight());
        Rect dstRectTopLeft = new Rect((tileTopLeftNoEgg.getWidth() / 2), 0, tileTopLeftNoEgg.getWidth(), tileTopLeftNoEgg.getHeight());
        canvasTopLeft.drawBitmap(tileTopLeftNoEgg, 0, 0, null);
        canvasTopLeft.drawBitmap(imageEgg, srcRectTopLeft, dstRectTopLeft, null);
        tileTopLeft.setImage(tileTopLeftYesEgg);

        Bitmap tileTopRightNoEgg = tileTopRight.getImage();
        Bitmap tileTopRightYesEgg = Bitmap.createBitmap(tileTopRightNoEgg.getWidth(), tileTopRightNoEgg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasTopRight = new Canvas(tileTopRightYesEgg);
        Rect srcRectTopRight = new Rect((imageEgg.getWidth() / 2), 0, imageEgg.getWidth(), imageEgg.getHeight());
        Rect dstRectTopRight = new Rect(0, 0, (tileTopRightNoEgg.getWidth() / 2), tileTopRightNoEgg.getHeight());
        canvasTopRight.drawBitmap(tileTopRightNoEgg, 0, 0, null);
        canvasTopRight.drawBitmap(imageEgg, srcRectTopRight, dstRectTopRight, null);
        tileTopRight.setImage(tileTopRightYesEgg);
    }

    public Bitmap getImageWithoutEgg() {
        return imageWithoutEgg;
    }

    public void setImageWithoutEgg(Bitmap imageWithoutEgg) {
        this.imageWithoutEgg = imageWithoutEgg;
    }

    public static boolean isAvailableToIncubate() {
        return availableToIncubate;
    }
}
