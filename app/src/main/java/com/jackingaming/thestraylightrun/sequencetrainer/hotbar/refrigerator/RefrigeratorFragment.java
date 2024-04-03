package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;

public class RefrigeratorFragment extends Fragment {
    public static final String TAG = RefrigeratorFragment.class.getSimpleName();
    public static final String DRAG_LABEL_MILK = "Milk";
    public static final String DRAG_LABEL_WHIPPED_CREAM = "WhippedCream";

    public static final String TAG_SPRITE_MILK_TWO_PERCENT = Milk.Type.TWO_PERCENT.name();
    public static final String TAG_SPRITE_MILK_WHOLE = Milk.Type.WHOLE.name();
    public static final String TAG_SPRITE_MILK_OAT = Milk.Type.OAT.name();
    public static final String TAG_SPRITE_MILK_COCONUT = Milk.Type.COCONUT.name();
    public static final String TAG_SPRITE_MILK_ALMOND = Milk.Type.ALMOND.name();
    public static final String TAG_SPRITE_MILK_SOY = Milk.Type.SOY.name();
    public static final String TAG_SPRITE_WHIPPED_CREAM = "WhippedCream";

    public static final int TEMPERATURE = 37;

    private ConstraintLayout clRefrigerator;
    private ImageView spriteMilkTwoPercent, spriteMilkWhole;
    private ImageView spriteMilkOat, spriteMilkCoconut, spriteMilkAlmond, spriteMilkSoy;
    private ImageView spriteWhippedCream;

    public static RefrigeratorFragment newInstance() {
        Log.e(TAG, "newInstance()");
        return new RefrigeratorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_refrigerator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        clRefrigerator = view.findViewById(R.id.constraintlayout_refrigerator);

        spriteMilkTwoPercent = view.findViewById(R.id.milk_two_percent);
        spriteMilkWhole = view.findViewById(R.id.milk_whole);
        spriteMilkOat = view.findViewById(R.id.milk_oat);
        spriteMilkCoconut = view.findViewById(R.id.milk_coconut);
        spriteMilkAlmond = view.findViewById(R.id.milk_almond);
        spriteMilkSoy = view.findViewById(R.id.milk_soy);
        spriteWhippedCream = view.findViewById(R.id.view_whipped_cream);

        spriteMilkTwoPercent.setTag(TAG_SPRITE_MILK_TWO_PERCENT);
        spriteMilkWhole.setTag(TAG_SPRITE_MILK_WHOLE);
        spriteMilkOat.setTag(TAG_SPRITE_MILK_OAT);
        spriteMilkCoconut.setTag(TAG_SPRITE_MILK_COCONUT);
        spriteMilkAlmond.setTag(TAG_SPRITE_MILK_ALMOND);
        spriteMilkSoy.setTag(TAG_SPRITE_MILK_SOY);
        spriteWhippedCream.setTag(TAG_SPRITE_WHIPPED_CREAM);

        View.OnTouchListener milkTouchListener = new MilkTouchListener();
        spriteMilkTwoPercent.setOnTouchListener(milkTouchListener);
        spriteMilkWhole.setOnTouchListener(milkTouchListener);
        spriteMilkOat.setOnTouchListener(milkTouchListener);
        spriteMilkCoconut.setOnTouchListener(milkTouchListener);
        spriteMilkAlmond.setOnTouchListener(milkTouchListener);
        spriteMilkSoy.setOnTouchListener(milkTouchListener);
        View.OnTouchListener whippedCreamTouchListener = new WhippedCreamTouchListener();
        spriteWhippedCream.setOnTouchListener(whippedCreamTouchListener);
    }

    private class MilkTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String label = DRAG_LABEL_MILK;

                ClipData dragData = ClipData.newPlainText(label, (CharSequence) view.getTag());
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                // Start the drag.
                view.startDragAndDrop(
                        dragData,           // The data to be dragged.
                        myShadow,           // The drag shadow builder.
                        null,    // No need to use local data.
                        0              // Flags. Not currently used, set to 0.
                );

                Log.e(TAG, "label: " + label);
                Log.e(TAG, "getTag(): " + getTag());

                // Indicate that the on-touch event is handled.
                return true;
            }

            return false;
        }
    }

    private class WhippedCreamTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String label = DRAG_LABEL_WHIPPED_CREAM;

                ClipData dragData = ClipData.newPlainText(label, (CharSequence) view.getTag());
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                // Start the drag.
                view.startDragAndDrop(
                        dragData,           // The data to be dragged.
                        myShadow,           // The drag shadow builder.
                        null,    // No need to use local data.
                        0              // Flags. Not currently used, set to 0.
                );

                Log.e(TAG, "label: " + label);
                Log.e(TAG, "getTag(): " + getTag());

                // Indicate that the on-touch event is handled.
                return true;
            }

            return false;
        }
    }
}