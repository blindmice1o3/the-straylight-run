package com.jackingaming.thestraylightrun.accelerometer.game.scenes.timer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;

public class CountdownTimer {
    public static final String TAG = CountdownTimer.class.getSimpleName();
    public static final long TARGET_COUNTDOWN_TIMER = 6000L;

    public interface CountdownListener {
        void onCountdownEnd();
    }

    private CountdownListener countdownListener;

    private long elapsed;
    private ObjectAnimator objectAnimator;

    public CountdownTimer(CountdownListener countdownListener) {
        this.countdownListener = countdownListener;

        elapsed = TARGET_COUNTDOWN_TIMER;

        objectAnimator = new ObjectAnimator();
        objectAnimator.setPropertyName("elapsed");
        objectAnimator.setFloatValues(elapsed, 0L);
        objectAnimator.setDuration(elapsed);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                countdownListener.onCountdownEnd();
            }
        });
    }

    public void start() {
        if (!objectAnimator.isRunning()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    objectAnimator.start();
                }
            });
        }
    }
}
