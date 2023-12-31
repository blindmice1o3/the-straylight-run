package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.jackingaming.thestraylightrun.R;

public class EspressoShot extends AppCompatImageView {
    public static final String TAG = EspressoShot.class.getSimpleName();

    public enum Type {BLONDE, SIGNATURE, DECAF;}

    private Type type;

    public EspressoShot(@NonNull Context context) {
        super(context);
    }

    public EspressoShot(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateType(Type typeSelected) {
        this.type = typeSelected;

        int colorToUse = getColorIdBasedOnType(this.type);
        setBackgroundColor(getResources().getColor(colorToUse));
    }

    public Type getType() {
        return type;
    }

    public static int getColorIdBasedOnType(Type type) {
        switch (type) {
            case BLONDE:
                return R.color.cream;
            case SIGNATURE:
                return R.color.light_blue_900;
            case DECAF:
                return R.color.teal_200;
            default:
                return R.color.red;
        }
    }
}
