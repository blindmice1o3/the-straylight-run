package com.jackingaming.thestraylightrun.accelerometer.game.entities;

import android.animation.ObjectAnimator;

import java.util.Map;
import java.util.Random;

public class RivalLeader extends Entity {
    private Random random = new Random();

    private ObjectAnimator objectAnimatorUp = ObjectAnimator.ofInt(sprites.get(Direction.UP), "index", 0, sprites.get(Direction.UP).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorDown = ObjectAnimator.ofInt(sprites.get(Direction.DOWN), "index", 0, sprites.get(Direction.DOWN).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofInt(sprites.get(Direction.LEFT), "index", 0, sprites.get(Direction.LEFT).getNumberOfFrames() - 1);
    private ObjectAnimator objectAnimatorRight = ObjectAnimator.ofInt(sprites.get(Direction.RIGHT), "index", 0, sprites.get(Direction.RIGHT).getNumberOfFrames() - 1);

    public RivalLeader(Map<Direction, Animation> sprites,
                       CollisionListener collisionListener, MovementListener movementListener) {
        super(sprites, collisionListener, movementListener);

        sprites.get(Direction.UP).setIndex(1);
        sprites.get(Direction.DOWN).setIndex(1);
        sprites.get(Direction.LEFT).setIndex(0);
        sprites.get(Direction.RIGHT).setIndex(0);

//        objectAnimatorUp.setDuration(500);
//        objectAnimatorUp.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimatorUp.start();
//
//        objectAnimatorDown.setDuration(500);
//        objectAnimatorDown.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimatorDown.start();
//
//        objectAnimatorLeft.setDuration(500);
//        objectAnimatorLeft.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimatorLeft.start();
//
//        objectAnimatorRight.setDuration(500);
//        objectAnimatorRight.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimatorRight.start();
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update() {
        // DIRECTION (changes 10% of the time)
        if (random.nextInt(10) < 1) {
            // determine direction
            switch (random.nextInt(4)) {
                case 0:
                    direction = Direction.UP;
                    break;
                case 1:
                    direction = Direction.DOWN;
                    break;
                case 2:
                    direction = Direction.LEFT;
                    break;
                case 3:
                    direction = Direction.RIGHT;
                    break;
            }
        }
    }
}
