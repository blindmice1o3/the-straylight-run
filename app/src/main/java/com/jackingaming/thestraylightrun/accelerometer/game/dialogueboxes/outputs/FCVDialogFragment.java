package com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.outputs;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

import java.io.Serializable;

public class FCVDialogFragment extends DialogFragment {
    public static final String TAG = FCVDialogFragment.class.getSimpleName();
    public static final String ARG_FRAGMENT = "fragment";
    public static final String ARG_TAG = "tag";
    public static final String ARG_CANCELED_ON_TOUCH_OUTSIDE = "canceledOnTouchOutside";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private DismissListener listener;

    private Fragment fragmentToShow;
    private String tag;
    private boolean canceledOnTouchOutside;

    public static FCVDialogFragment newInstance(Fragment fragmentToShow, String tag,
                                                boolean canceledOnTouchOutside,
                                                DismissListener listener) {
        FCVDialogFragment fragment = new FCVDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAGMENT, (Serializable) fragmentToShow);
        args.putString(ARG_TAG, tag);
        args.putBoolean(ARG_CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
        args.putSerializable(ARG_DISMISS_LISTENER, (Serializable) listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fragmentToShow = (Fragment) getArguments().getSerializable(ARG_FRAGMENT);
            tag = getArguments().getString(ARG_TAG);
            canceledOnTouchOutside = getArguments().getBoolean(ARG_CANCELED_ON_TOUCH_OUTSIDE);
            listener = (DismissListener) getArguments().getSerializable(ARG_DISMISS_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_fragment_container_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        window.setLayout((int) (width * 0.75), (int) (height * 0.75));
        window.setGravity(Gravity.CENTER);

        getDialog().setCanceledOnTouchOutside(canceledOnTouchOutside);

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fcv_dialog_fragment, fragmentToShow)
                .addToBackStack(null)
                .commit();
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
