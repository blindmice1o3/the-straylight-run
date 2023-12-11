package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

public class RefrigeratorFragment extends Fragment {
    public static final String TAG_DEBUG = RefrigeratorFragment.class.getSimpleName();

    public static final String TAG_MILK_COCONUT = "coconut";
    public static final String TAG_MILK_ALMOND = "almond";
    public static final String TAG_MILK_SOY = "soy";

    private ConstraintLayout clRefrigerator;
    private View viewCoconutMilk, viewAlmondMilk, viewSoyMilk;

    public static RefrigeratorFragment newInstance() {
        Log.e(TAG_DEBUG, "newInstance()");
        return new RefrigeratorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG_DEBUG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_refrigerator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG_DEBUG, "onViewCreated()");

        clRefrigerator = view.findViewById(R.id.clRefrigerator);

        viewCoconutMilk = view.findViewById(R.id.milk_coconut);
        viewAlmondMilk = view.findViewById(R.id.milk_almond);
        viewSoyMilk = view.findViewById(R.id.milk_soy);

        viewCoconutMilk.setTag(TAG_MILK_COCONUT);
        viewAlmondMilk.setTag(TAG_MILK_ALMOND);
        viewSoyMilk.setTag(TAG_MILK_SOY);

        MilkTouchListener milkTouchListener = new MilkTouchListener();
        viewCoconutMilk.setOnTouchListener(milkTouchListener);
        viewAlmondMilk.setOnTouchListener(milkTouchListener);
        viewSoyMilk.setOnTouchListener(milkTouchListener);
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

                Log.e(TAG_DEBUG, "label: " + label);
                Log.e(TAG_DEBUG, "view.getTag(): " + view.getTag());

                // Indicate that the on-touch event is handled.
                return true;
            }

            return false;
        }
    }
}