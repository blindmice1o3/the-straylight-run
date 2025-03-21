package com.jackingaming.thestraylightrun.accelerometer.game.quests;

import java.io.Serializable;
import java.util.Map;

public interface Quest extends Serializable {
    public static final String REWARD_COINS = "coins";

    enum RequirementType {
        ENTITY,
        ITEM,
        TILE;
    }

    enum State {
        NOT_STARTED,
        STARTED,
        COMPLETED;
    }

    boolean checkIfMetRequirements();

    Map<String, Integer> dispenseRewards();

    void changeToNextState();

    State getCurrentState();

    String getDialogueForCurrentState();

    String getTAG();

    void attachListener();

    void detachListener();
}
