package com.jackingaming.thestraylightrun.accelerometer.game.drawers.groupchat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.color.MaterialColors;
import com.jackingaming.thestraylightrun.R;

public class CircularBorderedImageView extends AppCompatImageView {
    private Path path = new Path();
    private int colorCircularBorder;

    public CircularBorderedImageView(Context context) {
        super(context);
    }

    public CircularBorderedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        colorCircularBorder = MaterialColors.getColor(getContext(), com.google.android.material.R.attr.colorSurface, getContext().getResources().getColor(R.color.yellow));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.addRoundRect(0, 0, getWidth(), getHeight(), 1000, 1000, Path.Direction.CW);
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.clipPath(path);
        canvas.drawColor(colorCircularBorder);
    }
}
