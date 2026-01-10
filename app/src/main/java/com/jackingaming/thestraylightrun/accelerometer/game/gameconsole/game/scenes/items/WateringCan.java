package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;

public class WateringCan extends Item
        implements TileCommandOwner {
    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public WateringCan(TileCommand tileCommand) {
        this.tileCommand = tileCommand;
    }

    @Override
    public void init(Game game) {
        super.init(game);

        tileCommand.init(game);
    }

    @Override
    protected void initName() {
        name = game.getContext().getString(R.string.text_watering_can);
    }

    @Override
    protected void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    protected void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_and_tiles);
        image = Bitmap.createBitmap(spriteSheet, 61, 381, 274, 236);
//        image = Bitmap.createBitmap(spriteSheet, 60, 379, 277, 240);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}
