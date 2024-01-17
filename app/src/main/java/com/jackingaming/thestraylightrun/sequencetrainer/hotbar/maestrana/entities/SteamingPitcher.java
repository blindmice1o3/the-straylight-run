package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;

import java.util.HashMap;

public class SteamingPitcher extends androidx.appcompat.widget.AppCompatImageView
        implements LiquidContainable {
    public static final String TAG = SteamingPitcher.class.getSimpleName();

    public interface SteamingPitcherListener {
        void showDialogFillSteamingPitcher(String contentToBeSteamed, int amount);
    }

    private SteamingPitcherListener listener;

    private String content;
    private int amount;

    private Paint textPaintPurple;
    private Paint textPaintRed;

    private int temperature;
    private ObjectAnimator temperatureAnimator;
    private int timeFrothed;
    private ObjectAnimator timeFrothedAnimator;

    public SteamingPitcher(@NonNull Context context) {
        super(context);
        init();
    }

    public SteamingPitcher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaintPurple = new Paint();
        textPaintPurple.setAntiAlias(true);
        textPaintPurple.setStyle(Paint.Style.STROKE);
        textPaintPurple.setColor(getResources().getColor(R.color.purple_700));
        textPaintPurple.setTextSize(14);

        textPaintRed = new Paint();
        textPaintRed.setAntiAlias(true);
        textPaintRed.setStyle(Paint.Style.STROKE);
        textPaintRed.setColor(getResources().getColor(R.color.red));
        textPaintRed.setTextSize(14);
    }

    public void startTimeFrothedAnimator() {
        Log.e(TAG, "startTimeFrothedAnimator()");

        timeFrothedAnimator = ObjectAnimator.ofInt(this, "timeFrothed", timeFrothed, 60);
        timeFrothedAnimator.setInterpolator(new LinearInterpolator());
        timeFrothedAnimator.setDuration(60000L);
        timeFrothedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        timeFrothedAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onAnimationEnd()");

                timeFrothedAnimator = null;
            }
        });
        timeFrothedAnimator.start();
    }

    public void cancelTimeFrothedAnimator() {
        Log.e(TAG, "cancelTimeFrothedAnimator()");

        if (timeFrothedAnimator != null) {
            timeFrothedAnimator.cancel();
            timeFrothedAnimator = null;
        }
    }

    public void startTemperatureAnimator() {
        Log.e(TAG, "startTemperatureAnimator()");

        temperatureAnimator = ObjectAnimator.ofInt(this, "temperature", temperature, 160);
        int coefficientAmount = 1 + (amount / 100);
        temperatureAnimator.setDuration(
                coefficientAmount * (((160L - temperature) * 1000L) / 20)
        );
        temperatureAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        temperatureAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onAnimationEnd()");

                temperatureAnimator = null;
            }
        });
        temperatureAnimator.start();
    }

    public void cancelTemperatureAnimator() {
        Log.e(TAG, "cancelTemperatureAnimator()");

        if (temperatureAnimator != null) {
            temperatureAnimator.cancel();
            temperatureAnimator = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint temperatureMessageColor = (temperature < 160) ? textPaintPurple : textPaintRed;
        canvas.drawText(Integer.toString(temperature), 5, 15, temperatureMessageColor);

        canvas.drawText(Integer.toString(timeFrothed), getWidth() - 8, 15, textPaintRed);

        String nameOfContent = (content == null) ? "null" : content;
        canvas.drawText(nameOfContent, 5, 30, textPaintPurple);

        canvas.drawText(Integer.toString(amount), 5, 45, textPaintPurple);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (temperatureAnimator != null && temperatureAnimator.isRunning()) {
            temperatureAnimator.cancel();
        }
        if (timeFrothedAnimator != null && timeFrothedAnimator.isRunning()) {
            timeFrothedAnimator.cancel();
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "SteamingPitcher";

            ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,               // The SteamingPitcher.
                    0              // Flags. Not currently used, set to 0.
            );

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

                    if (event.getClipDescription().getLabel().equals("Milk")) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(\"Milk\")");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);
                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    } else if (event.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(\"MaestranaToCaddy\") && ((CupImageView) event.getLocalState()).getNumberOfShots() == 0 && ((CupImageView) event.getLocalState()).getAmount() != 0");

                        if (((CupImageView) event.getLocalState()).getNumberOfShots() == 0 &&
                                ((CupImageView) event.getLocalState()).getAmount() != 0) {
                            Log.d(TAG, "((CupImageView) event.getLocalState()).getNumberOfShots() == 0 && ((CupImageView) event.getLocalState()).getAmount() != 0");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);
                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "NOT ((CupImageView) event.getLocalState()).getNumberOfShots() == 0 && ((CupImageView) event.getLocalState()).getAmount() != 0");
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

                if (event.getClipDescription().getLabel().equals("Milk")) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"Milk\")");

                    String contentToBeSteamed = event.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, "contentToBeSteamed: " + contentToBeSteamed);

                    if (listener != null) {
                        listener.showDialogFillSteamingPitcher(contentToBeSteamed, amount);
                    } else {
                        Log.e(TAG, "listener == null");
                    }
                } else if (event.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"MaestranaToCaddy\")");

                    CupImageView cupImageView = ((CupImageView) event.getLocalState());

                    Toast.makeText(getContext(), "transferring content of cup", Toast.LENGTH_SHORT).show();
                    transferIn(
                            cupImageView.transferOut()
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
                Log.e(TAG, "Unknown action type received by MilkDragListener.");
                break;
        }

        return false;
    }

    public void update(String content, int amount) {
        this.content = content;
        this.amount = amount;

        if (content == null) {
            setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
            return;
        }

        if (content.equals("coconut")) {
            setBackgroundColor(getResources().getColor(R.color.green));
        } else if (content.equals("almond")) {
            setBackgroundColor(getResources().getColor(R.color.brown));
        } else if (content.equals("soy")) {
            setBackgroundColor(getResources().getColor(R.color.cream));
        } else {
            Log.e(TAG, "update() content unknown.");
            setBackgroundColor(getResources().getColor(R.color.red));
        }
        invalidate();
    }

    public SteamingPitcherListener getListener() {
        return listener;
    }

    public void setListener(SteamingPitcherListener listener) {
        this.listener = listener;
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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTimeFrothed() {
        return timeFrothed;
    }

    public void setTimeFrothed(int timeFrothed) {
        this.timeFrothed = timeFrothed;
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
        if (content.containsKey("temperature")) {
            this.temperature = Integer.parseInt(
                    content.get("temperature")
            );
        }
        if (content.containsKey("timeFrothed")) {
            this.timeFrothed = Integer.parseInt(
                    content.get("timeFrothed")
            );
        }

        update(this.content, this.amount);
    }

    @Override
    public HashMap<String, String> transferOut() {
        HashMap<String, String> content = new HashMap<>();
        content.put("content", this.content);
        content.put("amount", Integer.toString(this.amount));
        content.put("temperature", Integer.toString(this.temperature));
        content.put("timeFrothed", Integer.toString(this.timeFrothed));

        this.content = null;
        this.amount = 0;
        this.temperature = 0;
        this.timeFrothed = 0;
        update(this.content, this.amount);

        return content;
    }

    @Override
    public void empty() {
        Log.e(TAG, "empty()");
        temperature = 0;
        timeFrothed = 0;
        update(null, 0);
    }
}
