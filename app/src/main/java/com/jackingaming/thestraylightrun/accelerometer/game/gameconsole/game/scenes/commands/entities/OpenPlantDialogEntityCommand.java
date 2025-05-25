package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;

public class OpenPlantDialogEntityCommand
        implements EntityCommand {
    public static final String TAG = OpenPlantDialogEntityCommand.class.getSimpleName();

    private Entity entity;

    public OpenPlantDialogEntityCommand(Entity entity) {
        this.entity = entity;
    }


    @Override
    public boolean execute() {
        if (entity instanceof Plant) {
            ((Plant) entity).showPlantDialogFragment();
        }
        return false;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
