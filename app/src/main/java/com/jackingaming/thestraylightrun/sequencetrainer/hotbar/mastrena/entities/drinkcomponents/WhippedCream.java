package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

public class WhippedCream extends DrinkComponent{
    public static final String TAG = WhippedCream.class.getSimpleName();

    public WhippedCream() {
        super();
    }

    @NonNull
    @Override
    public String toString() {
        String abbreviationOfPropeties = "whipped cream";

        if (shaken) {
            abbreviationOfPropeties += " shaken";
        }
        if (blended) {
            abbreviationOfPropeties += " blended";
        }

        return abbreviationOfPropeties;
    }
}
