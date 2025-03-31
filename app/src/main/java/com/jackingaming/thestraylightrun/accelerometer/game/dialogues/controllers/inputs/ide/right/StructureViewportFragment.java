package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.ClassesDataObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StructureViewportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StructureViewportFragment extends Fragment {
    public static final String TAG = StructureViewportFragment.class.getSimpleName();
    public static final String ARG_CLASSES_DATA_OBJECT = "classesDataObject";

    private List<Class> classes;

    public StructureViewportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classesDataObject ClassesDataObject.
     * @return A new instance of fragment StructureViewportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StructureViewportFragment newInstance(ClassesDataObject classesDataObject) {
        StructureViewportFragment fragment = new StructureViewportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASSES_DATA_OBJECT, classesDataObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ClassesDataObject classesDataObject = (ClassesDataObject) getArguments().getSerializable(ARG_CLASSES_DATA_OBJECT);
            classes = classesDataObject.getClasses();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_structure_viewport, container, false);
    }
}