package com.jackingaming.thestraylightrun.accelerometer.game.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManager {
    private List<Quest> quests = new ArrayList<>();
    private int indexCurrent;

    private Map<String, Integer> entitiesAsStringCollected;
    private Map<String, Integer> itemsAsStringCollected;
    private Map<String, Integer> tilesAsStringCollected;

    public QuestManager() {
        indexCurrent = 0;
        entitiesAsStringCollected = new HashMap<>();
        itemsAsStringCollected = new HashMap<>();
        tilesAsStringCollected = new HashMap<>();
    }

    public Quest getCurrentQuest() {
        return quests.get(indexCurrent);
    }

    public boolean acceptQuestAndAttachListener(Quest quest) {
        boolean acceptSuccessful = quests.add(quest);

        if (acceptSuccessful) {
            quest.attachListener();
        }

        return acceptSuccessful;
    }

    public boolean alreadyHaveQuest(String questTAG) {
        for (Quest quest : quests) {
            if (quest.getTAG().equals(questTAG)) {
                return true;
            }
        }
        return false;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void addEntityAsString(String entityAsString) {
        if (entitiesAsStringCollected.containsKey(entityAsString)) {
            int previousValue = entitiesAsStringCollected.get(entityAsString);
            entitiesAsStringCollected.put(entityAsString, (previousValue + 1));
        } else {
            entitiesAsStringCollected.put(entityAsString, 1);
        }
    }

    public void addItemAsString(String itemAsString) {
        if (itemsAsStringCollected.containsKey(itemAsString)) {
            int previousValue = itemsAsStringCollected.get(itemAsString);
            itemsAsStringCollected.put(itemAsString, (previousValue + 1));
        } else {
            itemsAsStringCollected.put(itemAsString, 1);
        }
    }

    public void addTileAsString(String tileAsString) {
        if (tilesAsStringCollected.containsKey(tileAsString)) {
            int previousValue = tilesAsStringCollected.get(tileAsString);
            tilesAsStringCollected.put(tileAsString, (previousValue + 1));
        } else {
            tilesAsStringCollected.put(tileAsString, 1);
        }
    }

    public Integer getNumberOfEntityAsString(String entityAsString) {
        if (entitiesAsStringCollected.containsKey(entityAsString)) {
            return entitiesAsStringCollected.get(entityAsString);
        } else {
            return 0;
        }
    }

    public Integer getNumberOfItemAsString(String itemAsString) {
        if (itemsAsStringCollected.containsKey(itemAsString)) {
            return itemsAsStringCollected.get(itemAsString);
        } else {
            return 0;
        }
    }

    public Integer getNumberOfTileAsString(String tileAsString) {
        if (tilesAsStringCollected.containsKey(tileAsString)) {
            return tilesAsStringCollected.get(tileAsString);
        } else {
            return 0;
        }
    }
}
