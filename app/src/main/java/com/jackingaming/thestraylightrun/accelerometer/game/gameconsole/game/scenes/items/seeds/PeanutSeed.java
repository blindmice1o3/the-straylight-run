package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class PeanutSeed extends Seed {
    public static final String TAG = PeanutSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 13f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_peanut);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
