package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.io.Serializable;

public abstract class ClickableAndDraggableImageView extends AppCompatImageView
        implements Serializable {
    public interface ShowDialogFragmentListener {
        void showSpriteDetailsDialogFragment(ClickableAndDraggableImageView clickableAndDraggableImageView);
    }

    protected ShowDialogFragmentListener listenerShowDialog;

    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;

    public ClickableAndDraggableImageView(@NonNull Context context) {
        super(context);
    }

    public ClickableAndDraggableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract void doClick(MotionEvent event);

    protected abstract void doMove(MotionEvent event);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                isOnClick = true;
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isOnClick) {
                    doClick(event);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {
                    isOnClick = false;

                    doMove(event);
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setListenerShowDialog(ShowDialogFragmentListener listenerShowDialog) {
        this.listenerShowDialog = listenerShowDialog;
    }
}