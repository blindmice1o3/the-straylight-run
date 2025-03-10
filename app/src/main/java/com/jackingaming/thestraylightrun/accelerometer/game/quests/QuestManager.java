package com.jackingaming.thestraylightrun.accelerometer.game.quests;

import java.util.List;

public class QuestManager {
    private List<Quest> quests;
    private int indexCurrent;

    public QuestManager(List<Quest> quests) {
        this.quests = quests;
        indexCurrent = 0;
    }

    public Quest getCurrentQuest() {
        return quests.get(indexCurrent);
    }
}
