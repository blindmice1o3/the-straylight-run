package com.jackingaming.thestraylightrun.accelerometer.game;

import android.util.Log;

public class GameRunner extends Thread {
    public static final String TAG = GameRunner.class.getSimpleName();

    private Game game;
    private volatile boolean running = true;

    public GameRunner(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        // Game loop.
        long lastTime = System.nanoTime();

        while (running) {
            try {
                long now = System.nanoTime();
                long elapsedMillis = (now - lastTime) / 1_000_000L;

                elapsedMillis = Math.min(elapsedMillis, 100);

                game.update(elapsedMillis);
                game.draw();

                lastTime = now;

                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            } catch (Throwable t) {
                Log.e(TAG, "Unhandled exception", t);
                running = false;
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}
