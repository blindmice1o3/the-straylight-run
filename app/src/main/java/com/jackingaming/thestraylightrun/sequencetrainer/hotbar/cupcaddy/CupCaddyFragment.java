package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupHot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

public class CupCaddyFragment extends Fragment {
    public static final String TAG = CupCaddyFragment.class.getSimpleName();
    public static final String DRAG_LABEL = CupCaddyFragment.class.getSimpleName();

    public static final String TAG_COLD_TALL = "coldTall";
    public static final String TAG_COLD_GRANDE = "coldGrande";
    public static final String TAG_COLD_VENTI = "coldVenti";
    public static final String TAG_COLD_TRENTA = "coldTrenta";
    public static final String TAG_HOT_VENTI = "hotVenti";
    public static final String TAG_HOT_GRANDE = "hotGrande";
    public static final String TAG_HOT_TALL = "hotTall";
    public static final String TAG_HOT_SHORT = "hotShort";

    private CupCaddyViewModel mViewModel;
    private ConstraintLayout constraintLayoutCupCaddy;
    private ImageView ivColdTall, ivColdGrande, ivColdVenti, ivColdTrenta;
    private ImageView ivHotVenti, ivHotGrande, ivHotTall, ivHotShort;

    public static CupCaddyFragment newInstance() {
        Log.e(TAG, "newInstance()");
        return new CupCaddyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_cup_caddy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        constraintLayoutCupCaddy = view.findViewById(R.id.constraintlayout_cup_caddy);
        constraintLayoutCupCaddy.setOnDragListener(new CupCaddyDragListener());

        ivColdTall = view.findViewById(R.id.iv_cold_tall);
        ivColdGrande = view.findViewById(R.id.iv_cold_grande);
        ivColdVenti = view.findViewById(R.id.iv_cold_venti);
        ivColdTrenta = view.findViewById(R.id.iv_cold_trenta);
        ivHotVenti = view.findViewById(R.id.iv_hot_venti);
        ivHotGrande = view.findViewById(R.id.iv_hot_grande);
        ivHotTall = view.findViewById(R.id.iv_hot_tall);
        ivHotShort = view.findViewById(R.id.iv_hot_short);

        ivColdTall.setTag(TAG_COLD_TALL);
        ivColdGrande.setTag(TAG_COLD_GRANDE);
        ivColdVenti.setTag(TAG_COLD_VENTI);
        ivColdTrenta.setTag(TAG_COLD_TRENTA);
        ivHotVenti.setTag(TAG_HOT_VENTI);
        ivHotGrande.setTag(TAG_HOT_GRANDE);
        ivHotTall.setTag(TAG_HOT_TALL);
        ivHotShort.setTag(TAG_HOT_SHORT);

        View.OnTouchListener caddyToMastrenaTouchListener = new CaddyToMastrenaTouchListener();
        ivColdTall.setOnTouchListener(caddyToMastrenaTouchListener);
        ivColdGrande.setOnTouchListener(caddyToMastrenaTouchListener);
        ivColdVenti.setOnTouchListener(caddyToMastrenaTouchListener);
        ivColdTrenta.setOnTouchListener(caddyToMastrenaTouchListener);
        ivHotVenti.setOnTouchListener(caddyToMastrenaTouchListener);
        ivHotGrande.setOnTouchListener(caddyToMastrenaTouchListener);
        ivHotTall.setOnTouchListener(caddyToMastrenaTouchListener);
        ivHotShort.setOnTouchListener(caddyToMastrenaTouchListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated()");

        mViewModel = new ViewModelProvider(this).get(CupCaddyViewModel.class);
        // TODO: Use the ViewModel
    }

    private class CupCaddyDragListener
            implements View.OnDragListener {
        int resIdNormal = R.drawable.shape_cup_caddy;
        int resIdDropTarget = R.drawable.shape_droptarget;

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        if (dragEvent.getClipDescription().getLabel().equals(CupHot.DRAG_LABEL) ||
                                dragEvent.getClipDescription().getLabel().equals(CupCold.DRAG_LABEL)) {
                            Log.d(TAG, "label.equals(CupHot.DRAG_LABEL) || label.equals(CupCold.DRAG_LABEL)");

                            if (((CupImageView) dragEvent.getLocalState()).getMilk() == null &&
                                    ((CupImageView) dragEvent.getLocalState()).getShots().size() == 0) {
                                Log.d(TAG, "((CupImageView) dragEvent.getLocalState()).getMilk() == null && ((CupImageView) dragEvent.getLocalState()).getShots().size() == 0");

                                // Change background drawable to indicate drop-target.
                                view.setBackgroundResource(resIdDropTarget);

                                // Return true to indicate that the View can accept the dragged
                                // data.
                                return true;
                            } else {
                                Log.e(TAG, "NOT ((CupImageView) dragEvent.getLocalState()).getMilk() == null && ((CupImageView) dragEvent.getLocalState()).getShots().size() == 0");
                            }
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
                    Log.d(TAG, "ACTION_DROP Derive ivToBeAdded from dragData");
                    CupImageView ivToBeAdded = (CupImageView) dragEvent.getLocalState();

                    ViewGroup owner = (ViewGroup) ivToBeAdded.getParent();
                    owner.removeView(ivToBeAdded);

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
                    Log.e(TAG, "Unknown action type received by CupCaddyDragListener.");
                    break;
            }

            return false;
        }
    }

    private class CaddyToMastrenaTouchListener
            implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Create a new ClipData. This is done in two steps to provide clarity. The
                // convenience method ClipData.newPlainText() can create a plain text
                // ClipData in one step.

                String label = DRAG_LABEL;
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                // Create a new ClipData.Item from the ImageView object's tag.
                ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

                // Create a new ClipData using the tag as a label, the plain text MIME type,
                // and the already-created item. This creates a new ClipDescription object
                // within the ClipData and sets its MIME type to "text/plain".
                ClipData dragData = new ClipData(
                        label,
                        mimeTypes,
                        item
                );
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

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
    }
}