package com.jackingaming.thestraylightrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jackingaming.thestraylightrun.accelerometer.AccelerometerFragment;
import com.jackingaming.thestraylightrun.spritesheetclipselector.controllers.SpriteSheetClipSelectorFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fcv_main, AccelerometerFragment.newInstance(null, null), null)
                    .commit();
        }
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
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fcv_main, fragment)
                .addToBackStack(null)
                .commit();
    }
}