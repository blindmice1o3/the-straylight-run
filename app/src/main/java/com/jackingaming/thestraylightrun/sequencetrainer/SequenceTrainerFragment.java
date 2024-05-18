package com.jackingaming.thestraylightrun.sequencetrainer;

import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupCaddyFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.icebin.IceBinFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.LabelPrinterFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.MastrenaFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.IceShaker;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.sink.SinkFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SequenceTrainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SequenceTrainerFragment extends Fragment
        implements MastrenaFragment.IceShakerListener {
    public static final String TAG = SequenceTrainerFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout constraintLayout;
    private String modeSelected = "standard";

    private static final int SHAKE_DETECTION_THRESHOLD = 50;
    private float yPrevious, yPeak, yTrough = -1f;
    private boolean shakeUpward, metThreshold = false;
    private int shakeCounter = 0;

    private class IceShakerDragListener
            implements View.OnDragListener {

        private String label;

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        label = dragEvent.getClipDescription().getLabel().toString();
                        if (label.equals(IceShaker.DRAG_LABEL)) {
                            Log.d(TAG, "label.equals(" + IceShaker.DRAG_LABEL + ")");

                            // Change background drawable to indicate drop-target.
//                            view.setBackgroundResource(resIdDropTarget);
                            // NOT NEEDED.

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
//                    view.setAlpha(0.5f);
                    // NOT NEEDED.

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
//                    Log.d(TAG, "ACTION_DRAG_LOCATION");

                    if (label.equals(IceShaker.DRAG_LABEL)) {
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        float yNow = ((float) (location[1])) + dragEvent.getY();
                        Log.e(TAG, "yNow:" + yNow);
                        IceShaker iceShaker = (IceShaker) dragEvent.getLocalState();

                        handleShake(yNow, iceShaker);
                    }

                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "ACTION_DRAG_EXITED");

                    // Reset value of alpha back to normal.
//                    view.setAlpha(1.0f);
                    // NOT NEEDED.

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "ACTION_DROP");

                    // Return FALSE. DragEvent.getResult() returns false.
                    return false;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED IceShakerDragListener");

                    // Reset value of alpha back to normal.
//                    view.setAlpha(1.0f);
                    // NOT NEEDED.
                    // Reset the background drawable to normal.
//                    view.setBackgroundResource(resIdNormal);
                    // NOT NEEDED.

                    if (label.equals(IceShaker.DRAG_LABEL)) {
                        IceShaker iceShaker = (IceShaker) dragEvent.getLocalState();

                        handleShakeTearDown(iceShaker);

                        Log.e(TAG, "setting label to null.");
                        label = null;
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by IceShakerDragListener.");
                    break;
            }

            return false;
        }
    }

    @Override
    public void handleShake(float yNow, IceShaker iceShaker) {
        // starting condition
        if (yPeak == -1 && yTrough == -1) {
            yPrevious = yNow;
            yPeak = yNow;
            yTrough = yNow;
        }
        // non-starting condition
        else {
            // was moving upward
            if (shakeUpward) {
                // still moving upward
                if (yPrevious - yNow >= 0) {
                    // pass up-threshold
                    if (yTrough - yNow > SHAKE_DETECTION_THRESHOLD) {
                        // TODO:
                        Log.e(TAG, "up-threshold met");
                        metThreshold = true;
                        yPeak = yPrevious;
                    }
                    // not pass threshold
                    else {
                        // INTENTIONALLY BLANK.
                    }
                }
                // change-to moving downward
                else {
                    shakeUpward = false;
                    yPeak = yPrevious;
                }

                yPrevious = yNow;
            }
            // was moving downward
            else {
                // still moving downward
                if (yPrevious - yNow < 0) {
                    // pass up-AND-down-thresholds
                    if (yPeak - yNow < -SHAKE_DETECTION_THRESHOLD &&
                            metThreshold) {
                        Log.e(TAG, "down-threshold met");
                        ///////////////
                        shakeCounter++;
                        metThreshold = false;
                        yTrough = yPrevious;
                        ///////////////
                    }
                    // not pass threshold
                    else {
                        // INTENTIONALLY BLANK.
                    }
                }
                // change-to moving upward
                else {
                    shakeUpward = true;
                    yTrough = yPrevious;
                }

                yPrevious = yNow;
            }
        }

        if (shakeCounter == 5) {
            Toast.makeText(getContext(), "SHAKEN", Toast.LENGTH_SHORT).show();
            iceShaker.shake();
        }
    }

    @Override
    public void handleShakeTearDown(IceShaker iceShaker) {
        shakeUpward = false;
        metThreshold = false;
        shakeCounter = 0;
        yPeak = -1;
        yTrough = -1;

        iceShaker.setVisibility(View.VISIBLE);
    }

    public SequenceTrainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SequenceTrainerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SequenceTrainerFragment newInstance(String param1, String param2) {
        SequenceTrainerFragment fragment = new SequenceTrainerFragment();
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
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sequence_trainer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MastrenaFragment mastrenaFragment = MastrenaFragment.newInstance();
        mastrenaFragment.setIceShakerListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fcv_main, mastrenaFragment);
        ft.add(R.id.fcv_right_top, LabelPrinterFragment.newInstance(new LabelPrinterFragment.Listener() {
            @Override
            public void onInitializationCompleted(LabelPrinter labelPrinter) {
                MastrenaFragment mastrenaFragment = (MastrenaFragment) getChildFragmentManager().findFragmentById(R.id.fcv_main);
                mastrenaFragment.setLabelPrinter(labelPrinter);
            }
        }));
        ft.add(R.id.fcv_right_middle, CupCaddyFragment.newInstance());
        ft.add(R.id.fcv_right_bottom, SinkFragment.newInstance("", ""));
        ft.add(R.id.fcv_bottom_left, RefrigeratorFragment.newInstance());
        ft.add(R.id.fcv_bottom_right, IceBinFragment.newInstance("", ""));
        ft.commit();

        // Set the listener on the fragmentManager.
        getChildFragmentManager().setFragmentResultListener(LabelPrinterModeDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                modeSelected = result.getString(LabelPrinterModeDialogFragment.BUNDLE_KEY_MODE_SELECTED);
                Log.e(TAG, "modeSelected: " + modeSelected);

                LabelPrinterFragment labelPrinterFragment = (LabelPrinterFragment) getChildFragmentManager().findFragmentById(R.id.fcv_right_top);
                labelPrinterFragment.changeLabelPrinterMode(modeSelected);
            }
        });

        constraintLayout = view.findViewById(R.id.constraintlayout_sequence_trainer);
        constraintLayout.setOnDragListener(new IceShakerDragListener());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Add your menu entries here.
        inflater.inflate(R.menu.options_activity_sequence_trainer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.options_item_label_printer_mode) {
                menu.getItem(i).setVisible(true);
            } else {
                menu.getItem(i).setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_item_label_printer_mode:
                Log.e(TAG, "onOptionsItemSelected() R.id.options_item_label_printer_mode YAY!");
                LabelPrinterModeDialogFragment labelPrinterModeDialogFragment = LabelPrinterModeDialogFragment.newInstance(modeSelected);
                labelPrinterModeDialogFragment.show(getChildFragmentManager(), LabelPrinterModeDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}