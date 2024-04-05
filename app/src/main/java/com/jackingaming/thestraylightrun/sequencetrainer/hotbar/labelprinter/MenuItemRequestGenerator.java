package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;

import java.util.Random;

public class MenuItemRequestGenerator {
    private static Random random = new Random();

    public static Drink requestRandomDrink() {
        // randomly select drink
        int indexRandomDrink = 6;
//        int indexRandomDrink = random.nextInt(Menu.queryNumberOfDrinksOnMenu());

        // randomly select size
        int indexRandomSize = random.nextInt(Drink.Size.values().length);

        // iced drinks
        if (indexRandomDrink == 7 ||
                indexRandomDrink == 8 ||
                indexRandomDrink == 9 ||
                indexRandomDrink == 10 ||
                indexRandomDrink == 11 ||
                indexRandomDrink == 12) {
            // ICED: shouldn't have SHORT for size, replace with GRANDE.
            if (Drink.Size.values()[indexRandomSize] == Drink.Size.SHORT) {
                indexRandomSize = Drink.Size.GRANDE.ordinal();
            }
            // ICED: shouldn't have VENTI_HOT for size, replace with VENTI_COLD.
            else if (Drink.Size.values()[indexRandomSize] == Drink.Size.VENTI_HOT) {
                indexRandomSize = Drink.Size.VENTI_COLD.ordinal();
            }
            // ICED (espresso-based): shouldn't have TRENTA for size, replace with GRANDE.
            else if (Drink.Size.values()[indexRandomSize] == Drink.Size.TRENTA) {
                indexRandomSize = Drink.Size.GRANDE.ordinal();
            }
        }
        // hot drinks
        else {
            // HOT: shouldn't have TRENTA for size, replace with GRANDE.
            if (Drink.Size.values()[indexRandomSize] == Drink.Size.TRENTA) {
                indexRandomSize = Drink.Size.GRANDE.ordinal();
            }
            // HOT: shouldn't have VENTI_COLD for size, replace with VENTI_HOT.
            else if (Drink.Size.values()[indexRandomSize] == Drink.Size.VENTI_COLD) {
                indexRandomSize = Drink.Size.VENTI_HOT.ordinal();
            }
        }

        // set size on menu item
        Drink drinkRandom = Menu.getDrinkByIndex(indexRandomDrink);
        drinkRandom.setSize(Drink.Size.values()[indexRandomSize]);

        return drinkRandom;
    }

    public static Drink requestRandomCustomizedDrink() {
        Drink drinkRandom = requestRandomDrink();
        generateCustomizationsForDrink(drinkRandom);
        return drinkRandom;
    }

    private static final int CUSTOMIZE_SYRUP = 0;
    private static final int CUSTOMIZE_SHOT = 1;
    private static final int CUSTOMIZE_BOTH = 2;

    private static void generateCustomizationsForDrink(Drink drink) {
        int randomCustomization = random.nextInt(3);

        if (randomCustomization == CUSTOMIZE_SYRUP) {
            generateRandomNumberOfSyrupForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_SHOT) {
            generateRandomNumberOfShotForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_BOTH) {
            generateRandomNumberOfSyrupForDrink(drink);
            generateRandomNumberOfShotForDrink(drink);
        }
    }

    private static void generateRandomNumberOfSyrupForDrink(Drink drink) {
        int maxNumberOfPumpsPlusOne = (6 + 1); // get random number of syrup pumps [0-6].
        int syrupVanilla = random.nextInt(maxNumberOfPumpsPlusOne);

        drink.addCustomization("vanilla: " + syrupVanilla);
    }

    private static void generateRandomNumberOfShotForDrink(Drink drink) {
        int maxNumberOfShotPlusOne = (4 + 1); // get random number of espresso shots [0-4].
        int shot = random.nextInt(maxNumberOfShotPlusOne);

        drink.addCustomization("shot: " + shot);
    }
}
