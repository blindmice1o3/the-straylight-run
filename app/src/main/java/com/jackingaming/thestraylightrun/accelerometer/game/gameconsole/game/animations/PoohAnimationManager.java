package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;

import java.io.Serializable;
import java.util.HashMap;

public class PoohAnimationManager
        implements Serializable {
    public static final int ANIMATION_SPEED_DEFAULT = 100;

    private int speed;

    transient private HashMap<Creature.Direction, Animation> animations;
    transient private Bitmap poohDefaultUp, poohDefaultDown, poohDefaultLeft, poohDefaultRight;
    transient private Bitmap poohDefaultUpLeft, poohDefaultUpRight, poohDefaultDownLeft, poohDefaultDownRight;

    public PoohAnimationManager() {
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
                    imageByDirection = poohDefaultUp;
                    break;
                case DOWN:
                    imageByDirection = poohDefaultDown;
                    break;
                case LEFT:
                    imageByDirection = poohDefaultLeft;
                    break;
                case RIGHT:
                    imageByDirection = poohDefaultRight;
                    break;
                case CENTER:
                    imageByDirection = poohDefaultDown;
                    break;
                case UP_LEFT:
                    imageByDirection = poohDefaultUpLeft;
                    break;
                case UP_RIGHT:
                    imageByDirection = poohDefaultUpRight;
                    break;
                case DOWN_LEFT:
                    imageByDirection = poohDefaultDownLeft;
                    break;
                case DOWN_RIGHT:
                    imageByDirection = poohDefaultDownRight;
                    break;
            }
        }
        return imageByDirection;
    }

    public void init(Game game) {
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.gba_kingdom_hearts_chain_of_memories_winnie_the_pooh);

        Bitmap spriteSheetDippyUp = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_dippy_up);
        Bitmap[] dippyUp = new Bitmap[4];
        dippyUp[0] = Bitmap.createBitmap(spriteSheetDippyUp, 57, 392, 145, 285);
        dippyUp[1] = Bitmap.createBitmap(spriteSheetDippyUp, 290, 392, 145, 285);
        dippyUp[2] = Bitmap.createBitmap(spriteSheetDippyUp, 524, 392, 155, 285);
        dippyUp[3] = Bitmap.createBitmap(spriteSheetDippyUp, 769, 392, 145, 285);

//        Bitmap[] poohUp = new Bitmap[11];
//        poohUp[0] = Bitmap.createBitmap(spriteSheet, 21, 1163, 27, 39);
//        poohUp[1] = Bitmap.createBitmap(spriteSheet, 62, 1164, 27, 38);
//        poohUp[2] = Bitmap.createBitmap(spriteSheet, 102, 1163, 28, 39);
//        poohUp[3] = Bitmap.createBitmap(spriteSheet, 142, 1162, 28, 40);
//        poohUp[4] = Bitmap.createBitmap(spriteSheet, 184, 1162, 26, 40);
//        poohUp[5] = Bitmap.createBitmap(spriteSheet, 229, 1162, 24, 40);
//        poohUp[6] = Bitmap.createBitmap(spriteSheet, 269, 1164, 26, 38);
//        poohUp[7] = Bitmap.createBitmap(spriteSheet, 311, 1164, 26, 38);
//        poohUp[8] = Bitmap.createBitmap(spriteSheet, 348, 1163, 28, 39);
//        poohUp[9] = Bitmap.createBitmap(spriteSheet, 392, 1163, 27, 39);
//        poohUp[10] = Bitmap.createBitmap(spriteSheet, 430, 1162, 26, 40);

        Bitmap spriteSheetDippyDown = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_dippy_down);
        Bitmap[] dippyDown = new Bitmap[4];
        dippyDown[0] = Bitmap.createBitmap(spriteSheetDippyDown, 48, 337, 200, 340);
        dippyDown[1] = Bitmap.createBitmap(spriteSheetDippyDown, 294, 337, 200, 340);
        dippyDown[2] = Bitmap.createBitmap(spriteSheetDippyDown, 536, 337, 200, 340);
        dippyDown[3] = Bitmap.createBitmap(spriteSheetDippyDown, 764, 337, 205, 340);

