package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public interface Carryable {
    boolean isCarryable();

    void becomeCarried();

    boolean becomeNotCarried(Tile tileToLandOn);

    void moveWithCarrier(float x, float y);
}
