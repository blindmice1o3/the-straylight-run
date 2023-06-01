package com.jackingaming.thestraylightrun.nextweektonight.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;

public class AnimatedLogoView extends View {
    public static final String TAG = AnimatedLogoView.class.getSimpleName();
    private static final String DEFAULT_LOGO = "Next Week Tonight";
    private static final float DEFAULT_X_LOGO = -500f;
    private static final float DEFAULT_Y_LOGO = 0f;
    private static final int DEFAULT_PADDING_TOP = 8;
    private static final int NUMBER_OF_EXTRA_ROWS = 1;
    private static final int NUMBER_OF_EXTRA_COLUMNS = 2;

    private Paint paintBackground;
    private Paint paintText;

    private int widthScreen;
    private int heightScreen;

    private String logo = DEFAULT_LOGO;
    private float xLogo1 = DEFAULT_X_LOGO;
    private float xLogo2 = DEFAULT_X_LOGO;
    private float yLogo = DEFAULT_Y_LOGO;
    private int widthText;
    private int heightText;
    private int numberOfRows;

    public AnimatedLogoView(Context context) {
        this(context, null);
    }

    public AnimatedLogoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint();
        paintBackground.setAntiAlias(true);
        paintBackground.setColor(getResources().getColor(R.color.purple_700));

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextSize(42f);
        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        paintText.setTypeface(typeface);
        paintText.setColor(getResources().getColor(R.color.teal_700));

        Rect bounds = new Rect();
        paintText.getTextBounds(logo, 0, logo.length(), bounds);
        widthText = bounds.width();
        heightText = bounds.height();
    }

    private int numberOfColumnsPerScreenWidth;
    private String textRepeated = "";

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        widthScreen = w;
        heightScreen = h;

        numberOfRows = (heightScreen / (heightText + DEFAULT_PADDING_TOP)) + NUMBER_OF_EXTRA_ROWS;
        numberOfColumnsPerScreenWidth = (widthScreen / widthText) + NUMBER_OF_EXTRA_COLUMNS;

        for (int i = 0; i < numberOfColumnsPerScreenWidth; i++) {
            textRepeated = textRepeated + logo + " ";
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(paintBackground);

        yLogo = heightText + DEFAULT_PADDING_TOP;
        for (int i = 0; i < numberOfRows; i++) {
            if (i % 2 == 0) {
                canvas.drawText(textRepeated, xLogo1, yLogo, paintText);
            } else {
                canvas.drawText(textRepeated, xLogo2, yLogo, paintText);
            }
            yLogo += (heightText + DEFAULT_PADDING_TOP);
        }
    }

    public float getXLogo1() {
        Log.i(TAG, "getXLogo1()");
        return xLogo1;
    }

    public void setXLogo1(float xLogo1) {
        Log.i(TAG, "setXLogo1() xLogo1: " + xLogo1);
        this.xLogo1 = xLogo1;
        invalidate();
    }

    public float getXLogo2() {
        Log.i(TAG, "getXLogo2()");
        return xLogo2;
    }

    public void setXLogo2(float xLogo2) {
        Log.i(TAG, "setXLogo2() xLogo2: " + xLogo2);
        this.xLogo2 = xLogo2;
        invalidate();
    }
}
