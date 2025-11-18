package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;

public class Milk extends Item
        implements Sellable {
    private static final float PRICE_DEFAULT = 80f;

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_milk);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_entities_carryable_and_bubbled);
        image = Bitmap.createBitmap(spriteSheet, 562, 62, 133, 274);
    }

    public Cheese process(int xSpawn, int ySpawn) {
        Cheese cheese = new Cheese();
        cheese.init(game);
        cheese.setPosition(xSpawn, ySpawn);
        return cheese;
    }
}