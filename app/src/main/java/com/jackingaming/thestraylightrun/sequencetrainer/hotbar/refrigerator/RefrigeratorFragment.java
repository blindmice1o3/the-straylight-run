package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

public class RefrigeratorFragment extends Fragment {
    public static final String TAG_DEBUG = RefrigeratorFragment.class.getSimpleName();

    private ConstraintLayout clRefrigerator;
    private View viewCoconutMilk, viewAlmondMilk, viewSoyMilk;

    public static RefrigeratorFragment newInstance() {
        Log.e(TAG_DEBUG, "newInstance()");
        return new RefrigeratorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG_DEBUG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_refrigerator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG_DEBUG, "onViewCreated()");

        clRefrigerator = view.findViewById(R.id.clRefrigerator);

        viewCoconutMilk = view.findViewById(R.id.milk_coconut);
        viewCoconutMilk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(getContext(), "COCONUT", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        viewAlmondMilk = view.findViewById(R.id.milk_almond);
        viewAlmondMilk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(getContext(), "ALMOND", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        viewSoyMilk = view.findViewById(R.id.milk_soy);
        viewSoyMilk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(getContext(), "SOY", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}