package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Bubblun extends Entity {
    public Bubblun(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    private int counterFrameTarget;
    private int counterFrame;

    private Bitmap[] framesBubble;
    private int indexFramesBubble;

    @Override
    public void init(Game game) {
        super.init(game);

        // Bubblun [Entity]: first frame
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);
        image = Bitmap.createBitmap(spriteSheet, 6, 16, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);

        // Bubble [projectile]: all frames.
        counterFrame = 0;
        counterFrameTarget = 30;
        indexFramesBubble = 0;
        framesBubble = new Bitmap[6];
        for (int i = 0; i < framesBubble.length; i++) {
            int y = 1050;
            int x = 5 + (i * (16 + 2));

            framesBubble[i] = Bitmap.createBitmap(spriteSheet, x, y, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        super.draw(canvas, paintLightingColorFilter);

        Bitmap imageOfBubble = framesBubble[indexFramesBubble];
        Rect rectOfImage = new Rect(0, 0, imageOfBubble.getWidth(), imageOfBubble.getHeight());
        Rect rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));
        rectOnScreen.offset(-rectOnScreen.width(), 0);

        canvas.drawBitmap(imageOfBubble, rectOfImage, rectOnScreen, paintLightingColorFilter);
    }

    @Override
    public void update(long elapsed) {
        counterFrame++;

        if (counterFrame >= counterFrameTarget) {
            indexFramesBubble++;

            if (indexFramesBubble >= framesBubble.length) {
                indexFramesBubble = 0;
            }

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
