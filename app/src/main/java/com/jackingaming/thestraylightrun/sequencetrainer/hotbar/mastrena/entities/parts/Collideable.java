package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts;

import android.view.View;

public interface Collideable {
    void update(boolean colliding);

    boolean isJustCollided();

    void onCollided(View collider);
}
