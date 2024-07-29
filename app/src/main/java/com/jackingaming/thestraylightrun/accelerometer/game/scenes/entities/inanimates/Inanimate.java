package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.inanimates;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

import java.util.Map;

public class Inanimate extends Entity {
    public static final String TAG = Inanimate.class.getSimpleName();

    private String id;

    public Inanimate(String id, Map<Direction, AnimationDrawable> animationsByDirection, CollisionListener collisionListener, MovementListener movementListener) {
        super(animationsByDirection, collisionListener, movementListener);

        this.id = id;
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update(Handler handler) {

    }
}
