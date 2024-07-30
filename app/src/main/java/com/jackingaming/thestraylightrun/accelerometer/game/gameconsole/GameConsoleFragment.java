package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.GamePadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.buttonpad.ButtonPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.directionpad.DirectionPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer.StatsDisplayerFragment;

import java.io.Serializable;

public class GameConsoleFragment extends Fragment
        implements Serializable,
        MySurfaceView.MySurfaceViewSurfaceChangeListener,
        Game.StatsChangeListener,
        StatsDisplayerFragment.ButtonHolderClickListener {
    public static final String TAG = "GameConsoleFragment";
    public static final String ARG_GAME_TITLE = "game";

    private MySurfaceView mySurfaceView;
    private StatsDisplayerFragment statsDisplayerFragment;
    private GamePadFragment gamePadFragment;
    private DirectionPadFragment directionPadFragment;
    private ButtonPadFragment buttonPadFragment;

    private String gameTitle;
    private Game game;
    private InputManager inputManager;

    public GameConsoleFragment() {
        // Required empty public constructor
    }

    public static GameConsoleFragment newInstance(String gameTitle) {
        GameConsoleFragment fragment = new GameConsoleFragment();

        Bundle args = new Bundle();
        args.putString(ARG_GAME_TITLE, gameTitle);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, getClass().getSimpleName() + ".onCreate(Bundle savedInstanceState)");
        inputManager = new InputManager();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, getClass().getSimpleName() + ".onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");

        View view = inflater.inflate(R.layout.fragment_game_console, container, false);
        mySurfaceView = view.findViewById(R.id.mysurfaceview_game_console_fragment);
        mySurfaceView.setMySurfaceViewSurfaceChangeListener(this);
        mySurfaceView.setMySurfaceViewTouchListener(inputManager);

        statsDisplayerFragment = (StatsDisplayerFragment) getChildFragmentManager().findFragmentById(R.id.statsdisplayerfragment_game_console_fragment);
        statsDisplayerFragment.setButtonHolderClickListener(this);

        gamePadFragment = (GamePadFragment) getChildFragmentManager().findFragmentById(R.id.gamepadfragment_game_console_fragment);
        directionPadFragment = (DirectionPadFragment) gamePadFragment.getChildFragmentManager().findFragmentById(R.id.directionpadfragment_game_pad_fragment);
        directionPadFragment.setDirectionPadListener(inputManager);
        buttonPadFragment = (ButtonPadFragment) gamePadFragment.getChildFragmentManager().findFragmentById(R.id.buttonpadfragment_game_pad_fragment);
        buttonPadFragment.setButtonPadListener(inputManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, getClass().getSimpleName() + ".onActivityCreated(Bundle savedInstanceState)");

        Bundle bundle = getArguments();
        if (bundle != null) {
            //////////////////////////////////////////
            gameTitle = bundle.getString(ARG_GAME_TITLE);
            Log.d(TAG, getClass().getSimpleName() + ".onActivityCreated(Bundle savedInstanceState) gameTitle selected is: " + gameTitle);
            //////////////////////////////////////////

            game = new Game(gameTitle);
        }

        game.setStatsChangeListener(this);

        if (savedInstanceState == null) {
            Log.d(TAG, getClass().getSimpleName() + ".onActivityCreated(Bundle savedInstanceState) savedInstanceState == null");
            // TODO: initial run, create new game (do NOT call load).
        } else {
            Log.d(TAG, getClass().getSimpleName() + ".onActivityCreated(Bundle savedInstanceState) savedInstanceState NOT null");
            // TODO: recovering from orientation change or some other recreate event, call load on game.
            game.setLoadNeeded(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, getClass().getSimpleName() + ".onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getClass().getSimpleName() + ".onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, getClass().getSimpleName() + ".onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, getClass().getSimpleName() + ".onSaveInstanceState(Bundle outState)");
        game.saveViaOS();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, getClass().getSimpleName() + ".onAttach(Context context)");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, getClass().getSimpleName() + ".onDetach()");
    }

    @Override
    public void onCurrencyChange(float currency) {
        statsDisplayerFragment.setCurrency(currency);
    }

    @Override
    public void onTimeChange(long timePlayedInMilliseconds) {
        statsDisplayerFragment.setTime(timePlayedInMilliseconds);
    }

    @Override
    public void onButtonHolderAChange(Bitmap image) {
        statsDisplayerFragment.setImageForButtonHolderA(image);
    }

    @Override
    public void onButtonHolderBChange(Bitmap image) {
        statsDisplayerFragment.setImageForButtonHolderB(image);
    }

    @Override
    public void onMySurfaceViewSurfaceChanged(MySurfaceView mySurfaceView, SurfaceHolder surfaceHolder, int format, int widthScreen, int heightScreen) {
        directionPadFragment.setupOnTouchListener();
        buttonPadFragment.setupOnTouchListener();

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        game.init(getContext(), inputManager, surfaceHolder, widthScreen, heightScreen);
        mySurfaceView.runGame(game, inputManager);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    }

    @Override
    public void onButtonHolderClicked(StatsDisplayerFragment.ButtonHolder buttonHolder) {
        game.doClickButtonHolder(buttonHolder);
    }
}