package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.List;

public class IcedVanillaLatte extends Drink {
    public IcedVanillaLatte() {
        super(IcedVanillaLatte.class.getSimpleName());
    }

    @Override
    protected void initDrinkProperties() {
        // not needed for IcedVanillaLatte.
    }

    @Override
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
                numberOfSyrupVanilla = 6;
                numberOfShots = 3;
                amountOfMilk = 100;
                break;
            case VENTI_HOT:
                numberOfSyrupVanilla = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case GRANDE:
                numberOfSyrupVanilla = 4;
                numberOfShots = 2;
                amountOfMilk = 100;
                break;
            case TALL:
                numberOfSyrupVanilla = 3;
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
}
