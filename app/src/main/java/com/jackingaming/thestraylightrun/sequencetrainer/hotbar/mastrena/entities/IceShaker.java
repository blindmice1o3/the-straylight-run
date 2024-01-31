package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.jackingaming.thestraylightrun.R;

public class IceShaker extends AppCompatImageView {
    public static final String TAG = IceShaker.class.getSimpleName();

    private Paint textPaint;
    private int idRed;

    public IceShaker(@NonNull Context context) {
        super(context);
        init();
    }

    public IceShaker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        idRed = getResources().getColor(R.color.red);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(idRed);
        textPaint.setTextSize(14);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    if (event.getClipDescription().getLabel().equals("Ice")) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(\"Ice\")");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);
                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    }
                } else {
                    Log.e(TAG, "ACTION_DRAG_STARTED clip description NOT ClipDescription.MIMETYPE_TEXT_PLAIN");
                }

                // Return false to indicate that, during the current drag and drop
                // operation, this View doesn't receive events again until
                // ACTION_DRAG_ENDED is sent.
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ACTION_DRAG_ENTERED");

                // Change value of alpha to indicate [ENTERED] state.
                setAlpha(0.5f);

                // Return true. The value is ignored.
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event.
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "ACTION_DRAG_EXITED");

                // Reset value of alpha back to normal.
                setAlpha(0.75f);

                // Return true. The value is ignored.
                return true;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "ACTION_DROP");

                if (event.getClipDescription().getLabel().equals("Ice")) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"Ice\")");

                    String contentToBeShaken = event.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, "contentToBeShaken: " + contentToBeShaken);

                    // TODO:
                    setBackgroundColor(getResources().getColor(R.color.blue));
                    invalidate();
                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED");

                // Reset value of alpha back to normal.
                setAlpha(1.0f);

                // Do a getResult() and displays what happens.
                if (event.getResult()) {
                    Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                }

                // Return true. The value is ignored.
                return true;
            default:
                Log.e(TAG, "Unknown action type received by onDragEvent().");
                break;
        }

        return false;
    }
}
