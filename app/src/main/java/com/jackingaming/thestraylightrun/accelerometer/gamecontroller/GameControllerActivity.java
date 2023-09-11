package com.jackingaming.thestraylightrun.accelerometer.gamecontroller;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import androidx.appcompat.app.AppCompatActivity;

import com.jackingaming.thestraylightrun.R;

public class GameControllerActivity extends AppCompatActivity
        implements SensorEventListener {
    public static final String TAG = GameControllerActivity.class.getSimpleName();

    private enum Direction {UP, DOWN, LEFT, RIGHT;}

    private float xAccelPrevious, yAccelPrevious = 0f;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private SensorManager sensorManager;

    private PlayerView playerView;
    private Direction direction = Direction.RIGHT;
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
        sprites = initSprites();
        playerView = new PlayerView(this);
        setContentView(playerView);

        Point sizeDisplay = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(sizeDisplay);
        xMax = (float) sizeDisplay.x - 100;
        yMax = (float) sizeDisplay.y - 100;

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
            updatePlayer();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Intentionally blank.
    }

    private void updatePlayer() {
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
                direction = Direction.DOWN;
            } else {
                direction = Direction.UP;
            }
        } else {
            if (xDelta < 0) {
                direction = Direction.RIGHT;
            } else {
                direction = Direction.LEFT;
            }
        }

        // Update image (based on direction)
        Bitmap imagePlayer = null;
        switch (direction) {
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
        playerView.setImage(imagePlayer);

        // Update position
        xPos -= xDelta;
        yPos -= yDelta;

        if (xPos > xMax) {
            xPos = xMax;
        } else if (xPos < 0) {
            xPos = 0;
        }

        if (yPos > yMax) {
            yPos = yMax;
        } else if (yPos < 0) {
            yPos = 0;
        }

        // Prepare for next sensor event
        xAccelPrevious = xAccel;
        yAccelPrevious = yAccel;
    }

    private class PlayerView extends View {
        private Bitmap image;

        public PlayerView(Context context) {
            super(context);
            image = sprites[1][1];
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(image, xPos, yPos, null);
            invalidate();
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }
    }
}