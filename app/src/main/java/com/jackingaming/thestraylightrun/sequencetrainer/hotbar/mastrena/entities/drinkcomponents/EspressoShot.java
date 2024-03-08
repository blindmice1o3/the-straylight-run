package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jackingaming.thestraylightrun.R;

public class EspressoShot extends DrinkComponent {
    public static final String TAG = EspressoShot.class.getSimpleName();

    public enum Type {BLONDE, SIGNATURE, DECAF;}

    public enum AmountOfWater {RISTRETTO, STANDARD, LONG;}

    public enum AmountOfBean {HALF_DECAF, STANDARD, UPDOSED;}

    private Type type = Type.SIGNATURE;
    private AmountOfWater amountOfWater = AmountOfWater.STANDARD;
    private AmountOfBean amountOfBean = AmountOfBean.STANDARD;

    public EspressoShot(@NonNull Context context) {
        super(context);
    }

    public EspressoShot(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Type type,
                     AmountOfWater amountOfWater, AmountOfBean amountOfBean) {
        this.type = type;

        int colorToUse = lookupColorIdByType(type);
        setBackgroundColor(getResources().getColor(colorToUse));

        this.amountOfWater = amountOfWater;
        this.amountOfBean = amountOfBean;
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

    public static int lookupColorIdByType(Type type) {
        switch (type) {
            case BLONDE:
                return R.color.green;
            case SIGNATURE:
                return R.color.light_blue_900;
            case DECAF:
                return R.color.teal_200;
            default:
                return R.color.red;
        }
    }
}
