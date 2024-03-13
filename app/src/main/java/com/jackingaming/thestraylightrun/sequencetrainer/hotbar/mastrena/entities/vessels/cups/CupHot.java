package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CupHot extends CupImageView {
    public static final String TAG = CupHot.class.getSimpleName();
    public static final String DRAG_LABEL = CupHot.class.getSimpleName();

    public CupHot(Context context) {
        super(context);
    }

    public CupHot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // TODO: onTouchEvent()
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = DRAG_LABEL;

            ClipData dragData = ClipData.newPlainText(label,
                    (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,    // The CupHot.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }

    // TODO: onDragEvent()
}
