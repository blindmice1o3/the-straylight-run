package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;

import java.util.List;

public class UndefinedDrink extends Drink {
    public UndefinedDrink() {
        super(UndefinedDrink.class.getSimpleName());
    }

    @Override
    protected void initDrinkProperties() {
        // not needed for UndefinedDrink.
    }

    @Override
    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();
        return drinkComponents;
    }
}
