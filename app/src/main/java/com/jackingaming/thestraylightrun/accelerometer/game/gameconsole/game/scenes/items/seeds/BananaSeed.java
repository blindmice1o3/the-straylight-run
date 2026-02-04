package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class BananaSeed extends Seed {
    public static final String TAG = BananaSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 300f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_banana);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
