package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.DialogueState;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.DialogueStateManager;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.farm_scene_ai.AIDialogue00;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.farm_scene_ai.AIDialogue01;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.farm_scene_ai.AIDialogue02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableIndoorTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.AimlessWalker;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.evo.Eel;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Egg;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.EntityCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowSystemParts;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowingPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Milk;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.seedshop.SeedShopDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time.TimeManager;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.daughter.RunThree;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.AIQuest00;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.RobotDialogQuest00;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.WorldScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SceneFarm extends Scene {
    public static final String TAG = SceneFarm.class.getSimpleName();

    private static final int X_INDEX_SPAWN_PLAYER_DEFAULT = 4;
    private static final int Y_INDEX_SPAWN_PLAYER_DEFAULT = 4;
    private static final int X_INDEX_SPAWN_ROBOT = 7;
    private static final int Y_INDEX_SPAWN_ROBOT = 7;
    private static final int X_INDEX_SPAWN_EEL_NEAR_COWBARN = 11;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_COWBARN = 7;
    private static final int X_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP = 18;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP = 9;
    private static final int X_INDEX_SPAWN_EEL_NEAR_SEEDSHOP = 6;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_SEEDSHOP = 15;
    private static final int X_INDEX_SPAWN_EEL_NEAR_HOTHOUSE = 20;
    private static final int Y_INDEX_SPAWN_EEL_NEAR_HOTHOUSE = 17;
    private static final int PATROL_LENGTH_EEL = 3 * Tile.WIDTH;
    private static final int X_INDEX_SPAWN_COLLIDING_ORBIT = X_INDEX_SPAWN_PLAYER_DEFAULT + 2;
    private static final int Y_INDEX_SPAWN_COLLIDING_ORBIT = Y_INDEX_SPAWN_PLAYER_DEFAULT;
    private static SceneFarm uniqueInstance;

    private boolean inSeedShopState;
    transient private SeedShopDialogFragment seedShopDialogFragment;

    private Robot robot;
    private List<GrowableTile> growableTiles;
    private ShippingBinTile.IncomeListener shippingBinIncomeListener;

    private Quest aIQuest00;
    private Quest robotDialogQuest00;
    private DialogueStateManager dialogueStateManager;

    private SceneFarm() {
        super();
        List<Entity> entitiesForFarm = createEntitiesForFarm();
        entityManager.loadEntities(entitiesForFarm);
        List<Item> itemsForFarm = createItemsForFarm();
        itemManager.loadItems(itemsForFarm);

        inSeedShopState = false;

        growableTiles = new ArrayList<>();

        shippingBinIncomeListener = new ShippingBinTile.IncomeListener() {
            @Override
            public void incrementCurrency(float amountToIncrement) {
                game.incrementCurrency(amountToIncrement);
            }
        };
    }

    private boolean newDay = false;

    public void startNewDay() {
        Log.e(TAG, "startNewDay()");
        newDay = true;

        for (GrowableTile growableTile : growableTiles) {
            growableTile.startNewDay();
        }
    }

    public boolean isInSeedShopState() {
        return inSeedShopState;
    }

    public void setInSeedShopState(boolean inSeedShopState) {
        this.inSeedShopState = inSeedShopState;
    }

    public SeedShopDialogFragment getSeedShopDialogFragment() {
        return seedShopDialogFragment;
    }

    public static SceneFarm getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SceneFarm();
        }
        return uniqueInstance;
    }

    public static void setInstance(SceneFarm sceneFarm) {
        uniqueInstance = sceneFarm;
    }

    private Random random = new Random();

    private int counterGrowingPotInstantiation = 0;

    private void addGrowingPotToRandomTile() {
        counterGrowingPotInstantiation++;
        Log.e(TAG, "counterGrowingPotInstantiation: " + counterGrowingPotInstantiation);

        int xIndex = -1;
        int yIndex = -1;
        boolean lookingForUnoccupiedWalkableTile = true;
        while (lookingForUnoccupiedWalkableTile) {
            xIndex = random.nextInt(
                    (tileManager.getWidthScene() / Tile.WIDTH)
            );
            yIndex = random.nextInt(
                    (tileManager.getHeightScene() / Tile.HEIGHT)
            );
            Tile tileRandom = tileManager.getTiles()[yIndex][xIndex];
            if (tileRandom.isWalkable()) {
                if (tileRandom instanceof GrowableTile) {
                    if (((GrowableTile) tileRandom).getEntity() == null) {
                        // unoccupied.
                        // TODO: load Item.
                        Item itemToAdd = new GrowingPot(
                                new TillGrowableIndoorTileCommand(null)
                        );
                        itemToAdd.init(game);
                        itemToAdd.setPosition(
                                (xIndex * Tile.WIDTH),
                                (yIndex * Tile.HEIGHT)
                        );
                        itemManager.addItem(itemToAdd);
                        lookingForUnoccupiedWalkableTile = false;
                    } else {
                        // Occupied.
                    }
                } else {
                    // walkable
                    // TODO: load Item.
                    Item itemToAdd = new Milk();
                    itemToAdd.init(game);
                    itemToAdd.setPosition(
                            (xIndex * Tile.WIDTH),
                            (yIndex * Tile.HEIGHT)
                    );
                    itemManager.addItem(itemToAdd);
                    lookingForUnoccupiedWalkableTile = false;
                }
            }
        }
    }

    public void reload(Game game) {
        this.game = game;
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                addSwarmOfEel();
            }
        }, 7, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                removeSwarmOfEel();
            }
        }, 9, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                addSwarmOfEel();
            }
        }, 4, 0, true);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                ShippingBinTile.sellStash();
            }
        }, 5, 0, true);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                removeSwarmOfEel();
            }
        }, 6, 0, true);
        updatePaintLightingColorFilter(game.getTimeManager().getModeOfDay());

        tileManager.reload(game);
        Map<String, Rect> transferPointsForFarm = createTransferPointsForFarm();
        tileManager.loadTransferPoints(transferPointsForFarm); // transferPoints are transient and should be reloaded everytime.
        reloadTileManager(game);

        for (GrowableTile growableTile : growableTiles) {
            growableTile.setGame(game);
        }
        Log.e(TAG, "growableTiles.size() is " + growableTiles.size());

        entityManager.init(game);
        itemManager.init(game);

        seedShopDialogFragment = new SeedShopDialogFragment();
        seedShopDialogFragment.init(game);

        if (needDisplaySeedShopFragment) {
            needDisplaySeedShopFragment = false;
            showSeedShopFragment();
        }
    }

    @Override
    public void init(Game game) {
        Log.e(TAG, "init()");

        this.game = game;
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                for (int i = 0; i < 6; i++) {
                    addGrowingPotToRandomTile();
                }
            }
        }, 7, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                addSwarmOfEel();
            }
        }, 7, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                removeSwarmOfEel();
            }
        }, 9, 0, false);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                addSwarmOfEel();
            }
        }, 4, 0, true);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                ShippingBinTile.sellStash();
            }
        }, 5, 0, true);
        game.getTimeManager().registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                removeSwarmOfEel();
            }
        }, 6, 0, true);

        // For scenes loaded from external file, the [create] and [init] steps in TileManager
        // are combined (unlike EntityManager and ItemManager).
        Tile[][] tilesForFarm = createAndInitTiles(game);
        tileManager.loadTiles(tilesForFarm);
        Map<String, Rect> transferPointsForFarm = createTransferPointsForFarm();
        tileManager.loadTransferPoints(transferPointsForFarm); // transferPoints are transient and should be reloaded everytime.
        tileManager.init(game); // updates tileManager's reference to the new game.

        // Set up GrowableTile in front of player's house with a [plant].
        Tile tileInitializedForHarvesting1 = tilesForFarm[17][11];
        if (tileInitializedForHarvesting1 instanceof GrowableTile) {
            ((GrowableTile) tileInitializedForHarvesting1).changeToSeeded(MysterySeed.TAG);
            ((GrowableTile) tileInitializedForHarvesting1).germinateSeed();
        }
        Tile tileInitializedForHarvesting2 = tilesForFarm[17][12];
        if (tileInitializedForHarvesting2 instanceof GrowableTile) {
            ((GrowableTile) tileInitializedForHarvesting2).changeToSeeded(MysterySeed.TAG);
            ((GrowableTile) tileInitializedForHarvesting2).germinateSeed();
        }
        Tile tileInitializedForHarvesting3 = tilesForFarm[17][13];
        if (tileInitializedForHarvesting3 instanceof GrowableTile) {
            ((GrowableTile) tileInitializedForHarvesting3).changeToSeeded(MysterySeed.TAG);
            ((GrowableTile) tileInitializedForHarvesting3).germinateSeed();
        }
        // Fix bug (having a QUANTITY_REQUIRED of 0) for
        // quest RunThree (its listener is never called).
        if (Plant.numberOfDiseasedPlant == 0) {
            int xIndex = 14;
            while (Plant.numberOfDiseasedPlant == 0) {
                if (xIndex > tilesForFarm[17].length - 1) {
                    Log.e(TAG, "xIndex > tilesForFarm[17].length!!!");
                    break;
                }

                Tile tileInitializingForHarvestingBugFix = tilesForFarm[17][xIndex];
                if (tileInitializingForHarvestingBugFix instanceof GrowableTile) {
                    ((GrowableTile) tileInitializingForHarvestingBugFix).changeToSeeded(MysterySeed.TAG);
                    ((GrowableTile) tileInitializingForHarvestingBugFix).germinateSeed();
                }
                xIndex++;
            }
        }


        for (int y = 0; y < tilesForFarm.length; y++) {
            for (int x = 0; x < tilesForFarm[y].length; x++) {
                Tile tile = tilesForFarm[y][x];
                if (tile instanceof GrowableTile) {
                    growableTiles.add(((GrowableTile) tile));
                }
            }
        }
        Log.e(TAG, "growableTiles.size() is " + growableTiles.size());

        entityManager.init(game);
        itemManager.init(game);

        aimlessWalker1.pickUp(growSystemParts1);
        aimlessWalker2.pickUp(growSystemParts2);
        aimlessWalker3.pickUp(growSystemParts3);
        aimlessWalker4.pickUp(growSystemParts4);
        aimlessWalker5.pickUp(growSystemParts5);
        aimlessWalker6.pickUp(growSystemParts6);
        aimlessWalker1.changeToWalk();
        aimlessWalker2.changeToWalk();
        aimlessWalker3.changeToWalk();
        aimlessWalker4.changeToWalk();
        aimlessWalker5.changeToWalk();
        aimlessWalker6.changeToWalk();

        // Age the [plant] so it's almost harvestable
        // (has to be done after Entity.init(), which sets ageInDays to 0).
        if (tileInitializedForHarvesting1 instanceof GrowableTile) {
            Plant plant1 = (Plant) ((GrowableTile) tileInitializedForHarvesting1).getEntity();
            Plant plant2 = (Plant) ((GrowableTile) tileInitializedForHarvesting2).getEntity();
            Plant plant3 = (Plant) ((GrowableTile) tileInitializedForHarvesting3).getEntity();
            for (int i = 0; i < 6; i++) {
                Log.e(TAG, "incrementing age: " + i);
                plant1.incrementAgeInDays();
                plant2.incrementAgeInDays();
                plant3.incrementAgeInDays();
            }

            int xIndexBugFix = 14;
            while (tilesForFarm[17][xIndexBugFix] instanceof GrowableTile &&
                    ((GrowableTile) tilesForFarm[17][xIndexBugFix]).getEntity() != null) {
                Plant plantBugFix = (Plant) ((GrowableTile) tilesForFarm[17][xIndexBugFix]).getEntity();
                for (int i = 0; i < 6; i++) {
                    plantBugFix.incrementAgeInDays();
                }
                xIndexBugFix++;
            }
        }

        Log.e(TAG, "BEFORE seedShipDialogFragment.init()");
        seedShopDialogFragment = new SeedShopDialogFragment();
        seedShopDialogFragment.init(game);
        Log.e(TAG, "AFTER seedShipDialogFragment.init()");

        if (needDisplaySeedShopFragment) {
            needDisplaySeedShopFragment = false;
            showSeedShopFragment();
        }

        String[] dialogueArray = game.getContext().getResources().getStringArray(R.array.clippit_dialogue_array);
        aIQuest00 = new AIQuest00(game, dialogueArray);
        robotDialogQuest00 = new RobotDialogQuest00(game, dialogueArray);

        List<DialogueState> dialogueStates = new ArrayList<>();
        dialogueStates.add(new AIDialogue00(game, aIQuest00));
        dialogueStates.add(new AIDialogue01(game, aIQuest00));
        dialogueStates.add(new AIDialogue02(game, robotDialogQuest00));
        dialogueStateManager = new DialogueStateManager(dialogueStates);

        ///////////////////
        setupForRunThree();
        ///////////////////
        Log.e(TAG, "init() END");
    }

    private static final int NUMBER_OF_MYSTERY_PRODUCT_TO_HARVEST = RunThree.HARVEST_QUANTITY_REQUIRED;
    private int numberOfMysteryProductReady = 0;

    public void setupForRunThree() {
        while (numberOfMysteryProductReady < NUMBER_OF_MYSTERY_PRODUCT_TO_HARVEST) {
            Tile[][] tiles = tileManager.getTiles();
            Log.e(TAG, "tiles.length: " + tiles.length);
            Log.e(TAG, "tiles[0].length: " + tiles[0].length);

            int yIndexRandom = (int) (Math.random() * tiles.length);
            int xIndexRandom = (int) (Math.random() * tiles[0].length);
            Log.e(TAG, "yIndexRandom: " + yIndexRandom);
            Log.e(TAG, "xIndexRandom: " + xIndexRandom);
            if (tiles[yIndexRandom][xIndexRandom] instanceof GrowableTile) {
                GrowableTile growableTileRandom = (GrowableTile) tiles[yIndexRandom][xIndexRandom];

                if (growableTileRandom.getState() == GrowableTile.State.UNTILLED) {
                    growableTileRandom.changeToTilled();
                }

                if (growableTileRandom.getState() == GrowableTile.State.TILLED) {
                    growableTileRandom.changeToSeeded(MysterySeed.TAG);
                    growableTileRandom.germinateSeed();

                    Plant plant = (Plant) growableTileRandom.getEntity();
                    for (int i = 0; i < 7; i++) {
                        plant.incrementAgeInDays();
                    }

                    numberOfMysteryProductReady++;
                }
            }
        }
    }

    public void changeToNextDialogueWithAI() {
        dialogueStateManager.changeToNextState();
    }

    @Override
    protected void doJustPressedButtonA() {
        super.doJustPressedButtonA();

        Player player = Player.getInstance();
        Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();
        Item itemCurrentlyFacing = player.getItemCurrentlyFacing();

        if (itemCurrentlyFacing != null) {
            Log.e(TAG, "itemCurrentlyFacing != null");
            player.respondToItemCollisionViaClick(
                    player.getItemCurrentlyFacing()
            );
        }

        if (player.hasCarryable() && entityCurrentlyFacing == null) {
            Log.e(TAG, "has carryable and entityFacing is null");
            Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();

            if (tileCurrentlyFacing instanceof ShippingBinTile) {
                Log.e(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");
                if (player.getCarryable() instanceof Sellable) {
                    Log.e(TAG, "carryable is Sellable");
                    player.placeInShippingBin();
                }
            } else if (tileCurrentlyFacing.isWalkable()) {
                Log.e(TAG, "tileCurrentlyFacing.isWalkable()");
                if (player.getCarryable() instanceof AimlessWalker) {
                    ((AimlessWalker) player.getCarryable()).changeToWalk();
                }

                player.placeDown();
            }
        } else if (entityCurrentlyFacing != null &&
                entityCurrentlyFacing instanceof Plant &&
                ((Plant) entityCurrentlyFacing).isHarvestable()) {
            player.pickUp(entityCurrentlyFacing);

            Tile tileFacing = player.checkTileCurrentlyFacing();
            if (tileFacing instanceof GrowableTile) {
                ((GrowableTile) tileFacing).changeToUntilled();
            } else {
                Log.e(TAG, "tileFacing NOT instanceof GrowableTile");
            }
        } else if (entityCurrentlyFacing != null &&
                entityCurrentlyFacing instanceof AimlessWalker) {
            ((AimlessWalker) entityCurrentlyFacing).changeToOff();

            player.pickUp(entityCurrentlyFacing);
        }
        // check item occupying StatsDisplayerFragment's button holder.
        else if (game.getItemStoredInButtonHolderA() instanceof TileCommandOwner) {
            TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderA();
            TileCommand tileCommand = tileCommandOwner.getTileCommand();

            Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
            Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
            tileCommand.setTile(tileCurrentlyFacing);
            tileCommand.execute();
        } else if (game.getItemStoredInButtonHolderA() instanceof EntityCommandOwner) {
            if (entityCurrentlyFacing != null) {
                EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderA();
                EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                Log.e(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                entityCommand.setEntity(entityCurrentlyFacing);
                entityCommand.execute();
            }
        }
        // !!! note: this should be last, other-wise there are side-effects !!!
        else if (!player.hasCarryable() &&
                entityCurrentlyFacing == null) {
            Log.e(TAG, "no carryable and entityFacing is null");
            Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
            if (tileCurrentlyFacing instanceof ShippingBinTile) {
                Log.e(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");
                ShippingBinTile.sellStash();
            }
        }
    }

    private boolean inDialogueWithClippitState = false;

    public void setInDialogueWithClippitState(boolean inDialogueWithClippitState) {
        this.inDialogueWithClippitState = inDialogueWithClippitState;
    }

    public void startDialogueWithAI(Bitmap portraitAI) {
        inDialogueWithClippitState = true;

        DialogueState currentDialogueState = dialogueStateManager.getCurrentDialogueState();

        if (currentDialogueState != null) {
            currentDialogueState.showDialogue(game.getContext().getResources(),
                    null, null, portraitAI);
        } else {
            Log.e(TAG, "currentDialogueState == null");
        }
    }

    @Override
    protected void doJustPressedButtonB() {
        super.doJustPressedButtonB();

        Player player = Player.getInstance();
        Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();

        if (inDialogueWithClippitState) {
            inDialogueWithClippitState = false;

            game.getTextboxListener().showStatsDisplayer();
        } else if (((SceneFarm) game.getSceneManager().getCurrentScene()).isInSeedShopState()) {
            ((SceneFarm) game.getSceneManager().getCurrentScene()).removeSeedShopFragment();

            game.getTextboxListener().showStatsDisplayer();

            // TODO: check for first quest's completion, show clippit TypeWriterDialogFragment,
            //  give/start second quest.
            boolean alreadyHaveQuest = Player.getInstance().alreadyHaveQuest(aIQuest00.getTAG());
            if (!alreadyHaveQuest && !player.getQuestManager().getQuests().isEmpty() && player.getQuestManager().getQuests().get(0).getCurrentState() == Quest.State.COMPLETED) {
                Log.e(TAG, "first quest's state == Quest.State.COMPLETED");
                Bitmap portraitAI = WorldScene.imagesClippit[0][0];
                startDialogueWithAI(portraitAI);
            }
        } else {
            // check item occupying StatsDisplayerFragment's button holder.
            if (game.getItemStoredInButtonHolderB() instanceof TileCommandOwner) {
                TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderB();
                TileCommand tileCommand = tileCommandOwner.getTileCommand();

                Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
                Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                tileCommand.setTile(tileCurrentlyFacing);
                tileCommand.execute();
            } else if (game.getItemStoredInButtonHolderB() instanceof EntityCommandOwner) {
                if (entityCurrentlyFacing != null) {
                    EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderB();
                    EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                    Log.e(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                    entityCommand.setEntity(entityCurrentlyFacing);
                    entityCommand.execute();
                }
            }
        }
    }

    @Override
    public void update(long elapsed) {
        super.update(elapsed);

        if (newDay) {
            Log.e(TAG, "NEW DAY");
            newDay = false;

            removeSwarmOfEel();
        }
    }

    public void showSeedShopFragment() {
        game.getTimeManager().setIsPaused(true);
        inSeedShopState = true;

        game.getViewportListener().showFragmentAndHideSurfaceView(seedShopDialogFragment);
    }

    public void removeSeedShopFragment() {
        game.getTimeManager().setIsPaused(false);
        inSeedShopState = false;
        game.getViewportListener().showSurfaceView();
    }

    private boolean needDisplaySeedShopFragment;

    public boolean isNeedDisplaySeedShopFragment() {
        return needDisplaySeedShopFragment;
    }

    public void setNeedDisplaySeedShopFragment(boolean needDisplaySeedShopFragment) {
        this.needDisplaySeedShopFragment = needDisplaySeedShopFragment;
    }

    @Override
    public void enter(List<Object> args) {
        super.enter(args);
        game.getTimeManager().setIsPaused(false);

        if (xLastKnown == 0 && yLastKnown == 0) {
            Player.getInstance().setX(X_INDEX_SPAWN_PLAYER_DEFAULT * Tile.WIDTH);
            Player.getInstance().setY(Y_INDEX_SPAWN_PLAYER_DEFAULT * Tile.HEIGHT);
        } else {
            Player.getInstance().setX(xLastKnown);
            Player.getInstance().setY(yLastKnown);
            Player.getInstance().setDirection(directionLastKnown);
        }
        GameCamera.getInstance().update(0L);
    }

    transient private Bitmap imageFarm;
    transient private Bitmap tileSpriteAndShippingBinQ1;
    transient private Bitmap tileSpriteAndShippingBinQ2;
    transient private Bitmap tileSpriteAndShippingBinQ3;
    transient private Bitmap tileSpriteAndShippingBinQ4;

    public void updateTilesBySeason(TimeManager.Season season) {
        imageFarm = cropImageFarm(game.getContext().getResources(), season);

        Tile[][] tiles = tileManager.getTiles();
        int rows = tiles.length;
        Log.e(TAG, "rows: " + rows);
        int columns = tiles[0].length;
        Log.e(TAG, "columns: " + columns);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;
                Bitmap tileSprite = Bitmap.createBitmap(imageFarm, xInPixel, yInPixel, widthInPixel, heightInPixel);
                Tile tile = tiles[y][x];

                if (tile instanceof ShippingBinTile) {
                    if (x == xIndexShippingBinQ1 && y == yIndexShippingBinQ1) {
                        Bitmap shippingBinQ1 = cropImageShippingBinTile(game.getContext().getResources(),
                                ShippingBinTile.Quadrant.TOP_LEFT);
                        tileSpriteAndShippingBinQ1 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(tileSpriteAndShippingBinQ1);
                        canvas.drawBitmap(tileSprite, 0, 0, null);
                        canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                        tileSprite = tileSpriteAndShippingBinQ1;
                    } else if (x == xIndexShippingBinQ2 && y == yIndexShippingBinQ2) {
                        Bitmap shippingBinQ2 = cropImageShippingBinTile(game.getContext().getResources(),
                                ShippingBinTile.Quadrant.TOP_RIGHT);
                        tileSpriteAndShippingBinQ2 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(tileSpriteAndShippingBinQ2);
                        canvas.drawBitmap(tileSprite, 0, 0, null);
                        canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                        tileSprite = tileSpriteAndShippingBinQ2;
                    } else if (x == xIndexShippingBinQ3 && y == yIndexShippingBinQ3) {
                        Bitmap shippingBinQ3 = cropImageShippingBinTile(game.getContext().getResources(),
                                ShippingBinTile.Quadrant.BOTTOM_LEFT);
                        tileSpriteAndShippingBinQ3 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(tileSpriteAndShippingBinQ3);
                        canvas.drawBitmap(tileSprite, 0, 0, null);
                        canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                        tileSprite = tileSpriteAndShippingBinQ3;
                    } else if (x == xIndexShippingBinQ4 && y == yIndexShippingBinQ4) {
                        Bitmap shippingBinQ4 = cropImageShippingBinTile(game.getContext().getResources(),
                                ShippingBinTile.Quadrant.BOTTOM_RIGHT);
                        tileSpriteAndShippingBinQ4 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(tileSpriteAndShippingBinQ4);
                        canvas.drawBitmap(tileSprite, 0, 0, null);
                        canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                        tileSprite = tileSpriteAndShippingBinQ4;
                    }

                    tile.setImage(tileSprite);
                } else if (tile instanceof GrowableTile) {
                    ((GrowableTile) tile).updateImageForStateUntilled(tileSprite);
                } else {
                    tile.setImage(tileSprite);
                }
            }
        }
    }

    private int xIndexShippingBinQ1, yIndexShippingBinQ1;
    private int xIndexShippingBinQ2, yIndexShippingBinQ2;
    private int xIndexShippingBinQ3, yIndexShippingBinQ3;
    private int xIndexShippingBinQ4, yIndexShippingBinQ4;

    public void reloadTileManager(Game game) {
        //rgbTileMapFarm is an image where each pixel represents a tile.
        Bitmap rgbTileMapFarm = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.tile_map_farm);
        int columns = rgbTileMapFarm.getWidth();            //Always need.
        int rows = rgbTileMapFarm.getHeight();              //Always need.

        Tile[][] tiles = tileManager.getTiles();

        imageFarm = cropImageFarm(
                game.getContext().getResources(),
                game.getTimeManager().getSeason());
        //DEFINE EACH ELEMENT.
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int xInPixel = x * Tile.WIDTH;
                int yInPixel = y * Tile.HEIGHT;
                int widthInPixel = Tile.WIDTH;
                int heightInPixel = Tile.HEIGHT;
                Bitmap tileSprite = Bitmap.createBitmap(imageFarm, xInPixel, yInPixel, widthInPixel, heightInPixel);

                int pixel = rgbTileMapFarm.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                if (pixel == Color.BLACK) {
                    tiles[y][x].init(game, x, y, tileSprite);
                } else if (pixel == Color.WHITE) {
                    tiles[y][x].init(game, x, y, tileSprite);
                } else if (pixel == Color.RED) {
                    tiles[y][x].init(game, x, y, tileSprite);
                } else if (pixel == Color.GREEN) {
                    tiles[y][x].init(game, x, y, tileSprite);
                }
                //SHIPPING_BIN_TILE
                else if ((red == 255) && (green == 255) && (blue == 1)) {
                    xIndexShippingBinQ1 = x;
                    yIndexShippingBinQ1 = y;

                    Bitmap shippingBinQ1 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.TOP_LEFT);
                    tileSpriteAndShippingBinQ1 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ1);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                    tiles[y][x].init(game, x, y, tileSpriteAndShippingBinQ1);
                } else if ((red == 255) && (green == 255) && (blue == 2)) {
                    xIndexShippingBinQ2 = x;
                    yIndexShippingBinQ2 = y;

                    Bitmap shippingBinQ2 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.TOP_RIGHT);
                    tileSpriteAndShippingBinQ2 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ2);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                    tiles[y][x].init(game, x, y, tileSpriteAndShippingBinQ2);
                } else if ((red == 255) && (green == 255) && (blue == 3)) {
                    xIndexShippingBinQ3 = x;
                    yIndexShippingBinQ3 = y;

                    Bitmap shippingBinQ3 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.BOTTOM_LEFT);
                    tileSpriteAndShippingBinQ3 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ3);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                    tiles[y][x].init(game, x, y, tileSpriteAndShippingBinQ3);
                } else if ((red == 255) && (green == 255) && (blue == 4)) {
                    xIndexShippingBinQ4 = x;
                    yIndexShippingBinQ4 = y;

                    Bitmap shippingBinQ4 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT);
                    tileSpriteAndShippingBinQ4 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ4);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                    tiles[y][x].init(game, x, y, tileSpriteAndShippingBinQ4);
                }
