package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.SeaJelly;

import java.io.Serializable;

public class SeaJellyAnimationManager
        implements Serializable {
    transient private static Bitmap[] framesPatrol;
    transient private static Bitmap[] framesAttackLeft;
    transient private static Bitmap[] framesAttackRight;
    transient private static Bitmap[] framesHurt;
    public static final int ANIMATION_SPEED_DEFAULT = 300;

    private int speed;

    transient private Animation animationPatrol;
    transient private Animation animationAttackLeft;
    transient private Animation animationAttackRight;
    transient private Animation animationHurt;

    public SeaJellyAnimationManager() {
        speed = ANIMATION_SPEED_DEFAULT;
    }

    public void update(long elapsed) {
        animationPatrol.update(elapsed);
        animationAttackLeft.update(elapsed);
        animationAttackRight.update(elapsed);
        animationHurt.update(elapsed);
    }

    public void changeSpeedForAllAnimations(int speed) {
        animationPatrol.setSpeed(speed);
        animationAttackLeft.setSpeed(speed);
        animationAttackRight.setSpeed(speed);
        animationHurt.setSpeed(speed);
    }

    public Bitmap getCurrentFrame(SeaJelly.State state, Creature.Direction directionOfOpponent) {
        Bitmap imageByState = null;
        switch (state) {
            case PATROL:
                imageByState = animationPatrol.getCurrentFrame();
                break;
            case ATTACK:
                if (directionOfOpponent == Creature.Direction.LEFT) {
                    imageByState = animationAttackLeft.getCurrentFrame();
                } else if (directionOfOpponent == Creature.Direction.RIGHT) {
                    imageByState = animationAttackRight.getCurrentFrame();
                }
                break;
            case HURT:
                imageByState = animationHurt.getCurrentFrame();
                break;
        }
        return imageByState;
    }

    public void init(Game game) {
        if (framesPatrol == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            // CENTER (forward-facing)
            framesPatrol = new Bitmap[3];
            framesPatrol[0] = Bitmap.createBitmap(spriteSheet, 552, 29, 16, 32);
            framesPatrol[1] = Bitmap.createBitmap(spriteSheet, 568, 29, 16, 32);
            framesPatrol[2] = Bitmap.createBitmap(spriteSheet, 584, 29, 16, 32);
        }
        animationPatrol = new Animation(framesPatrol, ANIMATION_SPEED_DEFAULT);

        if (framesAttackLeft == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            framesAttackLeft = new Bitmap[4];
            framesAttackLeft[0] = Bitmap.createBitmap(spriteSheet, 599, 27, 16, 32);
            framesAttackLeft[1] = Bitmap.createBitmap(spriteSheet, 619, 29, 16, 32);
            framesAttackLeft[2] = Bitmap.createBitmap(spriteSheet, 639, 34, 32, 32);
            framesAttackLeft[3] = Bitmap.createBitmap(spriteSheet, 674, 32, 32, 32);
        }
        animationAttackLeft = new Animation(framesAttackLeft, ANIMATION_SPEED_DEFAULT);

        if (framesAttackRight == null) {
            framesAttackRight = Animation.flipImageArrayHorizontally(framesAttackLeft);
        }
        animationAttackRight = new Animation(framesAttackRight, ANIMATION_SPEED_DEFAULT);

        if (framesHurt == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_evo_search_for_eden_chapter1_creatures);
            // CENTER (forward-facing)
            framesHurt = new Bitmap[1];
            framesHurt[0] = Bitmap.createBitmap(spriteSheet, 714, 28, 16, 32);
        }
        animationHurt = new Animation(framesHurt, ANIMATION_SPEED_DEFAULT);
    }
}
