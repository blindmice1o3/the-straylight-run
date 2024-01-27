package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.entities;

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
import androidx.appcompat.widget.AppCompatImageView;

public class Milk extends AppCompatImageView {
    public static final String TAG = Milk.class.getSimpleName();

    public Milk(@NonNull Context context) {
        super(context);
    }

    public Milk(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "Milk";

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
            Log.e(TAG, "getTag(): " + getTag());

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }
}
