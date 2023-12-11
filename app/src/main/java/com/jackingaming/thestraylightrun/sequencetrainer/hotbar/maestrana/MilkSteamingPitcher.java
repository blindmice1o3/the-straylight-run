package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;

public class MilkSteamingPitcher extends androidx.appcompat.widget.AppCompatImageView {

    private String milkTag;
    private Paint textPaint;

    public MilkSteamingPitcher(@NonNull Context context) {
        super(context);
        init();
    }

    public MilkSteamingPitcher(@NonNull Context context, @Nullable AttributeSet attrs) {
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
        String milkData = (milkTag == null) ? "null" : milkTag;
        canvas.drawText(milkData, 5, 20, textPaint);
    }

    public String getMilkTag() {
        return milkTag;
    }

    public void setMilkTag(String milkTag) {
        this.milkTag = milkTag;
    }
}
