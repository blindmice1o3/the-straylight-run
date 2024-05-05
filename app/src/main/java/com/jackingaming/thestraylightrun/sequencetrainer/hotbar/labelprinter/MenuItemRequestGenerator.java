package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import android.util.Log;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupCaddyFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
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

    private static final int CUSTOMIZE_MILK = 0;
    private static final int CUSTOMIZE_SYRUP = 1;
    private static final int CUSTOMIZE_SHOT = 2;
    private static final int CUSTOMIZE_MULTIPLE_COMPONENTS = 3;

    private static void generateCustomizationsForDrink(Drink drink) {
        int randomCustomization = random.nextInt(4);

        if (randomCustomization == CUSTOMIZE_MILK) {
            generateRandomTypeOfMilkForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_SYRUP) {
            generateRandomNumberOfSyrupForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_SHOT) {
            generateRandomNumberOfShotForDrink(drink);
        } else if (randomCustomization == CUSTOMIZE_MULTIPLE_COMPONENTS) {
            int randomCustomizationMultipleComponents = random.nextInt(4);

            if (randomCustomizationMultipleComponents == 0) {
                generateRandomTypeOfMilkForDrink(drink);
                generateRandomNumberOfSyrupForDrink(drink);
            } else if (randomCustomizationMultipleComponents == 1) {
                generateRandomTypeOfMilkForDrink(drink);
                generateRandomNumberOfShotForDrink(drink);
            } else if (randomCustomizationMultipleComponents == 2) {
                generateRandomNumberOfSyrupForDrink(drink);
                generateRandomNumberOfShotForDrink(drink);
            } else if (randomCustomizationMultipleComponents == 3) {
                generateRandomTypeOfMilkForDrink(drink);
                generateRandomNumberOfSyrupForDrink(drink);
                generateRandomNumberOfShotForDrink(drink);
            }
        }

        int customizeCupSize = random.nextInt(4);
        // 25% chance to specify cup size.
        if (customizeCupSize == 0) {
            generateRandomCupSize(drink);
        }
    }

    private static void generateRandomCupSize(Drink drink) {
        Log.e(TAG, "drink size: " + drink.getSize().name());
        int indexRandom = -1;
        String cupSizeSpecified = null;
        switch (drink.getSize()) {
            case TRENTA:
                // Intentionally blank.
                break;
            case VENTI_COLD:
                cupSizeSpecified = CupCaddyFragment.TAG_COLD_TRENTA;
                break;
            case VENTI_HOT:
                // Intentionally blank.
                break;
            case GRANDE:
                boolean isIcedDrink = false;
                for (DrinkComponent drinkComponent : drink.getDrinkComponents()) {
                    if (drinkComponent instanceof Ice) {
                        isIcedDrink = true;
                    }
                }
                if (isIcedDrink) {
                    indexRandom = random.nextInt(2);
                    if (indexRandom == 0) {
                        cupSizeSpecified = CupCaddyFragment.TAG_COLD_VENTI;
                    } else if (indexRandom == 1) {
                        cupSizeSpecified = CupCaddyFragment.TAG_COLD_TRENTA;
                    }
                } else {
                    cupSizeSpecified = CupCaddyFragment.TAG_HOT_VENTI;
                }
                break;
            case TALL:
                isIcedDrink = false;
                for (DrinkComponent drinkComponent : drink.getDrinkComponents()) {
                    if (drinkComponent instanceof Ice) {
                        isIcedDrink = true;
                    }
                }
                if (isIcedDrink) {
                    indexRandom = random.nextInt(2);
                    if (indexRandom == 0) {
                        cupSizeSpecified = CupCaddyFragment.TAG_COLD_GRANDE;
                    } else if (indexRandom == 1) {
                        cupSizeSpecified = CupCaddyFragment.TAG_COLD_VENTI;
                    } else if (indexRandom == 2) {
                        cupSizeSpecified = CupCaddyFragment.TAG_COLD_TRENTA;
                    }
                } else {
                    indexRandom = random.nextInt(1);
                    if (indexRandom == 0) {
                        cupSizeSpecified = CupCaddyFragment.TAG_HOT_GRANDE;
                    } else if (indexRandom == 1) {
                        cupSizeSpecified = CupCaddyFragment.TAG_HOT_VENTI;
                    }
                }
                break;
            case SHORT:
                indexRandom = random.nextInt(3);
                if (indexRandom == 0) {
                    cupSizeSpecified = CupCaddyFragment.TAG_HOT_TALL;
                } else if (indexRandom == 1) {
                    cupSizeSpecified = CupCaddyFragment.TAG_HOT_GRANDE;
                } else if (indexRandom == 2) {
                    cupSizeSpecified = CupCaddyFragment.TAG_HOT_VENTI;
                }
                break;
        }

        if (cupSizeSpecified != null) {
            drink.getDrinkProperties().put(Drink.Property.CUP_SIZE_SPECIFIED, cupSizeSpecified);
        }
    }

    private static void generateRandomNumberOfShotForDrink(Drink drink) {
        int numberOfShotsTotal = random.nextInt(4) + 1; // [1-4]
        Log.e(TAG, "numberOfShotsTotal:" + numberOfShotsTotal);

        // count how many shots are in the standard recipe.
        int counterShot = 0;
        EspressoShot shot = null;
        int indexFirstOccurrence = -1;
        for (int i = 0; i < drink.getDrinkComponents().size(); i++) {
            DrinkComponent drinkComponent = drink.getDrinkComponents().get(i);
            if (drinkComponent instanceof EspressoShot) {
                counterShot++;

                if (indexFirstOccurrence < 0) {
                    indexFirstOccurrence = i;
                }
                shot = (EspressoShot) drinkComponent;
            }
        }

        if (counterShot == numberOfShotsTotal) {
            numberOfShotsTotal++;
        }

        // add shot(s)
        if (counterShot < numberOfShotsTotal) {
            int numberOfShotsToAdd = numberOfShotsTotal - counterShot;

            boolean isShotInStandardRecipe = (shot != null);
            // shot in standard recipe
            if (isShotInStandardRecipe) {
                for (int i = 0; i < numberOfShotsToAdd; i++) {
                    EspressoShot shotToAdd = new EspressoShot(shot.getType(),
                            shot.getAmountOfWater(),
                            shot.getAmountOfBean());
                    shotToAdd.setShaken(shot.isShaken());
                    shotToAdd.setBlended(shot.isBlended());
                    drink.getDrinkComponents().add(indexFirstOccurrence, shotToAdd);
                }
            }
            // no shot in standard recipe (find milk)
            else {
                Milk milk = null;
                int indexToAdd = -1;
                for (int i = 0; i < drink.getDrinkComponents().size(); i++) {
                    if (drink.getDrinkComponents().get(i) instanceof Milk) {
                        milk = (Milk) drink.getDrinkComponents().get(i);
                        indexToAdd = i;
                        break;
                    }
                }

                int indexTypeRandom = random.nextInt(EspressoShot.Type.values().length);
                EspressoShot.Type type = EspressoShot.Type.values()[indexTypeRandom];
                for (int i = 0; i < numberOfShotsToAdd; i++) {
                    EspressoShot shotToAdd = new EspressoShot(type,
                            EspressoShot.AmountOfWater.STANDARD,
                            EspressoShot.AmountOfBean.STANDARD);
                    shotToAdd.setShaken(milk.isShaken());
                    shotToAdd.setBlended(milk.isBlended());
                    drink.getDrinkComponents().add(indexToAdd, shotToAdd);
                }
            }
        }
        // remove shot(s)
        else {
            int numberOfShotsToRemove = counterShot - numberOfShotsTotal;

            for (int i = 0; i < numberOfShotsToRemove; i++) {
                drink.getDrinkComponents().remove(indexFirstOccurrence);
            }
        }
    }

    private static void generateRandomNumberOfSyrupForDrink(Drink drink) {
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
                shaken = drinkComponent.isShaken();
                blended = drinkComponent.isBlended();
            }
        }

        boolean isSyrupInStandardRecipe = counterSyrup > 0;
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
        if (counterSyrup < numberOfPumpsRandom) {
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
        List<DrinkComponent> drinkComponents = drink.getDrinkComponents();

        // determine Milk.Type in standard recipe.
        Milk milkStandard = null;
        for (int i = 0; i < drinkComponents.size(); i++) {
            DrinkComponent drinkComponent = drinkComponents.get(i);
            if (drinkComponent instanceof Milk) {
                milkStandard = (Milk) drinkComponent;
                break;
            }
        }

        Milk.Type typeStandard = null;
        if (milkStandard != null) {
            typeStandard = milkStandard.getType();

            // select new random Milk.Type.
            int indexRandomMilkType = random.nextInt(Milk.Type.values().length);
            Milk.Type typeRandom = Milk.Type.values()[indexRandomMilkType];

            if (typeRandom == typeStandard) {
                typeRandom = (typeStandard != Milk.Type.OAT) ?
                        Milk.Type.OAT : Milk.Type.SOY;
            }

            milkStandard.setType(typeRandom);
        } else {
            Log.e(TAG, "milkStandard == null... meaning did not find milk in standard recipe.");
        }
    }
}
