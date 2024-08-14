package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class WalkRightCommand implements Command {
    public static final String TAG = WalkRightCommand.class.getSimpleName();

    private Robot robot;

    public WalkRightCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public boolean execute() {
        robot.setDirection(Creature.Direction.RIGHT);
        robot.prepareMoveRight();
        return robot.move();
    }
}
