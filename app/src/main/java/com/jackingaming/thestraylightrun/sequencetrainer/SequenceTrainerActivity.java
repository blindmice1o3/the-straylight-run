package com.jackingaming.thestraylightrun.sequencetrainer;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupCaddyFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.LabelPrinterFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.MastrenaFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.sink.SinkFragment;

public class SequenceTrainerActivity extends AppCompatActivity {
    public static final String TAG = SequenceTrainerActivity.class.getSimpleName();

    private String settingSelected = "both";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        setContentView(R.layout.activity_sequence_trainer);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fcv_main, MastrenaFragment.newInstance());
        ft.add(R.id.fcv_right_top, LabelPrinterFragment.newInstance());
        ft.add(R.id.fcv_right_middle, CupCaddyFragment.newInstance());
        ft.add(R.id.fcv_right_bottom, SinkFragment.newInstance("", ""));
        ft.add(R.id.fcv_bottom, RefrigeratorFragment.newInstance());
        ft.commit();

        // Set the listener on the fragmentManager.
        getSupportFragmentManager().setFragmentResultListener(DifficultySettingsDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                settingSelected = result.getString(DifficultySettingsDialogFragment.BUNDLE_KEY_SETTING_SELECTED);
                Log.e(TAG, "settingSelected: " + settingSelected);

                MastrenaFragment mastrenaFragment = (MastrenaFragment) getSupportFragmentManager().findFragmentById(R.id.fcv_main);
                mastrenaFragment.changeDifficultySetting(settingSelected);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_activity_sequence_trainer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_item_difficulty_settings:
                DifficultySettingsDialogFragment difficultySettingsDialogFragment = DifficultySettingsDialogFragment.newInstance(settingSelected);
                difficultySettingsDialogFragment.show(getSupportFragmentManager(), DifficultySettingsDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}