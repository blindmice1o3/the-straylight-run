package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.seedshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.List;

public class ItemRecyclerViewAdapterSeedShop extends RecyclerView.Adapter<ItemRecyclerViewAdapterSeedShop.ItemViewHolder> {
    public static final String TAG = ItemRecyclerViewAdapterSeedShop.class.getSimpleName();

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ItemRecyclerViewAdapterSeedShop.ItemClickListener itemClickListener;

    public void setClickListener(ItemRecyclerViewAdapterSeedShop.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private Context context;
    private List<Item> inventory;

    public ItemRecyclerViewAdapterSeedShop(Context context, List<Item> inventory) {
        this.context = context;
        this.inventory = inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapterSeedShop.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_seed_shop, parent, false);

        ItemRecyclerViewAdapterSeedShop.ItemViewHolder itemViewHolder = new ItemRecyclerViewAdapterSeedShop.ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapterSeedShop.ItemViewHolder holder, int position) {
        Bitmap image = inventory.get(position).getImage();
        if (image != null) {
            Log.d(TAG, getClass().getSimpleName() + " image of item from inventory is not null");
            holder.imageView.setImageBitmap(image);
        }

        String name = inventory.get(position).getName();
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;

        public ItemViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageview_recyclerview_item_seed_shop);
            textView = (TextView) view.findViewById(R.id.textview_recyclerview_item_seed_shop);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_color_by_state));
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}