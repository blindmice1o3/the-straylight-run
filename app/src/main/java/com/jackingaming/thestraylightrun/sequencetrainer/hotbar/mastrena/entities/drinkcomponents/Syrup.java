package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

public class Syrup extends DrinkComponent {
    public static final String TAG = Syrup.class.getSimpleName();

    public enum Type {VANILLA, BROWN_SUGAR, MOCHA;}

    private Type type;

    public Syrup() {
        super();
    }

    public Syrup(Type type) {
        super();

        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        String abbreviationOfPropeties = Character.toString(type.name().charAt(0));

        if (shaken) {
            abbreviationOfPropeties += " shaken";
        }
        if (blended) {
            abbreviationOfPropeties += " blended";
        }

        return abbreviationOfPropeties;
    }
}
