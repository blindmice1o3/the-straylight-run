package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

import java.io.Serializable;

public class SpinnerDialogFragment extends DialogFragment {
    public static final String TAG = SpinnerDialogFragment.class.getSimpleName();
    public static final String ARG_CONTENT_ARRAY = "contentArray";
    public static final String ARG_INDEX_DEFAULT = "indexDefault";
    public static final String ARG_ITEM_SELECTION_LISTENER = "itemSelectionListener";

    public interface ItemSelectionListener extends Serializable {
        void onDismiss();

        void onItemSelected(int indexSelected);
    }

    private ItemSelectionListener listener;

    private String[] contentArray;
    private int indexDefault;
    private Spinner spinner;
    private TextView tvSelect;

    public static SpinnerDialogFragment newInstance(String[] contentArray, int indexDefault, ItemSelectionListener listener) {
        SpinnerDialogFragment fragment = new SpinnerDialogFragment();

        Bundle args = new Bundle();
        args.putStringArray(ARG_CONTENT_ARRAY, contentArray);
        args.putInt(ARG_INDEX_DEFAULT, indexDefault);
        args.putSerializable(ARG_ITEM_SELECTION_LISTENER, listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            contentArray = getArguments().getStringArray(ARG_CONTENT_ARRAY);
            indexDefault = getArguments().getInt(ARG_INDEX_DEFAULT);
            listener = (ItemSelectionListener) getArguments().getSerializable(ARG_ITEM_SELECTION_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_spinner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, contentArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(indexDefault);

        tvSelect = view.findViewById(R.id.tv_select);
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(
                        spinner.getSelectedItemPosition()
                );
                dismiss();
            }
        });

        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        window.setLayout((int) (width * 0.50), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        listener.onDismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}
