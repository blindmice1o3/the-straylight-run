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

    public static final String TAG_WHIPPED_CREAM = "whippedCream";

    private ConstraintLayout clRefrigerator;
    private ImageView ivMilkTwoPercent, ivMilkWhole;
    private ImageView ivMilkOat, ivMilkCoconut, ivMilkAlmond, ivMilkSoy;
    private ImageView ivWhippedCream;

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

        ivMilkTwoPercent = view.findViewById(R.id.milk_two_percent);
        ivMilkWhole = view.findViewById(R.id.milk_whole);
        ivMilkOat = view.findViewById(R.id.milk_oat);
        ivMilkCoconut = view.findViewById(R.id.milk_coconut);
        ivMilkAlmond = view.findViewById(R.id.milk_almond);
        ivMilkSoy = view.findViewById(R.id.milk_soy);
        ivWhippedCream = view.findViewById(R.id.view_whipped_cream);

        ivMilkTwoPercent.setTag(Milk.Type.TWO_PERCENT.name());
        ivMilkWhole.setTag(Milk.Type.WHOLE.name());
        ivMilkOat.setTag(Milk.Type.OAT.name());
        ivMilkCoconut.setTag(Milk.Type.COCONUT.name());
        ivMilkAlmond.setTag(Milk.Type.ALMOND.name());
        ivMilkSoy.setTag(Milk.Type.SOY.name());

        ivWhippedCream.setTag(TAG_WHIPPED_CREAM);

        View.OnTouchListener milkTouchListener = new MilkTouchListener();
        ivMilkTwoPercent.setOnTouchListener(milkTouchListener);
        ivMilkWhole.setOnTouchListener(milkTouchListener);
        ivMilkOat.setOnTouchListener(milkTouchListener);
        ivMilkCoconut.setOnTouchListener(milkTouchListener);
        ivMilkAlmond.setOnTouchListener(milkTouchListener);
        ivMilkSoy.setOnTouchListener(milkTouchListener);
    }

    private class MilkTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String label = "Milk";

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