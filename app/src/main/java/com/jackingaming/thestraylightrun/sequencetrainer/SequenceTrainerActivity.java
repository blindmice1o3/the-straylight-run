package com.jackingaming.thestraylightrun.sequencetrainer;

import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class SequenceTrainerActivity extends AppCompatActivity
        implements LabelPrinterFragment.Listener {
    public static final String TAG = SequenceTrainerActivity.class.getSimpleName();

    public static final int SHAKE_DETECTION_THRESHOLD = 50;
    public static float yPrevious, yPeak, yTrough = -1f;
    public static boolean shakeUpward, metThreshold = false;
    public static int shakeCounter = 0;

    private ConstraintLayout constraintLayout;
    private String modeSelected = "standard";

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

                    // TODO: re-work IceShaker's shaking logic.
                    if (label.equals(IceShaker.DRAG_LABEL)) {
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        int yNow = (int) (location[1] + dragEvent.getY());
                        Log.e(TAG, "yNow:" + yNow);

                        // starting condition
                        if (yPeak == -1 && yTrough == -1) {
                            yPrevious = yNow;
                            yPeak = yNow;
                            yTrough = yNow;
                            Log.e(TAG, "yPrevious: " + yPrevious);
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
                            Toast.makeText(SequenceTrainerActivity.this, "SHAKEN", Toast.LENGTH_SHORT).show();
                            IceShaker iceShaker = (IceShaker) dragEvent.getLocalState();
                            iceShaker.shake();
                        }
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
                        shakeUpward = false;
                        metThreshold = false;
                        shakeCounter = 0;
                        yPeak = -1;
                        yTrough = -1;

                        IceShaker iceShaker = (IceShaker) dragEvent.getLocalState();
                        iceShaker.setVisibility(View.VISIBLE);

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        setContentView(R.layout.activity_sequence_trainer);

        constraintLayout = findViewById(R.id.constraintlayout_sequence_trainer);
        constraintLayout.setOnDragListener(new IceShakerDragListener());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fcv_main, MastrenaFragment.newInstance());
        ft.add(R.id.fcv_right_top, LabelPrinterFragment.newInstance());
        ft.add(R.id.fcv_right_middle, CupCaddyFragment.newInstance());
        ft.add(R.id.fcv_right_bottom, SinkFragment.newInstance("", ""));
        ft.add(R.id.fcv_bottom_left, RefrigeratorFragment.newInstance());
        ft.add(R.id.fcv_bottom_right, IceBinFragment.newInstance("", ""));
        ft.commit();

        // Set the listener on the fragmentManager.
        getSupportFragmentManager().setFragmentResultListener(LabelPrinterModeDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                modeSelected = result.getString(LabelPrinterModeDialogFragment.BUNDLE_KEY_MODE_SELECTED);
                Log.e(TAG, "modeSelected: " + modeSelected);

                LabelPrinterFragment labelPrinterFragment = (LabelPrinterFragment) getSupportFragmentManager().findFragmentById(R.id.fcv_right_top);
                labelPrinterFragment.changeLabelPrinterMode(modeSelected);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_activity_sequence_trainer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_item_label_printer_mode:
                LabelPrinterModeDialogFragment labelPrinterModeDialogFragment = LabelPrinterModeDialogFragment.newInstance(modeSelected);
                labelPrinterModeDialogFragment.show(getSupportFragmentManager(), LabelPrinterModeDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInitializationCompleted(LabelPrinter labelPrinter) {
        MastrenaFragment mastrenaFragment = (MastrenaFragment) getSupportFragmentManager().findFragmentById(R.id.fcv_main);
        mastrenaFragment.setLabelPrinter(labelPrinter);
    }
}