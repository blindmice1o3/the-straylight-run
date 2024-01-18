package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
                String.format("%s\n%s", drinkRandom.getSize(), drinkRandom.getName())
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "LabelPrinter";

            ClipData dragData = ClipData.newPlainText(label,
                    getText());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,               // The LabelPrinter.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }
}
