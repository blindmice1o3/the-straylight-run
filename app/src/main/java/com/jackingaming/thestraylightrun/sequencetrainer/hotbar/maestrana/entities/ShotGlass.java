package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.jackingaming.thestraylightrun.R;

public class ShotGlass extends AppCompatImageView {
    public static final String TAG = ShotGlass.class.getSimpleName();

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
        numberOfShots = 0;

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(getResources().getColor(R.color.brown));
        textPaint.setTextSize(18);
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

    public void onCollided() {
        Toast.makeText(getContext(), "onCollided()", Toast.LENGTH_SHORT).show();

        setAlpha(0.5f);
        numberOfShots++;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("shots: " + numberOfShots, 5, 20, textPaint);
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

    public boolean isJustCollided() {
        return justCollided;
    }
}