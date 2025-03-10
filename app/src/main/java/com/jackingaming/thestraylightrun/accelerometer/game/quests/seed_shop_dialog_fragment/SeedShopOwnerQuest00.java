package com.jackingaming.thestraylightrun.accelerometer.game.quests.seed_shop_dialog_fragment;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

public class SeedShopOwnerQuest00
        implements Quest {
    public static final String TAG = SeedShopOwnerQuest00.class.getSimpleName();

    private Quest.State state;

    public SeedShopOwnerQuest00() {
        state = State.NOT_STARTED;
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
}
