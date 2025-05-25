package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableIndoorTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class TillGrowableIndoorTileCommand
        implements TileCommand {
    public static final String TAG = TillGrowableIndoorTileCommand.class.getSimpleName();

    private Tile tile;

    public TillGrowableIndoorTileCommand(Tile tile) {
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
            Log.e(TAG, "growableIndoorTile has state: " + growableIndoorTile.getState());
            if (growableIndoorTile.getState() == GrowableTile.State.UNTILLED) {
                Log.e(TAG, "growableIndoorTile.changeToTilled()");
                growableIndoorTile.changeToTilled();
                return true;
            }
        }
        Log.e(TAG, "tile is NOT GrowableIndoorTile... tile's id: " + tile.getId());
        return false;
    }
}
