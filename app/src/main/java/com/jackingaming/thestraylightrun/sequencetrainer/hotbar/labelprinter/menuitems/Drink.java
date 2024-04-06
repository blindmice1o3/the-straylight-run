package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

import java.util.ArrayList;
import java.util.List;

public abstract class Drink extends MenuItem {
    public enum Size {TRENTA, VENTI_COLD, VENTI_HOT, GRANDE, TALL, SHORT;}

    protected Size size = Size.GRANDE;
    private String textForDrinkLabel = null;
    private List<String> customizations = new ArrayList<>();

    ////////////////////////////////////////////////////////
    public enum Property {SHOT_ON_TOP;}

    protected List<DrinkComponent> drinkComponents = new ArrayList<>();
    protected List<Property> drinkProperties = new ArrayList<>();

    public Drink(String name) {
        super(name);
        initDrinkProperties();
    }

    public void addCustomization(String customization) {
        customizations.add(customization);
    }

    public String getCustomizationByIndex(int index) {
        return customizations.get(index);
    }

    public int queryNumberOfCustomizations() {
        return customizations.size();
    }

    abstract protected void initDrinkProperties();

    abstract public List<DrinkComponent> getDrinkComponentsBySize(Size size);

    public boolean validate(CupImageView cupImageView, String size, List<String> customizations) {
        // Convert String size into enum Drink.Size.
        Drink.Size sizeFromLabel = null;
        for (Drink.Size sizeCurrent : Drink.Size.values()) {
            if (size.equals(sizeCurrent.name())) {
                sizeFromLabel = sizeCurrent;
                break;
            }
        }

        List<DrinkComponent> drinkComponentsExpected = drinkComponents;
//        List<DrinkComponent> drinkComponentsExpected = getDrinkComponentsBySize(sizeFromLabel);
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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getTextForDrinkLabel() {
        return textForDrinkLabel;
    }

    public void setTextForDrinkLabel(String textForDrinkLabel) {
        this.textForDrinkLabel = textForDrinkLabel;
    }

    public List<DrinkComponent> getDrinkComponents() {
        return drinkComponents;
    }

    public void setDrinkComponents(List<DrinkComponent> drinkComponents) {
        this.drinkComponents = drinkComponents;
    }

    public List<Property> getDrinkProperties() {
        return drinkProperties;
    }

    public void setDrinkProperties(List<Property> drinkProperties) {
        this.drinkProperties = drinkProperties;
    }
}
