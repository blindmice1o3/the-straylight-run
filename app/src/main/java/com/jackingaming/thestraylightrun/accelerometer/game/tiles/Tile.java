package com.jackingaming.thestraylightrun.accelerometer.game.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {

    // STATIC STUFF

    public static int widthSpriteDst;
    public static int heightSpriteDst;

    // CLASS

    protected Bitmap texture;

    public Tile(Bitmap texture) {
        this.texture = texture;
    }

    public static void init(int widthSpriteDst, int heightSpriteDst) {
        Tile.widthSpriteDst = widthSpriteDst;
        Tile.heightSpriteDst = heightSpriteDst;
    }

    public void tick() {

    }

    public void render(Canvas canvas, int x, int y) {
        Rect rectOfImage = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        Rect rectOnScreen = new Rect(x, y, (x + widthSpriteDst), (y + heightSpriteDst));

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        canvas.drawBitmap(texture, rectOfImage, rectOnScreen, null);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    }

    public boolean isSolid() {
        return false;
    }

}
