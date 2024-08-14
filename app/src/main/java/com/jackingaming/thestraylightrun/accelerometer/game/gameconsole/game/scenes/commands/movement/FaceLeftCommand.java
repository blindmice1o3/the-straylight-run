package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class FaceLeftCommand
        implements Command {
    public static final String TAG = FaceLeftCommand.class.getSimpleName();

    private Robot robot;

    public FaceLeftCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public boolean execute() {
        robot.setDirection(Creature.Direction.LEFT);
        return true;
    }
}
