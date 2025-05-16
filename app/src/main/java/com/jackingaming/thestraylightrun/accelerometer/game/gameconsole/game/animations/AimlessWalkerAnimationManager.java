package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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

    transient private HashMap<Creature.Direction, Animation> walkAnimationsBaby;
    transient private HashMap<Creature.Direction, Animation> walkAnimationsAdult;
    transient private Bitmap stateOff;

    public AimlessWalkerAnimationManager() {
        speed = ANIMATION_SPEED_DEFAULT;
    }

    public void update(long elapsed) {
        for (Animation walkAnimation : walkAnimationsBaby.values()) {
            walkAnimation.update(elapsed);
        }
        for (Animation runAnimation : walkAnimationsAdult.values()) {
            runAnimation.update(elapsed);
        }
    }

    public void changeSpeedForAllWalkAnimations(int speed) {
        for (Animation walkAnimation : walkAnimationsBaby.values()) {
            walkAnimation.setSpeed(speed);
        }
    }

    public void changeSpeedForAllRunAnimations(int speed) {
        for (Animation runAnimation : walkAnimationsAdult.values()) {
            runAnimation.setSpeed(speed);
        }
    }

    public void init(Game game) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetBaby = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_hm_chicken);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpBaby = new Bitmap[2];
        walkUpBaby[0] = Bitmap.createBitmap(spriteSheetBaby, 300, 0, 12, 16);
        walkUpBaby[1] = Bitmap.createBitmap(spriteSheetBaby, 300, 30, 12, 16);

        Bitmap[] walkDownBaby = new Bitmap[2];
        walkDownBaby[0] = Bitmap.createBitmap(spriteSheetBaby, 270, 0, 16, 16);
        walkDownBaby[1] = Bitmap.createBitmap(spriteSheetBaby, 270, 30, 16, 16);

        Bitmap[] walkLeftBaby = new Bitmap[2];
        walkLeftBaby[0] = Bitmap.createBitmap(spriteSheetBaby, 240, 0, 16, 16);
        walkLeftBaby[1] = Bitmap.createBitmap(spriteSheetBaby, 240, 30, 16, 16);

        Bitmap[] walkRightBaby = new Bitmap[2];
        walkRightBaby[0] = Animation.flipImageHorizontally(walkLeftBaby[0]);
        walkRightBaby[1] = Animation.flipImageHorizontally(walkLeftBaby[1]);

        walkAnimationsBaby = new HashMap<>();
        walkAnimationsBaby.put(Creature.Direction.UP, new Animation(walkUpBaby, speed));
        walkAnimationsBaby.put(Creature.Direction.DOWN, new Animation(walkDownBaby, speed));
        walkAnimationsBaby.put(Creature.Direction.LEFT, new Animation(walkLeftBaby, speed));
        walkAnimationsBaby.put(Creature.Direction.RIGHT, new Animation(walkRightBaby, speed));

        ////////////////////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetAdult = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_hm_creatures);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Bitmap[] walkUpAdult = new Bitmap[3];
        walkUpAdult[0] = Bitmap.createBitmap(spriteSheetAdult, 147, 144, 16, 16);
        walkUpAdult[1] = Bitmap.createBitmap(spriteSheetAdult, 172, 145, 16, 16);
        walkUpAdult[2] = Bitmap.createBitmap(spriteSheetAdult, 195, 144, 16, 16);

        Bitmap[] walkDownAdult = new Bitmap[3];
        walkDownAdult[0] = Bitmap.createBitmap(spriteSheetAdult, 147, 207, 16, 16);
        walkDownAdult[1] = Bitmap.createBitmap(spriteSheetAdult, 171, 208, 16, 16);
        walkDownAdult[2] = Bitmap.createBitmap(spriteSheetAdult, 195, 207, 16, 16);

        Bitmap[] walkLeftAdult = new Bitmap[3];
        walkLeftAdult[0] = Bitmap.createBitmap(spriteSheetAdult, 147, 239, 16, 16);
        walkLeftAdult[1] = Bitmap.createBitmap(spriteSheetAdult, 171, 240, 16, 16);
        walkLeftAdult[2] = Bitmap.createBitmap(spriteSheetAdult, 195, 239, 16, 16);

        Bitmap[] walkRightAdult = new Bitmap[3];
        walkRightAdult[0] = Bitmap.createBitmap(spriteSheetAdult, 147, 175, 16, 16);
        walkRightAdult[1] = Bitmap.createBitmap(spriteSheetAdult, 171, 176, 16, 16);
        walkRightAdult[2] = Bitmap.createBitmap(spriteSheetAdult, 195, 175, 16, 16);

        walkAnimationsAdult = new HashMap<Creature.Direction, Animation>();
        walkAnimationsAdult.put(Creature.Direction.UP, new Animation(walkUpAdult, speed));
        walkAnimationsAdult.put(Creature.Direction.DOWN, new Animation(walkDownAdult, speed));
        walkAnimationsAdult.put(Creature.Direction.LEFT, new Animation(walkLeftAdult, speed));
        walkAnimationsAdult.put(Creature.Direction.RIGHT, new Animation(walkRightAdult, speed));
    }

    public Bitmap getCurrentFrame(Creature.Direction direction, AimlessWalker.State state, float xMove, float yMove) {
        // TODO:
        switch (state) {
            case OFF:
                return walkAnimationsBaby.get(Creature.Direction.DOWN).getCurrentFrame();
            case WALK:
                if (direction == Creature.Direction.DOWN ||
                        direction == Creature.Direction.LEFT ||
                        direction == Creature.Direction.UP ||
                        direction == Creature.Direction.RIGHT) {
                    return walkAnimationsBaby.get(direction).getCurrentFrame();
                } else {
                    Log.e(TAG, "getCurrentFrame() state is WALK... direction is one that is not defined.");
                    return null;
                }
            case RUN:
                if (direction == Creature.Direction.DOWN ||
                        direction == Creature.Direction.LEFT ||
                        direction == Creature.Direction.UP ||
                        direction == Creature.Direction.RIGHT) {
                    return walkAnimationsAdult.get(direction).getCurrentFrame();
                } else {
                    Log.e(TAG, "getCurrentFrame() state is RUN... direction is one that is not defined.");
                    return null;
                }
            default:
                Log.e(TAG, "getCurrentFrame() switch's default branch");
                return null;
        }
    }
}
