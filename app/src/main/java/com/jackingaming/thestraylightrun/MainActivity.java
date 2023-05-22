package com.jackingaming.thestraylightrun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.jackingaming.thestraylightrun.accelerometer.AccelerometerFragment;

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
}