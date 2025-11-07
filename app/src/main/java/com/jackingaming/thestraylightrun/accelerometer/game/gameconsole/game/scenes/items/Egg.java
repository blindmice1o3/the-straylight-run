package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;

public class Egg extends Item
        implements Sellable {
    public static final float PRICE_DEFAULT = 60f;

    @Override
    void initName() {
        name = game.getContext().getString(R.string.text_egg);
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_entities_carryable_and_bubbled);
        image = Bitmap.createBitmap(spriteSheet, 783, 110, 151, 274);
    }

    public AimlessWalker hatch(int xSpawn, int ySpawn) {
        AimlessWalker chick = new AimlessWalker(AimlessWalker.Type.CHICK, xSpawn, ySpawn);
        chick.init(game);
        return chick;
    }
}