//        Bitmap spriteSheetMuhangDown = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.muhang_down);
//        Bitmap[] poohDown = new Bitmap[3];
//        poohDown[0] = Bitmap.createBitmap(spriteSheetMuhangDown, 40, 316, 268, 417);
//        poohDown[1] = Bitmap.createBitmap(spriteSheetMuhangDown, 386, 297, 256, 436);
//        poohDown[2] = Bitmap.createBitmap(spriteSheetMuhangDown, 716, 299, 264, 434);
//
//        Bitmap[] poohDown = new Bitmap[11];
//        poohDown[0] = Bitmap.createBitmap(spriteSheet, 17, 1114, 26, 36);
//        poohDown[1] = Bitmap.createBitmap(spriteSheet, 58, 1113, 26, 37);
//        poohDown[2] = Bitmap.createBitmap(spriteSheet, 99, 1112, 26, 38);
//        poohDown[3] = Bitmap.createBitmap(spriteSheet, 140, 1114, 26, 36);
//        poohDown[4] = Bitmap.createBitmap(spriteSheet, 181, 1115, 26, 35);
//        poohDown[5] = Bitmap.createBitmap(spriteSheet, 224, 1115, 24, 35);
//        poohDown[6] = Bitmap.createBitmap(spriteSheet, 264, 1113, 26, 37);
//        poohDown[7] = Bitmap.createBitmap(spriteSheet, 305, 1112, 26, 38);
//        poohDown[8] = Bitmap.createBitmap(spriteSheet, 350, 1113, 26, 37);
//        poohDown[9] = Bitmap.createBitmap(spriteSheet, 393, 1115, 26, 35);
//        poohDown[10] = Bitmap.createBitmap(spriteSheet, 434, 1116, 26, 34);

        Bitmap spriteSheetDippyRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_dippy_right);
        Bitmap[] dippyRight = new Bitmap[4];
        dippyRight[0] = Bitmap.createBitmap(spriteSheetDippyRight, 35, 397, 250, 190);
        dippyRight[1] = Bitmap.createBitmap(spriteSheetDippyRight, 290, 397, 227, 190);
        dippyRight[2] = Bitmap.createBitmap(spriteSheetDippyRight, 518, 397, 232, 190);
        dippyRight[3] = Bitmap.createBitmap(spriteSheetDippyRight, 751, 397, 235, 190);

//        Bitmap[] poohRight = new Bitmap[11];
//        poohRight[0] = Bitmap.createBitmap(spriteSheet, 20, 1217, 24, 34);
//        poohRight[1] = Bitmap.createBitmap(spriteSheet, 64, 1217, 19, 34);
//        poohRight[2] = Bitmap.createBitmap(spriteSheet, 107, 1215, 19, 36);
//        poohRight[3] = Bitmap.createBitmap(spriteSheet, 146, 1214, 22, 37);
//        poohRight[4] = Bitmap.createBitmap(spriteSheet, 184, 1215, 23, 36);
//        poohRight[5] = Bitmap.createBitmap(spriteSheet, 227, 1218, 25, 33);
//        poohRight[6] = Bitmap.createBitmap(spriteSheet, 269, 1215, 19, 36);
//        poohRight[7] = Bitmap.createBitmap(spriteSheet, 309, 1214, 19, 37);
//        poohRight[8] = Bitmap.createBitmap(spriteSheet, 354, 1213, 19, 38);
//        poohRight[9] = Bitmap.createBitmap(spriteSheet, 395, 1214, 21, 37);
//        poohRight[10] = Bitmap.createBitmap(spriteSheet, 433, 1216, 24, 35);

        Bitmap[] dippyLeft = new Bitmap[4];
        dippyLeft[0] = Animation.flipImageHorizontally(dippyRight[0]);
        dippyLeft[1] = Animation.flipImageHorizontally(dippyRight[1]);
        dippyLeft[2] = Animation.flipImageHorizontally(dippyRight[2]);
        dippyLeft[3] = Animation.flipImageHorizontally(dippyRight[3]);

