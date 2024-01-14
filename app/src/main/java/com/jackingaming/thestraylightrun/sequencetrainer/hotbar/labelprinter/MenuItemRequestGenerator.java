package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;

import java.util.Random;

public class MenuItemRequestGenerator {
    private static Random random = new Random();

    public static Drink requestRandomDrink() {
        // randomly select drink
        int indexRandomDrink = random.nextInt(Menu.queryNumberOfDrinksOnMenu());

        // randomly select size
        int indexRandomSize = random.nextInt(Drink.Size.values().length);

        // set size on menu item
        Drink drinkRandom = Menu.getDrinkByIndex(indexRandomDrink);
        drinkRandom.setSize(Drink.Size.values()[indexRandomSize]);

        return drinkRandom;
    }
}
