package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class HarvestGrowableTileCommand
        implements TileCommand {
    public static final String TAG = HarvestGrowableTileCommand.class.getSimpleName();

    private Tile tile;
    private Creature creatureHarvester;

    public HarvestGrowableTileCommand(Tile tile, Creature creatureHarvester) {
        this.tile = tile;
        this.creatureHarvester = creatureHarvester;
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
            if (growableTile.getState() == GrowableTile.State.OCCUPIED) {
//                Log.e(TAG, "growableTile.changeToWatered()");
                Entity entityToHarvest = growableTile.getEntity();
                if (entityToHarvest instanceof Plant) {
                    if (((Plant) entityToHarvest).isHarvestable()) {
                        creatureHarvester.pickUp(entityToHarvest);
                        growableTile.changeToUntilled();
                        creatureHarvester.setDirection(Creature.Direction.UP);
                        creatureHarvester.placeDown();
                        return true;
                    }
                }
            }
        }
        Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tile.getId());
        return false;
    }
}
