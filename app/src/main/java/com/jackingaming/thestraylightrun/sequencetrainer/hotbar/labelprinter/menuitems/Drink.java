package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems;

import android.util.Log;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Drink extends MenuItem {
    public static final String TAG = Drink.class.getSimpleName();

    public enum Size {TRENTA, VENTI_COLD, VENTI_HOT, GRANDE, TALL, SHORT;}

    protected Size size = Size.GRANDE;
    private String textForDrinkLabel = null;

    ////////////////////////////////////////////////////////
    public enum Property {SHOT_ON_TOP, CUP_SIZE_SPECIFIED;}

    protected List<DrinkComponent> drinkComponents = new ArrayList<>();
    protected Map<Property, Object> drinkProperties = new HashMap<>();

    public Drink(String name) {
        super(name);
        initDrinkProperties();
    }

    abstract protected void initDrinkProperties();

    abstract public List<DrinkComponent> getDrinkComponentsBySize(Size size);

    public boolean validate(CupImageView cupImageView) {
        List<DrinkComponent> drinkComponentsExpected = drinkComponents;
        List<DrinkComponent> drinkComponentsActual = cupImageView.getDrinkComponentsAsList();

        Log.e("Drink", "cupImageView's tag: " + cupImageView.getTag());

        boolean isSameDrinkComponents = drinkComponentsExpected.equals(drinkComponentsActual);
        boolean isSameDrinkProperties = true;
        if (!drinkProperties.isEmpty()) {
            for (Drink.Property property : drinkProperties.keySet()) {
                switch (property) {
                    case SHOT_ON_TOP:
                        if ((Boolean) drinkProperties.get(property) !=
                                cupImageView.isShotOnTop()) {
                            isSameDrinkProperties = false;
                        }
                        break;
                    case CUP_SIZE_SPECIFIED:
                        String cupSizeExpected = (String) drinkProperties.get(property);
                        if (!cupSizeExpected.equals(cupImageView.getTag())) {
                            isSameDrinkProperties = false;
                        }
                        break;
                    default:
                        Log.e(TAG, "Drink.validate(), switch(Drink.Property) default.");
                        break;
                }
            }
        }

        // Unspecified cup size
        if (drinkProperties.get(Property.CUP_SIZE_SPECIFIED) == null) {
            boolean isCupSizeAcceptable = size.name().equals(cupImageView.getTag());
            return isSameDrinkComponents && isSameDrinkProperties && isCupSizeAcceptable;
        } else {
            // Specified cup size (already checked in isSameDrinkProperties)
            return isSameDrinkComponents && isSameDrinkProperties;
        }
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

    public Map<Property, Object> getDrinkProperties() {
        return drinkProperties;
    }

    public void setDrinkProperties(Map<Property, Object> drinkProperties) {
        this.drinkProperties = drinkProperties;
    }
}
