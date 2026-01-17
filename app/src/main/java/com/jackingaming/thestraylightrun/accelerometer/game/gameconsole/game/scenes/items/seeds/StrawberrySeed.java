package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class StrawberrySeed extends Seed {
    public static final String TAG = StrawberrySeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 20f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_strawberry);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
