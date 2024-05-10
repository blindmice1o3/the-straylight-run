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
}
