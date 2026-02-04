package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.buttonpad.ButtonPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.directionpad.DirectionPadFragment;

public class GamePadFragment extends Fragment {
    public static final String TAG = GamePadFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DirectionPadFragment directionPadFragment;
    private ButtonPadFragment buttonPadFragment;

    public GamePadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamePadFragment.
     */
    public static GamePadFragment newInstance(String param1, String param2) {
        GamePadFragment fragment = new GamePadFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_pad, container, false);
        directionPadFragment = (DirectionPadFragment) getChildFragmentManager().findFragmentById(R.id.directionpadfragment_game_pad_fragment);
        buttonPadFragment = (ButtonPadFragment) getChildFragmentManager().findFragmentById(R.id.buttonpadfragment_game_pad_fragment);
        return view;
    }

    public void hideMenuButton() {
        buttonPadFragment.hideImageViewButtonMenu();
    }

    public void showMenuButton() {
        buttonPadFragment.showImageViewButtonMenu();
    }
}