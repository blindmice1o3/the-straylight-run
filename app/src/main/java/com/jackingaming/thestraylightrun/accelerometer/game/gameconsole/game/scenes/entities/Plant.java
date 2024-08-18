package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.Random;

public class Plant extends Entity
        implements Sellable {
    public static final String TAG = Plant.class.getSimpleName();

    public static final int BRACKET01_MIN_INCLUSIVE = 0;
    public static final int BRACKET01_MAX_EXCLUSIVE = 1;
    public static final int BRACKET02_MIN_INCLUSIVE = 1;
    public static final int BRACKET02_MAX_EXCLUSIVE = 3;
    public static final int BRACKET03_MIN_INCLUSIVE = 3;
    public static final int BRACKET03_MAX_EXCLUSIVE = 5;
    public static final int BRACKET04_MIN_INCLUSIVE = 5;
    public static final int BRACKET04_MAX_EXCLUSIVE = 7;
    private static final float PRICE_GREEN = 60f;
    private static final float PRICE_PURPLE = 85f;

    public enum Color {GREEN, PURPLE;}

    private int ageInDays;
    private boolean harvestable;
    private Bitmap imageBracket01, imageBracket02, imageBracket03, imageBracket04;
    private Bitmap imageHarvestableGreen, imageHarvestablePurple;
    private Bitmap imageHarvestable;
    private Color color;
    private float price;

    public Plant(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);
    }

    @Override
    public void init(Game game) {
        super.init(game);
        ageInDays = 0;
        harvestable = false;
        imageBracket01 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green249);
        imageBracket02 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green256);
        imageBracket03 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green266);
        imageBracket04 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green280);

        imageHarvestableGreen = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green2174);
        imageHarvestablePurple = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.purple2174);

        Random random = new Random();
        int numberRandom = random.nextInt(100);
        color = (numberRandom < 50) ?
                Color.GREEN : Color.PURPLE;

        switch (color) {
            case GREEN:
                imageHarvestable = imageHarvestableGreen;
                price = PRICE_GREEN;
                break;
            case PURPLE:
                imageHarvestable = imageHarvestablePurple;
                price = PRICE_PURPLE;
                break;
            default:
                Log.e(TAG, "color not defined.");
                break;
        }

        updateBasedOnAgeInDays();
    }

    public void incrementAgeInDays() {
        ageInDays++;
        updateBasedOnAgeInDays();
    }

    private void updateBasedOnAgeInDays() {
        if (ageInDays >= BRACKET01_MIN_INCLUSIVE && ageInDays < BRACKET01_MAX_EXCLUSIVE) {
            image = imageBracket01;
        } else if (ageInDays >= BRACKET02_MIN_INCLUSIVE && ageInDays < BRACKET02_MAX_EXCLUSIVE) {
            image = imageBracket02;
        } else if (ageInDays >= BRACKET03_MIN_INCLUSIVE && ageInDays < BRACKET03_MAX_EXCLUSIVE) {
            image = imageBracket03;
        } else if (ageInDays >= BRACKET04_MIN_INCLUSIVE && ageInDays < BRACKET04_MAX_EXCLUSIVE) {
            image = imageBracket04;
        } else {
            harvestable = true;
            image = imageHarvestable;
        }
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

    @Override
    public boolean isCarryable() {
        return true;
    }

    public boolean isHarvestable() {
        return harvestable;
    }

    @Override
    public float getPrice() {
        return price;
    }
}
