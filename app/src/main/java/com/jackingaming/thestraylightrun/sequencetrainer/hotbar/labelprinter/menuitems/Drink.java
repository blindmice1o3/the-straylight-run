package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

import java.util.ArrayList;
import java.util.List;

public abstract class Drink extends MenuItem {
    public enum Size {TRENTA, VENTI_COLD, VENTI_HOT, GRANDE, TALL, SHORT;}

    protected Size size = Size.GRANDE;
    private List<String> customizations = new ArrayList<>();

    ////////////////////////////////////////////////////////
    public enum Property {SHOT_ON_TOP;}

    protected List<DrinkComponent> drinkComponents = new ArrayList<>();
    protected List<Property> drinkProperties = new ArrayList<>();

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

    abstract public boolean validate(CupImageView cupImageView,
                                     String size, List<String> customizations);

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
