package com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles;

import android.graphics.Bitmap;

public class TransferPointTile extends WalkableTile {

    private String idSceneDestination;

    public TransferPointTile(Bitmap texture, String idSceneDestination) {
        super(texture);

        this.idSceneDestination = idSceneDestination;
    }

    public String getIdSceneDestination() {
        return idSceneDestination;
    }
}
