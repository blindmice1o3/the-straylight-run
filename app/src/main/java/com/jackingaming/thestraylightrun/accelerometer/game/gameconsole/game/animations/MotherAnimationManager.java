package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.MotherForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.WorldScene;

import java.io.Serializable;
import java.util.HashMap;

public class MotherAnimationManager
        implements Serializable {
    public static final int ANIMATION_SPEED_DEFAULT = 100;

    private int speed;

    transient private HashMap<Creature.Direction, Animation> animations;
    transient private Bitmap motherDefaultUp, motherDefaultDown, motherDefaultLeft, motherDefaultRight;
    transient private Bitmap motherDefaultUpLeft, motherDefaultUpRight, motherDefaultDownLeft, motherDefaultDownRight;

    public MotherAnimationManager() {
        speed = ANIMATION_SPEED_DEFAULT;
    }

    public void update(long elapsed) {
        for (Animation animation : animations.values()) {
            animation.update(elapsed);
        }
    }

    public void changeSpeedForAllAnimations(int speed) {
        for (Animation animation : animations.values()) {
            animation.setSpeed(speed);
        }
    }

    public Bitmap getCurrentFrame(Creature.Direction direction, float xMove, float yMove) {
        Bitmap imageByDirection = null;

        // moving
        if ((xMove != 0) || (yMove != 0)) {
            Animation animationByDirection = animations.get(direction);
            imageByDirection = animationByDirection.getCurrentFrame();
        }
        // standing still
        else {
            switch (direction) {
                case UP:
                    imageByDirection = motherDefaultUp;
                    break;
                case DOWN:
                    imageByDirection = motherDefaultDown;
                    break;
                case LEFT:
                    imageByDirection = motherDefaultLeft;
                    break;
                case RIGHT:
                    imageByDirection = motherDefaultRight;
                    break;
                case CENTER:
                    imageByDirection = motherDefaultDown;
                    break;
                case UP_LEFT:
                    imageByDirection = motherDefaultLeft;
                    break;
                case UP_RIGHT:
                    imageByDirection = motherDefaultRight;
                    break;
                case DOWN_LEFT:
                    imageByDirection = motherDefaultLeft;
                    break;
                case DOWN_RIGHT:
                    imageByDirection = motherDefaultRight;
                    break;
            }
        }
        return imageByDirection;
    }

    public void init(Game game, MotherForm.Type type) {
        switch (type) {
            case MOTHER:
                Bitmap spriteSheetMother = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_mother_down_up_right);

                Bitmap[] motherUp = new Bitmap[2];
                motherUp[0] = Bitmap.createBitmap(spriteSheetMother, 410, 26, 207, 432);
                motherUp[1] = Bitmap.createBitmap(spriteSheetMother, 406, 485, 211, 432);

                Bitmap[] motherDown = new Bitmap[2];
                motherDown[0] = Bitmap.createBitmap(spriteSheetMother, 95, 27, 205, 436);
                motherDown[1] = Bitmap.createBitmap(spriteSheetMother, 725, 26, 208, 436);

                Bitmap[] motherRight = new Bitmap[2];
                motherRight[0] = Bitmap.createBitmap(spriteSheetMother, 94, 484, 208, 446);
                motherRight[1] = Bitmap.createBitmap(spriteSheetMother, 720, 484, 204, 446);

                Bitmap[] motherLeft = new Bitmap[2];
                motherLeft[0] = Animation.flipImageHorizontally(motherRight[0]);
                motherLeft[1] = Animation.flipImageHorizontally(motherRight[1]);

                Bitmap[] motherUpLeft = new Bitmap[2];
                motherUpLeft[0] = motherLeft[0];
                motherUpLeft[1] = motherLeft[1];

                Bitmap[] motherUpRight = new Bitmap[2];
                motherUpRight[0] = motherRight[0];
                motherUpRight[1] = motherRight[1];

                Bitmap[] motherDownLeft = new Bitmap[2];
                motherDownLeft[0] = motherLeft[0];
                motherDownLeft[1] = motherLeft[1];

                Bitmap[] motherDownRight = new Bitmap[2];
                motherDownRight[0] = motherRight[0];
                motherDownRight[1] = motherRight[1];

                motherDefaultUp = motherUp[0];
                motherDefaultDown = motherDown[0];
                motherDefaultLeft = motherLeft[0];
                motherDefaultRight = motherRight[0];
                motherDefaultUpLeft = motherLeft[0];
                motherDefaultUpRight = motherRight[0];
                motherDefaultDownLeft = motherLeft[0];
                motherDefaultDownRight = motherRight[0];

                Bitmap[] motherCenter = {motherDefaultDown};

                Animation motherUpAnimation = new Animation(motherUp, speed);
                Animation motherDownAnimation = new Animation(motherDown, speed);
                Animation motherLeftAnimation = new Animation(motherLeft, speed);
                Animation motherRightAnimation = new Animation(motherRight, speed);
                Animation motherCenterAnimation = new Animation(motherCenter, speed);
                Animation motherUpLeftAnimation = new Animation(motherUpLeft, speed);
                Animation motherUpRightAnimation = new Animation(motherUpRight, speed);
                Animation motherDownLeftAnimation = new Animation(motherDownLeft, speed);
                Animation motherDownRightAnimation = new Animation(motherDownRight, speed);

                animations = new HashMap<Creature.Direction, Animation>();
                animations.put(Creature.Direction.UP, motherUpAnimation);
                animations.put(Creature.Direction.DOWN, motherDownAnimation);
                animations.put(Creature.Direction.LEFT, motherLeftAnimation);
                animations.put(Creature.Direction.RIGHT, motherRightAnimation);
                animations.put(Creature.Direction.CENTER, motherCenterAnimation);
                animations.put(Creature.Direction.UP_LEFT, motherUpLeftAnimation);
                animations.put(Creature.Direction.UP_RIGHT, motherUpRightAnimation);
                animations.put(Creature.Direction.DOWN_LEFT, motherDownLeftAnimation);
                animations.put(Creature.Direction.DOWN_RIGHT, motherDownRightAnimation);

                break;
            case DAUGHTER:
                Bitmap spriteSheetDaughter = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_daughter_down_up_right);

                Bitmap[] daughterUp = new Bitmap[1];
                daughterUp[0] = Bitmap.createBitmap(spriteSheetDaughter, 713, 74, 193, 431);

                Bitmap[] daughterDown = new Bitmap[2];
                daughterDown[0] = Bitmap.createBitmap(spriteSheetDaughter, 99, 71, 184, 432);
                daughterDown[1] = Bitmap.createBitmap(spriteSheetDaughter, 411, 74, 187, 431);

                Bitmap[] daughterRight = new Bitmap[3];
                daughterRight[0] = Bitmap.createBitmap(spriteSheetDaughter, 99, 541, 201, 422);
                daughterRight[1] = Bitmap.createBitmap(spriteSheetDaughter, 703, 542, 226, 429);
                daughterRight[2] = Bitmap.createBitmap(spriteSheetDaughter, 694, 1010, 234, 429);

                Bitmap[] daughterLeft = new Bitmap[3];
                daughterLeft[0] = Animation.flipImageHorizontally(daughterRight[0]);
                daughterLeft[1] = Animation.flipImageHorizontally(daughterRight[1]);
                daughterLeft[2] = Animation.flipImageHorizontally(daughterRight[2]);

                Bitmap[] daughterUpLeft = new Bitmap[3];
                daughterUpLeft[0] = daughterLeft[0];
                daughterUpLeft[1] = daughterLeft[1];
                daughterUpLeft[2] = daughterLeft[2];

                Bitmap[] daughterUpRight = new Bitmap[3];
                daughterUpRight[0] = daughterRight[0];
                daughterUpRight[1] = daughterRight[1];
                daughterUpRight[2] = daughterRight[2];

                Bitmap[] daughterDownLeft = new Bitmap[3];
                daughterDownLeft[0] = daughterLeft[0];
                daughterDownLeft[1] = daughterLeft[1];
                daughterDownLeft[2] = daughterLeft[2];

                Bitmap[] daughterDownRight = new Bitmap[3];
                daughterDownRight[0] = daughterRight[0];
                daughterDownRight[1] = daughterRight[1];
                daughterDownRight[2] = daughterRight[2];

                motherDefaultUp = daughterUp[0];
                motherDefaultDown = daughterDown[0];
                motherDefaultLeft = daughterLeft[0];
                motherDefaultRight = daughterRight[0];
                motherDefaultUpLeft = daughterLeft[0];
                motherDefaultUpRight = daughterRight[0];
                motherDefaultDownLeft = daughterLeft[0];
                motherDefaultDownRight = daughterRight[0];

                Bitmap[] daughterCenter = {daughterDown[0]};

                Animation daughterUpAnimation = new Animation(daughterUp, speed);
                Animation daughterDownAnimation = new Animation(daughterDown, speed);
                Animation daughterLeftAnimation = new Animation(daughterLeft, speed);
                Animation daughterRightAnimation = new Animation(daughterRight, speed);
                Animation daughterCenterAnimation = new Animation(daughterCenter, speed);
                Animation daughterUpLeftAnimation = new Animation(daughterUpLeft, speed);
                Animation daughterUpRightAnimation = new Animation(daughterUpRight, speed);
                Animation daughterDownLeftAnimation = new Animation(daughterDownLeft, speed);
                Animation daughterDownRightAnimation = new Animation(daughterDownRight, speed);

                animations = new HashMap<Creature.Direction, Animation>();
                animations.put(Creature.Direction.UP, daughterUpAnimation);
                animations.put(Creature.Direction.DOWN, daughterDownAnimation);
                animations.put(Creature.Direction.LEFT, daughterLeftAnimation);
                animations.put(Creature.Direction.RIGHT, daughterRightAnimation);
                animations.put(Creature.Direction.CENTER, daughterCenterAnimation);
                animations.put(Creature.Direction.UP_LEFT, daughterUpLeftAnimation);
                animations.put(Creature.Direction.UP_RIGHT, daughterUpRightAnimation);
                animations.put(Creature.Direction.DOWN_LEFT, daughterDownLeftAnimation);
                animations.put(Creature.Direction.DOWN_RIGHT, daughterDownRightAnimation);

                break;
        }
    }
}
