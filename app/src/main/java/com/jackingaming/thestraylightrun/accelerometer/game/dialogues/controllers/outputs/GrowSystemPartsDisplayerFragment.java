package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.GrowSystemPartsDataCarrier;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowSystemPart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrowSystemPartsDisplayerFragment extends DialogFragment
        implements Serializable {
    public static final String TAG = GrowSystemPartsDisplayerFragment.class.getSimpleName();
    public static final String ARG_GROW_SYSTEM_PARTS_DATA_CARRIER = "grow_system_parts_data_carrier";
    public static final String ARG_ANIMATION_LISTENER = "animation_listener";

    public interface AnimationListener extends Serializable {
        void onAnimationFinish();
    }

    private GrowSystemPartsDataCarrier growSystemPartsDataCarrier;
    private List<ImageView> ivGrowSystemPartsList;
    private AnimationListener animationListener;

    private ImageView ivGrowSystemParts0, ivGrowSystemParts1, ivGrowSystemParts2,
            ivGrowSystemParts3, ivGrowSystemParts4, ivGrowSystemParts5;

    public static GrowSystemPartsDisplayerFragment newInstance(GrowSystemPartsDataCarrier growSystemPartsDataCarrier, AnimationListener animationListener) {
        GrowSystemPartsDisplayerFragment fragment = new GrowSystemPartsDisplayerFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_GROW_SYSTEM_PARTS_DATA_CARRIER, growSystemPartsDataCarrier);
        args.putSerializable(ARG_ANIMATION_LISTENER, animationListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        if (getArguments() != null) {
            growSystemPartsDataCarrier = (GrowSystemPartsDataCarrier) getArguments().getSerializable(ARG_GROW_SYSTEM_PARTS_DATA_CARRIER);
            animationListener = (AnimationListener) getArguments().getSerializable(ARG_ANIMATION_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_grow_system_parts_displayer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        ivGrowSystemParts0 = view.findViewById(R.id.iv_grow_system_parts_0);
        ivGrowSystemParts1 = view.findViewById(R.id.iv_grow_system_parts_1);
        ivGrowSystemParts2 = view.findViewById(R.id.iv_grow_system_parts_2);
        ivGrowSystemParts3 = view.findViewById(R.id.iv_grow_system_parts_3);
        ivGrowSystemParts4 = view.findViewById(R.id.iv_grow_system_parts_4);
        ivGrowSystemParts5 = view.findViewById(R.id.iv_grow_system_parts_5);

        ivGrowSystemPartsList = new ArrayList<>();
        ivGrowSystemPartsList.add(ivGrowSystemParts0);
        ivGrowSystemPartsList.add(ivGrowSystemParts1);
        ivGrowSystemPartsList.add(ivGrowSystemParts2);
        ivGrowSystemPartsList.add(ivGrowSystemParts3);
        ivGrowSystemPartsList.add(ivGrowSystemParts4);
        ivGrowSystemPartsList.add(ivGrowSystemParts5);

        List<GrowSystemPart> growSystemParts = growSystemPartsDataCarrier.getGrowSystemParts();
        for (int i = 0; i < growSystemParts.size(); i++) {
            ivGrowSystemPartsList.get(i).setImageBitmap(
                    growSystemParts.get(i).getImage()
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    private long elapsedOverall;
    private boolean updatedIndex0, updatedIndex1, updatedIndex2,
            updatedIndex3, updatedIndex4, updatedIndex5;

    public void update(long elapsed) {
        if (ivGrowSystemParts0 == null) {
            return;
        }

        elapsedOverall += elapsed;

        if (elapsedOverall >= 0 && elapsedOverall < 1000 && !updatedIndex0) {
            updatedIndex0 = true;
            ivGrowSystemParts0.animate().setDuration(2500L);
            ivGrowSystemParts0.animate().rotation(360);
        } else if (elapsedOverall >= 1000 && elapsedOverall < 2000 && !updatedIndex1) {
            updatedIndex1 = true;
            ivGrowSystemParts1.animate().setDuration(2500L);
            ivGrowSystemParts1.animate().rotation(360);
        } else if (elapsedOverall >= 2000 && elapsedOverall < 3000 && !updatedIndex2) {
            updatedIndex2 = true;
            ivGrowSystemParts2.animate().setDuration(2500L);
            ivGrowSystemParts2.animate().rotation(360);
        } else if (elapsedOverall >= 3000 && elapsedOverall < 4000 && !updatedIndex3) {
            updatedIndex3 = true;
            ivGrowSystemParts3.animate().setDuration(2500L);
            ivGrowSystemParts3.animate().rotation(360);
        } else if (elapsedOverall >= 4000 && elapsedOverall < 5000 && !updatedIndex4) {
            updatedIndex4 = true;
            ivGrowSystemParts4.animate().setDuration(2500L);
            ivGrowSystemParts4.animate().rotation(360);
        } else if (elapsedOverall >= 5000 && elapsedOverall < 6000 && !updatedIndex5) {
            updatedIndex5 = true;
            ivGrowSystemParts5.animate().setDuration(2500L);
            ivGrowSystemParts5.animate().rotation(360);

            animationListener.onAnimationFinish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}
