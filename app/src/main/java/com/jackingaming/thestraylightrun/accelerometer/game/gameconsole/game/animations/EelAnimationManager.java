package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Eel;

import java.io.Serializable;

public class EelAnimationManager
        implements Serializable {
    transient private static Bitmap[] framesPatrolLeft;
    transient private static Bitmap[] framesPatrolRight;
    transient private static Bitmap[] framesPatrolRightTurnToLeft;
    transient private static Bitmap[] framesPatrolLeftTurnToRight;
    transient private static Bitmap[] framesAttackLeft;
    transient private static Bitmap[] framesAttackRight;
    transient private static Bitmap[] framesHurtLeft;
    transient private static Bitmap[] framesHurtRight;
    public static final int ANIMATION_SPEED_DEFAULT = 300;

    private int speed;

    transient private Animation animationPatrolLeft;
    transient private Animation animationPatrolRight;
    transient private Animation animationPatrolRightTurnToLeft;
    transient private Animation animationPatrolLeftTurnToRight;
    transient private Animation animationAttackLeft;
    transient private Animation animationAttackRight;
    transient private Animation animationHurtLeft;
    transient private Animation animationHurtRight;

    public EelAnimationManager() {
        speed = ANIMATION_SPEED_DEFAULT;
    }

    public void update(long elapsed) {
        animationPatrolLeft.update(elapsed);
        animationPatrolRight.update(elapsed);
        animationPatrolRightTurnToLeft.update(elapsed);
        animationPatrolLeftTurnToRight.update(elapsed);
        animationAttackLeft.update(elapsed);
        animationAttackRight.update(elapsed);
        animationHurtLeft.update(elapsed);
        animationHurtRight.update(elapsed);
    }

    public void changeSpeedForAllAnimations(int speed) {
        animationPatrolLeft.setSpeed(speed);
        animationPatrolRight.setSpeed(speed);
        animationPatrolRightTurnToLeft.setSpeed(speed);
        animationPatrolLeftTurnToRight.setSpeed(speed);
        animationAttackLeft.setSpeed(speed);
        animationAttackRight.setSpeed(speed);
        animationHurtLeft.setSpeed(speed);
        animationHurtRight.setSpeed(speed);
    }

    public Bitmap getCurrentFrame(Eel.State state, Eel.DirectionFacing directionFacing) {
        Bitmap imageByState = null;
        switch (state) {
            case MOVE_RANDOMLY:
            case PATROL:
                if (directionFacing == Eel.DirectionFacing.LEFT) {
                    imageByState = animationPatrolLeft.getCurrentFrame();
                } else if (directionFacing == Eel.DirectionFacing.RIGHT) {
                    imageByState = animationPatrolRight.getCurrentFrame();
                }
                break;
            case TURN:
                if (directionFacing == Eel.DirectionFacing.LEFT) {
                    imageByState = animationPatrolRightTurnToLeft.getCurrentFrame();
                } else if (directionFacing == Eel.DirectionFacing.RIGHT) {
                    imageByState = animationPatrolLeftTurnToRight.getCurrentFrame();
                }
                break;
            case ATTACK:
                if (directionFacing == Eel.DirectionFacing.LEFT) {
                    imageByState = animationAttackLeft.getCurrentFrame();
                } else if (directionFacing == Eel.DirectionFacing.RIGHT) {
                    imageByState = animationAttackRight.getCurrentFrame();
                }
                break;
            case HURT:
                if (directionFacing == Eel.DirectionFacing.LEFT) {
                    imageByState = animationHurtLeft.getCurrentFrame();
                } else if (directionFacing == Eel.DirectionFacing.RIGHT) {
                    imageByState = animationHurtRight.getCurrentFrame();
                }
                break;
            case CHASE:
                if (directionFacing == Eel.DirectionFacing.LEFT) {
                    imageByState = animationPatrolLeft.getCurrentFrame();
                } else if (directionFacing == Eel.DirectionFacing.RIGHT) {
                    imageByState = animationPatrolRight.getCurrentFrame();
                }
                break;
        }
        return imageByState;
    }

    public void init(Game game) {
        if (framesPatrolLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            framesPatrolLeft = new Bitmap[4];
            framesPatrolLeft[0] = Bitmap.createBitmap(spriteSheet, 554, 70, 32, 16);
            framesPatrolLeft[1] = Bitmap.createBitmap(spriteSheet, 587, 70, 32, 16);
            framesPatrolLeft[2] = Bitmap.createBitmap(spriteSheet, 620, 70, 32, 16);
            framesPatrolLeft[3] = Bitmap.createBitmap(spriteSheet, 654, 70, 32, 16);
        }
        animationPatrolLeft = new Animation(framesPatrolLeft, ANIMATION_SPEED_DEFAULT);

        if (framesPatrolRight == null) {
            framesPatrolRight = Animation.flipImageArrayHorizontally(framesPatrolLeft);
        }
        animationPatrolRight = new Animation(framesPatrolRight, ANIMATION_SPEED_DEFAULT);

        if (framesPatrolRightTurnToLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            framesPatrolRightTurnToLeft = new Bitmap[1];
            framesPatrolRightTurnToLeft[0] = Bitmap.createBitmap(spriteSheet, 687, 70, 28, 16);
        }
        animationPatrolRightTurnToLeft = new Animation(framesPatrolRightTurnToLeft, ANIMATION_SPEED_DEFAULT);

        if (framesPatrolLeftTurnToRight == null) {
            framesPatrolLeftTurnToRight = Animation.flipImageArrayHorizontally(framesPatrolRightTurnToLeft);
        }
        animationPatrolLeftTurnToRight = new Animation(framesPatrolLeftTurnToRight, ANIMATION_SPEED_DEFAULT);

        if (framesAttackLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            framesAttackLeft = new Bitmap[1];
            framesAttackLeft[0] = Bitmap.createBitmap(spriteSheet, 716, 62, 16, 32);
        }
        animationAttackLeft = new Animation(framesAttackLeft, ANIMATION_SPEED_DEFAULT);

        if (framesAttackRight == null) {
            framesAttackRight = Animation.flipImageArrayHorizontally(framesAttackLeft);
        }
        animationAttackRight = new Animation(framesAttackRight, ANIMATION_SPEED_DEFAULT);

        if (framesHurtLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            framesHurtLeft = new Bitmap[1];
            framesHurtLeft[0] = Bitmap.createBitmap(spriteSheet, 732, 69, 32, 16);
        }
        animationHurtLeft = new Animation(framesHurtLeft, ANIMATION_SPEED_DEFAULT);

        if (framesHurtRight == null) {
            framesHurtRight = Animation.flipImageArrayHorizontally(framesHurtLeft);
        }
        animationHurtRight = new Animation(framesHurtRight, ANIMATION_SPEED_DEFAULT);
    }
}