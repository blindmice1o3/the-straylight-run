package com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;

public class TypeWriterDialogFragment extends DialogFragment {
    public static final String TAG = TypeWriterDialogFragment.class.getSimpleName();
    public static final String ARG_DELAY = "delay";
    public static final String ARG_TEXT = "text";

    private TypeWriterTextView tvTypeWriter;
    private long delay;
    private String text;

    public static TypeWriterDialogFragment newInstance(long delay, String text) {
        TypeWriterDialogFragment fragment = new TypeWriterDialogFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_DELAY, delay);
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        delay = getArguments().getLong(ARG_DELAY);
        text = getArguments().getString(ARG_TEXT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        return inflater.inflate(R.layout.dialogfragment_type_writer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tvTypeWriter = view.findViewById(R.id.tv_type_writer);
        tvTypeWriter.setCharacterDelay(delay);
        tvTypeWriter.displayTextWithAnimation(text);
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