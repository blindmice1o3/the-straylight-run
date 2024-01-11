package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.LiquidContainable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.Syrup;

import java.util.HashMap;
import java.util.Map;

public class CupImageView extends androidx.appcompat.widget.AppCompatImageView
        implements LiquidContainable {
    public static final String TAG = CupImageView.class.getSimpleName();

    private EspressoShot.Type type;
    private int numberOfShots;
    private boolean colliding, cantCollide, justCollided;

    private int temperature;
    private String content;
    private int amount;

    private Map<Syrup.Type, Integer> syrups;

    private boolean shotOnTop;

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
        type = EspressoShot.Type.SIGNATURE;
        numberOfShots = 0;

        temperature = 0;
        content = null;
        amount = 0;

        syrups = new HashMap<>();

        shotOnTop = false;

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(getResources().getColor(R.color.brown));
        textPaint.setTextSize(14);
    }

    public void update(boolean colliding) {
        this.colliding = colliding;
        if (cantCollide && !this.colliding) {
            cantCollide = false;
        } else if (justCollided) {
            cantCollide = true;
            justCollided = false;
        }
        if (!cantCollide && this.colliding) {
            justCollided = true;
        }
    }

    public void onCollided(View collider) {
        Toast.makeText(getContext(), "onCollided(View)", Toast.LENGTH_SHORT).show();

        setAlpha(0.5f);

        if (collider instanceof EspressoShot) {
            Log.e(TAG, "collider instanceof EspressoShot");

            EspressoShot espressoShot = (EspressoShot) collider;
            type = espressoShot.getType();
            numberOfShots++;
            invalidate();

            if (isWinnerWinnerChickenDinner()) {
                showDialogWinner();
            }
        } else if (collider instanceof Syrup) {
            Log.e(TAG, "collider instanceof Syrup");

            Syrup syrup = (Syrup) collider;
            int quantityPrevious = (syrups.get(syrup.getType()) == null) ? 0 : syrups.get(syrup.getType());
            int quantityNew = quantityPrevious + 1;
            syrups.put(syrup.getType(), quantityNew);
            invalidate();

            // TODO:
        }
    }

    private int yLine1 = 15;
    private int yLine2 = 30;
    private int yLine3 = 45;
    private int yLine4 = 60;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setColor(getResources().getColor(
                EspressoShot.lookupColorIdByType(type)
        ));
        int yShot = (shotOnTop) ? yLine1 : yLine4;
        canvas.drawText("shots: " + numberOfShots, 5, yShot, textPaint);

        int idPaintColor = (temperature < 160) ? R.color.purple_700 : R.color.red;
        textPaint.setColor(getResources().getColor(idPaintColor));
        int yTemperature = (shotOnTop) ? yLine2 : yLine1;
        canvas.drawText(Integer.toString(temperature), 5, yTemperature, textPaint);

        String nameOfContent = (content == null) ? "null" : content;
        textPaint.setColor(getResources().getColor(R.color.purple_700));
        int yContentName = (shotOnTop) ? yLine3 : yLine2;
        canvas.drawText(nameOfContent, 5, yContentName, textPaint);

        int yContentAmount = (shotOnTop) ? yLine4 : yLine3;
        canvas.drawText(Integer.toString(amount), 5, yContentAmount, textPaint);

        textPaint.setColor(getResources().getColor(R.color.yellow));
        int quantityVanilla = (syrups.get(Syrup.Type.VANILLA) == null) ? 0 : syrups.get(Syrup.Type.VANILLA);
        int ySyrupVanilla = (shotOnTop) ? yLine2 : yLine1;
        canvas.drawText(Integer.toString(quantityVanilla), getWidth() - 8, ySyrupVanilla, textPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String label = "MaestranaToCaddy";

            ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
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
                    if (label.equals("ShotGlass")) {
                        Log.d(TAG, "label.equals(\"ShotGlass\")");

                        // Change value of alpha to indicate drop-target.
                        setAlpha(0.75f);

                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    } else if (label.equals("SteamingPitcher")) {
                        Log.d(TAG, "label.equals(\"SteamingPitcher\")");

                        if (((SteamingPitcher) event.getLocalState()).getAmount() != 0) {
                            Log.d(TAG, "((SteamingPitcher) event.getLocalState()).getAmount() != 0");

                            // Change value of alpha to indicate drop-target.
                            setAlpha(0.75f);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else {
                            Log.e(TAG, "((SteamingPitcher) event.getLocalState()).getAmount() == 0");
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
                    if (content != null) {
                        Log.d(TAG, "content != null... setting shotOnTop to true.");
                        shotOnTop = true;
                    }

                    ShotGlass shotGlass = (ShotGlass) event.getLocalState();

                    Toast.makeText(getContext(), "transferring content of shot glass", Toast.LENGTH_SHORT).show();
                    transferIn(
                            shotGlass.transferOut()
                    );
                }

                if (isWinnerWinnerChickenDinner()) {
                    showDialogWinner();
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

    private void showDialogWinner() {
        String title = "WINNER";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("WINNER WINNER CHICKEN DINNER!");
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

    private boolean isWinnerWinnerChickenDinner() {
        boolean isWinner = false;

        if (getTag().toString().equals("trenta")) {
            Log.e(TAG, "isWinnerWinnerChickenDinner() trenta");

            // blank.
        } else if (getTag().toString().equals("venti")) {
            Log.e(TAG, "isWinnerWinnerChickenDinner() venti");

            if (numberOfShots == 2 && temperature >= 160 && (amount >= (20 * 4))) {
                isWinner = true;
            }
        } else if (getTag().toString().equals("grande")) {
            Log.e(TAG, "isWinnerWinnerChickenDinner() grande");

            if (numberOfShots == 2 && temperature >= 160 && (amount >= (16 * 4))) {
                isWinner = true;
            }
        } else if (getTag().toString().equals("tall")) {
            Log.e(TAG, "isWinnerWinnerChickenDinner() tall");

            if (numberOfShots == 1 && temperature >= 160 && (amount >= (12 * 4))) {
                isWinner = true;
            }
        } else if (getTag().toString().equals("short")) {
            Log.e(TAG, "isWinnerWinnerChickenDinner() short");

            if (numberOfShots == 1 && temperature >= 160 && (amount >= (8 * 4))) {
                isWinner = true;
            }
        }

        return isWinner;
    }

    public EspressoShot.Type getType() {
        return type;
    }

    public void setType(EspressoShot.Type type) {
        this.type = type;
    }

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public void setNumberOfShots(int numberOfShots) {
        this.numberOfShots = numberOfShots;
    }

    public boolean isJustCollided() {
        return justCollided;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isShotOnTop() {
        return shotOnTop;
    }

    public void setShotOnTop(boolean shotOnTop) {
        this.shotOnTop = shotOnTop;
    }

    @Override
    public void transferIn(HashMap<String, String> content) {
        if (content.containsKey("type")) {
            for (EspressoShot.Type type : EspressoShot.Type.values()) {
                if (content.get("type").equals(type.name())) {
                    this.type = type;
                }
            }
        }
        if (content.containsKey("numberOfShots")) {
            // INCREMENT
            this.numberOfShots += Integer.parseInt(
                    content.get("numberOfShots")
            );
        }
        if (content.containsKey("temperature")) {
            this.temperature = Integer.parseInt(
                    content.get("temperature")
            );
        }
        if (content.containsKey("content")) {
            this.content = content.get("content");
        }
        if (content.containsKey("amount")) {
            this.amount = Integer.parseInt(
                    content.get("amount")
            );
        }

        invalidate();
    }

    @Override
    public HashMap<String, String> transferOut() {
        HashMap<String, String> content = new HashMap<>();
        content.put("type", this.type.name());
        content.put("numberOfShots", Integer.toString(this.numberOfShots));
        content.put("temperature", Integer.toString(this.temperature));
        content.put("content", this.content);
        content.put("amount", Integer.toString(this.amount));

        this.type = EspressoShot.Type.SIGNATURE;
        this.numberOfShots = 0;
        this.temperature = 0;
        this.content = null;
        this.amount = 0;
        invalidate();

        return content;
    }
}
