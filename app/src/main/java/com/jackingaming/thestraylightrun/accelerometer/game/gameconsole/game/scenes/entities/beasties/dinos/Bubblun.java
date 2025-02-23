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
    private int indexBubblunIdleLeft;
    private int counterFrame;
    private int counterFrameTarget;

    private Bitmap bubblunAttackLeft[];
    private Bitmap bubblunAttackRight[];
    private int indexBubblunAttackLeft;

    private boolean movingLeft;

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

        movingLeft = false;
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
            // TODO: (1) change below back to the commented-out-section (for idle state).
            //  (2) move attack state stuff into its own if-clause.
            indexBubblunAttackLeft++;
            if (indexBubblunAttackLeft >= bubblunAttackLeft.length) {
                indexBubblunAttackLeft = 0;
            }

            Bitmap imageForFrame = (movingLeft) ?
                    bubblunAttackLeft[indexBubblunAttackLeft] :
                    bubblunAttackRight[indexBubblunAttackLeft];
            image = imageForFrame;

//            indexBubblunIdleLeft++;
//            if (indexBubblunIdleLeft >= bubblunIdleLeft.length) {
//                indexBubblunIdleLeft = 0;
//            }
//
//            Bitmap imageForFrame = (movingLeft) ?
//                    bubblunIdleLeft[indexBubblunIdleLeft] :
//                    bubblunIdleRight[indexBubblunIdleLeft];
//            image = imageForFrame;

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
