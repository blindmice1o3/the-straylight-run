package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;

import java.io.Serializable;

public class PlantDialogFragment extends DialogFragment {
    public static final String TAG = PlantDialogFragment.class.getSimpleName();
    public static final String ARG_PLANT = "plant";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private Plant plant;
    private DismissListener dismissListener;

    private TextView tvNeedWatering, tvHealth, tvAgeInDays, tvColor;

    public static PlantDialogFragment newInstance(Plant plant, DismissListener dismissListener) {
        PlantDialogFragment fragment = new PlantDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_PLANT, plant);
        args.putSerializable(ARG_DISMISS_LISTENER, dismissListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        if (getArguments() != null) {
            plant = (Plant) getArguments().getSerializable(ARG_PLANT);
            dismissListener = (DismissListener) getArguments().getSerializable(ARG_DISMISS_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");

        getDialog().getWindow().setGravity(Gravity.CENTER);

        return inflater.inflate(R.layout.dialogfragment_plant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tvNeedWatering = view.findViewById(R.id.tv_need_watering);
        tvNeedWatering.append(
                Boolean.toString(plant.isNeedWatering())
        );

        tvHealth = view.findViewById(R.id.tv_health);
        tvHealth.append(
                Integer.toString(
                        plant.getHealth()
                )
        );

        tvAgeInDays = view.findViewById(R.id.tv_age_in_days);
        tvAgeInDays.append(
                Integer.toString(
                        plant.getAgeInDays()
                )
        );

        tvColor = view.findViewById(R.id.tv_color);
        tvColor.append(
                plant.getColor().toString().toLowerCase()
        );
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        dismissListener.onDismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}