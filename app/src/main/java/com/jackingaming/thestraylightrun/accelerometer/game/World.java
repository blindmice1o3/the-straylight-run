package com.jackingaming.thestraylightrun.accelerometer.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.TileMapLoader;

public class World extends FrameLayout {

    private GameActivity game;
    private int width, height;
    private int[][] tiles;

    public World(@NonNull Context context) {
        super(context);
    }

    public World(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(GameActivity game, int resId) {
        this.game = game;
        loadWorld(R.raw.world01);
    }

    public void loadWorld(int resId) {
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(game.getResources(), resId);
        Log.e("WORLD", stringOfTilesIDs);
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs);
        width = tiles.length;
        height = tiles[0].length;
    }

    public Tile getTile(int x, int y) {
        int idTile = tiles[x][y];
        Tile tile = Tile.tiles[idTile];

        if (tile == null) {
            return Tile.walkableTile;
        }

        return tile;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("WORLD", "onDraw()");

        canvas.drawColor(
                getResources().getColor(R.color.purple_700)
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                getTile(x, y).render(canvas,
                        (int) (x * Tile.widthTile - game.getGameCamera().getxOffset()),
                        (int) (y * Tile.heightTile - game.getGameCamera().getyOffset()));
            }
        }
    }
}
