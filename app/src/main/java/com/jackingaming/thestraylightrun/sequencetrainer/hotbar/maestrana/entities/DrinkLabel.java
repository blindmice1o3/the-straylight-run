package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class DrinkLabel extends AppCompatTextView {
    public static final String TAG = DrinkLabel.class.getSimpleName();

    public DrinkLabel(@NonNull Context context) {
        super(context);
    }

    public DrinkLabel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
