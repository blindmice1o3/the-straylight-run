package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.composeables;

import java.io.Serializable;

public class CooldownTimer
        implements Serializable {
    public static final long COOLDOWN_TARGET_DEFAULT = 1_500L;

    private long cooldownTarget;
    private long cooldown; //gets reset to 0 in Eel.doDamage(Damageable).

    public CooldownTimer() {
        cooldownTarget = COOLDOWN_TARGET_DEFAULT;
        cooldown = cooldownTarget;
    }

    public void update(long elapsed) {
        cooldown += elapsed;
    }

    public boolean isCooldowned() {
        return cooldown >= cooldownTarget;
    }

    public void reset() {
        cooldown = 0;
    }

    public void setCooldownTarget(long cooldownTarget) {
        this.cooldownTarget = cooldownTarget;
    }
}
