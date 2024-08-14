package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class WalkDownCommand implements Command {
    public static final String TAG = WalkDownCommand.class.getSimpleName();

    private Robot robot;

    public WalkDownCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public boolean execute() {
        robot.setDirection(Creature.Direction.DOWN);
        robot.prepareMoveDown();
        return robot.move();
    }
}
