package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;

import java.util.ArrayList;
import java.util.List;

public abstract class Drink extends MenuItem {
    public enum Size {VENTI, GRANDE, TALL, SHORT;}

    private Size size;
    private List<String> customizations = new ArrayList<>();

    public Drink(String name) {
        super(name);
    }

    public void addCustomization(String customization) {
        customizations.add(customization);
    }

    public String getCustomizationByIndex(int index) {
        return customizations.get(index);
    }

    public int queryNumberOfCustomizations() {
        return customizations.size();
    }

    abstract public boolean validate(String size, CupImageView cupImageView);

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
