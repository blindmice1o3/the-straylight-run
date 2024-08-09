package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class StandStillCommand implements Command {
    public static final String TAG = StandStillCommand.class.getSimpleName();

    private Robot robot;

    public StandStillCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void execute() {
        // intentionally blank.
    }
}
