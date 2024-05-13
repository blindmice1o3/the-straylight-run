package com.jackingaming.thestraylightrun.accelerometer.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.OnSwipeListener;

public class World extends FrameLayout {

    private GameActivity game;
    private int width, height;
    private Tile[][] tiles;
    private GestureDetector gestureDetector;

    public World(@NonNull Context context) {
        super(context);
    }

    public World(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void init(GameActivity game, int resId, OnSwipeListener bottomDrawerSwipeListener) {
        this.game = game;
//        loadWorld(R.raw.world01);
        loadWorld(R.raw.tiles_world_map);
        gestureDetector = new GestureDetector(getContext(), bottomDrawerSwipeListener);
    }

    public void loadWorld(int resId) {
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(game.getResources(), resId);
        Log.e("WORLD", stringOfTilesIDs);
        Bitmap fullWorldMap = BitmapFactory.decodeResource(getResources(),
                R.drawable.pokemon_gsc_kanto);
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, fullWorldMap);
        width = tiles.length;
        height = tiles[0].length;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(
                getResources().getColor(R.color.purple_700)
        );

        int xStart = (int) Math.max(0, game.getGameCamera().getxOffset() / Tile.widthTile);
        int xEnd = (int) Math.min(width, ((game.getGameCamera().getxOffset() + game.getxScreenSize()) / Tile.widthTile) + 1);
        int yStart = (int) Math.max(0, game.getGameCamera().getyOffset() / Tile.heightTile);
        int yEnd = (int) Math.min(height, ((game.getGameCamera().getyOffset() + game.getyScreenSize()) / Tile.heightTile) + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                getTile(x, y).render(canvas,
                        (int) (x * Tile.widthTile - game.getGameCamera().getxOffset()),
                        (int) (y * Tile.heightTile - game.getGameCamera().getyOffset()));
            }
        }
    }

    public int getWorldWidthInPixels() {
        return width * Tile.widthTile;
    }

    public int getWorldHeightInPixels() {
        return height * Tile.heightTile;
    }
}
