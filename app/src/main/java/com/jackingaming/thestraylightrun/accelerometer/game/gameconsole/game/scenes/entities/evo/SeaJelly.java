package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.SeaJellyAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.DamageDoer;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Damageable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Meat;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class SeaJelly extends Creature
        implements Damageable, DamageDoer {
    public static final String TAG = SeaJelly.class.getSimpleName();

    public enum State {PATROL, ATTACK, HURT;}

    public static final int HEALTH_MAX_DEFAULT = 3;

    private SeaJellyAnimationManager seaJellyAnimationManager;

    private State state;
    private float patrolLengthInPixelMax;
    private float patrolLengthInPixelCurrent;

    //ATTACK TIMER
    private long attackCooldown = 1_500L, attackTimer = attackCooldown;

    private int attackDamage;
    private int healthMax;
    private int health;

    public SeaJelly(int xSpawn, int ySpawn, Direction direction, int patrolLengthInPixelMax) {
        super(xSpawn, ySpawn);
        width = Tile.WIDTH / 2;
        height = Tile.HEIGHT;
        moveSpeed = 0.5f;
        state = State.PATROL;
        this.direction = direction;
        this.patrolLengthInPixelMax = patrolLengthInPixelMax;
        patrolLengthInPixelCurrent = 0f;

        attackDamage = 2;
        healthMax = HEALTH_MAX_DEFAULT;
        health = healthMax;

        seaJellyAnimationManager = new SeaJellyAnimationManager();
    }

    @Override
    public void init(Game game) {
        super.init(game);
        seaJellyAnimationManager.init(game);
    }

    @Override
    public void update(long elapsed) {
        seaJellyAnimationManager.update(elapsed);

        // ATTACK_COOLDOWN
        tickAttackCooldown(elapsed);

        xMove = 0f;
        yMove = 0f;

        determineNextMove();
        move();

        determineNextImage();
    }

    private void tickAttackCooldown(long elapsed) {
        attackTimer += elapsed;
        //attackTimer gets reset to 0 in respondToEntityCollision(Entity).
    }

    private int ticker = 0;

    private void determineNextMove() {
        switch (state) {
            case PATROL:
                // PATROL (set value for future-change-in-position).
                if (patrolLengthInPixelCurrent < patrolLengthInPixelMax) {
                    if (direction == Direction.DOWN) {
                        yMove = moveSpeed;
                        patrolLengthInPixelCurrent += moveSpeed;
                    } else if (direction == Direction.UP) {
                        yMove = -moveSpeed;
                        patrolLengthInPixelCurrent += moveSpeed;
                    }
                }
                // END OF PATROL LENGTH (reverse direction).
                else {
                    patrolLengthInPixelCurrent = 0f;
                    if (direction == Direction.DOWN) {
                        direction = Direction.UP;
                    } else if (direction == Direction.UP) {
                        direction = Direction.DOWN;
                    }
                }
                break;
            case ATTACK:
                ticker++;
                //TODO: is this attack-timer-target long enough to iterate through all 4 attackFrames images???
                //make transition-back-to-State.IDLE be based on the index of attackFrames???
                if (ticker == 40) {
                    ticker = 0;
                    state = State.PATROL;
                }
                break;
            case HURT:
                ticker++;
                //only has 1 hurtFrames image, so transition-back-to-State.IDLE
                //CAN BE BASED ON A TIME LIMIT (as oppose to State.ATTACK being based on its Animation's index).
                if (ticker == 40) {
                    ticker = 0;
                    state = State.PATROL;
                }
                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".update(long elapsed) switch's default block.");
                break;
        }
    }

    private void determineNextImage() {
        // TODO: if opponent present, figure out direction.
        Direction directionOfOpponent = Direction.LEFT;
        image = seaJellyAnimationManager.getCurrentFrame(state, directionOfOpponent);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        if (e instanceof Player) {
            // ATTACK_COOLDOWN
            if (attackTimer < attackCooldown) {
                return true;
            }

            // PERFORM ATTACK
            attackTimer = 0;
            Player player = (Player) e;
            FishForm fishForm = ((FishForm) player.getForm());
            doDamage(fishForm);
        }
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }

    @Override
    public void takeDamage(int incomingDamage) {
        state = State.HURT;
        health -= incomingDamage;

        if (health <= 0) {
            active = false;
            die();
        }
    }

    @Override
    public void die() {
        Log.d(TAG, getClass().getSimpleName() + ".die()");
        // TODO: drop items, reward exp points, etc.
        Item meat = new Meat();
        int widthOfMeat = Tile.WIDTH / 2;
        int heightOfMeat = Tile.HEIGHT / 2;
        meat.setWidth(widthOfMeat);
        meat.setHeight(heightOfMeat);
        int xCenterOfKelpAccountingForWidthOfMeat = (int) (x + (width / 2) - (widthOfMeat / 2));
        int yCenterOfKelpAccountingForHeightOfMeat = (int) (y + (height / 2) - (heightOfMeat / 2));
        meat.setPosition(xCenterOfKelpAccountingForWidthOfMeat, yCenterOfKelpAccountingForHeightOfMeat);
        meat.init(game);
        game.getSceneManager().getCurrentScene().getItemManager().addItem(meat);
    }

    @Override
    public void doDamage(Damageable damageable) {
        state = State.ATTACK;
        damageable.takeDamage(attackDamage);
    }
}