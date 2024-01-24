package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

import java.io.Serializable;

public class MenuItem
        implements Serializable {
    private String name;

    public MenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
