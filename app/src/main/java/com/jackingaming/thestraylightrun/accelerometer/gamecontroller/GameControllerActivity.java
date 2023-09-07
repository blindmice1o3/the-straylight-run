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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.jackingaming.thestraylightrun.R;

public class GameControllerActivity extends AppCompatActivity
        implements SensorEventListener {

    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private Bitmap imageBall;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BallView ballView = new BallView(this);
        setContentView(ballView);

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
            updateBall();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Intentionally blank.
    }

    private void updateBall() {
        float frameTime = 0.666f;
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        float xDelta = (xVel / 2) * frameTime;
        float yDelta = (yVel / 2) * frameTime;

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
    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);

            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(),
                    R.drawable.pokemon_mystery_dungeon_red_rescue_team_snorlax);
            final int dstWidth = 100;
            final int dstHeight = 100;
            imageBall = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(imageBall, xPos, yPos, null);
            invalidate();
        }
    }
}