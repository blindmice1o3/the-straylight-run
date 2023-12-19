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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.dialogfragments.FillSteamingPitcherDialogFragment;

public class MaestranaFragment extends Fragment
        implements FillSteamingPitcherDialogFragment.FillSteamingPitcherDialogListener {
    public static final String TAG = MaestranaFragment.class.getSimpleName();

    public static final String TAG_MILK_STEAMING_PITCHER = "milk steaming pitcher";
    public static final String TAG_ESPRESSO_SHOT = "espresso shot";

    private MaestranaViewModel mViewModel;
    private ConstraintLayout constraintLayoutMaestrana;
    private FrameLayout framelayoutEspressoStreamAndSteamWand;
    private FrameLayout framelayoutLeft, framelayoutCenter, framelayoutRight;
    private MilkSteamingPitcher milkSteamingPitcher;
    private ObjectAnimator animatorEspressoStream;

    private CupImageView ivToBeAdded;
    private float xTouch, yTouch;

    public static MaestranaFragment newInstance() {
        Log.e(TAG, "newInstance()");
        return new MaestranaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_maestrana, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        constraintLayoutMaestrana = view.findViewById(R.id.constraintlayout_maestrana);
        framelayoutEspressoStreamAndSteamWand = view.findViewById(R.id.framelayout_espresso_stream_and_steam_wand);
        framelayoutLeft = view.findViewById(R.id.framelayout_left);
        framelayoutCenter = view.findViewById(R.id.framelayout_center);
        framelayoutRight = view.findViewById(R.id.framelayout_right);

        View.OnDragListener maestranaDragListener = new MaestranaDragListener();
        framelayoutLeft.setOnDragListener(maestranaDragListener);
        framelayoutRight.setOnDragListener(maestranaDragListener);

        // MILK STEAMING PITCHER
        milkSteamingPitcher = new MilkSteamingPitcher(getContext());
        milkSteamingPitcher.setTag(TAG_MILK_STEAMING_PITCHER);
        milkSteamingPitcher.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        milkSteamingPitcher.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
        milkSteamingPitcher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(), "emptying cup", Toast.LENGTH_SHORT).show();

                milkSteamingPitcher.setMilkTag(null);
                milkSteamingPitcher.setCurrentMilkLevel(0);
                milkSteamingPitcher.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
                milkSteamingPitcher.invalidate();

                return true;
            }
        });
        milkSteamingPitcher.setOnDragListener(new MilkDragListener());
        milkSteamingPitcher.setX(30);
        milkSteamingPitcher.setY(50);
        framelayoutRight.addView(milkSteamingPitcher);

        // MILK STEAMING WAND
        // TODO:

        // ESPRESSO SHOT
        ImageView espressoShot = new ImageView(getContext());
        espressoShot.setTag(TAG_ESPRESSO_SHOT);
        espressoShot.setLayoutParams(new FrameLayout.LayoutParams(16, 64));
        espressoShot.setBackgroundColor(getResources().getColor(R.color.brown));
        espressoShot.setX(250);
