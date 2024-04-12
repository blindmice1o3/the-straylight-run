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
import androidx.appcompat.widget.AppCompatTextView;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.Menu;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.MenuItemRequestGenerator;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LabelPrinter extends AppCompatTextView {
    public static final String TAG = LabelPrinter.class.getSimpleName();
    public static final String DRAG_LABEL = LabelPrinter.class.getSimpleName();
    private static final int MAX_NUMBER_OF_DRINKS = 10;

    private List<Drink> queueDrinks = new ArrayList<>();
    //    private List<String> queueDrinks = new ArrayList<>();
    private int counter = 0;

    public LabelPrinter(@NonNull Context context) {
        super(context);
    }

    public LabelPrinter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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

            String contentNewDrinkLabel = String.format("%s\n%s\n%s",
                    formatDateTime,
                    drinkRandomStandard.getSize(),
                    drinkRandomStandard.getName());

            drinkRandomStandard.setTextForDrinkLabel(
                    contentNewDrinkLabel
            );

            queueDrinks.add(
                    drinkRandomStandard
            );
        } else if (standardOrCustomized == CUSTOMIZED) {
            Log.e(TAG, "MAKING CUSTOMIZED DRINK!!!");

            Drink drinkRandomCustomized = MenuItemRequestGenerator.requestRandomCustomizedDrink();
            StringBuilder sb = new StringBuilder();
            sb.append(formatDateTime + "\n")
                    .append(drinkRandomCustomized.getSize() + "\n")
                    .append(drinkRandomCustomized.getName());

            // Append differences between customized and standard to sb.

            // ***CUSTOMIZED***
            // SYRUP
            Map<Syrup.Type, Integer> syrupsMapCustomized = new HashMap<>();
            // MILK
            Milk.Type typeMilkCustomized = null;
            List<DrinkComponent> drinkComponentsCustomized = new ArrayList<>(
                    drinkRandomCustomized.getDrinkComponents()
            );
            for (DrinkComponent drinkComponent : drinkComponentsCustomized) {
                if (drinkComponent instanceof Syrup) {
                    Syrup.Type type = ((Syrup) drinkComponent).getType();
                    if (syrupsMapCustomized.containsKey(type)) {
                        Integer counter = syrupsMapCustomized.get(type);
                        counter++;
                        syrupsMapCustomized.put(type, counter);
                    } else {
                        syrupsMapCustomized.put(type, 1);
                    }
                } else if (drinkComponent instanceof Milk) {
                    typeMilkCustomized = ((Milk) drinkComponent).getType();
                }
            }

            // ***STANDARD***
            // SYRUP
            Map<Syrup.Type, Integer> syrupsMapStandard = new HashMap<>();
            // MILK
            Milk.Type typeMilkStandard = null;
            List<DrinkComponent> drinkComponentsStandard = new ArrayList<>(
                    Menu.getDrinkByName(drinkRandomCustomized.getName())
                            .getDrinkComponentsBySize(drinkRandomCustomized.getSize())
            );
            for (DrinkComponent drinkComponent : drinkComponentsStandard) {
                if (drinkComponent instanceof Syrup) {
                    Syrup.Type type = ((Syrup) drinkComponent).getType();
                    if (syrupsMapStandard.containsKey(type)) {
                        Integer counter = syrupsMapStandard.get(type);
                        counter++;
                        syrupsMapStandard.put(type, counter);
                    } else {
                        syrupsMapStandard.put(type, 1);
                    }
                } else if (drinkComponent instanceof Milk) {
                    typeMilkStandard = ((Milk) drinkComponent).getType();
                }
            }

            // ***APPEND***
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

            queueDrinks.add(
                    drinkRandomCustomized
            );
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
        boolean isSuccessRemoval = queueDrinks.remove(drinkToRemove);
        if (isSuccessRemoval) {
            Log.e(TAG, "SUCCESSFULLY removed!");
        }
        return isSuccessRemoval;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (queueDrinks.size() <= 0) {
                // intentionally blank.
                return true;
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

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
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
}
