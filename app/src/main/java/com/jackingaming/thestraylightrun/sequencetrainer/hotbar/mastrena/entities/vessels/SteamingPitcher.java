package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels;

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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupHot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.HashMap;

public class SteamingPitcher extends androidx.appcompat.widget.AppCompatImageView
        implements LiquidContainable {
    public static final String TAG = SteamingPitcher.class.getSimpleName();
    public static final String DRAG_LABEL = SteamingPitcher.class.getSimpleName();

    public interface SteamingPitcherListener {
        void showDialogFillSteamingPitcher(String contentToBeSteamed);
    }

    private SteamingPitcherListener listener;

    private Milk milk;

    private ObjectAnimator temperatureAnimator;
    private ObjectAnimator timeFrothedAnimator;

    private int idPurple, idRed;
    private Paint textPaint;

    public SteamingPitcher(@NonNull Context context) {
        super(context);
        init();
    }

    public SteamingPitcher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        idPurple = getResources().getColor(R.color.purple_700);
        idRed = getResources().getColor(R.color.red);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(idPurple);
        textPaint.setTextSize(14);
    }

    public void startTimeFrothedAnimator() {
        Log.e(TAG, "startTimeFrothedAnimator()");

        if (milk == null) {
            Log.e(TAG, "milk == null... returning.");
            return;
        }

        timeFrothedAnimator = ObjectAnimator.ofInt(milk, "timeFrothed", milk.getTimeFrothed(), 10);
        timeFrothedAnimator.setInterpolator(new LinearInterpolator());
        timeFrothedAnimator.setDuration(10000L);
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

        if (milk == null) {
            Log.e(TAG, "milk == null... returning.");
            return;
        }

        temperatureAnimator = ObjectAnimator.ofInt(milk, "temperature", milk.getTemperature(), 160);
        int coefficientAmount = 1 + (milk.getAmount() / 100);
        temperatureAnimator.setDuration(
                coefficientAmount * (((160L - milk.getTemperature()) * 1000L) / 20)
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

        int temperature = (milk == null) ? 0 : milk.getTemperature();
        int colorTemperature = (temperature < 160) ? idPurple : idRed;
        textPaint.setColor(colorTemperature);
        canvas.drawText(Integer.toString(temperature), 5, 15, textPaint);

        textPaint.setColor(idRed);
        int timeFrothed = (milk == null) ? 0 : milk.getTimeFrothed();
        canvas.drawText(Integer.toString(timeFrothed), getWidth() - 16, 15, textPaint);

        textPaint.setColor(idPurple);
        String nameOfContent = (milk == null) ? "null" : milk.getType().name();
        canvas.drawText(nameOfContent, 5, 30, textPaint);

        int amount = (milk == null) ? 0 : milk.getAmount();
        canvas.drawText(Integer.toString(amount), 5, 45, textPaint);
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
            String label = DRAG_LABEL;

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

                    if (event.getClipDescription().getLabel().equals(RefrigeratorFragment.DRAG_LABEL_MILK)) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(" + RefrigeratorFragment.DRAG_LABEL_MILK + ")");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);
                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    } else if (event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) ||
                            event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) || event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)");

                        if (((CupImageView) event.getLocalState()).getShots().size() == 0 &&
                                ((CupImageView) event.getLocalState()).getMilk() != null) {
                            Log.d(TAG, "((CupImageView) event.getLocalState()).getShots().size() == 0 && ((CupImageView) event.getLocalState()).getMilk() != null");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);
                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "NOT ((CupImageView) event.getLocalState()).getShots().size() == 0 && ((CupImageView) event.getLocalState()).getMilk() != null");
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

                if (event.getClipDescription().getLabel().equals(RefrigeratorFragment.DRAG_LABEL_MILK)) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(" + RefrigeratorFragment.DRAG_LABEL_MILK + ")");

                    String contentToBeSteamed = event.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, "contentToBeSteamed: " + contentToBeSteamed);

                    if (listener != null) {
                        listener.showDialogFillSteamingPitcher(contentToBeSteamed);
                    } else {
                        Log.e(TAG, "listener == null");
                    }
                } else if (event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) ||
                        event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) || event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)");

                    CupImageView cupImageView = ((CupImageView) event.getLocalState());

                    Toast.makeText(getContext(), "transferring content of cup", Toast.LENGTH_SHORT).show();
                    transferIn(
                            cupImageView.transferOut()
                    );
                    cupImageView.empty();
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

    public void updateMilk(Milk milk) {
        this.milk = milk;

        if (milk == null) {
            setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
            invalidate();
            return;
        }

        int idColor = -1;
        switch (milk.getType()) {
            case TWO_PERCENT:
                idColor = R.color.teal_200;
                break;
            case WHOLE:
                idColor = R.color.red;
                break;
//            case NON_FAT:
//                idColor = R.color.light_blue_A200;
//                break;
//            case HALF_AND_HALF:
//                idColor = R.color.purple;
//                break;
            case OAT:
                idColor = R.color.blue;
                break;
            case COCONUT:
                idColor = R.color.green;
                break;
            case ALMOND:
                idColor = R.color.brown;
                break;
            case SOY:
                idColor = R.color.cream;
                break;
            default:
                idColor = R.color.orange;
                break;
        }
        setBackgroundColor(
                getResources().getColor(idColor)
        );
        invalidate();
    }

    public SteamingPitcherListener getListener() {
        return listener;
    }

    public void setListener(SteamingPitcherListener listener) {
        this.listener = listener;
    }

    public Milk getMilk() {
        return milk;
    }

    public void setMilk(Milk milk) {
        this.milk = milk;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        if (content.containsKey("milk")) {
            milk = (Milk) content.get("milk");
        }

        updateMilk(milk);
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = new HashMap<>();

        content.put("milk", milk);

        return content;
    }

    @Override
    public void empty() {
        updateMilk(null);
    }
}
