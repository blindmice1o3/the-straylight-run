package com.jackingaming.thestraylightrun.accelerometer.gamecontroller;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jackingaming.thestraylightrun.R;

import java.util.Random;

public class GameControllerActivity extends AppCompatActivity
        implements SensorEventListener {
    public static final String TAG = GameControllerActivity.class.getSimpleName();

    private SoundPool soundPool;
    private int indexSfx = 0;
    private int sfxBallPoof, sfxBallToss, sfxCollision,
            sfxEnterPc, sfxGetItem, sfxHorn;
    private boolean loadedSfxBallPoof, loadedSfxBallToss, loadedSfxCollision,
            loadedSfxEnterPc, loadedSfxGetItem, loadedSfxHorn;

    private enum Direction {UP, DOWN, LEFT, RIGHT;}

    private float xAccelPrevious, yAccelPrevious = 0f;
    private float xPosPlayer, xAccel, xVel = 0.0f;
    private float yPosPlayer, yAccel, yVel = 0.0f;
    private float xPosRival, yPosRival = 400f;
    private int speedRival = 4;
    private Random random = new Random();
    private float xMax, yMax;
    private SensorManager sensorManager;

    //    private Viewport viewport;
    private FrameLayout frameLayout;
    private ImageView ivPlayer, ivRival;
    private Direction directionPlayer, directionRival = Direction.RIGHT;
    private Bitmap[][] sprites;

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
    private static final int WIDTH_SPRITE_DST = 100;
    private static final int HEIGHT_SPRITE_DST = 100;

    private Bitmap[][] initSprites() {
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
                sprites[i][j] = Bitmap.createScaledBitmap(sprite, WIDTH_SPRITE_DST, HEIGHT_SPRITE_DST, true);

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

        soundPool = new SoundPool(
                6,
                AudioManager.STREAM_MUSIC,
                0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                Log.d(TAG, "SoundPool.OnLoadCompleteListener sampleId: " + i);

                int sampleId = i;
                if (sampleId == sfxBallPoof) {
                    loadedSfxBallPoof = true;
                } else if (sampleId == sfxBallToss) {
                    loadedSfxBallToss = true;
                } else if (sampleId == sfxCollision) {
                    loadedSfxCollision = true;
                } else if (sampleId == sfxEnterPc) {
                    loadedSfxEnterPc = true;
                } else if (sampleId == sfxGetItem) {
                    loadedSfxGetItem = true;
                } else if (sampleId == sfxHorn) {
                    loadedSfxHorn = true;
                } else {
                    Log.e(TAG, "sampleId: " + sampleId + " (not a pre-defined sound sample).");
                }
            }
        });

        // load()'s parameters: context, file_name, priority
        sfxBallPoof = soundPool.load(this, R.raw.sfx_ball_poof, 1);
        Log.d(TAG, "sfxBallPoof: " + sfxBallPoof);

        sfxBallToss = soundPool.load(this, R.raw.sfx_ball_toss, 1);
        Log.d(TAG, "sfxBallToss: " + sfxBallToss);

        sfxCollision = soundPool.load(this, R.raw.sfx_collision, 1);
        Log.d(TAG, "sfxCollision: " + sfxCollision);

        sfxEnterPc = soundPool.load(this, R.raw.sfx_enter_pc, 1);
        Log.d(TAG, "sfxEnterPc: " + sfxEnterPc);

        sfxGetItem = soundPool.load(this, R.raw.sfx_get_item_1, 1);
        Log.d(TAG, "sfxGetItem: " + sfxGetItem);

        sfxHorn = soundPool.load(this, R.raw.horn, 1);
        Log.d(TAG, "sfxHorn: " + sfxHorn);

        sprites = initSprites();
