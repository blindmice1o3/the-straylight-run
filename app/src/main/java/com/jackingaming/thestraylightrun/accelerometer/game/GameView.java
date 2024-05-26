package com.jackingaming.thestraylightrun.accelerometer.game;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView
        implements SurfaceHolder.Callback {
    public static final String TAG = GameView.class.getSimpleName();

    public interface SurfaceCreatedListener {
        void onSurfaceCreated(Game game);
    }

    private SurfaceCreatedListener listener;

    private GameRunner runner;
    private Game game;

    public GameView(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceCreated()");

        game = new Game(surfaceHolder, getResources(), new Handler(),
                getWidth(), getHeight());
        listener.onSurfaceCreated(game);

        runner = new GameRunner(game);
        runner.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e(TAG, "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceDestroyed()");

        if (runner != null) {
            // shutdown drawing-thread.
            runner.shutdown();

            // wait for drawing-thread to shutdown.
            while (runner != null) {
                try {
                    runner.join(); // method that waits for thread to terminate.
                    runner = null;
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    // while-loop will loop until the thread is null.
                }
            }
        }
    }

    public void setListener(SurfaceCreatedListener listener) {
        this.listener = listener;
    }

    public Game getGame() {
        return game;
    }
}
