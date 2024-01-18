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
    public static final String REQUEST_KEY = "fillSteamingPitcher";
    public static final String BUNDLE_KEY_CONTENT = "content";
    public static final String BUNDLE_KEY_AMOUNT = "amount";

    private static final String CONTENT = "content";
    private static final String AMOUNT = "amount";
    private static final int MIN = 0;
    private static final int MAX = 25 * 4;
    private static final int BRACKET1 = 8 * 4;
    private static final int BRACKET2 = 12 * 4;
    private static final int BRACKET3 = 16 * 4;
    private static final int BRACKET4 = 20 * 4;

    private String content;
    private int amount;

    public static FillSteamingPitcherDialogFragment newInstance(String content, int amount) {
        Log.e(TAG, "newInstance(String, int)");

        FillSteamingPitcherDialogFragment fragment = new FillSteamingPitcherDialogFragment();

        Bundle args = new Bundle();
        args.putString(CONTENT, content);
        args.putInt(AMOUNT, amount);
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

        content = getArguments().getString(CONTENT);
        amount = getArguments().getInt(AMOUNT);

        tvDisplayer.setText(content + ": " + amount);
        refreshBackgroundResourceImage(ivSteamingCup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(MIN);
            seekBar.setMax(MAX);
        }
        seekBar.setProgress(amount);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amount = i;

                tvDisplayer.setText(content + ": " + amount);
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
        Log.e(TAG, "sendBackResult()");

        Bundle result = new Bundle();
        result.putString(BUNDLE_KEY_CONTENT, content);
        result.putInt(BUNDLE_KEY_AMOUNT, amount);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
    }

    private void refreshBackgroundResourceImage(ImageView imageView) {
        if (amount == 0) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_empty);
        } else if (amount > 0 && amount <= BRACKET1) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_short);
        } else if (amount > BRACKET1 && amount <= BRACKET2) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_tall);
        } else if (amount > BRACKET2 && amount <= BRACKET3) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_grande);
        } else if (amount > BRACKET3 && amount <= BRACKET4) {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_venti);
        } else {
            imageView.setBackgroundResource(R.drawable.steaming_pitcher_max);
        }
    }
}
