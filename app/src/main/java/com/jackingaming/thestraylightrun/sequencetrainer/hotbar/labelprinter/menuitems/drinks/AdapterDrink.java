package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.util.Collections;
import java.util.List;

public class AdapterDrink extends RecyclerView.Adapter<AdapterDrink.ViewHolderDrink>
        implements ItemTouchHelperAdapter {
    public static final String TAG = AdapterDrink.class.getSimpleName();

    public interface UpdateListener {
        void updateDisplay();
    }

    private UpdateListener listener;

    private List<Drink> drinks;

    public AdapterDrink(List<Drink> drinks, UpdateListener listener) {
        this.drinks = drinks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderDrink onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_drink, parent, false);
        return new ViewHolderDrink(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDrink holder, int position) {
        Drink drink = drinks.get(position);

        holder.tvSize.setText(
                drink.getSize().name()
        );
        holder.tvName.setText(
                drink.getName()
        );

        String[] textDrinkLabel = drink.getTextForDrinkLabel().split("\\s+");
        boolean isStandardRecipe = textDrinkLabel.length == 6;
        String textStandardRecipe = (isStandardRecipe) ? "standard" : "customized";
        holder.tvStandardRecipe.setText(
                textStandardRecipe
        );
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(drinks, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(drinks, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        listener.updateDisplay();
    }

    @Override
    public void onItemDismiss(int position) {
        drinks.remove(position);
        notifyItemRemoved(position);
        listener.updateDisplay();
    }


    public class ViewHolderDrink extends RecyclerView.ViewHolder {

        public TextView tvSize, tvName, tvStandardRecipe;

        public ViewHolderDrink(@NonNull View itemView) {
            super(itemView);

            tvSize = itemView.findViewById(R.id.tv_size);
            tvName = itemView.findViewById(R.id.tv_name);
            tvStandardRecipe = itemView.findViewById(R.id.tv_standard_recipe);
        }
    }
}
