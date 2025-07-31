package com.jackingaming.thestraylightrun.accelerometer.game.scenes.tiles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

public class UniqueSolidTile extends SolidTile {

    public static final String COMPUTER = "computer";
    public static final String GAME_CONSOLE = "game console";
    public static final String TELEVISION = "television";
    public static final String TABLE = "table";
    public static final String BED = "bed";

    private String id;

    private Paint paintCircle;
    private float radiusCirle = 4f;
    private int xCircleNow = -1;
    private int yCircleNow = -1;
    private int widthSpriteDst;
    private int heightSpriteDst;
    private ObjectAnimator animatorCircleRight;
    private ObjectAnimator animatorCircleDown;
    private ObjectAnimator animatorCircleLeft;
    private ObjectAnimator animatorCircleUp;

    public UniqueSolidTile(String id, Bitmap texture) {
        super(texture);

        this.id = id;
    }

    private void initCircle() {
        paintCircle = new Paint();
        paintCircle.setColor(Color.GREEN);
        paintCircle.setStyle(Paint.Style.FILL);

        animatorCircleRight = ObjectAnimator.ofInt(this, "xCircleNow", xCircleNow, (int) (xCircleNow + widthSpriteDst - radiusCirle));
        animatorCircleDown = ObjectAnimator.ofInt(this, "yCircleNow", yCircleNow, (int) (yCircleNow + heightSpriteDst - radiusCirle));
        animatorCircleLeft = ObjectAnimator.ofInt(this, "xCircleNow", (int) (xCircleNow + widthSpriteDst - radiusCirle), xCircleNow);
        animatorCircleUp = ObjectAnimator.ofInt(this, "yCircleNow", (int) (yCircleNow + heightSpriteDst - radiusCirle), yCircleNow);

        animatorCircleRight.setDuration(1000L);
        animatorCircleDown.setDuration(1000L);
        animatorCircleLeft.setDuration(1000L);
        animatorCircleUp.setDuration(1000L);
    }

    public void startCirleAnimation() {
        animatorCircleRight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorCircleDown.start();
            }
        });
        animatorCircleDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorCircleLeft.start();
            }
        });
        animatorCircleLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorCircleUp.start();
            }
        });
        animatorCircleUp.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorCircleRight.start();
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                animatorCircleRight.start();
            }
        });
    }

    public void stopCirleAnimation() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                animatorCircleRight.removeAllListeners();
                animatorCircleDown.removeAllListeners();
                animatorCircleLeft.removeAllListeners();
                animatorCircleUp.removeAllListeners();

                animatorCircleRight.cancel();
                animatorCircleDown.cancel();
                animatorCircleLeft.cancel();
                animatorCircleUp.cancel();
            }
        });
    }

    public boolean isAnimationRunning() {
        if (animatorCircleRight == null ||
                animatorCircleDown == null ||
                animatorCircleLeft == null ||
                animatorCircleUp == null) {
            return false;
        }

        return animatorCircleRight.isRunning() ||
                animatorCircleDown.isRunning() ||
                animatorCircleLeft.isRunning() ||
                animatorCircleUp.isRunning();
    }

    @Override
    public void render(Canvas canvas, int x, int y, int widthSpriteDst, int heightSpriteDst) {
        super.render(canvas, x, y, widthSpriteDst, heightSpriteDst);

        if (xCircleNow < 0 && yCircleNow < 0) {
            xCircleNow = x;
            yCircleNow = y;
            this.widthSpriteDst = widthSpriteDst;
            this.heightSpriteDst = heightSpriteDst;

            initCircle();
        }

        if (isAnimationRunning()) {
            canvas.drawCircle(xCircleNow, yCircleNow, radiusCirle, paintCircle);
        }
    }

    public String getId() {
        return id;
    }

    public int getXCircleNow() {
        return xCircleNow;
    }

    public void setXCircleNow(int xCircle) {
        this.xCircleNow = xCircle;
    }

    public int getYCircleNow() {
        return yCircleNow;
    }

    public void setYCircleNow(int yCircle) {
        this.yCircleNow = yCircle;
    }
}
