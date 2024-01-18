package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class Syrup extends AppCompatImageView {
    public static final String TAG = Syrup.class.getSimpleName();

    public enum Type {VANILLA;}

    private Type type = Type.VANILLA;
    private boolean collided;

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

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }
}
