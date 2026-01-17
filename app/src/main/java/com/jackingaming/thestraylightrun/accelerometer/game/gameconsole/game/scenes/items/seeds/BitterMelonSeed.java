package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class BitterMelonSeed extends Seed {
    public static final String TAG = BitterMelonSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 30f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_bitter_melon);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
