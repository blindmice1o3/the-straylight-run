package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassViewerFragment extends Fragment {
    public static final String TAG = ClassViewerFragment.class.getSimpleName();
    private static final String ARG_CLASS = "class";

    private Class myClass;
    private TextView tvClassName;
    private RecyclerView rvClassComponents;

    public ClassViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param myClass Class.
     * @return A new instance of fragment ClassViewerFragment.
     */
    public static ClassViewerFragment newInstance(Class myClass) {
        ClassViewerFragment fragment = new ClassViewerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS, myClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myClass = (Class) getArguments().getSerializable(ARG_CLASS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvClassName = view.findViewById(R.id.tv_class_name);
        rvClassComponents = view.findViewById(R.id.rv_class_components);

        tvClassName.setText(
                myClass.getName()
        );
        tvClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvClassComponents.getVisibility() == View.VISIBLE) {
                    rvClassComponents.setVisibility(View.INVISIBLE);
                } else {
                    rvClassComponents.setVisibility(View.VISIBLE);
                }
            }
        });

        ClassRVAdapterForStructure classRVAdapter = new ClassRVAdapterForStructure(myClass);
        rvClassComponents.setAdapter(classRVAdapter);
        rvClassComponents.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}