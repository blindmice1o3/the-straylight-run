package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;

public class Shovel extends Item
        implements TileCommandOwner {
    private static final String NAME_DEFAULT = "Shovel";
    private static final float PRICE_DEFAULT = -1f;

    private TileCommand tileCommand;

    public Shovel(TileCommand tileCommand) {
        this.tileCommand = tileCommand;
    }

    @Override
    void initName() {
        name = NAME_DEFAULT;
    }

    @Override
    void initPrice() {
        price = PRICE_DEFAULT;
    }

    @Override
    void initImage() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gbc_hm2_spritesheet_items);
        image = Bitmap.createBitmap(spriteSheet, 35, 52, 16, 16);
    }

    @Override
    public TileCommand getTileCommand() {
        return tileCommand;
    }
}