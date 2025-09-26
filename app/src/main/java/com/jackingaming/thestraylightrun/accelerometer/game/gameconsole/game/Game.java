package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.FCVDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.GameConsoleFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.adapters.ItemRecyclerViewAdapter;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.SceneManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.BounceEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.OpenPlantDialogEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.OpenRobotDialogEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.RemoveEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.SeedGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableIndoorTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.WaterGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowingPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.ItemStackable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.PlantInspectioner200;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.RobotReprogrammer4000;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Scissors;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Shovel;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.WateringCan;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.computer.ComputerDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneChickenCoop;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneCowBarn;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHothouse;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.StateManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time.TimeManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer.StatsDisplayerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.controllers.QuestAdapter;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.controllers.QuestDetailFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.controllers.QuestLogFragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Game {
    public static final String TAG = Game.class.getSimpleName();

    public interface ItemInButtonHolderListener extends Serializable {
        void onChangeItemInButtonHolder(Item itemButtonHolderA, Item itemButtonHolderB);
    }

    private ItemInButtonHolderListener itemInButtonHolderListener;

    public void setItemInButtonHolderListener(ItemInButtonHolderListener itemInButtonHolderListener) {
        this.itemInButtonHolderListener = itemInButtonHolderListener;
    }

    public interface ViewportListener extends Serializable {
        void showFragmentAndHideSurfaceView(Fragment fragmentReplacingSurfaceView);

        void showSurfaceView();

        void startBlinkingBorder();

        void stopBlinkingBorder();

        boolean isBlinkingBorderOn();

        void addAndShowParticleExplosionView();
    }

    private ViewportListener viewportListener;

    public ViewportListener getViewportListener() {
        return viewportListener;
    }

    public void setViewportListener(ViewportListener viewportListener) {
        this.viewportListener = viewportListener;
    }

    public interface TextboxListener extends Serializable {
        void showTextbox(TypeWriterDialogFragment typeWriterDialogFragment);

        void showStatsDisplayer();
    }

    private TextboxListener textboxListener;

    public void setTextboxListener(TextboxListener textboxListener) {
        this.textboxListener = textboxListener;
    }

    public interface StatsChangeListener extends Serializable {
        void onCurrencyChange(float currency);

        void onTimeChange(String inGameClockTime, String calendarText);

        void onButtonHolderAChange(Item itemA);

        void onButtonHolderBChange(Item itemB);

        void onRefreshQuantityInButtonHolderAAndB(ItemStackable stackableA, ItemStackable stackableB);
    }

    private StatsChangeListener statsChangeListener;

    public void setStatsChangeListener(StatsChangeListener statsChangeListener) {
        this.statsChangeListener = statsChangeListener;
    }

    private Context context;
    private InputManager inputManager;
    private SurfaceHolder holder;
    private int widthViewport;
    private int heightViewport;
    private boolean loadNeeded;
    private String gameTitle;
    private com.jackingaming.thestraylightrun.accelerometer.game.Game.Run run;

    private TimeManager timeManager;
    private SceneManager sceneManager;
    private StateManager stateManager;

    /**
     * Displayed in StatsDisplayerFragment through Game.StatsChangeListener.onCurrencyChange(int currency).
     * Triggered via Player.respondToItemCollisionViaMove(Item item) when item is instanceof HoneyPot.
     */
    private float currency;

    private List<ItemStackable> backpack;
    private List<ItemStackable> backpackWithoutItemsDisplayingInButtonHolders;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private Dialog backpackDialog;
    private Item scissors;

    private Item itemStoredInButtonHolderA;
    private Item itemStoredInButtonHolderB;
    private StatsDisplayerFragment.ButtonHolder buttonHolderCurrentlySelected;

    private boolean paused;
    private boolean inBackpackDialogState;

    public Game(String gameTitle, com.jackingaming.thestraylightrun.accelerometer.game.Game.Run run) {
        loadNeeded = false;
        this.gameTitle = gameTitle;
        this.run = run;

        timeManager = new TimeManager();
        sceneManager = new SceneManager(gameTitle);
        stateManager = new StateManager();

        currency = 100f;

        paused = false;
        inBackpackDialogState = false;
    }

    public void startNewDay() {
        timeManager.callRemainingActiveEventTimeObjects();
        timeManager.setAllEventTimeObjectsToActive();
        timeManager.resetInGameClock();
        timeManager.incrementDay();

        // TODO: implement startNewDay().
        SceneFarm.getInstance().startNewDay();
//        SceneFarm.getInstance().startNewDay(); //SEED_SHOP
        SceneChickenCoop.getInstance().startNewDay();
        SceneCowBarn.getInstance().startNewDay();
//        SceneSheepPen.getInstance().startNewDay();
        SceneHothouse.getInstance().startNewDay();
    }

    public void addItemToBackpack(Item item) {
        for (ItemStackable stackable : backpack) {
            if (stackable.getItem().getName().equals(item.getName())) {
                stackable.increment();
                statsChangeListener.onRefreshQuantityInButtonHolderAAndB(
                        findItemStackableViaItem(
                                itemStoredInButtonHolderA
                        ),
                        findItemStackableViaItem(
                                itemStoredInButtonHolderB
                        )
                );
                return;
            }
        }
        backpack.add(new ItemStackable(item, 1));
    }

    public void removeItemFromBackpack(Item item) {
        for (int i = 0; i < backpack.size(); i++) {
            ItemStackable stackable = backpack.get(i);

            if (stackable.getItem().getName().equals(item.getName())) {
                stackable.decrement();

                if (stackable.getQuantity() == 0) {
                    if (item == itemStoredInButtonHolderA) {
                        itemStoredInButtonHolderA = null;
                    } else if (item == itemStoredInButtonHolderB) {
                        itemStoredInButtonHolderB = null;
                    }

                    backpack.remove(i);
                }

                statsChangeListener.onRefreshQuantityInButtonHolderAAndB(
                        findItemStackableViaItem(
                                itemStoredInButtonHolderA
                        ),
                        findItemStackableViaItem(
                                itemStoredInButtonHolderB
                        )
                );
            }
        }
    }

    public void init(Context context, InputManager inputManager, SurfaceHolder holder, int widthViewport, int heightViewport) {
        this.context = context;
        this.inputManager = inputManager;
        this.holder = holder;
        this.widthViewport = widthViewport;
        this.heightViewport = heightViewport;

        timeManager.init(this, statsChangeListener);

        sceneManager.init(this);
        stateManager.init(this);


        backpack = new ArrayList<ItemStackable>();
        Item growingPot = new GrowingPot(
                new TillGrowableIndoorTileCommand(null)
        );
        growingPot.init(this);
        Item mysterySeed = new MysterySeed(
                new SeedGrowableTileCommand(null, MysterySeed.TAG)
        );
        mysterySeed.init(this);
        Item shovel = new Shovel(
                new TillGrowableTileCommand(null)
        );
        shovel.init(this);
        Item wateringCan = new WateringCan(
                new WaterGrowableTileCommand(null)
        );
        wateringCan.init(this);
        Item bugCatchingNet = new BugCatchingNet(
                new BounceEntityCommand(null)
        );
        bugCatchingNet.init(this);
        Item robotReprogrammer4000 = new RobotReprogrammer4000(
                new OpenRobotDialogEntityCommand(null)
        );
        robotReprogrammer4000.init(this);
        Item plantInspectioner200 = new PlantInspectioner200(
                new OpenPlantDialogEntityCommand(null)
        );
        plantInspectioner200.init(this);
        scissors = new Scissors(
                new RemoveEntityCommand(this, null)
        );
        scissors.init(this);

        addItemToBackpack(mysterySeed);
        addItemToBackpack(growingPot);
        addItemToBackpack(shovel);
        addItemToBackpack(wateringCan);
        addItemToBackpack(bugCatchingNet);
        addItemToBackpack(robotReprogrammer4000);
        addItemToBackpack(plantInspectioner200);
        addItemToBackpack(scissors);

        backpackWithoutItemsDisplayingInButtonHolders = new ArrayList<ItemStackable>();

        itemStoredInButtonHolderA = null;
        itemStoredInButtonHolderB = null;
        buttonHolderCurrentlySelected = StatsDisplayerFragment.ButtonHolder.A;
        //////////////////////////////////////////////
        for (ItemStackable stackable : backpack) {
            stackable.getItem().init(this);
        }

        createBackpackDialog();

        if (loadNeeded) {
            loadViaOS();
            loadNeeded = false;
        }

        statsChangeListener.onCurrencyChange(currency);
        GameCamera.getInstance().init(Player.getInstance(), widthViewport, heightViewport,
                sceneManager.getCurrentScene().getTileManager().getWidthScene(), sceneManager.getCurrentScene().getTileManager().getHeightScene());
    }

    private void createBackpackDialog() {
        final Context contextFinal = context;
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(context, backpackWithoutItemsDisplayingInButtonHolders);
        ItemRecyclerViewAdapter.ItemClickListener itemClickListener = new ItemRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(contextFinal, "Game.createBackpackDialog() ItemRecyclerViewAdapter.ItemClickListener.onItemClick(View view, int position): " + backpack.get(position), Toast.LENGTH_SHORT).show();

                Item item = backpackWithoutItemsDisplayingInButtonHolders.get(position).getItem();
                Log.d(TAG, "Game.createBackpackDialog() item: " + item.getName());
                switch (buttonHolderCurrentlySelected) {
                    case A:
                        Log.d(TAG, "Game.createBackpackDialog() itemClickListener.onItemClick() case A!!!");
                        itemStoredInButtonHolderA = item;
                        statsChangeListener.onButtonHolderAChange(item);
                        backpackDialog.dismiss();
                        break;
                    case B:
                        Log.d(TAG, "Game.createBackpackDialog() itemClickListener.onItemClick() case B!!!");
                        itemStoredInButtonHolderB = item;
                        statsChangeListener.onButtonHolderBChange(item);
                        backpackDialog.dismiss();
                        break;
                }

                itemInButtonHolderListener.onChangeItemInButtonHolder(
                        itemStoredInButtonHolderA, itemStoredInButtonHolderB
                );
            }
        };
        itemRecyclerViewAdapter.setClickListener(itemClickListener);

        View viewContainingRecyclerView = LayoutInflater.from(context).inflate(R.layout.view_cart_recyclerview, null);
        RecyclerView recyclerView = (RecyclerView) viewContainingRecyclerView.findViewById(R.id.recyclerview_view_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        backpackDialog = new AlertDialog.Builder(context)
                .setTitle("Backpack")
                .setView(viewContainingRecyclerView)
                .create();
        backpackDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                paused = false;
                inBackpackDialogState = false;
            }
        });
    }

    public void showToastMessageLong(final String message) {
        // DETERMINE heightActionBar.
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        final int heightActionBar = context.getResources().getDimensionPixelSize(tv.resourceId);
        // DETERMINE heightStatusBar.
        int idStatusBarHeight = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        final int heightStatusBar = context.getResources().getDimensionPixelSize(idStatusBarHeight);

        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                        0, (heightActionBar + heightStatusBar));
                toast.show();
            }
        });
    }

    private String savedFileViaOSFileName = "savedFileViaOS" + getClass().getSimpleName() + ".ser";

    public void saveViaOS() {
        saveToFile(savedFileViaOSFileName);
    }

    private void loadViaOS() {
        loadFromFile(savedFileViaOSFileName);
    }

    private String savedFileViaUserInputFileName = "savedFileViaUserInput" + gameTitle + ".ser";

    public void saveViaUserInput() {
        saveToFile(savedFileViaUserInputFileName);
    }

    public void loadViaUserInput() throws ExecutionException, InterruptedException {
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                loadFromFile(savedFileViaUserInputFileName);
                return null;
            }
        };

        FutureTask<Void> task = new FutureTask<>(callable);
        ((MainActivity) context).runOnUiThread(task);
        task.get(); // Blocks
    }

    private void saveToFile(String fileName) {
        Log.e(TAG, getClass().getSimpleName() + ".saveToFile(String fileName) START fileName: " + fileName);
        try (FileOutputStream fs = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            // MUST save form before exit() (otherwise it'll load formBeforeThisScene).
            os.writeObject(Player.getInstance().getForm());
            // Record player's xLastKnown and yLastKnown for the current scene.
            sceneManager.getCurrentScene().exit();

            Log.e(TAG, "BEFORE timeManager");
            os.writeObject(timeManager);
            Log.e(TAG, "AFTER timeManager");
            os.writeObject(sceneManager);
            os.writeFloat(currency);

            os.writeObject(backpack);
            boolean hasItemInButtonHolderA = (itemStoredInButtonHolderA != null);
            os.writeBoolean(hasItemInButtonHolderA);
            if (hasItemInButtonHolderA) {
                os.writeObject(itemStoredInButtonHolderA);
            }
            boolean hasItemInButtonHolderB = (itemStoredInButtonHolderB != null);
            os.writeBoolean(hasItemInButtonHolderB);
            if (hasItemInButtonHolderB) {
                os.writeObject(itemStoredInButtonHolderB);
            }
            int ordinalValueOfButtonHolderCurrentlySelected = buttonHolderCurrentlySelected.ordinal();
            Log.d(TAG, getClass().getSimpleName() + ".saveToFile(String fileName) ordinalValueOfButtonHolderCurrenlySelected: " + ordinalValueOfButtonHolderCurrentlySelected);
            os.writeInt(ordinalValueOfButtonHolderCurrentlySelected);

            // do AFTER SceneFarm is reloaded.
            List<Item> seedShopInventory = SceneFarm.getInstance().getSeedShopDialogFragment().getSeedShopInventory();
            os.writeObject(seedShopInventory);

            boolean isBlinkingBorderOn = viewportListener.isBlinkingBorderOn();
            os.writeBoolean(isBlinkingBorderOn);

            os.writeBoolean(paused);
            os.writeBoolean(inBackpackDialogState);
            if (inBackpackDialogState) {
                // If Dialog is open during an emergency shutdown, dismiss Dialog to prevent Exception.
                backpackDialog.dismiss();
            }

            // Scenes where player's form is specified need enter()
            // called because exit() was called at start of saving.
            sceneManager.getCurrentScene().enter(null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, getClass().getSimpleName() + ".saveToFile(String fileName) FINISHED.");
    }

    private void loadFromFile(String fileName) {
        Log.d(TAG, getClass().getSimpleName() + ".loadFromFile(String fileName) START fileName: " + fileName);
        try (FileInputStream fi = context.openFileInput(fileName);
             ObjectInputStream os = new ObjectInputStream(fi)) {
            Form form = (Form) os.readObject();
            form.init(this);

            timeManager = (TimeManager) os.readObject();
            timeManager.init(this, statsChangeListener);

            sceneManager = (SceneManager) os.readObject();
            Log.e(TAG, "loadFromFile(): BEFORE GameConsoleFragment loading.");
            GameConsoleFragment gameConsoleFragment = (GameConsoleFragment) ((MainActivity) context).getSupportFragmentManager().findFragmentByTag(GameConsoleFragment.TAG);
            Log.e(TAG, "loadFromFile(): AFTER GameConsoleFragment loading.");
            if (gameConsoleFragment != null) {
                ComputerDialogFragment computerDialogFragmentBeforeRestart = (ComputerDialogFragment) gameConsoleFragment.getFragmentManager().findFragmentByTag(ComputerDialogFragment.TAG);
                if ((sceneManager.getCurrentScene() instanceof SceneHome02) &&
                        (computerDialogFragmentBeforeRestart != null)) {
                    ((SceneHome02) sceneManager.getCurrentScene()).setComputerDialogFragment(computerDialogFragmentBeforeRestart);
                }
            } else {
                Log.e(TAG, "gameConsoleFragment == null");
            }
            sceneManager.reload(this);
            Player.getInstance().setForm(form);
            currency = os.readFloat();
            statsChangeListener.onCurrencyChange(currency);

//            GameConsoleFragment gameConsoleFragment = (GameConsoleFragment) ((PassingThroughActivity)context).getSupportFragmentManager().findFragmentByTag(GameConsoleFragment.TAG);
//            if (sceneManager.getCurrentScene() instanceof SceneFarm && gameConsoleFragment.isInSeedShopDialogState()) {
//                gameConsoleFragment.setInSeedShopDialogState(false);
//                SceneFarm.getInstance().showSeedShopDialog();
//            }


            backpack = (List<ItemStackable>) os.readObject();
            for (ItemStackable stackable : backpack) {
                stackable.getItem().init(this);
            }
            boolean hasItemInButtonHolderA = os.readBoolean();
            Log.e(TAG, "hasItemInButtonHolderA: " + hasItemInButtonHolderA);
            if (hasItemInButtonHolderA) {
                itemStoredInButtonHolderA = (Item) os.readObject();
                itemStoredInButtonHolderA.init(this);
                statsChangeListener.onButtonHolderAChange(itemStoredInButtonHolderA);
            } else {
                itemStoredInButtonHolderA = null;
                Bitmap imageDefault = BitmapFactory.decodeResource(context.getResources(), StatsDisplayerFragment.IMAGE_DEFAULT);
                statsChangeListener.onButtonHolderAChange(itemStoredInButtonHolderA);
            }
            boolean hasItemInButtonHolderB = os.readBoolean();
            if (hasItemInButtonHolderB) {
                itemStoredInButtonHolderB = (Item) os.readObject();
                itemStoredInButtonHolderB.init(this);
                statsChangeListener.onButtonHolderBChange(
                        itemStoredInButtonHolderB
                );
            } else {
                itemStoredInButtonHolderB = null;
                Bitmap imageDefault = BitmapFactory.decodeResource(context.getResources(), StatsDisplayerFragment.IMAGE_DEFAULT);
                statsChangeListener.onButtonHolderBChange(itemStoredInButtonHolderB);
            }
            int ordinalValueOfButtonHolderCurrentlySelected = os.readInt();
            buttonHolderCurrentlySelected = StatsDisplayerFragment.ButtonHolder.values()[ordinalValueOfButtonHolderCurrentlySelected];
            /////////////////////////////////////////////////////
            refreshBackpackWithoutItemsDisplayingInButtonHolders();
            /////////////////////////////////////////////////////
            if (itemStoredInButtonHolderA instanceof BugCatchingNet ||
                    itemStoredInButtonHolderB instanceof BugCatchingNet) {
                List<Entity> entities = sceneManager.getCurrentScene().getEntityManager().getEntities();
                for (Entity e : entities) {
                    if (e instanceof CollidingOrbit) {
                        ((CollidingOrbit) e).setEntityToOrbit(Player.getInstance());
                        break;
                    }
                }
            } else {
                List<Entity> entities = sceneManager.getCurrentScene().getEntityManager().getEntities();
                boolean doesExistCollidingOrbit = false;
                for (Entity e : entities) {
                    if (e instanceof CollidingOrbit) {
                        doesExistCollidingOrbit = true;
                        break;
                    }
                }
                if (doesExistCollidingOrbit) {
                    sceneManager.getCurrentScene().getEntityManager().removeCollidingOrbit();
                }
            }

            // do AFTER SceneFarm is reloaded.
            List<Item> seedShopInventory = (List<Item>) os.readObject();
            SceneFarm.getInstance().getSeedShopDialogFragment().reload(
                    this, seedShopInventory);

            boolean isBlinkingBorderOn = os.readBoolean();
            if (isBlinkingBorderOn) {
                viewportListener.startBlinkingBorder();
            } else {
                viewportListener.stopBlinkingBorder();
            }

            paused = os.readBoolean();
            if (holder != null) {
                Canvas canvas = holder.lockCanvas();
                sceneManager.getCurrentScene().drawCurrentFrame(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            inBackpackDialogState = os.readBoolean();
            if (inBackpackDialogState) {
                showBackpackDialog();
            }
//            inSeedShopDialogState = os.readBoolean();
            Player.getInstance().init(this);
            sceneManager.getCurrentScene().enter(null);
            Log.e(TAG, "loadFromFile() end.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, getClass().getSimpleName() + ".loadFromFile(String fileName) FINISHED.");
    }

    public void update(long elapsed) {
        if (paused) {
            return;
        }

        stateManager.update(elapsed);
    }

    public void draw() {
        if (holder == null) {
            return;
        }

        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            stateManager.render(canvas);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void doClickButtonHolder(StatsDisplayerFragment.ButtonHolder buttonHolder) {
        buttonHolderCurrentlySelected = buttonHolder;

        refreshBackpackWithoutItemsDisplayingInButtonHolders();

        showBackpackDialog();
    }

    public void doClickIcon(View view, FragmentManager fm) {
        String tagOfIcon = (String) view.getTag();

        if (tagOfIcon.equals(StatsDisplayerFragment.TAG_CURRENCY_ICON)) {
            Log.e(TAG, "doClickIcon(View): " + StatsDisplayerFragment.TAG_CURRENCY_ICON);
        } else if (tagOfIcon.equals(StatsDisplayerFragment.TAG_TIME_ICON)) {
            Log.e(TAG, "doClickIcon(View): " + StatsDisplayerFragment.TAG_TIME_ICON);
        } else if (tagOfIcon.equals(StatsDisplayerFragment.TAG_QUEST_ICON)) {
            Log.e(TAG, "doClickIcon(View): " + StatsDisplayerFragment.TAG_QUEST_ICON);
            // TODO: show QuestDisplayer dialog.
            List<Quest> quests = Player.getInstance().getQuestManager().getQuests();

            if (!quests.isEmpty()) {
                showQuestLogDialog(fm);
            } else {
                Log.e(TAG, "player does NOT have any quests.");
            }
        } else {
            Log.e(TAG, "doClickIcon(View) else-clause");
        }
    }

    private void refreshBackpackWithoutItemsDisplayingInButtonHolders() {
        backpackWithoutItemsDisplayingInButtonHolders.clear();
        backpackWithoutItemsDisplayingInButtonHolders.addAll(backpack);
        if (itemStoredInButtonHolderA != null) {
            for (int i = 0; i < backpackWithoutItemsDisplayingInButtonHolders.size(); i++) {
                ItemStackable stackable = backpackWithoutItemsDisplayingInButtonHolders.get(i);
                if (stackable.getItem().getName().equals(itemStoredInButtonHolderA.getName())) {
                    backpackWithoutItemsDisplayingInButtonHolders.remove(i);
                    break;
                }
            }
        }
        if (itemStoredInButtonHolderB != null) {
            for (int i = 0; i < backpackWithoutItemsDisplayingInButtonHolders.size(); i++) {
                ItemStackable stackable = backpackWithoutItemsDisplayingInButtonHolders.get(i);
                if (stackable.getItem().getName().equals(itemStoredInButtonHolderB.getName())) {
                    backpackWithoutItemsDisplayingInButtonHolders.remove(i);
                    break;
                }
            }
        }
        itemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showBackpackDialog() {
        paused = true;
        inBackpackDialogState = true;
        backpackDialog.show();
    }

    private void showQuestDetailDialog(FragmentManager fm, Quest questSelected) {
        paused = true;

        QuestDetailFragment questDetailFragment = QuestDetailFragment.newInstance(questSelected);

        FCVDialogFragment fcvDialogFragment = FCVDialogFragment.newInstance(questDetailFragment, QuestLogFragment.TAG,
                true, 0.5f, 0.5f,
                new FCVDialogFragment.LifecycleListener() {
                    @Override
                    public void onResume() {
                        // Intentionally blank.
                    }

                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "FCVDialogFragment onDismiss()");
                        paused = false;
                    }
                });

        fcvDialogFragment.show(fm, FCVDialogFragment.TAG);
    }

    private void showQuestLogDialog(FragmentManager fm) {
        paused = true;

        List<Quest> questsPlayer = Player.getInstance().getQuestManager().getQuests();
        QuestLogFragment questLogFragment = QuestLogFragment.newInstance(null, null);
        QuestAdapter questAdapter = new QuestAdapter(questsPlayer,
                new QuestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        Log.e(TAG, "onItemClick() position: " + position);

                        Quest questClicked = questsPlayer.get(position);
                        Log.e(TAG, "questClicked: " + questClicked.getTAG());
                        showQuestDetailDialog(fm, questClicked);
                    }
                }
        );

        FCVDialogFragment fcvDialogFragment = FCVDialogFragment.newInstance(questLogFragment, QuestLogFragment.TAG,
                false, 0.6f, 0.6f,
                new FCVDialogFragment.LifecycleListener() {
                    @Override
                    public void onResume() {
                        Log.e(TAG, "FCVDialogFragment onResume()");
                        questLogFragment.attachAdapter(questAdapter);
                    }

                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "FCVDialogFragment onDismiss()");
                        paused = false;
                    }
                });

        fcvDialogFragment.show(fm, FCVDialogFragment.TAG);
    }

    public Context getContext() {
        return context;
    }

    public int getWidthViewport() {
        return widthViewport;
    }

    public int getHeightViewport() {
        return heightViewport;
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public void setLoadNeeded(boolean loadNeeded) {
        this.loadNeeded = loadNeeded;
    }

    public float getCurrency() {
        return currency;
    }

    public void updateCurrency() {
        statsChangeListener.onCurrencyChange(currency);
    }

    public void incrementCurrency(float amountToIncrement) {
        currency += amountToIncrement;
        ///////////////////////////////////////////////
        statsChangeListener.onCurrencyChange(currency);
        ///////////////////////////////////////////////
    }

    public void decrementCurrencyBy(float amountToDecrement) {
        currency -= amountToDecrement;
        ///////////////////////////////////////////////
        statsChangeListener.onCurrencyChange(currency);
        ///////////////////////////////////////////////
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public TextboxListener getTextboxListener() {
        return textboxListener;
    }

    public Item getItemStoredInButtonHolderA() {
        return itemStoredInButtonHolderA;
    }

    public Item getItemStoredInButtonHolderB() {
        return itemStoredInButtonHolderB;
    }

    public ItemStackable findItemStackableViaItem(Item item) {
        if (item == null) {
            return null;
        }

        for (ItemStackable stackable : backpack) {
            if (stackable.getItem().getName().equals(item.getName())) {
                return stackable;
            }
        }
        return null;
    }

    public Item getScissors() {
        return scissors;
    }
}