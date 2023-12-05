package com.jackingaming.thestraylightrun.accelerometer.game.tiles;

import android.graphics.Bitmap;

public class SolidTile extends Tile {

    public SolidTile(Bitmap texture, int id) {
        super(texture, id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
