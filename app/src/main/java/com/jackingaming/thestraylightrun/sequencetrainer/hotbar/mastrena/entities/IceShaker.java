package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.jackingaming.thestraylightrun.R;

import java.util.HashMap;
import java.util.Map;

public class IceShaker extends AppCompatImageView
        implements LiquidContainable {
    public static final String TAG = IceShaker.class.getSimpleName();

    private boolean iced;
    private boolean cinnamoned;

    private EspressoShot.Type type;
    private EspressoShot.AmountOfWater amountOfWater;
    private EspressoShot.AmountOfBean amountOfBean;
    private int numberOfShots;

    private Map<Syrup.Type, Integer> syrups;

    private Paint textPaint;
    private int idRed;
    private int idLightBlueA200;
    private int idBlue;

    public IceShaker(@NonNull Context context) {
        super(context);
        init();
    }

    public IceShaker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        iced = false;
        cinnamoned = false;

        type = EspressoShot.Type.SIGNATURE;
        amountOfWater = EspressoShot.AmountOfWater.STANDARD;
        amountOfBean = EspressoShot.AmountOfBean.STANDARD;
        numberOfShots = 0;

        syrups = new HashMap<>();

        idRed = getResources().getColor(R.color.red);
        idLightBlueA200 = getResources().getColor(R.color.light_blue_A200);
        idBlue = getResources().getColor(R.color.blue);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(idRed);
        textPaint.setTextSize(14);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String typeAbbreviated = type.name().substring(0, 1);
        String amountOfWaterAbbreviated = amountOfWater.name().substring(0, 1);
        String amountOfBeanAbbreviated = amountOfBean.name().substring(0, 1);
        String textForShot = String.format("E: %d %s %s %s",
                numberOfShots, typeAbbreviated, amountOfWaterAbbreviated, amountOfBeanAbbreviated);
        canvas.drawText(textForShot, 5, 15, textPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "IceShaker";

            ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,    // The IceShaker.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    if (event.getClipDescription().getLabel().equals("Ice") ||
                            event.getClipDescription().getLabel().equals("ShotGlass")) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(\"Ice\") || event.getClipDescription().getLabel().equals(\"ShotGlass\")");

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

                    iced = true;
                    setBackgroundColor(idBlue);
                    invalidate();
                } else if (event.getClipDescription().getLabel().equals("ShotGlass")) {
                    // TODO:
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"ShotGlass\")");

                    ShotGlass shotGlass = (ShotGlass) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of shot glass", Toast.LENGTH_SHORT).show();
                    transferIn(
                            shotGlass.transferOut()
                    );
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

    @Override
    public void transferIn(HashMap<String, String> content) {
        if (content.containsKey("iced")) {
            iced = Boolean.parseBoolean(
                    content.get("iced")
            );
        }
        if (content.containsKey("cinnamoned")) {
            cinnamoned = Boolean.parseBoolean(
                    content.get("cinnamoned")
            );
        }

        if (content.containsKey("type")) {
            for (EspressoShot.Type type : EspressoShot.Type.values()) {
                if (content.get("type").equals(type.name())) {
                    this.type = type;
                }
            }
        }
        if (content.containsKey("amountOfWater")) {
            for (EspressoShot.AmountOfWater amountOfWater : EspressoShot.AmountOfWater.values()) {
                if (content.get("amountOfWater").equals(amountOfWater.name())) {
                    this.amountOfWater = amountOfWater;
                }
            }
        }
        if (content.containsKey("amountOfBean")) {
            for (EspressoShot.AmountOfBean amountOfBean : EspressoShot.AmountOfBean.values()) {
                if (content.get("amountOfBean").equals(amountOfBean.name())) {
                    this.amountOfBean = amountOfBean;
                }
            }
        }
        if (content.containsKey("numberOfShots")) {
            // INCREMENT
            numberOfShots += Integer.parseInt(
                    content.get("numberOfShots")
            );
        }

        // TODO: syrups

        invalidate();
    }

    @Override
    public HashMap<String, String> transferOut() {
        HashMap<String, String> content = new HashMap<>();

        content.put("iced", Boolean.toString(iced));
        content.put("cinnamoned", Boolean.toString(cinnamoned));

        content.put("type", type.name());
        content.put("amountOfWater", amountOfWater.name());
        content.put("amountOfBean", amountOfBean.name());
        content.put("numberOfShots", Integer.toString(numberOfShots));

        // TODO: syrups

        empty();

        return content;
    }

    @Override
    public void empty() {
        iced = false;
        setBackgroundColor(idLightBlueA200);
        cinnamoned = false;

        type = EspressoShot.Type.SIGNATURE;
        amountOfWater = EspressoShot.AmountOfWater.STANDARD;
        amountOfBean = EspressoShot.AmountOfBean.STANDARD;
        numberOfShots = 0;

        syrups.clear();

        invalidate();
    }
}
