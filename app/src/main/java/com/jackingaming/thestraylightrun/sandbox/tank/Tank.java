package com.jackingaming.thestraylightrun.sandbox.tank;

public class Tank {
    private float centerX, centerY;  // Fixed point around which the tank rotates
    private float radius;            // Distance from the center to the tank position
    private float angle;             // In degrees

    private float tankX, tankY;

    public Tank(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = 0;
        updatePosition(0L);
    }

    // Rotate the tank (positive is clockwise)
    public void rotate(float deltaAngle) {
        angle = (angle + deltaAngle) % 360;
        updatePosition(0L);
    }

    // Update tank's actual (x, y) based on angle and radius
    public void updatePosition(long elapsed) {
        double rad = Math.toRadians(angle);
        tankX = centerX + (float) (radius * Math.cos(rad));
        tankY = centerY + (float) (radius * Math.sin(rad));
    }

    // Fire a projectile in the direction the tank is facing
    public Projectile fire() {
        return new Projectile(tankX, tankY, angle);
    }

    // Getters
    public float getX() {
        return tankX;
    }

    public float getY() {
        return tankY;
    }

    public float getAngle() {
        return angle;
    }
}
