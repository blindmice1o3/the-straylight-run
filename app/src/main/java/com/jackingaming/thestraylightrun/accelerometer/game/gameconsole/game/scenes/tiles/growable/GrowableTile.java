package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.HashMap;
import java.util.Map;

public class GrowableTile extends Tile {
    public static final String TAG = GrowableTile.class.getSimpleName();

    public enum State {UNTILLED, TILLED, SEEDED, OCCUPIED;}

    private boolean watered;
    private State state;
    private String idSeed;
    private Entity entity;
    private Map<State, Bitmap> imageUnwateredViaState;
    private Map<State, Bitmap> imageWateredViaState;

    public GrowableTile(String id, Resources resources) {
        super(id);
        watered = false;
        state = State.UNTILLED;
        idSeed = null;
        entity = null;
        initImageMaps(resources);
    }

    private void initImageMaps(Resources resources) {
        Bitmap spriteSheetCropsAndItems = BitmapFactory.decodeResource(resources, R.drawable.gbc_hm_crops_and_items);
        Bitmap unwateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 7, 19, 16, 16);
        Bitmap unwateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 7, 106, 16, 16);
        Bitmap wateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 24, 19, 16, 16);
        Bitmap wateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 24, 106, 16, 16);

        imageUnwateredViaState = new HashMap<>();
        imageUnwateredViaState.put(State.UNTILLED, image);
        imageUnwateredViaState.put(State.TILLED, unwateredTilled);
        imageUnwateredViaState.put(State.SEEDED, unwateredSeeded);
        imageUnwateredViaState.put(State.OCCUPIED, unwateredTilled);

        imageWateredViaState = new HashMap<>();
        imageWateredViaState.put(State.UNTILLED, image);
        imageWateredViaState.put(State.TILLED, wateredTilled);
        imageWateredViaState.put(State.SEEDED, wateredSeeded);
        imageWateredViaState.put(State.OCCUPIED, wateredTilled);
    }

    private void updateImage() {
        if (watered) {
            image = imageWateredViaState.get(state);
        } else {
            image = imageUnwateredViaState.get(state);
        }
    }

    public void changeToWatered() {
        watered = true;
        updateImage();
    }

    public void changeToUnwatered() {
        watered = false;
        updateImage();
    }

    public void changeToUntilled() {
        state = State.UNTILLED;
        updateImage();
    }

    public void changeToTilled() {
        state = State.TILLED;
        updateImage();
    }

    public void changeToSeeded(Item itemSeed) {
        idSeed = itemSeed.getName();

        state = State.SEEDED;
        updateImage();
    }

    public void changeToOccupied() {
        state = State.OCCUPIED;
        updateImage();
    }

    public State getState() {
        return state;
    }
}
