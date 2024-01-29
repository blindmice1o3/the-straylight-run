package com.jackingaming.thestraylightrun.sequencetrainer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

public class LabelPrinterModeDialogFragment extends DialogFragment {
    public static final String TAG = LabelPrinterModeDialogFragment.class.getSimpleName();
    public static final String REQUEST_KEY = "labelPrinterMode";
    public static final String BUNDLE_KEY_MODE_SELECTED = "modeSelected";
    private static final String MODE_PASSED_IN = "modePassedIn";

    private RadioGroup radioGroup;
    private RadioButton radioButtonStandard, radioButtonCustomized, radioButtonBoth;
    private String modePassedIn;

    public static LabelPrinterModeDialogFragment newInstance(String modePassedIn) {
        Log.e(TAG, "newInstance(String)");

        LabelPrinterModeDialogFragment fragment = new LabelPrinterModeDialogFragment();

        Bundle args = new Bundle();
        args.putString(MODE_PASSED_IN, modePassedIn);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_label_printer_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        radioGroup = view.findViewById(R.id.radiogroup_label_printer_mode);
        radioButtonStandard = view.findViewById(R.id.radiobutton_standard);
        radioButtonCustomized = view.findViewById(R.id.radiobutton_customized);
        radioButtonBoth = view.findViewById(R.id.radiobutton_both);

        modePassedIn = getArguments().getString(MODE_PASSED_IN);

        if (modePassedIn.equals(radioButtonStandard.getText().toString())) {
            radioButtonStandard.setChecked(true);
        } else if (modePassedIn.equals(radioButtonCustomized.getText().toString())) {
            radioButtonCustomized.setChecked(true);
        } else if (modePassedIn.equals(radioButtonBoth.getText().toString())) {
            radioButtonBoth.setChecked(true);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        sendBackResult();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }

    private void sendBackResult() {
        Log.e(TAG, "sendBackResult()");

        RadioButton radioButtonSelected = (RadioButton) getView().findViewById(radioGroup.getCheckedRadioButtonId());
        String modeSelected = radioButtonSelected.getText().toString();

        Bundle result = new Bundle();
        result.putString(BUNDLE_KEY_MODE_SELECTED, modeSelected);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
    }
}
