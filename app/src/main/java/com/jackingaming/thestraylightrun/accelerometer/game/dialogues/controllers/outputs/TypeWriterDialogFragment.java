package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class TypeWriterDialogFragment extends DialogFragment
        implements Serializable {
    public static final String TAG = TypeWriterDialogFragment.class.getSimpleName();
    public static final String ARG_DELAY = "delay";
    public static final String ARG_PORTRAIT = "portrait";
    public static final String ARG_TEXT = "text";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";
    public static final String ARG_TEXT_COMPLETION_LISTENER = "textCompletionListener";

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private DismissListener dismissListener;

    transient private ImageView ivEntityPortrait;
    private TypeWriterTextView tvTypeWriter;
    private long delay;
    transient private Bitmap portrait;
    private String text;
    private TypeWriterTextView.TextCompletionListener textCompletionListener;

    public static TypeWriterDialogFragment newInstance(long delay, Bitmap portrait, String text,
                                                       DismissListener dismissListener,
                                                       TypeWriterTextView.TextCompletionListener textCompletionListener) {
        TypeWriterDialogFragment fragment = new TypeWriterDialogFragment();


        Bundle args = new Bundle();
        args.putLong(ARG_DELAY, delay);
        // @@@ Bitmap portrait (convert to ByteArray) @@@
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        portrait.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArrayPortrait = stream.toByteArray();
        args.putByteArray(ARG_PORTRAIT, byteArrayPortrait);
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        args.putString(ARG_TEXT, text);
        args.putSerializable(ARG_DISMISS_LISTENER, dismissListener);
        args.putSerializable(ARG_TEXT_COMPLETION_LISTENER, textCompletionListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            delay = getArguments().getLong(ARG_DELAY);
            // @@@ Bitmap portrait (convert from ByteArray) @@@
            byte[] byteArrayPortrait = getArguments().getByteArray(ARG_PORTRAIT);
            portrait = BitmapFactory.decodeByteArray(byteArrayPortrait, 0, byteArrayPortrait.length);
            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            text = getArguments().getString(ARG_TEXT);
            dismissListener = (DismissListener) getArguments().getSerializable(ARG_DISMISS_LISTENER);
            textCompletionListener = (TypeWriterTextView.TextCompletionListener) getArguments().getSerializable(ARG_TEXT_COMPLETION_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");

        if (getDialog() != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        } else {
            Log.e(TAG, "getDialog() == null");
        }

        return inflater.inflate(R.layout.dialogfragment_type_writer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        ivEntityPortrait = view.findViewById(R.id.iv_entity_portrait);
        ivEntityPortrait.setImageBitmap(portrait);
        tvTypeWriter = view.findViewById(R.id.tv_type_writer);
        tvTypeWriter.setTextCompletionListener(textCompletionListener);
        tvTypeWriter.setCharacterDelay(delay);
        tvTypeWriter.displayTextWithAnimation(text);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        tvTypeWriter.stopAnimation();
        dismissListener.onDismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");

        tvTypeWriter.stopAnimation();
    }
}
