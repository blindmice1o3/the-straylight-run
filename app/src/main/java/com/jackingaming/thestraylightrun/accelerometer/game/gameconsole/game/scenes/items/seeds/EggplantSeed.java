package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import com.jackingaming.thestraylightrun.R;

public class EggplantSeed extends Seed {
    public static final String TAG = EggplantSeed.class.getSimpleName();
    private static final float PRICE_DEFAULT = 25f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_seed_eggplant);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }
}
