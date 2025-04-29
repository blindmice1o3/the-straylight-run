package com.jackingaming.thestraylightrun.accelerometer.game.drawers.groupchat;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;

public class SlideFromBottomItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_from_bottom_via_translate);
        holder.itemView.startAnimation(animation);
        return super.animateAdd(holder);
    }
}
