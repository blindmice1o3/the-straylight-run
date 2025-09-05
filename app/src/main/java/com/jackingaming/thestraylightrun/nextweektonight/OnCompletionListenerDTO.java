package com.jackingaming.thestraylightrun.nextweektonight;

import android.media.MediaPlayer;

import java.io.Serializable;

public class OnCompletionListenerDTO
        implements Serializable {
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public OnCompletionListenerDTO(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public MediaPlayer.OnCompletionListener getOnCompletionListener() {
        return onCompletionListener;
    }
}
