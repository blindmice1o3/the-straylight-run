package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.GameConsoleFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.adapters.ItemRecyclerViewAdapter;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.SceneManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.BounceEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TillGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.WaterGrowableTileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Form;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Shovel;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.WateringCan;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.computer.ComputerDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.StateManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time.TimeManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer.StatsDisplayerFragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Game {
    public static final String TAG = Game.class.getSimpleName();

    public interface ItemInButtonHolderListener {
        void onChangeItemInButtonHolder(Item itemButtonHolderA, Item itemButtonHolderB);
    }

    private ItemInButtonHolderListener itemInButtonHolderListener;

    public void setItemInButtonHolderListener(ItemInButtonHolderListener itemInButtonHolderListener) {
        this.itemInButtonHolderListener = itemInButtonHolderListener;
    }

    public interface ViewportListener {
        void showFragmentAndHideSurfaceView(Fragment fragmentReplacingSurfaceView);

        void showSurfaceView();

        void startBlinkingBorder();

        void stopBlinkingBorder();
    }

    private ViewportListener viewportListener;

    public ViewportListener getViewportListener() {
        return viewportListener;
    }

    public void setViewportListener(ViewportListener viewportListener) {
        this.viewportListener = viewportListener;
    }

    public interface TextboxListener {
        void showTextbox(TypeWriterDialogFragment typeWriterDialogFragment);

        void showStatsDisplayer();
    }

    private TextboxListener textboxListener;

    public void setTextboxListener(TextboxListener textboxListener) {
        this.textboxListener = textboxListener;
    }

    public interface StatsChangeListener {
        void onCurrencyChange(float currency);

        void onTimeChange(String inGameClockTime, String calendarText);

        void onButtonHolderAChange(Bitmap image);

        void onButtonHolderBChange(Bitmap image);
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

    private TimeManager timeManager;
    private SceneManager sceneManager;
    private StateManager stateManager;

    /**
     * Displayed in StatsDisplayerFragment through Game.StatsChangeListener.onCurrencyChange(int currency).
     * Triggered via Player.respondToItemCollisionViaMove(Item item) when item is instanceof HoneyPot.
     */
    private float currency;

    private List<Item> backpack;
    private List<Item> backpackWithoutItemsDisplayingInButtonHolders;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private Dialog backpackDialog;

    private Item itemStoredInButtonHolderA;
    private Item itemStoredInButtonHolderB;
    private StatsDisplayerFragment.ButtonHolder buttonHolderCurrentlySelected;

    private boolean paused;
    private boolean inBackpackDialogState;

    public Game(String gameTitle) {
        loadNeeded = false;
        this.gameTitle = gameTitle;

        timeManager = new TimeManager();
        sceneManager = new SceneManager(gameTitle);
        stateManager = new StateManager();

        currency = 100f;

        backpack = new ArrayList<Item>();
        backpack.add(
                new Shovel(
                        new TillGrowableTileCommand(null)
                )
        );
        backpack.add(
                new WateringCan(
                        new WaterGrowableTileCommand(null)
                )
        );
        backpack.add(
                new BugCatchingNet(
                        new BounceEntityCommand(null)
                )
        );
        backpackWithoutItemsDisplayingInButtonHolders = new ArrayList<Item>();

        itemStoredInButtonHolderA = null;
        itemStoredInButtonHolderB = null;
        buttonHolderCurrentlySelected = StatsDisplayerFragment.ButtonHolder.A;

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
//        SceneChickenCoop.getInstance().startNewDay();
//        SceneCowBarn.getInstance().startNewDay();
//        SceneSheepPen.getInstance().startNewDay();
//        SceneHothouse.getInstance().startNewDay();
    }

    public void addItemToBackpack(Item item) {
        backpack.add(item);
    }

    public void init(Context context, InputManager inputManager, SurfaceHolder holder, int widthViewport, int heightViewport) {
        this.context = context;
        this.inputManager = inputManager;
        this.holder = holder;
        this.widthViewport = widthViewport;
        this.heightViewport = heightViewport;

        timeManager.init(this, statsChangeListener);
        timeManager.registerTimeManagerListener(new TimeManager.TimeManagerListener() {
            @Override
            public void executeTimedEvent() {
                ShippingBinTile.sellStash();
            }
        }, 5, 0, true);
        sceneManager.init(this);
        stateManager.init(this);

        for (Item item : backpack) {
            item.init(this);
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

                Item item = backpackWithoutItemsDisplayingInButtonHolders.get(position);
                Log.d(TAG, "Game.createBackpackDialog() item: " + item.getName());
                switch (buttonHolderCurrentlySelected) {
                    case A:
                        Log.d(TAG, "Game.createBackpackDialog() itemClickListener.onItemClick() case A!!!");
                        itemStoredInButtonHolderA = item;
                        statsChangeListener.onButtonHolderAChange(item.getImage());
                        backpackDialog.dismiss();
                        break;
                    case B:
                        Log.d(TAG, "Game.createBackpackDialog() itemClickListener.onItemClick() case B!!!");
                        itemStoredInButtonHolderB = item;
                        statsChangeListener.onButtonHolderBChange(item.getImage());
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
        Log.d(TAG, getClass().getSimpleName() + ".saveToFile(String fileName) START fileName: " + fileName);
        try (FileOutputStream fs = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            // MUST save form before exit() (otherwise it'll load formBeforeThisScene).
            os.writeObject(Player.getInstance().getForm());
            // Record player's xLastKnown and yLastKnown for the current scene.
            sceneManager.getCurrentScene().exit();

            os.writeObject(timeManager);
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

//            os.writeObject(seedShopInventory);

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
            GameConsoleFragment gameConsoleFragment = (GameConsoleFragment) ((MainActivity) context).getSupportFragmentManager().findFragmentByTag(GameConsoleFragment.TAG);
            ComputerDialogFragment computerDialogFragmentBeforeRestart = (ComputerDialogFragment) gameConsoleFragment.getFragmentManager().findFragmentByTag(ComputerDialogFragment.TAG);
            if ((sceneManager.getCurrentScene() instanceof SceneHome02) &&
                    (computerDialogFragmentBeforeRestart != null)) {
                ((SceneHome02) sceneManager.getCurrentScene()).setComputerDialogFragment(computerDialogFragmentBeforeRestart);
            }
            sceneManager.init(this);
            Player.getInstance().setForm(form);
            currency = os.readFloat();
            statsChangeListener.onCurrencyChange(currency);

//            GameConsoleFragment gameConsoleFragment = (GameConsoleFragment) ((PassingThroughActivity)context).getSupportFragmentManager().findFragmentByTag(GameConsoleFragment.TAG);
//            if (sceneManager.getCurrentScene() instanceof SceneFarm && gameConsoleFragment.isInSeedShopDialogState()) {
//                gameConsoleFragment.setInSeedShopDialogState(false);
//                SceneFarm.getInstance().showSeedShopDialog();
//            }


            backpack = (List<Item>) os.readObject();
            for (Item item : backpack) {
                item.init(this);
            }
            boolean hasItemInButtonHolderA = os.readBoolean();
            if (hasItemInButtonHolderA) {
                itemStoredInButtonHolderA = (Item) os.readObject();
                itemStoredInButtonHolderA.init(this);
                statsChangeListener.onButtonHolderAChange(itemStoredInButtonHolderA.getImage());
            }
            boolean hasItemInButtonHolderB = os.readBoolean();
            if (hasItemInButtonHolderB) {
                itemStoredInButtonHolderB = (Item) os.readObject();
                itemStoredInButtonHolderB.init(this);
                statsChangeListener.onButtonHolderBChange(itemStoredInButtonHolderB.getImage());
            }
            int ordinalValueOfButtonHolderCurrentlySelected = os.readInt();
            Log.d(TAG, getClass().getSimpleName() + ".loadFromFile(String fileName) ordinalValueOfButtonHolderCurrenlySelected: " + ordinalValueOfButtonHolderCurrentlySelected);
            buttonHolderCurrentlySelected = StatsDisplayerFragment.ButtonHolder.values()[ordinalValueOfButtonHolderCurrentlySelected];
            /////////////////////////////////////////////////////
            refreshBackpackWithoutItemsDisplayingInButtonHolders();
            /////////////////////////////////////////////////////

//            seedShopInventory = (List<Item>) os.readObject();
//            for (Item item : seedShopInventory) {
//                item.init(this);
//            }

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

    private void refreshBackpackWithoutItemsDisplayingInButtonHolders() {
        backpackWithoutItemsDisplayingInButtonHolders.clear();
        backpackWithoutItemsDisplayingInButtonHolders.addAll(backpack);
        if (itemStoredInButtonHolderA != null) {
            backpackWithoutItemsDisplayingInButtonHolders.remove(itemStoredInButtonHolderA);
        }
        if (itemStoredInButtonHolderB != null) {
            backpackWithoutItemsDisplayingInButtonHolders.remove(itemStoredInButtonHolderB);
        }
        itemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showBackpackDialog() {
        paused = true;
        inBackpackDialogState = true;
        backpackDialog.show();
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
}