package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class CornSeed extends Seed {
    public static final String TAG = CornSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 20f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_corn);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
