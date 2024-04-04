package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Drizzle extends DrinkComponent{
    public static final String TAG = Drizzle.class.getSimpleName();

    public enum Type {CARAMEL, MOCHA;}

    private Type type;

    public Drizzle() {
        super();
    }

    public Drizzle(Type type) {
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
        String abbreviationOfPropeties = Character.toString(type.name().charAt(0)) + " drizzle";

        if (shaken) {
            abbreviationOfPropeties += " shaken";
        }
        if (blended) {
            abbreviationOfPropeties += " blended";
        }

        return abbreviationOfPropeties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drizzle)) return false;
        if (!super.equals(o)) return false;
        Drizzle drizzle = (Drizzle) o;
        return type == drizzle.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
