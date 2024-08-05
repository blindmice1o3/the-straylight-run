package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class SeedGrowableTileCommand
        implements TileCommand {
    public static final String TAG = SeedGrowableTileCommand.class.getSimpleName();

    private Tile tile;
    private final String idSeed;

    public SeedGrowableTileCommand(Tile tile, String idSeed) {
        this.tile = tile;
        this.idSeed = idSeed;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void execute() {
        if (tile instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tile;
            Log.e(TAG, "growableTile has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.TILLED) {
                Log.e(TAG, "growableTile.changeToUnwatered()");
                growableTile.changeToUnwatered();
                Log.e(TAG, "growableTile.changeToSeeded()");
                growableTile.changeToSeeded(idSeed);
            }
        } else {
            Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tile.getId());
        }
    }
}
