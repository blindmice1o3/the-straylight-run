package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class WalkDownTileCommand implements TileCommand {
    public static final String TAG = WalkDownTileCommand.class.getSimpleName();

    private Robot robot;

    public WalkDownTileCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void setTile(Tile tile) {
        // intentionally blank.
    }

    @Override
    public void execute() {
        robot.setDirection(Creature.Direction.DOWN);
        robot.prepareMoveDown();
        robot.move();
    }
}
