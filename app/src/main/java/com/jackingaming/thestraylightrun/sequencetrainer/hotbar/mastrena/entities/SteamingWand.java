package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipDescription;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.SteamingPitcher;

public class SteamingWand extends ClickableAndDraggableImageView {
    public static final String TAG = SteamingWand.class.getSimpleName();

    private SteamingPitcher steamingPitcher;

    public SteamingWand(@NonNull Context context) {
        super(context);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (steamingPitcher != null) {
                    steamingPitcher.cancelTemperatureAnimator();
                }
            }
        });
    }

    public SteamingWand(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void doClick(MotionEvent event) {
        // TODO: open dialog listing details of steaming wand.
    }

    @Override
    protected void doMove(MotionEvent event) {
        // intentionally blank.
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    if (event.getClipDescription().getLabel().equals(SteamingPitcher.DRAG_LABEL)) {
                        Log.d(TAG, "label.equals(" + SteamingPitcher.DRAG_LABEL + ")");

                        steamingPitcher = (SteamingPitcher) event.getLocalState();
                        if (steamingPitcher.getMilk() != null &&
                                steamingPitcher.getMilk().getAmount() > 0) {
                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.8f);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.d(TAG, "steamingPitcher's milk is null or milk's amount <= 0.");
                        }
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

                steamingPitcher.startTimeFrothedAnimator();

                // Change value of alpha to indicate [ENTERED] state.
                setAlpha(0.5f);

                // Return true. The value is ignored.
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event.
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "ACTION_DRAG_EXITED");

                steamingPitcher.cancelTimeFrothedAnimator();

                // Reset value of alpha back to normal.
                setAlpha(0.8f);

                // Return true. The value is ignored.
                return true;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "ACTION_DROP");

                steamingPitcher.cancelTimeFrothedAnimator();
                steamingPitcher.startTemperatureAnimator();

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
                Log.e(TAG, "Unknown action type received by SteamingWandDragListener.");
                break;
        }

        return false;
    }
}
