package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;

public class GrowSystemParts extends Item {
    public static final String TAG = GrowSystemParts.class.getSimpleName();

    private static final float PRICE_DEFAULT = -1f;

    private int id;

    public GrowSystemParts(int id) {
        this.id = id;
    }

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_grow_system_parts);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_grow_system_parts);
        if (id == 1) {
            image = Bitmap.createBitmap(spriteSheet, 76, 67, 250, 383);
        } else if (id == 2) {
            image = Bitmap.createBitmap(spriteSheet, 424, 67, 198, 383);
        } else if (id == 3) {
            image = Bitmap.createBitmap(spriteSheet, 684, 89, 303, 423);
        } else if (id == 4) {
            image = Bitmap.createBitmap(spriteSheet, 53, 562, 264, 341);
        } else if (id == 5) {
            image = Bitmap.createBitmap(spriteSheet, 365, 540, 300, 407);
        } else if (id == 6) {
            image = Bitmap.createBitmap(spriteSheet, 729, 512, 258, 413);
        }
    }
}
