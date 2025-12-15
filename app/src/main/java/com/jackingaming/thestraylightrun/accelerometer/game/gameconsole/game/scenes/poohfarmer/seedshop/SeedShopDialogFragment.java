package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.seedshop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.EditTextDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.BounceEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.scene_farm.RunOne;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.seed_shop_dialog_fragment.SeedShopOwnerQuest00;

import java.util.ArrayList;
import java.util.List;

public class SeedShopDialogFragment extends DialogFragment {
    public static final String TAG = "SeedShopDialogFragment";

    public interface SeedListener {
        void onAssignedNameAndDescription();
    }

    public void setSeedListener(SeedListener seedListener) {
        this.seedListener = seedListener;
    }

    private Game game;
    private Bitmap seedShopBackgroundTop;
    private Bitmap seedShopBackgroundBottom;
    private ItemRecyclerViewAdapterSeedShop itemRecyclerViewAdapterSeedShop;
    private List<Item> seedShopInventory;
    private Quest seedShopOwnerQuest00;
    private RunOne runOne;
    private SeedListener seedListener;

    public void reload(Game game, List<Item> seedShopInventory) {
        this.game = game;

        this.seedShopInventory.clear();
        this.seedShopInventory.addAll(seedShopInventory);
        for (Item item : seedShopInventory) {
            item.init(game);
        }
    }

    public SeedShopDialogFragment() {
        seedShopInventory = new ArrayList<Item>();
        seedShopInventory.add(new BugCatchingNet(
                new BounceEntityCommand(null)
        ));
        seedShopInventory.add(new HoneyPot());
        seedShopInventory.add(new BugCatchingNet(
                new BounceEntityCommand(null)
        ));
        seedShopInventory.add(new BugCatchingNet(
                new BounceEntityCommand(null)
        ));
        seedShopInventory.add(new BugCatchingNet(
                new BounceEntityCommand(null)
        ));
        seedShopInventory.add(new HoneyPot());
        seedShopInventory.add(new BugCatchingNet(
                new BounceEntityCommand(null)
        ));
    }

    public void init(Game game) {
        Log.e(TAG, "init()");
        this.game = game;

        runOne = new RunOne(game);

        for (Item item : seedShopInventory) {
            item.init(game);
        }

        String[] dialogueArray = game.getContext().getResources().getStringArray(R.array.seed_shop_dialogue_array);
        seedShopOwnerQuest00 = new SeedShopOwnerQuest00(game, dialogueArray);
    }

