package com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm;

import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.IntroState;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RunOne
        implements Quest {
    public static final String TAG = RunOne.class.getSimpleName();
    public static final String ENTITY_REQUIREMENT_AS_STRING = Plant.TAG;
    public static final int QUANTITY_REQUIRED = 1;

    private Quest.State state;
    private Game game;
    private String[] dialogueArray;

    private Map<RequirementType, Map<String, Integer>> requirements;
    private Map<String, Integer> requirementEntitiesAsString;
    private Map<String, Item> startingItems;
    private Map<String, Integer> rewardsAsString;

    private String seedName;
    private String seedDescription;

    public RunOne(Game game) {
        this.game = game;
        dialogueArray = game.getContext().getResources().getStringArray(R.array.run_one_dialogue_array);
        state = State.NOT_STARTED;

        initRequirements();
        initStartingItemsAsString();
        initRewardsAsString();
    }

    @Override
    public void initRequirements() {
        requirements = new HashMap<>();

        requirementEntitiesAsString = new HashMap<>();
        requirementEntitiesAsString.put(ENTITY_REQUIREMENT_AS_STRING, QUANTITY_REQUIRED);

        requirements.put(RequirementType.ENTITY, requirementEntitiesAsString);
    }

    @Override
    public boolean checkIfMetRequirements() {
        if (state == State.COMPLETED) {
            Log.e(TAG, "checkIfMetRequirements() state == State.COMPLETED returning...");
            return false;
        }

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
                    case EVENT:
                        Set<String> eventsRequired = requirementsAsString.keySet();
                        for (String eventAsString : eventsRequired) {
                            int requiredNumberOfEventAsString = requirementsAsString.get(eventAsString);
                            int currentNumberOfEventAsString = Player.getInstance().getQuestManager().getNumberOfEventAsString(eventAsString);
                            return (currentNumberOfEventAsString >= requiredNumberOfEventAsString);
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

    @Override
    public void initStartingItemsAsString() {
        startingItems = new HashMap<>();

//        startingItems.put(SHOVEL,
//                new Shovel(
//                        new TillGrowableTileCommand(null)
//                )
//        );
//        startingItems.put(WATERING_CAN,
//                new WateringCan(
//                        new WaterGrowableTileCommand(null)
//                )
//        );
//        startingItems.put(MYSTERY_SEEDS,
//                new MysterySeed(
//                        new SeedGrowableTileCommand(null, MysterySeed.TAG)
//                )
//        );

        for (Item item : startingItems.values()) {
            item.init(game);
        }
    }

    @Override
    public void initRewardsAsString() {
        rewardsAsString = new HashMap<>();
        rewardsAsString.put(REWARD_COINS, 420);
    }

    @Override
    public void dispenseStartingItems() {
        for (Item item : startingItems.values()) {
            Player.getInstance().receiveItem(item);
        }

        attachListener();
        changeToNextState();
    }

    @Override
    public void dispenseRewards() {
        for (String rewardAsString : rewardsAsString.keySet()) {
            if (rewardAsString.equals(Quest.REWARD_COINS)) {
                int amountOfCoins = rewardsAsString.get(rewardAsString);

                Player.getInstance().incrementCurrency(amountOfCoins);
            }
        }

        detachListener();
        changeToNextState();
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
    public String getDialogueForCurrentState() {
        switch (state) {
            case NOT_STARTED:
                return dialogueArray[0];
            case STARTED:
                return dialogueArray[1];
            case COMPLETED:
                return String.format(dialogueArray[2], seedName, seedDescription);

        }
        return null;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void attachListener() {
        IntroState.SeedListener seedListener = new IntroState.SeedListener() {
            @Override
            public void onAssignedNameAndDescription() {
                Player.getInstance().getQuestManager().addEntityAsString(
                        ENTITY_REQUIREMENT_AS_STRING);
                Log.e(TAG, "number of named seeds: " + Player.getInstance().getQuestManager().getNumberOfEntityAsString(ENTITY_REQUIREMENT_AS_STRING));

                Log.e(TAG, "seedName: " + seedName);
                Log.e(TAG, "seedDescription: " + seedDescription);

                if (checkIfMetRequirements()) {
                    Log.e(TAG, "!!!REQUIREMENTS MET!!!");
                    game.getViewportListener().addAndShowParticleExplosionView();
                    dispenseRewards();
                } else {
                    Log.e(TAG, "!!!REQUIREMENTS [not] MET!!!");
                }
            }
        };

        game.getStateManager().getIntroState().setSeedListener(seedListener);
    }

    @Override
    public void detachListener() {
        game.getStateManager().getIntroState().setSeedListener(null);
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public void setSeedDescription(String seedDescription) {
        this.seedDescription = seedDescription;
    }
}
