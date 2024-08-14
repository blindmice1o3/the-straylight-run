package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TileSelectorView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class TileWorkRequest {
    private Tile tile;
    private TileSelectorView.Mode modeForTileSelectorView;

    public TileWorkRequest(Tile tile, TileSelectorView.Mode modeForTileSelectorView) {
        this.tile = tile;
        this.modeForTileSelectorView = modeForTileSelectorView;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public TileSelectorView.Mode getModeForTileSelectorView() {
        return modeForTileSelectorView;
    }

    public void setModeForTileSelectorView(TileSelectorView.Mode modeForTileSelectorView) {
        this.modeForTileSelectorView = modeForTileSelectorView;
    }
}
