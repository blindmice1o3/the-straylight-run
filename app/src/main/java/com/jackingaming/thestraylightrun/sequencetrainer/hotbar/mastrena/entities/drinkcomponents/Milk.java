package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Milk extends DrinkComponent {
    public static final String TAG = Milk.class.getSimpleName();

    public enum Type {TWO_PERCENT, WHOLE, NON_FAT, HALF_AND_HALF, OAT, COCONUT, ALMOND, SOY;}

    private Type type;
    private int amount;
    private int temperature;
    private int timeFrothed;

    public Milk() {
        super();
    }

    public Milk(Type type, int amount, int temperature, int timeFrothed) {
        this.type = type;
        this.amount = amount;
        this.temperature = temperature;
        this.timeFrothed = timeFrothed;
    }

    public void init(Type type, int amount, int temperature, int timeFrothed) {
        this.type = type;
        this.amount = amount;
        this.temperature = temperature;
        this.timeFrothed = timeFrothed;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTimeFrothed() {
        return timeFrothed;
    }

    public void setTimeFrothed(int timeFrothed) {
        this.timeFrothed = timeFrothed;
    }

    @NonNull
    @Override
    public String toString() {
        String abbreviationOfPropeties = type.name().charAt(0) + " " +
                Integer.toString(amount) + " " +
                Integer.toString(temperature) + " " +
                Integer.toString(timeFrothed);

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
        if (o == null || getClass() != o.getClass()) return false;
        Milk milk = (Milk) o;
        return amount == milk.amount && temperature == milk.temperature && timeFrothed == milk.timeFrothed && type == milk.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, temperature, timeFrothed);
    }
}
