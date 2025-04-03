package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassVP2Adapter extends FragmentStateAdapter {
    public static final String TAG = ClassVP2Adapter.class.getSimpleName();

    private List<Class> classes;
    private Map<Class, Fragment> fragmentsByClass;

    public ClassVP2Adapter(@NonNull Fragment fragment, List<Class> classes) {
        super(fragment);
        this.classes = classes;
        fragmentsByClass = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ClassEditorFragment classEditorFragment = ClassEditorFragment.newInstance(
                classes.get(position)
        );
        fragmentsByClass.put(classes.get(position), classEditorFragment);
        return classEditorFragment;
    }

    public void renameClass(Class classRenamed) {
        for (Class classToCheck : classes) {
            if (classToCheck.getName().equals(
                    classRenamed.getName())
            ) {
                ((ClassEditorFragment) fragmentsByClass.get(classToCheck)).renameClass(classRenamed);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }
}
