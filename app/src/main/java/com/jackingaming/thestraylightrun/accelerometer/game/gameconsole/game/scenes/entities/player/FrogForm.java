package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Damageable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class FrogForm
        implements Form, Damageable {
    transient private Game game;
    private Player player;

    public FrogForm(Player player) {
        this.player = player;
    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {

    }

    @Override
    public void interpretInput() {

    }

    @Override
    public void determineNextImage() {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void takeDamage(int incomingDamage) {

    }

    @Override
    public void die() {

    }
}