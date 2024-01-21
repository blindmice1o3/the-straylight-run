package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.CaffeLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Cappuccino;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.CaramelMacchiato;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.VanillaLatte;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private static List<Drink> drinks;

    static {
        drinks = new ArrayList<>();
        drinks.add(new CaffeLatte());
        drinks.add(new Cappuccino());
        drinks.add(new VanillaLatte());
        drinks.add(new CaramelMacchiato());
    }

    public static int queryNumberOfDrinksOnMenu() {
        return drinks.size();
    }

    public static Drink getDrinkByIndex(int index) {
        return drinks.get(index);
    }

    public static Drink getDrinkByName(String name) {
        for (Drink drink : drinks) {
            if (drink.getName().equals(name)) {
                return drink;
            }
        }

        return null;
    }
}
