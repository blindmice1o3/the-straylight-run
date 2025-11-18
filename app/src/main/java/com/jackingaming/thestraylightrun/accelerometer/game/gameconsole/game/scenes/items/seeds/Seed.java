package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.seeds;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;

public abstract class Seed extends Item
        implements TileCommandOwner {
    public static final String TAG = Seed.class.getSimpleName();

    protected TileCommand tileCommand;

    @Override
    public void init(Game game) {
        super.init(game);

        tileCommand = new SeedGrowableTileCommand(null, name);
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 717, 363, 242, 272);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
