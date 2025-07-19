package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;

import java.util.HashMap;

public class GrowableIndoorTile extends GrowableTile {
    public static final String TAG = GrowableIndoorTile.class.getSimpleName();

    public interface IndoorWaterChangeListener {
        void changeToWateredSeeded();
    }

    private IndoorWaterChangeListener indoorWaterChangeListener;

    public void setIndoorWaterChangeListener(IndoorWaterChangeListener indoorWaterChangeListener) {
        this.indoorWaterChangeListener = indoorWaterChangeListener;
    }

    public GrowableIndoorTile(String id, EntityListener entityListener) {
        super(id, entityListener);
    }

    public static Bitmap cropGrowableTableTile(Resources resources, GrowableTile.State state,
                                               boolean isWatered) {
        Log.e(TAG, "cropGrowableTableTile(Resources, GrowableTile.State, boolean)... (state: " + state + "), (isWatered: " + isWatered + ").");

        Bitmap spriteSheetPlantsHothouseHM2 = null;
        Bitmap spriteTableTile = null;

        //SELECT SPRITE_SHEET
        if (isWatered) {
            spriteSheetPlantsHothouseHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_hothouse_plants1);
        } else {
            spriteSheetPlantsHothouseHM2 = BitmapFactory.decodeResource(resources, R.drawable.hm2_hothouse_plants2);
        }

        Bitmap spriteSheetItemsAndTiles = BitmapFactory.decodeResource(resources, R.drawable.items_and_tiles);

        //CROP SPRITE
        switch (state) {
            case UNTILLED:
                spriteTableTile = null;
                break;
            case TILLED:
            case OCCUPIED:
                if (isWatered) {
                    spriteTableTile = Bitmap.createBitmap(spriteSheetItemsAndTiles, 1354, 375, 242, 241);
                } else {
                    spriteTableTile = Bitmap.createBitmap(spriteSheetItemsAndTiles, 1352, 680, 242, 239);
                }
//                if (isWatered) {
//                    spriteTableTile = Bitmap.createBitmap(spriteSheetPlantsHothouseHM2, 120, 120, 16, 16);
//                } else {
//                    spriteTableTile = Bitmap.createBitmap(spriteSheetPlantsHothouseHM2, 24, 56, 16, 16);
//                }
                break;
            case SEEDED:
                if (isWatered) {
                    spriteTableTile = Bitmap.createBitmap(spriteSheetItemsAndTiles, 1047, 376, 242, 241);
                } else {
                    spriteTableTile = Bitmap.createBitmap(spriteSheetItemsAndTiles, 1045, 681, 242, 238);
                }
//                if (isWatered) {
//                    spriteTableTile = Bitmap.createBitmap(spriteSheetPlantsHothouseHM2, 8, 120, 16, 16);
//                } else {
//                    spriteTableTile = Bitmap.createBitmap(spriteSheetPlantsHothouseHM2, 56, 120, 16, 16);
//                }
                break;
        }

        return spriteTableTile;
    }

    @Override
    protected void initImageMaps(Resources resources) {
        Bitmap unwateredTilled = cropGrowableTableTile(resources, State.TILLED, false);
        Bitmap unwateredSeeded = cropGrowableTableTile(resources, State.SEEDED, false);
        Bitmap wateredTilled = cropGrowableTableTile(resources, State.TILLED, true);
        Bitmap wateredSeeded = cropGrowableTableTile(resources, State.SEEDED, true);

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

    @Override
    public void changeToWatered() {
        watered = true;
        updateImage();

        if (indoorWaterChangeListener != null && state == State.SEEDED) {
            indoorWaterChangeListener.changeToWateredSeeded();
        } else {
            Log.e(TAG, "changeToWatered() waterChangeListener == null");
        }
    }
}
