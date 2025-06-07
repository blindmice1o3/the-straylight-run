package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableIndoorTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class WaterGrowableTileCommand
        implements TileCommand {
    public static final String TAG = WaterGrowableTileCommand.class.getSimpleName();

    private Tile tile;

    public WaterGrowableTileCommand(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public boolean execute() {
        if (tile instanceof GrowableIndoorTile) {
            GrowableIndoorTile growableIndoorTile = (GrowableIndoorTile) tile;
            if (growableIndoorTile.getState() == GrowableTile.State.TILLED ||
                    growableIndoorTile.getState() == GrowableTile.State.SEEDED ||
                    growableIndoorTile.getState() == GrowableTile.State.OCCUPIED) {
                Log.e(TAG, "growableIndoorTile.changeToWatered()");
                growableIndoorTile.changeToWatered();

                return true;
            }
        } else if (tile instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tile;
            Log.e(TAG, "growableTile has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.TILLED ||
                    growableTile.getState() == GrowableTile.State.SEEDED ||
                    growableTile.getState() == GrowableTile.State.OCCUPIED) {
                Log.e(TAG, "growableTile.changeToWatered()");
                growableTile.changeToWatered();

                return true;
            }
        }
        Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tile.getId());
        return false;
    }
}
