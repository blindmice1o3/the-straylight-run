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

    public interface ButtonListener extends Serializable {
        void onOffButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onWalkButtonClick(View view, RobotDialogFragment robotDialogFragment);

        void onRunButtonClick(View view, RobotDialogFragment robotDialogFragment);
    }

    private ButtonListener listener;

    private TextView tvOff, tvWalk, tvRun;

    public static RobotDialogFragment newInstance(ButtonListener listener) {
        RobotDialogFragment fragment = new RobotDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_BUTTON_LISTENER, listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listener = (ButtonListener) getArguments().getSerializable(ARG_BUTTON_LISTENER);
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

        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOffButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onWalkButtonClick(view, RobotDialogFragment.this);
                dismiss();
            }
        });

        tvRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRunButtonClick(view, RobotDialogFragment.this);
                dismiss();
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
