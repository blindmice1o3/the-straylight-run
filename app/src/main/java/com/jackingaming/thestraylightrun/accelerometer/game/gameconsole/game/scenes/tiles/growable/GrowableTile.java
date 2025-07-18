package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Assets;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GrowableTile extends Tile {
    public static final String TAG = GrowableTile.class.getSimpleName();

    public interface OutdoorWaterChangeListener {
        void changeToWateredSeeded();
    }

    private OutdoorWaterChangeListener outdoorWaterChangeListener;

    public void setOutdoorWaterChangeListener(OutdoorWaterChangeListener outdoorWaterChangeListener) {
        this.outdoorWaterChangeListener = outdoorWaterChangeListener;
    }

    public interface StateChangeListener {
        void changeToTilled();
    }

    private StateChangeListener stateChangeListener;

    public void setStateChangeListener(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public interface EntityListener extends Serializable {
        void addEntityToScene(Entity entityToAdd);
    }

    private EntityListener entityListener;

    public enum State {UNTILLED, TILLED, SEEDED, OCCUPIED;}

    protected boolean watered;
    protected State state;
    protected String idSeed;
    protected Entity entity;
    transient protected Map<State, Bitmap> imageUnwateredViaState;
    transient protected Map<State, Bitmap> imageWateredViaState;

    public GrowableTile(String id, EntityListener entityListener) {
        super(id);
        this.entityListener = entityListener;
        watered = false;
        state = State.UNTILLED;
        idSeed = null;
        entity = null;
    }

    @Override
    public void init(Game game, int xIndex, int yIndex, Bitmap image) {
        super.init(game, xIndex, yIndex, image);

        initImageMaps(game.getContext().getResources());
        updateImage();
    }

    public void germinateSeed() {
        Entity entityToAdd = null;
        if (idSeed.equals(MysterySeed.TAG)) {
            entityToAdd = new Plant(xIndex * Tile.WIDTH, yIndex * Tile.HEIGHT);
        } else {
            Log.e(TAG, "startNewDay() else-clause: idSeed does NOT match defined options");
        }

        entityToAdd.init(game);
        entityListener.addEntityToScene(entityToAdd);

        entity = entityToAdd;
        changeToOccupied();
    }

    public void startNewDay() {
        if (watered) {
            if (state == State.SEEDED) {
                germinateSeed();
            } else if (state == State.OCCUPIED) {
                if (entity instanceof Plant) {
                    ((Plant) entity).incrementAgeInDays();
                }
            }

            changeToUnwatered();
        }
    }

    public void updateImageForStateUntilled(Bitmap image) {
        imageUnwateredViaState.put(State.UNTILLED, image);
        imageWateredViaState.put(State.UNTILLED, image);

        if (state == State.UNTILLED) {
            updateImage();
        }
    }

    protected void initImageMaps(Resources resources) {
        imageUnwateredViaState = new HashMap<>();
        imageUnwateredViaState.put(State.UNTILLED, image);
        imageUnwateredViaState.put(State.TILLED, Assets.unwateredTilled);
        imageUnwateredViaState.put(State.SEEDED, Assets.unwateredSeeded);
        imageUnwateredViaState.put(State.OCCUPIED, Assets.unwateredTilled);

        imageWateredViaState = new HashMap<>();
        imageWateredViaState.put(State.UNTILLED, image);
        imageWateredViaState.put(State.TILLED, Assets.wateredTilled);
        imageWateredViaState.put(State.SEEDED, Assets.wateredSeeded);
        imageWateredViaState.put(State.OCCUPIED, Assets.wateredTilled);
    }

    protected void updateImage() {
        if (watered) {
            image = imageWateredViaState.get(state);
        } else {
            image = imageUnwateredViaState.get(state);
        }
    }

    public void changeToWatered() {
        watered = true;
        updateImage();

        if (outdoorWaterChangeListener != null && state == State.SEEDED) {
            outdoorWaterChangeListener.changeToWateredSeeded();
        } else {
            Log.e(TAG, "changeToWatered() waterChangeListener == null");
        }
    }

    public void changeToUnwatered() {
        watered = false;
        updateImage();
    }

    public void changeToUntilled() {
        state = State.UNTILLED;
        watered = false;
        idSeed = null;
        entity = null;

        updateImage();
    }

    public void changeToTilled() {
        state = State.TILLED;
        updateImage();

        if (stateChangeListener != null) {
            stateChangeListener.changeToTilled();
        } else {
            Log.e(TAG, "changeToTilled() stateChangeListener == null");
        }
    }

    public void changeToSeeded(String idSeed) {
        this.idSeed = idSeed;

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

    public boolean isWatered() {
        return watered;
    }

    public Entity getEntity() {
        return entity;
    }
}
