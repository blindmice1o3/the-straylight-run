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
        Bitmap spriteSheetChickDown = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_chick_down);
        Bitmap spriteSheetChickUp = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_chick_up);
        Bitmap spriteSheetChickRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_chick_right);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpChick = new Bitmap[3];
        walkUpChick[0] = Bitmap.createBitmap(spriteSheetChickUp, 197, 325, 294, 376);
        walkUpChick[1] = Bitmap.createBitmap(spriteSheetChickUp, 623, 325, 289, 377);
        walkUpChick[2] = Bitmap.createBitmap(spriteSheetChickUp, 1048, 325, 291, 375);

        Bitmap[] walkDownChick = new Bitmap[2];
        walkDownChick[0] = Bitmap.createBitmap(spriteSheetChickDown, 177, 343, 299, 356);
        walkDownChick[1] = Bitmap.createBitmap(spriteSheetChickDown, 625, 342, 284, 358);

        Bitmap[] walkRightChick = new Bitmap[3];
        walkRightChick[0] = Bitmap.createBitmap(spriteSheetChickRight, 152, 352, 373, 362);
        walkRightChick[1] = Bitmap.createBitmap(spriteSheetChickRight, 596, 352, 364, 362);
        walkRightChick[2] = Bitmap.createBitmap(spriteSheetChickRight, 1058, 352, 369, 371);

        Bitmap[] walkLeftChick = new Bitmap[3];
        walkLeftChick[0] = Animation.flipImageHorizontally(walkRightChick[0]);
        walkLeftChick[1] = Animation.flipImageHorizontally(walkRightChick[1]);
        walkLeftChick[2] = Animation.flipImageHorizontally(walkRightChick[2]);


        walkAnimations.put(Creature.Direction.UP, new Animation(walkUpChick, speed));
        walkAnimations.put(Creature.Direction.DOWN, new Animation(walkDownChick, speed));
        walkAnimations.put(Creature.Direction.LEFT, new Animation(walkLeftChick, speed));
        walkAnimations.put(Creature.Direction.RIGHT, new Animation(walkRightChick, speed));
    }

    private void initWalkAnimationsForChicken(Game game) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetChickenDownAndUp = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_chicken_down_and_up);
        Bitmap spriteSheetChickenRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_chicken_right);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpChicken = new Bitmap[1];
        walkUpChicken[0] = Bitmap.createBitmap(spriteSheetChickenDownAndUp, 565, 271, 396, 545);

        Bitmap[] walkDownChicken = new Bitmap[2];
        walkDownChicken[0] = Bitmap.createBitmap(spriteSheetChickenDownAndUp, 84, 273, 395, 537);
        walkDownChicken[1] = Bitmap.createBitmap(spriteSheetChickenDownAndUp, 1060, 270, 415, 543);

        Bitmap[] walkRightChicken = new Bitmap[2];
        walkRightChicken[0] = Bitmap.createBitmap(spriteSheetChickenRight, 176, 353, 342, 398);
        walkRightChicken[1] = Bitmap.createBitmap(spriteSheetChickenRight, 585, 353, 349, 400);

        Bitmap[] walkLeftChicken = new Bitmap[2];
        walkLeftChicken[0] = Animation.flipImageHorizontally(walkRightChicken[0]);
        walkLeftChicken[1] = Animation.flipImageHorizontally(walkRightChicken[1]);

        walkAnimations.put(Creature.Direction.UP, new Animation(walkUpChicken, speed));
        walkAnimations.put(Creature.Direction.DOWN, new Animation(walkDownChicken, speed));
        walkAnimations.put(Creature.Direction.LEFT, new Animation(walkLeftChicken, speed));
        walkAnimations.put(Creature.Direction.RIGHT, new Animation(walkRightChicken, speed));
    }

    private void initWalkAnimationsForSheep(Game game) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetSheepDown = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_sheep_down_adjusted_legs);
        Bitmap spriteSheetSheepUp = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_sheep_up);
        Bitmap spriteSheetSheepRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_sheep_right);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpSheep = new Bitmap[3];
        walkUpSheep[0] = Bitmap.createBitmap(spriteSheetSheepUp, 144, 262, 379, 503);
        walkUpSheep[1] = Bitmap.createBitmap(spriteSheetSheepUp, 570, 262, 388, 503);
        walkUpSheep[2] = Bitmap.createBitmap(spriteSheetSheepUp, 1006, 262, 374, 503);

        Bitmap[] walkDownSheep = new Bitmap[3];
        walkDownSheep[0] = Bitmap.createBitmap(spriteSheetSheepDown, 143, 284, 376, 454);
        walkDownSheep[1] = Bitmap.createBitmap(spriteSheetSheepDown, 587, 284, 376, 454);
        walkDownSheep[2] = Bitmap.createBitmap(spriteSheetSheepDown, 1005, 284, 386, 454);

        Bitmap[] walkRightSheep = new Bitmap[3];
        walkRightSheep[0] = Bitmap.createBitmap(spriteSheetSheepRight, 47, 342, 502, 349);
        walkRightSheep[1] = Bitmap.createBitmap(spriteSheetSheepRight, 558, 342, 462, 349);
        walkRightSheep[2] = Bitmap.createBitmap(spriteSheetSheepRight, 1045, 342, 447, 349);

        Bitmap[] walkLeftSheep = new Bitmap[3];
        walkLeftSheep[0] = Animation.flipImageHorizontally(walkRightSheep[0]);
        walkLeftSheep[1] = Animation.flipImageHorizontally(walkRightSheep[1]);
        walkLeftSheep[2] = Animation.flipImageHorizontally(walkRightSheep[2]);

        walkAnimations.put(Creature.Direction.UP, new Animation(walkUpSheep, speed));
        walkAnimations.put(Creature.Direction.DOWN, new Animation(walkDownSheep, speed));
        walkAnimations.put(Creature.Direction.LEFT, new Animation(walkLeftSheep, speed));
        walkAnimations.put(Creature.Direction.RIGHT, new Animation(walkRightSheep, speed));
    }

    private void initWalkAnimationsForCow(Game game) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetCowDown = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_cow_down);
        Bitmap spriteSheetCowUp = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_cow_up);
        Bitmap spriteSheetCowRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_cow_right);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpCow = new Bitmap[3];
        walkUpCow[0] = Bitmap.createBitmap(spriteSheetCowUp, 176, 315, 250, 438);
        walkUpCow[1] = Bitmap.createBitmap(spriteSheetCowUp, 636, 312, 258, 441);
        walkUpCow[2] = Bitmap.createBitmap(spriteSheetCowUp, 1100, 315, 258, 441);

        Bitmap[] walkDownCow = new Bitmap[3];
        walkDownCow[0] = Bitmap.createBitmap(spriteSheetCowDown, 128, 290, 320, 454);
        walkDownCow[1] = Bitmap.createBitmap(spriteSheetCowDown, 603, 286, 324, 456);
        walkDownCow[2] = Bitmap.createBitmap(spriteSheetCowDown, 1073, 288, 320, 451);

        Bitmap[] walkRightCow = new Bitmap[3];
        walkRightCow[0] = Bitmap.createBitmap(spriteSheetCowRight, 71, 374, 430, 323);
        walkRightCow[1] = Bitmap.createBitmap(spriteSheetCowRight, 561, 375, 453, 327);
        walkRightCow[2] = Bitmap.createBitmap(spriteSheetCowRight, 1055, 369, 404, 332);

        Bitmap[] walkLeftCow = new Bitmap[3];
        walkLeftCow[0] = Animation.flipImageHorizontally(walkRightCow[0]);
        walkLeftCow[1] = Animation.flipImageHorizontally(walkRightCow[1]);
        walkLeftCow[2] = Animation.flipImageHorizontally(walkRightCow[2]);

        walkAnimations.put(Creature.Direction.UP, new Animation(walkUpCow, speed));
        walkAnimations.put(Creature.Direction.DOWN, new Animation(walkDownCow, speed));
        walkAnimations.put(Creature.Direction.LEFT, new Animation(walkLeftCow, speed));
        walkAnimations.put(Creature.Direction.RIGHT, new Animation(walkRightCow, speed));
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
            case SHEEP:
                initWalkAnimationsForSheep(game);
                break;
            case COW:
                initWalkAnimationsForCow(game);
                break;
            default:
                break;
        }
    }

    public Bitmap getCurrentFrame(Creature.Direction direction, AimlessWalker.State state, float xMove, float yMove) {
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

    public void setType(AimlessWalker.Type type) {
        this.type = type;
    }
}
