package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.ClassesDataObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainViewportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainViewportFragment extends Fragment {
    public static final String TAG = MainViewportFragment.class.getSimpleName();
    public static final String ARG_CLASSES_DATA_OBJECT = "classesDataObject";

    private List<Class> classes;
    private ClassVP2Adapter classVP2Adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public MainViewportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classesDataObject ClassesDataObject.
     * @return A new instance of fragment MainViewportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainViewportFragment newInstance(ClassesDataObject classesDataObject) {
        MainViewportFragment fragment = new MainViewportFragment();
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
        return inflater.inflate(R.layout.fragment_main_viewport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);

        classVP2Adapter = new ClassVP2Adapter(this, classes);
        viewPager2 = view.findViewById(R.id.vp2_main_display);
        viewPager2.setUserInputEnabled(false); // prevent horizontal swiping.
        viewPager2.setAdapter(classVP2Adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Class classToEdit = classes.get(position);

                tab.setText(
                        classToEdit.getName()
                );
            }
        });
        tabLayoutMediator.attach();
    }
}