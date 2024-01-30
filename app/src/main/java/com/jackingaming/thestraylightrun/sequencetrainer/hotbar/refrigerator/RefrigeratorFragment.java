package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.entities.Milk;

public class RefrigeratorFragment extends Fragment {
    public static final String TAG = RefrigeratorFragment.class.getSimpleName();

    public static final String TAG_MILK_TWO_PERCENT = "twoPercent";
    public static final String TAG_MILK_WHOLE = "whole";
    public static final String TAG_MILK_OAT = "oat";
    public static final String TAG_MILK_COCONUT = "coconut";
    public static final String TAG_MILK_ALMOND = "almond";
    public static final String TAG_MILK_SOY = "soy";

    private ConstraintLayout clRefrigerator;
    private Milk milkTwoPercent, milkWhole;
    private Milk milkOat, milkCoconut, milkAlmond, milkSoy;

    public static RefrigeratorFragment newInstance() {
        Log.e(TAG, "newInstance()");
        return new RefrigeratorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_refrigerator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        clRefrigerator = view.findViewById(R.id.constraintlayout_refrigerator);

        milkTwoPercent = view.findViewById(R.id.milk_two_percent);
        milkWhole = view.findViewById(R.id.milk_whole);
        milkOat = view.findViewById(R.id.milk_oat);
        milkCoconut = view.findViewById(R.id.milk_coconut);
        milkAlmond = view.findViewById(R.id.milk_almond);
        milkSoy = view.findViewById(R.id.milk_soy);

        milkTwoPercent.setTag(TAG_MILK_TWO_PERCENT);
        milkWhole.setTag(TAG_MILK_WHOLE);
        milkOat.setTag(TAG_MILK_OAT);
        milkCoconut.setTag(TAG_MILK_COCONUT);
        milkAlmond.setTag(TAG_MILK_ALMOND);
        milkSoy.setTag(TAG_MILK_SOY);
    }
}