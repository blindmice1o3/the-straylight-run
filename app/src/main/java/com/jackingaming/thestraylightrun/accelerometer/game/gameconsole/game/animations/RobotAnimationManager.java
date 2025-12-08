package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;

import java.io.Serializable;
import java.util.HashMap;

public class RobotAnimationManager
        implements Serializable {
    public static final String TAG = RobotAnimationManager.class.getSimpleName();

    public static final int ANIMATION_SPEED_DEFAULT = 100;

    private int speed;

    transient private HashMap<Creature.Direction, Animation> walkAnimations;
    transient private HashMap<Creature.Direction, Animation> runAnimations;
    transient private Bitmap stateOff;
    transient private Animation spinAnimation;

    public RobotAnimationManager() {
        speed = ANIMATION_SPEED_DEFAULT;
    }

    public void update(long elapsed) {
        for (Animation walkAnimation : walkAnimations.values()) {
            walkAnimation.update(elapsed);
        }
        for (Animation runAnimation : runAnimations.values()) {
            runAnimation.update(elapsed);
        }
        spinAnimation.update(elapsed);
    }

    public void changeSpeedForAllWalkAnimations(int speed) {
        for (Animation walkAnimation : walkAnimations.values()) {
            walkAnimation.setSpeed(speed);
        }
    }

    public void changeSpeedForAllRunAnimations(int speed) {
        for (Animation runAnimation : runAnimations.values()) {
            runAnimation.setSpeed(speed);
        }
    }

    public Bitmap getCurrentFrame(Creature.Direction direction, Robot.State state, float xMove, float yMove) {
        if (state == Robot.State.OFF) {
            return stateOff;
        } else if (state == Robot.State.WALK ||
                state == Robot.State.TILE_SELECTED) {
            if (direction == Creature.Direction.DOWN ||
                    direction == Creature.Direction.LEFT ||
                    direction == Creature.Direction.UP ||
                    direction == Creature.Direction.RIGHT) {
                return walkAnimations.get(direction).getCurrentFrame();
            } else {
                Log.e(TAG, "getCurrentFrame() state is WALK... direction is one that is not defined.");
                return null;
            }
        } else if (state == Robot.State.RUN) {
            if (direction == Creature.Direction.DOWN ||
                    direction == Creature.Direction.LEFT ||
                    direction == Creature.Direction.UP ||
                    direction == Creature.Direction.RIGHT) {
                return runAnimations.get(direction).getCurrentFrame();
            } else {
                Log.e(TAG, "getCurrentFrame() state is RUN... direction is one that is not defined.");
                return null;
            }
        } else if (state == Robot.State.SPIN) {
            return spinAnimation.getCurrentFrame();
        } else {
            Log.e(TAG, "getCurrentFrame() else-clause... state is unknown.");
            return null;
        }
    }

    public void init(Game game) {
//        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.snes_chrono_trigger_r_series);
//
//        //crop all non-pink sprites////////////////////////////////////////////////////
//        Bitmap[][] robotRSeries = new Bitmap[22][11];
//
//        int widthSprite = 32;
//        int heightSprite = 32;
//        int offsetHorizontal = 8;
//        int offsetVertical = 8;
//        for (int row = 0; row < robotRSeries.length; row++) {
//            for (int column = 0; column < robotRSeries[row].length; column++) {
//                int xStart = column * (widthSprite + offsetHorizontal);
//                int yStart = row * (heightSprite + offsetVertical);
//                robotRSeries[row][column] = Bitmap.createBitmap(spriteSheet, xStart, yStart,
//                        widthSprite, heightSprite);
//            }
//        }
//        ///////////////////////////////////////////////////////////////////////////////
//
//        //group sprites by animationWalk category//////////////////////////////////////////
//        Bitmap[] walkDown = new Bitmap[5];
//        Bitmap[] walkLeft = new Bitmap[5];
//        Bitmap[] walkUp = new Bitmap[5];
//        Bitmap[] walkRight = new Bitmap[5];
//        Bitmap[] runDown = new Bitmap[5];
//        Bitmap[] runLeft = new Bitmap[5];
//        Bitmap[] runUp = new Bitmap[5];
//        Bitmap[] runRight = new Bitmap[5];
//        int xIndexStart = 3;
//        int yIndexStart = 17;
//        for (int xIndex = xIndexStart; xIndex < robotRSeries[0].length; xIndex++) {
//            for (int yIndex = yIndexStart; yIndex < robotRSeries.length; yIndex++) {
//                //walkDown
//                if (xIndex == 3) {
//                    walkDown[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //walkLeft
//                else if (xIndex == 4) {
//                    walkLeft[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //walkUp
//                else if (xIndex == 5) {
//                    walkUp[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //walkRight
//                else if (xIndex == 6) {
//                    walkRight[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //runDown
//                else if (xIndex == 7) {
//                    runDown[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //runLeft
//                else if (xIndex == 8) {
//                    runLeft[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //runUp
//                else if (xIndex == 9) {
//                    runUp[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//                //runRight
//                else if (xIndex == 10) {
//                    runRight[yIndex - yIndexStart] = robotRSeries[yIndex][xIndex];
//                }
//            }
//        }

        ///////////////////////////////////////////////////////////////////////////////
        Bitmap spriteSheetRobotDown = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_robot_down_redo_eyes);
        Bitmap spriteSheetRobotUp = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_robot_up);
        Bitmap spriteSheetRobotRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_robot_right);
        Bitmap spriteSheetRobotOff = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_robot_off);
        Bitmap[] walkDown = new Bitmap[3];
        Bitmap[] walkLeft = new Bitmap[2];
        Bitmap[] walkUp = new Bitmap[3];
        Bitmap[] walkRight = new Bitmap[2];
        Bitmap[] runDown = new Bitmap[3];
        Bitmap[] runLeft = new Bitmap[2];
        Bitmap[] runUp = new Bitmap[3];
        Bitmap[] runRight = new Bitmap[2];

        walkDown[0] = Bitmap.createBitmap(spriteSheetRobotDown, 22, 214, 392, 613);
        walkDown[1] = Bitmap.createBitmap(spriteSheetRobotDown, 472, 211, 470, 614);
        walkDown[2] = Bitmap.createBitmap(spriteSheetRobotDown, 969, 213, 416, 613);

        walkUp[0] = Bitmap.createBitmap(spriteSheetRobotUp, 0, 173, 413, 670);
        walkUp[1] = Bitmap.createBitmap(spriteSheetRobotUp, 470, 173, 416, 672);
        walkUp[2] = Bitmap.createBitmap(spriteSheetRobotUp, 962, 173, 430, 670);

        walkRight[0] = Bitmap.createBitmap(spriteSheetRobotRight, 210, 256, 452, 622);
        walkRight[1] = Bitmap.createBitmap(spriteSheetRobotRight, 910, 256, 378, 621);

        walkLeft[0] = Animation.flipImageHorizontally(walkRight[0]);
        walkLeft[1] = Animation.flipImageHorizontally(walkRight[1]);

        for (int indexDown = 0; indexDown < walkDown.length; indexDown++) {
            runDown[indexDown] = walkDown[indexDown];
        }

        for (int indexUp = 0; indexUp < walkUp.length; indexUp++) {
            runUp[indexUp] = walkUp[indexUp];
        }

        for (int indexRight = 0; indexRight < walkRight.length; indexRight++) {
            runRight[indexRight] = walkRight[indexRight];
        }

        for (int indexLeft = 0; indexLeft < walkLeft.length; indexLeft++) {
            runLeft[indexLeft] = walkLeft[indexLeft];
        }

        Animation walkDownAnimation = new Animation(walkDown, speed);
        Animation walkLeftAnimation = new Animation(walkLeft, speed);
        Animation walkUpAnimation = new Animation(walkUp, speed);
        Animation walkRightAnimation = new Animation(walkRight, speed);

        Animation runDownAnimation = new Animation(runDown, speed);
        Animation runLeftAnimation = new Animation(runLeft, speed);
        Animation runUpAnimation = new Animation(runUp, speed);
        Animation runRightAnimation = new Animation(runRight, speed);

        walkAnimations = new HashMap<Creature.Direction, Animation>();
        walkAnimations.put(Creature.Direction.DOWN, walkDownAnimation);
        walkAnimations.put(Creature.Direction.LEFT, walkLeftAnimation);
        walkAnimations.put(Creature.Direction.UP, walkUpAnimation);
        walkAnimations.put(Creature.Direction.RIGHT, walkRightAnimation);

        runAnimations = new HashMap<Creature.Direction, Animation>();
        runAnimations.put(Creature.Direction.DOWN, runDownAnimation);
        runAnimations.put(Creature.Direction.LEFT, runLeftAnimation);
        runAnimations.put(Creature.Direction.UP, runUpAnimation);
        runAnimations.put(Creature.Direction.RIGHT, runRightAnimation);

//        stateOff = robotRSeries[6][1];
        stateOff = Bitmap.createBitmap(spriteSheetRobotOff, 116, 379, 802, 830);

        Bitmap[] spin = new Bitmap[4];
        spin[0] = walkLeft[0];
        spin[1] = walkDown[0];
        spin[2] = walkRight[0];
        spin[3] = walkUp[0];
        spinAnimation = new Animation(spin, speed);
    }
}
