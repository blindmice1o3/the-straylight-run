package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.attacks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles.Bubble;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class AttackState
        implements State {

    private Game game;
    private Bubblun bubblun;

    private Bitmap bubblunAttackLeft[];
    private Bitmap bubblunAttackRight[];
    private int indexBubblunAttackLeft;
    private int counterFrame;
    private int counterFrameTarget;

    @Override
    public void enter() {
        Bitmap imageForFrame = (bubblun.isMovingLeft()) ?
                bubblunAttackLeft[indexBubblunAttackLeft] :
                bubblunAttackRight[indexBubblunAttackLeft];
        bubblun.setImage(imageForFrame);

        Bubble bubble = addBubbleEntityToScene();
        bubble.bounceToRight();
    }

    public Bubble addBubbleEntityToScene() {
        Bubble bubble = new Bubble(
                (int) (bubblun.getX() + bubblun.getWidth()), (int) bubblun.getY()
        );
        bubble.init(game);

        game.getSceneManager().getCurrentScene().getEntityManager().addEntity(
                bubble
        );

        return bubble;
    }

    @Override
    public void exit() {

    }

    @Override
    public void init(Game game, Entity e) {
        this.game = game;
        if (e instanceof Bubblun) {
            bubblun = (Bubblun) e;
        }

        counterFrame = 0;
        counterFrameTarget = 3;

        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexBubblunAttackLeft = 0;
        bubblunAttackLeft = new Bitmap[4];
//        bubblunAttackLeft = new Bitmap[8];
        bubblunAttackRight = new Bitmap[4];
        for (int i = 0; i < bubblunAttackLeft.length; i++) {
            int y = 16;
            int x = -1;
            if (i == 0) {
                x = 150;
            } else if (i == 1) {
                x = 171;
            } else if (i == 2) {
                x = 192;
            } else if (i == 3) {
                x = 213;
            }
//            else if (i == 4) {
//                x = 233;
//            } else if (i == 5) {
//                x = 254;
//            } else if (i == 6) {
//                x = 275;
//            } else if (i == 7) {
//                x = 296;
//            }

            bubblunAttackLeft[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            bubblunAttackRight[i] = Animation.flipImageHorizontally(bubblunAttackLeft[i]);
        }

        Bitmap imageForFrame = (bubblun.isMovingLeft()) ?
                bubblunAttackLeft[indexBubblunAttackLeft] :
                bubblunAttackRight[indexBubblunAttackLeft];
        bubblun.setImage(imageForFrame);
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexBubblunAttackLeft++;
            if (indexBubblunAttackLeft >= bubblunAttackLeft.length) {
                indexBubblunAttackLeft = 0;

                bubblun.popAttackState();
            }

            Bitmap imageForFrame = (bubblun.isMovingLeft()) ?
                    bubblunAttackLeft[indexBubblunAttackLeft] :
                    bubblunAttackRight[indexBubblunAttackLeft];
            bubblun.setImage(imageForFrame);

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item i) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item i) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }
}
