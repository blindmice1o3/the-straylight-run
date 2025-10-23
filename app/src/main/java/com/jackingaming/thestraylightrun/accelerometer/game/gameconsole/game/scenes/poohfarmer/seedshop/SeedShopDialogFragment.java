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
import com.jackingaming.thestraylightrun.accelerometer.game.quests.seed_shop_dialog_fragment.SeedShopOwnerQuest00;

import java.util.ArrayList;
import java.util.List;

public class SeedShopDialogFragment extends DialogFragment {
    public static final String TAG = "SeedShopDialogFragment";

    private Game game;
    private Bitmap seedShopBackgroundTop;
    private Bitmap seedShopBackgroundBottom;
    private ItemRecyclerViewAdapterSeedShop itemRecyclerViewAdapterSeedShop;
    private List<Item> seedShopInventory;
    private Quest seedShopOwnerQuest00;

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


        Bitmap seedShopSpriteSheet = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gbc_hm_seeds_shop);
        seedShopBackgroundTop = Bitmap.createBitmap(seedShopSpriteSheet, 31, 14, 160, 80);
        seedShopBackgroundBottom = Bitmap.createBitmap(seedShopSpriteSheet, 31, 102, 160, 16);

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

        boolean alreadyHaveQuest = Player.getInstance().alreadyHaveQuest(seedShopOwnerQuest00.getQuestLabel());
        TypeWriterTextView.TextCompletionListener textCompletionListener;
        if (alreadyHaveQuest) {
            textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                @Override
                public void onAnimationFinish() {
                    Log.e(TAG, "alreadyHaveQuest");
                    // TODO:
                }
            };
        } else {
            textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
                @Override
                public void onAnimationFinish() {
                    Log.e(TAG, "!alreadyHaveQuest");

                    boolean wasQuestAccepted =
                            Player.getInstance().getQuestManager().addQuest(
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

        Bitmap image = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.ic_coins_l);
        // TODO: switch to runOne.getDialogueForCurrentState().
        String messageGreeting = seedShopOwnerQuest00.getDialogueForCurrentState();

        TypeWriterDialogFragment typeWriterDialogFragment = TypeWriterDialogFragment.newInstance(
                100L, image, messageGreeting,
                new TypeWriterDialogFragment.DismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "onDismiss(): seed_shop_dialogue00");

                    }
                }, textCompletionListener
        );

        game.getTextboxListener().showTextbox(
                typeWriterDialogFragment
        );
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
}