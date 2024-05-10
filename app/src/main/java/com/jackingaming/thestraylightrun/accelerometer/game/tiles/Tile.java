package com.jackingaming.thestraylightrun.accelerometer.game.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {

    // STATIC STUFF

    public static int widthTile;
    public static int heightTile;

    // CLASS

    protected Bitmap texture;

    public Tile(Bitmap texture) {
        this.texture = texture;
    }

    public static void init(int widthTile, int heightTile) {
        Tile.widthTile = widthTile;
        Tile.heightTile = heightTile;
    }

    public void tick() {

    }

    public void render(Canvas canvas, int x, int y) {
        Rect rectOfImage = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        Rect rectOnScreen = new Rect(x, y, (x + widthTile), (y + heightTile));

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        canvas.drawBitmap(texture, rectOfImage, rectOnScreen, null);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    }

    public boolean isSolid() {
        return false;
    }

}
