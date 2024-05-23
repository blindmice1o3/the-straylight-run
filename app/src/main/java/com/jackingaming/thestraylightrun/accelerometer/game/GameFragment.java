package com.jackingaming.thestraylightrun.accelerometer.game;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.AppBarLayout;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.BottomDrawerDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerEndFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerStartFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerTopFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment
        implements SensorEventListener {
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
    private DrawerEndFragment drawerEndFragment;
    private DrawerTopFragment drawerTopFragment;
    private World world;

    private SensorManager sensorManager;
    private SoundManager soundManager;

    private int widthDeviceScreen, heightDeviceScreen;
    private int widthSpriteDst, heightSpriteDst;

    private Map<Entity, ImageView> imageViewViaEntity;
    private Player player;
    private float xAccelPrevious, yAccelPrevious = 0f;
    private float xVel, yVel = 0.0f;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    private static final int NUMBER_OF_TILES_ON_SHORTER_SIDE = 12;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = view.findViewById(R.id.app_bar_layout);
        world = view.findViewById(R.id.frame_layout_world);

        if (savedInstanceState == null) {
            drawerStartFragment = DrawerStartFragment.newInstance(null, null, new DrawerStartFragment.DrawerStartListener() {
                @Override
                public void onClickTypeWriterTextView(View view, String tag) {
                    String message = "DrawerStartFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
                    ((TypeWriterTextView) view).displayTextWithAnimation(message);
                }
            });
            drawerEndFragment = DrawerEndFragment.newInstance(null, null, new DrawerEndFragment.DrawerEndListener() {
                @Override
                public void onClickTypeWriterTextView(View view, String tag) {
                    String message = "DrawerEndFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
                    ((TypeWriterTextView) view).displayTextWithAnimation(message);
                }
            });
            drawerTopFragment = DrawerTopFragment.newInstance(null, null, new DrawerTopFragment.DrawerTopListener() {
                @Override
                public void onClickTypeWriterTextView(View view, String tag) {
                    String message = "DrawerTopFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
                    ((TypeWriterTextView) view).displayTextWithAnimation(message);
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

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        soundManager = new SoundManager(getContext());

        Point sizeDisplay = new Point();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(sizeDisplay);
        widthDeviceScreen = sizeDisplay.x;
        heightDeviceScreen = sizeDisplay.y;
        Log.e(TAG, "widthDeviceScreen, heightDeviceScreen: " + widthDeviceScreen + ", " + heightDeviceScreen);
        widthSpriteDst = Math.min(widthDeviceScreen, heightDeviceScreen) / NUMBER_OF_TILES_ON_SHORTER_SIDE;
        heightSpriteDst = widthSpriteDst;
        Log.e(TAG, "widthSpriteDst, heightSpriteDst: " + widthSpriteDst + ", " + heightSpriteDst);

        // TODO: overall sequence: Models -> ModelToViewMapper -> Views
        // TODO: Model
        world.setListener(new World.ShowDialogListener() {
            @Override
            public FragmentManager onShowDialog() {
                return getChildFragmentManager();
            }
        });
        world.init(widthDeviceScreen, heightDeviceScreen,
                widthSpriteDst, heightSpriteDst,
                R.raw.tiles_world_map,
                R.drawable.pokemon_gsc_kanto,
                soundManager,
                new OnSwipeListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent event) {
                        soundManager.sfxIterateAndPlay();

                        appBarLayout.setExpanded(false);

                        return super.onSingleTapUp(event);
                    }

                    @Override
                    public void onLongPress(MotionEvent event) {
                        super.onLongPress(event);
                        soundManager.backgroundMusicTogglePause();
                    }

                    @Override
                    public boolean onSwipe(Direction direction, int yInit) {
                        if (direction == OnSwipeListener.Direction.up &&
                                yInit > (heightDeviceScreen - 100)) {
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
                        return false;
                    }
                },
                replaceFragmentListener);

        // TODO: ModelToViewMapper
        List<Entity> entitiesFromWorld = world.getEntities();
        imageViewViaEntity = new HashMap<>();
        for (Entity e : entitiesFromWorld) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(e.getAnimationDrawableBasedOnDirection());
            if (e instanceof Player) {
                player = (Player) e;
            } else {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Entity e : imageViewViaEntity.keySet()) {
                            if (imageViewViaEntity.get(e) == view) {
                                ((NonPlayableCharacter) e).toggleStationary();
                            }
                        }
                    }
                });
            }
            imageViewViaEntity.put(e, imageView);
        }
        // TODO: View
        for (ImageView imageView : imageViewViaEntity.values()) {
            world.addView(imageView, new FrameLayout.LayoutParams(widthSpriteDst, heightSpriteDst));
        }
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        TypeWriterDialogFragment typeWriterDialogFragment = (TypeWriterDialogFragment) getChildFragmentManager().findFragmentByTag(TypeWriterDialogFragment.TAG);
        if (typeWriterDialogFragment != null && typeWriterDialogFragment.isVisible()) {
            return;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAccel = -sensorEvent.values[0];
            float yAccel = sensorEvent.values[1];

//            Log.e(TAG, String.format("(xAccel, yAccel): (%f, %f)", (xAccel / Math.abs(xAccel)), (yAccel / Math.abs(yAccel))));

            float xAccelDelta = xAccel - xAccelPrevious;
            float yAccelDelta = yAccel - yAccelPrevious;

            float frameTime = 0.666f;
            xVel += (xAccelDelta * frameTime);
            yVel += (yAccelDelta * frameTime);

//            Log.e(TAG, String.format("(xVel, yVel): (%f, %f)", xVel, yVel));
//            Log.e(TAG, String.format("((xVel / Math.abs(xVel)), (yVel / Math.abs(yVel))): (%f, %f)", (xVel / Math.abs(xVel)), (yVel / Math.abs(yVel))));

            float xDelta = (xVel / 2) * frameTime;
            float yDelta = (yVel / 2) * frameTime;

//            Log.e(TAG, String.format("(xDelta, yDelta): (%f, %f)", xDelta, yDelta));
            updateGameEntities(xDelta, yDelta);
            world.getGameCamera().centerOnEntity(player);
            world.invalidate();

            // Prepare for next sensor event
            xAccelPrevious = xAccel;
            yAccelPrevious = yAccel;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Intentionally blank.
    }

    private void updateGameEntities(float xDelta, float yDelta) {
        for (Entity e : imageViewViaEntity.keySet()) {
            // DO MOVE.
            if (e instanceof Player) {
                ((Player) e).updateViaSensorEvent(xDelta, yDelta);
            } else {
                e.update();
            }
            // VALIDATE MOVE.
            e.validatePosition(
                    world.getWidthWorldInPixels(),
                    world.getHeightWorldInPixels()
            );

            ImageView ivEntity = imageViewViaEntity.get(e);
            // IMAGE (based on speed bonus)
            if (e.getSpeedBonus() > Entity.DEFAULT_SPEED_BONUS) {
                ivEntity.setAlpha(0.5f);
            }

            // IMAGE (based on direction)
            if (e instanceof NonPlayableCharacter &&
                    ((NonPlayableCharacter) e).isStationary()) {
                ivEntity.setImageDrawable(e.getPausedFrameBasedOnDirection());
            } else {
                ivEntity.setImageDrawable(e.getAnimationDrawableBasedOnDirection());
            }

            // POSITION
            ivEntity.setX(e.getxPos() - world.getGameCamera().getxOffset());
            ivEntity.setY(e.getyPos() - world.getGameCamera().getyOffset());
        }
    }
}