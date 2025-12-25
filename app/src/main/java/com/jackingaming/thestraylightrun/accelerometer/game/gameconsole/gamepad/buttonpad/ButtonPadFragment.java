package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.gamepad.buttonpad;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

public class ButtonPadFragment extends Fragment {
    public static final String TAG = ButtonPadFragment.class.getSimpleName();

    public enum Button {BUTTON_MENU, BUTTON_A, BUTTON_B;}

    public interface ButtonPadListener {
        void onButtonPadTouched(Button button, MotionEvent event);
    }

    private ButtonPadListener buttonPadListener;

    public void setButtonPadListener(ButtonPadListener buttonPadListener) {
        this.buttonPadListener = buttonPadListener;
    }

    private ConstraintLayout constraintLayout;
    // TODO: associate imageViewButtonMenu, imageViewButtonA, and
    //  imageViewButtonB with R.string.text_menu, R.string.text_a, and
    //  R.string.text_b (instead of "MENU", "A", and "B" hard-coded
    //  inside the image).
    private ImageView imageViewButtonMenu;
    private ImageView imageViewButtonA;
    private ImageView imageViewButtonB;

    private Rect boundsOfButtonMenu;
    private Rect boundsOfButtonA;
    private Rect boundsOfButtonB;

    public ButtonPadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_pad, container, false);

        constraintLayout = view.findViewById(R.id.constraintlayout_button_pad_fragment);
        imageViewButtonMenu = view.findViewById(R.id.imageview_button_menu_button_pad_fragment);
        imageViewButtonA = view.findViewById(R.id.imageview_button_a_button_pad_fragment);
        imageViewButtonB = view.findViewById(R.id.imageview_button_b_button_pad_fragment);

        Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.d_pad_custom_color);
        Bitmap imageButtonMenu = Bitmap.createBitmap(source, 172, 375, 136, 52);
        Bitmap imageButtonA = Bitmap.createBitmap(source, 172, 435, 64, 52);
        Bitmap imageButtonB = Bitmap.createBitmap(source, 244, 435, 64, 52);

        imageViewButtonMenu.setImageBitmap(imageButtonMenu);
        imageViewButtonA.setImageBitmap(imageButtonA);
        imageViewButtonB.setImageBitmap(imageButtonB);

        return view;
    }

    public void setupOnTouchListener() {
        boundsOfButtonMenu = new Rect(imageViewButtonMenu.getLeft(), imageViewButtonMenu.getTop(), imageViewButtonMenu.getRight(), imageViewButtonMenu.getBottom());
        Log.d(TAG, "<" + imageViewButtonMenu.getLeft() + ", " + imageViewButtonMenu.getTop() + ", " + imageViewButtonMenu.getRight() + ", " + imageViewButtonMenu.getBottom() + ">");
        boundsOfButtonA = new Rect(imageViewButtonA.getLeft(), imageViewButtonA.getTop(), imageViewButtonA.getRight(), imageViewButtonA.getBottom());
        boundsOfButtonB = new Rect(imageViewButtonB.getLeft(), imageViewButtonB.getTop(), imageViewButtonB.getRight(), imageViewButtonB.getBottom());

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button button = null;

                // Determine if the touch event occurred within the bounds of a "button".
                if (boundsOfButtonMenu.contains((int) event.getX(), (int) event.getY())) {
                    if (imageViewButtonMenu.getVisibility() == View.VISIBLE) {
                        button = Button.BUTTON_MENU;
                    }
                } else if (boundsOfButtonA.contains((int) event.getX(), (int) event.getY())) {
                    button = Button.BUTTON_A;
                } else if (boundsOfButtonB.contains((int) event.getX(), (int) event.getY())) {
                    button = Button.BUTTON_B;
                }

                if (button != null) {
                    buttonPadListener.onButtonPadTouched(button, event);
                    return true;
                }

                return false;
            }
        });
    }

    public void hideImageViewButtonMenu() {
        imageViewButtonMenu.setVisibility(View.INVISIBLE);
    }

    public void showImageViewButtonMenu() {
        imageViewButtonMenu.setVisibility(View.VISIBLE);
    }
}