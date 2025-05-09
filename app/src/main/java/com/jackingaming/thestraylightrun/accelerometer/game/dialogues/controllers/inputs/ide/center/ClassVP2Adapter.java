package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Field;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.right.Method;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassVP2Adapter extends FragmentStateAdapter
        implements Serializable {
    public static final String TAG = ClassVP2Adapter.class.getSimpleName();

    private Fragment fragment;
    private List<Class> classes;
    private Map<Class, Fragment> fragmentsByClass;

    public ClassVP2Adapter(@NonNull Fragment fragment, List<Class> classes) {
        super(fragment);
        this.fragment = fragment;
        this.classes = classes;
        fragmentsByClass = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ClassEditorFragment classEditorFragment = ClassEditorFragment.newInstance(
                this, classes.get(position)
        );
        fragmentsByClass.put(classes.get(position), classEditorFragment);
        return classEditorFragment;
    }

    public void renameField(Class classWithFieldToEdit, Field fieldToEdit, String nameNew) {
        ((MainViewportFragment) fragment).renameField(classWithFieldToEdit, fieldToEdit, nameNew);
    }

    public void renameMethod(Class classWithMethodToEdit, Method methodToEdit, String name) {
        ((MainViewportFragment) fragment).renameMethod(classWithMethodToEdit, methodToEdit, name);
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
