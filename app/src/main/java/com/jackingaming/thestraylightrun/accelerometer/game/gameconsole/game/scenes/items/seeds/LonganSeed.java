package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class LonganSeed extends Seed {
    public static final String TAG = LonganSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 9000f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_longan);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
