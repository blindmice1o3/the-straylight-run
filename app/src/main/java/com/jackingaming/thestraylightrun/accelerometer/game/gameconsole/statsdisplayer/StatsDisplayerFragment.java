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

import java.io.Serializable;

public class StatsDisplayerFragment extends Fragment
        implements Serializable {
    public static final String TAG = StatsDisplayerFragment.class.getSimpleName();
    public static final int IMAGE_DEFAULT = R.drawable.btn_star_on_normal_holo_light;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public interface ButtonHolderClickListener extends Serializable {
        void onButtonHolderClicked(ButtonHolder buttonHolder);
    }

    private ButtonHolderClickListener buttonHolderClickListener;

    public void setButtonHolderClickListener(ButtonHolderClickListener buttonHolderClickListener) {
        this.buttonHolderClickListener = buttonHolderClickListener;
    }

    public enum ButtonHolder {A, B;}

    transient private Bitmap honeyPot;
    transient private Bitmap calendar;

    private TextView textViewCurrency;
    private TextView textViewTime;
    private ImageView imageViewButtonHolderA;
    private ImageView imageViewButtonHolderB;

    public StatsDisplayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsDisplayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsDisplayerFragment newInstance(String param1, String param2) {
        StatsDisplayerFragment fragment = new StatsDisplayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

//    private static final int MILLISECONDS_PER_SECOND = 1_000;
//    private static final int SECONDS_PER_MINUTE = 60;
//    private static final int MINUTES_PER_HOUR = 60;

    public void setTime(String inGameClockTime, String calendarText) {
//        // TODO: translate from milliseconds to HH:MM:SS
//        long secondsTotal = timePlayedInMilliseconds / MILLISECONDS_PER_SECOND;
//        long minutesTotal = secondsTotal / SECONDS_PER_MINUTE;
//        long secondsCalculated = secondsTotal % SECONDS_PER_MINUTE;
//        long hoursTotal = minutesTotal / MINUTES_PER_HOUR;
//        long minutesCaculated = minutesTotal % MINUTES_PER_HOUR;
//        String timeFormatted = String.format("%02d:%02d:%02d", hoursTotal, minutesCaculated, secondsCalculated);
//        textViewTime.setText(timeFormatted);
        textViewTime.setText(inGameClockTime + "\n" + calendarText);
    }

    public void setImageForButtonHolderA(Bitmap image) {
        imageViewButtonHolderA.setImageBitmap(image);
    }

    public void setImageForButtonHolderB(Bitmap image) {
        imageViewButtonHolderB.setImageBitmap(image);
    }

}