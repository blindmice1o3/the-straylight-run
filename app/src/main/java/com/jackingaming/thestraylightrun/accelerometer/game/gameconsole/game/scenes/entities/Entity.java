package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.io.Serializable;

public abstract class Entity
        implements Carryable, Serializable {
    transient protected Game game;

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    transient protected Rect bounds;
    transient protected Bitmap image;
    protected boolean active;

    public Entity(int xSpawn, int ySpawn) {
        x = xSpawn;
        y = ySpawn;
        width = Tile.WIDTH;
        height = Tile.HEIGHT;
        active = true;
    }

    public void init(Game game) {
        this.game = game;
        bounds = new Rect(0, 0, width, height);
    }

    public abstract void update(long elapsed);

    public abstract boolean respondToEntityCollision(Entity e);

    public abstract void respondToItemCollisionViaClick(Item item);

    public abstract void respondToItemCollisionViaMove(Item item);

    public abstract void respondToTransferPointCollision(String key);

    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        if (image != null) {
            Rect rectOfImage = new Rect(0, 0, image.getWidth(), image.getHeight());
            Rect rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(getCollisionBounds(0, 0));

            canvas.drawBitmap(image, rectOfImage, rectOnScreen, paintLightingColorFilter);
        }
    }

    public boolean checkItemCollision(float xOffset, float yOffset, boolean viaClick) {
        for (Item item : game.getSceneManager().getCurrentScene().getItemManager().getItems()) {
            if (item.getCollisionBounds(0f, 0f).intersect(getCollisionBounds(xOffset, yOffset))) {
                if (viaClick) {
                    respondToItemCollisionViaClick(item);
                } else {
                    respondToItemCollisionViaMove(item);
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkTransferPointCollision(float xOffset, float yOffset) {
        for (String key : game.getSceneManager().getCurrentScene().getTileManager().getTransferPointsKeySet()) {
            Rect transferPointBounds = game.getSceneManager().getCurrentScene().getTileManager().getTransferPointBounds(key);

            if (transferPointBounds.intersect(getCollisionBounds(xOffset, yOffset))) {
                respondToTransferPointCollision(key);
                return true;
            }
        }
        return false;
    }

    protected boolean skipEntityCollisionCheck(Entity e) {
        return e.equals(this);
    }

    public boolean checkEntityCollision(float xOffset, float yOffset) {
        for (Entity e : game.getSceneManager().getCurrentScene().getEntityManager().getEntities()) {
            if (skipEntityCollisionCheck(e)) {
                continue;
            }

            if (e.getCollisionBounds(0f, 0f).intersect(getCollisionBounds(xOffset, yOffset))) {
                boolean hadCollided = respondToEntityCollision(e);
                return hadCollided;
            }
        }
        return false;
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
        return false;
    }

    @Override
    public void becomeCarried() {
        active = false;
    }

    @Override
    public boolean becomeNotCarried(Tile tileToLandOn) {
        active = true;
        x = tileToLandOn.getxIndex() * Tile.WIDTH;
        y = tileToLandOn.getyIndex() * Tile.HEIGHT;
        return game.getSceneManager().getCurrentScene().getEntityManager().addEntity(this);
    }

    @Override
    public void moveWithCarrier(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}