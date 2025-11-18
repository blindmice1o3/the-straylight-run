package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class LemongrassSeed extends Seed {
    public static final String TAG = LemongrassSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 5f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_lemongrass);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
