package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import java.io.Serializable;

public abstract class DrinkComponent
        implements Serializable {
    public static final String TAG = DrinkComponent.class.getSimpleName();

    protected boolean shaken;
    protected boolean blended;

    public DrinkComponent() {
    }

    public boolean isShaken() {
        return shaken;
    }

    public void setShaken(boolean shaken) {
        this.shaken = shaken;
    }

    public boolean isBlended() {
        return blended;
    }

    public void setBlended(boolean blended) {
        this.blended = blended;
    }
}