//        Bitmap[] poohLeft = new Bitmap[11];
//        poohLeft[0] = Bitmap.createBitmap(spriteSheet, 20, 1268, 24, 34);
//        poohLeft[1] = Bitmap.createBitmap(spriteSheet, 64, 1268, 19, 34);
//        poohLeft[2] = Bitmap.createBitmap(spriteSheet, 107, 1266, 19, 36);
//        poohLeft[3] = Bitmap.createBitmap(spriteSheet, 146, 1265, 22, 37);
//        poohLeft[4] = Bitmap.createBitmap(spriteSheet, 184, 1266, 23, 36);
//        poohLeft[5] = Bitmap.createBitmap(spriteSheet, 227, 1269, 25, 33);
//        poohLeft[6] = Bitmap.createBitmap(spriteSheet, 269, 1266, 19, 36);
//        poohLeft[7] = Bitmap.createBitmap(spriteSheet, 309, 1265, 19, 37);
//        poohLeft[8] = Bitmap.createBitmap(spriteSheet, 354, 1264, 19, 38);
//        poohLeft[9] = Bitmap.createBitmap(spriteSheet, 395, 1265, 21, 37);
//        poohLeft[10] = Bitmap.createBitmap(spriteSheet, 433, 1267, 24, 35);

        ////////////////////////////////////////////////////////////////////

        Bitmap spriteSheetDippyUpRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_dippy_up_right);
        Bitmap[] dippyUpRight = new Bitmap[4];
        dippyUpRight[0] = Bitmap.createBitmap(spriteSheetDippyUpRight, 260, 63, 227, 257);
        dippyUpRight[1] = Bitmap.createBitmap(spriteSheetDippyUpRight, 0, 400, 260, 260);
        dippyUpRight[2] = Bitmap.createBitmap(spriteSheetDippyUpRight, 408, 460, 252, 242);
        dippyUpRight[3] = Bitmap.createBitmap(spriteSheetDippyUpRight, 716, 406, 247, 260);

//        Bitmap[] poohUpRight = new Bitmap[11];
//        poohUpRight[0] = Bitmap.createBitmap(spriteSheet, 20, 1428, 22, 36);
//        poohUpRight[1] = Bitmap.createBitmap(spriteSheet, 66, 1427, 20, 37);
//        poohUpRight[2] = Bitmap.createBitmap(spriteSheet, 108, 1426, 20, 38);
//        poohUpRight[3] = Bitmap.createBitmap(spriteSheet, 148, 1427, 19, 37);
//        poohUpRight[4] = Bitmap.createBitmap(spriteSheet, 185, 1426, 18, 38);
//        poohUpRight[5] = Bitmap.createBitmap(spriteSheet, 226, 1426, 20, 38);
//        poohUpRight[6] = Bitmap.createBitmap(spriteSheet, 265, 1425, 24, 39);
//        poohUpRight[7] = Bitmap.createBitmap(spriteSheet, 300, 1426, 26, 38);
//        poohUpRight[8] = Bitmap.createBitmap(spriteSheet, 347, 1426, 25, 38);
//        poohUpRight[9] = Bitmap.createBitmap(spriteSheet, 393, 1427, 24, 37);
//        poohUpRight[10] = Bitmap.createBitmap(spriteSheet, 430, 1429, 26, 35);

        Bitmap[] dippyUpLeft = new Bitmap[4];
        dippyUpLeft[0] = Animation.flipImageHorizontally(dippyUpRight[0]);
        dippyUpLeft[1] = Animation.flipImageHorizontally(dippyUpRight[1]);
        dippyUpLeft[2] = Animation.flipImageHorizontally(dippyUpRight[2]);
        dippyUpLeft[3] = Animation.flipImageHorizontally(dippyUpRight[3]);

