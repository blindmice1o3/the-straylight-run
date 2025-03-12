package com.jackingaming.thestraylightrun.accelerometer.game.quests;

import java.io.Serializable;

public interface Quest extends Serializable {
    enum State {
        NOT_STARTED,
        STARTED,
        COMPLETED;
    }

    void changeToNextState();

    State getCurrentState();

    int getDialogueForCurrentState();

    String getTAG();
}
