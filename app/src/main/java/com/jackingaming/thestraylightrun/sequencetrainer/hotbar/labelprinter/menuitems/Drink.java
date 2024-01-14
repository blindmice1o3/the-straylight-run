package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

public class Drink extends MenuItem {
    public enum Size {VENTI, GRANDE, TALL, SHORT;}

    private Size size;

    public Drink(String name) {
        super(name);
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
