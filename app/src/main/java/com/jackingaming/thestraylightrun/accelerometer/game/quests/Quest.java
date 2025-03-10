package com.jackingaming.thestraylightrun.accelerometer.game.quests;

public interface Quest {
    enum State {
        NOT_STARTED,
        STARTED,
        COMPLETED;
    }

    void changeToNextState();

    State getCurrentState();

    String getTAG();
}
