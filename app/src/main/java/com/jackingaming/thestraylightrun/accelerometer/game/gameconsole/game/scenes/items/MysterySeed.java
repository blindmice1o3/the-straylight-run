package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.TileCommand;

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
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm_seeds_shop);
        image = Bitmap.createBitmap(spriteSheet, 156, 150, 16, 16);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
