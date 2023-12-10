package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;

public class CupImageView extends androidx.appcompat.widget.AppCompatImageView {

    private int numberOfShots;
    private Paint textPaint;
    private boolean colliding, cantCollide, justCollided;

    public CupImageView(Context context) {
        super(context);
        init();
    }

    public CupImageView(Context context, @Nullable AttributeSet attrs) {
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

    public boolean isJustCollided() {
        return justCollided;
    }
}
