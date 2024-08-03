package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

public class Plant extends Entity {
    private int ageInDays;
    private boolean harvestable;
    private Bitmap imageBracket01, imageBracket02, imageBracket03, imageBracket04, imageHarvestable;

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
        imageHarvestable = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green2174);
        updateBasedOnAgeInDays();
    }

    public void incrementAgeInDays() {
        ageInDays++;
        updateBasedOnAgeInDays();
    }

    private void updateBasedOnAgeInDays() {
        if (ageInDays >= 0 && ageInDays < 1) {
            image = imageBracket01;
        } else if (ageInDays >= 1 && ageInDays < 3) {
            image = imageBracket02;
        } else if (ageInDays >= 3 && ageInDays < 5) {
            image = imageBracket03;
        } else if (ageInDays >= 5 && ageInDays < 7) {
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
}
