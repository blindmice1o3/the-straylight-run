package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.CaffeLatte;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Cappuccino;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.CaramelMacchiato;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.FlatWhite;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.VanillaLatte;

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
    }

    public static int queryNumberOfDrinksOnMenu() {
        return drinks.size();
    }

    public static Drink getDrinkByIndex(int index) {
        Drink drinkToCopy = drinks.get(index);
        Drink copy = createCopyOfDrink(drinkToCopy);
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

    public static Drink getDrinkByName(String name) {
        for (Drink drink : drinks) {
            if (drink.getName().equals(name)) {
                return drink;
            }
        }

        return null;
    }
}
