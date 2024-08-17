package com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs;

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

import java.io.Serializable;

public class RobotDialogFragment extends DialogFragment {
    public static final String TAG = RobotDialogFragment.class.getSimpleName();
    public static final String ARG_BUTTON_LISTENER = "buttonListener";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";

    public interface ButtonListener extends Serializable {
        void onOffButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onWalkButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onRunButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onTillSeedWaterButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onWaterButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onHarvestButtonClick(View view, RobotDialogFragment robotDialogFragment);
    }

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private ButtonListener buttonListener;
    private DismissListener dismissListener;

    private TextView tvOff, tvWalk, tvRun, tvTillSeedWater, tvWater, tvHarvest;

    public static RobotDialogFragment newInstance(ButtonListener buttonListener,
                                                  DismissListener dismissListener) {
        RobotDialogFragment fragment = new RobotDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_BUTTON_LISTENER, buttonListener);
        args.putSerializable(ARG_DISMISS_LISTENER, dismissListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            buttonListener = (ButtonListener) getArguments().getSerializable(ARG_BUTTON_LISTENER);
            dismissListener = (DismissListener) getArguments().getSerializable(ARG_DISMISS_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");

        getDialog().getWindow().setGravity(Gravity.CENTER);

        return inflater.inflate(R.layout.dialogfragment_robot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tvOff = view.findViewById(R.id.tv_off);
        tvWalk = view.findViewById(R.id.tv_walk);
        tvRun = view.findViewById(R.id.tv_run);
        tvTillSeedWater = view.findViewById(R.id.tv_till_seed_water);
        tvWater = view.findViewById(R.id.tv_water);
        tvHarvest = view.findViewById(R.id.tv_harvest);

        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onOffButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onWalkButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onRunButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvTillSeedWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onTillSeedWaterButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onWaterButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onHarvestButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });
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
