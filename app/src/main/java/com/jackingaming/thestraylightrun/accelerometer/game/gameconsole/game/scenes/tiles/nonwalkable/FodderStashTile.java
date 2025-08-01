package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Fodder;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class FodderStashTile extends Tile {
    public static final String TAG = FodderStashTile.class.getSimpleName();

    private static int numberOfFodder = 42;

    public FodderStashTile(String id) {
        super(id);
    }

    public Fodder generateFodder() {
        if (numberOfFodder > 0) {
            numberOfFodder--;

            return new Fodder();
        } else {
            Log.e(TAG, "numberOfFodder <= 0");

            return null;
        }
    }
}
