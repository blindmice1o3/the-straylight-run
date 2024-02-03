package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts;

import android.view.View;

public abstract class Collider {
    public static final String TAG = Collider.class.getSimpleName();

    private boolean colliding, cantCollide, justCollided;

    abstract public void onCollided(View collider);

    public void update(boolean colliding) {
        this.colliding = colliding;
        if (cantCollide && !this.colliding) {
            cantCollide = false;
        } else if (justCollided) {
            cantCollide = true;
            justCollided = false;
        }
        if (!cantCollide && this.colliding) {
            justCollided = true;
        }
    }

    public boolean isJustCollided() {
        return justCollided;
    }
}
