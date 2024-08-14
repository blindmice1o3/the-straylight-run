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
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

import java.util.ArrayList;
import java.util.List;

public class TileSelectorView extends View {
    public static final String TAG = TileSelectorView.class.getSimpleName();
    public static final int COLOR_DEFAULT = Color.GREEN;

    public enum Mode {TILL_SEED_WATER, ONLY_WATER;}

    private Mode mode = Mode.TILL_SEED_WATER;

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
        paint.setColor(COLOR_DEFAULT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int xTouch = (int) event.getX();
            int yTouch = (int) event.getY();
            int xIndexTouch = xTouch / widthTileOnScreen;
            int yIndexTouch = yTouch / heightTileOnScreen;
            Log.e(TAG, "(xIndexTouch, yIndexTouch): (" + xIndexTouch + ", " + yIndexTouch + ")");

            if (xIndexTouch < 0 || xIndexTouch >= columns ||
                    yIndexTouch < 0 || yIndexTouch >= rows) {
                Log.e(TAG, "(xIndexTouch, yIndexTouch) is out of bounds of the tile map.");
                return false;
            }

            Tile tile = tiles[yIndexTouch][xIndexTouch];
            if (tile instanceof GrowableTile) {
                switch (mode) {
                    case TILL_SEED_WATER:
                        if (((GrowableTile) tile).getState() == GrowableTile.State.UNTILLED ||
                                ((GrowableTile) tile).getState() == GrowableTile.State.TILLED) {
                            if (!tilesSelected.contains(tile)) {
                                tilesSelected.add(tile);
                                invalidate();
                            } else {
                                tilesSelected.remove(tile);
                                invalidate();
                            }
                        }
                        break;
                    case ONLY_WATER:
                        if (((GrowableTile) tile).getState() == GrowableTile.State.SEEDED ||
                                ((GrowableTile) tile).getState() == GrowableTile.State.OCCUPIED) {
                            if (!tilesSelected.contains(tile)) {
                                tilesSelected.add(tile);
                                invalidate();
                            } else {
                                tilesSelected.remove(tile);
                                invalidate();
                            }
                        }
                        break;
                    default:
                        Log.e(TAG, "switch() default block: mode NOT defined.");
                        break;
                }
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

        for (Tile[] rows : tiles) {
            for (Tile tile : rows) {
                int color = -1;
                if (tile.isWalkable()) {
                    if (tile instanceof GrowableTile) {
                        switch (((GrowableTile) tile).getState()) {
                            case UNTILLED:
                            case TILLED:
                                color = Color.WHITE;
                                break;
                            case SEEDED:
                            case OCCUPIED:
                                color = Color.MAGENTA;
                                break;
                        }
                    } else {
                        color = Color.YELLOW;
                    }
                } else {
                    color = Color.BLACK;
                }
                paint.setColor(color);

                int left = tile.getxIndex() * widthTileOnScreen;
                int top = tile.getyIndex() * heightTileOnScreen;
                int right = left + widthTileOnScreen;
                int bottom = top + heightTileOnScreen;
                Rect rect = new Rect(left, top, right, bottom);

                canvas.drawRect(rect, paint);
            }
        }

        for (Tile tile : tilesSelected) {
            paint.setColor(COLOR_DEFAULT);

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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
