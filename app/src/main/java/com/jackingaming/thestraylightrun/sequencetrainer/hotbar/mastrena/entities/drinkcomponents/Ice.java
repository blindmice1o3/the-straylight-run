package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

public class Ice extends DrinkComponent {
    public static final String TAG = Ice.class.getSimpleName();

    public Ice() {
        super();
    }

    @NonNull
    @Override
    public String toString() {
        String icePrettyPrint = "ice\n" +
                "(shaken:" + shaken + ") (blended:" + blended + ")";
        return icePrettyPrint;
    }
}
