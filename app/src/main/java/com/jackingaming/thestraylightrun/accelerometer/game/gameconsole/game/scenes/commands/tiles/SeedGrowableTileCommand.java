package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

public class SeedGrowableTileCommand
        implements TileCommand {
    public static final String TAG = SeedGrowableTileCommand.class.getSimpleName();

    private Game game;
    private Tile tile;
    private String idSeed;

    public SeedGrowableTileCommand(Tile tile, String idSeed) {
        this.tile = tile;
        if (idSeed != null) {
            this.idSeed = idSeed;
        }
    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    public void setIdSeed(String idSeed) {
        this.idSeed = idSeed;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public boolean execute() {
        if (tile instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tile;
            Log.e(TAG, "growableTile has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.TILLED) {
                Log.e(TAG, "growableTile.changeToUnwatered()");
                growableTile.changeToUnwatered();
                Log.e(TAG, "growableTile.changeToSeeded()");
                growableTile.changeToSeeded(idSeed);
                game.playSFX(SoundManager.sfxSow);
                return true;
            }
        }
        Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tile.getId());
        return false;
    }
}
