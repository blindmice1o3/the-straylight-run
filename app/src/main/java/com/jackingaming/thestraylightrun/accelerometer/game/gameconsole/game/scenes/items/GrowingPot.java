package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableIndoorTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class GrowingPot extends Item
        implements TileCommandOwner {
    public static final String TAG = GrowingPot.class.getSimpleName();

    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public GrowingPot(TileCommand tileCommand) {
        this.tileCommand = tileCommand;
    }

    @Override
    void initName() {
        name = game.getContext().getString(R.string.text_growing_pot);
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        image = GrowableIndoorTile.cropGrowableTableTile(game.getContext().getResources(),
                GrowableTile.State.TILLED, false);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
