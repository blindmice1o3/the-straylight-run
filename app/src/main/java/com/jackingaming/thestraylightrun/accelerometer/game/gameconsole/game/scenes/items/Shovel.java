package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;

public class Shovel extends Item
        implements TileCommandOwner {
    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public Shovel(TileCommand tileCommand) {
        this.tileCommand = tileCommand;
    }

    @Override
    void initName() {
        name = game.getContext().getString(R.string.text_shovel);
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 395, 363, 245, 254);
//        image = Bitmap.createBitmap(spriteSheet, 393, 361, 249, 258);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}