package com.jackingaming.thestraylightrun.accelerometer.game.quests;

import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    private List<Quest> quests = new ArrayList<>();
    private int indexCurrent;

    public QuestManager() {
        indexCurrent = 0;
    }

    public Quest getCurrentQuest() {
        return quests.get(indexCurrent);
    }

    public boolean acceptQuest(Quest quest) {
        return quests.add(quest);
    }

    public boolean alreadyHaveQuest(Quest questChecking) {
        for (Quest quest : quests) {
            if (quest.getTAG().equals(questChecking.getTAG())) {
                return true;
            }
        }
        return false;
    }

    public List<Quest> getQuests() {
        return quests;
    }
}
