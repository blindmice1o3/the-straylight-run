package com.jackingaming.thestraylightrun.accelerometer.game.quests.seed_shop_dialog_fragment;

import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SeedShopOwnerQuest00
        implements Quest {
    public static final String TAG = SeedShopOwnerQuest00.class.getSimpleName();
    public static final int QUANTITY_REQUIRED = 2;
    public static final int QUANTITY_UNDEFINED = -1;

    private Quest.State state;

    private Map<RequirementType, Map<String, Integer>> requirements;
    private Map<String, Integer> entitiesAsString;
    private Map<String, Integer> rewardsAsString;

    public SeedShopOwnerQuest00() {
        state = State.NOT_STARTED;
        initRequirements();
        initRewards();
    }

    private void initRequirements() {
        requirements = new HashMap<>();

        entitiesAsString = new HashMap<>();
        entitiesAsString.put(Plant.TAG, QUANTITY_REQUIRED);

        requirements.put(RequirementType.ENTITY, entitiesAsString);
    }

    @Override
    public boolean checkIfMetRequirements() {
        for (RequirementType requirementType : RequirementType.values()) {
            if (requirements.containsKey(requirementType)) {
                Log.e(TAG, "requirementType: " + requirementType);
                Map<String, Integer> requirementsAsString = requirements.get(requirementType);
                switch (requirementType) {
                    case ENTITY:
                        Set<String> entitiesRequired = requirementsAsString.keySet();
                        for (String entityAsString : entitiesRequired) {
                            int requiredNumberOfEntityAsString = requirementsAsString.get(entityAsString);
                            int currentNumberOfEntityAsString = Player.getInstance().getQuestManager().getNumberOfEntityAsString(entityAsString);
                            Log.e(TAG, "Player.getInstance().getQuestManager().getNumberOfEntityAsString(entityAsString): " + Player.getInstance().getQuestManager().getNumberOfEntityAsString(entityAsString));
                            return (currentNumberOfEntityAsString >= requiredNumberOfEntityAsString);
                        }
                        break;
                    case ITEM:
                        Set<String> itemsRequired = requirementsAsString.keySet();
                        for (String itemAsString : itemsRequired) {
                            int requiredNumberOfItemAsString = requirementsAsString.get(itemAsString);
                            int currentNumberOfItemAsString = Player.getInstance().getQuestManager().getNumberOfItemAsString(itemAsString);
                            return (currentNumberOfItemAsString >= requiredNumberOfItemAsString);
                        }
                        break;
                    case TILE:
                        Set<String> tilesRequired = requirementsAsString.keySet();
                        for (String tileAsString : tilesRequired) {
                            int requiredNumberOfTileAsString = requirementsAsString.get(tileAsString);
                            int currentNumberOfTileAsString = Player.getInstance().getQuestManager().getNumberOfTileAsString(tileAsString);
                            return (currentNumberOfTileAsString >= requiredNumberOfTileAsString);
                        }
                        break;
                }
            }
        }
        return false;
    }

    private void initRewards() {
        rewardsAsString = new HashMap<>();
        rewardsAsString.put(REWARD_COINS, 420);
    }

    @Override
    public Map<String, Integer> dispenseRewards() {
        return rewardsAsString;
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

    @Override
    public void attachListener() {
        Player.getInstance().setPlaceInShippingBinListener(
                new Creature.PlaceInShippingBinListener() {
                    @Override
                    public void sellableAdded(Sellable sellableAdded) {
                        if (sellableAdded instanceof Plant) {
                            Player.getInstance().getQuestManager().addEntityAsString(Plant.TAG);
                            Log.e(TAG, "numberOfPlants: " + Player.getInstance().getQuestManager().getNumberOfEntityAsString(Plant.TAG));
                            if (checkIfMetRequirements()) {
                                Log.e(TAG, "!!!REQUIREMENTS MET!!!");
                                Map<String, Integer> rewards = dispenseRewards();
                                for (String rewardAsString : rewards.keySet()) {
                                    if (rewardAsString.equals(Quest.REWARD_COINS)) {
                                        int amountOfCoins = rewards.get(rewardAsString);

                                        Player.getInstance().incrementCurrency(amountOfCoins);
                                    }
                                }
                                changeToNextState();
                                detachListener();
                            } else {
                                Log.e(TAG, "!!!REQUIREMENTS [not] MET!!!");
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void detachListener() {
        Player.getInstance().setPlaceInShippingBinListener(null);
    }
}
