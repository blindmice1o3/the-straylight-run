package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;

public class WateringCan extends Item
        implements TileCommandOwner {
    private static final String NAME_DEFAULT = "Watering Can";
    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public WateringCan(TileCommand tileCommand) {
        this.tileCommand = tileCommand;
    }

    @Override
    void initName() {
        name = NAME_DEFAULT;
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 61, 381, 274, 236);
//        image = Bitmap.createBitmap(spriteSheet, 60, 379, 277, 240);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
