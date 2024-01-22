package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;

public class LabelPrinterFragment extends Fragment {
    public static final String TAG = LabelPrinterFragment.class.getSimpleName();

    private FrameLayout frameLayoutLabelPrinter;
    private LabelPrinter labelPrinter;

    public LabelPrinterFragment() {
        // Required empty public constructor
    }

    public static LabelPrinterFragment newInstance() {
        Log.e(TAG, "newInstance()");
        LabelPrinterFragment fragment = new LabelPrinterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label_printer, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        frameLayoutLabelPrinter = view.findViewById(R.id.framelayout_label_printer);
        labelPrinter = view.findViewById(R.id.tv_label_printer);

        labelPrinter.generateRandomDrinkRequest();
        labelPrinter.updateDisplay();
    }
}