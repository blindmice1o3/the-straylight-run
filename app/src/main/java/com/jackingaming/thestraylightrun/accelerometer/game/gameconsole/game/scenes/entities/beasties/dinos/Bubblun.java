package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.projectiles.Bubble;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Bubblun extends Entity {

    private Bitmap bubblunIdleLeft[];
    private Bitmap bubblunIdleRight[];
    private int indexBubblunIdle;

    private int counterFrame;
    private int counterFrameTarget;

    public Bubblun(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        counterFrame = 0;
        counterFrameTarget = 30;

        // Bubblun [Entity]: first frame
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);

        indexBubblunIdle = 0;
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

        image = bubblunIdleRight[0];
    }

    public Bubble addBubbleEntityToScene() {
        Bubble bubble = new Bubble(
                (int) (x + width), (int) y
        );
        bubble.init(game);

        game.getSceneManager().getCurrentScene().getEntityManager().addEntity(
                bubble
        );

        return bubble;
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;
        if (counterFrame >= counterFrameTarget) {
            indexBubblunIdle++;
            if (indexBubblunIdle >= bubblunIdleLeft.length) {
                indexBubblunIdle = 0;
            }

            Bitmap imageForFrame = bubblunIdleRight[indexBubblunIdle];
            image = imageForFrame;

            counterFrame = 0;
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }
}
