package com.jackingaming.thestraylightrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.accelerometer.game.GameActivity;
import com.jackingaming.thestraylightrun.accelerometer.redandgreen.AccelerometerFragment;
import com.jackingaming.thestraylightrun.nextweektonight.NextWeekTonightFragment;
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
            case R.id.options_item_next_week_tonight:
                replaceFragmentInContainer(NextWeekTonightFragment.newInstance(null, null));
                return true;
            case R.id.options_item_game_controller:
                startActivity(
                        new Intent(this, GameActivity.class)
                );
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