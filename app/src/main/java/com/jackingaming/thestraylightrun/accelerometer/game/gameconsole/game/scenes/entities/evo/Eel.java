package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.EelAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.DamageDoer;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Damageable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.composeables.AttackCooldownManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Eel extends Creature
        implements Damageable, DamageDoer {
    public static final String TAG = Eel.class.getSimpleName();

    private static final int WIDTH_WIDE_SHORT = Tile.WIDTH;
    private static final int HEIGHT_WIDE_SHORT = Tile.HEIGHT / 2;
    private static final int WIDTH_NARROW_TALL = Tile.WIDTH / 2;
    private static final int HEIGHT_NARROW_TALL = Tile.HEIGHT;

    public enum State {PATROL, TURN, CHASE, ATTACK, HURT;}

    public enum DirectionFacing {LEFT, RIGHT;}

    public static final int HEALTH_MAX_DEFAULT = 3;

    private EelAnimationManager eelAnimationManager;

    private State state;
    private DirectionFacing directionFacing;
    private float patrolLengthInPixelMax;
    private float patrolLengthInPixelCurrent;

    private AttackCooldownManager attackCooldownManager;

    private int attackDamage;
    private int healthMax;
    private int health;

    public Eel(int xSpawn, int ySpawn,
               DirectionFacing directionFacing, int patrolLengthInPixelMax) {
        super(xSpawn, ySpawn);
        width = Tile.WIDTH;
        height = Tile.HEIGHT / 2;
        moveSpeed = 0.5f;
        state = State.PATROL;
        this.directionFacing = directionFacing;
        direction = (directionFacing == DirectionFacing.LEFT) ? (Direction.LEFT) : (Direction.RIGHT);
        this.patrolLengthInPixelMax = patrolLengthInPixelMax;
        patrolLengthInPixelCurrent = 0f;

        attackCooldownManager = new AttackCooldownManager();

        attackDamage = 1;
        healthMax = HEALTH_MAX_DEFAULT;
        health = healthMax;

        eelAnimationManager = new EelAnimationManager();
    }

    transient private Rect detectionRectangleBounds;
    private int detectionRadiusLength = 3 * Tile.WIDTH;
    private float xBeforeChase, yBeforeChase;

    private Rect getDetectionRectangle(float xOffset, float yOffset) {
        return new Rect(
                (int) (x + (width / 2) + detectionRectangleBounds.left + xOffset),
                (int) (y + (height / 2) + detectionRectangleBounds.top + yOffset),
                (int) (x + (width / 2) + detectionRectangleBounds.left + xOffset) + detectionRectangleBounds.right,
                (int) (y + (height / 2) + detectionRectangleBounds.top + yOffset) + detectionRectangleBounds.bottom);
    }

    private Entity checkDetectionCollisions(float xOffset, float yOffset) {
        for (Entity e : game.getSceneManager().getCurrentScene().getEntityManager().getEntities()) {
            if (e instanceof Player || e instanceof Plant) {
                if (e.getCollisionBounds(0f, 0f).intersect(getDetectionRectangle(xOffset, yOffset))) {
                    return e;
                }
            }
        }

        return null;
    }

    @Override
    public void init(Game game) {
        super.init(game);
        eelAnimationManager.init(game);
        image = eelAnimationManager.getCurrentFrame(state, directionFacing);

        detectionRectangleBounds = new Rect(
                -detectionRadiusLength,     //NEGATIVE
                -detectionRadiusLength,     //NEGATIVE
                2 * detectionRadiusLength,
                2 * detectionRadiusLength);
    }

    @Override
    public void update(long elapsed) {
        eelAnimationManager.update(elapsed);

        attackCooldownManager.update(elapsed);

        xMove = 0f;
        yMove = 0f;

        determineNextMove();
        move();

        determineNextImage();
    }

    private int ticker = 0;
    boolean isBlinkingBorder = false;
    private Entity target;

    private void determineNextMove() {
        switch (state) {
            case PATROL:
                // CHECK FOR SEARCH-TARGET (is target within detection range?)
                target = checkDetectionCollisions(0f, 0f);
                if (target != null) {
                    xBeforeChase = x;
                    yBeforeChase = y;
                    ///////////////////////////
                    state = State.CHASE;
                    changeBoundsToWideShortVersion();
                    ///////////////////////////
                }
                // PATROL (set value for future-change-in-position).
                else if (patrolLengthInPixelCurrent < patrolLengthInPixelMax) {
                    if (directionFacing == DirectionFacing.LEFT) {
                        xMove = -moveSpeed;
                        patrolLengthInPixelCurrent += moveSpeed;
                    } else if (directionFacing == DirectionFacing.RIGHT) {
                        xMove = moveSpeed;
                        patrolLengthInPixelCurrent += moveSpeed;
                    }
                }
                // END OF PATROL LENGTH (reverse direction).
                else {
                    patrolLengthInPixelCurrent = 0f;
                    state = State.TURN;
                    changeBoundsToWideShortVersion();
                }
                break;
            case CHASE:
                // Still chasing: move() Eel and see if hurt() should be called.
                target = checkDetectionCollisions(0f, 0f);
                if (target != null) {
                    Log.d(TAG, "IMMA GETCHA!");
                    if (!isBlinkingBorder && target instanceof Plant) {
                        isBlinkingBorder = true;

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                game.getViewportListener().startBlinkingBorder();
                            }
                        });
                    }

                    if (target.getX() < x && target.getY() < y) {
                        xMove = -moveSpeed;
                        yMove = -moveSpeed;
                        direction = Direction.UP_LEFT;
                        directionFacing = DirectionFacing.LEFT;
                    } else if (target.getX() < x && target.getY() > y) {
                        xMove = -moveSpeed;
                        yMove = moveSpeed;
                        direction = Direction.DOWN_LEFT;
                        directionFacing = DirectionFacing.LEFT;
                    } else if (target.getX() > x && target.getY() < y) {
                        xMove = moveSpeed;
                        yMove = -moveSpeed;
                        direction = Direction.UP_RIGHT;
                        directionFacing = DirectionFacing.RIGHT;
                    } else if (target.getX() > x && target.getY() > y) {
                        xMove = moveSpeed;
                        yMove = moveSpeed;
                        direction = Direction.DOWN_RIGHT;
                        directionFacing = DirectionFacing.RIGHT;
                    } else if (target.getX() < x && target.getY() == y) {
                        xMove = -moveSpeed;
                        yMove = 0;
                        direction = Direction.LEFT;
                        directionFacing = DirectionFacing.LEFT;
                    } else if (target.getX() > x && target.getY() == y) {
                        xMove = moveSpeed;
                        yMove = 0;
                        direction = Direction.RIGHT;
                        directionFacing = DirectionFacing.RIGHT;
                    } else if (target.getY() < y && target.getX() == x) {
                        xMove = 0;
                        yMove = -moveSpeed;
                        direction = Direction.UP;
                    } else if (target.getY() > y && target.getX() == x) {
                        xMove = 0;
                        yMove = moveSpeed;
                        direction = Direction.DOWN;
                    } // enter State.ATTACK through move()'s entity-collision response.
                }
                // target is beyond detection range.
                else {
                    Log.d(TAG, "awwww........ like sand slipping through the fingers (whatever those are).");
                    if (isBlinkingBorder) {
                        isBlinkingBorder = false;

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                game.getViewportListener().stopBlinkingBorder();
                            }
                        });
                    }

                    // RETURNING TO PATROL POSITION
                    if (x < xBeforeChase && y < yBeforeChase) {
                        xMove = moveSpeed;
                        yMove = moveSpeed;
                        direction = Direction.DOWN_RIGHT;
                        directionFacing = DirectionFacing.RIGHT;
                    } else if (x < xBeforeChase && y > yBeforeChase) {
                        xMove = moveSpeed;
                        yMove = -moveSpeed;
                        direction = Direction.UP_RIGHT;
                        directionFacing = DirectionFacing.RIGHT;
                    } else if (x > xBeforeChase && y < yBeforeChase) {
                        xMove = -moveSpeed;
                        yMove = moveSpeed;
                        direction = Direction.DOWN_LEFT;
                        directionFacing = DirectionFacing.LEFT;
                    } else if (x > xBeforeChase && y > yBeforeChase) {
                        xMove = -moveSpeed;
                        yMove = -moveSpeed;
                        direction = Direction.UP_LEFT;
                        directionFacing = DirectionFacing.LEFT;
                    } else if (x > xBeforeChase && y == yBeforeChase) {
                        xMove = -moveSpeed;
                        yMove = 0;
                        direction = Direction.LEFT;
                        directionFacing = DirectionFacing.LEFT;
                    } else if (x < xBeforeChase && y == yBeforeChase) {
                        xMove = moveSpeed;
                        yMove = 0;
                        direction = Direction.RIGHT;
                        directionFacing = DirectionFacing.RIGHT;
                    } else if (y > yBeforeChase && x == xBeforeChase) {
                        xMove = 0;
                        yMove = -moveSpeed;
                        direction = Direction.UP;
                    } else if (y < yBeforeChase && x == xBeforeChase) {
                        xMove = 0;
                        yMove = moveSpeed;
                        direction = Direction.DOWN;
                    } else if ((x == xBeforeChase) && (y == yBeforeChase)) {
                        ////////////////////////////
                        state = State.PATROL;
                        changeBoundsToWideShortVersion();
                        ////////////////////////////
                    }
                }

                break;
            case TURN:
                if (directionFacing == DirectionFacing.LEFT) {
                    direction = Direction.RIGHT;
                    directionFacing = DirectionFacing.RIGHT;
                } else if (directionFacing == DirectionFacing.RIGHT) {
                    direction = Direction.LEFT;
                    directionFacing = DirectionFacing.LEFT;
                }
                state = State.PATROL;
                changeBoundsToWideShortVersion();
                break;
            case ATTACK:
                ticker++;
                //TODO: is this attack-timer-target long enough to iterate through all 2 attackFrames images???
                //make transition-back-to-State.PATROL be based on the index of attackFrames???
                // TODO: doDamage()?
                if (ticker == 40) {
                    ticker = 0;
                    state = State.PATROL;
                    changeBoundsToWideShortVersion();
                }
                break;
            case HURT:
                ticker++;
                //only has 1 hurtFrames image, so transition-back-to-State.PATROL
                //CAN BE BASED ON A TIME LIMIT (as oppose to State.ATTACK being based on its Animation's index).
                if (ticker == 40) {
                    ticker = 0;
                    state = State.PATROL;
                    changeBoundsToWideShortVersion();
                }
                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".determineNextMove() switch's default block.");
                break;
        }
    }

    private void determineNextImage() {
        image = eelAnimationManager.getCurrentFrame(state, directionFacing);
    }

    @Override
    public void draw(Canvas canvas) {
        // DETECTION-RECTANGLE
        Rect detectionSquare = getDetectionRectangle(0, 0);
        Paint paintDetectionSquare = new Paint();
        paintDetectionSquare.setColor(Color.GREEN);
        paintDetectionSquare.setAlpha(60);
        canvas.drawRect(
                (int) ((detectionSquare.left - GameCamera.getInstance().getX()) * GameCamera.getInstance().getWidthPixelToViewportRatio()),
                (int) ((detectionSquare.top - GameCamera.getInstance().getY()) * GameCamera.getInstance().getHeightPixelToViewportRatio()),
                (int) ((detectionSquare.right - GameCamera.getInstance().getX()) * GameCamera.getInstance().getWidthPixelToViewportRatio()),
                (int) ((detectionSquare.bottom - GameCamera.getInstance().getY()) * GameCamera.getInstance().getHeightPixelToViewportRatio()),
                paintDetectionSquare);

        // EEL
        Rect rectOfImage = new Rect(0, 0, image.getWidth(), image.getHeight());
        Rect rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));
        canvas.drawBitmap(image, rectOfImage, rectOnScreen, null);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        // TODO: change to State.ATTACK... handle attack cooldown and doDamage() in update().
        if (e instanceof Player) {
            Player player = (Player) e;

            if (player.getForm() instanceof FishForm) {
                FishForm fishForm = ((FishForm) player.getForm());
                doDamage(fishForm);
            } else {
                Log.e(TAG, "player.getForm() NOT instanceof FishForm");
            }
        } else if (e instanceof Plant) {
            doDamage(((Plant) e));

            if (((Plant) e).getHealth() <= 0) {
                if (isBlinkingBorder) {
                    isBlinkingBorder = false;

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            game.getViewportListener().stopBlinkingBorder();
                        }
                    });
                }
            }
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
        changeBoundsToWideShortVersion();
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
        Item honeyPot = new HoneyPot();
        int widthOfHoneyPot = Tile.WIDTH / 2;
        int heightOfHoneyPot = Tile.HEIGHT / 2;
        honeyPot.setWidth(widthOfHoneyPot);
        honeyPot.setHeight(heightOfHoneyPot);
        int xCenterOfKelpAccountingForWidthOfHoneyPot = (int) (x + (width / 2) - (widthOfHoneyPot / 2));
        int yCenterOfKelpAccountingForHeightOfHoneyPot = (int) (y + (height / 2) - (heightOfHoneyPot / 2));
        honeyPot.setPosition(xCenterOfKelpAccountingForWidthOfHoneyPot, yCenterOfKelpAccountingForHeightOfHoneyPot);
        honeyPot.init(game);
        game.getSceneManager().getCurrentScene().getItemManager().addItem(honeyPot);
    }

    private void changeBoundsToWideShortVersion() {
        width = WIDTH_WIDE_SHORT;
        height = HEIGHT_WIDE_SHORT;
        bounds = new Rect(0, 0, width, height);
    }

    private void changeBoundsToNarrowTallVersion() {
        width = WIDTH_NARROW_TALL;
        height = HEIGHT_NARROW_TALL;

        // attack downward causes problem with heighten vertical collision bound.
        if (yMove > 0) {
            y -= (Tile.HEIGHT / 2);
        }

        if (directionFacing == DirectionFacing.LEFT) {
            // horizontal align: LEFT
            bounds = new Rect(0, 0, width, height);
        } else if (directionFacing == DirectionFacing.RIGHT) {
            // horizontal align: RIGHT
            bounds = new Rect(width, 0, width, height);
        }
    }

    @Override
    public void doDamage(Damageable damageable) {
        state = State.ATTACK;
        changeBoundsToNarrowTallVersion();

        if (attackCooldownManager.isCooldowned()) {
            attackCooldownManager.reset();

            damageable.takeDamage(attackDamage);
        }
    }
}