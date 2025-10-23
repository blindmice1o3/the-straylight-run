package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Consumer;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.Assets;

public class Meat extends Item
        implements Consumeable {
    private static final float PRICE_DEFAULT = -1f;
    private static final int REWARD_EXPERIENCE_POINTS = 125;
    private static final int REWARD_HEALTH = 2;

    @Override
    void initName() {
        name = game.getContext().getString(R.string.text_meat);
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        image = Assets.meat;
    }

    @Override
    public void integrateWithHost(Consumer consumer) {
        consumer.incrementExperiencePoints(REWARD_EXPERIENCE_POINTS);
        consumer.incrementHealth(REWARD_HEALTH);
    }
}