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
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.bottom.ConsoleViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center.MainViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left.ProjectViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.ClassViewerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.StructureViewportFragment;

import java.io.Serializable;

public class IDEDialogFragment extends DialogFragment
        implements Serializable {
    public static final String TAG = IDEDialogFragment.class.getSimpleName();
    public static final String ARG_BUTTON_LISTENER = "buttonListener";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";

    public interface ButtonListener extends Serializable {
        void onCloseButtonClicked(View view, IDEDialogFragment ideDialogFragment);
    }

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private ButtonListener buttonListener;
    private DismissListener dismissListener;

    private TextView tvClose;
    private TextView tvExecute;

    private ProjectViewportFragment projectViewportFragment;
    private MainViewportFragment mainViewportFragment;
    private StructureViewportFragment structureViewportFragment;
    private FragmentContainerView fcvBottom;
    private ConsoleViewportFragment consoleViewportFragment;

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

        projectViewportFragment = ProjectViewportFragment.newInstance();
        mainViewportFragment = MainViewportFragment.newInstance(
                projectViewportFragment.getMainClass()
        );
        structureViewportFragment = StructureViewportFragment.newInstance(null);
        consoleViewportFragment = ConsoleViewportFragment.newInstance(null, null);
        fcvBottom = view.findViewById(R.id.fcv_bottom);

        projectViewportFragment.setProjectViewportListener(new ProjectViewportFragment.ProjectViewportListener() {
            @Override
            public void onPackageClicked(Package packageClicked) {
                structureViewportFragment.replaceWithNewClass(
                        null
                );
            }

            @Override
            public void onClassClicked(Class classClicked) {
                structureViewportFragment.replaceWithNewClass(
                        ClassViewerFragment.newInstance(classClicked)
                );
            }

            @Override
            public void onClassRenamed(Class classRenamed) {
                // update MainViewportFragment and StructureViewportFragment.
                mainViewportFragment.renameClass(
                        classRenamed
                );
                structureViewportFragment.replaceWithNewClass(
                        ClassViewerFragment.newInstance(classRenamed)
                );
            }

            @Override
            public void onClassDoubleClicked(Class classDoubleClicked) {
                Log.e(TAG, "onClassDoubleClicked");
                mainViewportFragment.addClass(classDoubleClicked);
            }
        });

        mainViewportFragment.setListener(new MainViewportFragment.MainViewportListener() {
            @Override
            public void onFieldRenamed(Class classWithFieldToEdit, Field fieldToEdit, String nameNew) {
                fieldToEdit.setName(nameNew);

                structureViewportFragment.replaceWithNewClass(
                        ClassViewerFragment.newInstance(classWithFieldToEdit)
                );
            }

            @Override
            public void onMethodRenamed(Class classWithMethodToEdit, Method methodToEdit, String nameNew) {
                methodToEdit.setName(nameNew);

                structureViewportFragment.replaceWithNewClass(
                        ClassViewerFragment.newInstance(classWithMethodToEdit)
                );
            }
        });

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fcv_left, projectViewportFragment);
        ft.add(R.id.fcv_center, mainViewportFragment);
        ft.add(R.id.fcv_right, structureViewportFragment);
        ft.add(R.id.fcv_bottom, consoleViewportFragment);
        ft.commit();

        //////////////////////////////////////////////////////////////////////

        tvClose = view.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonListener.onCloseButtonClicked(view, IDEDialogFragment.this);
            }
        });

        tvExecute = view.findViewById(R.id.tv_execute);
        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class classMain = projectViewportFragment.getClassMain();

                Method methodMain = null;
                for (Method method : classMain.getMethods()) {
                    if (method.getName().equals(ProjectViewportFragment.METHOD_NAME_MAIN)) {
                        methodMain = method;
                        break;
                    }
                }

                consoleViewportFragment.displayToConsole(
                        methodMain.getBody()
                );

                //////////////////////////////
                openConsoleViewportFragment();
                //////////////////////////////
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");

        if (getDialog() != null) {
            getDialog().setCancelable(false);

            Window window = getDialog().getWindow();
            Display display = window.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            window.setLayout(width, (height / 2));
            window.setGravity(Gravity.CENTER);

            TextView tvConsole = getView().findViewById(R.id.tv_console);
            tvConsole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeConsoleViewportFragment();
                }
            });
            closeConsoleViewportFragment();
        }
    }

    private void closeConsoleViewportFragment() {
        fcvBottom.setVisibility(View.GONE);
    }

    public void openConsoleViewportFragment() {
        fcvBottom.setVisibility(View.VISIBLE);
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
