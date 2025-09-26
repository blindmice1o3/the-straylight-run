package com.jackingaming.thestraylightrun.accelerometer.game;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.BottomDrawerDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerEndFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerStartFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerTopFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.WorldScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveLeftCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveRightCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MoveUpCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands.MovementCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.sandbox.particleexplosion.ParticleExplosionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment
        implements SensorEventListener,
        GameView.SurfaceCreatedListener {
    public static final String TAG = GameFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_REPLACE_FRAGMENT_LISTENER = "replaceFragmentListener";

    public interface ReplaceFragmentListener extends Serializable {
        void onReplaceFragment(Fragment newFragment);
    }

    private ReplaceFragmentListener replaceFragmentListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AppBarLayout appBarLayout;
    private DrawerStartFragment drawerStartFragment;
    private View drawerGripStart;
    private View drawerGripEnd;
    private ObjectAnimator animatorDrawerGripStart;
    private ObjectAnimator animatorDrawerGripEnd;
    private DrawerEndFragment drawerEndFragment;
    private DrawerTopFragment drawerTopFragment;
    private FrameLayout frameLayout; // displays entities.
    private GestureDetector gestureDetector; // opens BottomDrawerDialogFragment.
    private GameView surfaceView; // displays tiles.

    private SensorManager sensorManager;
    private SoundManager soundManager;

    private Map<Entity, ImageView> imageViewViaEntity;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2,
                                           ReplaceFragmentListener replaceFragmentListener) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_REPLACE_FRAGMENT_LISTENER, replaceFragmentListener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            replaceFragmentListener = (ReplaceFragmentListener) getArguments().getSerializable(ARG_REPLACE_FRAGMENT_LISTENER);
        }

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imageViewViaEntity = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    void highlightGroupChatDrawer() {
        Log.e(TAG, "highlightGroupChatDrawer");
        drawerGripStart.setVisibility(View.VISIBLE);
        animatorDrawerGripStart.start();
    }

    void highlightJournalDrawer() {
        drawerGripEnd.setVisibility(View.VISIBLE);
        animatorDrawerGripEnd.start();
    }

    void unhighlightGroupChatDrawer() {
        drawerGripStart.setVisibility(View.INVISIBLE);
        animatorDrawerGripStart.cancel();
    }

    void unhighlightJournalDrawer() {
        drawerGripEnd.setVisibility(View.INVISIBLE);
        animatorDrawerGripEnd.cancel();
    }

    public static final float THRESHOLD_SLIDE_OFFSET = 0.05f;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                // triggered when opened partially.
                if (slideOffset >= THRESHOLD_SLIDE_OFFSET) {
                    RecyclerView rvDrawerStart = drawerView.findViewById(R.id.rv_drawer_start);
                    if (rvDrawerStart != null) {
                        Log.e(TAG, "drawer START met THRESHOLD_SLIDE_OFFSET");

                        unhighlightGroupChatDrawer();
                    } else {
                        Log.e(TAG, "drawer END met THRESHOLD_SLIDE_OFFSET");

                        unhighlightJournalDrawer();
                    }
                } else {
                    Log.e(TAG, "THRESHOLD_SLIDE_OFFSET not met!!!");
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                // triggered when opened completely.
                RecyclerView rvDrawerStart = drawerView.findViewById(R.id.rv_drawer_start);
                if (rvDrawerStart != null) {
                    Log.e(TAG, "drawer START opened");

                    drawerStartFragment.startMessageQueue(
                            gameListener.getRun()
                    );
                } else {
                    Log.e(TAG, "drawer END opened");

                    drawerEndFragment.updateJournalPrompt(
                            gameListener.getRun()
                    );
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                RecyclerView rvDrawerStart = drawerView.findViewById(R.id.rv_drawer_start);
                if (rvDrawerStart != null) {
                    Log.e(TAG, "drawer START closed");

                    //////////////////////////////////
                    if (game.getDailyLoop() == Game.DailyLoop.GROUP_CHAT) {
                        game.resetGroupChatState();
                        game.incrementDailyLoop();
                    }
                    //////////////////////////////////
                } else {
                    Log.e(TAG, "drawer END closed");

                    //////////////////////////////////
                    if (game.getDailyLoop() == Game.DailyLoop.JOURNAL) {
                        game.resetJournalState();
                        game.incrementDailyLoop();
                    }
                    //////////////////////////////////
                }

                DrawerStartFragment drawerStartFragment = (DrawerStartFragment) getChildFragmentManager().findFragmentById(R.id.fcv_drawer_start);
                DrawerEndFragment drawerEndFragment = (DrawerEndFragment) getChildFragmentManager().findFragmentById(R.id.fcv_drawer_end);
                if (drawerStartFragment != null) {
                    // TODO:
                }

                if (drawerEndFragment != null) {
                    // TODO:
                }
            }
        });
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        frameLayout = view.findViewById(R.id.frame_layout_world);
        surfaceView = view.findViewById(R.id.surface_view_game);
        surfaceView.setListener(this);

        drawerGripStart = view.findViewById(R.id.drawer_grip_start);
        drawerGripEnd = view.findViewById(R.id.drawer_grip_end);

        drawerGripStart.setVisibility(View.INVISIBLE);
        drawerGripEnd.setVisibility(View.INVISIBLE);

        animatorDrawerGripStart = ObjectAnimator.ofFloat(drawerGripStart, "alpha", 1f, 0f);
        animatorDrawerGripStart.setDuration(500L);
        animatorDrawerGripStart.setRepeatMode(ValueAnimator.REVERSE);
        animatorDrawerGripStart.setRepeatCount(ValueAnimator.INFINITE);

        animatorDrawerGripEnd = ObjectAnimator.ofFloat(drawerGripEnd, "alpha", 1f, 0f);
        animatorDrawerGripEnd.setDuration(500L);
        animatorDrawerGripEnd.setRepeatMode(ValueAnimator.REVERSE);
        animatorDrawerGripEnd.setRepeatCount(ValueAnimator.INFINITE);

        if (savedInstanceState == null) {
            drawerStartFragment = DrawerStartFragment.newInstance(null, null);
            drawerEndFragment = DrawerEndFragment.newInstance(null, null, new DrawerEndFragment.DrawerEndListener() {
                @Override
                public void onSubmitJournalEntry(View view, String journalEntry) {
                    // TODO: Save to local db, file, or in-memory list.
                    Toast.makeText(getContext(), "Journal saved!", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            });
            drawerTopFragment = DrawerTopFragment.newInstance(null, null,
                    new DrawerTopFragment.DrawerTopListener() {
                        @Override
                        public void onClickTypeWriterTextView(View view, String tag) {
                            String message = "DrawerTopFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
                            ((TypeWriterTextView) view).displayTextWithAnimation(message);
                        }
                    }, new DrawerTopFragment.RunSelectionListener() {
                        @Override
                        public void onRunSelected(Game.Run run) {
                            switch (run) {
                                case ONE:
                                    game.setRun(Game.Run.ONE);
                                    break;
                                case TWO:
                                    game.setRun(Game.Run.TWO);
                                    break;
                                case THREE:
                                    game.setRun(Game.Run.THREE);
                                    break;
                                case FOUR:
                                    game.setRun(Game.Run.FOUR);
                                    break;
                                case FIVE:
                                    game.setRun(Game.Run.FIVE);
                                    break;
                            }
                        }

                        @Override
                        public void onCloseDrawerTop() {
                            appBarLayout.setExpanded(false);

                            DrawerTopFragment drawerTopFragment = (DrawerTopFragment) getChildFragmentManager().findFragmentById(R.id.fcv_drawer_top);
                            drawerTopFragment.stopTypeWriterTextView();
                        }
                    });
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fcv_drawer_start,
                            drawerStartFragment,
                            DrawerStartFragment.TAG)
                    .add(R.id.fcv_drawer_end,
                            drawerEndFragment,
                            DrawerEndFragment.TAG)
                    .add(R.id.fcv_drawer_top,
                            drawerTopFragment,
                            DrawerTopFragment.TAG)
                    .commit();
        } else {
            drawerStartFragment = (DrawerStartFragment) getChildFragmentManager().findFragmentByTag(DrawerStartFragment.TAG);
            drawerEndFragment = (DrawerEndFragment) getChildFragmentManager().findFragmentByTag(DrawerEndFragment.TAG);
            drawerTopFragment = (DrawerTopFragment) getChildFragmentManager().findFragmentByTag(DrawerTopFragment.TAG);
        }

        OnSwipeListener bottomDrawerSwipeListener = new OnSwipeListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                soundManager.sfxIterateAndPlay();

                appBarLayout.setExpanded(false);

                DrawerTopFragment drawerTopFragment = (DrawerTopFragment) getChildFragmentManager().findFragmentById(R.id.fcv_drawer_top);
                drawerTopFragment.stopTypeWriterTextView();

                return super.onSingleTapUp(event);
            }

            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);
                soundManager.backgroundMusicTogglePause();
            }

            @Override
            public boolean onSwipe(Direction direction, int yInit) {
                if (direction == OnSwipeListener.Direction.up) {
                    Point sizeDisplay = new Point();
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    display.getSize(sizeDisplay);
                    int widthDeviceScreen = sizeDisplay.x;
                    int heightDeviceScreen = sizeDisplay.y;

                    if (yInit > (heightDeviceScreen - 100)) {
                        BottomDrawerDialogFragment.newInstance(new BottomDrawerDialogFragment.DrawerBottomListener() {
                                    @Override
                                    public void onClickTypeWriterTextView(View view, String tag) {
                                        String message = "BottomDrawerDialogFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
                                        ((TypeWriterTextView) view).displayTextWithAnimation(message);
                                    }
                                })
                                .show(getChildFragmentManager(), BottomDrawerDialogFragment.TAG);
                        return true;
                    }
                }
                return false;
            }
        };

        gestureDetector = new GestureDetector(getContext(), bottomDrawerSwipeListener);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        soundManager = new SoundManager(getContext());

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector != null) {
                    return gestureDetector.onTouchEvent(motionEvent);
                } else {
                    return false;
                }
            }
        });
    }

    private Game game;
    private Game.GameListener gameListener;

    @Override
    public void onSurfaceCreated(Game game) {
        this.game = game;

        gameListener = new Game.GameListener() {
            @Override
            public void onUpdateEntity(Entity e) {
                ImageView ivEntity = imageViewViaEntity.get(e);

                // IMAGE (based on speed bonus)
                if (e instanceof Player) {
                    if (((Player) e).isMovementSpeedIncreased()) {
                        ivEntity.setAlpha(0.5f);
                    }
                }

                // IMAGE (based on direction)
                if (e instanceof NonPlayableCharacter &&
                        ((NonPlayableCharacter) e).isStationary()) {
                    ivEntity.setImageDrawable(e.getPausedFrameBasedOnDirection());
                } else {
                    ivEntity.setImageDrawable(e.getAnimationDrawableBasedOnDirection());
                }

                // POSITION
                ivEntity.setX(e.getXPos() - game.getGameCamera().getxOffset());
                ivEntity.setY(e.getYPos() - game.getGameCamera().getyOffset());

                ivEntity.invalidate();
            }

            @Override
            public float[] onCheckAccelerometer() {
                float[] dataAccelerometer = new float[2];
                dataAccelerometer[0] = xDelta;
                dataAccelerometer[1] = yDelta;
                return dataAccelerometer;
            }

            @Override
            public void onShowDialogFragment(DialogFragment dialogFragment, String tag) {
                dialogFragment.show(getChildFragmentManager(), tag);
            }

            @Override
            public void onReplaceFragmentInMainActivity(Fragment fragment) {
                replaceFragmentListener.onReplaceFragment(fragment);
            }

            @Override
            public void onChangeScene(Scene sceneNext) {
                game.changeScene(sceneNext);
            }

            @Override
            public void instantiateImageViewForEntities(List<Entity> entitiesToAdd) {
                GameFragment.this.instantiateImageViewForEntities(
                        game.getSceneCurrent().getEntities()
                );
            }

            @Override
            public void instantiateParticleExplosionForPlayer(List<Entity> entitiesToAdd,
                                                              int widthSpriteDst, int heightSpriteDst) {
                GameFragment.this.instantiateParticleExplosionViewForPlayer(entitiesToAdd, game,
                        widthSpriteDst, heightSpriteDst
                );
            }

            @Override
            public void startParticleExplosionViewForPlayer() {
                GameFragment.this.startParticleExplosionViewForPlayer();
            }

            @Override
            public void addImageViewOfEntityToFrameLayout(int widthSpriteDst, int heightSpriteDst) {
                GameFragment.this.addImageViewOfEntityToFrameLayout(widthSpriteDst, heightSpriteDst);
            }

            @Override
            public void removeImageViewOfEntityFromFrameLayout() {
                GameFragment.this.removeImageViewOfEntityFromFrameLayout();
            }

            @Override
            public Game.Run getRun() {
                return game.getRun();
            }

            @Override
            public Game.DailyLoop getDailyLoop() {
                return game.getDailyLoop();
            }

            @Override
            public void incrementDailyLoop() {
                game.incrementDailyLoop();
            }

            @Override
            public void highlightGroupChatDrawer() {
                GameFragment.this.highlightGroupChatDrawer();
            }

            @Override
            public void highlightJournalDrawer() {
                GameFragment.this.highlightJournalDrawer();
            }

            @Override
            public void unhighlightGroupChatDrawer() {
                GameFragment.this.unhighlightGroupChatDrawer();
            }

            @Override
            public void unhighlightJournalDrawer() {
                GameFragment.this.unhighlightJournalDrawer();
            }
        };

        game.init(soundManager, gameListener);

//        // TODO: ModelToViewMapper
//        List<Entity> entitiesToAdd = game.getSceneCurrent().getEntities();
//        instantiateImageViewForEntities(entitiesToAdd);
//        addImageViewOfEntityToFrameLayout(game.getWidthSpriteDst(), game.getHeightSpriteDst());
    }

    private void instantiateParticleExplosionViewForPlayer(List<Entity> entitiesToAdd, Game game,
                                                           int widthSpriteDst, int heightSpriteDst) {
        for (Entity e : entitiesToAdd) {
            if (e instanceof Player) {
                particleExplosionView = new ParticleExplosionView(getContext());
                particleExplosionView.setX(
                        ((Player) e).getXPos() - game.getGameCamera().getxOffset()
                );
                particleExplosionView.setY(
                        ((Player) e).getYPos() - game.getGameCamera().getyOffset()
                );
                particleExplosionView.setZ(1f);
                particleExplosionView.setVisibility(View.INVISIBLE);
                frameLayout.addView(particleExplosionView, new FrameLayout.LayoutParams(widthSpriteDst, heightSpriteDst));
                frameLayout.invalidate();

                animatorExplosion = ObjectAnimator.ofFloat(particleExplosionView, "progress", 0.0f, 1.0f);
                animatorExplosion.setInterpolator(new LinearInterpolator());
                animatorExplosion.setDuration(500L);
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
            }
        }
    }

    private void startParticleExplosionViewForPlayer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                particleExplosionView.setVisibility(View.VISIBLE);
                animatorExplosion.start();
            }
        });
    }

    private ObjectAnimator animatorExplosion;
    private ParticleExplosionView particleExplosionView;

    private void instantiateImageViewForEntities(List<Entity> entitiesToAdd) {
        for (Entity e : entitiesToAdd) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(e.getAnimationDrawableBasedOnDirection());
            if (!(e instanceof Player)) {
                if (e instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) e).getId().equals(WorldScene.ID_BUG_CATCH)) {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (((NonPlayableCharacter) e).isStationary()) {
                                    Handler handler = new Handler();
                                    List<MovementCommand> movementCommands = new ArrayList<>();
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveLeftCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveRightCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveLeftCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveUpCommand(e, handler));
                                    movementCommands.add(new MoveRightCommand(e, handler));

                                    e.appendMovementCommands(movementCommands);
                                    ((NonPlayableCharacter) e).turnStationaryOff();
                                    e.runMovementCommands();
                                }
                            }
                        });
                    } else {
                        // ALL NPCs other than bug catcher.
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((NonPlayableCharacter) e).toggleStationary();
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "instantiateImageViewForEntities() else-clause (NOT NonPlayableCharacter), do NOT define click listener. Entity's class: " + e.getClass().getSimpleName());
                }
            }
            imageViewViaEntity.put(e, imageView);
        }
    }

    private void addImageViewOfEntityToFrameLayout(int widthSpriteDst, int heightSpriteDst) {
        // TODO: View
        for (Entity e : imageViewViaEntity.keySet()) {
            ImageView ivToAdd = imageViewViaEntity.get(e);
            frameLayout.addView(ivToAdd, new FrameLayout.LayoutParams(widthSpriteDst, heightSpriteDst));
        }
        frameLayout.invalidate();
    }

    private void removeImageViewOfEntityFromFrameLayout() {
        for (Entity e : imageViewViaEntity.keySet()) {
            ImageView ivToRemove = imageViewViaEntity.get(e);
            frameLayout.removeView(ivToRemove);
        }
        frameLayout.invalidate();

        imageViewViaEntity.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        soundManager.onStart();

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    // "Perform method calls [BEFORE] the call to the superclass.
    // This is because we'd like to unregister our listener
    // [before] the system does it's tasks to free up resources"
    // -https://gamedevacademy.org/android-sensors-game-tutorial/
    @Override
    public void onStop() {
        sensorManager.unregisterListener(this);
        soundManager.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        soundManager.onDestroy();
        super.onDestroy();
    }

    private float xDelta, yDelta;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        TypeWriterDialogFragment typeWriterDialogFragment = (TypeWriterDialogFragment) getChildFragmentManager().findFragmentByTag(TypeWriterDialogFragment.TAG);
        if (typeWriterDialogFragment != null && typeWriterDialogFragment.isVisible()) {
            return;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAccel = -sensorEvent.values[0];
            float yAccel = sensorEvent.values[1];

            // Going full-tilt (assigning xAccel straight to xDelta)
            // is too fast, damping to be only 25% of full-tilt.
            xDelta = xAccel * 0.25f;
            yDelta = yAccel * 0.25f;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Intentionally blank.
    }
}