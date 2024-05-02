package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.UndefinedDrink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.ColdMilk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.IcedBrownSugarOatmilkShakenEspresso;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.IcedCaffeLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.IcedCaramelMacchiato;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.IcedFlatWhite;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.IcedMochaLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold.IcedVanillaLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.CaffeLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.Cappuccino;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.CaramelMacchiato;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.FlatWhite;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.HotChocolate;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.MochaLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot.VanillaLatte;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        drinks.add(new FlatWhite());
        drinks.add(new MochaLatte());
        drinks.add(new HotChocolate());
        drinks.add(new IcedBrownSugarOatmilkShakenEspresso());
        drinks.add(new IcedCaffeLatte());
        drinks.add(new IcedVanillaLatte());
        drinks.add(new IcedCaramelMacchiato());
        drinks.add(new IcedFlatWhite());
        drinks.add(new IcedMochaLatte());
        drinks.add(new ColdMilk());
    }

    public static int queryNumberOfDrinksOnMenu() {
        return drinks.size();
    }

    public static Drink getDrinkByIndex(int index) {
        Drink drinkToCopy = drinks.get(index);
        Drink copy = createCopyOfDrink(drinkToCopy);
        return copy;
    }

    public static Drink getDrinkByName(String name) {
        Drink drinkToCopy = null;
        for (Drink drink : drinks) {
            if (drink.getName().equals(name)) {
                drinkToCopy = drink;
                break;
            }
        }

        Drink copy = (drinkToCopy != null) ?
                createCopyOfDrink(drinkToCopy) : new UndefinedDrink();

        return copy;
    }

    private static Drink createCopyOfDrink(Drink drink) {
        Drink original = drink;
        Drink copy = null;
        try {
            // Serialize the object
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            // Deserialize the object
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (Drink) ois.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return copy;
    }
}
