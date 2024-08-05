package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public interface TileCommand {
    void setTile(Tile tile);

    void execute();
}
