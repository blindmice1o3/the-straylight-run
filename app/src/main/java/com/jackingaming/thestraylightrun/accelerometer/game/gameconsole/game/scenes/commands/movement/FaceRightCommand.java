package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class FaceRightCommand
        implements Command {
    public static final String TAG = FaceRightCommand.class.getSimpleName();

    private Robot robot;

    public FaceRightCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void execute() {
        robot.setDirection(Creature.Direction.RIGHT);
    }
}
