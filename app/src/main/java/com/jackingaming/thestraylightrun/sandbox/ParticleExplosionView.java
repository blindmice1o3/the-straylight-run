package com.jackingaming.thestraylightrun.sandbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;

public class ParticleExplosionView extends View {
    public static final String TAG = ParticleExplosionView.class.getSimpleName();

    private int widthInPixel, heightInPixel;
    private int halfWidthInPixel, halfHeightInPixel;
    private Paint paintAxis, paintParticle;

    public ParticleExplosionView(Context context) {
        super(context);

        init();
    }

    public ParticleExplosionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        paintAxis = new Paint();
//        paintAxis.setStrokeWidth(16f);
        paintAxis.setColor(Color.YELLOW);

        paintParticle = new Paint();
        paintParticle.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthInPixel = MeasureSpec.getSize(widthMeasureSpec);
        heightInPixel = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "widthInPixel: " + widthInPixel);
        Log.e(TAG, "heightInPixel: " + heightInPixel);
        halfWidthInPixel = widthInPixel / 2;
        halfHeightInPixel = heightInPixel / 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float cx = 0; // offset
    private float cy = 0; // offset

    public void updateCenterOfParticle(float progress) {
        // Derive velocity using second and third kinematic equations of motion.
        // ------------------------------------------------------
        // Second kinematic equation of motion: s = ut - 0.5at^2.
        // ------------------------------------------------------
        // On returning to ground, total displacement is 0.
        // 0 = ut - 0.5at^2
        // 0.5at^2 = ut
        // (1) a = 2u / t
        // ----------------------------------------------------
        // Third kinematic equation of motion: v^2 = u^2 + 2as.
        // ----------------------------------------------------
        // For acceleration in opposite direction (subtract).
        // v^2 = u^2 - 2as
        // At top, v = 0.
        // 0 = u^2 - 2as
        // 2as = u^2
        // (2) a = u^2 / 2s
        // From (1) and (2).
        // u^2 / 2s = 2u / t
        // u = 4s / t
        // u = 4s (for t = 1)
        float velocity = 4 * halfHeightInPixel;
        // Derive acceleration (the change in velocity over time).
        // a = (final velocity - initial velocity) / t
        // Initial velocity is v and final velocity is -v.
        // a = ((-v) - (v)) / t
        // a = -2v / t
        // a = -2v (for t = 1)
        float acceleration = -2 * velocity;
        float currentTime = progress;

        float horizontalDisplacement = halfWidthInPixel * progress;
        // "To calculate the vertical displacement of the particle with
        // constant velocity and acceleration due to gravity, we can
        // use the second kinematic motion equation:
        // s = ut + 0.5at^2.
        // s = displacement (distance from initial position)
        // u = initial velocity
        // a = constant acceleration
        // t = time".
        float verticalDisplacement = (velocity * currentTime) + (float) (0.5f * acceleration * Math.pow(currentTime, 2));

        // halfWidthInPixel added to start particle at center of axis,
        //   as oppose to starting at origin (0, 0).
        // verticalDisplacement subtracted to have it move up as
        // [progress] increases.
        cx = halfWidthInPixel + horizontalDisplacement;
        cy = halfHeightInPixel - verticalDisplacement;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int colorBackground = getResources().getColor(R.color.purple_200);
        canvas.drawColor(colorBackground);

        // X-axis
        canvas.drawLine(0f, halfHeightInPixel, widthInPixel, halfHeightInPixel, paintAxis);
        // Y-axis
        canvas.drawLine(halfWidthInPixel, 0, halfWidthInPixel, heightInPixel, paintAxis);
        // Particle
        canvas.drawCircle(cx, cy, 4f, paintParticle);
    }
}
