package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;

public class PlantInspectioner200 extends Item
        implements EntityCommandOwner {
    public static final String TAG = PlantInspectioner200.class.getSimpleName();
    private static final String NAME_DEFAULT = "Plant Inspectioner 200";
    private static final float PRICE_DEFAULT = -1f;

    private EntityCommand entityCommand;

    public PlantInspectioner200(EntityCommand entityCommand) {
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
        image = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.ic_menu_find);
    }

    @Override
    public EntityCommand getEntityCommand() {
        return entityCommand;
    }
}
