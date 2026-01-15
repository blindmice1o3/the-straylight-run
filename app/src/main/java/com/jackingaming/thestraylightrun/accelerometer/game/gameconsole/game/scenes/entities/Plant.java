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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHothouse;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.util.Random;

public class Plant extends Entity
        implements Sellable, Damageable {
    public static final String TAG = Plant.class.getSimpleName();

    public static final int BRACKET01_MIN_INCLUSIVE = 0;
    public static final int BRACKET01_MAX_EXCLUSIVE = 1;
    public static final int BRACKET02_MIN_INCLUSIVE = 1;
    public static final int BRACKET02_MAX_EXCLUSIVE = 2;
    public static final int BRACKET03_MIN_INCLUSIVE = 2;
    public static final int BRACKET03_MAX_EXCLUSIVE = 3;
    public static final int BRACKET04_MIN_INCLUSIVE = 3;
    public static final int BRACKET04_MAX_EXCLUSIVE = 4;
    private static final float PRICE_GREEN = 60f;
    private static final float PRICE_PURPLE = 85f;
    private static final int HEALTH_MAX_DEFAULT = 10;

    public enum Color {GREEN, PURPLE;}

    private String type;
    private int ageInDays;
    private boolean harvestable;
    transient private Bitmap imageBracket01, imageBracket02, imageBracket03, imageBracket04;
    transient private Bitmap imageHarvested;
    private Color color;
    private float price;

    private int health;
    transient private Paint paintBorder;
    private boolean isShowingBorder = false;
    private CooldownTimer damageReceivedCooldownTimer;

    public static int numberOfDiseasedPlant = 0;
    private boolean isDiseased;

    public Plant(String type, int xSpawn, int ySpawn) {
        super(xSpawn, ySpawn);

        this.type = type;
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
    public void becomeCarried() {
        super.becomeCarried();

        image = imageHarvested;
    }

    @Override
    public void init(Game game) {
        super.init(game);

        if (type.equals(
                game.getContext().getString(R.string.text_seed_banana))) {
            initImageOfBanana();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_bitter_melon))) {
            initImageOfBitterMelon();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_carrot))) {
            initImageOfCarrot();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_corn))) {
            initImageOfCorn();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_eggplant))) {
            initImageOfEggplant();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_garlic))) {
            initImageOfGarlic();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_guava))) {
            initImageOfGuava();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_lemongrass))) {
            initImageOfLemongrass();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_longan))) {
            initImageOfLongan();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_mystery))) {
            initImageOfMysteryPlant();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_onion))) {
            initImageOfOnion();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_papaya))) {
            initImageOfPapaya();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_peanut))) {
            initImageOfPeanut();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_radish))) {
            initImageOfRadish();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_strawberry))) {
            initImageOfStrawberry();
        } else if (type.equals(
                game.getContext().getString(R.string.text_seed_tomato))) {
            initImageOfTomato();
        } else {
            Log.e(TAG, "Plant.type is not listed. Cannot initialize images.");
        }

        updateImageBasedOnAgeInDays();

        if (color == Color.GREEN) {
            price = PRICE_GREEN;
        } else if (color == Color.PURPLE) {
            price = PRICE_PURPLE;
        }

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(4f);
        paintBorder.setColor(android.graphics.Color.GREEN);
    }

    private void initImageOfBanana() {
        Bitmap spriteSheetBanana = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_tree_banana);
        imageBracket01 = Bitmap.createBitmap(spriteSheetBanana, 106, 210, 243, 633);
        imageBracket02 = Bitmap.createBitmap(spriteSheetBanana, 338, 210, 375, 633);
        imageBracket03 = Bitmap.createBitmap(spriteSheetBanana, 722, 210, 375, 633);
        imageBracket04 = Bitmap.createBitmap(spriteSheetBanana, 1120, 210, 375, 633);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 642, 392, 221, 205);
    }

    private void initImageOfBitterMelon() {
        Bitmap spriteSheetBitterMelon = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_bittermelon);
        imageBracket01 = Bitmap.createBitmap(spriteSheetBitterMelon, 59, 266, 343, 575);
        imageBracket02 = Bitmap.createBitmap(spriteSheetBitterMelon, 370, 266, 343, 575);
        imageBracket03 = Bitmap.createBitmap(spriteSheetBitterMelon, 736, 266, 343, 575);
        imageBracket04 = Bitmap.createBitmap(spriteSheetBitterMelon, 1140, 266, 343, 575);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 967, 370, 157, 237);
    }

    private void initImageOfCarrot() {
        Bitmap spriteSheetCarrot = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_carrot);
        imageBracket01 = Bitmap.createBitmap(spriteSheetCarrot, 115, 214, 331, 628);
        imageBracket02 = Bitmap.createBitmap(spriteSheetCarrot, 409, 214, 331, 628);
        imageBracket03 = Bitmap.createBitmap(spriteSheetCarrot, 747, 214, 331, 628);
        imageBracket04 = Bitmap.createBitmap(spriteSheetCarrot, 1142, 214, 331, 628);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 386, 654, 142, 239);
    }

    private void initImageOfCorn() {
        Bitmap spriteSheetCorn = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_corn);
        imageBracket01 = Bitmap.createBitmap(spriteSheetCorn, 68, 198, 306, 643);
        imageBracket02 = Bitmap.createBitmap(spriteSheetCorn, 368, 198, 306, 643);
        imageBracket03 = Bitmap.createBitmap(spriteSheetCorn, 744, 198, 306, 643);
        imageBracket04 = Bitmap.createBitmap(spriteSheetCorn, 1132, 198, 306, 643);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 411, 371, 152, 242);
    }

    private void initImageOfEggplant() {
        Bitmap spriteSheetEggplant = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_eggplant);
        imageBracket01 = Bitmap.createBitmap(spriteSheetEggplant, 183, 252, 208, 514);
        imageBracket02 = Bitmap.createBitmap(spriteSheetEggplant, 406, 252, 323, 514);
        imageBracket03 = Bitmap.createBitmap(spriteSheetEggplant, 740, 252, 323, 514);
        imageBracket04 = Bitmap.createBitmap(spriteSheetEggplant, 1106, 252, 323, 514);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 1206, 67, 166, 264);
    }

    private void initImageOfGarlic() {
        Bitmap spriteSheetGarlic = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_garlic);
        imageBracket01 = Bitmap.createBitmap(spriteSheetGarlic, 98, 195, 300, 608);
        imageBracket02 = Bitmap.createBitmap(spriteSheetGarlic, 375, 195, 300, 608);
        imageBracket03 = Bitmap.createBitmap(spriteSheetGarlic, 726, 195, 300, 608);
        imageBracket04 = Bitmap.createBitmap(spriteSheetGarlic, 1108, 195, 300, 608);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 1200, 681, 174, 189);
    }

    private void initImageOfGuava() {
        Bitmap spriteSheetGuava = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_tree_guava);
        imageBracket01 = Bitmap.createBitmap(spriteSheetGuava, 92, 234, 341, 560);
        imageBracket02 = Bitmap.createBitmap(spriteSheetGuava, 378, 234, 341, 560);
        imageBracket03 = Bitmap.createBitmap(spriteSheetGuava, 732, 234, 341, 560);
        imageBracket04 = Bitmap.createBitmap(spriteSheetGuava, 1100, 234, 341, 560);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 676, 86, 198, 230);
    }

    private void initImageOfLemongrass() {
        Bitmap spriteSheetLemongrass = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_lemongrass);
        imageBracket01 = Bitmap.createBitmap(spriteSheetLemongrass, 148, 252, 308, 513);
        imageBracket02 = Bitmap.createBitmap(spriteSheetLemongrass, 434, 252, 308, 513);
        imageBracket03 = Bitmap.createBitmap(spriteSheetLemongrass, 772, 252, 308, 513);
        imageBracket04 = Bitmap.createBitmap(spriteSheetLemongrass, 1136, 252, 308, 513);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 157, 363, 111, 255);
    }

    private void initImageOfLongan() {
        Bitmap spriteSheetLongan = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_tree_logan);
        imageBracket01 = Bitmap.createBitmap(spriteSheetLongan, 173, 233, 203, 543);
        imageBracket02 = Bitmap.createBitmap(spriteSheetLongan, 390, 233, 331, 543);
        imageBracket03 = Bitmap.createBitmap(spriteSheetLongan, 729, 233, 342, 543);
        imageBracket04 = Bitmap.createBitmap(spriteSheetLongan, 1110, 233, 342, 543);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 964, 95, 151, 202);
    }

    private void initImageOfMysteryPlant() {
        Bitmap spriteSheetMysteryPlantGreen = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_mystery_plant);
        imageBracket01 = Bitmap.createBitmap(spriteSheetMysteryPlantGreen, 29, 195, 162, 612);
        imageBracket02 = Bitmap.createBitmap(spriteSheetMysteryPlantGreen, 247, 195, 191, 612);
        imageBracket03 = Bitmap.createBitmap(spriteSheetMysteryPlantGreen, 825, 195, 300, 612);
        imageBracket04 = Bitmap.createBitmap(spriteSheetMysteryPlantGreen, 1160, 195, 310, 612);

        if (color == Color.GREEN) {
            Bitmap spriteSheetItemsEntitiesCarryableAndBubbled = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.items_entities_carryable_and_bubbled);
            imageHarvested = Bitmap.createBitmap(spriteSheetItemsEntitiesCarryableAndBubbled, 78, 94, 192, 237);
        } else if (color == Color.PURPLE) {
            Bitmap spriteSheetMysteryProductPurple = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_mystery_product_purple);
            imageHarvested = Bitmap.createBitmap(spriteSheetMysteryProductPurple, 283, 180, 499, 675);
        }
    }

    private void initImageOfOnion() {
        Bitmap spriteSheetOnion = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_onion);
        imageBracket01 = Bitmap.createBitmap(spriteSheetOnion, 99, 224, 303, 569);
        imageBracket02 = Bitmap.createBitmap(spriteSheetOnion, 369, 224, 303, 569);
        imageBracket03 = Bitmap.createBitmap(spriteSheetOnion, 723, 224, 303, 569);
        imageBracket04 = Bitmap.createBitmap(spriteSheetOnion, 1095, 224, 303, 569);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 946, 678, 164, 207);
    }

    private void initImageOfPapaya() {
        Bitmap spriteSheetPapaya = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_tree_papaya);
        imageBracket01 = Bitmap.createBitmap(spriteSheetPapaya, 88, 206, 307, 592);
        imageBracket02 = Bitmap.createBitmap(spriteSheetPapaya, 372, 206, 326, 592);
        imageBracket03 = Bitmap.createBitmap(spriteSheetPapaya, 732, 206, 326, 592);
        imageBracket04 = Bitmap.createBitmap(spriteSheetPapaya, 1102, 206, 326, 592);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 108, 86, 180, 236);
    }

    private void initImageOfPeanut() {
        Bitmap spriteSheetPeanut = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_peanut);
        imageBracket01 = Bitmap.createBitmap(spriteSheetPeanut, 104, 276, 326, 558);
        imageBracket02 = Bitmap.createBitmap(spriteSheetPeanut, 400, 276, 326, 558);
        imageBracket03 = Bitmap.createBitmap(spriteSheetPeanut, 742, 276, 326, 558);
        imageBracket04 = Bitmap.createBitmap(spriteSheetPeanut, 1141, 276, 326, 558);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 124, 681, 119, 200);
    }

    private void initImageOfRadish() {
        Bitmap spriteSheetRadish = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_radish);
        imageBracket01 = Bitmap.createBitmap(spriteSheetRadish, 82, 270, 346, 565);
        imageBracket02 = Bitmap.createBitmap(spriteSheetRadish, 380, 270, 346, 565);
        imageBracket03 = Bitmap.createBitmap(spriteSheetRadish, 731, 270, 346, 565);
        imageBracket04 = Bitmap.createBitmap(spriteSheetRadish, 1138, 270, 346, 565);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 1209, 365, 168, 249);
    }

    private void initImageOfStrawberry() {
        Bitmap spriteSheetStrawberry = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_strawberry);
        imageBracket01 = Bitmap.createBitmap(spriteSheetStrawberry, 55, 547, 265, 318);
        imageBracket02 = Bitmap.createBitmap(spriteSheetStrawberry, 365, 547, 357, 318);
        imageBracket03 = Bitmap.createBitmap(spriteSheetStrawberry, 750, 547, 357, 318);
        imageBracket04 = Bitmap.createBitmap(spriteSheetStrawberry, 1134, 547, 357, 318);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 381, 86, 210, 235);
    }

    private void initImageOfTomato() {
        Bitmap spriteSheetTomato = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_tomato);
        imageBracket01 = Bitmap.createBitmap(spriteSheetTomato, 97, 203, 244, 588);
        imageBracket02 = Bitmap.createBitmap(spriteSheetTomato, 278, 203, 382, 588);
        imageBracket03 = Bitmap.createBitmap(spriteSheetTomato, 643, 203, 382, 588);
        imageBracket04 = Bitmap.createBitmap(spriteSheetTomato, 1048, 203, 382, 588);

        Bitmap spriteSheetEntityPlantHarvestables = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.entity_plant_harvestables);
        imageHarvested = Bitmap.createBitmap(spriteSheetEntityPlantHarvestables, 647, 680, 204, 203);
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
        } else if (ageInDays >= BRACKET04_MIN_INCLUSIVE) {
            harvestable = true;
            image = imageBracket04;
        } else {
            Log.e(TAG, "updateImageBasedOnAgeInDays() else-clause");
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
    public boolean respondToItemCollisionViaClick(Item item) {
        return false;
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

            Rect rectOnScreen = null;
            if (game.getSceneManager().getCurrentScene() instanceof SceneHothouse) {
                rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, -(Tile.HEIGHT / 2)));
            } else {
                rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));
            }

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
