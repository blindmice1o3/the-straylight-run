package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.dialogfragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

    public interface FillSteamingPitcherDialogListener {
        void onFinishFillDialog(int current);
    }

    private static final String CURRENT = "current";
    private static final int MIN = 0;
    private static final int MAX = 25 * 4;
    private static final int BRACKET1 = 8 * 4;
    private static final int BRACKET2 = 12 * 4;
    private static final int BRACKET3 = 16 * 4;
    private static final int BRACKET4 = 20 * 4;

    private int current;

    public static FillSteamingPitcherDialogFragment newInstance(int current) {
        Log.e(TAG, "newInstance(int)");

        FillSteamingPitcherDialogFragment fragment = new FillSteamingPitcherDialogFragment();

        Bundle args = new Bundle();
        args.putInt(CURRENT, current);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_fill_steaming_pitcher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        TextView tvDisplayer = view.findViewById(R.id.tv_displayer);
        ImageView ivSteamingCup = view.findViewById(R.id.iv_steaming_cup);
        AppCompatSeekBar seekBar = view.findViewById(R.id.seekbar);

        current = getArguments().getInt(CURRENT);
        tvDisplayer.setText(Integer.toString(current));
        refreshBackgroundResourceImage(ivSteamingCup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(MIN);
            seekBar.setMax(MAX);
        }
        seekBar.setProgress(current);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                current = i;
                tvDisplayer.setText(Integer.toString(i));
                refreshBackgroundResourceImage(ivSteamingCup);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Intentionally blank.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Intentionally blank.
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        sendBackResult();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }

    private void sendBackResult() {
        FillSteamingPitcherDialogListener listener = (FillSteamingPitcherDialogListener) getTargetFragment();
        listener.onFinishFillDialog(current);
    }

    private void refreshBackgroundResourceImage(ImageView imageView) {
        if (current == 0) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_empty);
        } else if (current > 0 && current <= BRACKET1) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_short);
        } else if (current > BRACKET1 && current <= BRACKET2) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_tall);
        } else if (current > BRACKET2 && current <= BRACKET3) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_grande);
        } else if (current > BRACKET3 && current <= BRACKET4) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_venti);
        } else {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_max);
        }
    }
}
