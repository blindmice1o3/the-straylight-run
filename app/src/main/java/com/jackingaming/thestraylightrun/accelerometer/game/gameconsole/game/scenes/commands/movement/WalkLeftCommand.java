package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.movement;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

public class WalkLeftCommand implements Command {
    public static final String TAG = WalkLeftCommand.class.getSimpleName();

    private Robot robot;

    public WalkLeftCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void execute() {
        robot.setDirection(Creature.Direction.LEFT);
        robot.prepareMoveLeft();
        robot.move();
    }
}
