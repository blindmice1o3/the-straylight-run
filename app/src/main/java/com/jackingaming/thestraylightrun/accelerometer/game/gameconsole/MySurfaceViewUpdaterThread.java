package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class MySurfaceViewUpdaterThread extends Thread {
    public static final int FRAMES_PER_SEC = 60;
    public static final int TIME_PER_FRAME = 1000 / FRAMES_PER_SEC;

    private Game game;
    private InputManager inputManager;
    private volatile boolean running = true;

    public MySurfaceViewUpdaterThread(Game game, InputManager inputManager) {
        this.game = game;
        this.inputManager = inputManager;
    }

    @Override
    public void run() {
        int timeCounter = 0;
        long last = System.currentTimeMillis();

        while (running) {
            long now = System.currentTimeMillis();
            long elapsed = now - last;
            timeCounter += elapsed;
            last = now;

            if (timeCounter >= TIME_PER_FRAME) {
                /////////////////////
                inputManager.update(elapsed);
                game.update(elapsed);
                game.draw();
                /////////////////////
                timeCounter = 0;
            }

//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void shutdown() {
        running = false;
    }
}
