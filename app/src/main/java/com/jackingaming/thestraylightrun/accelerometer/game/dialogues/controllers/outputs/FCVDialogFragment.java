package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs;

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
    public static final float DEFAULT_WIDTH_IN_DECIMAL = 0.75f;
    public static final float DEFAULT_HEIGHT_IN_DECIMAL = 0.75f;

    public static final String ARG_FRAGMENT = "fragment";
    public static final String ARG_TAG = "tag";
    public static final String ARG_CANCELED_ON_TOUCH_OUTSIDE = "canceledOnTouchOutside";
    public static final String ARG_WIDTH_IN_DECIMAL = "widthInDecimal";
    public static final String ARG_HEIGHT_IN_DECIMAL = "heightInDecimal";
    public static final String ARG_LIFE_CYCLE_LISTENER = "lifeCycleListener";

    public interface LifecycleListener extends Serializable {
        void onResume();

        void onDismiss();
    }

    private LifecycleListener listener;

    private Fragment fragmentToShow;
    private String tag;
    private boolean canceledOnTouchOutside;
    private float widthInDecimal;
    private float heightInDecimal;

    public static FCVDialogFragment newInstance(Fragment fragmentToShow, String tag,
                                                boolean canceledOnTouchOutside,
                                                float widthInDecimal, float heightInDecimal,
                                                LifecycleListener listener) {
        FCVDialogFragment fragment = new FCVDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAGMENT, (Serializable) fragmentToShow);
        args.putString(ARG_TAG, tag);
        args.putBoolean(ARG_CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
        args.putFloat(ARG_WIDTH_IN_DECIMAL, widthInDecimal);
        args.putFloat(ARG_HEIGHT_IN_DECIMAL, heightInDecimal);
        args.putSerializable(ARG_LIFE_CYCLE_LISTENER, (Serializable) listener);
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
            widthInDecimal = getArguments().getFloat(ARG_WIDTH_IN_DECIMAL);
            heightInDecimal = getArguments().getFloat(ARG_HEIGHT_IN_DECIMAL);
            listener = (LifecycleListener) getArguments().getSerializable(ARG_LIFE_CYCLE_LISTENER);
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
        window.setLayout((int) (width * widthInDecimal), (int) (height * heightInDecimal));
        window.setGravity(Gravity.CENTER);

        getDialog().setCanceledOnTouchOutside(canceledOnTouchOutside);

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fcv_dialog_fragment, fragmentToShow)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");

        listener.onResume();
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
