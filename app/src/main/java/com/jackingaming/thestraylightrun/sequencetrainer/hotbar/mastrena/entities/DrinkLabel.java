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

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;

public class DrinkLabel extends ClickableAndDraggableTextView {
    public static final String TAG = DrinkLabel.class.getSimpleName();
    public static final String DRAG_LABEL = DrinkLabel.class.getSimpleName();

    private Drink drink;

    public DrinkLabel(@NonNull Context context) {
        super(context);
    }

    public DrinkLabel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void doClick(MotionEvent event) {
        // TODO: open dialog listing details of drink label.
        listenerShowDialog.showSpriteDetailsDialogFragment(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void doMove(MotionEvent event) {
        String label = DRAG_LABEL;

        ClipData dragData = ClipData.newPlainText(label,
                getText());
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

        // Start the drag.
        startDragAndDrop(
                dragData,           // The data to be dragged.
                myShadow,           // The drag shadow builder.
                this,    // The DrinkLabel.
                0              // Flags. Not currently used, set to 0.
        );
        setVisibility(View.INVISIBLE);

        Log.e(TAG, "label: " + label);
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }
}
