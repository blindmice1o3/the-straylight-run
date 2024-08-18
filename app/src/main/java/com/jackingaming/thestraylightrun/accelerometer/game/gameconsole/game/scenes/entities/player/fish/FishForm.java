package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Consumer;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.DamageDoer;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Damageable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.SceneEvo;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.hud.ComponentHUD;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Meat;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class FishForm
        implements Form, Damageable, DamageDoer, Consumer {
    public static final String TAG = FishForm.class.getSimpleName();

    public static final int HEALTH_MAX_DEFAULT = 20;
    public static final int BODY_ANIMATION_SPEED_DEFAULT = 600;
    public static final int HEAD_ANIMATION_SPEED_DEFAULT = 400;

    public enum DirectionFacing {LEFT, RIGHT;}

    transient private Game game;

    private Player player;
    private FishStateManager fishStateManager;
    private DirectionFacing directionFacing;

    //EXPERIENCE POINTS
    private int experiencePoints;

    //HEALTH
    private int healthMax;
    private int health;

    //ANIMATIONS
    transient private Animation idleHeadAnimation, eatHeadAnimation, biteHeadAnimation, hurtHeadAnimation;
    transient private Animation currentHeadAnimation;
    transient private Animation currentBodyAnimation;

    //ATTACK TIMER
    private long attackCooldown = 1_500L, attackTimer = attackCooldown;

    private int speed;
    private int damageBite;
    private int armor;

    public FishForm() {
        fishStateManager = new FishStateManager();
    }

    @Override
    public void init(Game game) {
        this.game = game;
        this.player = Player.getInstance();
        fishStateManager.init(game);
        directionFacing = DirectionFacing.RIGHT;

        experiencePoints = 2000;
        healthMax = HEALTH_MAX_DEFAULT;
        health = healthMax;

        Assets.init(game);

        player.setWidth(Tile.WIDTH);
        player.setHeight(Tile.HEIGHT / 2);
        player.setBounds(new Rect(0, 0, player.getWidth(), player.getHeight()));

        initAnimations();

/*
        currentHeadImage = Assets.eatFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [fishStateManager.getCurrentActionState().ordinal()]
                [0];
        currentBodyImage = Assets.tailOriginal[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentFinPectoral().ordinal()]
                [fishStateManager.getCurrentTail().ordinal()]
                [0];
 */

        speed = fishStateManager.getAgility();
        damageBite = fishStateManager.getDamageBite();
        armor = fishStateManager.getDefense();
    }

    public void initAnimations() {
        initHeadAnimations();
        switch (fishStateManager.getCurrentTail()) {
            case ORIGINAL:
                currentBodyAnimation = new Animation(
                        Assets.tailOriginal[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case COELAFISH:
                currentBodyAnimation = new Animation(
                        Assets.tailCoelafish[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case TERATISU:
                currentBodyAnimation = new Animation(
                        Assets.tailTeratisu[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case ZINICHTHY:
                currentBodyAnimation = new Animation(
                        Assets.tailZinichthy[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case KURASELACHE:
                currentBodyAnimation = new Animation(
                        Assets.tailKuraselache[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            default:
                System.out.println("Fish.updateHeadAndTailAnimations() switch-construct's default.");
                break;
        }
    }

    public void initHeadAnimations() {
        //for FishStateManager.ActionState.NONE
        Bitmap[] idleFrames = new Bitmap[1];
        idleFrames[0] = Assets.eatFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.EAT.ordinal()]
                [0];
        idleHeadAnimation = new Animation(idleFrames, HEAD_ANIMATION_SPEED_DEFAULT);

        //for FishStateManager.ActionState.HURT
        Bitmap[] hurtFrames = new Bitmap[1];
        hurtFrames[0] = Assets.hurtFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.HURT.ordinal()]
                [0];
        hurtHeadAnimation = new Animation(hurtFrames, HEAD_ANIMATION_SPEED_DEFAULT);

        //for FishStateManager.ActionState.EAT
        Bitmap[] eatFrames = new Bitmap[3];
        eatFrames[0] = Assets.eatFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.EAT.ordinal()]
                [1];
        eatFrames[1] = Assets.eatFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.EAT.ordinal()]
                [2];
        eatFrames[2] = Assets.biteFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.BITE.ordinal()]
                [2];
        eatHeadAnimation = new Animation(eatFrames, HEAD_ANIMATION_SPEED_DEFAULT);

        //for FishStateManager.ActionState.BITE
        Bitmap[] biteFrames = new Bitmap[3];
        biteFrames[0] = Assets.biteFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.BITE.ordinal()]
                [0];
        biteFrames[1] = Assets.biteFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.BITE.ordinal()]
                [1];
        biteFrames[2] = Assets.hurtFrames[fishStateManager.getCurrentBodySize().ordinal()]
                [fishStateManager.getCurrentBodyTexture().ordinal()]
                [fishStateManager.getCurrentJaws().ordinal()]
                [FishStateManager.ActionState.HURT.ordinal()]
                [1];
        biteHeadAnimation = new Animation(biteFrames, HEAD_ANIMATION_SPEED_DEFAULT);

        /////////////////////////////////////////
        currentHeadAnimation = idleHeadAnimation;
        /////////////////////////////////////////
    }

    private int hurtTimer = 0;
    private static final int TARGET_HURT_TIMER = 20;

    @Override
    public void update(long elapsed) {
        //HEAD ANIMATION
        switch (fishStateManager.getCurrentActionState()) {
            case HURT:
                currentHeadAnimation = hurtHeadAnimation;
                //no need tick (1 frame).
                hurtTimer++;
                if (hurtTimer == TARGET_HURT_TIMER) {
                    hurtTimer = 0;
                    fishStateManager.setCurrentActionState(FishStateManager.ActionState.NONE);
                }
                break;
            case BITE:
                currentHeadAnimation.update(elapsed);
                if (currentHeadAnimation.isLastFrame()) {
                    fishStateManager.setCurrentActionState(FishStateManager.ActionState.NONE);
                }
                break;
            case EAT:
                currentHeadAnimation.update(elapsed);
                if (currentHeadAnimation.isLastFrame()) {
                    fishStateManager.setCurrentActionState(FishStateManager.ActionState.NONE);
                }
                break;
            case NONE:
                currentHeadAnimation = idleHeadAnimation;
                //no need tick (1 frame).
                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".update(long elapsed) switch's default.");
        }
        //BODY ANIMATION
        currentBodyAnimation.update(elapsed);

        // ATTACK_COOLDOWN
        tickAttackCooldown(elapsed);

        // RESET [offset-of-next-step] TO ZERO (standing still)
        player.setxMove(0f);
        player.setyMove(0f);
        player.setMoveSpeed(Creature.MOVE_SPEED_DEFAULT);

        // USER_INPUT (determine values of [offset-of-next-step]... potential movement)
        interpretInput();

        // MOVEMENT (check tile, item, entity, and transfer point collisions... actual movement)
        player.move();

        // PREPARE_FOR_RENDER
        determineNextImage();
    }

    private void tickAttackCooldown(long elapsed) {
        attackTimer += elapsed;
        //attackTimer gets reset to 0 in getInput()'s attack-button pressed.
    }

    @Override
    public void draw(Canvas canvas) {
        //ACTUAL IMAGE OF FISH
        if (directionFacing == DirectionFacing.RIGHT) {
            Bitmap imageOfBody = currentBodyAnimation.getCurrentFrame();
            Bitmap imageOfHead = currentHeadAnimation.getCurrentFrame();

            Rect rectOfBodyInGame = getRectOfBodyInGameFacingRight(0f, 0f);
            Rect rectOfHeadInGame = getRectOfHeadInGameFacingRight(0f, 0f);

            Rect rectOfBodyImage = new Rect(0, 0, imageOfBody.getWidth(), imageOfBody.getHeight());
            Rect rectOfBodyOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(rectOfBodyInGame);
            Rect rectOfHeadImage = new Rect(0, 0, imageOfHead.getWidth(), imageOfHead.getHeight());
            Rect rectOfHeadOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(rectOfHeadInGame);

            canvas.drawBitmap(imageOfBody, rectOfBodyImage, rectOfBodyOnScreen, null);
            canvas.drawBitmap(imageOfHead, rectOfHeadImage, rectOfHeadOnScreen, null);
        } else if (directionFacing == DirectionFacing.LEFT) {
            //FLIP IMAGES of head and body.
            Bitmap imageOfHeadFlipped = Animation.flipImageHorizontally(currentHeadAnimation.getCurrentFrame());
            Bitmap imageOfBodyFlipped = Animation.flipImageHorizontally(currentBodyAnimation.getCurrentFrame());

            Rect rectOfHeadInGame = getRectOfHeadInGameFacingLeft(0f, 0f);
            Rect rectOfBodyInGame = getRectOfBodyInGameFacingLeft(0f, 0f);

            Rect rectOfHeadImage = new Rect(0, 0, imageOfHeadFlipped.getWidth(), imageOfHeadFlipped.getHeight());
            Rect rectOfHeadOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(rectOfHeadInGame);
            Rect rectOfBodyImage = new Rect(0, 0, imageOfBodyFlipped.getWidth(), imageOfBodyFlipped.getHeight());
            Rect rectOfBodyOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(rectOfBodyInGame);

            canvas.drawBitmap(imageOfHeadFlipped, rectOfHeadImage, rectOfHeadOnScreen, null);
            canvas.drawBitmap(imageOfBodyFlipped, rectOfBodyImage, rectOfBodyOnScreen, null);
        }
    }

    private Rect getRectOfBodyInGameFacingRight(float xOffset, float yOffset) {
        return new Rect(
                (int) (player.getX() + xOffset),
                (int) (player.getY() + yOffset),
                (int) (player.getX() + (2 * player.getWidth() / 3f) + xOffset),
                (int) (player.getY() + player.getHeight() + yOffset));
    }

    private Rect getRectOfHeadInGameFacingRight(float xOffset, float yOffset) {
        return new Rect(
                (int) (player.getX() + (2 * player.getWidth() / 3f) + xOffset),
                (int) (player.getY() + yOffset),
                (int) (player.getX() + player.getWidth() + xOffset),
                (int) (player.getY() + player.getHeight() + yOffset));
    }

    private Rect getRectOfHeadInGameFacingLeft(float xOffset, float yOffset) {
        return new Rect(
                (int) (player.getX() + xOffset),
                (int) (player.getY() + yOffset),
                (int) (player.getX() + (1 * player.getWidth() / 3f) + xOffset),
                (int) (player.getY() + player.getHeight() + yOffset));
    }

    private Rect getRectOfBodyInGameFacingLeft(float xOffset, float yOffset) {
        return new Rect(
                (int) (player.getX() + (1 * player.getWidth() / 3f) + xOffset),
                (int) (player.getY() + yOffset),
                (int) (player.getX() + player.getWidth() + xOffset),
                (int) (player.getY() + player.getHeight() + yOffset));
    }

    @Override
    public void interpretInput() {
        // Check InputManager's ButtonPadFragment-specific boolean fields.
        if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.A)");
            // ATTACK_COOLDOWN
            if (attackTimer < attackCooldown) {
                return;
            }
            // ANIMATION AND STATE
            currentHeadAnimation = biteHeadAnimation;
            currentHeadAnimation.resetIndex();
            fishStateManager.setCurrentActionState(FishStateManager.ActionState.BITE);

            // PERFORM ATTACK
            attackTimer = 0;
            float xOffset = Tile.WIDTH / 4;
            Rect rectOfHeadInGame = (directionFacing == DirectionFacing.RIGHT) ?
                    (getRectOfHeadInGameFacingRight(xOffset, 0f)) :
                    (getRectOfHeadInGameFacingLeft(-xOffset, 0f));
            for (Entity e : game.getSceneManager().getCurrentScene().getEntityManager().getEntities()) {
                if (e.equals(player)) {
                    continue;
                }

                if (rectOfHeadInGame.intersect(e.getCollisionBounds(0f, 0f))) {
                    if (e instanceof Damageable) {
                        doDamage(((Damageable) e));
                    } else {
                        Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(Button.A) Entity is NOT a Damageable.");
                    }
                }
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            // [ALERT] Check isJustPressed() BEFORE isPressing().
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.B)");
            currentHeadAnimation = eatHeadAnimation;
            currentHeadAnimation.resetIndex();
            fishStateManager.setCurrentActionState(FishStateManager.ActionState.EAT);
            player.doCheckItemCollisionViaClick();
        } else if (game.getInputManager().isPressing(InputManager.Button.B)) {
            // [ALERT] Check isJustPressed() BEFORE isPressing().
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isPressing(InputManager.Button.B)");
            float doubledMoveSpeedDefault = 2 * Creature.MOVE_SPEED_DEFAULT;
            player.setMoveSpeed(doubledMoveSpeedDefault);
        }
        // MENU (SAVE/LOAD)
        else if (game.getInputManager().isJustPressed(InputManager.Button.MENU)) {
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.MENU)");
            game.getStateManager().toggleMenuState();
        }
        // BODY-PARTS SWAPPING
        else if (game.getInputManager().isJustPressedViewportButton(InputManager.ViewportButton.UP)) {
            // JAWS
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressedViewportButton(ViewportButton.UP)");
            int currentJawsOrdinal = fishStateManager.getCurrentJaws().ordinal();
            FishStateManager.Jaws[] jaws = FishStateManager.Jaws.values();
            if ((currentJawsOrdinal + 1) < jaws.length) {
                fishStateManager.setCurrentJaws(jaws[currentJawsOrdinal + 1]);
            } else {
                fishStateManager.setCurrentJaws(jaws[0]);
            }
            //TODO: inefficient, (though unlikely) could be returning to an already-existing Animation object.
            initHeadAnimations();
            updatePlayerStats();
        } else if (game.getInputManager().isJustPressedViewportButton(InputManager.ViewportButton.DOWN)) {
            // BODY_TEXTURE
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressedViewportButton(ViewportButton.DOWN)");
            int currentBodyTextureOrdinal = fishStateManager.getCurrentBodyTexture().ordinal();
            FishStateManager.BodyTexture[] bodyTexture = FishStateManager.BodyTexture.values();
            if ((currentBodyTextureOrdinal + 1) < bodyTexture.length) {
                fishStateManager.setCurrentBodyTexture(bodyTexture[currentBodyTextureOrdinal + 1]);
            } else {
                fishStateManager.setCurrentBodyTexture(bodyTexture[0]);
            }
            //TODO: inefficient, (though unlikely) could be returning to an already-existing Animation object.
            updateHeadAndTailAnimations();
            updatePlayerStats();
        } else if (game.getInputManager().isJustPressedViewportButton(InputManager.ViewportButton.LEFT)) {
            // FIN_PECTORAL
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressedViewportButton(ViewportButton.LEFT");
            int currentFinPectoralOrdinal = fishStateManager.getCurrentFinPectoral().ordinal();
            FishStateManager.FinPectoral[] finPectoral = FishStateManager.FinPectoral.values();
            if ((currentFinPectoralOrdinal + 1) < finPectoral.length) {
                fishStateManager.setCurrentFinPectoral(finPectoral[currentFinPectoralOrdinal + 1]);
            } else {
                fishStateManager.setCurrentFinPectoral(finPectoral[0]);
            }
            //TODO: inefficient, (though unlikely) could be returning to an already-existing Animation object.
            updateHeadAndTailAnimations();
            updatePlayerStats();
        } else if (game.getInputManager().isJustPressedViewportButton(InputManager.ViewportButton.RIGHT)) {
            // TAIL
            Log.d(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressedViewportButton(ViewportButton.RIGHT)");
            int currentTailOrdinal = fishStateManager.getCurrentTail().ordinal();
            FishStateManager.Tail[] tails = FishStateManager.Tail.values();
            if ((currentTailOrdinal + 1) < tails.length) {
                fishStateManager.setCurrentTail(tails[currentTailOrdinal + 1]);
            } else {
                fishStateManager.setCurrentTail(tails[0]);
            }
            //TODO: inefficient, (though unlikely) could be returning to an already-existing Animation object.
            updateHeadAndTailAnimations();
            updatePlayerStats();
        }

        float moveSpeed = player.getMoveSpeed();
        // Check InputManager's DirectionPadFragment-specific boolean fields.
        if (game.getInputManager().isPressing(InputManager.Button.UP)) {
            player.setDirection(Creature.Direction.UP);
            player.setyMove(-moveSpeed);    // vertical NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.DOWN)) {
            player.setDirection(Creature.Direction.DOWN);
            player.setyMove(moveSpeed);     // vertical POSITIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.LEFT)) {
            directionFacing = DirectionFacing.LEFT;
            player.setDirection(Creature.Direction.LEFT);
            player.setxMove(-moveSpeed);    // horizontal NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.RIGHT)) {
            directionFacing = DirectionFacing.RIGHT;
            player.setDirection(Creature.Direction.RIGHT);
            player.setxMove(moveSpeed);     // horizontal POSITIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.CENTER)) {
            player.setDirection(Creature.Direction.CENTER);
            player.setxMove(0f);            // horizontal ZERO
            player.setyMove(0f);            // vertical ZERO
        } else if (game.getInputManager().isPressing(InputManager.Button.UPLEFT)) {
            directionFacing = DirectionFacing.LEFT;
            player.setDirection(Creature.Direction.UP_LEFT);
            player.setxMove(-moveSpeed);    // horizontal NEGATIVE
            player.setyMove(-moveSpeed);    // vertical NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.UPRIGHT)) {
            directionFacing = DirectionFacing.RIGHT;
            player.setDirection(Creature.Direction.UP_RIGHT);
            player.setxMove(moveSpeed);     // horizontal POSITIVE
            player.setyMove(-moveSpeed);    // vertical NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.DOWNLEFT)) {
            directionFacing = DirectionFacing.LEFT;
            player.setDirection(Creature.Direction.DOWN_LEFT);
            player.setxMove(-moveSpeed);    // horizontal NEGATIVE
            player.setyMove(moveSpeed);     // vertical POSITIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.DOWNRIGHT)) {
            directionFacing = DirectionFacing.RIGHT;
            player.setDirection(Creature.Direction.DOWN_RIGHT);
            player.setxMove(moveSpeed);     // horizontal POSITIVE
            player.setyMove(moveSpeed);     // vertical POSITIVE
        }
    }

    public void updatePlayerStats() {
        //refresh bonuses-based-on-body-parts and takes care of refreshing healthMax.
        fishStateManager.updatePlayerStats();

        damageBite = fishStateManager.getDamageBite();
        //TODO: damageStrength.
        armor = fishStateManager.getDefense();
        speed = fishStateManager.getAgility();
        //TODO: jump.
    }

    public void updateHeadAndTailAnimations() {
        initHeadAnimations();
        switch (fishStateManager.getCurrentTail()) {
            case ORIGINAL:
                currentBodyAnimation = new Animation(
                        Assets.tailOriginal[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case COELAFISH:
                currentBodyAnimation = new Animation(
                        Assets.tailCoelafish[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case TERATISU:
                currentBodyAnimation = new Animation(
                        Assets.tailTeratisu[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case ZINICHTHY:
                currentBodyAnimation = new Animation(
                        Assets.tailZinichthy[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            case KURASELACHE:
                currentBodyAnimation = new Animation(
                        Assets.tailKuraselache[fishStateManager.getCurrentBodySize().ordinal()]
                                [fishStateManager.getCurrentBodyTexture().ordinal()]
                                [fishStateManager.getCurrentFinPectoral().ordinal()], BODY_ANIMATION_SPEED_DEFAULT);
                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".updateHeadAndTailAnimations() switch's default.");
                break;
        }
    }

    @Override
    public void determineNextImage() {
        player.setImage(null);
    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {
        if (item instanceof Meat) {
            // TODO: display these numbers via HeadUpDisplay?
            Meat meat = (Meat) item;
            meat.integrateWithHost(this);
            game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
        }
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        if (item instanceof HoneyPot) {
            game.incrementCurrency(
                    item.getPrice()
            );

            game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
        }
    }

    @Override
    public void die() {
        Log.d(TAG, getClass().getSimpleName() + ".die() PLAYER DIED!");
    }

    @Override
    public void doDamage(Damageable damageable) {
        damageable.takeDamage(damageBite);
    }

    @Override
    public void takeDamage(int incomingDamage) {
        ///////////////////////////////////////
        int netDamage = incomingDamage - armor;
        ///////////////////////////////////////

        if (netDamage > 0) {
            fishStateManager.setCurrentActionState(FishStateManager.ActionState.HURT);
            health -= netDamage;

            if (health <= 0) {
//            active = false;
//            die();
            }

            ComponentHUD damageHUD = new ComponentHUD(game, ComponentHUD.ComponentType.DAMAGE,
                    netDamage, player);
            SceneEvo sceneEvo = ((SceneEvo) game.getSceneManager().getCurrentScene());
            sceneEvo.getHeadUpDisplay().addTimedNumericIndicator(damageHUD);
        }
    }

    @Override
    public void incrementExperiencePoints(int experiencePoints) {
        this.experiencePoints += experiencePoints;

        ComponentHUD experiencePointsHUD = new ComponentHUD(game, ComponentHUD.ComponentType.EXP,
                experiencePoints, player);
        SceneEvo sceneEvo = ((SceneEvo) game.getSceneManager().getCurrentScene());
        sceneEvo.getHeadUpDisplay().addTimedNumericIndicator(experiencePointsHUD);
    }

    @Override
    public void incrementHealth(int health) {
        this.health += health;
        if (this.health > healthMax) {
            this.health = healthMax;
        }

        ComponentHUD healthHUD = new ComponentHUD(game, ComponentHUD.ComponentType.HP,
                health, player);
        SceneEvo sceneEvo = ((SceneEvo) game.getSceneManager().getCurrentScene());
        sceneEvo.getHeadUpDisplay().addTimedNumericIndicator(healthHUD);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthMax() {
        return healthMax;
    }

    public void setHealthMax(int healthMax) {
        this.healthMax = healthMax;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public FishStateManager getFishStateManager() {
        return fishStateManager;
    }
}