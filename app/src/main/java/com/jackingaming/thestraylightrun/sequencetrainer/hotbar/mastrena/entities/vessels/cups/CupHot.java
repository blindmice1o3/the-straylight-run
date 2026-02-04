package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CaramelDrizzleBottle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Drizzle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.WhippedCream;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

public class CupHot extends CupImageView {
    public static final String TAG = CupHot.class.getSimpleName();
    public static final String DRAG_LABEL = CupHot.class.getSimpleName();

    public CupHot(Context context) {
        super(context);
    }

    public CupHot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void doClick(MotionEvent event) {
        // Open dialog listing content of cup.
        listenerShowDialog.showSpriteDetailsDialogFragment(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void doMove(MotionEvent event) {
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
    }

    private String label;

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    label = event.getClipDescription().getLabel().toString();
                    if (label.equals(ShotGlass.DRAG_LABEL) ||
                            label.equals(CaramelDrizzleBottle.DRAG_LABEL) ||
                            label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM) ||
                            label.equals(DrinkLabel.DRAG_LABEL)) {
                        Log.d(TAG, "label.equals(ShotGlass.DRAG_LABEL) || label.equals(CaramelDrizzleBottle.DRAG_LABEL) || label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM) || label.equals(DrinkLabel.DRAG_LABEL)");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);

                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    } else if (label.equals(SteamingPitcher.DRAG_LABEL)) {
                        Log.d(TAG, "label.equals(SteamingPitcher.DRAG_LABEL)");

                        if (((SteamingPitcher) event.getLocalState()).getMilk() != null &&
                                ((SteamingPitcher) event.getLocalState()).getMilk().getAmount() != 0) {
                            Log.d(TAG, "((SteamingPitcher) event.getLocalState()).getMilk() != null && ((SteamingPitcher) event.getLocalState()).getMilk().getAmount() != 0");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "((SteamingPitcher) event.getLocalState()).getMilk().getAmount() == 0");
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

                if (label.equals(SteamingPitcher.DRAG_LABEL)) {
                    Log.d(TAG, "label.equals(SteamingPitcher.DRAG_LABEL)");

                    SteamingPitcher steamingPitcher = (SteamingPitcher) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of steaming pitcher", Toast.LENGTH_SHORT).show();
                    transferIn(
                            steamingPitcher.transferOut()
                    );
                    steamingPitcher.empty();
                } else if (label.equals(ShotGlass.DRAG_LABEL)) {
                    Log.d(TAG, "label.equals(ShotGlass.DRAG_LABEL)");

                    // shotOnTop
                    if (milk != null) {
                        Log.d(TAG, "milk != null... setting shotOnTop to true.");
                        shotOnTop = true;
                    }

                    ShotGlass shotGlass = (ShotGlass) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of shot glass", Toast.LENGTH_SHORT).show();
                    transferIn(
                            shotGlass.transferOut()
                    );
                    shotGlass.empty();
                } else if (label.equals(CaramelDrizzleBottle.DRAG_LABEL)) {
                    Log.d(TAG, "label.equals(CaramelDrizzleBottle.DRAG_LABEL)");

                    if (milk != null && shots.size() > 0) {
                        Log.d(TAG, "milk != null && shots.size() > 0... instantiating Drizzle with Type.CARAMEL.");
                        drizzle = new Drizzle(Drizzle.Type.CARAMEL);
                    }
                } else if (label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM)) {
                    Log.d(TAG, "label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM)");

                    String tagWhippedCream = event.getClipData().getItemAt(0).getText().toString();
                    Log.d(TAG, "tagWhippedCream: " + tagWhippedCream);

                    whippedCream = new WhippedCream();
                } else if (label.equals(DrinkLabel.DRAG_LABEL)) {
                    DrinkLabel drinkLabel = (DrinkLabel) event.getLocalState();

                    if (isWinnerWinnerChickenDinner(drinkLabel)) {
                        Toast.makeText(getContext(), "WINNER", Toast.LENGTH_SHORT).show();
                        showDialogWinner(drinkLabel);

                        ((FrameLayout) drinkLabel.getParent()).removeView(drinkLabel);
                        ((FrameLayout) getParent()).removeView(this);
                    } else {
                        Toast.makeText(getContext(), "NOT a winner", Toast.LENGTH_SHORT).show();
                        showDialogExpectedVsActual(drinkLabel);

                        drinkLabel.setVisibility(View.VISIBLE);
                    }
                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED CupImageView");

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
                Log.e(TAG, "Unknown action type received by onDragEvent(DragEvent).");
                break;
        }

        return false;
    }
}
