package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game;

import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.io.Serializable;
import java.util.Random;

public class GameCamera
        implements Serializable {
    private enum State {STABLE, SHAKING;}

    public static final int CLIP_WIDTH_IN_TILE_DEFAULT = 8;
    public static final int CLIP_HEIGHT_IN_TILE_DEFAULT = 8;

    private static GameCamera uniqueInstance;
    private State state;
    transient private Random random;

    private Entity entity;
    private float x;
    private float y;

    private int widthScene;
    private int heightScene;

    private int clipWidthInTile;
    private int clipHeightInTile;
    private int clipWidthInPixel;
    private int clipHeightInPixel;
    transient private int widthViewport;
    transient private int heightViewport;
    private float widthPixelToViewportRatio;
    private float heightPixelToViewportRatio;

    private GameCamera() {
        state = State.STABLE;
        x = 0f;
        y = 0f;
        xOffset = 0f;
        yOffset = 0f;
        clipWidthInTile = CLIP_WIDTH_IN_TILE_DEFAULT;
        clipHeightInTile = CLIP_HEIGHT_IN_TILE_DEFAULT;
    }

    public static GameCamera getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameCamera();
        }
        return uniqueInstance;
    }

    public void init(Entity entity, int widthViewport, int heightViewport, int widthScene, int heightScene) {
        this.entity = entity;
        // Want to use the values-newly-assigned-by-OS for widthViewport and heightViewport when loading.
        this.widthViewport = widthViewport;
        this.heightViewport = heightViewport;
        this.widthScene = widthScene;
        this.heightScene = heightScene;

        updateWidthPixelToViewportRatio();
        updateHeightPixelToViewportRatio();

        random = new Random();
        timer = 0;

        update(0L);
    }

    private static final int TIMER_TARGET_SHAKING_IN_MILLI = 500;
    private int timer;

    public void update(long elapsed) {
        if (state == State.STABLE) {
            centerOnEntity();
            doNotMoveOffScreen();
        } else if (state == State.SHAKING) {
            timer += elapsed;
            if (timer >= TIMER_TARGET_SHAKING_IN_MILLI) {
                timer = 0;

                xOffset = 0f;
                yOffset = 0f;
                state = State.STABLE;
            }

            updateShaking();
            centerOnEntity();
        }
    }

    public void startShaking() {
        state = State.SHAKING;
    }

    private static final int MAGNITUDE_MAX = 2;
    private float xOffset;
    private float yOffset;

    private void updateShaking() {
        // Randomly assign 1 or -1.
        int xDirection = random.nextInt(2) * 2 - 1;
        int yDirection = random.nextInt(2) * 2 - 1;
        int magnitude = random.nextInt(MAGNITUDE_MAX);

        xOffset += (xDirection * magnitude);
        yOffset += (yDirection * magnitude);
    }

    private void centerOnEntity() {
        //get entity's xCenter, subtract half of clipWidthInPixel.
        x = (entity.getX() + (entity.getWidth() / 2f)) - (clipWidthInPixel / 2f) + xOffset;
        //get entity's yCenter, subtract half of clipHeightInPixel.
        y = (entity.getY() + (entity.getHeight() / 2f)) - (clipHeightInPixel / 2f) + yOffset;
    }

    private void doNotMoveOffScreen() {
        //LEFT
        if (x < 0) {
            x = 0;
        }
        //RIGHT
        else if ((x + clipWidthInPixel) > widthScene) {
            x = (widthScene - clipWidthInPixel);
        }

        //TOP
        if (y < 0) {
            y = 0;
        }
        //BOTTOM
        else if ((y + clipHeightInPixel) > heightScene) {
            y = (heightScene - clipHeightInPixel);
        }
    }

    public Rect convertInGameRectToScreenRect(Rect collisionBounds) {
        Rect screenRect = new Rect(
                (int) ((collisionBounds.left - x) * widthPixelToViewportRatio),
                (int) ((collisionBounds.top - y) * heightPixelToViewportRatio),
                (int) ((collisionBounds.right - x) * widthPixelToViewportRatio),
                (int) ((collisionBounds.bottom - y) * heightPixelToViewportRatio));
        return screenRect;
    }

    public Rect convertScreenRectToInGameRect(Rect screenRect) {
        Rect collisionBounds = new Rect(
                (int) ((screenRect.left / widthPixelToViewportRatio) + x),
                (int) ((screenRect.top / heightPixelToViewportRatio) + y),
                (int) ((screenRect.right / widthPixelToViewportRatio) + x),
                (int) ((screenRect.bottom / heightPixelToViewportRatio) + y));
        return collisionBounds;
    }

    // TODO: [updateShaking] GameCamera on successful viewport-entity collision (bring in DeadCow).


    public float convertInGameXPositionToScreenXPosition(float xInGame) {
        return (xInGame - x) * widthPixelToViewportRatio;
    }

    public float convertInGameYPositionToScreenYPosition(float yInGame) {
        return (yInGame - y) * heightPixelToViewportRatio;
    }

    public float convertScreenXPositionToInGameXPosition(float xOnScreen) {
        return (xOnScreen / widthPixelToViewportRatio) + x;
    }

    public float convertScreenYPositionToInGameYPosition(float yOnScreen) {
        return (yOnScreen / heightPixelToViewportRatio) + y;
    }

    public void updateClipWidthInTile(int clipWidthInTile) {
        this.clipWidthInTile = clipWidthInTile;
        updateWidthPixelToViewportRatio();
    }

    public void updateClipHeightInTile(int clipHeightInTile) {
        this.clipHeightInTile = clipHeightInTile;
        updateHeightPixelToViewportRatio();
    }

    public void updateWidthPixelToViewportRatio() {
        clipWidthInPixel = clipWidthInTile * Tile.WIDTH;
        widthPixelToViewportRatio = widthViewport / (float) clipWidthInPixel;
    }

    public void updateHeightPixelToViewportRatio() {
        clipHeightInPixel = clipHeightInTile * Tile.HEIGHT;
        heightPixelToViewportRatio = heightViewport / (float) clipHeightInPixel;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getClipWidthInTile() {
        return clipWidthInTile;
    }

    public int getClipHeightInTile() {
        return clipHeightInTile;
    }

    public int getClipWidthInPixel() {
        return clipWidthInPixel;
    }

    public int getClipHeightInPixel() {
        return clipHeightInPixel;
    }

    public float getWidthPixelToViewportRatio() {
        return widthPixelToViewportRatio;
    }

    public float getHeightPixelToViewportRatio() {
        return heightPixelToViewportRatio;
    }
}
