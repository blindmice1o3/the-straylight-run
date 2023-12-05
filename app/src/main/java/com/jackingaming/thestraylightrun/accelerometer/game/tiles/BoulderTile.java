package com.jackingaming.thestraylightrun.accelerometer.game.tiles;

import android.graphics.Bitmap;

public class BoulderTile extends Tile {

    public BoulderTile(Bitmap texture, int id) {
        super(texture, id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
