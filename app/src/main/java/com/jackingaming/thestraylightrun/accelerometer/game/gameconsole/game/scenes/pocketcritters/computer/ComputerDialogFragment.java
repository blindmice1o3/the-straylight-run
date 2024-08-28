package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.computer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.BounceEntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.seedshop.ItemRecyclerViewAdapterSeedShop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComputerDialogFragment extends DialogFragment
        implements Serializable {
    public static final String TAG = ComputerDialogFragment.class.getSimpleName();

    private static final String INVENTORY_SIZE = "seedShopInventorySize";
    private static final String INVENTORY = "seedShopInventory";
    transient private Game game;
    transient private Bitmap seedShopBackgroundTop;
    transient private Bitmap seedShopBackgroundBottom;
    private List<Item> seedShopInventory;
    transient private ItemRecyclerViewAdapterSeedShop itemRecyclerViewAdapterSeedShop;

    public ComputerDialogFragment() {
        Log.d(TAG, getClass().getSimpleName() + "() constructor");
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
        Log.d(TAG, getClass().getSimpleName() + ".init(Game) " + game);
        this.game = game;

        for (Item item : seedShopInventory) {
            item.init(game);
        }
    }

    private void performTrade(Item itemToTrade, Player player) {
        Log.d(TAG, getClass().getSimpleName() + ".performTrade(Item, Player)");
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, getClass().getSimpleName() + ".onSaveInstanceState(Bundle)");
        outState.putInt(INVENTORY_SIZE, seedShopInventory.size());
        for (int i = 0; i < seedShopInventory.size(); i++) {
            Item item = seedShopInventory.get(i);
            String key = INVENTORY + i;
            outState.putSerializable(key, item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Log.d(TAG, getClass().getSimpleName() + ".onCreate(Bundle) savedInstanceState is null");
            // Intentionally blank.
        } else {
            Log.d(TAG, getClass().getSimpleName() + ".onCreate(Bundle) savedInstanceState is NOT null");
            seedShopInventory.clear();
            int inventorySize = savedInstanceState.getInt(INVENTORY_SIZE);
            for (int i = 0; i < inventorySize; i++) {
                String key = INVENTORY + i;
                Item item = (Item) savedInstanceState.getSerializable(key);
                seedShopInventory.add(item);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, getClass().getSimpleName() + ".onCreateView(LayoutInflater, ViewGroup, Bundle)");
        final Context contextFinal = getContext();
        itemRecyclerViewAdapterSeedShop = new ItemRecyclerViewAdapterSeedShop(getContext(), seedShopInventory);
        ItemRecyclerViewAdapterSeedShop.ItemClickListener itemClickListener = new ItemRecyclerViewAdapterSeedShop.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "ComputerDialogFragment ItemRecyclerViewAdapterSeedShop.ItemClickListener.onItemClick(View view, int position): " + seedShopInventory.get(position));
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
    public void onStart() {
        super.onStart();
        Log.d(TAG, getClass().getSimpleName() + ".onStart()");
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Log.d(TAG, getClass().getSimpleName() + ".onDismiss(DialogInterface)");
        if (game == null) {
            Log.d(TAG, getClass().getSimpleName() + ".onDismiss(DialogInterface) game is null");
        } else {
            Log.d(TAG, getClass().getSimpleName() + ".onDismiss(DialogInterface) game is NOT null");
        }
        game.setPaused(false);
        super.onDismiss(dialog);
    }
}