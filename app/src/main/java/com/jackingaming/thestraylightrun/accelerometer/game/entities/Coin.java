package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.graphics.Bitmap;

import java.util.Map;

public class Coin extends Entity {

    public Coin(Map<Direction, Bitmap> sprites) {
        super(sprites);

        xPos = 200f;
        yPos = 200f;
    }

    @Override
    public void collided(Entity collider) {
        collider.setSpeedBonus(2f);
    }

    @Override
    public void update() {

    }
}
