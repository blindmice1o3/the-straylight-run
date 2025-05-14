package com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RunTwo
        implements Quest {
    public static final String TAG = RunTwo.class.getSimpleName();
    public static final String TILE_REQUIREMENT_AS_STRING = "wateredTile";
    public static final int QUANTITY_REQUIRED = 3;

    private Quest.State state;
    private Game game;
    private String[] dialogueArray;

    private Map<RequirementType, Map<String, Integer>> requirements;
    private Map<String, Integer> requirementTilesAsString;
    private Map<String, Item> startingItems;
    private Map<String, Integer> rewardsAsString;

    public RunTwo(Game game, String[] dialogueArray) {
        state = State.NOT_STARTED;
        this.game = game;
        this.dialogueArray = dialogueArray;

        initRequirements();
        initStartingItemsAsString();
        initRewardsAsString();
    }

    @Override
    public void initRequirements() {
        requirements = new HashMap<>();

        requirementTilesAsString = new HashMap<>();
        requirementTilesAsString.put(TILE_REQUIREMENT_AS_STRING, QUANTITY_REQUIRED);

        requirements.put(RequirementType.TILE, requirementTilesAsString);
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
                            Log.e(TAG, "Player.getInstance().getQuestManager().getNumberOfTileAsString(tileAsString): " + Player.getInstance().getQuestManager().getNumberOfTileAsString(tileAsString));
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
        rewardsAsString.put(REWARD_COINS, 4200);
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
                return dialogueArray[2];
        }
        return null;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void attachListener() {
        if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
            GrowableTile.WaterChangeListener waterChangeListener = new GrowableTile.WaterChangeListener() {
                @Override
                public void changeToWatered() {
                    Player.getInstance().getQuestManager().addTileAsString(
                            TILE_REQUIREMENT_AS_STRING);
                    Log.e(TAG, "numberOfWateredTiles: " + Player.getInstance().getQuestManager().getNumberOfTileAsString(TILE_REQUIREMENT_AS_STRING));
                    if (checkIfMetRequirements()) {
                        Log.e(TAG, "!!!REQUIREMENTS MET!!!");
                        game.getViewportListener().addAndShowParticleExplosionView();
                        dispenseRewards();
                    } else {
                        Log.e(TAG, "!!!REQUIREMENTS [not] MET!!!");
                    }
                }
            };

            ((SceneFarm) game.getSceneManager().getCurrentScene()).registerWaterChangeListenerForAllGrowableTile(
                    waterChangeListener
            );
        }
    }

    @Override
    public void detachListener() {
        ((SceneFarm) game.getSceneManager().getCurrentScene()).unregisterWaterChangeListenerForAllGrowableTile();
    }
}
