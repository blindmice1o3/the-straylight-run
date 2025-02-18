package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.monsta.Monsta;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public interface State {
    void enter();
    void exit();

    void init(Game game, Entity e);
    void update(long elapsed);

    boolean respondToEntityCollision(Entity e);
    void respondToItemCollisionViaClick(Item i);
    void respondToItemCollisionViaMove(Item i);
    void respondToTransferPointCollision(String key);
}
