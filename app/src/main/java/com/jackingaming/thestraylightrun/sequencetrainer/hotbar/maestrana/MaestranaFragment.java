package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupImageView;

public class MaestranaFragment extends Fragment {
    public static final String TAG_DEBUG = MaestranaFragment.class.getSimpleName();

    private MaestranaViewModel mViewModel;
    private ConstraintLayout constraintLayoutMaestrana;
    private FrameLayout framelayoutEspressoStreamAndSteamWand,
            framelayoutLeft, framelayoutCenter, framelayoutRight;
    private ObjectAnimator animatorEspressoStream;

    private View ivToBeAdded;
    private float xTouch, yTouch;

    public static MaestranaFragment newInstance() {
        Log.e(TAG_DEBUG, "newInstance()");
        return new MaestranaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG_DEBUG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_maestrana, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG_DEBUG, "onViewCreated()");

        constraintLayoutMaestrana = view.findViewById(R.id.constraintlayout_maestrana);
        framelayoutEspressoStreamAndSteamWand = view.findViewById(R.id.framelayout_espresso_stream_and_steam_wand);
        framelayoutLeft = view.findViewById(R.id.framelayout_left);
        framelayoutCenter = view.findViewById(R.id.framelayout_center);
        framelayoutRight = view.findViewById(R.id.framelayout_right);

        View.OnDragListener maestranaDragListener = new MaestranaDragListener();
        framelayoutLeft.setOnDragListener(maestranaDragListener);
        framelayoutRight.setOnDragListener(maestranaDragListener);

        ImageView espressoStream = new ImageView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                16,
                64);
        espressoStream.setLayoutParams(params);
        espressoStream.setBackgroundColor(getResources().getColor(R.color.brown));
        espressoStream.setX(250);
