package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments;

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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

public class FillCupColdDialogFragment extends DialogFragment {
    public static final String TAG = FillCupColdDialogFragment.class.getSimpleName();

    private static final String CUP_COLD = "cupCold";
    private static final String CONTENT = "content";
    private static final int MIN = 0;
    private static final int MAX = 25 * 4;
    private static final int BRACKET1 = 8 * 4;
    private static final int AMOUNT_OF_MILK_TALL = (12 * 4) - 12;
    private static final int AMOUNT_OF_MILK_GRANDE = (16 * 4) - 16;
    private static final int AMOUNT_OF_MILK_VENTI = (24 * 4) - 24;

//    private static final int BRACKET1 = 8 * 4;
//    private static final int BRACKET2 = 12 * 4;
//    private static final int BRACKET3 = 16 * 4;
//    private static final int BRACKET4 = 20 * 4;

    private CupCold cupCold;
    private String content;
    private int amount;

    public static FillCupColdDialogFragment newInstance(CupCold cupCold, String content) {
        Log.e(TAG, "newInstance(CupCold, String)");

        FillCupColdDialogFragment fragment = new FillCupColdDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(CUP_COLD, cupCold);
        args.putString(CONTENT, content);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_fill_cup_cold, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        TextView tvDisplayer = view.findViewById(R.id.tv_displayer);
        ImageView ivCupCold = view.findViewById(R.id.iv_cup_cold);
        AppCompatSeekBar seekBar = view.findViewById(R.id.seekbar);

        cupCold = (CupCold) getArguments().getSerializable(CUP_COLD);
        content = getArguments().getString(CONTENT);

        // TODO: find out cup size (via tag)... calculate seekBar's MIN using cup size...
        Log.e(TAG, "!!!!! cupCold.getTag() (should be cup size): " + cupCold.getTag());

        Milk.Type type = null;
        for (Milk.Type typeMilk : Milk.Type.values()) {
            if (content.equals(typeMilk.name())) {
                type = typeMilk;
            }
        }

        String textForDisplayer = null;
        if (cupCold.getMilk() != null) {
            amount = cupCold.getMilk().getAmount();

            cupCold.getMilk().setType(type);
            cupCold.invalidate();

            textForDisplayer = cupCold.getMilk().getType().name() + ": " + amount;
        } else {
            amount = 0;

            Milk milkToBeAdded = new Milk(getContext());
            milkToBeAdded.init(type, amount, RefrigeratorFragment.TEMPERATURE, 0);
            cupCold.setMilk(milkToBeAdded);
            cupCold.invalidate();

            textForDisplayer = "no milk";
        }
        tvDisplayer.setText(textForDisplayer);

        refreshBackgroundResourceImage(ivCupCold);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(MIN);
            seekBar.setMax(MAX);
        }
        seekBar.setProgress(amount);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amount = i;

                cupCold.getMilk().setAmount(amount);
                cupCold.invalidate();

                String textForDisplayer = cupCold.getMilk().getType().name() + ": " + amount;
                tvDisplayer.setText(textForDisplayer);
                refreshBackgroundResourceImage(ivCupCold);
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
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }

    private void refreshBackgroundResourceImage(ImageView imageView) {
        // cup has ice inside.
        if (cupCold.getIce() != null) {
            if (amount == 0) {
                imageView.setBackgroundResource(R.drawable.cup_cold_iced_0);
            } else if (amount > 0 && amount <= BRACKET1) {
                imageView.setBackgroundResource(R.drawable.cup_cold_iced_1);
            } else if (amount > BRACKET1 && amount <= AMOUNT_OF_MILK_TALL) {
                imageView.setBackgroundResource(R.drawable.cup_cold_iced_2);
            } else if (amount > AMOUNT_OF_MILK_TALL && amount <= AMOUNT_OF_MILK_GRANDE) {
                imageView.setBackgroundResource(R.drawable.cup_cold_iced_3);
            } else if (amount > AMOUNT_OF_MILK_GRANDE && amount <= AMOUNT_OF_MILK_VENTI) {
                imageView.setBackgroundResource(R.drawable.cup_cold_iced_4);
            } else {
                imageView.setBackgroundResource(R.drawable.cup_cold_iced_5);
            }
        }
        // cup does NOT have ice inside.
        else {
            if (amount == 0) {
                imageView.setBackgroundResource(R.drawable.cup_cold_empty_0);
            } else if (amount > 0 && amount <= BRACKET1) {
                imageView.setBackgroundResource(R.drawable.cup_cold_empty_1);
            } else if (amount > BRACKET1 && amount <= AMOUNT_OF_MILK_TALL) {
                imageView.setBackgroundResource(R.drawable.cup_cold_empty_2);
            } else if (amount > AMOUNT_OF_MILK_TALL && amount <= AMOUNT_OF_MILK_GRANDE) {
                imageView.setBackgroundResource(R.drawable.cup_cold_empty_3);
            } else if (amount > AMOUNT_OF_MILK_GRANDE && amount <= AMOUNT_OF_MILK_VENTI) {
                imageView.setBackgroundResource(R.drawable.cup_cold_empty_4);
            } else {
                imageView.setBackgroundResource(R.drawable.cup_cold_empty_5);
            }
        }
    }
}
