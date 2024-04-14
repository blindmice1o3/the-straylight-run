package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import androidx.annotation.NonNull;

import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans.EspressoShotRequest;

import java.util.Objects;

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
        String textType = (type != Type.SIGNATURE) ? type.name() : "      ";
        String textAmountOfWater = null;
        switch (amountOfWater) {
            case RISTRETTO:
                textAmountOfWater = "RIST";
                break;
            case STANDARD:
                textAmountOfWater = "    ";
                break;
            case LONG:
                textAmountOfWater = "LONG";
                break;
        }
        String textAmountOfBean = null;
        switch (amountOfBean) {
            case HALF_DECAF:
                textAmountOfBean = "1/2 DECAF";
                break;
            case STANDARD:
                textAmountOfBean = "    ";
                break;
            case UPDOSED:
                textAmountOfBean = "UPDOSED";
                break;
        }

        String espressoShotPrettyPrint = textType;
        if (amountOfWater != AmountOfWater.STANDARD &&
                amountOfBean != AmountOfBean.STANDARD) {
            espressoShotPrettyPrint += "\n(amountOfWater:" + textAmountOfWater + ") (amountOfBean:" + textAmountOfBean + ")";
        } else if (amountOfWater != AmountOfWater.STANDARD) {
            espressoShotPrettyPrint += "\n(amountOfWater:" + textAmountOfWater + ")";
        } else if (amountOfBean != AmountOfBean.STANDARD) {
            espressoShotPrettyPrint += "\n(amountOfBean:" + textAmountOfBean + ")";
        }
        espressoShotPrettyPrint += "\n(shaken:" + shaken + ") (blended:" + blended + ")";

        return espressoShotPrettyPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EspressoShot)) return false;
        if (!super.equals(o)) return false;
        EspressoShot that = (EspressoShot) o;
        return type == that.type && amountOfWater == that.amountOfWater && amountOfBean == that.amountOfBean;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, amountOfWater, amountOfBean);
    }
}
