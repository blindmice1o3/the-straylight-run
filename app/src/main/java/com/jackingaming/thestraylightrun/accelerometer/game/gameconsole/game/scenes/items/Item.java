package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Carryable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.io.Serializable;

public abstract class Item
        implements Carryable, Serializable {
    transient protected Game game;

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    transient protected Rect bounds;
    transient protected Bitmap image;
    protected float price;

    protected String name;

    public Item() {
        x = 0;
        y = 0;
        width = Tile.WIDTH;
        height = Tile.HEIGHT;
        price = -1f;
    }

    public void init(Game game) {
        this.game = game;
        bounds = new Rect(0, 0, width, height);
        initName();
        initPrice();
        initImage();
    }

    protected abstract void initName();

    protected abstract void initPrice();

    protected abstract void initImage();

    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        if (image != null) {
            Rect rectOfImage = new Rect(0, 0, image.getWidth(), image.getHeight());
            Rect rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));

            canvas.drawBitmap(image, rectOfImage, rectOnScreen, paintLightingColorFilter);
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Rect getCollisionBounds(float xOffset, float yOffset) {
        return new Rect(
                (int) (x + bounds.left + xOffset),
                (int) (y + bounds.top + yOffset),
                (int) (x + bounds.left + xOffset) + bounds.right,
                (int) (y + bounds.top + yOffset) + bounds.bottom);
    }

    @Override
    public boolean isCarryable() {
        return true;
    }

    @Override
    public Carryable becomeCarried() {
        // Intentionally blank.
        game.getSceneManager().getCurrentScene().getItemManager().removeItem(this);

        return this;
    }

    @Override
    public boolean becomeNotCarried(Tile tileToLandOn) {
        x = tileToLandOn.getxIndex() * Tile.WIDTH;
        y = tileToLandOn.getyIndex() * Tile.HEIGHT;
        return game.getSceneManager().getCurrentScene().getItemManager().addItem(this);
    }

    @Override
    public void moveWithCarrier(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }
}