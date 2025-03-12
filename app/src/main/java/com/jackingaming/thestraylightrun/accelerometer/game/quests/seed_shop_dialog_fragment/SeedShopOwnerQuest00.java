package com.jackingaming.thestraylightrun.accelerometer.game.quests.seed_shop_dialog_fragment;

import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.util.HashMap;
import java.util.Map;

public class SeedShopOwnerQuest00
        implements Quest {
    public static final String TAG = SeedShopOwnerQuest00.class.getSimpleName();
    public static final int QUANTITY_UNDEFINED = -1;

    private Quest.State state;

    private Map<String, Integer> requirementsAsString;
    private Map<String, Integer> rewardsAsString;

    public SeedShopOwnerQuest00() {
        state = State.NOT_STARTED;
        initRequirements();
        initRewards();
    }

    public void

    public boolean checkIfRequirementsAreFulfilled() {

    }

    private void initRequirements() {
        requirementsAsString = new HashMap<>();
        Plant plant = new Plant(0, 0);
        requirementsAsString.put(Plant.TAG)

    }

    private void initRewards() {
        rewardsAsString = new HashMap<>();
    }

    @Override
    public void changeToNextState() {
        switch (state) {
            case NOT_STARTED:
                state = State.STARTED;
                break;
            case STARTED:
                state = State.COMPLETED;
                break;
            case COMPLETED:
                Log.e(TAG, "changeToNextState() switch() COMPLETED... already at end of quest. Doing nothing.");
                break;
        }
    }

    @Override
    public State getCurrentState() {
        return state;
    }

    @Override
    public int getDialogueForCurrentState() {
        switch (state) {
            case NOT_STARTED:
                return R.string.seed_shop_dialogue00_0;
            case STARTED:
                return R.string.seed_shop_dialogue00_1;
            case COMPLETED:
                return R.string.seed_shop_dialogue00_2;
        }
        return -1;
    }

    @Override
    public String getTAG() {
        return TAG;
    }
}
