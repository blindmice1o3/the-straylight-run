package com.jackingaming.thestraylightrun.accelerometer.game;

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
        long lastTime = System.currentTimeMillis();
        while (running) {
            // Draw stuff.
            long now = System.currentTimeMillis();
            long elapsed = now - lastTime;

            // Update game only if elapsed time is less than 1/10th of second.
            // Otherwise, too much time had passed.
            if (elapsed < 100) {
                game.update(elapsed);
                game.draw();
            }

            lastTime = now;
        }
    }

    public void shutdown() {
        running = false;
    }
}
