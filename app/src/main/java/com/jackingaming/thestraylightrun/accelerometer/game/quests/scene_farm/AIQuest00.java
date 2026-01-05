package com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm;

import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AIQuest00
        implements Quest {
    public static final String TAG = AIQuest00.class.getSimpleName();
    public static final String HONEY_POT = "honeyPot";
    public static final String TILE_REQUIREMENT_AS_STRING = GrowableTile.State.TILLED.toString();
    public static final int QUANTITY_REQUIRED = 4200;

    private Quest.State state;
    transient private Game game;
    private String[] dialogueArray;

    private Map<RequirementType, Map<String, Integer>> requirements;
    private Map<String, Integer> requirementTilesAsString;
    private Map<String, Item> startingItems;
    private Map<String, Integer> rewardsAsString;

    public AIQuest00(Game game, String[] dialogueArray) {
        state = State.NOT_STARTED;
        this.game = game;
        this.dialogueArray = dialogueArray;

        initRequirements();
        initStartingItemsAsString();
        initRewardsAsString();
    }

    @Override
    public void reload(Game game) {
        this.game = game;
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

        startingItems.put(HONEY_POT, new HoneyPot());

        for (Item item : startingItems.values()) {
            item.init(game);
        }
    }

    @Override
    public void initRewardsAsString() {
        rewardsAsString = new HashMap<>();
        rewardsAsString.put(REWARD_COINS, 320);
    }

    @Override
    public void dispenseStartingItems() {
        for (Item item : startingItems.values()) {
            Player.getInstance().receiveItem(item);
            if (item instanceof HoneyPot) {
                Log.e(TAG, "honeyPot GIVEN");
            }
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
                return dialogueArray[3];
        }
        return null;
    }

    @Override
    public String getQuestLabel() {
        String textQuestLabel = game.getContext().getString(R.string.text_ai_quest_00);
        return textQuestLabel;
    }

    @Override
    public void attachListener() {
        if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
            GrowableTile.StateChangeListener stateChangeListener = new GrowableTile.StateChangeListener() {
                @Override
                public void changeToTilled() {
                    Player.getInstance().getQuestManager().addTileAsString(
                            TILE_REQUIREMENT_AS_STRING);
                    Log.e(TAG, "numberOfTilledTiles: " + Player.getInstance().getQuestManager().getNumberOfTileAsString(TILE_REQUIREMENT_AS_STRING));
                    if (checkIfMetRequirements()) {
                        Log.e(TAG, "!!!REQUIREMENTS MET!!!");
                        game.getViewportListener().addAndShowParticleExplosionView(null);
                        dispenseRewards();
                    } else {
                        Log.e(TAG, "!!!REQUIREMENTS [not] MET!!!");
                    }
                }
            };

            ((SceneFarm) game.getSceneManager().getCurrentScene()).registerStateChangeListenerForAllGrowableTile(
                    stateChangeListener
            );
        }
    }

    @Override
    public void detachListener() {
        ((SceneFarm) game.getSceneManager().getCurrentScene()).unregisterStateChangeListenerForAllGrowableTile();
    }
}
