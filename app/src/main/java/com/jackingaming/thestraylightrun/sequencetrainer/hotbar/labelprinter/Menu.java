package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private static List<Drink> drinks;

    static {
        drinks = new ArrayList<>();
        drinks.add(new Drink("CaffeLatte"));
        drinks.add(new Drink("VanillaLatte"));
        drinks.add(new Drink("CaramelMacchiato"));
    }

    public static int queryNumberOfDrinksOnMenu() {
        return drinks.size();
    }

    public static Drink getDrinkByIndex(int index) {
        return drinks.get(index);
    }
}
