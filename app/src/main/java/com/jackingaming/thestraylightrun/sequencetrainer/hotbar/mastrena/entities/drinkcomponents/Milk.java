package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Milk extends DrinkComponent {
    public static final String TAG = Milk.class.getSimpleName();

    public enum Type {TWO_PERCENT, WHOLE, OAT, COCONUT, ALMOND, SOY;}
    // NON_FAT, HALF_AND_HALF

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
        String textType = (type != Type.TWO_PERCENT) ? type.name() : "2%";
        String textAmount = Integer.toString(amount);
        String textTemperature = Integer.toString(temperature);
        String textFroth = Integer.toString(timeFrothed);

        String milkPrettyPrint = textType + " milk (amount:" + textAmount + ")\n" +
                "(temperature:" + textTemperature + ") (froth:" + textFroth + ")";

        return milkPrettyPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Milk milk = (Milk) o;
        // TODO: (to be changed to a +/- range)... currently, if cup has amount
        //  greater-than (OR equal-to) required, it'll count as equal.
        return amount <= milk.amount &&
                temperature == milk.temperature &&
                (timeFrothed <= milk.timeFrothed && (timeFrothed + 2) >= milk.timeFrothed) &&
                type == milk.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, temperature, timeFrothed);
    }
}
