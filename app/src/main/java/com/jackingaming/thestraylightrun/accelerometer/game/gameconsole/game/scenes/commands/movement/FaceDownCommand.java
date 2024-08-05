package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class FaceDownCommand
        implements Command {
    public static final String TAG = FaceDownCommand.class.getSimpleName();

    private Robot robot;

    public FaceDownCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void execute() {
        robot.setDirection(Creature.Direction.DOWN);
    }
}
