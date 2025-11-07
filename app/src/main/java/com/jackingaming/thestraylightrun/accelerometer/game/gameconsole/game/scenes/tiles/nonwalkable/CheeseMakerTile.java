package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Milk;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class CheeseMakerTile extends Tile {
    public static final String TAG = CheeseMakerTile.class.getSimpleName();

    private Milk milkToProcessIntoCheese;
    private int daysProcessed;

    public CheeseMakerTile(String id) {
        super(id);
    }

    public void startProcessingMilkIntoCheese(Milk milkToProcessIntoCheese) {
        this.milkToProcessIntoCheese = milkToProcessIntoCheese;
        daysProcessed = 0;
    }
}
