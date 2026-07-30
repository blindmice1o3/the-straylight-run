package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.IDEFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;
import com.jackingaming.thestraylightrun.sandbox.particleexplosion.ParticleExplosionView;

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
    public static final String ARG_CLASS_MAIN = "class_main";
    public static final String ARG_MODE = "mode";
    public static final String ARG_RUN = "run";

    public static final int NUMBER_OF_TODOS_IN_RUN_ONE = 1;
    public static final int NUMBER_OF_TODOS_IN_RUN_TWO = 0;
    public static final int NUMBER_OF_TODOS_IN_RUN_THREE = 1;
    public static final int NUMBER_OF_TODOS_IN_RUN_FOUR = 3;

    public interface MainViewportListener extends Serializable {
        void changeFieldType(Class classWithFieldToEdit, Field fieldToEdit, String typeAsString);

        void changeMethodReturnType(Class classWithMethodToEdit, Method methodToEdit, String typeAsString);

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

    private IDEFragment.Mode mode;
    private Game.Run run;
    private int numberOfTODOSClicked;

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
    public static MainViewportFragment newInstance(
            Class classMain, IDEFragment.Mode mode, Game.Run run) {
        MainViewportFragment fragment = new MainViewportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS_MAIN, classMain);
        args.putSerializable(ARG_MODE, mode);
        args.putSerializable(ARG_RUN, run);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Class classMain = (Class) arguments.getSerializable(ARG_CLASS_MAIN);
            classes.add(classMain);
            mode = (IDEFragment.Mode) arguments.getSerializable(ARG_MODE);
            run = (Game.Run) arguments.getSerializable(ARG_RUN);
            numberOfTODOSClicked = 0;
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

        classVP2Adapter = new ClassVP2Adapter(this,
                classes, mode, new ClassEditorFragment.TodoListener() {
            @Override
            public void todoWasLongClicked() {
                // Increment numberOfTODOSClicked.
                numberOfTODOSClicked++;

                // Check requirements for finishing this run's IDE.
                int numberOfTODOSRequired = 0;
                switch (run) {
                    case ONE:
                        numberOfTODOSRequired = NUMBER_OF_TODOS_IN_RUN_ONE;
                        break;
                    case TWO:
                        numberOfTODOSRequired = NUMBER_OF_TODOS_IN_RUN_TWO;
                        break;
                    case THREE:
                        numberOfTODOSRequired = NUMBER_OF_TODOS_IN_RUN_THREE;
                        break;
                    case FOUR:
                        numberOfTODOSRequired = NUMBER_OF_TODOS_IN_RUN_FOUR;
                        break;
                }

                if (numberOfTODOSClicked >= numberOfTODOSRequired) {
                    finishLongClickingAllTODOS();
                }
            }
        });
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

    public void finishLongClickingAllTODOS() {
        Log.e(TAG, "finishLongClickingAllTODOS()");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                int width = 128;
                int height = 128;
                float xCenter = (getView().getRootView().getWidth() / 2) - (width / 2);
                float yCenter = (getView().getRootView().getHeight() / 2) - (height / 2);


                ParticleExplosionView particleExplosionView = new ParticleExplosionView(getContext());
                particleExplosionView.initParticles();
                particleExplosionView.setX(
                        xCenter
                );
                particleExplosionView.setY(
                        yCenter
                );
                particleExplosionView.setZ(1f);
                particleExplosionView.setVisibility(View.INVISIBLE);

                ConstraintLayout constraintLayout = ((ConstraintLayout) (getView().getParent().getParent()));
                constraintLayout.addView(particleExplosionView,
                        new ConstraintLayout.LayoutParams(
                                width,
                                height
                        )
                );
                constraintLayout.invalidate();

                ObjectAnimator animatorExplosion = ObjectAnimator.ofFloat(particleExplosionView, "progress", 0.0f, 1.0f);
                animatorExplosion.setInterpolator(new LinearInterpolator());
                animatorExplosion.setDuration(1000L);
                animatorExplosion.setRepeatCount(ValueAnimator.INFINITE);
                animatorExplosion.setRepeatMode(ValueAnimator.REVERSE);
                animatorExplosion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        particleExplosionView.updateProgressOfParticles(
                                particleExplosionView.getProgress()
                        );
                    }
                });

                startWaitTimer(4200L, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.e(TAG, "waitTimer's onAnimationEnd()");

                        particleExplosionView.setVisibility(View.INVISIBLE);
                        constraintLayout.removeView(particleExplosionView);

                        // TODO: maybe show yellow close/x button?
                    }
                });

                particleExplosionView.setVisibility(View.VISIBLE);
                animatorExplosion.start();
            }
        });
    }

    private void startWaitTimer(long duration, AnimatorListenerAdapter animatorListenerAdapter) {
        Log.e(TAG, "startWaitTimer()");
        ObjectAnimator animatorDummyValue = ObjectAnimator.ofFloat(this, "dummyValue", 0f, 1f);
        animatorDummyValue.setInterpolator(new LinearInterpolator());
        animatorDummyValue.setDuration(duration);
        animatorDummyValue.addListener(animatorListenerAdapter);
        animatorDummyValue.start();
    }

    private float dummyValue = 0f;

    public float getDummyValue() {
        return dummyValue;
    }

    public void setDummyValue(float dummyValue) {
        this.dummyValue = dummyValue;
    }

    public void changeFieldType(Class classWithFieldToEdit, Field fieldToEdit, String typeAsString) {
        listener.changeFieldType(classWithFieldToEdit, fieldToEdit, typeAsString);
    }

    public void changeMethodReturnType(Class classWithMethodToEdit, Method methodToEdit, String typeAsString) {
        listener.changeMethodReturnType(classWithMethodToEdit, methodToEdit, typeAsString);
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

    public void replaceMainWithFirstClassFromList(Class firstClassFromList) {
        classes.set(0, firstClassFromList);
        classVP2Adapter.notifyItemChanged(0);
    }
}