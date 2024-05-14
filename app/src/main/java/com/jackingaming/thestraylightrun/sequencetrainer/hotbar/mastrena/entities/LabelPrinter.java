package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.Menu;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.MenuItemRequestGenerator;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LabelPrinter extends ClickableAndDraggableTextView {
    public static final String TAG = LabelPrinter.class.getSimpleName();
    public static final String DRAG_LABEL = LabelPrinter.class.getSimpleName();
    private static final int MAX_NUMBER_OF_DRINKS = 10;

    public interface LabelPrinterListener {
        void onDrinkAdded(int indexToAdd);

        void onDrinkRemoved(int indexToRemove);

        void onLastDrinkRemoved(Drink drink);
    }

    private LabelPrinterListener listener;

    private List<Drink> queueDrinks = new ArrayList<>();
    //    private List<String> queueDrinks = new ArrayList<>();
    private int counter = 0;

    public LabelPrinter(@NonNull Context context) {
        super(context);
    }

    public LabelPrinter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void doClick(MotionEvent event) {
        // TODO: open dialog listing details of label printer.
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void doMove(MotionEvent event) {
        if (queueDrinks.size() <= 0) {
            // intentionally blank.
            return;
        }

        String label = DRAG_LABEL;
        Drink drinkFirst = queueDrinks.get(0);

        ClipData dragData = ClipData.newPlainText(label,
                getText());
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

        // Start the drag.
        startDragAndDrop(
                dragData,           // The data to be dragged.
                myShadow,           // The drag shadow builder.
                drinkFirst,               // The first Drink from queueDrinks.
                0              // Flags. Not currently used, set to 0.
        );
        setVisibility(View.INVISIBLE);

        Log.e(TAG, "label: " + label);
    }

    private int modeSelected = STANDARD;
    private static final int STANDARD = 0;
    private static final int CUSTOMIZED = 1;
    private static final int BOTH = 2;
    private Random random = new Random();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateRandomDrinkRequest() {
        if (counter >= MAX_NUMBER_OF_DRINKS) {
            Log.e(TAG, "counter >= MAX_NUMBER_OF_DRINKS... returning");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String formatDateTime = now.format(formatter);

        int standardOrCustomized = -1;
        if (modeSelected == STANDARD) {
            standardOrCustomized = STANDARD;
        } else if (modeSelected == CUSTOMIZED) {
            standardOrCustomized = CUSTOMIZED;
        } else {
            // randomly select between standard or customized.
            standardOrCustomized = random.nextInt(2);
        }

        if (standardOrCustomized == STANDARD) {
            Drink drinkRandomStandard = MenuItemRequestGenerator.requestRandomDrink();

            // initialize textForDrinkLabel for drink
            String contentNewDrinkLabel = String.format("%s\n%s\n%s\n%s",
                    formatDateTime,
                    "cafe",
                    drinkRandomStandard.getSize(),
                    drinkRandomStandard.getName());

            drinkRandomStandard.setTextForDrinkLabel(
                    contentNewDrinkLabel
            );

            int indexToAdd = queueDrinks.size();
            queueDrinks.add(
                    drinkRandomStandard
            );
            listener.onDrinkAdded(indexToAdd);
        } else if (standardOrCustomized == CUSTOMIZED) {
            Log.e(TAG, "MAKING CUSTOMIZED DRINK!!!");

            Drink drinkRandomCustomized = MenuItemRequestGenerator.requestRandomCustomizedDrink();

            // initialize textForDrinkLabel for drink
            StringBuilder sb = new StringBuilder();
            sb.append(formatDateTime + "\n")
                    .append("cafe" + "\n")
                    .append(drinkRandomCustomized.getSize() + "\n")
                    .append(drinkRandomCustomized.getName());

            // Append differences between customized and standard to sb.

            // ***CUSTOMIZED***
            // ESPRESSO SHOT
            Map<EspressoShot.Type, Integer> shotsMapCustomized = new HashMap<>();
            // SYRUP
            Map<Syrup.Type, Integer> syrupsMapCustomized = new HashMap<>();
            // MILK
            Milk.Type typeMilkCustomized = null;
            List<DrinkComponent> drinkComponentsCustomized = new ArrayList<>(
                    drinkRandomCustomized.getDrinkComponents()
            );
            for (DrinkComponent drinkComponent : drinkComponentsCustomized) {
                if (drinkComponent instanceof EspressoShot) {
                    EspressoShot.Type type = ((EspressoShot) drinkComponent).getType();
                    if (shotsMapCustomized.containsKey(type)) {
                        Integer counterShot = shotsMapCustomized.get(type);
                        counterShot++;
                        shotsMapCustomized.put(type, counterShot);
                    } else {
                        shotsMapCustomized.put(type, 1);
                    }
                } else if (drinkComponent instanceof Syrup) {
                    Syrup.Type type = ((Syrup) drinkComponent).getType();
                    if (syrupsMapCustomized.containsKey(type)) {
                        Integer counterSyrup = syrupsMapCustomized.get(type);
                        counterSyrup++;
                        syrupsMapCustomized.put(type, counterSyrup);
                    } else {
                        syrupsMapCustomized.put(type, 1);
                    }
                } else if (drinkComponent instanceof Milk) {
                    typeMilkCustomized = ((Milk) drinkComponent).getType();
                }
            }

            // ***STANDARD***
            // ESPRESSO SHOT
            Map<EspressoShot.Type, Integer> shotsMapStandard = new HashMap<>();
            // SYRUP
            Map<Syrup.Type, Integer> syrupsMapStandard = new HashMap<>();
            // MILK
            Milk.Type typeMilkStandard = null;
            List<DrinkComponent> drinkComponentsStandard = new ArrayList<>(
                    Menu.getDrinkByName(drinkRandomCustomized.getName())
                            .getDrinkComponentsBySize(drinkRandomCustomized.getSize())
            );
            for (DrinkComponent drinkComponent : drinkComponentsStandard) {
                if (drinkComponent instanceof EspressoShot) {
                    EspressoShot.Type type = ((EspressoShot) drinkComponent).getType();
                    if (shotsMapStandard.containsKey(type)) {
                        Integer counterShot = shotsMapStandard.get(type);
                        counterShot++;
                        shotsMapStandard.put(type, counterShot);
                    } else {
                        shotsMapStandard.put(type, 1);
                    }
                } else if (drinkComponent instanceof Syrup) {
                    Syrup.Type type = ((Syrup) drinkComponent).getType();
                    if (syrupsMapStandard.containsKey(type)) {
                        Integer counterSyrup = syrupsMapStandard.get(type);
                        counterSyrup++;
                        syrupsMapStandard.put(type, counterSyrup);
                    } else {
                        syrupsMapStandard.put(type, 1);
                    }
                } else if (drinkComponent instanceof Milk) {
                    typeMilkStandard = ((Milk) drinkComponent).getType();
                }
            }

            // ***APPEND***
            // ESPRESSO SHOT
            for (EspressoShot.Type type : shotsMapCustomized.keySet()) {
                int counterCustomized = shotsMapCustomized.get(type);
                if (shotsMapStandard.containsKey(type)) {
                    int counterStandard = shotsMapStandard.get(type);

                    if (counterCustomized == counterStandard) {
                        continue;
                    } else {
                        String textUnit = (counterCustomized > 1) ? "shots" : "shot";
                        String textType = (type == EspressoShot.Type.SIGNATURE) ? "    " : type.name();
                        sb.append("\n" + counterCustomized + " " + textUnit + " " + textType);
                    }
                } else {
                    String textUnit = (counterCustomized > 1) ? "shots" : "shot";
                    String textType = (type == EspressoShot.Type.SIGNATURE) ? "    " : type.name();
                    sb.append("\n" + counterCustomized + " " + textUnit + " " + textType);
                }
            }
            // normally has shot... customization is 0-shot.
            for (EspressoShot.Type type : shotsMapStandard.keySet()) {
                int counterStandard = shotsMapStandard.get(type);
                if (!shotsMapCustomized.containsKey(type)) {
                    String textType = (type == EspressoShot.Type.SIGNATURE) ? "    " : type.name();
                    sb.append("\n" + 0 + " " + "shot" + " " + textType);
                }
            }
            // SYRUP
            for (Syrup.Type type : syrupsMapCustomized.keySet()) {
                int counterCustomized = syrupsMapCustomized.get(type);
                if (syrupsMapStandard.containsKey(type)) {
                    int counterStandard = syrupsMapStandard.get(type);

                    if (counterCustomized == counterStandard) {
                        continue;
                    } else {
                        sb.append("\n" + counterCustomized + " " + type);
                    }
                } else {
                    sb.append("\n" + counterCustomized + " " + type);
                }
            }
            // normally has syrup... customization is 0-syrup.
            for (Syrup.Type type : syrupsMapStandard.keySet()) {
                int counterStandard = syrupsMapStandard.get(type);
                if (!syrupsMapCustomized.containsKey(type)) {
                    sb.append("\n" + 0 + " " + type);
                }
            }
            // MILK
            if (typeMilkCustomized != typeMilkStandard) {
                sb.append("\n" + typeMilkCustomized);
            }
            // Drink.Property.CUP_SIZE_SPECIFIED
            if (!drinkRandomCustomized.getDrinkProperties().isEmpty()) {
                if (drinkRandomCustomized.getDrinkProperties().containsKey(Drink.Property.CUP_SIZE_SPECIFIED)) {
                    String cupStringSpecified = (String) drinkRandomCustomized.getDrinkProperties().get(Drink.Property.CUP_SIZE_SPECIFIED);
                    sb.append("\n" + "cup: " + cupStringSpecified);
                }
            }

            drinkRandomCustomized.setTextForDrinkLabel(
                    sb.toString()
            );

            int indexToAdd = queueDrinks.size();
            queueDrinks.add(
                    drinkRandomCustomized
            );
            listener.onDrinkAdded(indexToAdd);
        }

        counter++;
    }

    public int numberOfDrinksInQueue() {
        return queueDrinks.size();
    }

    public void updateDisplay() {
        if (queueDrinks.size() > 0) {
            Drink drinkFirst = queueDrinks.get(0);
            String textOnLabel = drinkFirst.getTextForDrinkLabel();

            setText(textOnLabel);
        } else {
            setText("queue is empty");
        }
    }

    public boolean removeFromQueue(Drink drinkToRemove) {
        int indexToRemove = queueDrinks.indexOf(drinkToRemove);
        boolean isSuccessRemoval = queueDrinks.remove(drinkToRemove);
        if (isSuccessRemoval) {
            listener.onDrinkRemoved(indexToRemove);
            Log.e(TAG, "SUCCESSFULLY removed!");

            // store timestamp of last drink from queue (before the last label is pulled).
            //   used to check db-service for drinks that are more recent.
            if (queueDrinks.isEmpty()) {
                listener.onLastDrinkRemoved(drinkToRemove);
            }
        }
        return isSuccessRemoval;
    }

    public void selectModeStandard() {
        modeSelected = STANDARD;
    }

    public void selectModeCustomized() {
        modeSelected = CUSTOMIZED;
    }

    public void selectModeBoth() {
        modeSelected = BOTH;
    }

    public LabelPrinterListener getListener() {
        return listener;
    }

    public void setListener(LabelPrinterListener listener) {
        this.listener = listener;
    }

    public List<Drink> getQueueDrinks() {
        return queueDrinks;
    }

    public void setQueueDrinks(List<Drink> queueDrinks) {
        this.queueDrinks = queueDrinks;
    }
}
