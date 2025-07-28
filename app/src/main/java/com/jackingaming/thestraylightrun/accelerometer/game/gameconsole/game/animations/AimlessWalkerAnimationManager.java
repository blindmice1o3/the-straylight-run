package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;

import java.io.Serializable;
import java.util.HashMap;

public class AimlessWalkerAnimationManager
        implements Serializable {
    public static final String TAG = AimlessWalkerAnimationManager.class.getSimpleName();

    public static final int ANIMATION_SPEED_DEFAULT = 100;

    private int speed;
    private AimlessWalker.Type type;

    transient private HashMap<Creature.Direction, Animation> walkAnimations;
//    transient private HashMap<Creature.Direction, Animation> walkAnimationsAdult;
//    transient private Bitmap stateOff;

    public AimlessWalkerAnimationManager(AimlessWalker.Type type) {
        speed = ANIMATION_SPEED_DEFAULT;
        this.type = type;
    }

    public void update(long elapsed) {
        for (Animation walkAnimation : walkAnimations.values()) {
            walkAnimation.update(elapsed);
        }
//        for (Animation runAnimation : walkAnimationsAdult.values()) {
//            runAnimation.update(elapsed);
//        }
    }

    public void changeSpeedForAllWalkAnimations(int speed) {
        for (Animation walkAnimation : walkAnimations.values()) {
            walkAnimation.setSpeed(speed);
        }
    }

//    public void changeSpeedForAllRunAnimations(int speed) {
//        for (Animation runAnimation : walkAnimationsAdult.values()) {
//            runAnimation.setSpeed(speed);
//        }
//    }

    private void initWalkAnimationsForChick(Game game) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetChick = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_hm_chicken);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpChick = new Bitmap[2];
        walkUpChick[0] = Bitmap.createBitmap(spriteSheetChick, 300, 0, 12, 16);
        walkUpChick[1] = Bitmap.createBitmap(spriteSheetChick, 300, 30, 12, 16);

        Bitmap[] walkDownChick = new Bitmap[2];
        walkDownChick[0] = Bitmap.createBitmap(spriteSheetChick, 270, 0, 16, 16);
        walkDownChick[1] = Bitmap.createBitmap(spriteSheetChick, 270, 30, 16, 16);

        Bitmap[] walkLeftChick = new Bitmap[2];
        walkLeftChick[0] = Bitmap.createBitmap(spriteSheetChick, 240, 0, 16, 16);
        walkLeftChick[1] = Bitmap.createBitmap(spriteSheetChick, 240, 30, 16, 16);

        Bitmap[] walkRightChick = new Bitmap[2];
        walkRightChick[0] = Animation.flipImageHorizontally(walkLeftChick[0]);
        walkRightChick[1] = Animation.flipImageHorizontally(walkLeftChick[1]);

        walkAnimations.put(Creature.Direction.UP, new Animation(walkUpChick, speed));
        walkAnimations.put(Creature.Direction.DOWN, new Animation(walkDownChick, speed));
        walkAnimations.put(Creature.Direction.LEFT, new Animation(walkLeftChick, speed));
        walkAnimations.put(Creature.Direction.RIGHT, new Animation(walkRightChick, speed));
    }

    private void initWalkAnimationsForChicken(Game game) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetChicken = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_hm_creatures);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpChicken = new Bitmap[3];
        walkUpChicken[0] = Bitmap.createBitmap(spriteSheetChicken, 147, 144, 16, 16);
        walkUpChicken[1] = Bitmap.createBitmap(spriteSheetChicken, 172, 145, 16, 16);
        walkUpChicken[2] = Bitmap.createBitmap(spriteSheetChicken, 195, 144, 16, 16);

        Bitmap[] walkDownChicken = new Bitmap[3];
        walkDownChicken[0] = Bitmap.createBitmap(spriteSheetChicken, 147, 207, 16, 16);
        walkDownChicken[1] = Bitmap.createBitmap(spriteSheetChicken, 171, 208, 16, 16);
        walkDownChicken[2] = Bitmap.createBitmap(spriteSheetChicken, 195, 207, 16, 16);

        Bitmap[] walkLeftChicken = new Bitmap[3];
        walkLeftChicken[0] = Bitmap.createBitmap(spriteSheetChicken, 147, 239, 16, 16);
        walkLeftChicken[1] = Bitmap.createBitmap(spriteSheetChicken, 171, 240, 16, 16);
        walkLeftChicken[2] = Bitmap.createBitmap(spriteSheetChicken, 195, 239, 16, 16);

        Bitmap[] walkRightChicken = new Bitmap[3];
        walkRightChicken[0] = Bitmap.createBitmap(spriteSheetChicken, 147, 175, 16, 16);
        walkRightChicken[1] = Bitmap.createBitmap(spriteSheetChicken, 171, 176, 16, 16);
        walkRightChicken[2] = Bitmap.createBitmap(spriteSheetChicken, 195, 175, 16, 16);

        walkAnimations.put(Creature.Direction.UP, new Animation(walkUpChicken, speed));
        walkAnimations.put(Creature.Direction.DOWN, new Animation(walkDownChicken, speed));
        walkAnimations.put(Creature.Direction.LEFT, new Animation(walkLeftChicken, speed));
        walkAnimations.put(Creature.Direction.RIGHT, new Animation(walkRightChicken, speed));
    }

    public void init(Game game) {
        walkAnimations = new HashMap<>();

        switch (type) {
            case CHICK:
                initWalkAnimationsForChick(game);
                break;
            case CHICKEN:
                initWalkAnimationsForChicken(game);
                break;
            case CALF:
                break;
            case COW:
                break;
            default:
                break;
        }
    }

    public Bitmap getCurrentFrame(Creature.Direction direction, AimlessWalker.State state, float xMove, float yMove) {
        // TODO:

//        switch (state) {
//            case OFF:
//                return walkAnimationsBaby.get(Creature.Direction.DOWN).getCurrentFrame();
//            case WALK:
//                if (direction == Creature.Direction.DOWN ||
//                        direction == Creature.Direction.LEFT ||
//                        direction == Creature.Direction.UP ||
//                        direction == Creature.Direction.RIGHT) {
//                    return walkAnimationsBaby.get(direction).getCurrentFrame();
//                } else {
//                    Log.e(TAG, "getCurrentFrame() state is WALK... direction is one that is not defined.");
//                    return null;
//                }
//            case RUN:
//                if (direction == Creature.Direction.DOWN ||
//                        direction == Creature.Direction.LEFT ||
//                        direction == Creature.Direction.UP ||
//                        direction == Creature.Direction.RIGHT) {
//                    return walkAnimationsAdult.get(direction).getCurrentFrame();
//                } else {
//                    Log.e(TAG, "getCurrentFrame() state is RUN... direction is one that is not defined.");
//                    return null;
//                }
//            default:
//                Log.e(TAG, "getCurrentFrame() switch's default branch");
//                return null;
//        }
        if (direction == Creature.Direction.DOWN ||
                direction == Creature.Direction.LEFT ||
                direction == Creature.Direction.UP ||
                direction == Creature.Direction.RIGHT) {
            return walkAnimations.get(direction).getCurrentFrame();
        }
        return null;
    }
}
