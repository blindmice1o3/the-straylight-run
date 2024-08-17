package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.Random;

public class Plant extends Entity {
    public static final int BRACKET01_MIN_INCLUSIVE = 0;
    public static final int BRACKET01_MAX_EXCLUSIVE = 1;
    public static final int BRACKET02_MIN_INCLUSIVE = 1;
    public static final int BRACKET02_MAX_EXCLUSIVE = 3;
    public static final int BRACKET03_MIN_INCLUSIVE = 3;
    public static final int BRACKET03_MAX_EXCLUSIVE = 5;
    public static final int BRACKET04_MIN_INCLUSIVE = 5;
    public static final int BRACKET04_MAX_EXCLUSIVE = 7;

    private int ageInDays;
    private boolean harvestable;
    private Bitmap imageBracket01, imageBracket02, imageBracket03, imageBracket04;
    private Bitmap imageHarvestableGreen, imageHarvestablePurple;
    private Bitmap imageHarvestable;

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
        int numberRandom = random.nextInt();
        imageHarvestable = (numberRandom % 2 == 0) ?
                imageHarvestableGreen : imageHarvestablePurple;

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
}
