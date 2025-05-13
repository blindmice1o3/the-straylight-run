package com.jackingaming.thestraylightrun.accelerometer.game.notes;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesViewerFragment extends Fragment
        implements Serializable {
    public static final String TAG = NotesViewerFragment.class.getSimpleName();

    // ref: https://stackoverflow.com/questions/71579104/zoom-in-and-zoom-out-android-imageview

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FrameLayout flNotesViewer;
    private ScrollView svZoom;
    private ImageView ivNotes;
    private List<Drawable> drawbles;
    private int indexDrawables;

    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector gestureDetector;

    public NotesViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesViewerFragment newInstance(String param1, String param2) {
        NotesViewerFragment fragment = new NotesViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            drawbles = new ArrayList<>();
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_01)
            );
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_02)
            );
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_03)
            );
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_04)
            );
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_05)
            );
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_06)
            );
            drawbles.add(
                    getResources().getDrawable(R.drawable.notes_07)
            );
            indexDrawables = 0;

            gestureDetector = new GestureDetector(getContext(), new GestureListener());

            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    float scale = 1 - detector.getScaleFactor();

                    float prevScale = mScale;
                    mScale += scale;

                    if (mScale < 0.1f) // Minimum scale condition:
                        mScale = 0.1f;

                    if (mScale > 10f) // Maximum scale condition:
                        mScale = 10f;
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                    scaleAnimation.setDuration(0);
                    scaleAnimation.setFillAfter(true);
                    ScrollView layout = svZoom;

                    layout.startAnimation(scaleAnimation);
                    return true;
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flNotesViewer = view.findViewById(R.id.fl_notes_viewer);

        svZoom = new ScrollView(getContext()) {
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                super.dispatchTouchEvent(event);
                mScaleDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);
                return gestureDetector.onTouchEvent(event);
            }
        };
        svZoom.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ivNotes = new AppCompatImageView(getContext());
        ivNotes.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ivNotes.setScaleType(ImageView.ScaleType.FIT_START);
        ivNotes.setImageDrawable(
                drawbles.get(indexDrawables)
        );
        ivNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexDrawables++;
                if (indexDrawables > drawbles.size() - 1) {
                    indexDrawables = 0;
                }

                ivNotes.setImageDrawable(
                        drawbles.get(indexDrawables)
                );
            }
        });

        svZoom.addView(ivNotes);

        flNotesViewer.addView(svZoom);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            return true;
        }
    }
}