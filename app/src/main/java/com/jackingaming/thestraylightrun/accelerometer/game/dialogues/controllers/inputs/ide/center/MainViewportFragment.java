package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.os.Bundle;
import android.util.Log;
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
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainViewportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainViewportFragment extends Fragment {
    public static final String TAG = MainViewportFragment.class.getSimpleName();
    public static final String ARG_CLASS_MAIN = "classMain";

    public interface MainViewportListener extends Serializable {
        void changeFieldReturnType(Class classWithFieldToEdit, Field fieldToEdit, String returnTypeAsString);

        void onFieldRenamed(Class classWithFieldToEdit, Field fieldToEdit, String nameNew);

        void onMethodRenamed(Class classWithMethodToEdit, Method methodToEdit, String nameNew);
    }

    private MainViewportListener listener;

    public void setListener(MainViewportListener listener) {
        this.listener = listener;
    }

    private List<Class> classes = new ArrayList<>();
    private ClassVP2Adapter classVP2Adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public MainViewportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param classesDataObject ClassesDataObject.
     *
     * @return A new instance of fragment MainViewportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainViewportFragment newInstance(Class classMain) {
        MainViewportFragment fragment = new MainViewportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS_MAIN, classMain);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Class classMain = (Class) getArguments().getSerializable(ARG_CLASS_MAIN);
            classes.add(classMain);
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

    public void changeFieldReturnType(Class classWithFieldToEdit, Field fieldToEdit, String returnTypeAsString) {
        listener.changeFieldReturnType(classWithFieldToEdit, fieldToEdit, returnTypeAsString);
    }

    public void renameField(Class classWithFieldToEdit, Field fieldToEdit, String nameNew) {
        listener.onFieldRenamed(classWithFieldToEdit, fieldToEdit, nameNew);
    }

    public void renameMethod(Class classWithMethodToEdit, Method methodToEdit, String nameNew) {
        listener.onMethodRenamed(classWithMethodToEdit, methodToEdit, nameNew);
    }

    public void renameClass(Class classRenamed) {
        for (int i = 0; i < classes.size(); i++) {
            Class classToCheck = classes.get(i);

            if (classToCheck.getName().equals(
                    classRenamed.getName()
            )) {
                classVP2Adapter.renameClass(classRenamed);
                tabLayout.getTabAt(i).setText(
                        classRenamed.getName()
                );
                return;
            }
        }
    }

    public void addClass(Class classToAdd) {
        Log.e(TAG, "addClass()");
        for (int i = 0; i < classes.size(); i++) {
            Class classOpened = classes.get(i);

            if (classOpened.getName().equals(classToAdd.getName())) {
                Log.e(TAG, "classToAdd already in list of opened classes... open it in vp2");

                tabLayout.selectTab(
                        tabLayout.getTabAt(i)
                );

                return;
            }
        }

        classes.add(classToAdd);
        int indexClassToAdd = classes.size() - 1;
        classVP2Adapter.notifyItemInserted(indexClassToAdd);
        tabLayout.selectTab(
                tabLayout.getTabAt(indexClassToAdd)
        );
        Log.e(TAG, "classToAdd was added");
    }
}