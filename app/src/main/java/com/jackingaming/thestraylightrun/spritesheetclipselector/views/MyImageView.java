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

    public interface ValueChangedListener {
        void onXRectSelectedChanged(float xScreen);

        void onYRectSelectedChanged(float yScreen);

        void onXDatasourceChanged(float xDatasource);

        void onYDatasourceChanged(float yDatasource);
    }

    private ValueChangedListener listener;

    public void setListener(ValueChangedListener listener) {
        this.listener = listener;
    }

    private static final float STROKE_WIDTH_DEFAULT = 1f;
    private static final float WIDTH_RECT_SELECTED_DEFAULT = 160f;
    private static final float HEIGHT_RECT_SELECTED_DEFAULT = 160f;

    private float xRectSelected = 0f;
    private float yRectSelected = 0f;
    private RectF rectSelected = new RectF(xRectSelected, yRectSelected, WIDTH_RECT_SELECTED_DEFAULT, HEIGHT_RECT_SELECTED_DEFAULT);
    private Paint paintRectSelectedFill = new Paint();
    private Paint paintRectSelectedStroke = new Paint();
    private float xDatasource;
    private float yDatasource;
    private float widthDatasource;
    private float heightDatasource;

    private int widthScreen;
    private int heightScreen;
    private float scaleHorizontal;
    private float scaleVertical;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        setOnTouchListener(this);
    }

    private void initPaint() {
        paintRectSelectedFill.setAntiAlias(true);
        paintRectSelectedFill.setStyle(Paint.Style.FILL);
        paintRectSelectedFill.setColor(Color.YELLOW);
        paintRectSelectedFill.setAlpha(0x33);   // 0xff=255 in decimal

        paintRectSelectedStroke.setAntiAlias(true);
        paintRectSelectedStroke.setStyle(Paint.Style.STROKE);
        paintRectSelectedStroke.setColor(Color.BLUE);
        paintRectSelectedStroke.setStrokeWidth(STROKE_WIDTH_DEFAULT);
        paintRectSelectedStroke.setAlpha(0x33); // 0xff=255 in decimal
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged()");

        widthScreen = w;
        heightScreen = h;
        Log.i(TAG, "widthScreen: " + widthScreen);
        Log.i(TAG, "heightScreen: " + heightScreen);

        if (getDrawable() != null) {
            Log.d(TAG, "getDrawable() != null");

            widthDatasource = ((float) getDrawable().getIntrinsicWidth());
            heightDatasource = ((float) getDrawable().getIntrinsicHeight());
            Log.i(TAG, "widthDatasource: " + widthDatasource);
            Log.i(TAG, "heightDatasource: " + heightDatasource);

            scaleHorizontal = widthScreen / widthDatasource;
            scaleVertical = heightScreen / heightDatasource;
            Log.i(TAG, "scaleHorizontal: " + scaleHorizontal);
            Log.i(TAG, "scaleVertical: " + scaleVertical);
        } else {
            Log.d(TAG, "getDrawable() == null");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rectSelected, paintRectSelectedFill);
        canvas.drawRect(rectSelected, paintRectSelectedStroke);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, "onTouch(View, MotionEvent)");

        switch (motionEvent.getAction()) {
            // DOWN, MOVE, and UP will all do the same thing.
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, MotionEvent.actionToString(motionEvent.getAction()));

                xRectSelected = motionEvent.getX();
                yRectSelected = motionEvent.getY();
                xDatasource = xRectSelected / scaleHorizontal;
                yDatasource = yRectSelected / scaleVertical;
                listener.onXRectSelectedChanged(xRectSelected);
                listener.onYRectSelectedChanged(yRectSelected);
                listener.onXDatasourceChanged(xDatasource);
                listener.onYDatasourceChanged(yDatasource);

                rectSelected.left = xRectSelected;
                rectSelected.top = yRectSelected;
                rectSelected.right = rectSelected.left + WIDTH_RECT_SELECTED_DEFAULT;
                rectSelected.bottom = rectSelected.top + HEIGHT_RECT_SELECTED_DEFAULT;
                view.invalidate();
                return true;
            default:
                Log.e(TAG, "default-block");
                return false;
        }
    }
}
