package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StructureViewportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StructureViewportFragment extends Fragment {
    public static final String TAG = StructureViewportFragment.class.getSimpleName();
    public static final String ARG_CLASS = "class";

    private Class classToDisplay;
    private NoStructureFragment noStructureFragment;

    public StructureViewportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classToDisplay Class.
     * @return A new instance of fragment StructureViewportFragment.
     */
    public static StructureViewportFragment newInstance(Class classToDisplay) {
        StructureViewportFragment fragment = new StructureViewportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS, classToDisplay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classToDisplay = (Class) getArguments().getSerializable(ARG_CLASS);
        }

        noStructureFragment = NoStructureFragment.newInstance(null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_structure_viewport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fcv_structure, noStructureFragment);
        ft.commit();
    }

    public void replaceWithNewClass(Fragment fragment) {
        Fragment fragmentNew = (fragment != null) ? fragment : noStructureFragment;
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fcv_structure, fragmentNew);
        ft.commit();
    }
}