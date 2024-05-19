package com.jackingaming.thestraylightrun;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.accelerometer.game.GameFragment;
import com.jackingaming.thestraylightrun.accelerometer.redandgreen.AccelerometerFragment;
import com.jackingaming.thestraylightrun.nextweektonight.NextWeekTonightFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.SequenceTrainerFragment;
import com.jackingaming.thestraylightrun.spritesheetclipselector.controllers.SpriteSheetClipSelectorFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.options_item_sprite_sheet_clip_selector ||
                    menu.getItem(i).getItemId() == R.id.options_item_next_week_tonight ||
                    menu.getItem(i).getItemId() == R.id.options_item_game_controller ||
                    menu.getItem(i).getItemId() == R.id.options_item_hot_bar) {
                menu.getItem(i).setVisible(true);
            } else {
                menu.getItem(i).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_item_sprite_sheet_clip_selector:
                replaceFragmentInContainerUsingCardFlipAnimations(SpriteSheetClipSelectorFragment.newInstance(null, null));
                return true;
            case R.id.options_item_next_week_tonight:
                replaceFragmentInContainerUsingCardFlipAnimations(NextWeekTonightFragment.newInstance(null, null));
                return true;
            case R.id.options_item_game_controller:
                replaceFragmentInContainerUsingCardFlipAnimations(GameFragment.newInstance(null, null, new GameFragment.ReplaceFragmentListener() {
                    @Override
                    public void onReplaceFragment(Fragment newFragment) {
                        replaceFragmentInContainerUsingCardFlipAnimations(newFragment);
                    }
                }));
                return true;
            case R.id.options_item_hot_bar:
                replaceFragmentInContainerUsingCardFlipAnimations(SequenceTrainerFragment.newInstance(null, null));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getSupportFragmentManager().findFragmentById(R.id.fcv_main) instanceof GameFragment) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
    }

    private void replaceFragmentInContainerUsingCardFlipAnimations(Fragment fragment) {
        // Create and commit a new fragment transaction that adds the fragment
        // for the back of the card, uses custom animations, and is part of the
        // fragment manager's back stack.

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)

                // Replace the default fragment animations with animator
                // resources representing rotations when switching to the back
                // of the car, as well as animator resources representing
                // rotations when flipping back to the front, such as when the
                // system Back button is pressed.
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments in the container view with a fragment
                // representing the fragment passed in.
                .replace(R.id.fcv_main, fragment)

                // Add this transaction to the back stack, letting users press
                // Back to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
    }
}