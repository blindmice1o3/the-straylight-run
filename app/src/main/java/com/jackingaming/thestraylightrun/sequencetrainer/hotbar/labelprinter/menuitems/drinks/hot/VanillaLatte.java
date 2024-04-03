package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.hot;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteSyrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

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

        if (cupImageView.getMilk() == null) {
            return false;
        }

        if (cupImageView.getMilk().getTemperature() < 160) {
            return false;
        }

        if (cupImageView.getMilk().getTimeFrothed() < 3 || cupImageView.getMilk().getTimeFrothed() > 5) {
            return false;
        }

        if (size.equals("venti")) {
            if (cupImageView.getMilk().getAmount() < (20 * 4)) {
                return false;
            }
        } else if (size.equals("grande")) {
            if (cupImageView.getMilk().getAmount() < (16 * 4)) {
                return false;
            }
        } else if (size.equals("tall")) {
            if (cupImageView.getMilk().getAmount() < (12 * 4)) {
                return false;
            }
        } else if (size.equals("short")) {
            if (cupImageView.getMilk().getAmount() < (8 * 4)) {
                return false;
            }
        }

        if (shotCustom == -1) {
            // standard shots
            if (cupImageView.getShots().size() == shotStandard) {
                if (syrupCustom == -1) {
                    // standard syrups
                    if (cupImageView.getSyrupsMap().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrupsMap().get(Syrup.Type.VANILLA).size();

                        if (quantitySyrupVanilla == syrupStandard) {
                            return true;
                        }
                    }
                } else {
                    // custom syrups
                    if (cupImageView.getSyrupsMap().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrupsMap().get(Syrup.Type.VANILLA).size();

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
                    if (cupImageView.getSyrupsMap().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrupsMap().get(Syrup.Type.VANILLA).size();

                        if (quantitySyrupVanilla == syrupStandard) {
                            return true;
                        }
                    }
                } else {
                    // custom syrups
                    if (cupImageView.getSyrupsMap().containsKey(Syrup.Type.VANILLA)) {
                        int quantitySyrupVanilla = cupImageView.getSyrupsMap().get(Syrup.Type.VANILLA).size();

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
