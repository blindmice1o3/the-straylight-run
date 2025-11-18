package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class PapayaSeed extends Seed {
    public static final String TAG = PapayaSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 5f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_papaya);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
