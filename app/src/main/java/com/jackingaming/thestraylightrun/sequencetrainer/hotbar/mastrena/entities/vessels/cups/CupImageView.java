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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.ClickableAndDraggableImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteEspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteSyrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Cinnamon;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkOptions;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Drizzle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.WhippedCream;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collideable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collider;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.LiquidContainable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CupImageView extends ClickableAndDraggableImageView
        implements LiquidContainable, Collideable, Serializable {
    public static final String TAG = CupImageView.class.getSimpleName();

    public interface CupImageViewListener {
        void showExpectedVsActualDialogFragment(Drink drink, CupImageView cupImageView);
    }

    private CupImageViewListener listener;

    protected Map<DrinkOptions, List<DrinkComponent>> content = new HashMap<>();

    protected WhippedCream whippedCream;
    protected Drizzle drizzle;

    protected Map<Syrup.Type, List<Syrup>> syrupsMap;

    protected List<EspressoShot> shots;

    protected Milk milk;

    protected boolean shotOnTop;
    protected Cinnamon cinnamon;

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
        whippedCream = null;
        drizzle = null;

        syrupsMap = new HashMap<>();

        shots = new ArrayList<>();

        milk = null;

        shotOnTop = false;

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
        // (0) date, (1) time, (2) amOrPm, (3) mobile/cafe (4) size, (5) name.
        String[] drinkLabelSplitted = drinkLabel.getText().toString().split("\\s+");
//        String date = drinkLabelSplitted[0];
//        String time = drinkLabelSplitted[1];
//        String amOrPm = drinkLabelSplitted[2];
        String mobileOrCafe = drinkLabelSplitted[3];
        String size = drinkLabelSplitted[4];
        String name = drinkLabelSplitted[5];
        // (6+) customization(s).

        /////////////////////////////////////////////////////////////////////////////////

        Drink drink = drinkLabel.getDrink();
        listener.showExpectedVsActualDialogFragment(drink, this);
    }

    public List<DrinkComponent> getDrinkComponentsAsList() {
        List<DrinkComponent> drinkComponentsInsideCup = new ArrayList<>();

        if (cinnamon != null) {
            drinkComponentsInsideCup.add(cinnamon);
        }
        if (drizzle != null) {
            drinkComponentsInsideCup.add(drizzle);
        }
        if (whippedCream != null) {
            drinkComponentsInsideCup.add(whippedCream);
        }
        for (Syrup.Type type : Syrup.Type.values()) {
            if (syrupsMap.containsKey(type)) {
                List<Syrup> syrups = syrupsMap.get(type);
                for (Syrup syrup : syrups) {
                    drinkComponentsInsideCup.add(syrup);
                }
            }
        }
        for (EspressoShot shot : shots) {
            drinkComponentsInsideCup.add(shot);
        }
        if (milk != null) {
            drinkComponentsInsideCup.add(milk);
        }

        return drinkComponentsInsideCup;
    }

    protected void showDialogWinner(DrinkLabel drinkLabel) {
        // (0) date, (1) time, (2) amOrPm, (3) mobile/cafe (4) size, (5) name.
        String[] drinkLabelSplitted = drinkLabel.getText().toString().split("\\s+");
        String date = drinkLabelSplitted[0];
        String time = drinkLabelSplitted[1];
        String amOrPm = drinkLabelSplitted[2];
        String mobileOrCafe = drinkLabelSplitted[3];
        String size = drinkLabelSplitted[4];
        String name = drinkLabelSplitted[5];
        // (6+) customization(s).

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
        // (0) date, (1) time, (2) amOrPm, (3) mobile/cafe (4) size, (5) name.
        String size = text[4];
        String name = text[5];
        // (6+) customization(s).

        Drink drink = drinkLabel.getDrink();
        return drink.validate(this);
    }

    public CupImageViewListener getCupImageViewListener() {
        return listener;
    }

    public void setCupImageViewListener(CupImageViewListener listener) {
        this.listener = listener;
    }

    public WhippedCream getWhippedCream() {
        return whippedCream;
    }

    public void setWhippedCream(WhippedCream whippedCream) {
        this.whippedCream = whippedCream;
    }

    public Drizzle getDrizzle() {
        return drizzle;
    }

    public void setDrizzle(Drizzle drizzle) {
        this.drizzle = drizzle;
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

    public Cinnamon getCinnamon() {
        return cinnamon;
    }

    public void setCinnamon(Cinnamon cinnamon) {
        this.cinnamon = cinnamon;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        if (content.containsKey("whippedCream")) {
            whippedCream = (WhippedCream) content.get("whippedCream");
        }

        if (content.containsKey("drizzle")) {
            drizzle = (Drizzle) content.get("drizzle");
        }

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
        if (content.containsKey("cinnamon")) {
            cinnamon = (Cinnamon) content.get("cinnamon");
        }

        invalidate();
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = new HashMap<>();

        if (whippedCream != null) {
            content.put("whippedCream", whippedCream);
        }

        if (drizzle != null) {
            content.put("drizzle", drizzle);
        }

        Map<Syrup.Type, List<Syrup>> syrupsMapCopy = new HashMap<>(syrupsMap);
        content.put("syrupsMap", syrupsMapCopy);

        List<EspressoShot> shotsCopy = new ArrayList<>(shots);
        content.put("shots", shotsCopy);

        if (milk != null) {
            content.put("milk", milk);
        }

        content.put("shotOnTop", Boolean.toString(shotOnTop));
        if (cinnamon != null) {
            content.put("cinnamon", cinnamon);
        }

        return content;
    }

    @Override
    public void empty() {
        whippedCream = null;
        drizzle = null;

        syrupsMap.clear();

        shots.clear();

        milk = null;

        shotOnTop = false;
        cinnamon = null;

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
