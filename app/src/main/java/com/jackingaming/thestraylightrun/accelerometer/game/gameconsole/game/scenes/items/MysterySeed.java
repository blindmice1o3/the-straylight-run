package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;

public class MysterySeed extends Item {
    private static final String NAME_DEFAULT = "Mystery Seed";
    private static final float PRICE_DEFAULT = -1f;

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
}
