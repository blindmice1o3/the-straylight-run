package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.FillSteamingPitcherDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;

import java.util.List;

public class CaffeLatte extends Drink {
    public CaffeLatte() {
        super(CaffeLatte.class.getSimpleName());
    }

    @Override
    protected void initDrinkProperties() {
        // not needed for CaffeLatte.
    }

    @Override
    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();

        int numberOfShots = -1;
        int amountOfMilk = -1;
        int temperatureMilk = 160;
        int timeFrothedMilk = 3;

        switch (size) {
            case TRENTA:
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_COLD:
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_HOT:
                numberOfShots = 2;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET4;
                break;
            case GRANDE:
                numberOfShots = 2;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET3;
                break;
            case TALL:
                numberOfShots = 1;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET2;
                break;
            case SHORT:
                numberOfShots = 1;
                amountOfMilk = FillSteamingPitcherDialogFragment.BRACKET1;
                break;
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
