package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Syrup extends DrinkComponent {
    public static final String TAG = Syrup.class.getSimpleName();

    public enum Type {VANILLA, BROWN_SUGAR;}

    private Type type;

    public Syrup(@NonNull Context context) {
        super(context);
    }

    public Syrup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
