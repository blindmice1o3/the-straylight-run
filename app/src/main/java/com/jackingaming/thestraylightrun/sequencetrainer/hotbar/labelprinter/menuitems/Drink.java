package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;

public abstract class Drink extends MenuItem {
    public enum Size {VENTI, GRANDE, TALL, SHORT;}

    private Size size;

    public Drink(String name) {
        super(name);
    }

    abstract public boolean validate(String size, CupImageView cupImageView);

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
