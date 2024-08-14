package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class FaceUpCommand
        implements Command {
    public static final String TAG = FaceUpCommand.class.getSimpleName();

    private Robot robot;

    public FaceUpCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public boolean execute() {
        robot.setDirection(Creature.Direction.UP);
        return true;
    }
}
