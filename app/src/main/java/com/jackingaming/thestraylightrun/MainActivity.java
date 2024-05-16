package com.jackingaming.thestraylightrun;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.accelerometer.game.GameActivity;
import com.jackingaming.thestraylightrun.accelerometer.redandgreen.AccelerometerFragment;
import com.jackingaming.thestraylightrun.nextweektonight.NextWeekTonightFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.SequenceTrainerActivity;
import com.jackingaming.thestraylightrun.spritesheetclipselector.controllers.SpriteSheetClipSelectorFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//            getWindow().setAllowEnterTransitionOverlap(true);
            // Set an exit transition
            getWindow().setExitTransition(new Explode());
            // Set an enter transition
            getWindow().setEnterTransition(new Fade(Fade.IN));
        }
        ///////////////////////////////////////
        setContentView(R.layout.activity_main);
        ///////////////////////////////////////

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(
                            new Intent(this, GameActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    );
                } else {
                    startActivity(
                            new Intent(this, GameActivity.class)
                    );
                }
                return true;
            case R.id.options_item_hot_bar:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(
                            new Intent(this, SequenceTrainerActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    );
                } else {
                    startActivity(
                            new Intent(this, SequenceTrainerActivity.class)
                    );
                }
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