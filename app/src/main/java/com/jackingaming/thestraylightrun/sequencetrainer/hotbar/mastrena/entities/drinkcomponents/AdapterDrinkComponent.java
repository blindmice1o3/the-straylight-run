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
            if (drinkComponentFirstTime instanceof Ice) {
                Ice ice = (Ice) drinkComponentFirstTime;
                drinkComponentsPrettyPrint.add(ice.toString());
            } else if (drinkComponentFirstTime instanceof Cinnamon) {
                Cinnamon cinnamon = (Cinnamon) drinkComponentFirstTime;
                drinkComponentsPrettyPrint.add(cinnamon.toString());
            } else if (drinkComponentFirstTime instanceof Milk) {
                Milk milk = (Milk) drinkComponentFirstTime;
                drinkComponentsPrettyPrint.add(milk.toString());
            } else if (drinkComponentFirstTime instanceof Syrup) {
                Syrup syrup = (Syrup) drinkComponentFirstTime;

                // first occurrence of this Syrup
                if (!isFirstTimeSyrup.containsKey(syrup)) {
                    isFirstTimeSyrup.put(syrup, false);

                    int numberOfPumps = syrupsMap.get(syrup);
                    String textUnit = (numberOfPumps == 1) ? "pump" : "pumps";
                    String syrupPrettyPrint = numberOfPumps + " " + textUnit +
                            " " + syrup.toString();

                    drinkComponentsPrettyPrint.add(syrupPrettyPrint);
                } else {
                    continue;
                }
            } else if (drinkComponentFirstTime instanceof EspressoShot) {
                EspressoShot espressoShot = (EspressoShot) drinkComponentFirstTime;

                // first occurrence of this EspressoShot
                if (!isFirstTimeEspressoShot.containsKey(espressoShot)) {
                    isFirstTimeEspressoShot.put(espressoShot, false);

                    int numberOfShots = espressoShotsMap.get(espressoShot);
                    String textUnit = (numberOfShots == 1) ? "shot" : "shots";
                    String espressoShotPrettyPrint = numberOfShots + " " + textUnit +
                            " " + espressoShot.toString();

                    drinkComponentsPrettyPrint.add(espressoShotPrettyPrint);
                }
            } else {
                drinkComponentsPrettyPrint.add(
                        drinkComponentFirstTime.toString()
                );
            }
        }

        return drinkComponentsPrettyPrint;
    }

    public class ViewHolderDrinkComponent extends RecyclerView.ViewHolder {

        public TextView tvDrinkComponent;

        public ViewHolderDrinkComponent(@NonNull View itemView) {
            super(itemView);

            tvDrinkComponent = itemView.findViewById(R.id.tv_drink_component);
        }
    }
}
