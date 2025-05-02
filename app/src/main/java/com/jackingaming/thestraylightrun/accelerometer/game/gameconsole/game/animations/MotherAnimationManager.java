package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations;

import android.graphics.Bitmap;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
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

    public void init(Game game, int yIndexForSprites) {
        Bitmap[][] sprites = WorldScene.SpriteInitializer.initSprites(game.getContext().getResources(), Tile.WIDTH, Tile.HEIGHT);

        Bitmap[] motherUp = new Bitmap[3];
        motherUp[0] = sprites[3][yIndexForSprites];
        motherUp[1] = sprites[4][yIndexForSprites];
        motherUp[2] = sprites[5][yIndexForSprites];

        Bitmap[] motherDown = new Bitmap[3];
        motherDown[0] = sprites[0][yIndexForSprites];
        motherDown[1] = sprites[1][yIndexForSprites];
        motherDown[2] = sprites[2][yIndexForSprites];

        Bitmap[] motherLeft = new Bitmap[2];
        motherLeft[0] = sprites[6][yIndexForSprites];
        motherLeft[1] = sprites[7][yIndexForSprites];

        Bitmap[] motherRight = new Bitmap[2];
        motherRight[0] = sprites[8][yIndexForSprites];
        motherRight[1] = sprites[9][yIndexForSprites];

        Bitmap[] motherUpLeft = new Bitmap[2];
        motherUpLeft[0] = sprites[6][yIndexForSprites];
        motherUpLeft[1] = sprites[7][yIndexForSprites];

        Bitmap[] motherUpRight = new Bitmap[2];
        motherUpRight[0] = sprites[8][yIndexForSprites];
        motherUpRight[1] = sprites[9][yIndexForSprites];

        Bitmap[] motherDownLeft = new Bitmap[2];
        motherDownLeft[0] = sprites[6][yIndexForSprites];
        motherDownLeft[1] = sprites[7][yIndexForSprites];

        Bitmap[] motherDownRight = new Bitmap[2];
        motherDownRight[0] = sprites[8][yIndexForSprites];
        motherDownRight[1] = sprites[9][yIndexForSprites];

        motherDefaultUp = motherUp[1];
        motherDefaultDown = motherDown[1];
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
    }
}
