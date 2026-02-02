package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Assets;

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

    @Override
    protected void initImageMaps(Resources resources) {
        Bitmap unwateredTilled = Assets.unwateredTilledIndoor;
        Bitmap unwateredSeeded = Assets.unwateredSeededIndoor;
        Bitmap wateredTilled = Assets.wateredTilledIndoor;
        Bitmap wateredSeeded = Assets.wateredSeededIndoor;

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
