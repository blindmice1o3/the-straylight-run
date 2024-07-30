package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer.buttonholder.ButtonHolderFragment;

public class StatsDisplayerFragment extends Fragment {
    public interface ButtonHolderClickListener {
        void onButtonHolderClicked(ButtonHolder buttonHolder);
    }

    private ButtonHolderClickListener buttonHolderClickListener;

    public void setButtonHolderClickListener(ButtonHolderClickListener buttonHolderClickListener) {
        this.buttonHolderClickListener = buttonHolderClickListener;
    }

    public enum ButtonHolder {A, B;}

    private Bitmap honeyPot;
    private Bitmap calendar;

    private TextView textViewCurrency;
    private TextView textViewTime;
    private ImageView imageViewButtonHolderA;
    private ImageView imageViewButtonHolderB;

    public StatsDisplayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_displayer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewCurrency = view.findViewById(R.id.textview_currency_stats_displayer_fragment);
        textViewTime = view.findViewById(R.id.textview_time_stats_displayer_fragment);
        imageViewButtonHolderA = view.findViewById(R.id.buttonholderfragment_a_stats_displayer_fragment).findViewById(R.id.imageview_buttonholderfragment);
        imageViewButtonHolderA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHolderClickListener.onButtonHolderClicked(ButtonHolder.A);
            }
        });
        imageViewButtonHolderB = view.findViewById(R.id.buttonholderfragment_b_stats_displayer_fragment).findViewById(R.id.imageview_buttonholderfragment);
        imageViewButtonHolderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHolderClickListener.onButtonHolderClicked(ButtonHolder.B);
            }
        });


        initImageHoneyPot(getResources());
        ImageView imageViewCurrencyIcon = view.findViewById(R.id.imageview_currency_stats_displayer_fragment);
        imageViewCurrencyIcon.setImageBitmap(honeyPot);

        initImageCalendar(getResources());
        ImageView imageViewTimeIcon = view.findViewById(R.id.imageview_time_stats_displayer_fragment);
        imageViewTimeIcon.setImageBitmap(calendar);

        ButtonHolderFragment buttonHolderFragmentA = (ButtonHolderFragment) getChildFragmentManager().findFragmentById(R.id.buttonholderfragment_a_stats_displayer_fragment);
        buttonHolderFragmentA.setTextForTextView("A");
        ButtonHolderFragment buttonHolderFragmentB = (ButtonHolderFragment) getChildFragmentManager().findFragmentById(R.id.buttonholderfragment_b_stats_displayer_fragment);
        buttonHolderFragmentB.setTextForTextView("B");
    }

    private void initImageCalendar(Resources resources) {
        Bitmap spriteSheet = BitmapFactory.decodeResource(resources, R.drawable.d_pad);
        calendar = Bitmap.createBitmap(spriteSheet, 17, 320, 20, 20);
    }

    private void initImageHoneyPot(Resources resources) {
        Bitmap spriteSheet = BitmapFactory.decodeResource(resources, R.drawable.gba_kingdom_hearts_chain_of_memories_winnie_the_pooh);
        honeyPot = Bitmap.createBitmap(spriteSheet, 318, 1556, 38, 37);
    }

    public void setCurrency(float currency) {
        String currencyAsString = String.format("%03d", (int) currency);
        textViewCurrency.setText(currencyAsString);
    }

    private static final int MILLISECONDS_PER_SECOND = 1_000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;

    public void setTime(long timePlayedInMilliseconds) {
        // TODO: translate from milliseconds to HH:MM:SS
        long secondsTotal = timePlayedInMilliseconds / MILLISECONDS_PER_SECOND;
        long minutesTotal = secondsTotal / SECONDS_PER_MINUTE;
        long secondsCalculated = secondsTotal % SECONDS_PER_MINUTE;
        long hoursTotal = minutesTotal / MINUTES_PER_HOUR;
        long minutesCaculated = minutesTotal % MINUTES_PER_HOUR;
        String timeFormatted = String.format("%02d:%02d:%02d", hoursTotal, minutesCaculated, secondsCalculated);
        textViewTime.setText(timeFormatted);
    }

    public void setImageForButtonHolderA(Bitmap image) {
        imageViewButtonHolderA.setImageBitmap(image);
    }

    public void setImageForButtonHolderB(Bitmap image) {
        imageViewButtonHolderB.setImageBitmap(image);
    }

}