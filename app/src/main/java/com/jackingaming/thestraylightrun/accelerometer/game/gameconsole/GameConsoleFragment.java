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
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.GamePadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.buttonpad.ButtonPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.directionpad.DirectionPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer.StatsDisplayerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.viewport.MySurfaceView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.viewport.ViewportFragment;

import java.io.Serializable;

public class GameConsoleFragment extends Fragment
        implements Serializable,
        MySurfaceView.MySurfaceViewSurfaceChangeListener,
        Game.ReplaceViewportListener,
        Game.TextboxListener,
        Game.StatsChangeListener,
        StatsDisplayerFragment.ButtonHolderClickListener {
    public static final String TAG = GameConsoleFragment.class.getSimpleName();
    public static final String ARG_GAME_TITLE = "game";

    private ViewportFragment viewportFragment;
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
        Log.e(TAG, "onCreate()");
        inputManager = new InputManager();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_game_console, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        viewportFragment = ViewportFragment.newInstance(null, null);
        statsDisplayerFragment = StatsDisplayerFragment.newInstance(null, null);
        gamePadFragment = GamePadFragment.newInstance(null, null);

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fcv_mysurfaceview, viewportFragment)
                .add(R.id.fcv_statsdisplayerfragment, statsDisplayerFragment)
                .add(R.id.fcv_gamepadfragment, gamePadFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated()");

        mySurfaceView = viewportFragment.getView().findViewById(R.id.mysurfaceview_game_console_fragment);
        mySurfaceView.setMySurfaceViewSurfaceChangeListener(this);
        mySurfaceView.setMySurfaceViewTouchListener(inputManager);

        statsDisplayerFragment.setButtonHolderClickListener(this);

        directionPadFragment = (DirectionPadFragment) gamePadFragment.getChildFragmentManager().findFragmentById(R.id.directionpadfragment_game_pad_fragment);
        directionPadFragment.setDirectionPadListener(inputManager);
        buttonPadFragment = (ButtonPadFragment) gamePadFragment.getChildFragmentManager().findFragmentById(R.id.buttonpadfragment_game_pad_fragment);
        buttonPadFragment.setButtonPadListener(inputManager);

        Bundle bundle = getArguments();
        if (bundle != null) {
            //////////////////////////////////////////
            gameTitle = bundle.getString(ARG_GAME_TITLE);
            Log.d(TAG, getClass().getSimpleName() + ".onActivityCreated(Bundle savedInstanceState) gameTitle selected is: " + gameTitle);
            //////////////////////////////////////////

            game = new Game(gameTitle);
        }

        game.setReplaceViewportListener(this);
        game.setTextboxListener(this);
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
        Log.e(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState()");

        game.saveViaOS();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach()");
    }

    private Fragment fragmentReplacingSurfaceView;

    @Override
    public void showFragmentAndHideSurfaceView(Fragment fragmentReplacingSurfaceView) {
        this.fragmentReplacingSurfaceView = fragmentReplacingSurfaceView;

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fcv_mysurfaceview, fragmentReplacingSurfaceView)
                .hide(viewportFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showSurfaceView() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (fragmentReplacingSurfaceView != null) {
            fragmentTransaction.remove(fragmentReplacingSurfaceView);
        }

        fragmentTransaction
                .setReorderingAllowed(true)
                .show(viewportFragment)
                .addToBackStack(null)
                .commit();

        fragmentReplacingSurfaceView = null;
    }

    @Override
    public void showTextbox(TypeWriterDialogFragment typeWriterDialogFragment) {
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.slide_from_bottom_via_translate, R.anim.slide_to_top_via_translate)
                .replace(R.id.fcv_statsdisplayerfragment, typeWriterDialogFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showStatsDisplayer() {
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.slide_from_bottom_via_translate, R.anim.slide_to_top_via_translate)
                .replace(R.id.fcv_statsdisplayerfragment, statsDisplayerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCurrencyChange(float currency) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setCurrency(currency);
        } else {
            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
    }

    @Override
    public void onTimeChange(long timePlayedInMilliseconds) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setTime(timePlayedInMilliseconds);
        } else {
//            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
    }

    @Override
    public void onButtonHolderAChange(Bitmap image) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setImageForButtonHolderA(image);
        } else {
            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
    }

    @Override
    public void onButtonHolderBChange(Bitmap image) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setImageForButtonHolderB(image);
        } else {
            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
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