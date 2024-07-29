package com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

import java.io.Serializable;

public class EditTextDialogFragment extends DialogFragment {
    public static final String TAG = EditTextDialogFragment.class.getSimpleName();
    public static final String ARG_ENTER_LISTENER = "enterListener";

    public interface EnterListener extends Serializable {
        void onDismiss();

        void onEnterKeyPressed(String name);
    }

    private EnterListener listener;

    private EditText editText;
    private Button buttonDone;

    public static EditTextDialogFragment newInstance(EnterListener listener) {
        EditTextDialogFragment fragment = new EditTextDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ENTER_LISTENER, listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listener = (EnterListener) getArguments().getSerializable(ARG_ENTER_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_edittext, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        editText = view.findViewById(R.id.et_name);
        buttonDone = view.findViewById(R.id.button_done);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String name = editText.getText().toString();
                    if (!name.isEmpty()) {
                        dismiss();
                        listener.onEnterKeyPressed(name);
                        return true;
                    }
                }
                return false;
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                if (!name.isEmpty()) {
                    dismiss();
                    listener.onEnterKeyPressed(name);
                }
            }
        });

        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        editText.requestFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
