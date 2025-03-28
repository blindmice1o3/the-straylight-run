package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs;

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

public class IDEDialogFragment extends DialogFragment {
    public static final String TAG = IDEDialogFragment.class.getSimpleName();
    public static final String ARG_BUTTON_LISTENER = "buttonListener";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";

    public interface ButtonListener extends Serializable {
        void onExecuteButtonClick(View view, IDEDialogFragment ideDialogFragment);
    }

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private ButtonListener buttonListener;
    private DismissListener dismissListener;

    private TextView tvExecute;

    public static IDEDialogFragment newInstance(ButtonListener buttonListener,
                                                DismissListener dismissListener) {
        IDEDialogFragment fragment = new IDEDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_BUTTON_LISTENER, buttonListener);
        args.putSerializable(ARG_DISMISS_LISTENER, dismissListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

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

        return inflater.inflate(R.layout.dialogfragment_ide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tvExecute = view.findViewById(R.id.tv_execute);

        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onExecuteButtonClick(view, IDEDialogFragment.this);
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
