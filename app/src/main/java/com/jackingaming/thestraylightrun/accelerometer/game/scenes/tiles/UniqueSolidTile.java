package com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles;

import android.graphics.Bitmap;

public class UniqueSolidTile extends SolidTile {

    public static final String COMPUTER = "computer";
    public static final String GAME_CONSOLE = "game console";
    public static final String TELEVISION = "television";

    private String id;

    public UniqueSolidTile(String id, Bitmap texture) {
        super(texture);

        this.id = id;
    }

    public String getId() {
        return id;
    }
}
