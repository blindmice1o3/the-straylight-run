package com.jackingaming.thestraylightrun.accelerometer.game.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {

    // STATIC STUFF

    public static int widthTile;
    public static int heightTile;

    public static Tile[] tiles = new Tile[256];
    public static Tile solidTile;
    public static Tile walkableTile;
    public static Tile boulderTile;
    public static final int ID_SOLID = 0;
    public static final int ID_WALKABLE = 1;
    public static final int ID_BOULDER = 2;

    // CLASS

    protected Bitmap texture;
    protected final int id;

    public Tile(Bitmap texture, int id) {
        this.texture = texture;
        this.id = id;

        tiles[id] = this;
    }

    public static void init(int widthTile, int heightTile, Bitmap spriteSolid, Bitmap spriteWalkable, Bitmap spriteBoulder) {
        Tile.widthTile = widthTile;
        Tile.heightTile = heightTile;

        Tile.solidTile = new SolidTile(spriteSolid, Tile.ID_SOLID);
        Tile.walkableTile = new WalkableTile(spriteWalkable, Tile.ID_WALKABLE);
        Tile.boulderTile = new BoulderTile(spriteBoulder, Tile.ID_BOULDER);
    }

    public void tick() {

    }

    public void render(Canvas canvas, int x, int y) {
        Rect rectOfImage = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        Rect rectOnScreen = new Rect(x, y, (x + widthTile), (y + heightTile));

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        canvas.drawBitmap(texture, rectOfImage, rectOnScreen, null);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    }

    public boolean isSolid() {
        return false;
    }

}
