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

public class DifficultySettingsDialogFragment extends DialogFragment {
    public static final String TAG = DifficultySettingsDialogFragment.class.getSimpleName();
    public static final String REQUEST_KEY = "difficultySettings";
    public static final String BUNDLE_KEY_SETTING_SELECTED = "settingSelected";
    private static final String DIFFICULTY_SETTING_PASSED_IN = "difficultySettingPassedIn";

    private RadioGroup radioGroup;
    private RadioButton radioButtonStandard, radioButtonCustomized, radioButtonBoth;
    private String difficultySettingPassedIn;

    public static DifficultySettingsDialogFragment newInstance(String difficultySettingPassedIn) {
        Log.e(TAG, "newInstance(String)");

        DifficultySettingsDialogFragment fragment = new DifficultySettingsDialogFragment();

        Bundle args = new Bundle();
        args.putString(DIFFICULTY_SETTING_PASSED_IN, difficultySettingPassedIn);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_difficulty_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        radioGroup = view.findViewById(R.id.radiogroup_difficulty_settings);
        radioButtonStandard = view.findViewById(R.id.radiobutton_standard);
        radioButtonCustomized = view.findViewById(R.id.radiobutton_customized);
        radioButtonBoth = view.findViewById(R.id.radiobutton_both);

        difficultySettingPassedIn = getArguments().getString(DIFFICULTY_SETTING_PASSED_IN);

        if (difficultySettingPassedIn.equals(radioButtonStandard.getText().toString())) {
            radioButtonStandard.setChecked(true);
        } else if (difficultySettingPassedIn.equals(radioButtonCustomized.getText().toString())) {
            radioButtonCustomized.setChecked(true);
        } else if (difficultySettingPassedIn.equals(radioButtonBoth.getText().toString())) {
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
        String settingSelected = radioButtonSelected.getText().toString();

        Bundle result = new Bundle();
        result.putString(BUNDLE_KEY_SETTING_SELECTED, settingSelected);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
    }
}
