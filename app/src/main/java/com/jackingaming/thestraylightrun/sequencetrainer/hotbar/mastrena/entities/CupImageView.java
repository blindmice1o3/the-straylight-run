package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.Menu;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collideable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CupImageView extends androidx.appcompat.widget.AppCompatImageView
        implements LiquidContainable, Collideable {
    public static final String TAG = CupImageView.class.getSimpleName();

    private List<EspressoShot> shots;
//    private EspressoShot.Type type;
//    private EspressoShot.AmountOfWater amountOfWater;
//    private EspressoShot.AmountOfBean amountOfBean;
//    private int numberOfShots;

    // TODO: replace with List<Milk> milks
    private Milk milk;
//    private String content;
//    private int amount;
//    private int temperature;
//    private int timeFrothed;

    private Map<Syrup.Type, List<Syrup>> syrupsMap;

    private boolean shotOnTop;
    private boolean drizzled;

    private Collider collider;

    private Paint textPaint;

    public CupImageView(Context context) {
        super(context);
        init();
    }

    public CupImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        shots = new ArrayList<>();

        milk = null;
//        content = null;
//        amount = 0;
//        temperature = 0;
//        timeFrothed = 0;

        syrupsMap = new HashMap<>();

        shotOnTop = false;
        drizzled = false;

        collider = new Collider() {
            @Override
            public void onCollided(View collider) {
                Toast.makeText(getContext(), "onCollided(View)", Toast.LENGTH_SHORT).show();

                setAlpha(0.5f);

                if (collider instanceof EspressoShot) {
                    Log.e(TAG, "collider instanceof EspressoShot");

                    EspressoShot espressoShot = (EspressoShot) collider;

                    shots.add(espressoShot);
                    invalidate();
                } else if (collider instanceof Syrup) {
                    Log.e(TAG, "collider instanceof Syrup");

                    Syrup syrup = (Syrup) collider;
                    Syrup.Type type = syrup.getType();

                    List<Syrup> syrups = null;
                    if (syrupsMap.get(type) == null) {
                        syrups = new ArrayList<>();
                    } else {
                        syrups = syrupsMap.get(type);
                    }
                    syrups.add(syrup);
                    syrupsMap.put(type, syrups);
                    invalidate();
                }
            }
        };

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(getResources().getColor(R.color.brown));
        textPaint.setTextSize(14);
    }

    private int yLine1 = 15;
    private int yLine2 = 30;
    private int yLine3 = 45;
    private int yLine4 = 60;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        EspressoShot.Type typeMostRecent = EspressoShot.Type.SIGNATURE;
        String typeAbbreviated = typeMostRecent.name().substring(0, 1);
        String amountOfWaterAbbreviated = EspressoShot.AmountOfWater.STANDARD.name().substring(0, 1);
        String amountOfBeanAbbreviated = EspressoShot.AmountOfBean.STANDARD.name().substring(0, 1);
        int numberOfShots = shots.size();
        if (numberOfShots != 0) {
            int indexOfLast = numberOfShots - 1;
            EspressoShot shotMostRecent = shots.get(indexOfLast);

            typeMostRecent = shotMostRecent.getType();
            typeAbbreviated = typeMostRecent.name().substring(0, 1);
            amountOfWaterAbbreviated = shotMostRecent.getAmountOfWater().name().substring(0, 1);
            amountOfBeanAbbreviated = shotMostRecent.getAmountOfBean().name().substring(0, 1);
        }

        textPaint.setColor(getResources().getColor(
                EspressoShot.lookupColorIdByType(typeMostRecent)
        ));

        int yShot = (shotOnTop) ? yLine1 : yLine4;
        String textForShot = String.format("E: %d %s %s %s",
                numberOfShots, typeAbbreviated, amountOfWaterAbbreviated, amountOfBeanAbbreviated);
        canvas.drawText(textForShot, 5, yShot, textPaint);

        int temperature = (milk == null) ? (0) : (milk.getTemperature());
        int idPaintColor = (temperature < 160) ? R.color.purple_700 : R.color.red;
        textPaint.setColor(getResources().getColor(idPaintColor));
        int yTemperature = (shotOnTop) ? yLine2 : yLine1;
        canvas.drawText(Integer.toString(temperature), 5, yTemperature, textPaint);

        int timeFrothed = (milk == null) ? (0) : (milk.getTimeFrothed());
        textPaint.setColor(getResources().getColor(R.color.red));
        canvas.drawText(Integer.toString(timeFrothed), getWidth() - 16, yTemperature, textPaint);

        String nameOfContent = (milk == null) ? ("null") : (milk.getType().name());
        textPaint.setColor(getResources().getColor(R.color.purple_700));
        int yContentName = (shotOnTop) ? yLine3 : yLine2;
        canvas.drawText(nameOfContent, 5, yContentName, textPaint);

        int amount = (milk == null) ? (0) : (milk.getAmount());
        int yContentAmount = (shotOnTop) ? yLine4 : yLine3;
        canvas.drawText(Integer.toString(amount), 5, yContentAmount, textPaint);

        textPaint.setColor(getResources().getColor(R.color.amber));
        int quantityVanilla = (syrupsMap.get(Syrup.Type.VANILLA) == null) ? 0 : syrupsMap.get(Syrup.Type.VANILLA).size();
        int ySyrupVanilla = (shotOnTop) ? yLine3 : yLine2;
        canvas.drawText(Integer.toString(quantityVanilla), getWidth() - 16, ySyrupVanilla, textPaint);

        textPaint.setColor(getResources().getColor(R.color.brown));
        int quantityBrownSugar = (syrupsMap.get(Syrup.Type.BROWN_SUGAR) == null) ? 0 : syrupsMap.get(Syrup.Type.BROWN_SUGAR).size();
        int ySyrupBrownSugar = (shotOnTop) ? yLine4 : yLine3;
        canvas.drawText(Integer.toString(quantityBrownSugar), getWidth() - 16, ySyrupBrownSugar, textPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "MastrenaToCaddy";

            ClipData dragData = ClipData.newPlainText(label,
                    (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,    // The CupImageView.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }

    private String label;

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    label = event.getClipDescription().getLabel().toString();
                    if (label.equals("ShotGlass") ||
                            label.equals("CaramelDrizzleBottle") ||
                            label.equals("DrinkLabel")) {
                        Log.d(TAG, "label.equals(\"ShotGlass\") || label.equals(\"CaramelDrizzleBottle\") || label.equals(\"DrinkLabel\")");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);

                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    } else if (label.equals("SteamingPitcher")) {
                        Log.d(TAG, "label.equals(\"SteamingPitcher\")");

                        if (((SteamingPitcher) event.getLocalState()).getMilk().getAmount() != 0) {
                            Log.d(TAG, "((SteamingPitcher) event.getLocalState()).getMilk().getAmount() != 0");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "((SteamingPitcher) event.getLocalState()).getMilk().getAmount() == 0");
                        }
                    }
                } else {
                    Log.e(TAG, "ACTION_DRAG_STARTED clip description NOT ClipDescription.MIMETYPE_TEXT_PLAIN");
                }

                // Return false to indicate that, during the current drag and drop
                // operation, this View doesn't receive events again until
                // ACTION_DRAG_ENDED is sent.
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ACTION_DRAG_ENTERED");

                // Change value of alpha to indicate [ENTERED] state.
                setAlpha(0.5f);

                // Return true. The value is ignored.
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event.
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "ACTION_DRAG_EXITED");

                // Reset value of alpha back to normal.
                setAlpha(0.75f);

                // Return true. The value is ignored.
                return true;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "ACTION_DROP");

                if (label.equals("SteamingPitcher")) {
                    Log.d(TAG, "label.equals(\"SteamingPitcher\")");

                    SteamingPitcher steamingPitcher = (SteamingPitcher) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of steaming pitcher", Toast.LENGTH_SHORT).show();
                    transferIn(
                            steamingPitcher.transferOut()
                    );
                } else if (label.equals("ShotGlass")) {
                    Log.d(TAG, "label.equals(\"ShotGlass\")");

                    // TODO:
                    if (milk != null) {
                        Log.d(TAG, "milk != null... setting shotOnTop to true.");
                        shotOnTop = true;
                    }

                    ShotGlass shotGlass = (ShotGlass) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of shot glass", Toast.LENGTH_SHORT).show();
                    transferIn(
                            shotGlass.transferOut()
                    );
                } else if (label.equals("CaramelDrizzleBottle")) {
                    Log.d(TAG, "label.equals(\"CaramelDrizzleBottle\")");

                    if (milk != null && shots.size() > 0) {
                        Log.d(TAG, "milk != null && shots.size() > 0... setting drizzled to true.");
                        drizzled = true;
                    }
                } else if (label.equals("DrinkLabel")) {
                    DrinkLabel drinkLabel = (DrinkLabel) event.getLocalState();

                    if (isWinnerWinnerChickenDinner(drinkLabel)) {
                        showDialogWinner(drinkLabel);

                        ((FrameLayout) drinkLabel.getParent()).removeView(drinkLabel);
                        ((FrameLayout) getParent()).removeView(this);
                    } else {
                        Toast.makeText(getContext(), "NOT a winner", Toast.LENGTH_SHORT).show();
                        drinkLabel.setVisibility(View.VISIBLE);
                    }
                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED CupImageView");

                // Reset value of alpha back to normal.
                setAlpha(1.0f);

                // Do a getResult() and displays what happens.
                if (event.getResult()) {
                    Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                }
                // Return true. The value is ignored.
                return true;
            default:
                Log.e(TAG, "Unknown action type received by onDragEvent(DragEvent).");
                break;
        }

        return false;
    }

    private void showDialogWinner(DrinkLabel drinkLabel) {
        String[] drinkLabelSplitted = drinkLabel.getText().toString().split("\\s+");
        String date = drinkLabelSplitted[0];
        String time = drinkLabelSplitted[1];
        String amOrPm = drinkLabelSplitted[2];
        String size = drinkLabelSplitted[3];
        String name = drinkLabelSplitted[4];

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("WINNER WINNER CHICKEN DINNER");
        alertDialogBuilder.setMessage(size + " " + name + " (" + time + " " + amOrPm + ")!");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    private boolean isWinnerWinnerChickenDinner(DrinkLabel drinkLabel) {
        String[] text = drinkLabel.getText().toString().split("\\s+");
        // (1) date, (2) time, (3) amOrPm, (4) size, (5) name.
        String size = text[3].toLowerCase();
        String name = text[4];
        // (6+) customization(s).
        int numberOfNonCustomizations = 5;
        List<String> customizations = null;
        if (text.length > numberOfNonCustomizations) {
            customizations = new ArrayList<>();
            for (int i = 0; i < (text.length - numberOfNonCustomizations); i++) {
                int indexCustomization = numberOfNonCustomizations + i;
                customizations.add(text[indexCustomization]);
            }
        }

        return Menu.getDrinkByName(name).validate(this, size, customizations);
    }

    public List<EspressoShot> getShots() {
        return shots;
    }

    public void setShots(List<EspressoShot> shots) {
        this.shots = shots;
    }

    public Milk getMilk() {
        return milk;
    }

    public void setMilk(Milk milk) {
        this.milk = milk;
    }

    public Map<Syrup.Type, List<Syrup>> getSyrupsMap() {
        return syrupsMap;
    }

    public void setSyrupsMap(Map<Syrup.Type, List<Syrup>> syrupsMap) {
        this.syrupsMap = syrupsMap;
    }

    public boolean isDrizzled() {
        return drizzled;
    }

    public void setDrizzled(boolean drizzled) {
        this.drizzled = drizzled;
    }

    public boolean isShotOnTop() {
        return shotOnTop;
    }

    public void setShotOnTop(boolean shotOnTop) {
        this.shotOnTop = shotOnTop;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        if (content.containsKey("shots")) {
            List<EspressoShot> shotsToTransferIn = (List<EspressoShot>) content.get("shots");
            shots.addAll(shotsToTransferIn);
        }

        if (content.containsKey("milk")) {
            milk = (Milk) content.get("milk");
        }

        // TODO: syrups

        if (content.containsKey("shotOnTop")) {
            shotOnTop = Boolean.parseBoolean(
                    (String) content.get("shotOnTop")
            );
        }
        if (content.containsKey("drizzled")) {
            drizzled = Boolean.parseBoolean(
                    (String) content.get("drizzled")
            );
        }

        invalidate();
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = new HashMap<>();

        List<EspressoShot> shotsCopy = new ArrayList<>(shots);
        content.put("shots", shotsCopy);

        content.put("milk", milk);

        // TODO: syrups

        content.put("shotOnTop", Boolean.toString(shotOnTop));
        content.put("drizzled", Boolean.toString(drizzled));

        empty();

        return content;
    }

    @Override
    public void empty() {
        shots.clear();

        milk = null;

        syrupsMap.clear();

        shotOnTop = false;
        drizzled = false;

        invalidate();
    }

    @Override
    public void update(boolean colliding) {
        collider.update(colliding);
    }

    @Override
    public boolean isJustCollided() {
        return collider.isJustCollided();
    }

    @Override
    public void onCollided(View collider) {
        this.collider.onCollided(collider);
    }
}
