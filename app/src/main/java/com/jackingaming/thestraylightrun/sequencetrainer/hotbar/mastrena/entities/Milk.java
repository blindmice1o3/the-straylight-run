package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class Milk extends AppCompatImageView {
    public static final String TAG = Milk.class.getSimpleName();

    public enum Type {TWO_PERCENT, WHOLE, NON_FAT, HALF_AND_HALF, OAT, COCONUT, ALMOND, SOY;}

    private Type type;
    private int amount;
    private int temperature;
    private int timeFrothed;

    public Milk(@NonNull Context context) {
        super(context);
    }

    public Milk(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
}
