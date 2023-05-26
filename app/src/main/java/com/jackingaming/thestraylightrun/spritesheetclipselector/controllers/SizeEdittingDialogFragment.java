package com.jackingaming.thestraylightrun.spritesheetclipselector.controllers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

public class SizeEdittingDialogFragment extends DialogFragment {
    public static final String TAG = SizeEdittingDialogFragment.class.getSimpleName();
    public static final String REQUEST_KEY_SIZE_EDITTING = "size_editting";
    public static final String BUNDLE_KEY_WIDTH = "width";
    public static final String BUNDLE_KEY_HEIGHT = "height";

    private static final String ARG_TITLE = "title";
    private static final String ARG_PARAM2 = "param2";

    private String mTitle;
    private String mParam2;

    public SizeEdittingDialogFragment() {
    }

    public static SizeEdittingDialogFragment newInstance(String title, String param2) {
        SizeEdittingDialogFragment fragment = new SizeEdittingDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_dialog_size_editting, container);
    }

    private EditText etWidthDatasource;
    private EditText etHeightDatasource;
    private Button buttonApply;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

        etWidthDatasource = view.findViewById(R.id.et_datasource_width);
        etWidthDatasource.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i(TAG, "onEditorAction etWidthDatasource i: " + i);
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    etHeightDatasource.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        etHeightDatasource = view.findViewById(R.id.et_datasource_height);
        etHeightDatasource.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i(TAG, "onEditorAction etWidthDatasource i: " + i);
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    apply();
                    handled = true;
                }
                return handled;
            }
        });

        buttonApply = view.findViewById(R.id.button_apply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply();
            }
        });

        etWidthDatasource.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        );
    }

    private void apply() {
        int widthDatasourceNew = Integer.parseInt(
                etWidthDatasource.getText().toString()
        );
        int heightDatasourceNew = Integer.parseInt(
                etHeightDatasource.getText().toString()
        );

        // TODO:
        Log.i(TAG, "MUST SEND DATA BACK TO CALLING FRAGMENT");
        Bundle result = new Bundle();
        result.putInt(BUNDLE_KEY_WIDTH, widthDatasourceNew);
        result.putInt(BUNDLE_KEY_HEIGHT, heightDatasourceNew);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY_SIZE_EDITTING,
                result);

        etWidthDatasource.setText("");
        etHeightDatasource.setText("");

        dismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.i(TAG, "onCancel()");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i(TAG, "onDismiss()");
    }
}
