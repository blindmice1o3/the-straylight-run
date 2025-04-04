package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

import java.io.Serializable;

public class Package
        implements Serializable {
    private String name;

    public Package(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
