package com.jackingaming.thestraylightrun.sandbox.particleexplosion;

import java.util.Random;

public class Particle {
    int color;
    int xStart, yStart;
    float maxHorizontalDisplacement, maxVerticalDisplacement;

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
    float velocity;
    // Derive acceleration (the change in velocity over time).
    // a = (final velocity - initial velocity) / t
    // Initial velocity is v and final velocity is -v.
    // a = ((-v) - (v)) / t
    // a = -2v / t
    // a = -2v (for t = 1)
    float acceleration;
    private float cx; // offset
    private float cy; // offset

    private Random random = new Random();
    private float visibilityThresholdLow = generateRandomNumberInRange(0f, 0.14f);
    private float visibilityThresholdHigh = generateRandomNumberInRange(0f, 0.4f);
    private float alpha;

    private float generateRandomNumberInRange(float min, float max) {
        float numberRandom = (random.nextFloat() * (max - min)) + min;
        return numberRandom;
    }

    public Particle(int color, int xStart, int yStart, float maxHorizontalDisplacement, float maxVerticalDisplacement) {
        this.color = color;
        this.xStart = xStart;
        this.yStart = yStart;
        this.maxHorizontalDisplacement = maxHorizontalDisplacement;
        this.maxVerticalDisplacement = maxVerticalDisplacement;

        velocity = 4 * maxVerticalDisplacement;
        acceleration = -2 * velocity;
    }

    private float mapInRange(float transformSource,
                             float inMin, float inMax,
                             float outMin, float outMax) {
        return outMin + (((transformSource - inMin) / (inMax - inMin)) * (outMax - outMin));
    }

    public void update(float progressExplosion) {
        float progressTrajectory = 0f;
        if (progressExplosion < visibilityThresholdLow ||
                (progressExplosion > (1 - visibilityThresholdHigh))) {
            alpha = 0f;
            return;
        } else {
            alpha = 1f;
            progressTrajectory = mapInRange((progressExplosion - visibilityThresholdLow),
                    0f, (1f - visibilityThresholdHigh - visibilityThresholdLow),
                    0f, 1f);
        }

        float currentTime = mapInRange(progressExplosion,
                0f, 1f,
//                0f, 1f);
                0f, 1.4f);

        float horizontalDisplacement = maxHorizontalDisplacement * progressTrajectory;
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
        cx = xStart + horizontalDisplacement;
        cy = yStart - verticalDisplacement;
    }

    public int getColor() {
        return color;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getAlpha() {
        return alpha;
    }
}
