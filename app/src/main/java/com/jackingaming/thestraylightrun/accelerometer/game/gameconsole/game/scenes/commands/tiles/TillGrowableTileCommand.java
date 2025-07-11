package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableIndoorTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class TillGrowableTileCommand
        implements TileCommand {
    public static final String TAG = TillGrowableTileCommand.class.getSimpleName();

    private Tile tile;

    public TillGrowableTileCommand(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public boolean execute() {
        if (tile instanceof GrowableIndoorTile) {
            return false;
        } else if (tile instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tile;
            Log.e(TAG, "growableTile has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.UNTILLED) {
                Log.e(TAG, "growableTile.changeToTilled()");
                growableTile.changeToTilled();
                return true;
            }
        }
        Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tile.getId());
        return false;
    }
}
