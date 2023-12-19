package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.dialogfragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

public class FillSteamingPitcherDialogFragment extends DialogFragment {
    public static final String TAG = FillSteamingPitcherDialogFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.dialogfragment_fill_steaming_pitcher, container, false);
    }

    private int current;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvDisplayer = view.findViewById(R.id.tv_displayer);
        ImageView ivSteamingCup = view.findViewById(R.id.iv_steaming_cup);
        AppCompatSeekBar seekBar = view.findViewById(R.id.seekbar);

        int min = 0;
        int max = 100;
        current = 0;
        ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_empty);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(min);
            seekBar.setMax(max);
        }
        seekBar.setProgress(current);
        tvDisplayer.setText(Integer.toString(current));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                current = i;
                tvDisplayer.setText(Integer.toString(i));

                int bracket1 = 25;
                int bracket2 = 50;
                int bracket3 = 75;
                int bracket4 = 100;
                if (current == 0) {
                    ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_empty);
                } else if (current > 0 && current < bracket1) {
                    ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_short);
                } else if (current > bracket1 && current < bracket2) {
                    ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_tall);
                } else if (current > bracket2 && current < bracket3) {
                    ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_grande);
                } else if (current > bracket3 && current < bracket4) {
                    ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_venti);
                } else {
                    ivSteamingCup.setBackgroundResource(R.drawable.steaming_pitcher_max);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
