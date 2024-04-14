package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

public class Cinnamon extends DrinkComponent {
    public static final String TAG = Cinnamon.class.getSimpleName();

    public Cinnamon() {
        super();
    }

    @NonNull
    @Override
    public String toString() {
        String cinnamonPrettyPrint = "cinnamon\n" +
                "(shaken:" + shaken + ") (blended:" + blended + ")";
        return cinnamonPrettyPrint;
    }
}
