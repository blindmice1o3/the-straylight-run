package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import java.io.Serializable;
import java.util.List;

public class DrinkComponentListHolder
        implements Serializable {

    private List<DrinkComponent> drinkComponents;

    public DrinkComponentListHolder(List<DrinkComponent> drinkComponents) {
        this.drinkComponents = drinkComponents;
    }

    public List<DrinkComponent> getDrinkComponents() {
        return drinkComponents;
    }

    public void setDrinkComponents(List<DrinkComponent> drinkComponents) {
        this.drinkComponents = drinkComponents;
    }
}
