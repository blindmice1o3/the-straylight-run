package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.jackingaming.thestraylightrun.R;

public class EspressoShot extends AppCompatImageView {
    public static final String TAG = EspressoShot.class.getSimpleName();

    public enum Type {BLONDE, SIGNATURE, DECAF;}

    public enum AmountOfWater {RISTRETTO, STANDARD, LONG;}

    public enum AmountOfBean {HALF_DECAF, STANDARD, UPDOSED;}

    private Type type;
    private AmountOfWater amountOfWater;
    private AmountOfBean amountOfBean;
    private boolean collided;

    public EspressoShot(@NonNull Context context) {
        super(context);
    }

    public EspressoShot(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateShot(Type typeSelected,
                           AmountOfWater amountOfWaterSelected, AmountOfBean amountOfBeanSelected) {
        type = typeSelected;

        int colorToUse = lookupColorIdByType(type);
        setBackgroundColor(getResources().getColor(colorToUse));

        amountOfWater = amountOfWaterSelected;
        amountOfBean = amountOfBeanSelected;
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

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
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
