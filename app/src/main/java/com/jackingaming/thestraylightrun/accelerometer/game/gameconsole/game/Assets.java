package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

public class Assets {
    public static final String TAG = Assets.class.getSimpleName();

    public static Bitmap unwateredTilledOutdoor, wateredTilledOutdoor;
    public static Bitmap unwateredSeededOutdoor, wateredSeededOutdoor;

    public static Bitmap unwateredTilledIndoor, wateredTilledIndoor;
    public static Bitmap unwateredSeededIndoor, wateredSeededIndoor;

    public static Bitmap shippingBinQuadrantTopLeft, shippingBinQuadrantTopRight,
            shippingBinQuadrantBottomLeft, shippingBinQuadrantBottomRight;

    public static void init(Resources resources) {
        Bitmap spriteSheetCropsAndItems = BitmapFactory.decodeResource(resources, R.drawable.items_and_tiles);
        unwateredTilledOutdoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 58, 55, 277, 272);
        unwateredSeededOutdoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 712, 56, 275, 271);
        wateredTilledOutdoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 378, 56, 274, 271);
        wateredSeededOutdoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 1032, 56, 274, 271);

        unwateredTilledIndoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 1352, 680, 242, 239);
        unwateredSeededIndoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 1045, 681, 242, 238);
        wateredTilledIndoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 1354, 375, 242, 241);
        wateredSeededIndoor = Bitmap.createBitmap(spriteSheetCropsAndItems, 1047, 376, 242, 241);

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
