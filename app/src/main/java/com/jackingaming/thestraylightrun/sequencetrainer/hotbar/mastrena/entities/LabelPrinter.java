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

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.MenuItemRequestGenerator;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LabelPrinter extends AppCompatTextView {
    public static final String TAG = LabelPrinter.class.getSimpleName();
    public static final String DRAG_LABEL = LabelPrinter.class.getSimpleName();
    private static final int MAX_NUMBER_OF_DRINKS = 10;

    private List<String> queueDrinks = new ArrayList<>();
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

            queueDrinks.add(
                    contentNewDrinkLabel
            );
        } else if (standardOrCustomized == CUSTOMIZED) {
            Drink drinkRandomCustomized = MenuItemRequestGenerator.requestRandomCustomizedDrink();
            StringBuilder sb = new StringBuilder();
            sb.append(formatDateTime + "\n")
                    .append(drinkRandomCustomized.getSize() + "\n")
                    .append(drinkRandomCustomized.getName() + "\n");
            for (int i = 0; i < drinkRandomCustomized.queryNumberOfCustomizations(); i++) {
                sb.append(drinkRandomCustomized.getCustomizationByIndex(i));

                if (i != (drinkRandomCustomized.queryNumberOfCustomizations() - 1)) {
                    sb.append("\n");
                }
            }

            queueDrinks.add(
                    sb.toString()
            );
        }

        counter++;
    }

    public int numberOfDrinksInQueue() {
        return queueDrinks.size();
    }

    public void updateDisplay() {
        if (queueDrinks.size() > 0) {
            setText(
                    queueDrinks.get(0)
            );
        } else {
            setText("queue is empty");
        }
    }

    public boolean removeFromQueue(String contentDrinkLabel) {
        boolean isSuccessRemoval = queueDrinks.remove(contentDrinkLabel);
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
                return false;
            }

            String label = DRAG_LABEL;

            ClipData dragData = ClipData.newPlainText(label,
                    getText());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,               // The LabelPrinter.
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
