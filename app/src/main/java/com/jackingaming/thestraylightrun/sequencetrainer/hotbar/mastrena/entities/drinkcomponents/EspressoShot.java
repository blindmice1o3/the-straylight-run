package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans.EspressoShotRequest;

public class EspressoShot extends DrinkComponent {
    public static final String TAG = EspressoShot.class.getSimpleName();

    public enum Type {BLONDE, SIGNATURE, DECAF;}

    public enum AmountOfWater {RISTRETTO, STANDARD, LONG;}

    public enum AmountOfBean {HALF_DECAF, STANDARD, UPDOSED;}

    private Type type = Type.SIGNATURE;
    private AmountOfWater amountOfWater = AmountOfWater.STANDARD;
    private AmountOfBean amountOfBean = AmountOfBean.STANDARD;

    public EspressoShot() {
        super();
    }

    public EspressoShot(Type type, AmountOfWater amountOfWater, AmountOfBean amountOfBean) {
        super();

        this.type = type;
        this.amountOfWater = amountOfWater;
        this.amountOfBean = amountOfBean;
    }

    public EspressoShot(EspressoShotRequest espressoShotRequest) {
        super();

        this.type = espressoShotRequest.getType();
        this.amountOfWater = espressoShotRequest.getAmountOfWater();
        this.amountOfBean = espressoShotRequest.getAmountOfBean();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public AmountOfWater getAmountOfWater() {
        return amountOfWater;
    }

    public void setAmountOfWater(AmountOfWater amountOfWater) {
        this.amountOfWater = amountOfWater;
    }

    public AmountOfBean getAmountOfBean() {
        return amountOfBean;
    }

    public void setAmountOfBean(AmountOfBean amountOfBean) {
        this.amountOfBean = amountOfBean;
    }

    @NonNull
    @Override
    public String toString() {
        String abbreviationOfPropeties = type.name().charAt(0) + " " +
                amountOfWater.name().charAt(0) + " " +
                amountOfBean.name().charAt(0);

        if (shaken) {
            abbreviationOfPropeties += " shaken";
        }
        if (blended) {
            abbreviationOfPropeties += " blended";
        }

        return abbreviationOfPropeties;
    }
}