//                //TODO: handle special tiles (stashWood, flowerPlot, hotSpring)
                else if (pixel == Color.BLUE) {
                    tiles[y][x].init(game, x, y, tileSprite);
                } else {
                    tiles[y][x].init(game, x, y, tileSprite);
                }
            }
        }
    }

    public void registerWaterChangeListenerForAllGrowableTile(GrowableTile.OutdoorWaterChangeListener waterChangeListener) {
        Tile[][] tiles = tileManager.getTiles();
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[0].length; column++) {
                if (tiles[row][column] instanceof GrowableTile) {
                    ((GrowableTile) tiles[row][column]).setOutdoorWaterChangeListener(
                            waterChangeListener
                    );
                }
            }
        }
    }

    public void unregisterWaterChangeListenerForAllGrowableTile() {
        Tile[][] tiles = tileManager.getTiles();
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[0].length; column++) {
                if (tiles[row][column] instanceof GrowableTile) {
                    ((GrowableTile) tiles[row][column]).setOutdoorWaterChangeListener(
                            null
                    );
                }
            }
        }
    }

    public void registerStateChangeListenerForAllGrowableTile(GrowableTile.StateChangeListener stateChangeListener) {
        Tile[][] tiles = tileManager.getTiles();
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[0].length; column++) {
                if (tiles[row][column] instanceof GrowableTile) {
                    ((GrowableTile) tiles[row][column]).setStateChangeListener(
                            stateChangeListener
                    );
                }
            }
        }
    }

    public void unregisterStateChangeListenerForAllGrowableTile() {
        Tile[][] tiles = tileManager.getTiles();
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[0].length; column++) {
                if (tiles[row][column] instanceof GrowableTile) {
                    ((GrowableTile) tiles[row][column]).setStateChangeListener(
                            null
                    );
                }
            }
        }
    }

    private Tile[][] createAndInitTiles(Game game) {
        //rgbTileMapMyFarm is an image where each pixel represents a tile.
        Bitmap rgbTileMapFarm = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.tile_map_my_farm);
        int columns = rgbTileMapFarm.getWidth();            //Always need.
        int rows = rgbTileMapFarm.getHeight();              //Always need.

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Tile[][] tiles = new Tile[rows][columns];           //Always need.
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        imageFarm = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.scene_town_aligned_doors);
//        imageFarm = cropImageFarm(
//                game.getContext().getResources(),
//                game.getTimeManager().getSeason());
        //DEFINE EACH ELEMENT.
        int tileWidth = 64;
        int tileHeight = 64;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int xInPixel = x * tileWidth;
                int yInPixel = y * tileHeight;
                int widthInPixel = tileWidth;
                int heightInPixel = tileHeight;
                Bitmap tileSprite = Bitmap.createBitmap(imageFarm, xInPixel, yInPixel, widthInPixel, heightInPixel);

                int pixel = rgbTileMapFarm.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                if (pixel == Color.BLACK) {
                    Tile tile = new Tile("black");
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new GenericSolidTile(gameCartridge, x, y);
                } else if (pixel == Color.WHITE) {
//                    Tile tile = new Tile("white");
                    Tile tile = new GrowableTile(GrowableTile.TAG, new GrowableTile.EntityListener() {
                        @Override
                        public void addEntityToScene(Entity entityToAdd) {
                            entityManager.addEntity(entityToAdd);
                        }
                    });
                    tile.init(game, x, y, tileSprite);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new GrowableGroundTile(gameCartridge, x, y);
                    //tiles[y][x] = new GenericWalkableTile(gameCartridge, x, y);
                } else if (pixel == Color.RED) {
                    Tile tile = new Tile("red");
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new SignPostTile(gameCartridge, x, y);
                } else if (pixel == Color.GREEN) {
                    Tile tile = new Tile("green");
                    tile.init(game, x, y, tileSprite);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new TransferPointTile(gameCartridge, x, y);
                }
                //SHIPPING_BIN_TILE
                else if ((red == 255) && (green == 255) && (blue == 1)) {
                    xIndexShippingBinQ1 = x;
                    yIndexShippingBinQ1 = y;

                    Bitmap shippingBinQ1 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.TOP_LEFT);
                    tileSpriteAndShippingBinQ1 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ1);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ1, 0, 0, null);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_LEFT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, tileSpriteAndShippingBinQ1);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                } else if ((red == 255) && (green == 255) && (blue == 2)) {
                    xIndexShippingBinQ2 = x;
                    yIndexShippingBinQ2 = y;

                    Bitmap shippingBinQ2 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.TOP_RIGHT);
                    tileSpriteAndShippingBinQ2 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ2);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ2, 0, 0, null);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.TOP_RIGHT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, tileSpriteAndShippingBinQ2);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                } else if ((red == 255) && (green == 255) && (blue == 3)) {
                    xIndexShippingBinQ3 = x;
                    yIndexShippingBinQ3 = y;

                    Bitmap shippingBinQ3 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.BOTTOM_LEFT);
                    tileSpriteAndShippingBinQ3 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ3);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ3, 0, 0, null);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_LEFT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, tileSpriteAndShippingBinQ3);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                } else if ((red == 255) && (green == 255) && (blue == 4)) {
                    xIndexShippingBinQ4 = x;
                    yIndexShippingBinQ4 = y;

                    Bitmap shippingBinQ4 = cropImageShippingBinTile(game.getContext().getResources(),
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT);
                    tileSpriteAndShippingBinQ4 = Bitmap.createBitmap(tileSprite.getWidth(), tileSprite.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(tileSpriteAndShippingBinQ4);
                    canvas.drawBitmap(tileSprite, 0, 0, null);
                    canvas.drawBitmap(shippingBinQ4, 0, 0, null);

                    Tile tile = new ShippingBinTile(ShippingBinTile.TAG,
                            ShippingBinTile.Quadrant.BOTTOM_RIGHT,
                            shippingBinIncomeListener);
                    tile.init(game, x, y, tileSpriteAndShippingBinQ4);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                }
//                //TODO: handle special tiles (stashWood, flowerPlot, hotSpring)
                else if (pixel == Color.BLUE) {
                    Tile tile = new Tile("blue");
                    tile.init(game, x, y, tileSprite);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
//                    tiles[y][x] = new GenericSolidTile(gameCartridge, x, y);
                } else {
                    Tile tile = new Tile("default");
                    Bitmap defaultImage = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon_gridview);
                    tile.init(game, x, y, defaultImage);
                    tile.setWalkable(false);
                    tiles[y][x] = tile;
                }
            }
        }

        return tiles;
    }

    public static Bitmap cropImageFarm(Resources resources, TimeManager.Season season) {
        Log.d(TAG, "SceneFarm.cropImageFarm(Resources resources, TimeManager.Season season)");

        Bitmap farmSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.gbc_hm3_farm);
        Bitmap croppedImageFarm = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        switch (season) {
            case SPRING:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 14, 39, 384, 400);
                break;
            case SUMMER:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 410, 40, 384, 400);
                break;
            case FALL:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 806, 40, 384, 400);
                break;
            case WINTER:
                croppedImageFarm = Bitmap.createBitmap(farmSpriteSheet, 1202, 40, 384, 400);
                break;
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "Bitmap croppedImageFarm's (width, height): " + croppedImageFarm.getWidth() + ", " + croppedImageFarm.getHeight());

        return croppedImageFarm;
    }

    public static Bitmap cropImageShippingBinTile(Resources resources, ShippingBinTile.Quadrant quadrant) {
        Log.d(TAG, "SceneFarm.cropImageShippingBinTile(Resources, ShippingBinTile.Quadrant)");

        Bitmap customTilesSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.shipping_bin128x128);
