package com.jackingaming.thestraylightrun.accelerometer.game;

import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.DOWN;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.LEFT;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.RIGHT;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.UP;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.choiceboxes.ChoiceDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.BottomDrawerDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerEndFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerStartFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.drawers.DrawerTopFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.controllables.Controllable;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.Tile;
import com.jackingaming.thestraylightrun.sequencetrainer.SequenceTrainerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity
        implements SensorEventListener,
        DrawerStartFragment.DrawerStartListener,
        DrawerEndFragment.DrawerEndListener,
        DrawerTopFragment.DrawerTopListener,
        BottomDrawerDialogFragment.DrawerBottomListener {
    public static final String TAG = GameActivity.class.getSimpleName();

    private DrawerStartFragment drawerStartFragment;
    private DrawerEndFragment drawerEndFragment;
    private DrawerTopFragment drawerTopFragment;

    private SoundManager soundManager;
    private AppBarLayout appBarLayout;
    // TODO: change FrameLayout to World
    private FrameLayout frameLayout;
    private int xScreenSize, yScreenSize;
    private GameCamera gameCamera;
    private Map<Entity, ImageView> imageViewViaEntity;
    private Bitmap[][] sprites;
    private Bitmap spriteCoin;
    private Bitmap tileSolid, tileWalkable, tileBoulder;

    private Controllable controllable;
    private SensorManager sensorManager;
    private float xAccelPrevious, yAccelPrevious = 0f;
    private float xVel, yVel = 0.0f;
    private int widthSpriteDst, heightSpriteDst;

    private static final int WIDTH_SPRITE_SHEET_ACTUAL = 187;
    private static final int HEIGHT_SPRITE_SHEET_ACTUAL = 1188;
    private static final int COLUMNS = 10;
    private static final int ROWS = 56; // section: Characters
    private static final int X_OFFSET_INIT = 9;
    private static final int Y_OFFSET_INIT = 34;
    private static final int WIDTH_SPRITE = 16;
    private static final int HEIGHT_SPRITE = 16;
    private static final int WIDTH_DIVIDER = 1;
    private static final int HEIGHT_DIVIDER = 1;

    private void initSprites(int widthSpriteDst, int heightSpriteDst) {
        sprites = new Bitmap[COLUMNS][ROWS];

        Bitmap spriteSheet = BitmapFactory.decodeResource(getResources(),
                R.drawable.gbc_pokemon_red_blue_characters_overworld);

        float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL;
        float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL;

        int widthSpriteConverted = (int) (WIDTH_SPRITE * ratioHorizontal);
        int heightSpriteConverted = (int) (HEIGHT_SPRITE * ratioVertical);

        int yOffset = Y_OFFSET_INIT;
        int xOffset = X_OFFSET_INIT;
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                int xOffsetConverted = (int) (xOffset * ratioHorizontal);
                int yOffsetConverted = (int) (yOffset * ratioVertical);

                Bitmap sprite = Bitmap.createBitmap(spriteSheet,
                        xOffsetConverted, yOffsetConverted, widthSpriteConverted, heightSpriteConverted);
                sprites[i][j] = Bitmap.createScaledBitmap(sprite, widthSpriteDst, heightSpriteDst, true);

                yOffset += (HEIGHT_SPRITE + HEIGHT_DIVIDER);
            }
            yOffset = Y_OFFSET_INIT;
            xOffset += (WIDTH_SPRITE + WIDTH_DIVIDER);
        }

        spriteCoin = BitmapFactory.decodeResource(getResources(), R.drawable.ic_coins_l);
        tileSolid = Bitmap.createBitmap(spriteSheet,
                (int) (10 * ratioHorizontal), (int) (1147 * ratioVertical),
                (int) (widthSpriteConverted - ratioHorizontal), (int) (heightSpriteConverted - ratioVertical));
        tileWalkable = Bitmap.createBitmap(spriteSheet,
                (int) (10 * ratioHorizontal), (int) (1164 * ratioVertical),
                (int) (widthSpriteConverted - ratioHorizontal), (int) (heightSpriteConverted - ratioVertical));
        tileBoulder = Bitmap.createBitmap(spriteSheet,
                (int) (112 * ratioHorizontal), (int) (1088 * ratioVertical),
                (int) (widthSpriteConverted - ratioHorizontal), (int) (heightSpriteConverted - ratioVertical));
    }

    private void stopEntityAnimations() {
        for (Entity e : imageViewViaEntity.keySet()) {
            e.stopAnimations();
        }
    }

    private void startEntityAnimations() {
        for (Entity e : imageViewViaEntity.keySet()) {
            e.startAnimations();
        }
    }

    public static int durationOfFrameInMilli = 420;

    private void initPlayer(int xIndexSpawn, int yIndexSpawn) {
        Resources resources = getResources();

        AnimationDrawable animationDrawableUp = new AnimationDrawable();
        animationDrawableUp.setOneShot(false);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[3][1]), durationOfFrameInMilli);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[4][1]), durationOfFrameInMilli);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[5][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableDown = new AnimationDrawable();
        animationDrawableDown.setOneShot(false);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[0][1]), durationOfFrameInMilli);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[1][1]), durationOfFrameInMilli);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[2][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableLeft = new AnimationDrawable();
        animationDrawableLeft.setOneShot(false);
        animationDrawableLeft.addFrame(
                new BitmapDrawable(resources, sprites[6][1]), durationOfFrameInMilli);
        animationDrawableLeft.addFrame(
                new BitmapDrawable(resources, sprites[7][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableRight = new AnimationDrawable();
        animationDrawableRight.setOneShot(false);
        animationDrawableRight.addFrame(
                new BitmapDrawable(resources, sprites[8][1]), durationOfFrameInMilli);
        animationDrawableRight.addFrame(
                new BitmapDrawable(resources, sprites[9][1]), durationOfFrameInMilli);

        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawableUp);
        animationsByDirection.put(DOWN, animationDrawableDown);
        animationsByDirection.put(LEFT, animationDrawableLeft);
        animationsByDirection.put(RIGHT, animationDrawableRight);
        Player player = new Player(animationsByDirection,
                new Entity.CollisionListener() {
                    @Override
                    public void onJustCollided(Entity collided) {
                        if (collided instanceof NonPlayableCharacter) {
                            if (((NonPlayableCharacter) collided).getId().equals("coin")) {
                                soundManager.sfxPlay(soundManager.sfxGetItem);
                            } else if (((NonPlayableCharacter) collided).getId().equals("rival")) {
//                                soundManager.sfxPlay(soundManager.sfxHorn);
                            } else if (((NonPlayableCharacter) collided).getId().equals("rival leader")) {
                                TypeWriterDialogFragment typeWriterDialogFragment = (TypeWriterDialogFragment) getSupportFragmentManager().findFragmentByTag(TypeWriterDialogFragment.TAG);
                                if (typeWriterDialogFragment != null) {
                                    Log.e(TAG, "typeWriterDialogFragment != null");

                                    if (!typeWriterDialogFragment.isVisible()) {
                                        Log.e(TAG, "!typeWriterDialogFragment.isVisible()... show TypeWriterDialogFragment.");
                                        typeWriterDialogFragment.show(getSupportFragmentManager(), TypeWriterDialogFragment.TAG);
                                    } else {
                                        Log.e(TAG, "typeWriterDialogFragment.isVisible()... do nothing.");
                                    }
                                } else {
                                    Log.e(TAG, "typeWriterDialogFragment == null");
                                    Log.e(TAG, "First time instantiating TypeWriterDialogFragment... show TypeWriterDialogFragment.");

                                    String message = "Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";

                                    TypeWriterDialogFragment typeWriterDialogFragmentRivalLeader =
                                            TypeWriterDialogFragment.newInstance(50L, message, new TypeWriterDialogFragment.DismissListener() {
                                                        @Override
                                                        public void onDismiss() {
                                                            Log.e(TAG, "onDismiss()");

                                                            startEntityAnimations();
                                                        }
                                                    },
                                                    new TypeWriterTextView.TextCompletionListener() {
                                                        @Override
                                                        public void onAnimationFinish() {
                                                            Log.e(TAG, "onAnimationFinish()");

                                                            // TODO:
                                                            ChoiceDialogFragment choiceDialogFragmentYesOrNo =
                                                                    ChoiceDialogFragment.newInstance(
                                                                            new ChoiceDialogFragment.ChoiceListener() {
                                                                                @Override
                                                                                public void onChoiceYesSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                                                                    Log.e(TAG, "YES selected");

                                                                                    soundManager.sfxPlay(soundManager.sfxGetItem);

                                                                                    choiceDialogFragment.dismiss();
                                                                                    TypeWriterDialogFragment typeWriterDialogFragmentRivalLeaderFromFM =
                                                                                            (TypeWriterDialogFragment) getSupportFragmentManager().findFragmentByTag(TypeWriterDialogFragment.TAG);
                                                                                    typeWriterDialogFragmentRivalLeaderFromFM.dismiss();

                                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                                        // Apply activity trainsition
                                                                                        startActivity(
                                                                                                new Intent(GameActivity.this, SequenceTrainerActivity.class),
                                                                                                ActivityOptions.makeSceneTransitionAnimation(GameActivity.this).toBundle()
                                                                                        );
                                                                                    } else {
                                                                                        // Swap without transition
                                                                                        startActivity(
                                                                                                new Intent(GameActivity.this, SequenceTrainerActivity.class)
                                                                                        );
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onChoiceNoSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                                                                    Log.e(TAG, "NO selected");

                                                                                    soundManager.sfxPlay(soundManager.sfxCollision);
                                                                                }
                                                                            });
                                                            choiceDialogFragmentYesOrNo.show(getSupportFragmentManager(), ChoiceDialogFragment.TAG);
                                                        }
                                                    });
                                    stopEntityAnimations();
                                    typeWriterDialogFragmentRivalLeader.show(getSupportFragmentManager(), TypeWriterDialogFragment.TAG);
                                }
                            }
                        }
                    }
                },
                new Entity.MovementListener() {
                    @Override
                    public boolean onMove(int[] futureCorner1, int[] futureCorner2) {
                        int xFutureCorner1 = futureCorner1[0];
                        int yFutureCorner1 = futureCorner1[1];
                        int xFutureCorner2 = futureCorner2[0];
                        int yFutureCorner2 = futureCorner2[1];

                        int xIndex1 = xFutureCorner1 / Tile.widthTile;
                        int yIndex1 = yFutureCorner1 / Tile.heightTile;
                        int xIndex2 = xFutureCorner2 / Tile.widthTile;
                        int yIndex2 = yFutureCorner2 / Tile.heightTile;

                        Tile tileCorner1 = ((World) frameLayout).getTile(xIndex1, yIndex1);
                        Tile tileCorner2 = ((World) frameLayout).getTile(xIndex2, yIndex2);

                        boolean isSolidCorner1 = tileCorner1.isSolid();
                        boolean isSolidCorner2 = tileCorner2.isSolid();

                        return (!isSolidCorner1 && !isSolidCorner2);
                    }
                });
        controllable = (Controllable) player;

        ImageView ivPlayer = new ImageView(this);
        ivPlayer.setImageDrawable(player.getAnimationDrawableBasedOnDirection());
        frameLayout.addView(ivPlayer, new FrameLayout.LayoutParams(widthSpriteDst, heightSpriteDst));
        imageViewViaEntity.put(player, ivPlayer);

        player.setxPos(xIndexSpawn * widthSpriteDst);
        player.setyPos(yIndexSpawn * heightSpriteDst);
    }

    private void initNonPlayableCharacter(String id, int yIndexForSprites,
                                          int xIndexSpawn, int yIndexSpawn,
                                          boolean isStationary, Direction directionFacing,
                                          Entity.CollisionListener entityCollisionListener,
                                          Entity.MovementListener entityMovementListener,
                                          View.OnClickListener viewOnClickListener) {
        Resources resources = getResources();

        AnimationDrawable animationDrawableUp = new AnimationDrawable();
        animationDrawableUp.setOneShot(false);
        AnimationDrawable animationDrawableDown = new AnimationDrawable();
        animationDrawableDown.setOneShot(false);
        AnimationDrawable animationDrawableLeft = new AnimationDrawable();
        animationDrawableLeft.setOneShot(false);
        AnimationDrawable animationDrawableRight = new AnimationDrawable();
        animationDrawableRight.setOneShot(false);
        if (yIndexForSprites >= 0) {
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[3][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[4][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[5][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[0][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[1][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[2][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, sprites[6][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, sprites[7][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, sprites[8][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, sprites[9][yIndexForSprites]), durationOfFrameInMilli);
        } else {
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
        }

        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawableUp);
        animationsByDirection.put(DOWN, animationDrawableDown);
        animationsByDirection.put(LEFT, animationDrawableLeft);
        animationsByDirection.put(RIGHT, animationDrawableRight);
        NonPlayableCharacter nonPlayableCharacter = new NonPlayableCharacter(id,
                animationsByDirection,
                directionFacing,
                entityCollisionListener,
                entityMovementListener);

        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(nonPlayableCharacter.getAnimationDrawableBasedOnDirection());
        frameLayout.addView(imageView, new FrameLayout.LayoutParams(widthSpriteDst, heightSpriteDst));
        imageViewViaEntity.put(nonPlayableCharacter, imageView);

        nonPlayableCharacter.setxPos(xIndexSpawn * widthSpriteDst);
        nonPlayableCharacter.setyPos(yIndexSpawn * heightSpriteDst);
        if (isStationary) {
            nonPlayableCharacter.turnStationaryOn();
        }
        imageView.setOnClickListener(viewOnClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        soundManager = new SoundManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//            getWindow().setAllowEnterTransitionOverlap(true);
            // Set an exit transition
            getWindow().setExitTransition(new Slide(Gravity.TOP));
            // Set an enter transition
            getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
        }
        ///////////////////////////////////////
        // TODO: convert activity_game.xml's app_bar_main.xml to be fragment-based
        //  (change class World [FrameLayout] into a fragment, replace app_bar_main.xml's World
        //  with FragmentContainerView, add WorldFragment to FragmentContainerView).
        setContentView(R.layout.activity_game);
        ///////////////////////////////////////

        appBarLayout = findViewById(R.id.app_bar_layout);
        frameLayout = findViewById(R.id.frameLayout);

        if (savedInstanceState == null) {
            drawerStartFragment = DrawerStartFragment.newInstance(null, null);
            drawerEndFragment = DrawerEndFragment.newInstance(null, null);
            drawerTopFragment = DrawerTopFragment.newInstance(null, null);
            getSupportFragmentManager().beginTransaction()
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
            drawerStartFragment = (DrawerStartFragment) getSupportFragmentManager().findFragmentByTag(DrawerStartFragment.TAG);
            drawerEndFragment = (DrawerEndFragment) getSupportFragmentManager().findFragmentByTag(DrawerEndFragment.TAG);
            drawerTopFragment = (DrawerTopFragment) getSupportFragmentManager().findFragmentByTag(DrawerTopFragment.TAG);
        }

        imageViewViaEntity = new HashMap<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Point sizeDisplay = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(sizeDisplay);
        xScreenSize = sizeDisplay.x;
        yScreenSize = sizeDisplay.y;
        Log.e(TAG, "xScreenSize, yScreenSize: " + xScreenSize + ", " + yScreenSize);
        widthSpriteDst = Math.min(xScreenSize, yScreenSize) / 12;
        heightSpriteDst = widthSpriteDst;
        Log.e(TAG, "widthSpriteDst, heightSpriteDst: " + widthSpriteDst + ", " + heightSpriteDst);

        initSprites(widthSpriteDst, heightSpriteDst);

        Entity.CollisionListener collisionListener = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals("coin")) {
                        soundManager.sfxPlay(soundManager.sfxGetItem);
                    }
                } else if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.CollisionListener collisionListenerCoin = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.MovementListener movementListener = new Entity.MovementListener() {
            @Override
            public boolean onMove(int[] futureCorner1, int[] futureCorner2) {
                int xFutureCorner1 = futureCorner1[0];
                int yFutureCorner1 = futureCorner1[1];
                int xFutureCorner2 = futureCorner2[0];
                int yFutureCorner2 = futureCorner2[1];

                int xIndex1 = xFutureCorner1 / Tile.widthTile;
                int yIndex1 = yFutureCorner1 / Tile.heightTile;
                int xIndex2 = xFutureCorner2 / Tile.widthTile;
                int yIndex2 = yFutureCorner2 / Tile.heightTile;

                Tile tileCorner1 = ((World) frameLayout).getTile(xIndex1, yIndex1);
                Tile tileCorner2 = ((World) frameLayout).getTile(xIndex2, yIndex2);

                boolean isSolidCorner1 = tileCorner1.isSolid();
                boolean isSolidCorner2 = tileCorner2.isSolid();

                return (!isSolidCorner1 && !isSolidCorner2);
            }
        };
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Entity e : imageViewViaEntity.keySet()) {
                    if (imageViewViaEntity.get(e) == view) {
                        ((NonPlayableCharacter) e).toggleStationary();
                    }
                }
            }
        };

        ////////////////////////////////////////////////////////////
        initNonPlayableCharacter("rival", 3,
                200, 12,
                false, DOWN,
                collisionListener,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("coin", -1,
                201, 14,
                true, DOWN,
                collisionListenerCoin,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("rival leader", 28,
                201, 15,
                true, LEFT,
                collisionListener,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("jr trainer", 11,
                201, 19,
                true, LEFT,
                collisionListener,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("lass02", 17,
                200, 22,
                true, RIGHT,
                collisionListener,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("youngster", 10,
                201, 25,
                true, LEFT,
                collisionListener,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("lass02", 17,
                200, 28,
                true, RIGHT,
                collisionListener,
                movementListener,
                onClickListener);
        initNonPlayableCharacter("bug catcher", 10,
                201, 31,
                true, LEFT,
                collisionListener,
                movementListener,
                onClickListener);
        initPlayer(200, 34);
        ////////////////////////////////////////////////////////////

        Tile.init(widthSpriteDst, heightSpriteDst);

        ((World) frameLayout).init(this, R.raw.world01, new OnSwipeListener() {
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
                        yInit > (yScreenSize - 100)) {
                    BottomDrawerDialogFragment.newInstance(GameActivity.this)
                            .show(getSupportFragmentManager(), BottomDrawerDialogFragment.TAG);
                    return true;
                }
                return false;
            }
        });

        gameCamera = new GameCamera(0f, 0f,
                xScreenSize, yScreenSize,
                ((World) frameLayout).getWorldWidthInPixels(), ((World) frameLayout).getWorldHeightInPixels());

        float xMax = (float) ((World) frameLayout).getWorldWidthInPixels();
        float yMax = (float) ((World) frameLayout).getWorldHeightInPixels();
        Entity.init(
                new ArrayList<Entity>(imageViewViaEntity.keySet()),
                widthSpriteDst, heightSpriteDst,
                0, xMax, 0, yMax
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        soundManager.onStart();
    }

    // "Perform method calls [BEFORE] the call to the superclass.
    // This is because we'd like to unregister our listener
    // [before] the system does it's tasks to free up resources"
    // -https://gamedevacademy.org/android-sensors-game-tutorial/
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        soundManager.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        soundManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        TypeWriterDialogFragment typeWriterDialogFragment = (TypeWriterDialogFragment) getSupportFragmentManager().findFragmentByTag(TypeWriterDialogFragment.TAG);
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

            controllable.updateViaSensorEvent(xDelta, yDelta);
            updateGameEntities();
            gameCamera.centerOnEntity((Player) controllable);
//            gameCamera.move(1, 1);
            frameLayout.invalidate();

            // Prepare for next sensor event
            xAccelPrevious = xAccel;
            yAccelPrevious = yAccel;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Intentionally blank.
    }

    private void updateGameEntities() {
        for (Entity e : imageViewViaEntity.keySet()) {
            if (!(e instanceof Player)) {
                e.update();
            }

            ImageView ivEntity = imageViewViaEntity.get(e);
            // IMAGE (based on speed bonus)
            if (e.getSpeedBonus() > Entity.DEFAULT_SPEED_BONUS) {
                ivEntity.setAlpha(0.5f);
            }

            // IMAGE (based on direction)
            if (e instanceof NonPlayableCharacter) {
                if (((NonPlayableCharacter) e).isStationary()) {
                    ivEntity.setImageDrawable(e.getPausedFrameBasedOnDirection());
                } else {
                    ivEntity.setImageDrawable(e.getAnimationDrawableBasedOnDirection());
                }
            } else if (e instanceof Player) {
                ivEntity.setImageDrawable(e.getAnimationDrawableBasedOnDirection());
            }

            // POSITION
            ivEntity.setX(e.getxPos() - gameCamera.getxOffset());
            ivEntity.setY(e.getyPos() - gameCamera.getyOffset());
        }
    }

    public int getxScreenSize() {
        return xScreenSize;
    }

    public int getyScreenSize() {
        return yScreenSize;
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    @Override
    public void onClickTypeWriterTextView(View view, String tag) {
        if (tag.equals(DrawerStartFragment.TAG)) {
            String message = "DrawerStartFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
            ((TypeWriterTextView) view).displayTextWithAnimation(message);
        } else if (tag.equals(DrawerEndFragment.TAG)) {
            String message = "DrawerEndFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
            ((TypeWriterTextView) view).displayTextWithAnimation(message);
        } else if (tag.equals(DrawerTopFragment.TAG)) {
            String message = "DrawerTopFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
            ((TypeWriterTextView) view).displayTextWithAnimation(message);
        } else if (tag.equals(BottomDrawerDialogFragment.TAG)) {
            String message = "BottomDrawerDialogFragment: Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";
            ((TypeWriterTextView) view).displayTextWithAnimation(message);
        }
    }
}