//        Bitmap[] poohUpLeft = new Bitmap[11];
//        poohUpLeft[0] = Bitmap.createBitmap(spriteSheet, 20, 1493, 22, 36);
//        poohUpLeft[1] = Bitmap.createBitmap(spriteSheet, 66, 1492, 20, 37);
//        poohUpLeft[2] = Bitmap.createBitmap(spriteSheet, 108, 1491, 20, 38);
//        poohUpLeft[3] = Bitmap.createBitmap(spriteSheet, 148, 1492, 19, 37);
//        poohUpLeft[4] = Bitmap.createBitmap(spriteSheet, 185, 1491, 18, 38);
//        poohUpLeft[5] = Bitmap.createBitmap(spriteSheet, 226, 1491, 20, 38);
//        poohUpLeft[6] = Bitmap.createBitmap(spriteSheet, 265, 1490, 24, 39);
//        poohUpLeft[7] = Bitmap.createBitmap(spriteSheet, 300, 1491, 26, 38);
//        poohUpLeft[8] = Bitmap.createBitmap(spriteSheet, 347, 1491, 25, 38);
//        poohUpLeft[9] = Bitmap.createBitmap(spriteSheet, 393, 1492, 24, 37);
//        poohUpLeft[10] = Bitmap.createBitmap(spriteSheet, 430, 1494, 26, 35);

        Bitmap spriteSheetDippyDownRight = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_dippy_down_right);
        Bitmap[] dippyDownRight = new Bitmap[4];
        dippyDownRight[0] = Bitmap.createBitmap(spriteSheetDippyDownRight, 176, 12, 324, 300);
        dippyDownRight[1] = Bitmap.createBitmap(spriteSheetDippyDownRight, 0, 365, 286, 315);
        dippyDownRight[2] = Bitmap.createBitmap(spriteSheetDippyDownRight, 390, 630, 285, 304);
        dippyDownRight[3] = Bitmap.createBitmap(spriteSheetDippyDownRight, 687, 324, 294, 308);

//        Bitmap[] poohDownRight = new Bitmap[11];
//        poohDownRight[0] = Bitmap.createBitmap(spriteSheet, 15, 1325, 24, 33);
//        poohDownRight[1] = Bitmap.createBitmap(spriteSheet, 60, 1323, 23, 35);
//        poohDownRight[2] = Bitmap.createBitmap(spriteSheet, 103, 1322, 21, 36);
//        poohDownRight[3] = Bitmap.createBitmap(spriteSheet, 148, 1321, 20, 37);
//        poohDownRight[4] = Bitmap.createBitmap(spriteSheet, 190, 1322, 19, 36);
//        poohDownRight[5] = Bitmap.createBitmap(spriteSheet, 232, 1324, 18, 34);
//        poohDownRight[6] = Bitmap.createBitmap(spriteSheet, 274, 1326, 19, 32);
//        poohDownRight[7] = Bitmap.createBitmap(spriteSheet, 308, 1324, 23, 34);
//        poohDownRight[8] = Bitmap.createBitmap(spriteSheet, 355, 1323, 26, 35);
//        poohDownRight[9] = Bitmap.createBitmap(spriteSheet, 397, 1323, 27, 35);
//        poohDownRight[10] = Bitmap.createBitmap(spriteSheet, 437, 1325, 25, 33);

        Bitmap[] dippyDownLeft = new Bitmap[4];
        dippyDownLeft[0] = Animation.flipImageHorizontally(dippyDownRight[0]);
        dippyDownLeft[1] = Animation.flipImageHorizontally(dippyDownRight[1]);
        dippyDownLeft[2] = Animation.flipImageHorizontally(dippyDownRight[2]);
        dippyDownLeft[3] = Animation.flipImageHorizontally(dippyDownRight[3]);

