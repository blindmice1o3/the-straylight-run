package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.dialogfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

public class EspressoShotControlDialogFragment extends DialogFragment {
    public static final String TAG = EspressoShotControlDialogFragment.class.getSimpleName();

    private Button buttonBlonde, buttonSignature, buttonDecaf;
    private Button buttonSingle, buttonDouble, buttonTriple;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_espresso_shot_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        buttonBlonde = view.findViewById(R.id.button_blonde);
        buttonSignature = view.findViewById(R.id.button_signature);
        buttonDecaf = view.findViewById(R.id.button_decaf);
        buttonSingle = view.findViewById(R.id.button_single);
        buttonDouble = view.findViewById(R.id.button_double);
        buttonTriple = view.findViewById(R.id.button_triple);

        buttonBlonde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "BLONDE");
            }
        });
        buttonSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "SIGNATURE");
            }
        });
        buttonDecaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "DECAF");
            }
        });
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "SINGLE");
            }
        });
        buttonDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "DOUBLE");
            }
        });
        buttonTriple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "TRIPLE");
            }
        });
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
