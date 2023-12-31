package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities;

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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.LiquidContainable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.SteamingPitcher;

import java.util.HashMap;

public class CupImageView extends androidx.appcompat.widget.AppCompatImageView
        implements LiquidContainable {
    public static final String TAG = CupImageView.class.getSimpleName();

    private EspressoShot.Type type;
    private int numberOfShots;
    private boolean colliding, cantCollide, justCollided;

    private boolean steamed;
    private String content;
    private int amount;

    private Paint textPaint;

    public CupImageView(Context context) {
        super(context);
        init();
    }

    public CupImageView(Context context, @Nullable AttributeSet attrs) {
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
        textPaint.setTextSize(18);
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
                    if (label.equals("SteamingPitcher") ||
                            label.equals("ShotGlass")) {
                        Log.d(TAG, "label.equals(\"SteamingPitcher\") || label.equals(\"ShotGlass\")");

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

                if (label.equals("SteamingPitcher")) {
                    Log.d(TAG, "label.equals(\"SteamingPitcher\"");

                    SteamingPitcher steamingPitcher = (SteamingPitcher) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of steaming pitcher", Toast.LENGTH_SHORT).show();
                    transferIn(
                            steamingPitcher.transferOut()
                    );
                } else if (label.equals("ShotGlass")) {
                    Log.d(TAG, "label.equals(\"ShotGlass\"");

                    ShotGlass shotGlass = (ShotGlass) event.getLocalState();
                    // TODO:

                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setColor(getResources().getColor(
                EspressoShot.getColorIdBasedOnType(type)
        ));
        canvas.drawText("shots: " + numberOfShots, 5, 20, textPaint);

        String nameOfContent = (content == null) ? "null" : content;
        textPaint.setColor(getResources().getColor(R.color.purple_700));
        canvas.drawText(nameOfContent, 5, 40, textPaint);
        canvas.drawText(Integer.toString(amount), 5, 60, textPaint);
        if (steamed) {
            textPaint.setColor(getResources().getColor(R.color.red));
            canvas.drawText("steamed", 5, 80, textPaint);
        } else {
            textPaint.setColor(getResources().getColor(R.color.purple_700));
            canvas.drawText("unsteamed", 5, 80, textPaint);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "MaestranaToCaddy";

            ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,    // The CupImageView.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }

    public boolean isJustCollided() {
        return justCollided;
    }

    public boolean isSteamed() {
        return steamed;
    }

    public void setSteamed(boolean steamed) {
        this.steamed = steamed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void transferIn(HashMap<String, String> content) {
        if (content.containsKey("content")) {
            this.content = content.get("content");
        }
        if (content.containsKey("amount")) {
            this.amount = Integer.parseInt(
                    content.get("amount")
            );
        }
        if (content.containsKey("steamed")) {
            this.steamed = Boolean.parseBoolean(
                    content.get("steamed")
            );
        }

        invalidate();
    }

    @Override
    public HashMap<String, String> transferOut() {
        HashMap<String, String> content = new HashMap<>();
        content.put("content", this.content);
        content.put("amount", Integer.toString(this.amount));
        content.put("steamed", Boolean.toString(this.steamed));

        this.content = null;
        this.amount = 0;
        this.steamed = false;
        invalidate();

        return content;
    }
}
