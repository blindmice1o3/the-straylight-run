package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterDrinkComponent extends RecyclerView.Adapter<AdapterDrinkComponent.ViewHolderDrinkComponent> {
    public static final String TAG = AdapterDrinkComponent.class.getSimpleName();

    private List<DrinkComponent> drinkComponents;
    private List<String> drinkComponentsPrettyPrint;

    public AdapterDrinkComponent(List<DrinkComponent> drinkComponents) {
        this.drinkComponents = drinkComponents;
        this.drinkComponentsPrettyPrint = convertToPrettyPrint(drinkComponents);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolderDrinkComponent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_drink_component, parent, false);
        return new ViewHolderDrinkComponent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDrinkComponent holder, int position) {
        String drinkComponentAsString = drinkComponentsPrettyPrint.get(position);
        holder.tvDrinkComponent.setText(drinkComponentAsString);
    }

    @Override
    public int getItemCount() {
        return drinkComponentsPrettyPrint.size();
    }

    private List<String> convertToPrettyPrint(List<DrinkComponent> drinkComponents) {
        List<String> drinkComponentsPrettyPrint = new ArrayList<>();

        Map<Syrup, Integer> syrupsMap = new HashMap<>();
        Map<EspressoShot, Integer> espressoShotsMap = new HashMap<>();
        for (DrinkComponent drinkComponent : drinkComponents) {
            if (drinkComponent instanceof Syrup) {
                Syrup syrup = (Syrup) drinkComponent;
                if (syrupsMap.containsKey(syrup)) {
                    Integer counterSyrup = syrupsMap.get(syrup);
                    counterSyrup++;
                    syrupsMap.put(syrup, counterSyrup);
                } else {
                    syrupsMap.put(syrup, 1);
                }
            } else if (drinkComponent instanceof EspressoShot) {
                EspressoShot espressoShot = (EspressoShot) drinkComponent;
                if (espressoShotsMap.containsKey(espressoShot)) {
                    Integer counterEspressoShot = espressoShotsMap.get(espressoShot);
                    counterEspressoShot++;
                    espressoShotsMap.put(espressoShot, counterEspressoShot);
                } else {
                    espressoShotsMap.put(espressoShot, 1);
                }
            }
        }

        Map<Syrup, Boolean> isFirstTimeSyrup = new HashMap<>();
        Map<EspressoShot, Boolean> isFirstTimeEspressoShot = new HashMap<>();
        for (DrinkComponent drinkComponentFirstTime : drinkComponents) {
            if (drinkComponentFirstTime instanceof Syrup) {
                Syrup syrup = (Syrup) drinkComponentFirstTime;

                // first occurrence of this Syrup
                if (!isFirstTimeSyrup.containsKey(syrup)) {
                    isFirstTimeSyrup.put(syrup, false);

                    drinkComponentsPrettyPrint.add(
                            syrupsMap.get(syrup) + " " + syrup.getType().name() + " (shaken:" + syrup.isShaken() + ") (blended:" + syrup.isBlended() + ")"
                    );
                } else {
                    continue;
                }
            } else if (drinkComponentFirstTime instanceof EspressoShot) {
                EspressoShot espressoShot = (EspressoShot) drinkComponentFirstTime;

                // first occurrence of this EspressoShot
                if (!isFirstTimeEspressoShot.containsKey(espressoShot)) {
                    isFirstTimeEspressoShot.put(espressoShot, false);

                    drinkComponentsPrettyPrint.add(
                            espressoShotsMap.get(espressoShot) + " " + espressoShot.getType().name() + "(amountOfWater:" + espressoShot.getAmountOfWater().name() + ") (amountOfBean:" + espressoShot.getAmountOfBean().name() + ") (shaken:" + espressoShot.isShaken() + ") (blended:" + espressoShot.isBlended() + ")"
                    );
                }
            } else {
                drinkComponentsPrettyPrint.add(
                        drinkComponentFirstTime.toString()
                );
            }
        }

        return drinkComponentsPrettyPrint;
    }

//    @Override
//    public int getItemViewType(int position) {
//        DrinkComponent drinkComponent = drinkComponents.get(position).getDrinkComponent();
//        if (drinkComponent instanceof Incrementable) {
//            return VIEW_TYPE_INCREMENTABLE_SELECTION;
//        } else if (drinkComponent instanceof Granular) {
//            return VIEW_TYPE_GRANULAR_SELECTION;
//        } else if (drinkComponent instanceof MixedType) {
//            return VIEW_TYPE_MIXED_TYPE_SELECTION;
//        } else {
//            return VIEW_TYPE_SIMPLE_SELECTION;
//        }
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_INCREMENTABLE_SELECTION) {
//            View viewIncrementable = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_incrementable, parent, false);
//            return new ViewHolderIncrementableSelection(viewIncrementable);
//        } else if (viewType == VIEW_TYPE_GRANULAR_SELECTION) {
//            View viewGranularSelection = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_granular_selection, parent, false);
//            return new ViewHolderGranularSelection(viewGranularSelection);
//        } else if (viewType == VIEW_TYPE_MIXED_TYPE_SELECTION) {
//            View viewMixedTypeSelection = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mixed_type_selection, parent, false);
//            return new ViewHolderMixedTypeSelection(viewMixedTypeSelection);
//        } else {
//            View viewSimpleSelection = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple_selection, parent, false);
//            return new ViewHolderSimpleSelection(viewSimpleSelection);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        DrinkComponentWithDefaultAsString drinkComponentWithDefaultAsString = drinkComponents.get(position);
//        if (holder instanceof ViewHolderIncrementableSelection) {
//            ((ViewHolderIncrementableSelection) holder).bind(drinkComponentWithDefaultAsString);
//        } else if (holder instanceof ViewHolderGranularSelection) {
//            ((ViewHolderGranularSelection) holder).bind(drinkComponentWithDefaultAsString);
//        } else if (holder instanceof ViewHolderMixedTypeSelection) {
//            ((ViewHolderMixedTypeSelection) holder).bind(drinkComponentWithDefaultAsString);
//        } else {
//            ((ViewHolderSimpleSelection) holder).bind(drinkComponentWithDefaultAsString);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return drinkComponents.size();
//    }

    public class ViewHolderDrinkComponent extends RecyclerView.ViewHolder {

        public TextView tvDrinkComponent;

        public ViewHolderDrinkComponent(@NonNull View itemView) {
            super(itemView);

            tvDrinkComponent = itemView.findViewById(R.id.tv_drink_component);
        }
    }
}
