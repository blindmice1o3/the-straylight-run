package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class HoneyPot extends Item {
    private static final float PRICE_DEFAULT = 1f;

    public HoneyPot() {
        super();
        name = "Honey Pot";
        price = PRICE_DEFAULT;
    }

    @Override
    public void init(Game game) {
        super.init(game);
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gba_kingdom_hearts_chain_of_memories_winnie_the_pooh);
        image = Bitmap.createBitmap(spriteSheet, 318, 1556, 38, 37);
    }
}