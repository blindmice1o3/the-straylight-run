package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GestureDetectorCompat;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Cinnamon;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collideable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collider;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.OnSwipeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IceShaker extends AppCompatImageView
        implements LiquidContainable, Collideable {
    public static final String TAG = IceShaker.class.getSimpleName();

    private Ice ice;
    private Cinnamon cinnamon;
//    private boolean iced;
//    private boolean cinnamoned;

    private List<EspressoShot> shots;
//    private EspressoShot.Type type;
//    private EspressoShot.AmountOfWater amountOfWater;
//    private EspressoShot.AmountOfBean amountOfBean;
//    private int numberOfShots;

    private Map<Syrup.Type, List<Syrup>> syrupsMap;

    private Collider collider;

    private GestureDetectorCompat gestureDetector;

    private Paint textPaint;
    private int idRed;
    private int idLightBlueA200;
    private int idBlue;

    public IceShaker(@NonNull Context context) {
        super(context);
        init();
    }

    public IceShaker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ice = null;
        cinnamon = null;
//        iced = false;
//        cinnamoned = false;

        shots = new ArrayList<>();

        syrupsMap = new HashMap<>();

        collider = new Collider() {
            @Override
            public void onCollided(View collider) {
                Toast.makeText(getContext(), "onCollided(View)", Toast.LENGTH_SHORT).show();

                setAlpha(0.5f);

                if (collider instanceof Syrup) {
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

        gestureDetector = new GestureDetectorCompat(getContext(), new OnSwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                Log.e(TAG, "!!!!!!onSwipe direction: " + direction.toString());

                return true;
            }
        });

        idRed = getResources().getColor(R.color.red);
        idLightBlueA200 = getResources().getColor(R.color.light_blue_A200);
        idBlue = getResources().getColor(R.color.blue);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(idRed);
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

        String textForShot = String.format("E: %d %s %s %s",
                numberOfShots, typeAbbreviated, amountOfWaterAbbreviated, amountOfBeanAbbreviated);
        canvas.drawText(textForShot, 5, 15, textPaint);

        textPaint.setColor(getResources().getColor(R.color.amber));
        int quantityVanilla = (syrupsMap.get(Syrup.Type.VANILLA) == null) ? 0 : syrupsMap.get(Syrup.Type.VANILLA).size();
        int ySyrupVanilla = yLine2;
        canvas.drawText(Integer.toString(quantityVanilla), getWidth() - 16, ySyrupVanilla, textPaint);

        textPaint.setColor(getResources().getColor(R.color.brown));
        int quantityBrownSugar = (syrupsMap.get(Syrup.Type.BROWN_SUGAR) == null) ? 0 : syrupsMap.get(Syrup.Type.BROWN_SUGAR).size();
        int ySyrupBrownSugar = yLine3;
        canvas.drawText(Integer.toString(quantityBrownSugar), getWidth() - 16, ySyrupBrownSugar, textPaint);

        String textForCinnamoned = (cinnamon != null) ? "cinn" : "no-cinn";
        int colorForCinnamoned = (cinnamon != null) ? R.color.red : R.color.black;
        textPaint.setColor(getResources().getColor(colorForCinnamoned));
        canvas.drawText(textForCinnamoned, 5, ySyrupBrownSugar, textPaint);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.e(TAG, "onTouchEvent()");
//
//        if (gestureDetector.onTouchEvent(event)) {
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "IceShaker";

            ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

            // Start the drag.
            startDragAndDrop(
                    dragData,           // The data to be dragged.
                    myShadow,           // The drag shadow builder.
                    this,    // The IceShaker.
                    0              // Flags. Not currently used, set to 0.
            );
            setVisibility(View.INVISIBLE);

            Log.e(TAG, "label: " + label);

            // Indicate that the on-touch event is handled.
            return true;
        }

        return false;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    if (event.getClipDescription().getLabel().equals("MastrenaToCaddy") ||
                            event.getClipDescription().getLabel().equals("Ice") ||
                            event.getClipDescription().getLabel().equals("ShotGlass") ||
                            event.getClipDescription().getLabel().equals("CinnamonDispenser")) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(\"MastrenaToCaddy\") || event.getClipDescription().getLabel().equals(\"Ice\") || event.getClipDescription().getLabel().equals(\"ShotGlass\") || event.getClipDescription().getLabel().equals(\"CinnamonDispenser\")");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);
                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
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

                if (event.getClipDescription().getLabel().equals("MastrenaToCaddy")) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"MastrenaToCaddy\")");

                    // TODO:
                    CupImageView cupImageView = (CupImageView) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of cup", Toast.LENGTH_SHORT).show();
                    transferIn(
                            cupImageView.transferOut()
                    );
                } else if (event.getClipDescription().getLabel().equals("Ice")) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"Ice\")");

                    String contentToBeShaken = event.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, "contentToBeShaken: " + contentToBeShaken);

                    ice = new Ice(getContext());
                    setBackgroundColor(idBlue);
                    invalidate();
                } else if (event.getClipDescription().getLabel().equals("ShotGlass")) {
                    // TODO:
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"ShotGlass\")");

                    ShotGlass shotGlass = (ShotGlass) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of shot glass", Toast.LENGTH_SHORT).show();
                    transferIn(
                            shotGlass.transferOut()
                    );
                } else if (event.getClipDescription().getLabel().equals("CinnamonDispenser")) {
                    Log.e(TAG, "event.getClipDescription().getLabel().equals(\"CinnamonDispenser\")");

                    cinnamon = new Cinnamon(getContext());
                    invalidate();
                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED");

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
                Log.e(TAG, "Unknown action type received by onDragEvent().");
                break;
        }

        return false;
    }

    public void shake() {
        Log.e(TAG, "shake()");

        if (ice != null) {
            Log.e(TAG, "ice");
            ice.setShaken(true);
        }

        if (cinnamon != null) {
            Log.e(TAG, "cinnamon");
            cinnamon.setShaken(true);
        }

        if (!shots.isEmpty()) {
            for (EspressoShot shot : shots) {
                Log.e(TAG, "shot");
                shot.setShaken(true);
            }
        }

        if (!syrupsMap.isEmpty()) {
            for (Syrup.Type type : syrupsMap.keySet()) {
                List<Syrup> syrups = syrupsMap.get(type);
                for (Syrup syrup : syrups) {
                    Log.e(TAG, "syrup");
                    syrup.setShaken(true);
                }
            }
        }

        setBackgroundColor(getResources().getColor(R.color.purple_700));
    }

    public Ice getIce() {
        return ice;
    }

    public void setIce(Ice ice) {
        this.ice = ice;
    }

    public Cinnamon getCinnamon() {
        return cinnamon;
    }

    public void setCinnamon(Cinnamon cinnamon) {
        this.cinnamon = cinnamon;
    }

    public List<EspressoShot> getShots() {
        return shots;
    }

    public void setShots(List<EspressoShot> shots) {
        this.shots = shots;
    }

    public Map<Syrup.Type, List<Syrup>> getSyrupsMap() {
        return syrupsMap;
    }

    public void setSyrupsMap(Map<Syrup.Type, List<Syrup>> syrupsMap) {
        this.syrupsMap = syrupsMap;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        if (content.containsKey("ice")) {
            ice = (Ice) content.get("ice");
        }
        if (content.containsKey("cinnamon")) {
            cinnamon = (Cinnamon) content.get("cinnamon");
        }

        if (content.containsKey("shots")) {
            List<EspressoShot> shotsToTransferIn = (List<EspressoShot>) content.get("shots");
            shots.addAll(shotsToTransferIn);
        }

        // TODO: syrups
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

        invalidate();
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = new HashMap<>();

        if (ice != null) {
            content.put("ice", ice);
        }
        if (cinnamon != null) {
            content.put("cinnamon", cinnamon);
        }

        List<EspressoShot> shotsCopy = new ArrayList<>(shots);
        content.put("shots", shotsCopy);

        Map<Syrup.Type, List<Syrup>> syrupsMapCopy = new HashMap<>(syrupsMap);
        content.put("syrupsMap", syrupsMapCopy);

        empty();

        return content;
    }

    @Override
    public void empty() {
        ice = null;
        setBackgroundColor(idLightBlueA200);
        cinnamon = null;

        shots.clear();

        syrupsMap.clear();

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
