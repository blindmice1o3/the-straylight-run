package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.cold;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Cinnamon;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.util.List;

public class IcedBrownSugarOatmilkShakenEspresso extends Drink {
    public IcedBrownSugarOatmilkShakenEspresso() {
        super(IcedBrownSugarOatmilkShakenEspresso.class.getSimpleName());
    }

    @Override
    protected void initDrinkProperties() {
        // not needed for IcedBrownSugarOatmilkShakenEspresso.
    }

    @Override
    public List<DrinkComponent> getDrinkComponentsBySize(Size size) {
        drinkComponents.clear();

        int numberOfSyrupBrownSugar = -1;
        int numberOfShots = -1;
        int amountOfMilk = -1;
        int temperatureMilk = RefrigeratorFragment.TEMPERATURE;
        int timeFrothedMilk = 0;

        switch (size) {
            case TRENTA:
                numberOfSyrupBrownSugar = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case VENTI_COLD:
                numberOfSyrupBrownSugar = 6;
                numberOfShots = 4;
                amountOfMilk = 100;
                break;
            case VENTI_HOT:
                numberOfSyrupBrownSugar = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
            case GRANDE:
                numberOfSyrupBrownSugar = 4;
                numberOfShots = 3;
                amountOfMilk = 100;
                break;
            case TALL:
                numberOfSyrupBrownSugar = 3;
                numberOfShots = 2;
                amountOfMilk = 100;
                break;
            case SHORT:
                numberOfSyrupBrownSugar = -1;
                numberOfShots = -1;
                amountOfMilk = -1;
                break;
        }

        // ICE
        Ice ice = new Ice();
        ice.setShaken(true);
        drinkComponents.add(ice);
        // CINNAMON
        Cinnamon cinnamon = new Cinnamon();
        cinnamon.setShaken(true);
        drinkComponents.add(cinnamon);
        // SYRUPS
        for (int i = 0; i < numberOfSyrupBrownSugar; i++) {
            Syrup syrup = new Syrup(Syrup.Type.BROWN_SUGAR);
            syrup.setShaken(true);
            drinkComponents.add(syrup);
        }
        // ESPRESSO SHOTS
        for (int i = 0; i < numberOfShots; i++) {
            EspressoShot espressoShot = new EspressoShot(EspressoShot.Type.BLONDE, EspressoShot.AmountOfWater.STANDARD, EspressoShot.AmountOfBean.STANDARD);
            espressoShot.setShaken(true);
            drinkComponents.add(espressoShot);
        }
        // MILK
        drinkComponents.add(new Milk(Milk.Type.OAT, amountOfMilk, temperatureMilk, timeFrothedMilk));

        return drinkComponents;
    }
}
