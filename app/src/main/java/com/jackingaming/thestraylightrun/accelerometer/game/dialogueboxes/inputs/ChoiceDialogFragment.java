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

public class ChoiceDialogFragment extends DialogFragment {
    public static final String TAG = ChoiceDialogFragment.class.getSimpleName();
    public static final String ARG_CHOICE_LISTENER = "choiceListener";

    public interface ChoiceListener extends Serializable {
        void onChoiceYesSelected(View view, ChoiceDialogFragment choiceDialogFragment);

        void onChoiceNoSelected(View view, ChoiceDialogFragment choiceDialogFragment);
    }

    private ChoiceListener listener;

    private TextView tvYes, tvNo;

    public static ChoiceDialogFragment newInstance(ChoiceListener listener) {
        ChoiceDialogFragment fragment = new ChoiceDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CHOICE_LISTENER, listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = (ChoiceListener) getArguments().getSerializable(ARG_CHOICE_LISTENER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");

        getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.END);

        return inflater.inflate(R.layout.dialogfragment_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tvYes = view.findViewById(R.id.tv_yes);
        tvNo = view.findViewById(R.id.tv_no);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChoiceYesSelected(view, ChoiceDialogFragment.this);
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChoiceNoSelected(view, ChoiceDialogFragment.this);
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
