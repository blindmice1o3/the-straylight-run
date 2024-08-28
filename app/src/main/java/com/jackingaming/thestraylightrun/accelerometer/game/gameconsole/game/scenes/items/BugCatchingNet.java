package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;

public class BugCatchingNet extends Item
        implements EntityCommandOwner {
    private static final String NAME_DEFAULT = "Bug Catching Net";
    private static final float PRICE_DEFAULT = 3f;

    private EntityCommand entityCommand;

    public BugCatchingNet(EntityCommand entityCommand) {
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
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm2_spritesheet_items);
        image = Bitmap.createBitmap(spriteSheet, 103, 52, 16, 16);
    }

    @Override
    public EntityCommand getEntityCommand() {
        return entityCommand;
    }
}