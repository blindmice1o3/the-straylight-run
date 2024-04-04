package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.Menu;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.CaffeLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteEspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteSyrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Cinnamon;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkOptions;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collideable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collider;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.LiquidContainable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CupImageView extends androidx.appcompat.widget.AppCompatImageView
        implements LiquidContainable, Collideable, Serializable {
    public static final String TAG = CupImageView.class.getSimpleName();

    protected Map<DrinkOptions, List<DrinkComponent>> content = new HashMap<>();

    protected Map<Syrup.Type, List<Syrup>> syrupsMap;

    protected List<EspressoShot> shots;

    protected Milk milk;

    protected boolean shotOnTop;
    protected boolean drizzled;
    protected Cinnamon cinnamon;
    protected boolean whippedCream;

    protected Collider collider;

    protected Paint textPaint;

    public CupImageView(Context context) {
        super(context);
        init();
    }

    public CupImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        syrupsMap = new HashMap<>();

        shots = new ArrayList<>();

        milk = null;

        shotOnTop = false;
        drizzled = false;

        collider = new Collider() {
            @Override
            public void onCollided(View collider) {
                Toast.makeText(getContext(), "onCollided(View)", Toast.LENGTH_SHORT).show();

                setAlpha(0.5f);

                if (collider instanceof SpriteEspressoShot) {
                    Log.e(TAG, "collider instanceof SpriteEspressoShot");

                    SpriteEspressoShot spriteEspressoShot = (SpriteEspressoShot) collider;
                    shots.add(
                            spriteEspressoShot.instantiateEspressoShot()
                    );
                    invalidate();

                    // TODO: shotOnTop
                    if (milk != null) {
                        Log.d(TAG, "milk != null... setting shotOnTop to true.");
                        shotOnTop = true;
                    }
                } else if (collider instanceof SpriteSyrup) {
                    Log.e(TAG, "collider instanceof Syrup");

                    SpriteSyrup spriteSyrup = (SpriteSyrup) collider;
                    Syrup.Type type = spriteSyrup.getType();

                    List<Syrup> syrups = null;
                    if (syrupsMap.get(type) == null) {
                        syrups = new ArrayList<>();
                    } else {
                        syrups = syrupsMap.get(type);
                    }
                    syrups.add(
                            spriteSyrup.instantiateSyrup()
                    );
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
                SpriteEspressoShot.lookupColorIdByType(typeMostRecent)
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
        canvas.drawText(Integer.toString(quantityVanilla), getWidth() - 16 - 16 - 16, yLine3, textPaint);

        textPaint.setColor(getResources().getColor(R.color.brown));
        int quantityBrownSugar = (syrupsMap.get(Syrup.Type.BROWN_SUGAR) == null) ? 0 : syrupsMap.get(Syrup.Type.BROWN_SUGAR).size();
        canvas.drawText(Integer.toString(quantityBrownSugar), getWidth() - 16 - 16, yLine3, textPaint);

        textPaint.setColor(getResources().getColor(R.color.dark_brown));
        int quantityMocha = (syrupsMap.get(Syrup.Type.MOCHA) == null) ? 0 : syrupsMap.get(Syrup.Type.MOCHA).size();
        canvas.drawText(Integer.toString(quantityMocha), getWidth() - 16, yLine3, textPaint);
    }

    protected void showDialogExpectedVsActual(DrinkLabel drinkLabel) {
        // (1) date, (2) time, (3) amOrPm, (4) size, (5) name.
        String[] drinkLabelSplitted = drinkLabel.getText().toString().split("\\s+");
//        String date = drinkLabelSplitted[0];
//        String time = drinkLabelSplitted[1];
//        String amOrPm = drinkLabelSplitted[2];
        String size = drinkLabelSplitted[3];
        String name = drinkLabelSplitted[4];
        // (6+) customization(s).
        int numberOfNonCustomizations = 5;
        List<String> customizations = new ArrayList<>();
        if (drinkLabelSplitted.length > numberOfNonCustomizations) {
            for (int i = 0; i < (drinkLabelSplitted.length - numberOfNonCustomizations); i++) {
                int indexCustomization = numberOfNonCustomizations + i;
                customizations.add(drinkLabelSplitted[indexCustomization]);
            }
        }

        /////////////////////////////////////////////////////////////////////////////////

        // Convert String size into enum Drink.Size.
        Drink.Size sizeFromLabel = null;
        for (Drink.Size sizeCurrent : Drink.Size.values()) {
            if (size.equals(sizeCurrent.name())) {
                sizeFromLabel = sizeCurrent;
                break;
            }
        }

        // EXPECTED
        Drink drink = Menu.getDrinkByName(name);
        List<DrinkComponent> drinkComponentsExpected =
                ((CaffeLatte) drink).getDrinkComponentsBySize(sizeFromLabel);

        StringBuilder sbExpected = new StringBuilder();
        for (DrinkComponent drinkComponent : drinkComponentsExpected) {
            sbExpected.append("\n");
            sbExpected.append(drinkComponent.toString());
        }

        // ACTUAL
        List<DrinkComponent> drinkComponentsActual = getDrinkComponentsAsList();
        StringBuilder sbActual = new StringBuilder();
        for (DrinkComponent drinkComponent : drinkComponentsActual) {
            sbActual.append("\n");
            sbActual.append(drinkComponent.toString());
        }

        String title = (customizations.isEmpty()) ?
                size + " | " + name + " (standard)" :
                size + " | " + name + " (customized)";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(
                String.format("EXPECTED: \n%s\n\n\nACTUAL: \n%s",
                        sbExpected.toString(),
                        sbActual.toString())
        );
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

    public List<DrinkComponent> getDrinkComponentsAsList() {
        List<DrinkComponent> drinkComponentsInsideCup = new ArrayList<>();

        for (EspressoShot shot : shots) {
            drinkComponentsInsideCup.add(shot);
        }
        if (milk != null) {
            drinkComponentsInsideCup.add(milk);
        }
        for (Syrup.Type type : Syrup.Type.values()) {
            if (syrupsMap.containsKey(type)) {
                List<Syrup> syrups = syrupsMap.get(type);
                for (Syrup syrup : syrups) {
                    drinkComponentsInsideCup.add(syrup);
                }
            }
        }
        if (cinnamon != null) {
            drinkComponentsInsideCup.add(cinnamon);
        }

        return drinkComponentsInsideCup;
    }

    protected void showDialogWinner(DrinkLabel drinkLabel) {
        // (1) date, (2) time, (3) amOrPm, (4) size, (5) name.
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

    protected boolean isWinnerWinnerChickenDinner(DrinkLabel drinkLabel) {
        String[] text = drinkLabel.getText().toString().split("\\s+");
        // (1) date, (2) time, (3) amOrPm, (4) size, (5) name.
        String size = text[3];
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

    public Map<Syrup.Type, List<Syrup>> getSyrupsMap() {
        return syrupsMap;
    }

    public void setSyrupsMap(Map<Syrup.Type, List<Syrup>> syrupsMap) {
        this.syrupsMap = syrupsMap;
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

    public boolean isShotOnTop() {
        return shotOnTop;
    }

    public void setShotOnTop(boolean shotOnTop) {
        this.shotOnTop = shotOnTop;
    }

    public boolean isDrizzled() {
        return drizzled;
    }

    public void setDrizzled(boolean drizzled) {
        this.drizzled = drizzled;
    }

    public Cinnamon getCinnamon() {
        return cinnamon;
    }

    public void setCinnamon(Cinnamon cinnamon) {
        this.cinnamon = cinnamon;
    }

    public boolean isWhippedCream() {
        return whippedCream;
    }

    public void setWhippedCream(boolean whippedCream) {
        this.whippedCream = whippedCream;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        if (content.containsKey("syrupsMap")) {
            Map<Syrup.Type, List<Syrup>> syrupsMapToTransferIn = (Map<Syrup.Type, List<Syrup>>) content.get("syrupsMap");
            for (Syrup.Type type : syrupsMapToTransferIn.keySet()) {
                List<Syrup> syrupsToTransferIn = syrupsMapToTransferIn.get(type);

                if (syrupsMap.containsKey(type)) {
                    syrupsMap.get(type).addAll(syrupsToTransferIn);
                } else {
                    syrupsMap.put(type, syrupsToTransferIn);
                }
            }
        }

        if (content.containsKey("shots")) {
            List<EspressoShot> shotsToTransferIn = (List<EspressoShot>) content.get("shots");
            shots.addAll(shotsToTransferIn);

            // TODO: shotOnTop
            if (milk != null) {
                Log.d(TAG, "milk != null... setting shotOnTop to true.");
                shotOnTop = true;
            }
        }

        if (content.containsKey("milk")) {
            milk = (Milk) content.get("milk");
        }

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
        if (content.containsKey("cinnamon")) {
            cinnamon = (Cinnamon) content.get("cinnamon");
        }
        if (content.containsKey("whippedCream")) {
            whippedCream = (Boolean) content.get("whippedCream");
        }

        invalidate();
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = new HashMap<>();

        Map<Syrup.Type, List<Syrup>> syrupsMapCopy = new HashMap<>(syrupsMap);
        content.put("syrupsMap", syrupsMapCopy);

        List<EspressoShot> shotsCopy = new ArrayList<>(shots);
        content.put("shots", shotsCopy);

        content.put("milk", milk);

        content.put("shotOnTop", Boolean.toString(shotOnTop));
        content.put("drizzled", Boolean.toString(drizzled));
        if (cinnamon != null) {
            content.put("cinnamon", cinnamon);
        }
        content.put("whippedCream", whippedCream);

        return content;
    }

    @Override
    public void empty() {
        syrupsMap.clear();

        shots.clear();

        milk = null;

        shotOnTop = false;
        drizzled = false;
        cinnamon = null;
        whippedCream = false;

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
