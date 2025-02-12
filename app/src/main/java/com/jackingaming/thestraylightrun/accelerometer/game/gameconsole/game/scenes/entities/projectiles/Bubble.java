package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.projectiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Bubble extends Entity {

    private int counterFrameTarget;
    private int counterFrame;

    private Bitmap[] framesBubble;
    private int indexFramesBubble;

    public Bubble(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        // Bubble [projectile]: all frames.
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);
        counterFrame = 0;
        counterFrameTarget = 30;
        indexFramesBubble = 0;
        framesBubble = new Bitmap[6];
        for (int i = 0; i < framesBubble.length; i++) {
            int y = 1050;
            int x = 5 + (i * (16 + 2));

            framesBubble[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }
        image = framesBubble[0];
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;

        if (counterFrame >= counterFrameTarget) {
            indexFramesBubble++;
            if (indexFramesBubble >= framesBubble.length) {
                indexFramesBubble = 0;
            }

            image = framesBubble[indexFramesBubble];

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
