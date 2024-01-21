package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.Syrup;

public class CaramelMacchiato extends Drink {
    public CaramelMacchiato() {
        super(CaramelMacchiato.class.getSimpleName());
    }

    @Override
    public boolean validate(String size, CupImageView cupImageView) {
        if (cupImageView.getTemperature() >= 160 &&
                (cupImageView.getTimeFrothed() >= 3 && cupImageView.getTimeFrothed() <= 5)) {
            if (size.equals("trenta")) {
                // blank.
            } else if (size.equals("venti")) {
                if (cupImageView.getNumberOfShots() == 2 && (cupImageView.getAmount() >= (20 * 4))) {
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == 4 && cupImageView.isShotOnTop() && cupImageView.isDrizzled()) {
                            return true;
                        }
                    }
                }
            } else if (size.equals("grande")) {
                if (cupImageView.getNumberOfShots() == 2 && (cupImageView.getAmount() >= (16 * 4))) {
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == 3 && cupImageView.isShotOnTop() && cupImageView.isDrizzled()) {
                            return true;
                        }
                    }
                }
            } else if (size.equals("tall")) {
                if (cupImageView.getNumberOfShots() == 1 && (cupImageView.getAmount() >= (12 * 4))) {
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == 2 && cupImageView.isShotOnTop() && cupImageView.isDrizzled()) {
                            return true;
                        }
                    }
                }
            } else if (size.equals("short")) {
                if (cupImageView.getNumberOfShots() == 1 && (cupImageView.getAmount() >= (8 * 4))) {
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == 1 && cupImageView.isShotOnTop() && cupImageView.isDrizzled()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
