package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;

public class Scissors extends Item
        implements EntityCommandOwner {
    private static final String NAME_DEFAULT = "Scissors";
    private static final float PRICE_DEFAULT = -1f;

    private EntityCommand entityCommand;

    public Scissors(EntityCommand entityCommand) {
        this.entityCommand = entityCommand;
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
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 395, 671, 245, 257);
//        image = Bitmap.createBitmap(spriteSheet, 393, 669, 250, 262);
    }

    @Override
    public EntityCommand getEntityCommand() {
        return entityCommand;
    }
}
