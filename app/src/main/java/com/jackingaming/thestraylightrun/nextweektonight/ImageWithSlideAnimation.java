package com.jackingaming.thestraylightrun.nextweektonight;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

public class ImageWithSlideAnimation {
    private ImageView imageView;
    private ObjectAnimator animatorSlideDown;

    public ImageWithSlideAnimation(ImageView imageView) {
        this.imageView = imageView;
        animatorSlideDown = ObjectAnimator.ofFloat(imageView,
                "translationY", 128, 0);
        animatorSlideDown.setDuration(4000L);
        animatorSlideDown.setInterpolator(new AnticipateOvershootInterpolator());
    }

    public void startAnimator() {
        animatorSlideDown.setFloatValues(imageView.getY(),
                imageView.getY() + 280);
        animatorSlideDown.start();
    }
}
