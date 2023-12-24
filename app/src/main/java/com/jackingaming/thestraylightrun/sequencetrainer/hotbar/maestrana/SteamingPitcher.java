package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jackingaming.thestraylightrun.R;

public class SteamingPitcher extends androidx.appcompat.widget.AppCompatImageView {
    public static final String TAG = SteamingPitcher.class.getSimpleName();

    private String content;
    private int amount;
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
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(getResources().getColor(R.color.purple_700));
        textPaint.setTextSize(18);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String nameOfContent = (content == null) ? "null" : content;
        canvas.drawText(nameOfContent, 5, 20, textPaint);
        canvas.drawText(Integer.toString(amount), 5, 40, textPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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

    public void empty() {
        Log.e(TAG, "empty()");
        update(null, 0);
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
}
