package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public abstract class DrinkComponent {
    public static final String TAG = DrinkComponent.class.getSimpleName();

    private boolean shaken;
    private boolean blended;

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
