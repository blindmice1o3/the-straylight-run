package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.sink;

import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.IceShaker;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupHot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SinkFragment extends Fragment {
    public static final String TAG = SinkFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FrameLayout framelayoutSink;

    public SinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SinkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SinkFragment newInstance(String param1, String param2) {
        Log.e(TAG, "newInstance(String, String)");
        SinkFragment fragment = new SinkFragment();
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
        return inflater.inflate(R.layout.fragment_sink, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        framelayoutSink = view.findViewById(R.id.framelayout_sink);
        framelayoutSink.setOnDragListener(new SinkDragListener());
    }

    private class SinkDragListener
            implements View.OnDragListener {
        int resIdNormal = R.drawable.shape_sink;
        int resIdDropTarget = R.drawable.shape_droptarget;

        String label = null;

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        label = dragEvent.getClipDescription().getLabel().toString();

                        if (label.equals(SteamingPitcher.DRAG_LABEL) ||
                                label.equals(ShotGlass.DRAG_LABEL) ||
                                label.equals(CupHot.DRAG_LABEL) ||
                                label.equals(CupCold.DRAG_LABEL) ||
                                label.equals(IceShaker.DRAG_LABEL)) {
                            Log.d(TAG, "label.equals(" + SteamingPitcher.DRAG_LABEL + ") || label.equals(" + ShotGlass.DRAG_LABEL + ") || label.equals(" + CupHot.DRAG_LABEL + ") || label.equals(" + CupCold.DRAG_LABEL + ") || label.equals(" + IceShaker.DRAG_LABEL + ")");

                            // Change background drawable to indicate drop-target.
                            view.setBackgroundResource(resIdDropTarget);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        }
                    } else {
                        Log.e(TAG, "ACTION_DRAG_STARTED clip description NOT ClipDescription.MIMETYPE_TEXT_PLAIN");
                    }

                    // Return false to indicate that, during the current drag and drop
                    // operation, this View doesn't receive events again until
                    // ACTION_DRAG_ENDED is sent.
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG, "ACTION_DRAG_ENTERED");

                    // Change value of alpha to indicate [ENTERED] state.
                    view.setAlpha(0.5f);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // Ignore the event.
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "ACTION_DRAG_EXITED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "ACTION_DROP ");

                    if (label.equals(SteamingPitcher.DRAG_LABEL)) {
                        Log.d(TAG, "Derive steamingPitcher from dragData");
                        SteamingPitcher steamingPitcher = (SteamingPitcher) dragEvent.getLocalState();

                        Toast.makeText(getContext(), "emptying steaming pitcher", Toast.LENGTH_SHORT).show();
                        steamingPitcher.cancelTemperatureAnimator();
                        steamingPitcher.empty();
                    } else if (label.equals(ShotGlass.DRAG_LABEL)) {
                        Log.d(TAG, "Derive shotGlass from dragData");

                        ShotGlass shotGlass = (ShotGlass) dragEvent.getLocalState();
                        Toast.makeText(getContext(), "emptying shot glass", Toast.LENGTH_SHORT).show();
                        shotGlass.empty();
                    } else if (label.equals(CupHot.DRAG_LABEL) ||
                            label.equals(CupCold.DRAG_LABEL)) {
                        Log.d(TAG, "Derive CupImageView from dragData");

                        CupImageView cupImageView = (CupImageView) dragEvent.getLocalState();
                        Toast.makeText(getContext(), "emptying cup", Toast.LENGTH_SHORT).show();
                        cupImageView.empty();
                    } else if (label.equals(IceShaker.DRAG_LABEL)) {
                        Log.d(TAG, "Derive IceShaker from dragData");

                        IceShaker iceShaker = (IceShaker) dragEvent.getLocalState();
                        Toast.makeText(getContext(), "emptying ice shaker", Toast.LENGTH_SHORT).show();
                        iceShaker.empty();
                    } else {
                        Log.e(TAG, "unknown label.");
                    }

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);
                    // Reset the background drawable to normal.
                    view.setBackgroundResource(resIdNormal);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }
                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by SinkDragListener.");
                    break;
            }

            return false;
        }
    }
}