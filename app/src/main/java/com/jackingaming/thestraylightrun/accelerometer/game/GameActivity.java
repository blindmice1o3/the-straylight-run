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
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Animation;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Coin;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Controllable;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Rival;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity
        implements SensorEventListener {
    public static final String TAG = GameActivity.class.getSimpleName();

    private SoundManager soundManager;

    private FrameLayout frameLayout;
    private Map<Entity, ImageView> imageViewViaEntity;
    private Bitmap[][] sprites;
    private Bitmap spriteCoin;

    private Controllable controllable;
    private SensorManager sensorManager;
    private float xAccelPrevious, yAccelPrevious = 0f;
    private float xVel, yVel = 0.0f;

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

    private Bitmap[][] initSprites(int widthSpriteDst, int heightSpriteDst) {
        Bitmap[][] sprites = new Bitmap[COLUMNS][ROWS];

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

        return sprites;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        soundManager = new SoundManager(this);

        ///////////////////////////////////////////////////////////////////////////////

        frameLayout = new FrameLayout(this);

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

        Point sizeDisplay = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(sizeDisplay);
        int xScreenSize = sizeDisplay.x;
        int yScreenSize = sizeDisplay.y;
        Log.e(TAG, "xScreenSize, yScreenSize: " + xScreenSize + ", " + yScreenSize);
        int widthSpriteDst = Math.min(xScreenSize, yScreenSize) / 12;
        int heightSpriteDst = widthSpriteDst;
        Log.e(TAG, "widthSpriteDst, heightSpriteDst: " + widthSpriteDst + ", " + heightSpriteDst);

        sprites = initSprites(widthSpriteDst, heightSpriteDst);
        spriteCoin = BitmapFactory.decodeResource(getResources(), R.drawable.ic_coins_l);

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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Player player = new Player(spritesPlayer, new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof Coin) {
                    soundManager.sfxPlay(soundManager.sfxGetItem);
                } else if (collided instanceof Rival) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        });
        controllable = (Controllable) player;

        Bitmap[] upRival = new Bitmap[3];
        upRival[0] = sprites[3][3];
        upRival[1] = sprites[4][3];
        upRival[2] = sprites[5][3];

        Bitmap[] downRival = new Bitmap[3];
        downRival[0] = sprites[0][3];
        downRival[1] = sprites[1][3];
        downRival[2] = sprites[2][3];

        Bitmap[] leftRival = new Bitmap[2];
        leftRival[0] = sprites[6][3];
        leftRival[1] = sprites[7][3];

        Bitmap[] rightRival = new Bitmap[2];
        rightRival[0] = sprites[8][3];
        rightRival[1] = sprites[9][3];

        Map<Direction, Animation> spritesRival = new HashMap<>();
        spritesRival.put(UP, new Animation(upRival));
        spritesRival.put(DOWN, new Animation(downRival));
        spritesRival.put(LEFT, new Animation(leftRival));
        spritesRival.put(RIGHT, new Animation(rightRival));
        Rival rival = new Rival(spritesRival, new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof Coin) {
                    soundManager.sfxPlay(soundManager.sfxGetItem);
                } else if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        });

        Map<Direction, Animation> spritesCoin = new HashMap<>();
        Bitmap[] downCoin = new Bitmap[2];
        downCoin[0] = spriteCoin;
        downCoin[1] = spriteCoin;
        spritesCoin.put(DOWN, new Animation(downCoin));
        Coin coin = new Coin(spritesCoin, new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                // intentionally blank.
            }
        });

        ImageView ivPlayer = new ImageView(this);
        ImageView ivRival = new ImageView(this);
        ImageView ivCoin = new ImageView(this);
        ivPlayer.setImageBitmap(player.getFrame());
        ivRival.setImageBitmap(rival.getFrame());
        ivCoin.setImageBitmap(coin.getFrame());
        ivCoin.setX(coin.getxPos());
        ivCoin.setY(coin.getyPos());
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(widthSpriteDst, heightSpriteDst);
        frameLayout.addView(ivPlayer, layoutParams);
        frameLayout.addView(ivRival, layoutParams);
        frameLayout.addView(ivCoin, layoutParams);

        ////////////////////////////
        setContentView(frameLayout);
        ////////////////////////////

        imageViewViaEntity = new HashMap<>();
        imageViewViaEntity.put(player, ivPlayer);
        imageViewViaEntity.put(rival, ivRival);
        imageViewViaEntity.put(coin, ivCoin);

        float xMax = (float) xScreenSize - widthSpriteDst;
        float yMax = (float) yScreenSize - heightSpriteDst;
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
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAccel = -sensorEvent.values[0];
            float yAccel = sensorEvent.values[1];

            Log.e(TAG, String.format("(xAccel, yAccel): (%f, %f)", (xAccel / Math.abs(xAccel)), (yAccel / Math.abs(yAccel))));

            float xAccelDelta = xAccel - xAccelPrevious;
            float yAccelDelta = yAccel - yAccelPrevious;

            float frameTime = 0.666f;
            xVel += (xAccelDelta * frameTime);
            yVel += (yAccelDelta * frameTime);

            Log.e(TAG, String.format("(xVel, yVel): (%f, %f)", xVel, yVel));
            Log.e(TAG, String.format("((xVel / Math.abs(xVel)), (yVel / Math.abs(yVel))): (%f, %f)", (xVel / Math.abs(xVel)), (yVel / Math.abs(yVel))));

            float xDelta = (xVel / 2) * frameTime;
            float yDelta = (yVel / 2) * frameTime;

            Log.e(TAG, String.format("(xDelta, yDelta): (%f, %f)", xDelta, yDelta));

            controllable.updateViaSensorEvent(xDelta, yDelta);
            updateGameEntities();

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
            ivEntity.setX(e.getxPos());
            ivEntity.setY(e.getyPos());
        }
    }
}