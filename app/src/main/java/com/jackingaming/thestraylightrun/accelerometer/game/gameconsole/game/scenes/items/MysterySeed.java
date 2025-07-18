package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;

public class MysterySeed extends Item
        implements TileCommandOwner {
    public static final String TAG = MysterySeed.class.getSimpleName();

    private static final String NAME_DEFAULT = "Mystery Seed";
    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public MysterySeed(TileCommand tileCommand) {
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
        image = Bitmap.createBitmap(spriteSheet, 715, 360, 246, 278);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
