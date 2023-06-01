package com.jackingaming.thestraylightrun.nextweektonight.controllers;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.nextweektonight.views.AnimatedLogoView;

public class NextWeekTonightFragment extends Fragment {
    public static final String TAG = NextWeekTonightFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private AnimatedLogoView animatedLogoView;

    public NextWeekTonightFragment() {
        // Required empty public constructor
    }

    public static NextWeekTonightFragment newInstance(String param1, String param2) {
        NextWeekTonightFragment fragment = new NextWeekTonightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_next_week_tonight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");
        animatedLogoView = view.findViewById(R.id.animated_logo_view);
        ObjectAnimator animation = ObjectAnimator.ofFloat(animatedLogoView, "xLogo", 800f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(5000L);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(ObjectAnimator.REVERSE);
        animation.start();
    }
}