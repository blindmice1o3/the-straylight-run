package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableTileCommand;

public class Shovel extends Item
        implements TileCommandOwner {
    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public Shovel(TileCommand tileCommand) {
        this.tileCommand = tileCommand;
    }

    @Override
    public void init(Game game) {
        super.init(game);

        tileCommand.init(game);
    }

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_shovel);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 395, 363, 245, 254);
//        image = Bitmap.createBitmap(spriteSheet, 393, 361, 249, 258);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}