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

                boolean isShaken = ice.isShaken();
                boolean isBlended = ice.isBlended();
                String icePrettyPrint = "ice \n(shaken:" + isShaken + ") " +
                        "(blended:" + isBlended + ")";

                drinkComponentsPrettyPrint.add(icePrettyPrint);
            } else if (drinkComponentFirstTime instanceof Cinnamon) {
                Cinnamon cinnamon = (Cinnamon) drinkComponentFirstTime;

                boolean isShaken = cinnamon.isShaken();
                boolean isBlended = cinnamon.isBlended();
                String cinnamonPrettyPrint = "cinnamon \n(shaken:" + isShaken + ") " +
                        "(blended:" + isBlended + ")";

                drinkComponentsPrettyPrint.add(cinnamonPrettyPrint);
            } else if (drinkComponentFirstTime instanceof Milk) {
                Milk milk = (Milk) drinkComponentFirstTime;

                Milk.Type type = milk.getType();
                int amount = milk.getAmount();
                int temperature = milk.getTemperature();
                int froth = milk.getTimeFrothed();

                String textType = null;
                switch (type) {
                    case TWO_PERCENT:
                        textType = "2%";
                        break;
                    case WHOLE:
                        textType = "WHOLE";
                        break;
                    case OAT:
                        textType = "OAT";
                        break;
                    case COCONUT:
                        textType = "COCONUT";
                        break;
                    case ALMOND:
                        textType = "ALMOND";
                        break;
                    case SOY:
                        textType = "SOY";
                        break;
                }
                String textClass = "milk";
                String textAmount = Integer.toString(amount);
                String textTemperature = Integer.toString(temperature);
                String textFroth = Integer.toString(froth);

                String milkPrettyPrint = textType + " " +
                        textClass + " (amount:" +
                        textAmount + ") \n(temperature:" +
                        textTemperature + ") (froth:" +
                        textFroth + ")";

                drinkComponentsPrettyPrint.add(milkPrettyPrint);
            } else if (drinkComponentFirstTime instanceof Syrup) {
                Syrup syrup = (Syrup) drinkComponentFirstTime;

                // first occurrence of this Syrup
                if (!isFirstTimeSyrup.containsKey(syrup)) {
                    isFirstTimeSyrup.put(syrup, false);

                    int numberOfPumps = syrupsMap.get(syrup);
                    Syrup.Type type = syrup.getType();
                    boolean isShaken = syrup.isShaken();
                    boolean isBlended = syrup.isBlended();

                    String textNumberOfPumps = Integer.toString(numberOfPumps);
                    String textClass = (numberOfPumps == 1) ? "pump" : "pumps";
                    String textType = type.name();

                    String espressoShotPrettyPrint = textNumberOfPumps + " " +
                            textClass + " " +
                            textType + " " +
                            "\n(shaken:" + isShaken + ") " +
                            "(blended:" + isBlended + ")";

                    drinkComponentsPrettyPrint.add(espressoShotPrettyPrint);
                } else {
                    continue;
                }
            } else if (drinkComponentFirstTime instanceof EspressoShot) {
                EspressoShot espressoShot = (EspressoShot) drinkComponentFirstTime;

                // first occurrence of this EspressoShot
                if (!isFirstTimeEspressoShot.containsKey(espressoShot)) {
                    isFirstTimeEspressoShot.put(espressoShot, false);

                    int numberOfShots = espressoShotsMap.get(espressoShot);
                    EspressoShot.Type type = espressoShot.getType();
                    EspressoShot.AmountOfWater amountOfWater = espressoShot.getAmountOfWater();
                    EspressoShot.AmountOfBean amountOfBean = espressoShot.getAmountOfBean();
                    boolean isShaken = espressoShot.isShaken();
                    boolean isBlended = espressoShot.isBlended();

                    String textNumberOfShots = Integer.toString(numberOfShots);
                    String textClass = (numberOfShots == 1) ? "shot" : "shots";
                    String textType = null;
                    switch (type) {
                        case BLONDE:
                            textType = "BLONDE";
                            break;
                        case SIGNATURE:
                            textType = "      ";
                            break;
                        case DECAF:
                            textType = "DECAF";
                            break;
                    }
                    String textAmountOfWater = null;
                    switch (amountOfWater) {
                        case RISTRETTO:
                            textAmountOfWater = "RIST";
                            break;
                        case STANDARD:
                            textAmountOfWater = "    ";
                            break;
                        case LONG:
                            textAmountOfWater = "LONG";
                            break;
                    }
                    String textAmountOfBean = null;
                    switch (amountOfBean) {
                        case HALF_DECAF:
                            textAmountOfBean = "1/2 DECAF";
                            break;
                        case STANDARD:
                            textAmountOfBean = "    ";
                            break;
                        case UPDOSED:
                            textAmountOfBean = "UPDOSED";
                            break;
                    }
                    String espressoShotPrettyPrint = textNumberOfShots + " " +
                            textClass + " " +
                            textType + " " +
                            textAmountOfWater + " " +
                            textAmountOfBean + " " +
                            "\n(shaken:" + isShaken + ") " +
                            "(blended:" + isBlended + ")";

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
