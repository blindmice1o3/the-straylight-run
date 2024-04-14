package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

import java.util.Objects;

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
        String syrupPrettyPrint = type.name() + " syrup\n" +
                "(shaken:" + shaken + ") (blended:" + blended + ")";
        return syrupPrettyPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Syrup)) return false;
        if (!super.equals(o)) return false;
        Syrup syrup = (Syrup) o;
        return type == syrup.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
