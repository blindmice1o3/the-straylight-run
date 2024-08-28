package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;

public interface EntityCommand extends Command {
    void setEntity(Entity entity);
}
