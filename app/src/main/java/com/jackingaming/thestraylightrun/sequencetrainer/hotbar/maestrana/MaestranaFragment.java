package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jackingaming.thestraylightrun.R;

public class MaestranaFragment extends Fragment {
    public static final String TAG_DEBUG = MaestranaFragment.class.getSimpleName();

    private MaestranaViewModel mViewModel;
    private FrameLayout framelayoutLeft, framelayoutCenter, framelayoutRight;

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

        framelayoutLeft = view.findViewById(R.id.framelayout_left);
        framelayoutCenter = view.findViewById(R.id.framelayout_center);
        framelayoutRight = view.findViewById(R.id.framelayout_right);

        View.OnDragListener myDragListener = new MyDragListener();
        framelayoutLeft.setOnDragListener(myDragListener);
        framelayoutRight.setOnDragListener(myDragListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG_DEBUG, "onActivityCreated()");

        mViewModel = new ViewModelProvider(this).get(MaestranaViewModel.class);
        // TODO: Use the ViewModel
    }

    private class MyDragListener
            implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // do nothing

                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    }

                    // Return false to indicate that, during the current drag and drop
                    // operation, this View doesn't receive events again until
                    // ACTION_DRAG_ENDED is sent.
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // Change background drawable to indicate drop-target.
                    view.setBackgroundResource(R.drawable.shape_droptarget);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // Ignore the event.
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    // Reset the background drawable to normal.
                    view.setBackgroundResource(R.drawable.shape);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DROP:
                    // TODO:
                    // Get the item containing the dragged data.
                    ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                    // Get the text data from the item.
                    CharSequence dragData = item.getText();

                    // Instantiate ImageView and set background resource according to dragData.
                    ivToBeAdded = new ImageView(getContext());
                    int resId = -1;
                    if (dragData.toString().equals("trenta")) {
                        resId = R.drawable.drinksize_trenta;
                    } else if (dragData.toString().equals("venti")) {
                        resId = R.drawable.drinksize_venti;
                    } else if (dragData.toString().equals("grande")) {
                        resId = R.drawable.drinksize_grande;
                    } else if (dragData.toString().equals("tall")) {
                        resId = R.drawable.drinksize_tall;
                    } else if (dragData.toString().equals("short")) {
                        resId = R.drawable.drinksize_short;
                    } else {
                        Log.e(TAG_DEBUG, "else-clause (generating ImageView to add to LinearLayout).");
                    }
                    ivToBeAdded.setBackgroundResource(resId);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    ivToBeAdded.setLayoutParams(layoutParams);

                    // Add ImageView to FrameLayout.
                    ((FrameLayout) view).addView(ivToBeAdded);
                    ivToBeAdded.setVisibility(View.INVISIBLE);

                    xTouch = dragEvent.getX();
                    yTouch = dragEvent.getY();

                    // Display a message containing the dragged data.
                    Toast.makeText(getContext(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                    // Reset the background drawable to normal.
                    view.setBackgroundResource(R.drawable.shape);

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Reset the background drawable to normal.
                    view.setBackgroundResource(R.drawable.shape);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();

                        if (ivToBeAdded != null) {
                            ivToBeAdded.setX(xTouch - (ivToBeAdded.getWidth() / 2));
                            ivToBeAdded.setY(yTouch - (ivToBeAdded.getHeight() / 2));
                            ivToBeAdded.setVisibility(View.VISIBLE);

                            ivToBeAdded = null;
                            xTouch = -1f;
                            yTouch = -1f;
                        }
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG_DEBUG, "Unknown action type received by View.OnDragListener.");
                    break;
            }

            return false;
        }
    }
}