package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans.EspressoShotRequest;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;

public class SpriteEspressoShot extends AppCompatImageView {
    public static final String TAG = SpriteEspressoShot.class.getSimpleName();

    private EspressoShotRequest espressoShotRequest;

    public SpriteEspressoShot(@NonNull Context context) {
        super(context);
    }

    public SpriteEspressoShot(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(EspressoShotRequest espressoShotRequest) {
        this.espressoShotRequest = espressoShotRequest;

        int colorToUse = lookupColorIdByType(espressoShotRequest.getType());
        setBackgroundColor(getResources().getColor(colorToUse));
    }

    public EspressoShot instantiateEspressoShot() {
        return new EspressoShot(espressoShotRequest);
    }

    public static int lookupColorIdByType(EspressoShot.Type type) {
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
