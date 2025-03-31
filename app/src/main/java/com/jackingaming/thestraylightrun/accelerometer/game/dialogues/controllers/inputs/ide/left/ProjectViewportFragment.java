package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.left;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.ClassesDataObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectViewportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectViewportFragment extends Fragment {
    public static final String TAG = ProjectViewportFragment.class.getSimpleName();
    public static final String ARG_CLASSES_DATA_OBJECT = "classesDataObject";

    public interface GestureListener {
        void onClassClicked(Class classClicked);
    }

    private GestureListener gestureListener;

    public void setGestureListener(GestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    private List<Class> classes;
    private RecyclerView recyclerView;

    public ProjectViewportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classesDataObject ClassesDataObject.
     * @return A new instance of fragment ProjectViewportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectViewportFragment newInstance(ClassesDataObject classesDataObject) {
        ProjectViewportFragment fragment = new ProjectViewportFragment();
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
        return inflater.inflate(R.layout.fragment_project_viewport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_project_view);

        ClassRVAdapterForProject classRVAdapter = new ClassRVAdapterForProject(getContext(), classes, new ClassRVAdapterForProject.GestureListener() {
            @Override
            public void onSingleTapUp(int position) {
                Log.e(TAG, "projectviewportfragment classrvadapter onSingleTapUp(int)... position: " + position);
                gestureListener.onClassClicked(
                        classes.get(position)
                );
            }
        });
        recyclerView.setAdapter(classRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}