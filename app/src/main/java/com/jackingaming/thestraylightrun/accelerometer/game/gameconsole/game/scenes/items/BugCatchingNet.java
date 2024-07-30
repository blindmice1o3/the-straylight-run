package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class BugCatchingNet extends Item {
    private static final float PRICE_DEFAULT = 3f;

    public BugCatchingNet() {
        super();
        name = "Bug Catching Net";
        price = PRICE_DEFAULT;
    }

    @Override
    public void init(Game game) {
        super.init(game);
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm2_spritesheet_items);
        image = Bitmap.createBitmap(spriteSheet, 103, 52, 16, 16);
    }
}