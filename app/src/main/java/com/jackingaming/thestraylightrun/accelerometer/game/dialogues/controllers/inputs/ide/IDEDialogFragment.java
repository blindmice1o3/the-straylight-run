package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center.MainViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left.ProjectViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.StructureViewportFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IDEDialogFragment extends DialogFragment {
    public static final String TAG = IDEDialogFragment.class.getSimpleName();
    public static final String ARG_BUTTON_LISTENER = "buttonListener";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";

    public interface ButtonListener extends Serializable {
        void onExecuteButtonClick(View view, IDEDialogFragment ideDialogFragment);
    }

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private ButtonListener buttonListener;
    private DismissListener dismissListener;

    private List<Class> classes;
    private TextView tvExecute;

    public static IDEDialogFragment newInstance(ButtonListener buttonListener,
                                                DismissListener dismissListener) {
        IDEDialogFragment fragment = new IDEDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_BUTTON_LISTENER, buttonListener);
        args.putSerializable(ARG_DISMISS_LISTENER, dismissListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        if (getArguments() != null) {
            buttonListener = (ButtonListener) getArguments().getSerializable(ARG_BUTTON_LISTENER);
            dismissListener = (DismissListener) getArguments().getSerializable(ARG_DISMISS_LISTENER);
        }

        classes = new ArrayList<>();
        classes.add(new Class("Main"));
        classes.add(new Class("Foo"));
        classes.add(new Class("Bar"));
        for (int i = 0; i < 100; i++) {
            classes.add(new Class(i + "_cat"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_ide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        ClassesDataObject classesDataObject = new ClassesDataObject(classes);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fcv_left, ProjectViewportFragment.newInstance(classesDataObject));
        ft.add(R.id.fcv_center, MainViewportFragment.newInstance(classesDataObject));
        ft.add(R.id.fcv_right, StructureViewportFragment.newInstance(classesDataObject));
        ft.commit();

        //////////////////////////////////////////////////////////////////////

        tvExecute = view.findViewById(R.id.tv_execute);
        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onExecuteButtonClick(view, IDEDialogFragment.this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");

        getDialog().setCancelable(false);

        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        window.setLayout(width, (height / 2));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        dismissListener.onDismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}
