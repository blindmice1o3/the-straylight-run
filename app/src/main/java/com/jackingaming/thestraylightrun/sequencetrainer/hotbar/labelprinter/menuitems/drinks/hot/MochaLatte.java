package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.FillSteamingPitcherDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.WhippedCream;

import java.util.List;

public class MochaLatte extends Drink {
    public MochaLatte() {
        super(MochaLatte.class.getSimpleName());
    }

    @Override
    protected void initDrinkProperties() {
        // not needed for MochaLatte.
    }

    @Override
    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();

        int numberOfSyrupMocha = -1;
        int numberOfShots = -1;
        int amountOfMilk = -1;
        int temperatureMilk = 160;
        int timeFrothedMilk = 3;

        switch (size) {
            case TRENTA:
                numberOfSyrupMocha = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_COLD:
                numberOfSyrupMocha = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_HOT:
                numberOfSyrupMocha = 5;
                numberOfShots = 2;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET4;
                break;
            case GRANDE:
                numberOfSyrupMocha = 4;
                numberOfShots = 2;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET3;
                break;
            case TALL:
                numberOfSyrupMocha = 3;
                numberOfShots = 1;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET2;
                break;
            case SHORT:
                numberOfSyrupMocha = 2;
                numberOfShots = 1;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET1;
                break;
        }

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
