package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels;

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

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.ClickableAndDraggableImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteEspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collideable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collider;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupHot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShotGlass extends ClickableAndDraggableImageView
        implements LiquidContainable, Collideable {
    public static final String TAG = ShotGlass.class.getSimpleName();
    public static final String DRAG_LABEL = ShotGlass.class.getSimpleName();

    private List<EspressoShot> espressoShots;

    private Collider collider;

    private Paint textPaint;

    public ShotGlass(@NonNull Context context) {
        super(context);
        init();
    }

    public ShotGlass(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void doClick(MotionEvent event) {
        // TODO: open dialog listing content of shot glass.
        listenerShowDialog.showSpriteDetailsDialogFragment(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void doMove(MotionEvent event) {
        String label = DRAG_LABEL;

        ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(this);

        // Start the drag.
        startDragAndDrop(
                dragData,           // The data to be dragged.
                myShadow,           // The drag shadow builder.
                this,    // The ShotGlass.
                0              // Flags. Not currently used, set to 0.
        );
        setVisibility(View.INVISIBLE);

        Log.e(TAG, "label: " + label);
    }

    private void init() {
        espressoShots = new ArrayList<>();

        collider = new Collider() {
            @Override
            public void onCollided(View collider) {
                Toast.makeText(getContext(), "onCollided(View)", Toast.LENGTH_SHORT).show();

                setAlpha(0.5f);

                if (collider instanceof SpriteEspressoShot) {
                    Log.e(TAG, "collider instanceof SpriteEspressoShot");

                    SpriteEspressoShot spriteEspressoShot = (SpriteEspressoShot) collider;
                    espressoShots.add(
                            spriteEspressoShot.instantiateEspressoShot()
                    );
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        EspressoShot.Type typeMostRecent = EspressoShot.Type.SIGNATURE;
        String typeAbbreviated = typeMostRecent.name().substring(0, 1);
        String amountOfWaterAbbreviated = EspressoShot.AmountOfWater.STANDARD.name().substring(0, 1);
        String amountOfBeanAbbreviated = EspressoShot.AmountOfBean.STANDARD.name().substring(0, 1);
        int numberOfShots = espressoShots.size();
        if (numberOfShots != 0) {
            int indexOfLast = numberOfShots - 1;
            EspressoShot shotMostRecent = espressoShots.get(indexOfLast);

            typeMostRecent = shotMostRecent.getType();
            typeAbbreviated = typeMostRecent.name().substring(0, 1);
            amountOfWaterAbbreviated = shotMostRecent.getAmountOfWater().name().substring(0, 1);
            amountOfBeanAbbreviated = shotMostRecent.getAmountOfBean().name().substring(0, 1);
        }

        textPaint.setColor(getResources().getColor(
                SpriteEspressoShot.lookupColorIdByType(typeMostRecent)
        ));

        String textForShot = String.format("E: %d %s %s %s",
                numberOfShots, typeAbbreviated, amountOfWaterAbbreviated, amountOfBeanAbbreviated);
        canvas.drawText(textForShot, 5, 15, textPaint);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determine whether this View can accept the dragged data.
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                    if (event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) || event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)) {
                        Log.d(TAG, "event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) || event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)");

                        if (((CupImageView) event.getLocalState()).getMilk() == null &&
                                ((CupImageView) event.getLocalState()).getShots().size() != 0) {
                            Log.d(TAG, "((CupImageView) event.getLocalState()).getMilk() == null && ((CupImageView) event.getLocalState()).getShots().size() != 0");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "NOT ((CupImageView) event.getLocalState()).getMilk() == null && ((CupImageView) event.getLocalState()).getShots().size() != 0");
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

                if (event.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) ||
                        event.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)) {
                    CupImageView cupImageView = ((CupImageView) event.getLocalState());

                    Toast.makeText(getContext(), "transferring content of cup", Toast.LENGTH_SHORT).show();
                    transferIn(
                            cupImageView.transferOut()
                    );
                    cupImageView.empty();
                }

                // Return true. DragEvent.getResult() returns true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED ShotGlass");

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

    public List<EspressoShot> getEspressoShots() {
        return espressoShots;
    }

    @Override
    public void transferIn(HashMap<String, Object> content) {
        if (content.containsKey("shots")) {
            List<EspressoShot> shotsToTransferIn = (List<EspressoShot>) content.get("shots");
            espressoShots.addAll(shotsToTransferIn);
        }

        invalidate();
    }

    @Override
    public HashMap<String, Object> transferOut() {
        HashMap<String, Object> content = new HashMap<>();

        List<EspressoShot> shotsCopy = new ArrayList<>(espressoShots);
        content.put("shots", shotsCopy);

        return content;
    }

    @Override
    public void empty() {
        espressoShots.clear();
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