//        framelayoutEspressoStreamAndSteamWand.addView(espressoShot);
        constraintLayoutMaestrana.addView(espressoShot);

        animatorEspressoStream = ObjectAnimator.ofFloat(
                espressoShot,
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

                    boolean colliding = isViewOverlapping(espressoShot, ivCup);
                    if (colliding) {
                        Log.e(TAG, "OVERLAP");
                    }
                    ivCup.update(colliding);

                    if (ivCup.isJustCollided()) {
                        Log.e(TAG, "ivCup.isJustCollided()");

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
        Log.e(TAG, "onActivityCreated()");

        mViewModel = new ViewModelProvider(this).get(MaestranaViewModel.class);
        // TODO: Use the ViewModel
    }

    private class MilkDragListener
            implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        if (dragEvent.getClipDescription().getLabel().equals("Milk")) {
                            Log.d(TAG, "label.equals(\"Milk\")");

                            // Change value of alpha to indicate drop-target.
                            view.setAlpha(0.8f);
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
                    view.setAlpha(0.8f);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "ACTION_DROP");

                    String dragData = dragEvent.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, dragData);

                    if (dragData.equals("coconut")) {
                        milkSteamingPitcher.setMilkTag("coconut");
                        milkSteamingPitcher.setBackgroundColor(getResources().getColor(R.color.green));
                    } else if (dragData.equals("almond")) {
                        milkSteamingPitcher.setMilkTag("almond");
                        milkSteamingPitcher.setBackgroundColor(getResources().getColor(R.color.brown));
                    } else if (dragData.equals("soy")) {
                        milkSteamingPitcher.setMilkTag("soy");
                        milkSteamingPitcher.setBackgroundColor(getResources().getColor(R.color.cream));
                    } else if (dragData.equals("null")) {
                        milkSteamingPitcher.setMilkTag(null);
                        milkSteamingPitcher.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
                    }
                    milkSteamingPitcher.invalidate();

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();

                        showFillSteamingPitcherDialog();
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by MilkDragListener.");
                    break;
            }

            return false;
        }
    }

    @Override
    public void onFinishFillDialog(int current) {
        Log.e(TAG, "onFinishFillDialog(int)");

        // TODO:
        int currentMilkLevel = current;
        Toast.makeText(getContext(), "currentMilkLevel: " + currentMilkLevel, Toast.LENGTH_SHORT).show();
        milkSteamingPitcher.setCurrentMilkLevel(currentMilkLevel);
        milkSteamingPitcher.invalidate();
    }

    private void showFillSteamingPitcherDialog() {
        int currentMilkLevelOfMilkSteamingPitcher = milkSteamingPitcher.getCurrentMilkLevel();
        FillSteamingPitcherDialogFragment dialogFragment =
                FillSteamingPitcherDialogFragment.newInstance(currentMilkLevelOfMilkSteamingPitcher);
        dialogFragment.setTargetFragment(MaestranaFragment.this, 300);
        dialogFragment.show(getParentFragmentManager(), FillSteamingPitcherDialogFragment.TAG);
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
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        if (dragEvent.getClipDescription().getLabel().equals("CaddyToMaestrana") ||
                                dragEvent.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                            Log.d(TAG, "label.equals(\"CaddyToMaestrana\") || label.equals(\"MaestranaToCaddy\")");

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

                    if (dragEvent.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                        ivToBeAdded = (CupImageView) dragEvent.getLocalState();
                    }

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
                    Log.d(TAG, "ACTION_DROP");

                    if (dragEvent.getClipDescription().getLabel().equals("CaddyToMaestrana")) {
                        Log.d(TAG, "ACTION_DROP dragEvent.getClipDescription().getLabel().equals(\"CaddyToMaestrana\")");
                        Log.d(TAG, "ACTION_DROP Instantiate ImageView for ivToBeAdded");

                        // Instantiate CupImageView.
                        ivToBeAdded = new CupImageView(getContext());
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        ivToBeAdded.setLayoutParams(layoutParams);
                        ivToBeAdded.setOnTouchListener(new MaestranaToCaddyTouchListener());

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
                            Log.e(TAG, "else-clause (generating ImageView to add to LinearLayout).");
                        }
                        ivToBeAdded.setBackgroundResource(resId);
                        ivToBeAdded.setTag(tag);
                    } else if (dragEvent.getClipDescription().getLabel().equals("MaestranaToCaddy")) {
                        Log.d(TAG, "ACTION_DROP dragEvent.getClipDescription().getLabel().equals(\"MaestranaToCaddy\")");
                        Log.d(TAG, "ACTION_DROP Derive ivToBeAdded from dragData");
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
                    Log.d(TAG, "ACTION_DRAG_ENDED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);
                    // Reset the background drawable to normal.
                    view.setBackgroundResource(resIdNormal);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();

                        if (ivToBeAdded != null) {
                            ivToBeAdded.setX(xTouch - (ivToBeAdded.getWidth() / 2));
                            ivToBeAdded.setY(yTouch - (ivToBeAdded.getHeight() / 2));
                        }
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }

                    if (ivToBeAdded != null) {
                        ivToBeAdded.setVisibility(View.VISIBLE);

                        ivToBeAdded = null;
                        xTouch = -1f;
                        yTouch = -1f;
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by MaestranaDragListener.");
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
                        view,               // The CupImageView.
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