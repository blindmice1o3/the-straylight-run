package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;

import java.util.HashMap;

public class ShotGlass extends AppCompatImageView
        implements LiquidContainable {
    public static final String TAG = ShotGlass.class.getSimpleName();

    private EspressoShot.Type type;
    private int numberOfShots;
    private boolean colliding, cantCollide, justCollided;

    private Paint textPaint;

    public ShotGlass(@NonNull Context context) {
        super(context);
        init();
    }

    public ShotGlass(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        type = EspressoShot.Type.SIGNATURE;
        numberOfShots = 0;

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(getResources().getColor(R.color.brown));
        textPaint.setTextSize(14);
    }

    public void update(boolean colliding) {
        this.colliding = colliding;
        if (cantCollide && !this.colliding) {
            cantCollide = false;
        } else if (justCollided) {
            cantCollide = true;
            justCollided = false;
        }
        if (!cantCollide && this.colliding) {
            justCollided = true;
        }
    }

    public void onCollided(View collider) {
        Toast.makeText(getContext(), "onCollided(View)", Toast.LENGTH_SHORT).show();

        setAlpha(0.5f);

        if (collider instanceof EspressoShot) {
            Log.e(TAG, "collider instanceof EspressoShot");

            EspressoShot espressoShot = (EspressoShot) collider;
            // TODO:
            type = espressoShot.getType();
            numberOfShots++;
            invalidate();
        }
        // TODO: Syrup
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setColor(getResources().getColor(
                EspressoShot.lookupColorIdByType(type)
        ));
        canvas.drawText("shots: " + numberOfShots, 5, 15, textPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "ShotGlass";

            ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,    // The ShotGlass.
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

                    if (event.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(\"MaestranaToCaddy\")");

                        if (((CupImageView) event.getLocalState()).getAmount() == 0 &&
                                ((CupImageView) event.getLocalState()).getNumberOfShots() != 0) {
                            Log.d(TAG, "((CupImageView) event.getLocalState()).getAmount() == 0 && ((CupImageView) event.getLocalState()).getNumberOfShots() != 0");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "NOT ((CupImageView) event.getLocalState()).getAmount() == 0 && ((CupImageView) event.getLocalState()).getNumberOfShots() != 0");
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

                if (event.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                    CupImageView cupImageView = ((CupImageView) event.getLocalState());

                    Toast.makeText(getContext(), "transferring content of cup", Toast.LENGTH_SHORT).show();
                    transferIn(
                            cupImageView.transferOut()
                    );
                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED ShotGlass");

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

    public boolean isJustCollided() {
        return justCollided;
    }

    @Override
    public void transferIn(HashMap<String, String> content) {
        if (content.containsKey("type")) {
            for (EspressoShot.Type type : EspressoShot.Type.values()) {
                if (content.get("type").equals(type.name())) {
                    this.type = type;
                }
            }

        }
        if (content.containsKey("numberOfShots")) {
            // INCREMENT
            this.numberOfShots += Integer.parseInt(
                    content.get("numberOfShots")
            );
        }

        invalidate();
    }

    @Override
    public HashMap<String, String> transferOut() {
        HashMap<String, String> content = new HashMap<>();
        content.put("type", this.type.name());
        content.put("numberOfShots", Integer.toString(this.numberOfShots));

        this.type = EspressoShot.Type.SIGNATURE;
        this.numberOfShots = 0;
        invalidate();

        return content;
    }

    @Override
    public void empty() {
        type = EspressoShot.Type.SIGNATURE;
        numberOfShots = 0;

        invalidate();
    }
}