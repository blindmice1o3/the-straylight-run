package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrinkComponent)) return false;
        DrinkComponent that = (DrinkComponent) o;
        return shaken == that.shaken && blended == that.blended;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shaken, blended);
    }
}
