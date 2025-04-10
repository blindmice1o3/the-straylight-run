package com.jackingaming.thestraylightrun.sandbox.particleexplosion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jackingaming.thestraylightrun.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleExplosionView extends View {
    public static final String TAG = ParticleExplosionView.class.getSimpleName();
    private static final int NUMBER_OF_PARTICLES_PER_EXPLOSION = 150;

    private int widthInPixel, heightInPixel;
    private int halfWidthInPixel, halfHeightInPixel;
    private Paint paintAxis, paintParticle;
    private Random random;
    private List<Particle> particles;
    private float progress;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

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
        paintAxis.setColor(Color.YELLOW);
    }

    // Called in "onMeasure()". Dependent on widthInPixel/heightInPixel being initialized.
    public void initParticles() {
        paintParticle = new Paint();
        paintParticle.setColor(Color.RED);

        random = new Random();

        particles = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PARTICLES_PER_EXPLOSION; i++) {
            int colorRandom = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            int xStart = halfWidthInPixel;
            int yStart = halfHeightInPixel;
            float maxHorizontalDisplacementRandom =
                    halfWidthInPixel * generateRandomNumberInRange(-0.9f, 0.9f);
            float maxVerticalDisplacementRandom =
                    heightInPixel * generateRandomNumberInRange(0.2f, 0.38f);

            particles.add(new Particle(
                    colorRandom,
                    xStart,
                    yStart,
                    maxHorizontalDisplacementRandom,
                    maxVerticalDisplacementRandom
            ));
        }
    }

    private float generateRandomNumberInRange(float min, float max) {
        float numberRandom = (random.nextFloat() * (max - min)) + min;
        return numberRandom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure()");

        widthInPixel = MeasureSpec.getSize(widthMeasureSpec);
        heightInPixel = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "(widthInPixel, heightInPixel): (" + widthInPixel + ", " + heightInPixel + ")");
        halfWidthInPixel = widthInPixel / 2;
        halfHeightInPixel = heightInPixel / 2;

        initParticles();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void updateProgressOfParticles(float progress) {
        for (Particle particle : particles) {
            particle.update(progress);
        }
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Background
        int colorBackground = getResources().getColor(R.color.purple_200);
        canvas.drawColor(colorBackground);

        // X-axis
        canvas.drawLine(0f, halfHeightInPixel, widthInPixel, halfHeightInPixel, paintAxis);
        // Y-axis
        canvas.drawLine(halfWidthInPixel, 0, halfWidthInPixel, heightInPixel, paintAxis);

        // Particle
        for (Particle particle : particles) {
            if (particle.getAlpha() != 0) {
                int colorParticle = particle.getColor();
                int colorParticleWithAlpha = Color.argb(particle.getAlpha(),
                        Color.red(colorParticle),
                        Color.green(colorParticle),
                        Color.blue(colorParticle));
                paintParticle.setColor(colorParticleWithAlpha);
                canvas.drawCircle(particle.getxCurrent(), particle.getyCurrent(),
                        particle.getRadiusCurrent(), paintParticle);
            }
        }
    }
}
