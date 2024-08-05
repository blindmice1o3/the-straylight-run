package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.Command;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public interface TileCommand extends Command {
    void setTile(Tile tile);
}
