package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Monsta extends Entity {
    public static final String TAG = Monsta.class.getSimpleName();

    private Bitmap monstaLeft, monstaLeftBubbled;

    public Monsta(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        // Monsta [Entity]: first frame
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);
        monstaLeft = Bitmap.createBitmap(spriteSheet, 6, 333, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        monstaLeftBubbled = Bitmap.createBitmap(spriteSheet, 267, 333, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);

        image = monstaLeft;
    }

    public void becomeBubbled() {
        image = monstaLeftBubbled;
    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        Log.e(TAG, "respondToEntityCollision(Entity)");
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