//        Bitmap[] poohDownLeft = new Bitmap[11];
//        poohDownLeft[0] = Bitmap.createBitmap(spriteSheet, 20, 1375, 24, 33);
//        poohDownLeft[1] = Bitmap.createBitmap(spriteSheet, 65, 1373, 23, 35);
//        poohDownLeft[2] = Bitmap.createBitmap(spriteSheet, 108, 1372, 21, 36);
//        poohDownLeft[3] = Bitmap.createBitmap(spriteSheet, 147, 1373, 20, 37);
//        poohDownLeft[4] = Bitmap.createBitmap(spriteSheet, 185, 1372, 19, 36);
//        poohDownLeft[5] = Bitmap.createBitmap(spriteSheet, 228, 1374, 18, 34);
//        poohDownLeft[6] = Bitmap.createBitmap(spriteSheet, 269, 1376, 19, 32);
//        poohDownLeft[7] = Bitmap.createBitmap(spriteSheet, 303, 1374, 23, 34);
//        poohDownLeft[8] = Bitmap.createBitmap(spriteSheet, 349, 1373, 26, 35);
//        poohDownLeft[9] = Bitmap.createBitmap(spriteSheet, 391, 1373, 27, 35);
//        poohDownLeft[10] = Bitmap.createBitmap(spriteSheet, 431, 1375, 25, 33);

        poohDefaultUp = dippyUp[0];
        poohDefaultDown = dippyDown[0];
        poohDefaultLeft = dippyLeft[0];
        poohDefaultRight = dippyRight[0];
        poohDefaultDownLeft = dippyDownLeft[0];
        poohDefaultDownRight = dippyDownRight[0];
//        poohDefaultUp = Bitmap.createBitmap(spriteSheet, 314, 1063, 20, 36);
//        poohDefaultDown = Bitmap.createBitmap(spriteSheet, 178, 1061, 20, 38);
//        poohDefaultLeft = Bitmap.createBitmap(spriteSheet, 111, 1062, 17, 37);
//        poohDefaultRight = Bitmap.createBitmap(spriteSheet, 247, 1062, 17, 37);
        poohDefaultUpLeft = dippyUpLeft[0];
        poohDefaultUpRight = dippyUpRight[0];
//        poohDefaultDownLeft = Bitmap.createBitmap(spriteSheet, 145, 1060, 17, 39);
//        poohDefaultDownRight = Bitmap.createBitmap(spriteSheet, 213, 1060, 17, 39);

        Bitmap[] dippyCenter = {poohDefaultDown};

        Animation poohUpAnimation = new Animation(dippyUp, speed);
        Animation poohDownAnimation = new Animation(dippyDown, speed);
        Animation poohLeftAnimation = new Animation(dippyLeft, speed);
        Animation poohRightAnimation = new Animation(dippyRight, speed);
        Animation poohCenterAnimation = new Animation(dippyCenter, speed);
        Animation poohUpLeftAnimation = new Animation(dippyUpLeft, speed);
        Animation poohUpRightAnimation = new Animation(dippyUpRight, speed);
        Animation poohDownLeftAnimation = new Animation(dippyDownLeft, speed);
        Animation poohDownRightAnimation = new Animation(dippyDownRight, speed);

        animations = new HashMap<Creature.Direction, Animation>();
        animations.put(Creature.Direction.UP, poohUpAnimation);
        animations.put(Creature.Direction.DOWN, poohDownAnimation);
        animations.put(Creature.Direction.LEFT, poohLeftAnimation);
        animations.put(Creature.Direction.RIGHT, poohRightAnimation);
        animations.put(Creature.Direction.CENTER, poohCenterAnimation);
        animations.put(Creature.Direction.UP_LEFT, poohUpLeftAnimation);
        animations.put(Creature.Direction.UP_RIGHT, poohUpRightAnimation);
        animations.put(Creature.Direction.DOWN_LEFT, poohDownLeftAnimation);
        animations.put(Creature.Direction.DOWN_RIGHT, poohDownRightAnimation);
    }
}