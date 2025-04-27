package com.jackingaming.thestraylightrun.sandbox.tank;

public class Projectile {
    private float x, y;
    private float angle;
    private float speed = 5f;

    public Projectile(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    // Move the projectile based on its angle
    public void update(long elapsed) {
        double rad = Math.toRadians(angle);
        x += speed * Math.cos(rad);
        y += speed * Math.sin(rad);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
