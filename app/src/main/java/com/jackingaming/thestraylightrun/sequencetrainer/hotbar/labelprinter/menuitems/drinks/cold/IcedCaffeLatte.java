package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.List;

public class IcedCaffeLatte extends Drink {
    public IcedCaffeLatte() {
        super(IcedCaffeLatte.class.getSimpleName());
    }

    private void initDrinkProperties() {
        // TODO: (not needed for IcedCaffeLatte)
    }

    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();

        int numberOfShots = -1;
        int amountOfMilk = -1;
        int temperatureMilk = RefrigeratorFragment.TEMPERATURE;
        int timeFrothedMilk = 0;

        switch (size) {
            case TRENTA:
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_COLD:
                numberOfShots = 3;
                amountOfMilk = 100;
                break;
            case VENTI_HOT:
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case GRANDE:
                numberOfShots = 2;
                amountOfMilk = 100;
                break;
            case TALL:
                numberOfShots = 1;
                amountOfMilk = 100;
                break;
            case SHORT:
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
        }

        // ICE
        drinkComponents.add(new Ice());
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

        return drinkComponentsExpected.equals(drinkComponentsActual);
    }
}
