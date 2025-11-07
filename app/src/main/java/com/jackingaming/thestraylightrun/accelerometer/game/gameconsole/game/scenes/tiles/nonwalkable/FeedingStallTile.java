package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Fodder;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class FeedingStallTile extends Tile {
    public static final String TAG = FeedingStallTile.class.getSimpleName();

    private boolean occupied;
    private Bitmap imageDefaultBackground;

    public FeedingStallTile(String id) {
        super(id);
    }

    public void startNewDay() {
        Log.e(TAG, "startNewDay()");

        resetFodder();
    }

    @Override
    public void init(Game game, int xIndex, int yIndex, Bitmap image) {
        super.init(game, xIndex, yIndex, image);

        imageDefaultBackground = image;
    }

    public void acceptFodder(Fodder fodder) {
        occupied = true;

        Bitmap imageFodder = fodder.getImage();
        Bitmap tileSpriteAndFodder = Bitmap.createBitmap(imageDefaultBackground.getWidth(), imageDefaultBackground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tileSpriteAndFodder);
        Rect srcRect = new Rect(0, 0, imageFodder.getWidth(), imageFodder.getHeight());
        Rect dstRect = new Rect(0, 0, tileSpriteAndFodder.getWidth(), tileSpriteAndFodder.getHeight());

        canvas.drawBitmap(imageDefaultBackground, 0, 0, null);
        canvas.drawBitmap(imageFodder, srcRect, dstRect, null);

        image = tileSpriteAndFodder;
    }

    public void resetFodder() {
        occupied = false;

        image = imageDefaultBackground;
    }

    public boolean isOccupied() {
        return occupied;
    }
}
