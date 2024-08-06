package com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

import java.util.ArrayList;
import java.util.List;

public class TileSelectorView extends View {
    public static final String TAG = TileSelectorView.class.getSimpleName();

    private TileManager tileManager;
    private Tile[][] tiles;
    private int rows, columns;
    private int widthTileOnScreen, heightTileOnScreen;

    private List<Tile> tilesSelected;
    private Paint paint;

    public TileSelectorView(Context context) {
        super(context);
    }

    public TileSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(TileManager tileManager) {
        this.tileManager = tileManager;

        tiles = tileManager.getTiles();
        rows = tiles.length;
        columns = tiles[0].length;
        Log.e(TAG, "init() rows, columns: " + rows + ", " + columns);

        tilesSelected = new ArrayList<>();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xTouch = (int) event.getX();
        int yTouch = (int) event.getY();

        int xIndexTouch = xTouch / widthTileOnScreen;
        int yIndexTouch = yTouch / heightTileOnScreen;
        Log.e(TAG, "(xIndexTouch, yIndexTouch): (" + xIndexTouch + ", " + yIndexTouch + ")");

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (xIndexTouch < 0 || xIndexTouch >= columns ||
                    yIndexTouch < 0 || yIndexTouch >= rows) {
                return false;
            }

            Tile tile = tiles[yIndexTouch][xIndexTouch];
            if (!tilesSelected.contains(tile)) {
                tilesSelected.add(tile);
                invalidate();
            } else {
                tilesSelected.remove(tile);
                invalidate();
            }
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (widthTileOnScreen == 0 && heightTileOnScreen == 0) {
            widthTileOnScreen = getWidth() / columns;
            heightTileOnScreen = getHeight() / rows;
        }

        for (Tile tile : tilesSelected) {
            int left = tile.getxIndex() * widthTileOnScreen;
            int top = tile.getyIndex() * heightTileOnScreen;
            int right = left + widthTileOnScreen;
            int bottom = top + heightTileOnScreen;
            Rect rect = new Rect(left, top, right, bottom);

            canvas.drawRect(rect, paint);
        }
    }

    public List<Tile> getTilesSelected() {
        return tilesSelected;
    }
}
