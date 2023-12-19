package com.jackingaming.thestraylightrun.sequencetrainer;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupCaddyFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.MaestranaFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

public class SequenceTrainerActivity extends AppCompatActivity {
    public static final String TAG = SequenceTrainerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        setContentView(R.layout.activity_sequence_trainer);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fcv_main, MaestranaFragment.newInstance());
        ft.add(R.id.fcv_right, CupCaddyFragment.newInstance());
        ft.add(R.id.fcv_bottom, RefrigeratorFragment.newInstance());
        ft.commit();
    }
}