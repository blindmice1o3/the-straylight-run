package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class Syrup extends AppCompatImageView {
    public static final String TAG = Syrup.class.getSimpleName();

    private boolean collided;

    public Syrup(@NonNull Context context) {
        super(context);
    }

    public Syrup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }
}
