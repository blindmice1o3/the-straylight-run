package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;

public class GrowSystemParts extends Item {
    public static final String TAG = GrowSystemParts.class.getSimpleName();
    private static final String NAME_DEFAULT = "Grow System Parts";
    private static final float PRICE_DEFAULT = -1f;

    private int id;

    public GrowSystemParts(int id) {
        this.id = id;
    }

    @Override
    void initName() {
        name = NAME_DEFAULT;
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm2_spritesheet_items);
        if (id == 1) {
            image = Bitmap.createBitmap(spriteSheet, 35, 103, 16, 16);
        } else if (id == 2) {
            image = Bitmap.createBitmap(spriteSheet, 52, 103, 16, 16);
        } else if (id == 3) {
            image = Bitmap.createBitmap(spriteSheet, 69, 103, 16, 16);
        } else if (id == 4) {
            image = Bitmap.createBitmap(spriteSheet, 86, 103, 16, 16);
        }
    }
}
