package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;

import java.io.Serializable;

public class Tile
        implements Serializable {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;

    transient private Game game;

    private String id;
    private boolean walkable = true;
    protected int xIndex;
    protected int yIndex;
    transient protected Bitmap image;

    private int x0InScene;
    private int y0InScene;
    private int x1InScene;
    private int y1InScene;
    transient private Rect collisionBoundsInScene;

    public Tile(String id) {
        this.id = id;
    }

    public void init(Game game, int xIndex, int yIndex, Bitmap image) {
        this.game = game;
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.image = image;

        x0InScene = xIndex * Tile.WIDTH;
        y0InScene = yIndex * Tile.HEIGHT;
        x1InScene = x0InScene + Tile.WIDTH;
        y1InScene = y0InScene + Tile.HEIGHT;
        collisionBoundsInScene = new Rect(x0InScene, y0InScene, x1InScene, y1InScene);
    }

    public void draw(Canvas canvas) {
        if (image != null) {
            Rect rectOfImage = new Rect(0, 0, image.getWidth(), image.getHeight());
            Rect rectOnScreen = GameCamera.getInstance().convertInGameRectToScreenRect(collisionBoundsInScene);

            canvas.drawBitmap(image, rectOfImage, rectOnScreen, null);
        }
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public String getId() {
        return id;
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }
}