package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;

public class HoneyPot extends Item {
    private static final String NAME_DEFAULT = "Honey Pot";
    private static final float PRICE_DEFAULT = 1f;

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
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gba_kingdom_hearts_chain_of_memories_winnie_the_pooh);
        image = Bitmap.createBitmap(spriteSheet, 318, 1556, 38, 37);
    }
}