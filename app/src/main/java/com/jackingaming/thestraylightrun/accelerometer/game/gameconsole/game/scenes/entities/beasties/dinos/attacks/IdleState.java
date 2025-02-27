package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.attacks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.Bubblun;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class IdleState
        implements State {

    private Game game;
    private Bubblun bubblun;

    private Bitmap bubblunIdleLeft[];
    private Bitmap bubblunIdleRight[];
    private int indexBubblunIdleLeft;
    private int counterFrame;
    private int counterFrameTarget;

    @Override
    public void enter() {

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
        counterFrameTarget = 30;

        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexBubblunIdleLeft = 0;
        bubblunIdleLeft = new Bitmap[7];
        bubblunIdleRight = new Bitmap[7];
        for (int i = 0; i < bubblunIdleLeft.length; i++) {
            int y = 16;
            int x = 0;
            if (i == 0) {
                x = (6 + (i * Tile.WIDTH));
            } else if (i > 0 && i <= 4) {
                x = (6 + (i * Tile.WIDTH) + (i * 5));
            } else if (i > 4 && i <= 6) {
                if (i == 5) {
                    x = (6 + (i * Tile.WIDTH) + (i * 5) - 1);
                } else if (i == 6) {
                    x = (6 + (i * Tile.WIDTH) + (i * 5) - 2);
                }
            }

            bubblunIdleLeft[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
            bubblunIdleRight[i] = Animation.flipImageHorizontally(bubblunIdleLeft[i]);
        }

        Bitmap imageForFrame = (bubblun.isMovingLeft()) ?
                bubblunIdleLeft[indexBubblunIdleLeft] :
                bubblunIdleRight[indexBubblunIdleLeft];
        bubblun.setImage(imageForFrame);
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexBubblunIdleLeft++;
            if (indexBubblunIdleLeft >= bubblunIdleLeft.length) {
                indexBubblunIdleLeft = 0;
            }

            Bitmap imageForFrame = (bubblun.isMovingLeft()) ?
                    bubblunIdleLeft[indexBubblunIdleLeft] :
                    bubblunIdleRight[indexBubblunIdleLeft];
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
