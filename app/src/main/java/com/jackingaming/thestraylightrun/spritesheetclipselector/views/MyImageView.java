package com.jackingaming.thestraylightrun.spritesheetclipselector.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView
        implements View.OnTouchListener {
    private static final String TAG = MyImageView.class.getSimpleName();
    private float x = 0f;
    private float y = 0f;
    private float width = 160f;
    private float height = 160f;

    private Paint paintFill = new Paint();
    private Paint paintStroke = new Paint();
    private RectF rectF = new RectF(x, y, x + width, y + height);

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        setOnTouchListener(this);
    }

    private void initPaint() {
        paintFill.setAntiAlias(true);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(Color.YELLOW);
        paintFill.setAlpha(0x33);   // 0xff=255 in decimal

        paintStroke.setAntiAlias(true);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setColor(Color.BLUE);
        paintStroke.setStrokeWidth(1f);
        paintStroke.setAlpha(0x33); // 0xff=255 in decimal
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rectF, paintFill);
        canvas.drawRect(rectF, paintStroke);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, "onTouch(View, MotionEvent)");

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouch(View, MotionEvent) ACTION_DOWN");
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouch(View, MotionEvent) ACTION_MOVE (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
                rectF.left = motionEvent.getX();
                rectF.top = motionEvent.getY();
                rectF.right = rectF.left + width;
                rectF.bottom = rectF.top + height;
                view.invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouch(View, MotionEvent) ACTION_UP");
                return true;
            default:
                Log.e(TAG, "onTouch(View, MotionEvent) default-block");
                return false;
        }
    }
}
