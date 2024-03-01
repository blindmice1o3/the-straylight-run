package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.Syrup;

import java.util.List;

public class VanillaLatte extends Drink {
    public VanillaLatte() {
        super(VanillaLatte.class.getSimpleName());
    }

    @Override
    public boolean validate(CupImageView cupImageView,
                            String size, List<String> customizations) {
        int shotCustom = -1;
        int syrupCustom = -1;
        if (customizations != null) {
            for (int i = 0; i < customizations.size(); i++) {
                if (i % 2 == 1) {
                    continue;
                }

                if (customizations.get(i).equals("vanilla:")) {
                    syrupCustom = Integer.parseInt(
                            customizations.get(i + 1)
                    );
                    continue;
                }

                if (customizations.get(i).equals("shot:")) {
                    shotCustom = Integer.parseInt(
                            customizations.get(i + 1)
                    );
                    continue;
                }
            }
        }

        int shotStandard = -1;
        int syrupStandard = -1;
        if (size.equals("venti")) {
            shotStandard = 2;
            syrupStandard = 5;
        } else if (size.equals("grande")) {
            shotStandard = 2;
            syrupStandard = 4;
        } else if (size.equals("tall")) {
            shotStandard = 1;
            syrupStandard = 3;
        } else if (size.equals("short")) {
            shotStandard = 1;
            syrupStandard = 2;
        }

        if (cupImageView.getTemperature() < 160) {
            return false;
        }

        if (cupImageView.getTimeFrothed() < 3 || cupImageView.getTimeFrothed() > 5) {
            return false;
        }

        if (size.equals("venti")) {
            if (cupImageView.getAmount() < (20 * 4)) {
                return false;
            }
        } else if (size.equals("grande")) {
            if (cupImageView.getAmount() < (16 * 4)) {
                return false;
            }
        } else if (size.equals("tall")) {
            if (cupImageView.getAmount() < (12 * 4)) {
                return false;
            }
        } else if (size.equals("short")) {
            if (cupImageView.getAmount() < (8 * 4)) {
                return false;
            }
        }

        if (shotCustom == -1) {
            // standard shots
            if (cupImageView.getShots().size() == shotStandard) {
                if (syrupCustom == -1) {
                    // standard syrups
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == syrupStandard) {
                            return true;
                        }
                    }
                } else {
                    // custom syrups
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == syrupCustom) {
                            return true;
                        }
                    } else if (syrupCustom == 0) {
                        return true;
                    }
                }
            }
        } else {
            // custom shots
            if (cupImageView.getShots().size() == shotCustom) {
                if (syrupCustom == -1) {
                    // standard syrups
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == syrupStandard) {
                            return true;
                        }
                    }
                } else {
                    // custom syrups
                    if (cupImageView.getSyrups().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrups().get(Syrup.Type.VANILLA);

                        if (quantitySyrupVanilla == syrupCustom) {
                            return true;
                        }
                    } else if (syrupCustom == 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