//        Bitmap customTilesSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.custom_hm_tile_sprites_sheet);
        Bitmap shippingBinTile = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        switch (quadrant) {
            case TOP_LEFT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 0, 0, 64, 64);
                break;
            case TOP_RIGHT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 64, 0, 64, 64);
                break;
            case BOTTOM_LEFT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 0, 64, 64, 64);
                break;
            case BOTTOM_RIGHT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 64, 64, 64, 64);
                break;
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "shippingBinTile: " + shippingBinTile.getWidth() + ", " + shippingBinTile.getHeight());

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //May be redundant because local variable.
        customTilesSpriteSheet = null;
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "SceneFarm.cropImageShippingBinTile(Resources, ShippingBinTile.Quadrant)... customTilesSpriteSheet is null? " + customTilesSpriteSheet);

        return shippingBinTile;
    }

    private Map<String, Rect> createTransferPointsForFarm() {
        Map<String, Rect> transferPoints = new HashMap<String, Rect>();
        transferPoints.put("HOUSE_LEVEL_01", new Rect((6 * Tile.WIDTH), (4 * Tile.HEIGHT),
                (6 * Tile.WIDTH) + (1 * Tile.WIDTH), (4 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        transferPoints.put("HOTHOUSE", new Rect((12 * Tile.WIDTH), (10 * Tile.HEIGHT),
                (12 * Tile.WIDTH) + (1 * Tile.WIDTH), (10 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
//        transferPoints.put("SHEEP_PEN", new Rect((21 * Tile.WIDTH), (7 * Tile.HEIGHT),
//                (21 * Tile.WIDTH) + (1 * Tile.WIDTH), (7 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        transferPoints.put("CHICKEN_COOP", new Rect((11 * Tile.WIDTH), (13 * Tile.HEIGHT),
                (11 * Tile.WIDTH) + (1 * Tile.WIDTH), (13 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        transferPoints.put("COW_BARN", new Rect((7 * Tile.WIDTH), (14 * Tile.HEIGHT),
                (7 * Tile.WIDTH) + (1 * Tile.WIDTH), (14 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));

        transferPoints.put("SEEDS_SHOP", new Rect((12 * Tile.WIDTH), (5 * Tile.HEIGHT),
                (12 * Tile.WIDTH) + (1 * Tile.WIDTH), (5 * Tile.HEIGHT) + (1 * Tile.HEIGHT)));
        return transferPoints;
    }

    private AimlessWalker aimlessWalker1, aimlessWalker2, aimlessWalker3, aimlessWalker4, aimlessWalker5, aimlessWalker6;

    private List<Entity> createEntitiesForFarm() {
        List<Entity> entities = new ArrayList<Entity>();
        // TODO: Insert scene specific entities here.
        robot = new Robot((X_INDEX_SPAWN_ROBOT * Tile.WIDTH),
                (Y_INDEX_SPAWN_ROBOT * Tile.HEIGHT));
        aimlessWalker1 = new AimlessWalker(AimlessWalker.Type.COW,
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                (Y_INDEX_SPAWN_ROBOT * Tile.HEIGHT));
        aimlessWalker2 = new AimlessWalker(AimlessWalker.Type.CHICKEN,
                ((X_INDEX_SPAWN_ROBOT) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 1) * Tile.HEIGHT));
        aimlessWalker3 = new AimlessWalker(AimlessWalker.Type.CHICK,
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 1) * Tile.HEIGHT));
        aimlessWalker4 = new AimlessWalker(AimlessWalker.Type.COW,
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 2) * Tile.HEIGHT));
        aimlessWalker5 = new AimlessWalker(AimlessWalker.Type.CHICKEN,
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 3) * Tile.HEIGHT));
        aimlessWalker6 = new AimlessWalker(AimlessWalker.Type.CHICK,
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 3) * Tile.HEIGHT));
        entities.add(robot);
        entities.add(aimlessWalker1);
        entities.add(aimlessWalker2);
        entities.add(aimlessWalker3);
        entities.add(aimlessWalker4);
        entities.add(aimlessWalker5);
        entities.add(aimlessWalker6);
        return entities;
    }

    public void addSwarmOfEel() {
        Eel eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_COWBARN * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_COWBARN * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
        eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_CHICKENCOOP * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
        eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_SEEDSHOP * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_SEEDSHOP * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
        eel = new Eel((X_INDEX_SPAWN_EEL_NEAR_HOTHOUSE * Tile.WIDTH),
                (Y_INDEX_SPAWN_EEL_NEAR_HOTHOUSE * Tile.HEIGHT),
                Eel.DirectionFacing.LEFT, PATROL_LENGTH_EEL);
        eel.setState(Eel.State.MOVE_RANDOMLY);
        eel.init(game);
        entityManager.addEntity(
                eel
        );
    }

    public void removeSwarmOfEel() {
        for (Entity e : entityManager.getEntities()) {
            if (e instanceof Eel) {
                e.setActive(false);
            }
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                game.getViewportListener().stopBlinkingBorder();
            }
        });
    }

    private GrowSystemParts growSystemParts1, growSystemParts2, growSystemParts3, growSystemParts4, growSystemParts5, growSystemParts6;
    private Milk milkOnGround;
    private Egg eggOnGround;

    private List<Item> createItemsForFarm() {
        List<Item> items = new ArrayList<Item>();
        // TODO: Insert scene specific items here.
        growSystemParts1 = new GrowSystemParts(1);
        growSystemParts2 = new GrowSystemParts(2);
        growSystemParts3 = new GrowSystemParts(3);
        growSystemParts4 = new GrowSystemParts(4);
        growSystemParts5 = new GrowSystemParts(5);
        growSystemParts6 = new GrowSystemParts(6);
        milkOnGround = new Milk();
        milkOnGround.setPosition(
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 1) * Tile.HEIGHT)
        );
        eggOnGround = new Egg();
        eggOnGround.setPosition(
                ((X_INDEX_SPAWN_ROBOT - 1) * Tile.WIDTH),
                ((Y_INDEX_SPAWN_ROBOT + 2) * Tile.HEIGHT)
        );
        items.add(eggOnGround);
        items.add(milkOnGround);
        items.add(growSystemParts1);
        items.add(growSystemParts2);
        items.add(growSystemParts3);
        items.add(growSystemParts4);
        items.add(growSystemParts5);
        items.add(growSystemParts6);
        return items;
    }


    public Robot getRobot() {
        return robot;
    }
}