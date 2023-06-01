package com.jackingaming.thestraylightrun.nextweektonight.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;

public class AnimatedLogoView extends View {
    public static final String TAG = AnimatedLogoView.class.getSimpleName();
    private static final String DEFAULT_LOGO = "Next Week Tonight";
    private static final float DEFAULT_X_LOGO = 50f;
    private static final float DEFAULT_Y_LOGO = 300f;

    private Paint paintBackground;
    private Paint paintText;

    private int widthScreen;
    private int heightScreen;

    private String logo = DEFAULT_LOGO;
    private float xLogo = DEFAULT_X_LOGO;
    private float yLogo = DEFAULT_Y_LOGO;

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        widthScreen = w;
        heightScreen = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(paintBackground);

        canvas.drawText(logo, xLogo, yLogo, paintText);
    }

    public float getXLogo() {
        Log.i(TAG, "getXLogo()");
        return xLogo;
    }

    public void setXLogo(float xLogo) {
        Log.i(TAG, "setXLogo() xLogo: " + xLogo);
        this.xLogo = xLogo;
        invalidate();
    }
}
