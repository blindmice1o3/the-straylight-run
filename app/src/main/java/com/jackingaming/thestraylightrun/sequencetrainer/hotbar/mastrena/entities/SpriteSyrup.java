package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;

public class SpriteSyrup extends ClickableAndDraggableImageView {
    public static final String TAG = SpriteSyrup.class.getSimpleName();

    private Syrup.Type type;

    public SpriteSyrup(@NonNull Context context) {
        super(context);
    }

    public SpriteSyrup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void doClick(MotionEvent event) {
        // TODO: open dialog listing details of syrup.
    }

    @Override
    protected void doMove(MotionEvent event) {
        // intentionally blank.
    }

    public void init(Syrup.Type type) {
        this.type = type;

        int colorToUse = lookupColorByType(type);
        setBackgroundColor(getResources().getColor(colorToUse));
    }

    public Syrup instantiateSyrup() {
        return new Syrup(type);
    }

    public static int lookupColorByType(Syrup.Type type) {
        switch (type) {
            case VANILLA:
                return R.color.cream;
            case BROWN_SUGAR:
                return R.color.brown;
            case MOCHA:
                return R.color.dark_brown;
            default:
                return R.color.red;
        }
    }

    public Syrup.Type getType() {
        return type;
    }

    public void setType(Syrup.Type type) {
        this.type = type;
    }
}
