package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.MenuItemRequestGenerator;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;

public class LabelPrinter extends AppCompatTextView {
    public static final String TAG = LabelPrinter.class.getSimpleName();

    public LabelPrinter(@NonNull Context context) {
        super(context);
    }

    public LabelPrinter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void generateRandomDrinkRequest() {
        Drink drinkRandom = MenuItemRequestGenerator.requestRandomDrink();
        setText(
                String.format("%s - %s", drinkRandom.getSize(), drinkRandom.getName())
        );
    }
}
