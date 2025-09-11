package com.jackingaming.thestraylightrun.accelerometer.game.notes.topics;

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

    public enum NoteType {LEARNERS, TOPICS;}

    // ref: https://stackoverflow.com/questions/71579104/zoom-in-and-zoom-out-android-imageview

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NOTE_TYPE = "noteType";

    // TODO: Rename and change types of parameters
    private NoteType noteType;

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
     * @param noteType Parameter 1.
     * @return A new instance of fragment NotesViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesViewerFragment newInstance(NoteType noteType) {
        NotesViewerFragment fragment = new NotesViewerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_TYPE, noteType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteType = (NoteType) getArguments().getSerializable(ARG_NOTE_TYPE);

            drawbles = new ArrayList<>();

            if (noteType == NoteType.TOPICS) {
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_one_topic_methods)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_two_topic_if_else)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_three_topic_for_loops)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_four_topic_lists)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_topic_primitive_01)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_topic_primitive_02)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_topic_01)
                );
            } else if (noteType == NoteType.LEARNERS) {
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_one_learner_a_student)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_one_learner_honest_struggler)
                );
                drawbles.add(
                        getResources().getDrawable(R.drawable.notes_run_one_learner_not_trying)
                );
            }
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