//        viewport = new Viewport(this);
//        setContentView(viewport);
        ivPlayer = new ImageView(this);
        ivRival = new ImageView(this);
        ivPlayer.setImageBitmap(sprites[1][1]);
        ivRival.setImageBitmap(sprites[1][3]);
        frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(WIDTH_SPRITE_DST, HEIGHT_SPRITE_DST);
        frameLayout.addView(ivPlayer, layoutParams);
        frameLayout.addView(ivRival, layoutParams);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loadedSfxBallPoof && loadedSfxBallToss && loadedSfxCollision
                        && loadedSfxEnterPc && loadedSfxGetItem && loadedSfxHorn) {
                    // change selection of sound sample
                    indexSfx++;
                    if (indexSfx >= 6) {
                        indexSfx = 0;
                    }

                    // play()'s parameters: leftVolume, rightVolume,
                    // priority, loop, and rate
                    soundPool.play(indexSfx, 1, 1,
                            0, 0, 1);
                }
            }
        });

        ////////////////////////////
        setContentView(frameLayout);
        ////////////////////////////

        Point sizeDisplay = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(sizeDisplay);
        xMax = (float) sizeDisplay.x - WIDTH_SPRITE_DST;
        yMax = (float) sizeDisplay.y - HEIGHT_SPRITE_DST;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    // "Perform method calls [BEFORE] the call to the superclass.
    // This is because we'd like to unregister our listener
    // [before] the system does it's tasks to free up resources"
    // -https://gamedevacademy.org/android-sensors-game-tutorial/
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            updateGameEntities();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Intentionally blank.
    }

    private boolean colliding;
    private boolean justCollided;
    private boolean cantCollide;

    private void updateGameEntities() {
        // COLLISION DETECTION
        colliding = isOverlapping();

        if (cantCollide && !colliding) {
            cantCollide = false;
        } else if (justCollided) {
            cantCollide = true;
            justCollided = false;
        }
        if (!cantCollide && colliding) {
            justCollided = true;
        }

        if (justCollided) {
            if (loadedSfxGetItem) {
                int streamId = soundPool.play(sfxGetItem, 1, 1,
                        0, 0, 1);
                Log.e(TAG, "streamId: " + streamId);
            }
            return;
        } else if (colliding) {
            frameLayout.setBackgroundColor(Color.CYAN);
        } else {
            frameLayout.setBackgroundColor(Color.WHITE);
        }

        // PLAYER
        float xAccelDelta = xAccel - xAccelPrevious;
        float yAccelDelta = yAccel - yAccelPrevious;

        Log.e(TAG, String.format("(xAccel, yAccel): (%f, %f)", (xAccel / Math.abs(xAccel)), (yAccel / Math.abs(yAccel))));

        float frameTime = 0.666f;
        xVel += (xAccelDelta * frameTime);
        yVel += (yAccelDelta * frameTime);

//        Log.e(TAG, String.format("(xVel, yVel): (%f, %f)", (xVel / Math.abs(xVel)), (yVel / Math.abs(yVel))));

        float xDelta = (xVel / 2) * frameTime;
        float yDelta = (yVel / 2) * frameTime;

        Log.e(TAG, String.format("(xDelta, yDelta): (%f, %f)", xDelta, yDelta));

        // Update direction
        if (Math.abs(yDelta) >= Math.abs(xDelta)) {
            if (yDelta < 0) {
                directionPlayer = Direction.DOWN;
            } else {
                directionPlayer = Direction.UP;
            }
        } else {
            if (xDelta < 0) {
                directionPlayer = Direction.RIGHT;
            } else {
                directionPlayer = Direction.LEFT;
            }
        }

        // Update image (based on direction)
        Bitmap imagePlayer = null;
        switch (directionPlayer) {
            case UP:
                imagePlayer = sprites[4][1];
                break;
            case DOWN:
                imagePlayer = sprites[1][1];
                break;
            case LEFT:
                imagePlayer = sprites[6][1];
                break;
            case RIGHT:
                imagePlayer = sprites[8][1];
                break;
        }
        ivPlayer.setImageBitmap(imagePlayer);
//        viewport.setImagePlayer(imagePlayer);

        // Update position
        xPosPlayer -= xDelta;
        yPosPlayer -= yDelta;

        if (xPosPlayer > xMax) {
            xPosPlayer = xMax;
        } else if (xPosPlayer < 0) {
            xPosPlayer = 0;
        }

        if (yPosPlayer > yMax) {
            yPosPlayer = yMax;
        } else if (yPosPlayer < 0) {
            yPosPlayer = 0;
        }

        ivPlayer.setX(xPosPlayer);
        ivPlayer.setY(yPosPlayer);

        // Prepare for next sensor event
        xAccelPrevious = xAccel;
        yAccelPrevious = yAccel;

        // RIVAL
        // Update direction (changes 10% of the time)
        if (random.nextInt(10) < 1) {
            // determine direction
            switch (random.nextInt(4)) {
                case 0:
                    directionRival = Direction.UP;
                    break;
                case 1:
                    directionRival = Direction.DOWN;
                    break;
                case 2:
                    directionRival = Direction.LEFT;
                    break;
                case 3:
                    directionRival = Direction.RIGHT;
                    break;
            }
        }
        // don't change direction (90% of the time)
        else {
            // do nothing
        }
        Log.e(TAG, "RIVAL direction: " + directionRival);

        // Update image (based on direction)
        Bitmap imageRival = null;
        switch (directionRival) {
            case UP:
                imageRival = sprites[4][3];
                break;
            case DOWN:
                imageRival = sprites[1][3];
                break;
            case LEFT:
                imageRival = sprites[6][3];
                break;
            case RIGHT:
                imageRival = sprites[8][3];
                break;
        }
        ivRival.setImageBitmap(imageRival);
//        viewport.setImageRival(imageRival);

        // Update position
        switch (directionRival) {
            case UP:
                yPosRival -= speedRival;
                Log.e(TAG, "subtract y");
                break;
            case DOWN:
                yPosRival += speedRival;
                Log.e(TAG, "add y");
                break;
            case LEFT:
                xPosRival -= speedRival;
                Log.e(TAG, "subtract x");
                break;
            case RIGHT:
                xPosRival += speedRival;
                Log.e(TAG, "add x");
                break;
        }

        if (xPosRival > xMax) {
            xPosRival = xMax;
        } else if (xPosRival < 0) {
            xPosRival = 0;
        }

        if (yPosRival > yMax) {
            yPosRival = yMax;
        } else if (yPosRival < 0) {
            yPosRival = 0;
        }

        ivRival.setX(xPosRival);
        ivRival.setY(yPosRival);
    }

    private boolean isOverlapping() {
        return xPosPlayer < xPosRival + WIDTH_SPRITE_DST &&
                xPosPlayer + WIDTH_SPRITE_DST > xPosRival &&
                yPosPlayer < yPosRival + HEIGHT_SPRITE_DST &&
                yPosPlayer + HEIGHT_SPRITE_DST > yPosRival;
    }

    private class Viewport extends View {
        private Bitmap imagePlayer;
        private Bitmap imageRival;

        public Viewport(Context context) {
            super(context);
            imagePlayer = sprites[1][1];
            imageRival = sprites[1][3];
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(imagePlayer, xPosPlayer, yPosPlayer, null);
            canvas.drawBitmap(imageRival, xPosRival, yPosRival, null);
            invalidate();
        }

        public void setImagePlayer(Bitmap imagePlayer) {
            this.imagePlayer = imagePlayer;
        }

        public void setImageRival(Bitmap imageRival) {
            this.imageRival = imageRival;
        }
    }
}