package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class GuavaSeed extends Seed {
    public static final String TAG = GuavaSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 9000f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_guava);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