    private void performTrade(Item itemToTrade, Player player) {
        float priceOfItemToTrade = itemToTrade.getPrice();
        if (priceOfItemToTrade > 0) {
            if (player.canAffordToBuy(priceOfItemToTrade)) {
                player.buyItem(itemToTrade);
                seedShopInventory.remove(itemToTrade);
                itemRecyclerViewAdapterSeedShop.notifyDataSetChanged();
            } else {
                Toast.makeText(game.getContext(), getClass().getSimpleName() + ".performTrade(Item, Player) player can NOT afford to buy [" + itemToTrade.getName() + "] for [" + priceOfItemToTrade + "].", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, getClass().getSimpleName() + ".performTrade(Item, Player) itemToTrade has NEGATIVE price.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemRecyclerViewAdapterSeedShop = new ItemRecyclerViewAdapterSeedShop(getContext(), seedShopInventory);
        ItemRecyclerViewAdapterSeedShop.ItemClickListener itemClickListener = new ItemRecyclerViewAdapterSeedShop.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "ItemRecyclerViewAdapterSeedShop.ItemClickListener.onItemClick(View view, int position): " + seedShopInventory.get(position));
                // TODO: buy/sell transactions.
                Item itemToTrade = seedShopInventory.get(position);
                performTrade(itemToTrade, Player.getInstance());
            }
        };
        itemRecyclerViewAdapterSeedShop.setClickListener(itemClickListener);

        View viewContainingRecyclerView = inflater.inflate(R.layout.dialog_seed_shop, null);

        RecyclerView recyclerView = (RecyclerView) viewContainingRecyclerView.findViewById(R.id.recyclerview_seed_shop_inventory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(itemRecyclerViewAdapterSeedShop);
        int numberOfRows = 1;
        GridLayoutManager gridLayoutManagerHorizontal =
                new GridLayoutManager(getContext(), numberOfRows, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManagerHorizontal);

        Bitmap seedShopSpriteSheet = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.scene_seed_shop);
        seedShopBackgroundTop = Bitmap.createBitmap(seedShopSpriteSheet, 0, 0, 1253, 589);
        seedShopBackgroundBottom = Bitmap.createBitmap(seedShopSpriteSheet, 0, 767, 1253, 141);
//        Bitmap seedShopSpriteSheet = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gbc_hm_seeds_shop);
//        seedShopBackgroundTop = Bitmap.createBitmap(seedShopSpriteSheet, 31, 14, 160, 80);
//        seedShopBackgroundBottom = Bitmap.createBitmap(seedShopSpriteSheet, 31, 102, 160, 16);

        ImageView imageViewBackgroundTop = (ImageView) viewContainingRecyclerView.findViewById(R.id.imageview_seed_shop_background_top);
        imageViewBackgroundTop.setImageBitmap(seedShopBackgroundTop);
        ImageView imageViewBackgroundBottom = (ImageView) viewContainingRecyclerView.findViewById(R.id.imageview_seed_shop_background_bottom);
        imageViewBackgroundBottom.setImageBitmap(seedShopBackgroundBottom);

        return viewContainingRecyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout clBaseLayout = view.findViewById(R.id.cl_base_layout);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) clBaseLayout.getLayoutParams();
        params.setMargins(8, 8, 8, 8);
        clBaseLayout.setLayoutParams(params);
    }

    public void showTextboxAboutRobotReprogrammer4000QuestStatus() {
        Bitmap portrait = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.dialogue_image_seed_shop_owner);
        String textToShow = null;
        if (SceneFarm.getInstance().isFirstTimeEquippingRobotReprogrammer4000()) {
            textToShow = game.getContext().getString(R.string.click_button_holder_a_or_b);
        } else if (SceneFarm.getInstance().getRobot().isFirstTimeShowingRobotDialogFragment()) {
            textToShow = game.getContext().getString(R.string.equip_robot_reprogrammer_4000);
        } else {
            textToShow = game.getContext().getString(R.string.use_robot_reprogrammer_4000);
        }
        TypeWriterDialogFragment.DismissListener dismissListener1 = new TypeWriterDialogFragment.DismissListener() {
            @Override
            public void onDismiss() {
                Log.e(TAG, "onStart() TypeWriterDialogFragment.DismissListener.onDismiss()");
            }
        };
        TypeWriterTextView.TextCompletionListener textCompletionListener1 = new TypeWriterTextView.TextCompletionListener() {
            @Override
            public void onAnimationFinish() {
                Log.e(TAG, "onStart() TypeWriterTextView.TextCompletionListener.onAnimationFinish()");

                game.getStateManager().getTextboxState().setTextboxAnimationFinished(true);
            }
        };

        game.getStateManager().pushTextboxState(portrait,
                textToShow,
                dismissListener1,
                textCompletionListener1);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Set up to push TextboxState based on Run Number.
        Bitmap image = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.dialogue_image_seed_shop_owner);
        String messageGreeting = null;
        TypeWriterTextView.TextCompletionListener textCompletionListener = null;

        switch (game.getRun()) {
            case ONE:
                messageGreeting = runOne.getDialogueForCurrentState();
                textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                    @Override
                    public void onAnimationFinish() {
                        Log.d(TAG, "onStart() textCompletionListener.onAnimationFinish()");

                        game.getStateManager().getTextboxState().setTextboxAnimationFinished(true);

                        boolean wasQuestAcceptedRunOne =
                                Player.getInstance().getQuestManager().addQuest(
                                        runOne
                                );

                        if (wasQuestAcceptedRunOne) {
                            Log.e(TAG, "wasQuestAcceptedRunOne");
                            runOne.dispenseStartingItems();
                        } else {
                            Log.e(TAG, "!wasQuestAcceptedRunOne");
                        }

                        if (runOne.getCurrentState() == Quest.State.STARTED) {
                            Log.e(TAG, "runOne.getCurrentState() == Quest.State.STARTED");

                            String textSeedName = game.getContext().getResources().getString(R.string.text_seed_name);
                            String textSeedDescription = game.getContext().getResources().getString(R.string.text_seed_description);
                            EditTextDialogFragment dialogFragmentRunOneName = EditTextDialogFragment.newInstance(
                                    new EditTextDialogFragment.EnterListener() {
                                        @Override
                                        public void onDismiss() {
                                            Log.e(TAG, "onDismiss()");
                                        }

                                        @Override
                                        public void onEnterKeyPressed(String name) {
                                            Log.e(TAG, "onEnterKeyPressed()");

                                            runOne.setSeedName(name);

                                            EditTextDialogFragment dialogFragmentRunOneDescription = EditTextDialogFragment.newInstance(
                                                    new EditTextDialogFragment.EnterListener() {
                                                        @Override
                                                        public void onDismiss() {
                                                            Log.e(TAG, "onDismiss()");
                                                        }

                                                        @Override
                                                        public void onEnterKeyPressed(String name) {
                                                            Log.e(TAG, "onEnterKeyPressed()");

                                                            runOne.setSeedDescription(name);

                                                            seedListener.onAssignedNameAndDescription();
                                                        }
                                                    },
                                                    textSeedDescription,
                                                    false
                                            );
                                            dialogFragmentRunOneDescription.show(game.getFragmentManager(), TAG);
                                        }
                                    },
                                    textSeedName,
                                    false
                            );
                            dialogFragmentRunOneName.show(game.getFragmentManager(), TAG);
                        } else {
                            Log.e(TAG, "runOne.getCurrentState() != Quest.State.STARTED");

                            showTextboxAboutRobotReprogrammer4000QuestStatus();
                        }
                    }
                };
                break;
            case TWO:
            case THREE:
            case FOUR:
                messageGreeting = getString(R.string.text_message_greeting_default);
                textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                    @Override
                    public void onAnimationFinish() {
                        Log.d(TAG, "onStart() textCompletionListener.onAnimationFinish()");

                        game.getStateManager().getTextboxState().setTextboxAnimationFinished(true);
                    }
                };
                break;
            case FIVE:
                // unlocked version
                messageGreeting = seedShopOwnerQuest00.getDialogueForCurrentState();

                boolean alreadyHaveQuest = Player.getInstance().alreadyHaveQuest(seedShopOwnerQuest00.getQuestLabel());
                if (alreadyHaveQuest) {
                    Log.d(TAG, "alreadyHaveQuest");

                    textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                        @Override
                        public void onAnimationFinish() {
                            Log.d(TAG, "onStart() textCompletionListener.onAnimationFinish()");

                            game.getStateManager().getTextboxState().setTextboxAnimationFinished(true);
                        }
                    };
                } else {
                    Log.d(TAG, "!alreadyHaveQuest");

                    textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                        @Override
                        public void onAnimationFinish() {
                            Log.d(TAG, "onStart() textCompletionListener.onAnimationFinish()");

                            game.getStateManager().getTextboxState().setTextboxAnimationFinished(true);

                            boolean wasQuestAccepted = Player.getInstance().getQuestManager().addQuest(
                                    seedShopOwnerQuest00
                            );

                            if (wasQuestAccepted) {
                                Log.e(TAG, "wasQuestAccepted");
                                seedShopOwnerQuest00.dispenseStartingItems();
                            } else {
                                Log.e(TAG, "!wasQuestAccepted");
                            }
                        }
                    };
                }

                break;
        }

        //////////////////////////////////////////////
        game.getStateManager().pushTextboxState(image,
                messageGreeting,
                null,
                textCompletionListener);
        //////////////////////////////////////////////
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, getClass().getSimpleName() + ".onPause() SceneFarm's needLaunchSeedShopDialog: " + SceneFarm.getInstance().isNeedDisplaySeedShopFragment());
        if (SceneFarm.getInstance().isInSeedShopState()) {
            SceneFarm.getInstance().setNeedDisplaySeedShopFragment(true);
            dismiss();
        }
        Log.d(TAG, getClass().getSimpleName() + ".onPause() SceneFarm's needLaunchSeedShopDialog: " + SceneFarm.getInstance().isNeedDisplaySeedShopFragment());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Log.d(TAG, getClass().getSimpleName() + ".onDismiss(DialogInterface) SceneFarm's inSeedShopDialogState: " + SceneFarm.getInstance().isInSeedShopState());
        SceneFarm.getInstance().setInSeedShopState(false);
        Log.d(TAG, getClass().getSimpleName() + ".onDismiss(DialogInterface) SceneFarm's inSeedShopDialogState: " + SceneFarm.getInstance().isInSeedShopState());
        game.setPaused(false);
        super.onDismiss(dialog);
    }

    public List<Item> getSeedShopInventory() {
        return seedShopInventory;
    }

    public RunOne getRunOne() {
        return runOne;
    }
}