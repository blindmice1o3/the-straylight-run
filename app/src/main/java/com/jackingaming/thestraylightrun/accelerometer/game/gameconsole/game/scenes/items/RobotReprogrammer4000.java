package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;

public class RobotReprogrammer4000 extends Item
        implements EntityCommandOwner {
    private static final float PRICE_DEFAULT = -1f;

    private EntityCommand entityCommand;

    public RobotReprogrammer4000(EntityCommand entityCommand) {
        this.entityCommand = entityCommand;
    }

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_robot_reprogrammer_4000);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm2_spritesheet_items);
        image = Bitmap.createBitmap(spriteSheet, 137, 103, 16, 16);
    }

    @Override
    public EntityCommand getEntityCommand() {
        return entityCommand;
    }
}
