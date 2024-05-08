package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import java.util.Map;

public class Coin extends Entity {

    public Coin(Map<Direction, Animation> sprites,
                CollisionListener collisionListener, MovementListener movementListener) {
        super(sprites, collisionListener, movementListener);

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
