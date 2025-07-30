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
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_right);
            framesPatrolLeft = new Bitmap[3];
            framesPatrolLeft[0] = Animation.flipImageHorizontally(
                    Bitmap.createBitmap(spriteSheet, 0, 439, 340, 218)
            );
            framesPatrolLeft[1] = Animation.flipImageHorizontally(
                    Bitmap.createBitmap(spriteSheet, 371, 448, 311, 208)
            );
            framesPatrolLeft[2] = Animation.flipImageHorizontally(
                    Bitmap.createBitmap(spriteSheet, 682, 460, 341, 195)
            );
        }
        animationPatrolLeft = new Animation(framesPatrolLeft, ANIMATION_SPEED_DEFAULT);

        if (framesPatrolRight == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_right);
            framesPatrolRight = new Bitmap[3];
            framesPatrolRight[0] = Bitmap.createBitmap(spriteSheet, 0, 439, 340, 218);
            framesPatrolRight[1] = Bitmap.createBitmap(spriteSheet, 371, 448, 311, 208);
            framesPatrolRight[2] = Bitmap.createBitmap(spriteSheet, 682, 460, 341, 195);
        }
        animationPatrolRight = new Animation(framesPatrolRight, ANIMATION_SPEED_DEFAULT);

        if (framesPatrolRightTurnToLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_right);
            framesPatrolRightTurnToLeft = new Bitmap[1];
            framesPatrolRightTurnToLeft[0] = Bitmap.createBitmap(spriteSheet, 0, 439, 340, 218);
        }
        animationPatrolRightTurnToLeft = new Animation(framesPatrolRightTurnToLeft, ANIMATION_SPEED_DEFAULT);

        if (framesPatrolLeftTurnToRight == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_right);
            framesPatrolLeftTurnToRight = new Bitmap[1];
            framesPatrolLeftTurnToRight[0] = Animation.flipImageHorizontally(
                    Bitmap.createBitmap(spriteSheet, 0, 439, 340, 218)
            );
        }
        animationPatrolLeftTurnToRight = new Animation(framesPatrolLeftTurnToRight, ANIMATION_SPEED_DEFAULT);

        if (framesAttackLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_attack);
            framesAttackLeft = new Bitmap[1];
            framesAttackLeft[0] = Animation.flipImageHorizontally(
                    Bitmap.createBitmap(spriteSheet, 0, 349, 474, 366)
            );
        }
        animationAttackLeft = new Animation(framesAttackLeft, ANIMATION_SPEED_DEFAULT);

        if (framesAttackRight == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_attack);
            framesAttackRight = new Bitmap[1];
            framesAttackRight[0] = Bitmap.createBitmap(spriteSheet, 0, 349, 474, 366);
        }
        animationAttackRight = new Animation(framesAttackRight, ANIMATION_SPEED_DEFAULT);

        if (framesHurtLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_attack);
            framesHurtLeft = new Bitmap[1];
            framesHurtLeft[0] = Bitmap.createBitmap(spriteSheet, 1066, 303, 365, 434);
        }
        animationHurtLeft = new Animation(framesHurtLeft, ANIMATION_SPEED_DEFAULT);

        if (framesHurtRight == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_caterpillar_attack);
            framesHurtRight = new Bitmap[1];
            framesHurtRight[0] = Bitmap.createBitmap(spriteSheet, 1066, 303, 365, 434);
        }
        animationHurtRight = new Animation(framesHurtRight, ANIMATION_SPEED_DEFAULT);
    }
}