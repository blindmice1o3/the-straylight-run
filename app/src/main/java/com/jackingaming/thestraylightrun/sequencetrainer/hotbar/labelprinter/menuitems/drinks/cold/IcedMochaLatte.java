package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.WhippedCream;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.List;

public class IcedMochaLatte extends Drink {
    public IcedMochaLatte() {
        super(IcedMochaLatte.class.getSimpleName());
    }

    @Override
    protected void initDrinkProperties() {
        // TODO: (not needed for IcedMochaLatte)
    }

    @Override
    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();

        int numberOfSyrupMocha = -1;
        int numberOfShots = -1;
        int amountOfMilk = -1;
        int temperatureMilk = RefrigeratorFragment.TEMPERATURE;
        int timeFrothedMilk = 0;

        switch (size) {
            case TRENTA:
                numberOfSyrupMocha = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_COLD:
                numberOfSyrupMocha = 6;
                numberOfShots = 3;
                amountOfMilk = 100;
                break;
            case VENTI_HOT:
                numberOfSyrupMocha = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case GRANDE:
                numberOfSyrupMocha = 4;
                numberOfShots = 2;
                amountOfMilk = 100;
                break;
            case TALL:
                numberOfSyrupMocha = 3;
                numberOfShots = 1;
                amountOfMilk = 100;
                break;
            case SHORT:
                numberOfSyrupMocha = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
        }

        // ICE
        drinkComponents.add(new Ice());
        // WHIPPED CREAM
        drinkComponents.add(new WhippedCream());
        // SYRUPS
        for (int i = 0; i < numberOfSyrupMocha; i++) {
            drinkComponents.add(new Syrup(Syrup.Type.MOCHA));
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