//        framelayoutEspressoStreamAndSteamWand.addView(espressoStream);
        constraintLayoutMaestrana.addView(espressoStream);

        animatorEspressoStream = ObjectAnimator.ofFloat(
                espressoStream,
                "y",
                0f,
                800f);
        animatorEspressoStream.setDuration(4000);
        animatorEspressoStream.setRepeatCount(ObjectAnimator.INFINITE);
        animatorEspressoStream.setRepeatMode(ObjectAnimator.REVERSE);
        animatorEspressoStream.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorEspressoStream.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                for (int i = 0; i < framelayoutLeft.getChildCount(); i++) {
                    CupImageView ivCup = (CupImageView) framelayoutLeft.getChildAt(i);

                    boolean colliding = isViewOverlapping(espressoStream, ivCup);
                    if (colliding) {
                        Log.e(TAG_DEBUG, "OVERLAP");
                    }
                    ivCup.update(colliding);

                    if (ivCup.isJustCollided()) {
                        Log.e(TAG_DEBUG, "ivCup.isJustCollided()");

                        ivCup.onCollided();
                    }
                }
            }
        });
        animatorEspressoStream.start();
    }

    private boolean isViewOverlapping(View firstView, View secondView) {
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        firstView.getLocationOnScreen(firstPosition);
        secondView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        secondView.getLocationOnScreen(secondPosition);

        return firstPosition[0] < secondPosition[0] + secondView.getMeasuredWidth()
                && firstPosition[0] + firstView.getMeasuredWidth() > secondPosition[0]
                && firstPosition[1] < secondPosition[1] + secondView.getMeasuredHeight()
                && firstPosition[1] + firstView.getMeasuredHeight() > secondPosition[1];
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG_DEBUG, "onActivityCreated()");

        mViewModel = new ViewModelProvider(this).get(MaestranaViewModel.class);
        // TODO: Use the ViewModel
    }

    private class MaestranaDragListener
            implements View.OnDragListener {
        int resIdNormal = R.drawable.shape_maestrana;
        int resIdDropTarget = R.drawable.shape_droptarget;

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG_DEBUG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        if (dragEvent.getClipDescription().getLabel().equals("CaddyToMaestrana") ||
                                dragEvent.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                            Log.d(TAG_DEBUG, "label.equals(\"CaddyToMaestrana\") || label.equals(\"MaestranaToCaddy\")");

                            // Change background drawable to indicate drop-target.
                            view.setBackgroundResource(resIdDropTarget);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        }
                    } else {
                        Log.e(TAG_DEBUG, "ACTION_DRAG_STARTED clip description NOT ClipDescription.MIMETYPE_TEXT_PLAIN");
                    }

                    // Return false to indicate that, during the current drag and drop
                    // operation, this View doesn't receive events again until
                    // ACTION_DRAG_ENDED is sent.
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG_DEBUG, "ACTION_DRAG_ENTERED");

                    // Change value of alpha to indicate [ENTERED] state.
                    view.setAlpha(0.5f);

                    if (dragEvent.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                        ivToBeAdded = (CupImageView) dragEvent.getLocalState();
                    }

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // Ignore the event.
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG_DEBUG, "ACTION_DRAG_EXITED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DROP:
                    // TODO:
                    if (dragEvent.getClipDescription().getLabel().equals("CaddyToMaestrana")) {
                        Log.d(TAG_DEBUG, "ACTION_DROP dragEvent.getClipDescription().getLabel().equals(\"CaddyToMaestrana\")");
                        Log.d(TAG_DEBUG, "ACTION_DROP Instantiate ImageView for ivToBeAdded");

                        // Instantiate CupImageView.
                        ivToBeAdded = new CupImageView(getContext());
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        ivToBeAdded.setLayoutParams(layoutParams);

                        // Get the item containing the dragged data.
                        ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                        // Get the text data from the item.
                        String dragData = item.getText().toString();

                        // Display a message containing the dragged data.
                        Toast.makeText(getContext(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        // Set ImageView's background and tag according to dragData.
                        int resId = -1;
                        String tag = null;
                        if (dragData.equals("trenta")) {
                            resId = R.drawable.drinksize_trenta;
                            tag = "trenta";
                        } else if (dragData.equals("venti")) {
                            resId = R.drawable.drinksize_venti;
                            tag = "venti";
                        } else if (dragData.equals("grande")) {
                            resId = R.drawable.drinksize_grande;
                            tag = "grande";
                        } else if (dragData.equals("tall")) {
                            resId = R.drawable.drinksize_tall;
                            tag = "tall";
                        } else if (dragData.equals("short")) {
                            resId = R.drawable.drinksize_short;
                            tag = "short";
                        } else {
                            Log.e(TAG_DEBUG, "else-clause (generating ImageView to add to LinearLayout).");
                        }
                        ivToBeAdded.setBackgroundResource(resId);
                        ivToBeAdded.setTag(tag);
                    } else if (dragEvent.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                        Log.d(TAG_DEBUG, "ACTION_DROP dragEvent.getClipDescription().getLabel().equals(\"MaestranaToCaddy\")");
                        Log.d(TAG_DEBUG, "ACTION_DROP Derive ivToBeAdded from dragData");
                        ivToBeAdded = (CupImageView) dragEvent.getLocalState();

                        ViewGroup owner = (ViewGroup) ivToBeAdded.getParent();
                        owner.removeView(ivToBeAdded);
                    }

                    // Add ImageView to FrameLayout.
                    ((FrameLayout) view).addView(ivToBeAdded);
                    ivToBeAdded.setVisibility(View.INVISIBLE);

                    xTouch = dragEvent.getX();
                    yTouch = dragEvent.getY();

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

                        if (ivToBeAdded != null) {
                            View.OnTouchListener maestranaToCaddyTouchListener = new MaestranaToCaddyTouchListener();
                            ivToBeAdded.setOnTouchListener(maestranaToCaddyTouchListener);

                            ivToBeAdded.setX(xTouch - (ivToBeAdded.getWidth() / 2));
                            ivToBeAdded.setY(yTouch - (ivToBeAdded.getHeight() / 2));

                            ivToBeAdded.setVisibility(View.VISIBLE);

                            ivToBeAdded = null;
                            xTouch = -1f;
                            yTouch = -1f;
                        }
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();

                        if (ivToBeAdded != null) {
                            ivToBeAdded.setVisibility(View.VISIBLE);

                            ivToBeAdded = null;
                            xTouch = -1f;
                            yTouch = -1f;
                        }
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG_DEBUG, "Unknown action type received by MaestranaDragListener.");
                    break;
            }

            return false;
        }
    }

    private class MaestranaToCaddyTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String label = "MaestranaToCaddy";

                ClipData dragData = ClipData.newPlainText(label, (CharSequence) view.getTag());
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                // Start the drag.
                view.startDragAndDrop(
                        dragData,           // The data to be dragged.
                        myShadow,           // The drag shadow builder.
                        view,               // The ImageView.
                        0              // Flags. Not currently used, set to 0.
                );
                view.setVisibility(View.INVISIBLE);

                Log.e(TAG_DEBUG, "label: " + label);

                // Indicate that the on-touch event is handled.
                return true;
            }

            return false;
        }
    }
}