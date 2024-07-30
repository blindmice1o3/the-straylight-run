package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.adapters;

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

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {
    public static final String TAG = ItemRecyclerViewAdapter.class.getSimpleName();

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ItemClickListener itemClickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private Context context;
    private List<Item> backpack;

    public ItemRecyclerViewAdapter(Context context, List<Item> backpack) {
        this.context = context;
        this.backpack = backpack;
    }

    public void setBackpack(List<Item> backpack) {
        this.backpack = backpack;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recyclerview_item_in_backpack, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Bitmap image = backpack.get(position).getImage();
        if (image != null) {
            Log.d(TAG, getClass().getSimpleName() + " image of item from backpack is not null");
            holder.imageView.setImageBitmap(image);
        }

        String name = backpack.get(position).getName();
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return backpack.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;

        public ItemViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageview_adapter_recyclerview_item_in_backpack);
            textView = (TextView) view.findViewById(R.id.textview_adapter_recyclerview_item_in_backpack);
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