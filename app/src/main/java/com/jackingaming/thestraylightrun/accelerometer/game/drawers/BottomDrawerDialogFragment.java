package com.jackingaming.thestraylightrun.accelerometer.game.drawers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;

import java.io.Serializable;

public class BottomDrawerDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = BottomDrawerDialogFragment.class.getSimpleName();
    public static final String ARG_DRAWER_BOTTOM_LISTENER = "drawerBottomListener";

    public interface DrawerBottomListener extends Serializable {
        void onClickTypeWriterTextView(View view, String tag);
    }

    private DrawerBottomListener listener;

    private TypeWriterTextView typeWriterTextView;

    public static BottomDrawerDialogFragment newInstance(DrawerBottomListener listener) {
        BottomDrawerDialogFragment fragment = new BottomDrawerDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_DRAWER_BOTTOM_LISTENER, listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (DrawerBottomListener) getArguments().getSerializable(ARG_DRAWER_BOTTOM_LISTENER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_bottom_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        typeWriterTextView = view.findViewById(R.id.type_writer_tv_in_drawer_bottom);

        typeWriterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickTypeWriterTextView(view, BottomDrawerDialogFragment.TAG);
            }
        });
        typeWriterTextView.setTextCompletionListener(new TypeWriterTextView.TextCompletionListener() {
            @Override
            public void onAnimationFinish() {
                Log.e(TAG, "onAnimationFinish()");

                // TODO:
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
