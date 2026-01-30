package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

public class PlaceInShippingBinTileCommand
        implements TileCommand {
    public static final String TAG = PlaceInShippingBinTileCommand.class.getSimpleName();

    transient private Game game;
    private Tile tile;
    private Creature creatureHarvester;

    public PlaceInShippingBinTileCommand(Tile tile, Creature creatureHarvester) {
        this.tile = tile;
        this.creatureHarvester = creatureHarvester;
    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public boolean execute() {
        if (tile instanceof ShippingBinTile) {
            creatureHarvester.placeInShippingBin();
            return true;
        }

        return false;
    }
}
