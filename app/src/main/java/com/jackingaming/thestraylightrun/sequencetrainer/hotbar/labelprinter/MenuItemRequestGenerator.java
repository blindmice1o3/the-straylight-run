package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import android.util.Log;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;

import java.util.List;
import java.util.Random;

public class MenuItemRequestGenerator {
    public static final String TAG = MenuItemRequestGenerator.class.getSimpleName();

    private static Random random = new Random();

    public static Drink requestRandomDrink() {
        // randomly select drink
        int indexRandomDrink = random.nextInt(Menu.queryNumberOfDrinksOnMenu());

        // randomly select size
        int indexRandomSize = random.nextInt(Drink.Size.values().length);

        // iced drinks
        if (indexRandomDrink == 7 ||
                indexRandomDrink == 8 ||
                indexRandomDrink == 9 ||
                indexRandomDrink == 10 ||
                indexRandomDrink == 11 ||
                indexRandomDrink == 12 ||
                indexRandomDrink == 13) {
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

        // set size for drink
        Drink drinkRandom = Menu.getDrinkByIndex(indexRandomDrink);
        drinkRandom.setSize(Drink.Size.values()[indexRandomSize]);

        // initialize drinkComponents for drink
        drinkRandom.getDrinkComponentsBySize(
                drinkRandom.getSize()
        );

        return drinkRandom;
    }

    public static Drink requestRandomCustomizedDrink() {
        Drink drinkRandom = requestRandomDrink();
        generateCustomizationsForDrink(drinkRandom);
        return drinkRandom;
    }

    private static final int CUSTOMIZE_SYRUP = 0;
    private static final int CUSTOMIZE_MILK = 1;
    private static final int CUSTOMIZE_BOTH = 2;

    private static void generateCustomizationsForDrink(Drink drink) {
        int randomCustomization = random.nextInt(3);

        if (randomCustomization == CUSTOMIZE_SYRUP) {
            generateRandomNumberOfSyrupForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_MILK) {
            generateRandomTypeOfMilkForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_BOTH) {
            generateRandomNumberOfSyrupForDrink(drink);
            generateRandomTypeOfMilkForDrink(drink);
        }
    }

    private static void generateRandomNumberOfSyrupForDrink(Drink drink) {
//        int maxNumberOfPumpsPlusOne = (6 + 1); // get random number of syrup pumps [0-6].
//        int syrupVanilla = random.nextInt(maxNumberOfPumpsPlusOne);
//
//        drink.addCustomization("vanilla: " + syrupVanilla);

        // if drink has syrup in standard recipe... find out what type of syrup.
        List<DrinkComponent> drinkComponents = drink.getDrinkComponents();
        int counterSyrup = 0;
        Syrup.Type type = null;
        boolean shaken = false;
        boolean blended = false;
        for (DrinkComponent drinkComponent : drinkComponents) {
            if (drinkComponent instanceof Syrup) {
                counterSyrup++;
                type = ((Syrup) drinkComponent).getType();
                shaken = ((Syrup) drinkComponent).isShaken();
                blended = ((Syrup) drinkComponent).isBlended();
            }
        }

        boolean isSyrupInStandardRecipe = (counterSyrup > 0);
        Log.e(TAG, drink.getSize().name() + " " + drink.getName());
        Log.e(TAG, "isSyrupInStandardRecipe: " + isSyrupInStandardRecipe);

        // drink does NOT have syrup in standard recipe... pick a random syrup.
        if (!isSyrupInStandardRecipe) {
            int indexRandomSyrupType = random.nextInt(Syrup.Type.values().length);
            type = Syrup.Type.values()[indexRandomSyrupType];
        }

        // generate random number of syrup pumps to use.
        final int NUMBER_OF_PUMPS_MAX = 12;
        int numberOfPumpsRandom = random.nextInt(NUMBER_OF_PUMPS_MAX); // [0-12)
        // numberOfPumpsRandom is same as standard recipe, use NUMBER_OF_PUMP_MAX.
        if (counterSyrup == numberOfPumpsRandom) {
            numberOfPumpsRandom = NUMBER_OF_PUMPS_MAX;
        }

        Log.e(TAG, "numberOfPumpsRandom: " + numberOfPumpsRandom);

        // add pump(s)
        if (numberOfPumpsRandom > counterSyrup) {
            int numberOfPumpsToAdd = numberOfPumpsRandom - counterSyrup;

            int index = -1;
            // find index of first syrup
            if (isSyrupInStandardRecipe) {
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof Syrup) {
                        index = i;
                        break;
                    }
                }
            }
            // not syrup in standard recipe (find espresso shot or milk)
            else {
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof EspressoShot) {
                        index = i;
                        break;
                    } else if (drinkComponents.get(i) instanceof Milk) {
                        index = i;
                        break;
                    }
                }
            }

            for (int i = 0; i < numberOfPumpsToAdd; i++) {
                Syrup syrupToAdd = new Syrup(type);
                syrupToAdd.setShaken(shaken);
                syrupToAdd.setBlended(blended);
                drinkComponents.add(index, syrupToAdd);
                Log.e(TAG, "Add syrup of type: " + type + " (shaken: " + shaken + ") (blended: " + blended + ")");
            }
        }
        // remove pump(s)
        else {
            int numberOfPumpsToRemove = counterSyrup - numberOfPumpsRandom;

            int index = -1;
            // find index of first syrup
            if (isSyrupInStandardRecipe) {
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof Syrup) {
                        index = i;
                        break;
                    }
                }
            }

            for (int i = 0; i < numberOfPumpsToRemove; i++) {
                drinkComponents.remove(index);
                Log.e(TAG, "Remove syrup of type: " + type);
            }
        }

        for (DrinkComponent drinkComponent : drinkComponents) {
            String classOfDrinkComponent = drinkComponent.getClass().getSimpleName();
            if (drinkComponent instanceof Syrup) {
                classOfDrinkComponent += (" " + ((Syrup) drinkComponent).getType());
            }
            Log.e(TAG, classOfDrinkComponent);
        }
    }

    private static void generateRandomTypeOfMilkForDrink(Drink drink) {
//        int maxNumberOfShotPlusOne = (4 + 1); // get random number of espresso shots [0-4].
//        int shot = random.nextInt(maxNumberOfShotPlusOne);
//
//        drink.addCustomization("shot: " + shot);

        List<DrinkComponent> drinkComponents = drink.getDrinkComponents();
        // determine Milk.Type in standard recipe.
        Milk.Type typeStandard = null;
        int indexMilk = -1;
        for (int i = 0; i < drinkComponents.size(); i++) {
            DrinkComponent drinkComponent = drinkComponents.get(i);
            if (drinkComponent instanceof Milk) {
                typeStandard = ((Milk) drinkComponent).getType();
                indexMilk = i;
            }
        }
        // select new random Milk.Type.
        int indexRandomMilkType = random.nextInt(Milk.Type.values().length);
        Milk.Type typeRandom = Milk.Type.values()[indexRandomMilkType];

        if (typeRandom == typeStandard) {
            typeRandom = (typeStandard != Milk.Type.OAT) ?
                    Milk.Type.OAT : Milk.Type.SOY;

        }

        if (indexMilk != -1) {
            ((Milk) drinkComponents.get(indexMilk)).setType(typeRandom);
        } else {
            Log.e(TAG, "indexMilk == -1... meaning did not find milk in standard recipe.");
        }
    }
}
