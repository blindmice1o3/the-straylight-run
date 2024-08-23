package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.composeables;

public class AttackCooldownManager {
    public static final long ATTACK_COOLDOWN_TARGET_DEFAULT = 1_500L;

    private long attackCooldownTarget;
    private long attackCooldown; //gets reset to 0 in Eel.doDamage(Damageable).

    public AttackCooldownManager() {
        attackCooldownTarget = ATTACK_COOLDOWN_TARGET_DEFAULT;
        attackCooldown = attackCooldownTarget;
    }

    public void update(long elapsed) {
        attackCooldown += elapsed;
    }

    public boolean isCooldowned() {
        return attackCooldown >= attackCooldownTarget;
    }

    public void reset() {
        attackCooldown = 0;
    }

    public void setAttackCooldownTarget(long attackCooldownTarget) {
        this.attackCooldownTarget = attackCooldownTarget;
    }
}
