package com.jackingaming.thestraylightrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jackingaming.thestraylightrun.accelerometer.AccelerometerFragment;
import com.jackingaming.thestraylightrun.spritesheetclipselector.SpriteSheetClipSelectorFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fcv_main, AccelerometerFragment.newInstance(null, null));
        ft.setReorderingAllowed(true);
        ft.commitNow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_item_sprite_sheet_clip_selector:
                replaceFragmentInContainer(SpriteSheetClipSelectorFragment.newInstance(null, null));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void replaceFragmentInContainer(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fcv_main, fragment);
        ft.setReorderingAllowed(true);
        ft.addToBackStack(null);
        ft.commit();
    }
}