package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Assets;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.ItemStackable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.RobotReprogrammer4000;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.GameState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.GamePadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.buttonpad.ButtonPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.directionpad.DirectionPadFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.statsdisplayer.StatsDisplayerFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.viewport.MySurfaceView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.viewport.ViewportFragment;
import com.jackingaming.thestraylightrun.sandbox.particleexplosion.ParticleExplosionView;

import java.io.Serializable;

public class GameConsoleFragment extends Fragment
        implements Serializable,
        MySurfaceView.MySurfaceViewSurfaceChangeListener,
        Game.ViewportListener,
        Game.TextboxListener,
        Game.StatsChangeListener,
        StatsDisplayerFragment.ButtonHolderClickListener,
        StatsDisplayerFragment.IconClickListener {
    public static final String TAG = GameConsoleFragment.class.getSimpleName();
    public static final String ARG_GAME_TITLE = "game";
    public static final String ARG_RUN = "run";
    private static final int COLOR_VIEWPORT_BORDER_DEFAULT = Color.WHITE;

    transient private ObjectAnimator animatorBackgroundColor;
    transient private FrameLayout frameLayoutUsedAsBorderForViewport;
    private ViewportFragment viewportFragment;
    transient private MySurfaceView mySurfaceView;
    private StatsDisplayerFragment statsDisplayerFragment;
    transient private GamePadFragment gamePadFragment;
    transient private DirectionPadFragment directionPadFragment;
    transient private ButtonPadFragment buttonPadFragment;

    private String gameTitle;
    transient private Game game;
    private com.jackingaming.thestraylightrun.accelerometer.game.Game.Run run;
    transient private InputManager inputManager;

    public GameConsoleFragment() {
        // Required empty public constructor
    }

    public static GameConsoleFragment newInstance(String gameTitle, com.jackingaming.thestraylightrun.accelerometer.game.Game.Run run) {
        GameConsoleFragment fragment = new GameConsoleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GAME_TITLE, gameTitle);
        args.putSerializable(ARG_RUN, run);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        inputManager = new InputManager();

        viewportFragment = ViewportFragment.newInstance(null, null);
        statsDisplayerFragment = StatsDisplayerFragment.newInstance(null, null);
        gamePadFragment = GamePadFragment.newInstance(null, null);
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

        frameLayoutUsedAsBorderForViewport = view.findViewById(R.id.fcv_mysurfaceview);

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

        frameLayoutUsedAsBorderForViewport.setBackgroundColor(COLOR_VIEWPORT_BORDER_DEFAULT);
        animatorBackgroundColor = ObjectAnimator.ofInt(frameLayoutUsedAsBorderForViewport, "backgroundColor", Color.WHITE, Color.RED);
        animatorBackgroundColor.setDuration(400L);
        animatorBackgroundColor.setEvaluator(new ArgbEvaluator());
        animatorBackgroundColor.setRepeatMode(ValueAnimator.REVERSE);
        animatorBackgroundColor.setRepeatCount(ValueAnimator.INFINITE);
//        animatorBackgroundColor.start();

        mySurfaceView = viewportFragment.getView().findViewById(R.id.mysurfaceview_game_console_fragment);
        mySurfaceView.setMySurfaceViewSurfaceChangeListener(this);
        mySurfaceView.setMySurfaceViewTouchListener(inputManager);

        statsDisplayerFragment.setButtonHolderClickListener(this);
        statsDisplayerFragment.setIconClickListener(this);

        directionPadFragment = (DirectionPadFragment) gamePadFragment.getChildFragmentManager().findFragmentById(R.id.directionpadfragment_game_pad_fragment);
        directionPadFragment.setDirectionPadListener(inputManager);
        buttonPadFragment = (ButtonPadFragment) gamePadFragment.getChildFragmentManager().findFragmentById(R.id.buttonpadfragment_game_pad_fragment);
        buttonPadFragment.setButtonPadListener(inputManager);

        Bundle arguments = getArguments();
        if (arguments != null) {
            //////////////////////////////////////////
            gameTitle = arguments.getString(ARG_GAME_TITLE);
            run = (com.jackingaming.thestraylightrun.accelerometer.game.Game.Run) arguments.getSerializable(ARG_RUN);
            Log.d(TAG, getClass().getSimpleName() + ".onActivityCreated(Bundle savedInstanceState) gameTitle selected is: " + gameTitle);
            //////////////////////////////////////////
            Assets.init(getResources());
            game = new Game(gameTitle, run, getChildFragmentManager());
        }

        game.setViewportListener(this);
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

    @Override
    public void startBlinkingBorder() {
        if (!animatorBackgroundColor.isRunning()) {
            animatorBackgroundColor.start();
        }
    }

    @Override
    public void stopBlinkingBorder() {
        if (animatorBackgroundColor.isRunning()) {
            animatorBackgroundColor.cancel();
            frameLayoutUsedAsBorderForViewport.setBackgroundColor(COLOR_VIEWPORT_BORDER_DEFAULT);
        }
    }

    @Override
    public boolean isBlinkingBorderOn() {
        return animatorBackgroundColor.isRunning();
    }

    private ParticleExplosionView particleExplosionView;

    @Override
    public void addAndShowParticleExplosionView() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Stop previous particleExplosionView before starting new particleExplosionView.
                // This prevents overlap-bug (immediately finishing multiple quests,
                // one-after-another, caused previous particleExplosionView to stay on screen).
                if (particleExplosionView != null && particleExplosionView.getVisibility() == View.VISIBLE) {
                    particleExplosionView.setVisibility(View.INVISIBLE);
                    frameLayoutUsedAsBorderForViewport.removeView(particleExplosionView);
                }

                particleExplosionView = new ParticleExplosionView(getContext());
                particleExplosionView.initParticles();
                particleExplosionView.setX(
                        GameCamera.getInstance().convertInGameXPositionToScreenXPosition(
                                Player.getInstance().getX()
                        )
                );
                particleExplosionView.setY(
                        GameCamera.getInstance().convertInGameYPositionToScreenYPosition(
                                Player.getInstance().getY()
                        )
                );
                particleExplosionView.setZ(1f);
                particleExplosionView.setVisibility(View.INVISIBLE);

                frameLayoutUsedAsBorderForViewport.addView(particleExplosionView,
                        new FrameLayout.LayoutParams(
                                (int) (Tile.WIDTH * GameCamera.getInstance().getWidthPixelToViewportRatio()),
                                (int) (Tile.HEIGHT * GameCamera.getInstance().getHeightPixelToViewportRatio())
                        )
                );
                frameLayoutUsedAsBorderForViewport.invalidate();

                ObjectAnimator animatorExplosion = ObjectAnimator.ofFloat(particleExplosionView, "progress", 0.0f, 1.0f);
                animatorExplosion.setInterpolator(new LinearInterpolator());
                animatorExplosion.setDuration(1000L);
                animatorExplosion.setRepeatCount(ValueAnimator.INFINITE);
                animatorExplosion.setRepeatMode(ValueAnimator.REVERSE);
                animatorExplosion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        particleExplosionView.updateProgressOfParticles(
                                particleExplosionView.getProgress()
                        );
                    }
                });

                startWaitTimer(4200L, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.e(TAG, "waitTimer's onAnimationEnd()");

                        particleExplosionView.setVisibility(View.INVISIBLE);
                        frameLayoutUsedAsBorderForViewport.removeView(particleExplosionView);
                    }
                });

                particleExplosionView.setVisibility(View.VISIBLE);
                animatorExplosion.start();
            }
        });
    }

    private void startWaitTimer(long duration, AnimatorListenerAdapter animatorListenerAdapter) {
        Log.e(TAG, "startWaitTimer()");
        ObjectAnimator animatorDummyValue = ObjectAnimator.ofFloat(this, "dummyValue", 0f, 1f);
        animatorDummyValue.setInterpolator(new LinearInterpolator());
        animatorDummyValue.setDuration(duration);
        animatorDummyValue.addListener(animatorListenerAdapter);
        animatorDummyValue.start();
    }

    private float dummyValue = 0f;

    public float getDummyValue() {
        return dummyValue;
    }

    public void setDummyValue(float dummyValue) {
        this.dummyValue = dummyValue;
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

        game.updateCurrency();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (game.getItemStoredInButtonHolderA() != null) {
                    statsDisplayerFragment.setImageAndQuantityForButtonHolderA(
                            game.findItemStackableViaItem(
                                    game.getItemStoredInButtonHolderA()
                            )
                    );
                }
                if (game.getItemStoredInButtonHolderB() != null) {
                    statsDisplayerFragment.setImageaAndQuantityForButtonHolderB(
                            game.findItemStackableViaItem(
                                    game.getItemStoredInButtonHolderB()
                            )
                    );
                }
            }
        });
    }

    @Override
    public void onCurrencyChange(float currency) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                statsDisplayerFragment.setCurrency(currency);
            }
        });
    }

    @Override
    public void onTimeChange(String inGameClockTime, String calendarText) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setTime(inGameClockTime, calendarText);
        } else {
//            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
    }

    @Override
    public void onButtonHolderAChange(Item itemA) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setImageAndQuantityForButtonHolderA(
                    game.findItemStackableViaItem(
                            itemA
                    )
            );

            // used for RunOne's "tutorial"
            if (itemA instanceof RobotReprogrammer4000) {
                checkIfFirstTimeEquippingRobotReprogrammer4000();
            }
        } else {
            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
    }

    public void checkIfFirstTimeEquippingRobotReprogrammer4000() {
        if (SceneFarm.getInstance().isFirstTimeEquippingRobotReprogrammer4000()) {
            Log.d(TAG, "SceneFarm.getInstance().isFirstTimeEquippingRobotReprogrammer4000()");
            SceneFarm.getInstance().setFirstTimeEquippingRobotReprogrammer4000(
                    false
            );

            game.getViewportListener().addAndShowParticleExplosionView();

            Bitmap portrait = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.group_chat_image_nwt_host);
            String text = getResources().getString(R.string.equip_robot_reprogrammer_4000);
            TypeWriterDialogFragment typeWriterDialogFragment = TypeWriterDialogFragment.newInstance(
                    50L, portrait, text,
                    new TypeWriterDialogFragment.DismissListener() {
                        @Override
                        public void onDismiss() {
                            Log.e(TAG, "TextboxListener.showTextbox( TypeWriterDialogFragment.onDismiss() )");
                        }
                    }, new TypeWriterTextView.TextCompletionListener() {
                        @Override
                        public void onAnimationFinish() {
                            Log.e(TAG, "TextboxListener.showTextbox( TypeWriterDialogFragment.onAnimationFinish() )");
                        }
                    }
            );

            SceneFarm.getInstance().setInTutorialEquipRobotReprogrammer4000(
                    true
            );
            game.getTextboxListener().showTextbox(typeWriterDialogFragment);
        } else {
            Log.d(TAG, "NOT SceneFarm.getInstance().isFirstTimeEquippingRobotReprogrammer4000()");

            // do nothing;
        }
    }

    @Override
    public void onButtonHolderBChange(Item itemB) {
        Fragment fragmentInMiddleContainer = getChildFragmentManager().findFragmentById(R.id.fcv_statsdisplayerfragment);
        if (fragmentInMiddleContainer instanceof StatsDisplayerFragment) {
            statsDisplayerFragment.setImageaAndQuantityForButtonHolderB(
                    game.findItemStackableViaItem(
                            itemB
                    )
            );

            // used for RunOne's "tutorial"
            if (itemB instanceof RobotReprogrammer4000) {
                checkIfFirstTimeEquippingRobotReprogrammer4000();
            }
        } else {
            Log.e(TAG, "fragmentInMiddleContainer NOT instanceof StatsDisplayerFragment");
        }
    }

    @Override
    public void onRefreshQuantityInButtonHolderAAndB(ItemStackable stackableA, ItemStackable stackableB) {
        statsDisplayerFragment.refreshTvQuantityInButtonHolderAAndB(stackableA, stackableB);
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
        Log.d(TAG, "onButtonHolderClicked(buttonHolder): buttonHolder=" + buttonHolder.name());

        if (game.getStateManager().getCurrentState() instanceof GameState) {
            Log.d(TAG, "game.getStateManager().getCurrentState() instanceof GameState");

            game.doClickButtonHolder(buttonHolder);
        } else {
            Log.d(TAG, "NOT game.getStateManager().getCurrentState() instanceof GameState");

            // Do nothing.
        }
    }

    @Override
    public void onIconClicked(View view) {
        Log.d(TAG, "onIconClicked(view): view.getTag()=" + view.getTag());

        if (game.getStateManager().getCurrentState() instanceof GameState) {
            Log.d(TAG, "game.getStateManager().getCurrentState() instanceof GameState");

            game.doClickIcon(view, getChildFragmentManager());
        } else {
            Log.d(TAG, "NOT game.getStateManager().getCurrentState() instanceof GameState");

            // Do nothing.
        }
    }
}