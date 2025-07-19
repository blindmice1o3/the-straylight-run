package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jackingaming.thestraylightrun.R;

public class Assets {
    public static Bitmap unwateredTilled;
    public static Bitmap unwateredSeeded;
    public static Bitmap wateredTilled;
    public static Bitmap wateredSeeded;

    public static void init(Resources resources) {
        Bitmap spriteSheetCropsAndItems = BitmapFactory.decodeResource(resources, R.drawable.items_and_tiles);
        unwateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 58, 55, 277, 272);
        unwateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 712, 56, 275, 271);
        wateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 378, 56, 274, 271);
        wateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 1032,  56, 274, 271);
//        unwateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 59, 53, 280, 275);
//        unwateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 698, 53, 280, 275);
//        wateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 378, 53, 280, 275);
//        wateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 1032, 53, 280, 275);
    }
}
