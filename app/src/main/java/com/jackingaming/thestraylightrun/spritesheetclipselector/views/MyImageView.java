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
    public static final float DEFAULT_WIDTH_IN_PIXEL = 16f;
    public static final float DEFAULT_HEIGHT_IN_PIXEL = 16f;

    public interface ValueChangedListener {
        void onRectSelectedXChanged(float xScreen);

        void onRectSelectedYChanged(float yScreen);

        void onDatasourceXChanged(float xDatasource);

        void onDatasourceYChanged(float yDatasource);
    }

    private ValueChangedListener listener;

    public void setListener(ValueChangedListener listener) {
        this.listener = listener;
    }

    private static final float STROKE_WIDTH_DEFAULT = 1f;

    private Paint paintRectSelectedFill = new Paint();
    private Paint paintRectSelectedStroke = new Paint();
    private float rectSelectedDatasourceX;
    private float rectSelectedDatasourceY;
    private float rectSelectedDatasourceWidth;
    private float rectSelectedDatasourceHeight;

    private int screenWidth;
    private int screenHeight;
    private float datasourceWidth;
    private float datasourceHeight;
    private float scaleHorizontal;
    private float scaleVertical;

    private float rectSelectedScreenX;
    private float rectSelectedScreenY;
    private float rectSelectedScreenWidth;
    private float rectSelectedScreenHeight;
    private RectF rectSelectedScreen;

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

        screenWidth = w;
        screenHeight = h;
        Log.i(TAG, "widthScreen: " + screenWidth);
        Log.i(TAG, "heightScreen: " + screenHeight);

        if (getDrawable() != null) {
            Log.d(TAG, "getDrawable() != null");

            datasourceWidth = ((float) getDrawable().getIntrinsicWidth());
            datasourceHeight = ((float) getDrawable().getIntrinsicHeight());
            Log.i(TAG, "widthDatasource: " + datasourceWidth);
            Log.i(TAG, "heightDatasource: " + datasourceHeight);

            scaleHorizontal = screenWidth / datasourceWidth;
            scaleVertical = screenHeight / datasourceHeight;
            Log.i(TAG, "scaleHorizontal: " + scaleHorizontal);
            Log.i(TAG, "scaleVertical: " + scaleVertical);

            rectSelectedScreenWidth = convertToScreenCoordinateSystemHorizontally(DEFAULT_WIDTH_IN_PIXEL);
            rectSelectedScreenHeight = convertToScreenCoordinateSystemVertically(DEFAULT_HEIGHT_IN_PIXEL);
            rectSelectedScreen = new RectF(rectSelectedScreenX, rectSelectedScreenY, rectSelectedScreenWidth, rectSelectedScreenHeight);
        } else {
            Log.d(TAG, "getDrawable() == null");
        }
    }

    public void setRectSelectedScreenWidth(float rectSelectedScreenWidth) {
        this.rectSelectedScreenWidth = rectSelectedScreenWidth;
        rectSelectedScreen.right = rectSelectedScreen.left + rectSelectedScreenWidth;
        invalidate();
    }

    public void setRectSelectedScreenHeight(float rectSelectedScreenHeight) {
        this.rectSelectedScreenHeight = rectSelectedScreenHeight;
        rectSelectedScreen.bottom = rectSelectedScreen.top + rectSelectedScreenHeight;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rectSelectedScreen, paintRectSelectedFill);
        canvas.drawRect(rectSelectedScreen, paintRectSelectedStroke);
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

                rectSelectedScreenX = motionEvent.getX();
                rectSelectedScreenY = motionEvent.getY();
                rectSelectedDatasourceX = convertToDatasourceCoordinateSystemHorizontally(rectSelectedScreenX);
                rectSelectedDatasourceY = convertToDatasourceCoordinateSystemVertically(rectSelectedScreenY);
                listener.onRectSelectedXChanged(rectSelectedScreenX);
                listener.onRectSelectedYChanged(rectSelectedScreenY);
                listener.onDatasourceXChanged(rectSelectedDatasourceX);
                listener.onDatasourceYChanged(rectSelectedDatasourceY);

                rectSelectedScreen.left = rectSelectedScreenX;
                rectSelectedScreen.top = rectSelectedScreenY;
                rectSelectedScreen.right = rectSelectedScreen.left + rectSelectedScreenWidth;
                rectSelectedScreen.bottom = rectSelectedScreen.top + rectSelectedScreenHeight;
                view.invalidate();
                return true;
            default:
                Log.e(TAG, "default-block");
                return false;
        }
    }

    public float convertToDatasourceCoordinateSystemHorizontally(float valueScreenCoordinateSystem) {
        return valueScreenCoordinateSystem / scaleHorizontal;
    }

    public float convertToDatasourceCoordinateSystemVertically(float valueScreenCoordinateSystem) {
        return valueScreenCoordinateSystem / scaleVertical;
    }

    public float convertToScreenCoordinateSystemHorizontally(float valueDatasourceCoordinateSystem) {
        return valueDatasourceCoordinateSystem * scaleHorizontal;
    }

    public float convertToScreenCoordinateSystemVertically(float valueDatasourceCoordinateSystem) {
        return valueDatasourceCoordinateSystem * scaleVertical;
    }
}
