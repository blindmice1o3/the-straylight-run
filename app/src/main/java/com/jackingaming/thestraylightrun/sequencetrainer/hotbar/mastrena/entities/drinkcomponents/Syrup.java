package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

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
}
