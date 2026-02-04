package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.icebin;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IceBinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IceBinFragment extends Fragment {
    public static final String TAG = IceBinFragment.class.getSimpleName();
    public static final String DRAG_LABEL = IceBinFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FrameLayout framelayoutIceBin;
    private View viewDragShadow;

    public IceBinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IceBinFragment.
     */
    public static IceBinFragment newInstance(String param1, String param2) {
        Log.e(TAG, "newInstance(String, String)");
        IceBinFragment fragment = new IceBinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ice_bin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        framelayoutIceBin = view.findViewById(R.id.framelayout_ice_bin);
        framelayoutIceBin.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    String label = DRAG_LABEL;

                    ClipData dragData = ClipData.newPlainText(label, "ice");
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(viewDragShadow);

                    // Start the drag.
                    view.startDragAndDrop(
                            dragData,           // The data to be dragged.
                            myShadow,           // The drag shadow builder.
                            null,    // No need to use local data.
                            0              // Flags. Not currently used, set to 0.
                    );

                    Log.e(TAG, "label: " + label);

                    // Indicate that the on-touch event is handled.
                    return true;
                }

                return false;
            }
        });

        viewDragShadow = new View(getContext());
        viewDragShadow.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        viewDragShadow.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
        viewDragShadow.setVisibility(View.INVISIBLE);
        framelayoutIceBin.addView(viewDragShadow);
    }
}