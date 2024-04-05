package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Drizzle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.List;

public class IcedCaramelMacchiato extends Drink {
    public IcedCaramelMacchiato() {
        super(IcedCaramelMacchiato.class.getSimpleName());

        initDrinkProperties();
    }

    private void initDrinkProperties() {
        drinkProperties.add(Property.SHOT_ON_TOP);
    }

    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();

        int numberOfSyrupVanilla = -1;
        int numberOfShots = -1;
        int amountOfMilk = -1;
        int temperatureMilk = RefrigeratorFragment.TEMPERATURE;
        int timeFrothedMilk = 0;

        switch (size) {
            case TRENTA:
                numberOfSyrupVanilla = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_COLD:
                numberOfSyrupVanilla = 5;
                numberOfShots = 3;
                amountOfMilk = 100;
                break;
            case VENTI_HOT:
                numberOfSyrupVanilla = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case GRANDE:
                numberOfSyrupVanilla = 3;
                numberOfShots = 2;
                amountOfMilk = 100;
                break;
            case TALL:
                numberOfSyrupVanilla = 2;
                numberOfShots = 1;
                amountOfMilk = 100;
                break;
            case SHORT:
                numberOfSyrupVanilla = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
        }

        // ICE
        drinkComponents.add(new Ice());
        // DRIZZLE
        drinkComponents.add(new Drizzle(Drizzle.Type.CARAMEL));
        // SYRUPS
        for (int i = 0; i < numberOfSyrupVanilla; i++) {
            drinkComponents.add(new Syrup(Syrup.Type.VANILLA));
        }
        // ESPRESSO SHOTS
        for (int i = 0; i < numberOfShots; i++) {
            drinkComponents.add(new EspressoShot(EspressoShot.Type.SIGNATURE, EspressoShot.AmountOfWater.STANDARD, EspressoShot.AmountOfBean.STANDARD));
        }
        // MILK
        drinkComponents.add(new Milk(Milk.Type.TWO_PERCENT, amountOfMilk, temperatureMilk, timeFrothedMilk));

        return drinkComponents;
    }

    @Override
    public boolean validate(CupImageView cupImageView,
                            String size, List<String> customizations) {
        // Convert String size into enum Drink.Size.
        Drink.Size sizeFromLabel = null;
        for (Drink.Size sizeCurrent : Drink.Size.values()) {
            if (size.equals(sizeCurrent.name())) {
                sizeFromLabel = sizeCurrent;
                break;
            }
        }

        List<DrinkComponent> drinkComponentsExpected = getDrinkComponentsBySize(sizeFromLabel);
        List<DrinkComponent> drinkComponentsActual = cupImageView.getDrinkComponentsAsList();

        boolean isSameDrinkComponents = drinkComponentsExpected.equals(drinkComponentsActual);
        boolean isSameDrinkProperties = true;
        if (!drinkProperties.isEmpty()) {
            for (Drink.Property property : drinkProperties) {
                switch (property) {
                    case SHOT_ON_TOP:
                        if (!cupImageView.isShotOnTop()) {
                            isSameDrinkProperties = false;
                        }
                        break;
                }
            }
        }

        return isSameDrinkComponents && isSameDrinkProperties;
    }
}
