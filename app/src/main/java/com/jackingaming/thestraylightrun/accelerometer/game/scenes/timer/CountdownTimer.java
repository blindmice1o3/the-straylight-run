package com.jackingaming.thestraylightrun.accelerometer.game.scenes.timer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;

import java.io.Serializable;

public class CountdownTimer
        implements Serializable {
    public static final String TAG = CountdownTimer.class.getSimpleName();

    public interface CountdownListener {
        void onCountdownEnd();
    }

    private CountdownListener countdownListener;
    private long targetCountdownTimer;

    transient private ObjectAnimator objectAnimator;

    public CountdownTimer(CountdownListener countdownListener, long targetCountdownTimer) {
        this.countdownListener = countdownListener;
        this.targetCountdownTimer = targetCountdownTimer;
    }

    public void init() {
        objectAnimator = new ObjectAnimator();
        objectAnimator.setPropertyName("elapsed");
        objectAnimator.setFloatValues(targetCountdownTimer, 0L);
        objectAnimator.setDuration(targetCountdownTimer);
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
