package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;

public class BugCatchingNet extends Item
        implements EntityCommandOwner {
    private static final float PRICE_DEFAULT = 3f;

    private EntityCommand entityCommand;

    public BugCatchingNet(EntityCommand entityCommand) {
        this.entityCommand = entityCommand;
    }

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_bug_catching_net);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 96, 671, 238, 257);
//        image = Bitmap.createBitmap(spriteSheet, 93, 669, 244, 261);
    }

    @Override
    public EntityCommand getEntityCommand() {
        return entityCommand;
    }
}