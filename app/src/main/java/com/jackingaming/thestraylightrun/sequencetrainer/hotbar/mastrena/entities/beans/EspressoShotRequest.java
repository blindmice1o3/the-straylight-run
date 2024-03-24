package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;

public class EspressoShotRequest {
    private int quantity;
    private EspressoShot.Type type;
    private EspressoShot.AmountOfWater amountOfWater;
    private EspressoShot.AmountOfBean amountOfBean;

    public EspressoShotRequest(int quantity, EspressoShot.Type type, EspressoShot.AmountOfWater amountOfWater, EspressoShot.AmountOfBean amountOfBean) {
        this.quantity = quantity;
        this.type = type;
        this.amountOfWater = amountOfWater;
        this.amountOfBean = amountOfBean;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EspressoShot.Type getType() {
        return type;
    }

    public void setType(EspressoShot.Type type) {
        this.type = type;
    }

    public EspressoShot.AmountOfWater getAmountOfWater() {
        return amountOfWater;
    }

    public void setAmountOfWater(EspressoShot.AmountOfWater amountOfWater) {
        this.amountOfWater = amountOfWater;
    }

    public EspressoShot.AmountOfBean getAmountOfBean() {
        return amountOfBean;
    }

    public void setAmountOfBean(EspressoShot.AmountOfBean amountOfBean) {
        this.amountOfBean = amountOfBean;
    }
}
