package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;

public class LabelPrinterFragment extends Fragment {
    public static final String TAG = LabelPrinterFragment.class.getSimpleName();

    private FrameLayout frameLayoutLabelPrinter;
    private TextView tvLabelPrinter;

    public LabelPrinterFragment() {
        // Required empty public constructor
    }

    public static LabelPrinterFragment newInstance() {
        Log.e(TAG, "newInstance()");
        LabelPrinterFragment fragment = new LabelPrinterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label_printer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        frameLayoutLabelPrinter = view.findViewById(R.id.framelayout_label_printer);
        // TODO: temporarily disable drag-and-drop.
//        frameLayoutLabelPrinter.setOnTouchListener(new LabelPrinterTouchListener());

        tvLabelPrinter = view.findViewById(R.id.tv_label_printer);

        Drink drinkRandom = MenuItemRequestGenerator.requestRandomDrink();
        tvLabelPrinter.setText(
                String.format("%s - %s", drinkRandom.getSize(), drinkRandom.getName())
        );

        tvLabelPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drink drinkRandom = MenuItemRequestGenerator.requestRandomDrink();
                tvLabelPrinter.setText(
                        String.format("%s - %s", drinkRandom.getSize(), drinkRandom.getName())
                );
            }
        });
    }

    private class LabelPrinterTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String label = "LabelPrinter";

                ClipData dragData = ClipData.newPlainText(label, (CharSequence) getTag());
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                // Start the drag.
                view.startDragAndDrop(
                        dragData,           // The data to be dragged.
                        myShadow,           // The drag shadow builder.
                        view,               // The LabelPrinter.
                        0              // Flags. Not currently used, set to 0.
                );
                view.setVisibility(View.INVISIBLE);

                Log.e(TAG, "label: " + label);

                // Indicate that the on-touch event is handled.
                return true;
            }

            return false;
        }
    }
}