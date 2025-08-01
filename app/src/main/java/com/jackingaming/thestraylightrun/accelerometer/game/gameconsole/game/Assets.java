package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

public class Assets {
    public static final String TAG = Assets.class.getSimpleName();

    public static Bitmap unwateredTilled, wateredTilled;
    public static Bitmap unwateredSeeded, wateredSeeded;
    public static Bitmap shippingBinQuadrantTopLeft, shippingBinQuadrantTopRight,
            shippingBinQuadrantBottomLeft, shippingBinQuadrantBottomRight;

    public static void init(Resources resources) {
        Bitmap spriteSheetCropsAndItems = BitmapFactory.decodeResource(resources, R.drawable.items_and_tiles);
        unwateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 58, 55, 277, 272);
        unwateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 712, 56, 275, 271);
        wateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 378, 56, 274, 271);
        wateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 1032, 56, 274, 271);
//        unwateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 59, 53, 280, 275);
//        unwateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 698, 53, 280, 275);
//        wateredTilled = Bitmap.createBitmap(spriteSheetCropsAndItems, 378, 53, 280, 275);
//        wateredSeeded = Bitmap.createBitmap(spriteSheetCropsAndItems, 1032, 53, 280, 275);

        shippingBinQuadrantTopLeft = cropImageShippingBinTile(resources, ShippingBinTile.Quadrant.TOP_LEFT);
        shippingBinQuadrantTopRight = cropImageShippingBinTile(resources, ShippingBinTile.Quadrant.TOP_RIGHT);
        shippingBinQuadrantBottomLeft = cropImageShippingBinTile(resources, ShippingBinTile.Quadrant.BOTTOM_LEFT);
        shippingBinQuadrantBottomRight = cropImageShippingBinTile(resources, ShippingBinTile.Quadrant.BOTTOM_RIGHT);
    }

    public static Bitmap cropImageShippingBinTile(Resources resources, ShippingBinTile.Quadrant quadrant) {
        Log.d(TAG, "SceneFarm.cropImageShippingBinTile(Resources, ShippingBinTile.Quadrant)");

        Bitmap customTilesSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.shipping_bin128x128);
//        Bitmap customTilesSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.custom_hm_tile_sprites_sheet);
        Bitmap shippingBinTile = null;

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        switch (quadrant) {
            case TOP_LEFT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 0, 0, 64, 64);
                break;
            case TOP_RIGHT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 64, 0, 64, 64);
                break;
            case BOTTOM_LEFT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 0, 64, 64, 64);
                break;
            case BOTTOM_RIGHT:
                shippingBinTile = Bitmap.createBitmap(customTilesSpriteSheet, 64, 64, 64, 64);
                break;
        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "shippingBinTile: " + shippingBinTile.getWidth() + ", " + shippingBinTile.getHeight());

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //May be redundant because local variable.
        customTilesSpriteSheet = null;
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        Log.d(TAG, "SceneFarm.cropImageShippingBinTile(Resources, ShippingBinTile.Quadrant)... customTilesSpriteSheet is null? " + customTilesSpriteSheet);

        return shippingBinTile;
    }
}
