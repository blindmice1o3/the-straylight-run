package com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {
    protected Bitmap image;
    private Rect rectOfImage;

    public Tile(Bitmap image) {
        this.image = image;
        rectOfImage = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    public void tick() {

    }

    public void render(Canvas canvas, int x, int y, int widthSpriteDst, int heightSpriteDst) {
        Rect rectOnScreen = new Rect(x, y, (x + widthSpriteDst), (y + heightSpriteDst));

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        canvas.drawBitmap(image, rectOfImage, rectOnScreen, null);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    }

    public boolean isSolid() {
        return false;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
