package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.IDEDialogFragment;
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

    private IDEDialogFragment.Mode mode;

    public ClassVP2Adapter(@NonNull Fragment fragment, List<Class> classes, IDEDialogFragment.Mode mode) {
        super(fragment);
        this.fragment = fragment;
        this.classes = classes;
        this.mode = mode;
        fragmentsByClass = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // TODO: use mode to decide between ClassEditorFragment (LONG_PRESS_REVEALS)
        //  and a currently undefined class.
        Fragment fragmentToUse = ClassEditorFragment.newInstance(
                this, classes.get(position), mode
        );

        fragmentsByClass.put(
                classes.get(position), fragmentToUse
        );

        return fragmentToUse;
    }

    public void changeFieldType(Class classWithFieldToEdit, Field fieldToEdit, String typeAsString) {
        ((MainViewportFragment) fragment).changeFieldType(classWithFieldToEdit, fieldToEdit, typeAsString);
    }

    public void changeMethodReturnType(Class classWithMethodToEdit, Method methodToEdit, String typeAsString) {
        ((MainViewportFragment) fragment).changeMethodReturnType(classWithMethodToEdit, methodToEdit, typeAsString);
    }

    public void renameField(Class classWithFieldToEdit, Field fieldToEdit, String nameNew) {
        ((MainViewportFragment) fragment).renameField(classWithFieldToEdit, fieldToEdit, nameNew);
    }

    public void renameMethod(Class classWithMethodToEdit, Method methodToEdit, String name) {
        ((MainViewportFragment) fragment).renameMethod(classWithMethodToEdit, methodToEdit, name);
    }

    public void renameClass(Class classRenamed) {
        Log.e(TAG, "renameClass()");
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
