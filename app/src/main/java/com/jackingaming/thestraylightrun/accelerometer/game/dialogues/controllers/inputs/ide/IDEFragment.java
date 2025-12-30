package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center.MainViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left.ProjectViewportFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.ClassViewerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.StructureViewportFragment;

import java.io.Serializable;

public class IDEFragment extends Fragment
        implements Serializable {
    public static final String TAG = IDEFragment.class.getSimpleName();
    public static final String ARG_MODE = "mode";
    public static final String ARG_RUN = "run";

    public enum Mode {LONG_PRESS_REVEALS, KEYBOARD_TRAINER;}

    private Mode mode;
    private Game.Run run;

    private ProjectViewportFragment projectViewportFragment;
    private MainViewportFragment mainViewportFragment;
    private StructureViewportFragment structureViewportFragment;

    public static IDEFragment newInstance(Mode mode,
                                          Game.Run run) {
        IDEFragment fragment = new IDEFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_MODE, mode);
        args.putSerializable(ARG_RUN, run);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        Bundle arguments = getArguments();
        if (arguments != null) {
            mode = (Mode) arguments.getSerializable(ARG_MODE);
            run = (Game.Run) arguments.getSerializable(ARG_RUN);
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

        projectViewportFragment = ProjectViewportFragment.newInstance(run);
        mainViewportFragment = MainViewportFragment.newInstance(
                projectViewportFragment.getClassMain(), mode
        );
        structureViewportFragment = StructureViewportFragment.newInstance(null);

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
            public void changeFieldType(Class classWithFieldToEdit, Field fieldToEdit, String typeAsString) {
                fieldToEdit.setType(typeAsString);

                structureViewportFragment.replaceWithNewClass(
                        ClassViewerFragment.newInstance(classWithFieldToEdit)
                );
            }

            @Override
            public void changeMethodReturnType(Class classWithMethodToEdit, Method methodToEdit, String typeAsString) {
                methodToEdit.setType(typeAsString);

                structureViewportFragment.replaceWithNewClass(
                        ClassViewerFragment.newInstance(classWithMethodToEdit)
                );
            }

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
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        projectViewportFragment.removeClassMain();
        Class firstClassFromList = projectViewportFragment.getFirstClassFromList();
        mainViewportFragment.replaceMainWithFirstClassFromList(firstClassFromList);
    }
}
