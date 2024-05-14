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

public class CinnamonDispenser extends ClickableAndDraggableImageView {
    public static final String TAG = CinnamonDispenser.class.getSimpleName();
    public static final String DRAG_LABEL = CinnamonDispenser.class.getSimpleName();

    public CinnamonDispenser(@NonNull Context context) {
        super(context);
    }

    public CinnamonDispenser(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void doClick(MotionEvent event) {
        // TODO: open dialog listing content of cinnamon dispenser.
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void doMove(MotionEvent event) {
        String label = DRAG_LABEL;

        ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

        // Start the drag.
        startDragAndDrop(
                dragData,           // The data to be dragged.
                myShadow,           // The drag shadow builder.
                null,    // No need to use local data.
                0              // Flags. Not currently used, set to 0.
        );

        Log.e(TAG, "label: " + label);
    }
}
