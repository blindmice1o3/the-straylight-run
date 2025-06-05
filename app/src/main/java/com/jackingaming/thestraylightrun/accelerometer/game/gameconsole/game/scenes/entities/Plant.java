package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.PlantDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.composeables.CooldownTimer;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.Random;

public class Plant extends Entity
        implements Sellable, Damageable {
    public static final String TAG = Plant.class.getSimpleName();

    public static final int BRACKET01_MIN_INCLUSIVE = 0;
    public static final int BRACKET01_MAX_EXCLUSIVE = 1;
    public static final int BRACKET02_MIN_INCLUSIVE = 1;
    public static final int BRACKET02_MAX_EXCLUSIVE = 3;
    public static final int BRACKET03_MIN_INCLUSIVE = 3;
    public static final int BRACKET03_MAX_EXCLUSIVE = 5;
    public static final int BRACKET04_MIN_INCLUSIVE = 5;
    public static final int BRACKET04_MAX_EXCLUSIVE = 7;
    private static final float PRICE_GREEN = 60f;
    private static final float PRICE_PURPLE = 85f;
    private static final int HEALTH_MAX_DEFAULT = 10;

    public enum Color {GREEN, PURPLE;}

    private int ageInDays;
    private boolean harvestable;
    transient private Bitmap imageBracket01, imageBracket02, imageBracket03, imageBracket04;
    transient private Bitmap imageHarvestableGreen, imageHarvestablePurple;
    transient private Bitmap imageHarvestable;
    private Color color;
    private float price;

    private int health;
    transient private Paint paintBorder;
    private boolean isShowingBorder = false;
    private CooldownTimer damageReceivedCooldownTimer;

    public static int numberOfDiseasedPlant = 0;
    private boolean isDiseased;

    public Plant(int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        ageInDays = 0;
        harvestable = false;

        Random random = new Random();
        int numberRandom = random.nextInt(100);
        color = (numberRandom < 50) ?
                Color.GREEN : Color.PURPLE;

        health = HEALTH_MAX_DEFAULT;

        damageReceivedCooldownTimer = new CooldownTimer();
        damageReceivedCooldownTimer.setCooldownTarget(3000L);

        // initialize diseased status
        numberRandom = random.nextInt(100);
        if (numberRandom < 33) {
            isDiseased = true;
            numberOfDiseasedPlant++;
        }
    }

    public void showPlantDialogFragment() {
        Log.e(TAG, "showPlantDialogFragment()");
        game.setPaused(true);

        PlantDialogFragment plantDialogFragment = PlantDialogFragment.newInstance(
                this, new PlantDialogFragment.DismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "onDismiss()");
                        game.setPaused(false);
                    }
                }
        );

        plantDialogFragment.show(
                ((MainActivity) game.getContext()).getSupportFragmentManager(),
                PlantDialogFragment.TAG
        );
    }

    @Override
    public void init(Game game) {
        super.init(game);

        imageBracket01 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green249);
        imageBracket02 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green256);
        imageBracket03 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green266);
        imageBracket04 = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green280);

        imageHarvestableGreen = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green2174);
        imageHarvestablePurple = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.purple2174);

        switch (color) {
            case GREEN:
                imageHarvestable = imageHarvestableGreen;
                price = PRICE_GREEN;
                break;
            case PURPLE:
                imageHarvestable = imageHarvestablePurple;
                price = PRICE_PURPLE;
                break;
            default:
                Log.e(TAG, "color not defined.");
                break;
        }

        updateImageBasedOnAgeInDays();

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(4f);
        paintBorder.setColor(android.graphics.Color.GREEN);
    }

    public void incrementAgeInDays() {
        ageInDays++;
        updateImageBasedOnAgeInDays();
    }

    private void updateImageBasedOnAgeInDays() {
        if (ageInDays >= BRACKET01_MIN_INCLUSIVE && ageInDays < BRACKET01_MAX_EXCLUSIVE) {
            image = imageBracket01;
        } else if (ageInDays >= BRACKET02_MIN_INCLUSIVE && ageInDays < BRACKET02_MAX_EXCLUSIVE) {
            image = imageBracket02;
        } else if (ageInDays >= BRACKET03_MIN_INCLUSIVE && ageInDays < BRACKET03_MAX_EXCLUSIVE) {
            image = imageBracket03;
        } else if (ageInDays >= BRACKET04_MIN_INCLUSIVE && ageInDays < BRACKET04_MAX_EXCLUSIVE) {
            image = imageBracket04;
        } else {
            harvestable = true;
            image = imageHarvestable;
        }
    }

    @Override
    public void update(long elapsed) {
        damageReceivedCooldownTimer.update(elapsed);
        if (damageReceivedCooldownTimer.isCooldowned()) {
            if (isShowingBorder) {
                isShowingBorder = false;
            }
        }
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {

    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {

    }

    @Override
    public void respondToTransferPointCollision(String key) {

    }

    @Override
    public boolean isCarryable() {
        return true;
    }

    public boolean isHarvestable() {
        return harvestable;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void takeDamage(int incomingDamage) {
        health -= incomingDamage;

        damageReceivedCooldownTimer.reset();
        isShowingBorder = true;
        updateBorderColor();

        if (health <= 0) {
            active = false;
            die();
        }
    }

    private void updateBorderColor() {
        if (health > (int) (HEALTH_MAX_DEFAULT * 0.66f)) {
            paintBorder.setColor(android.graphics.Color.GREEN);
        } else if (health > (int) (HEALTH_MAX_DEFAULT * 0.33f) && health <= (int) (HEALTH_MAX_DEFAULT * 0.66f)) {
            paintBorder.setColor(android.graphics.Color.YELLOW);
        } else if (health <= (int) (HEALTH_MAX_DEFAULT * 0.33f)) {
            paintBorder.setColor(android.graphics.Color.RED);
        }
    }

    @Override
    public void die() {
        Log.d(TAG, getClass().getSimpleName() + ".die()");
    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        if (image != null) {
            Rect rectOfImage = new Rect(0, 0, image.getWidth(), image.getHeight());
            Rect rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));

            // BORDER
            if (isShowingBorder) {
                canvas.drawRect(rectOnScreen, paintBorder);
            }
            // CONTENT
            canvas.drawBitmap(image, rectOfImage, rectOnScreen, paintLightingColorFilter);
        }
    }

    public int getHealth() {
        return health;
    }

    public int getAgeInDays() {
        return ageInDays;
    }

    public Color getColor() {
        return color;
    }

    public boolean isDiseased() {
        return isDiseased;
    }
}
