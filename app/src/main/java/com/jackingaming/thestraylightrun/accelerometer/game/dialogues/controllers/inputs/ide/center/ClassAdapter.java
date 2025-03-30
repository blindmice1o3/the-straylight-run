package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.center;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide.Class;

import java.util.List;

public class ClassAdapter extends FragmentStateAdapter {

    private List<Class> classes;

    public ClassAdapter(@NonNull Fragment fragment, List<Class> classes) {
        super(fragment);
        this.classes = classes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ClassEditorFragment classEditorFragment =
                ClassEditorFragment.newInstance(
                        classes.get(position)
                );
        return classEditorFragment;
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }
}
