package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class Shovel extends Item {

    public Shovel() {
        super();
        name = "Shovel";
    }

    @Override
    public void init(Game game) {
        super.init(game);
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm2_spritesheet_items);
        image = Bitmap.createBitmap(spriteSheet, 35, 52, 16, 16);
    }
}