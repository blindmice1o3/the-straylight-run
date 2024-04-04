package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
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

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.icebin.IceBinFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CaramelDrizzleBottle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Drizzle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.WhippedCream;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.IceShaker;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.HashMap;

public class CupCold extends CupImageView {
    public static final String TAG = CupCold.class.getSimpleName();
    public static final String DRAG_LABEL = CupCold.class.getSimpleName();

    public interface CupColdListener {
        void showDialogFillCupCold(CupCold cupCold, String contentToBePoured);
    }

    private CupColdListener listener;

    private Ice ice;

    public CupCold(Context context) {
        super(context);
    }

    public CupCold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String textIce = (ice != null) ? "iced" : "empty";
        textPaint.setColor(getResources().getColor(R.color.blue));
        canvas.drawText(textIce, 16, 15, textPaint);
    }

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
                    this,    // The CupCold.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
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
                    if (label.equals(IceShaker.DRAG_LABEL) ||
                            label.equals(IceBinFragment.DRAG_LABEL) ||
                            label.equals(RefrigeratorFragment.DRAG_LABEL_MILK) ||
                            label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM) ||
                            label.equals(ShotGlass.DRAG_LABEL) ||
                            label.equals(CaramelDrizzleBottle.DRAG_LABEL) ||
                            label.equals(DrinkLabel.DRAG_LABEL)) {
                        Log.d(TAG, "label.equals(IceShaker.DRAG_LABEL) || label.equals(RefrigeratorFragment.DRAG_LABEL_MILK) || label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM) || label.equals(ShotGlass.DRAG_LABEL) || label.equals(CaramelDrizzleBottle.DRAG_LABEL) || label.equals(DrinkLabel.DRAG_LABEL)");

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

                if (label.equals(IceBinFragment.DRAG_LABEL)) {
                    Log.e(TAG, "label.equals(IceBinFragment.DRAG_LABEL)");

                    String hardCodedWord = event.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, "hardCodedWord: " + hardCodedWord);

                    ice = new Ice();
                    invalidate();
                } else if (label.equals(RefrigeratorFragment.DRAG_LABEL_MILK)) {
                    Log.d(TAG, "label.equals(RefrigeratorFragment.DRAG_LABEL_MILK)");

                    String contentToBePoured = event.getClipData().getItemAt(0).getText().toString();
                    Log.d(TAG, "contentToBePoured: " + contentToBePoured);

                    // TODO:
                    if (listener != null) {
                        listener.showDialogFillCupCold(this, contentToBePoured);
                    } else {
                        Log.e(TAG, "listener == null");
                    }
                } else if (label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM)) {
                    Log.d(TAG, "label.equals(RefrigeratorFragment.DRAG_LABEL_WHIPPED_CREAM)");

                    String tagWhippedCream = event.getClipData().getItemAt(0).getText().toString();
                    Log.d(TAG, "tagWhippedCream: " + tagWhippedCream);

                    whippedCream = new WhippedCream();
                } else if (label.equals(IceShaker.DRAG_LABEL)) {
                    Log.d(TAG, "label.equals(IceShaker.DRAG_LABEL)");

                    IceShaker iceShaker = (IceShaker) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of ice shaker", Toast.LENGTH_SHORT).show();
                    transferIn(
                            iceShaker.transferOut()
                    );
                    iceShaker.empty();
                } else if (label.equals(ShotGlass.DRAG_LABEL)) {
                    Log.d(TAG, "label.equals(ShotGlass.DRAG_LABEL)");

                    // TODO: shotOnTop
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

    public CupColdListener getListener() {
        return listener;
    }

    public void setListener(CupColdListener listener) {
        this.listener = listener;
    }

    public Ice getIce() {
        return ice;
    }

    public void setIce(Ice ice) {
        this.ice = ice;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        super.transferIn(content);

        if (content.containsKey("ice")) {
            ice = (Ice) content.get("ice");
        }
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = super.transferOut();

        if (ice != null) {
            content.put("ice", ice);
        }

        return content;
    }

    @Override
    public void empty() {
        super.empty();

        ice = null;

        invalidate();
    }
}
