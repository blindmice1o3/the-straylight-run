package com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import java.io.Serializable;

public class TypeWriterTextView extends AppCompatTextView {
    public interface TextCompletionListener extends Serializable {
        void onAnimationFinish();
    }

    private TextCompletionListener textCompletionListener;

    private CharSequence sequence;
    private int mIndex;
    private long delay = 150; //default is 150 milliseconds

    public TypeWriterTextView(Context context) {
        super(context);
    }

    public TypeWriterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            setText(sequence.subSequence(0, mIndex++));
            if (mIndex <= sequence.length()) {
                handler.postDelayed(runnable, delay);
            } else {
                if (textCompletionListener != null) {
                    textCompletionListener.onAnimationFinish();
                }
            }
        }
    };

    /**
     * Display text with type writer animation
     *
     * @param txt content will be displayed
     */
    public void displayTextWithAnimation(CharSequence txt) {
        sequence = txt;
        mIndex = 0;

        setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }

    /**
     * Change the delay value with this method
     *
     * @param m
     */
    public void setCharacterDelay(long m) {
        delay = m;
    }

    public void setTextCompletionListener(TextCompletionListener textCompletionListener) {
        this.textCompletionListener = textCompletionListener;
    }
}
