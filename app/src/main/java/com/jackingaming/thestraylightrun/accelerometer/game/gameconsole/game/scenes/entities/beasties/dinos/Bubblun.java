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
    public Bubblun(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);

        // Bubblun [Entity]: first frame
        Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.arcade_bubble_bobble);
        Bitmap bubblunLeft = Bitmap.createBitmap(spriteSheet, 6, 16, 1 * Tile.WIDTH, 1 * Tile.HEIGHT);
        Bitmap bubblunRight = Animation.flipImageHorizontally(bubblunLeft);
        image = bubblunRight;
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
