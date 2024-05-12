package com.jackingaming.thestraylightrun.accelerometer.game;

import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.DOWN;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.LEFT;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.RIGHT;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.UP;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogfragments.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogfragments.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.controllables.Controllable;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity
        implements SensorEventListener {
    public static final String TAG = GameActivity.class.getSimpleName();

    private TypeWriterTextView drawerStart;
    private TypeWriterTextView drawerEnd;

    private SoundManager soundManager;
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

    private void initPlayer(int xIndexSpawn, int yIndexSpawn) {
        Bitmap[] upPlayer = new Bitmap[3];
        upPlayer[0] = sprites[3][1];
        upPlayer[1] = sprites[4][1];
        upPlayer[2] = sprites[5][1];

        Bitmap[] downPlayer = new Bitmap[3];
        downPlayer[0] = sprites[0][1];
        downPlayer[1] = sprites[1][1];
        downPlayer[2] = sprites[2][1];

        Bitmap[] leftPlayer = new Bitmap[2];
        leftPlayer[0] = sprites[6][1];
        leftPlayer[1] = sprites[7][1];

        Bitmap[] rightPlayer = new Bitmap[2];
        rightPlayer[0] = sprites[8][1];
        rightPlayer[1] = sprites[9][1];

        Map<Direction, Animation> spritesPlayer = new HashMap<>();
        spritesPlayer.put(UP, new Animation(upPlayer));
        spritesPlayer.put(DOWN, new Animation(downPlayer));
        spritesPlayer.put(LEFT, new Animation(leftPlayer));
        spritesPlayer.put(RIGHT, new Animation(rightPlayer));
        Player player = new Player(spritesPlayer,
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

                                    TypeWriterDialogFragment dialogFragment =
                                            TypeWriterDialogFragment.newInstance(50L, message);
                                    dialogFragment.show(getSupportFragmentManager(), TypeWriterDialogFragment.TAG);
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
        ivPlayer.setImageBitmap(player.getFrame());
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
        Bitmap[] upSprites = new Bitmap[3];
        Bitmap[] downSprites = new Bitmap[3];
        Bitmap[] leftSprites = new Bitmap[2];
        Bitmap[] rightSprites = new Bitmap[2];
        if (yIndexForSprites >= 0) {
            upSprites[0] = sprites[3][yIndexForSprites];
            upSprites[1] = sprites[4][yIndexForSprites];
            upSprites[2] = sprites[5][yIndexForSprites];

            downSprites[0] = sprites[0][yIndexForSprites];
            downSprites[1] = sprites[1][yIndexForSprites];
            downSprites[2] = sprites[2][yIndexForSprites];

            leftSprites[0] = sprites[6][yIndexForSprites];
            leftSprites[1] = sprites[7][yIndexForSprites];

            rightSprites[0] = sprites[8][yIndexForSprites];
            rightSprites[1] = sprites[9][yIndexForSprites];
        } else {
            upSprites[0] = spriteCoin;
            upSprites[1] = spriteCoin;
            upSprites[2] = spriteCoin;

            downSprites[0] = spriteCoin;
            downSprites[1] = spriteCoin;
            downSprites[2] = spriteCoin;

            leftSprites[0] = spriteCoin;
            leftSprites[1] = spriteCoin;

            rightSprites[0] = spriteCoin;
            rightSprites[1] = spriteCoin;
        }

        Map<Direction, Animation> animationMap = new HashMap<>();
        animationMap.put(UP, new Animation(upSprites));
        animationMap.put(DOWN, new Animation(downSprites));
        animationMap.put(LEFT, new Animation(leftSprites));
        animationMap.put(RIGHT, new Animation(rightSprites));
        NonPlayableCharacter nonPlayableCharacter = new NonPlayableCharacter(id,
                animationMap,
                directionFacing,
                entityCollisionListener,
                entityMovementListener);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(nonPlayableCharacter.getFrame());
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

        ///////////////////////////////////////////////////////////////////////////////
        setContentView(R.layout.activity_game);

        frameLayout = findViewById(R.id.frameLayout);
        drawerStart = findViewById(R.id.drawer_start);
        drawerEnd = findViewById(R.id.drawer_end);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.sfxIterateAndPlay();
            }
        });
        frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                soundManager.backgroundMusicTogglePause();
                return true;
            }
        });
        drawerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                drawerStart.displayTextWithAnimation(message);
            }
        });
        drawerEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                drawerEnd.displayTextWithAnimation(message);
            }
        });

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

        ((World) frameLayout).init(this, R.raw.world01);

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
            ivEntity.setImageBitmap(e.getFrame());

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
}