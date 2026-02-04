package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Damageable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Meat;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class Kelp extends Entity
        implements Damageable {
    public static final String TAG = Kelp.class.getSimpleName();

    public static final int HEALTH_MAX_DEFAULT = 2;
    private static final int WIDTH_IMAGE = 12;
    private static final int HEIGHT_IMAGE = 32;
    private static final int ANIMATION_SPEED_DEFAULT = 300;
    transient private static Bitmap[] kelpFrames;

    transient private Animation animation;

    private int healthMax;
    private int health;

    public Kelp(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
        width = Tile.WIDTH / 2;
        height = Tile.HEIGHT;

        healthMax = HEALTH_MAX_DEFAULT;
        health = healthMax;
    }

    @Override
    public void init(Game game) {
        super.init(game);

        if (kelpFrames == null) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(game.getContext().getResources(),
                    R.drawable.snes_evo_search_for_eden_chapter1_creatures);

            kelpFrames = new Bitmap[6];
            kelpFrames[0] = Bitmap.createBitmap(spriteSheet, 557, 718, WIDTH_IMAGE, HEIGHT_IMAGE);
            kelpFrames[1] = Bitmap.createBitmap(spriteSheet, 569, 718, WIDTH_IMAGE, HEIGHT_IMAGE);
            kelpFrames[2] = Bitmap.createBitmap(spriteSheet, 582, 718, WIDTH_IMAGE, HEIGHT_IMAGE);
            kelpFrames[3] = Bitmap.createBitmap(spriteSheet, 594, 718, WIDTH_IMAGE, HEIGHT_IMAGE);
            kelpFrames[4] = Bitmap.createBitmap(spriteSheet, 582, 718, WIDTH_IMAGE, HEIGHT_IMAGE);
            kelpFrames[5] = Bitmap.createBitmap(spriteSheet, 569, 718, WIDTH_IMAGE, HEIGHT_IMAGE);
        }
        animation = new Animation(kelpFrames, ANIMATION_SPEED_DEFAULT);
    }

    @Override
    public void update(long elapsed) {
        animation.update(elapsed);
        image = animation.getCurrentFrame();
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        // Intentionally blank.
        return true;
    }

    @Override
    public boolean respondToItemCollisionViaClick(Item item) {
        // Intentionally blank.
        return false;
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        // Intentionally blank.
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        // Intentionally blank.
    }

    @Override
    public void takeDamage(int incomingDamage) {
        health -= incomingDamage;

        if (health <= 0) {
            active = false;
            die();
        }
    }

    @Override
    public void die() {
        Log.d(TAG, getClass().getSimpleName() + ".die()");
        // Drop items, reward exp points, etc.
        Item meat = new Meat();
        int widthOfMeat = Tile.WIDTH / 2;
        int heightOfMeat = Tile.HEIGHT / 2;
        meat.setWidth(widthOfMeat);
        meat.setHeight(heightOfMeat);
        int xCenterOfKelpAccountingForWidthOfMeat = (int) (x + (width / 2) - (widthOfMeat / 2));
        int yCenterOfKelpAccountingForHeightOfMeat = (int) (y + (height / 2) - (heightOfMeat / 2));
        meat.setPosition(xCenterOfKelpAccountingForWidthOfMeat, yCenterOfKelpAccountingForHeightOfMeat);
        meat.init(game);
        game.getSceneManager().getCurrentScene().getItemManager().addItem(meat);
    }
}