package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowSystemPart;

import java.io.Serializable;
import java.util.List;

public class GrowSystemPartsDataCarrier
        implements Serializable {
    private List<GrowSystemPart> growSystemParts;

    public GrowSystemPartsDataCarrier(List<GrowSystemPart> growSystemParts) {
        this.growSystemParts = growSystemParts;
    }

    public List<GrowSystemPart> getGrowSystemParts() {
        return growSystemParts;
    }
}
