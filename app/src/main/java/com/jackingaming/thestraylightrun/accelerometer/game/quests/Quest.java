package com.jackingaming.thestraylightrun.accelerometer.game.quests;

import java.io.Serializable;

public interface Quest extends Serializable {
    public static final String REWARD_COINS = "coins";

    enum RequirementType {
        ENTITY,
        EVENT,
        ITEM,
        TILE;
    }

    enum State {
        NOT_STARTED,
        STARTED,
        COMPLETED;
    }

    void initRequirements();

    boolean checkIfMetRequirements();

    void initStartingItemsAsString();

    void initRewardsAsString();

    void dispenseStartingItems();

    void dispenseRewards();

    void changeToNextState();

    State getCurrentState();

    String getDialogueForCurrentState();

    String getTAG();

    void attachListener();

    void detachListener();
}
