package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.entities.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.EspressoShotControlDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.FillSteamingPitcherDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CaramelDrizzleBottle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SteamingWand;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.Syrup;

public class MaestranaFragment extends Fragment {
    public static final String TAG = MaestranaFragment.class.getSimpleName();

    public static final String TAG_STEAMING_PITCHER = "SteamingPitcher";
    public static final String TAG_STEAMING_WAND = "SteamingWand";
    public static final String TAG_ESPRESSO_SHOT_CONTROL = "EspressoShotControl";
    public static final String TAG_ESPRESSO_SHOT = "EspressoShot";
    public static final String TAG_SHOT_GLASS = "ShotGlass";
    public static final String TAG_SYRUP_BOTTLE_VANILLA = "SyrupBottleVanilla";
    public static final String TAG_SYRUP_VANILLA = "SyrupVanilla";
    public static final String TAG_CARAMEL_DRIZZLE_BOTTLE = "CaramelDrizzleBottle";

    private MaestranaViewModel mViewModel;
    private ConstraintLayout constraintLayoutMaestrana;
    private FrameLayout framelayoutLabelStagingArea;
    private FrameLayout framelayoutEspressoStream, framelayoutSyrupCaddy, framelayoutSteamingWand;
    private FrameLayout framelayoutLeft, framelayoutCenter, framelayoutRight;
    private SteamingPitcher steamingPitcher;
    private SteamingWand steamingWand;
    private ImageView espressoShotControl;
    private EspressoShot espressoShot;
    private ObjectAnimator animatorEspressoShot;
    private ShotGlass shotGlass;
    private ImageView syrupBottleVanilla;
    private CaramelDrizzleBottle caramelDrizzleBottle;

    private CupImageView ivToBeAdded;
    private float xTouch, yTouch;

    public static MaestranaFragment newInstance() {
        Log.e(TAG, "newInstance()");
        return new MaestranaFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        // Set the listener on the child fragmentManager.
        getChildFragmentManager()
                .setFragmentResultListener(FillSteamingPitcherDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                        String content = result.getString(FillSteamingPitcherDialogFragment.BUNDLE_KEY_CONTENT);
                        int amount = result.getInt(FillSteamingPitcherDialogFragment.BUNDLE_KEY_AMOUNT);

                        Log.e(TAG, content + ": " + amount);
                        steamingPitcher.setTemperature(37);
                        steamingPitcher.update(content, amount);
                    }
                });
        getChildFragmentManager()
                .setFragmentResultListener(EspressoShotControlDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
                    private static final int REPEAT_COUNT_SINGLE = 0;
                    private static final int REPEAT_COUNT_DOUBLE = 1;
                    private static final int REPEAT_COUNT_TRIPLE = 2;

                    @RequiresApi(api = 33)
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                        if (animatorEspressoShot != null && animatorEspressoShot.isRunning()) {
                            Log.e(TAG, "animatorEspressoShot.isRunning() RETURN");
                            return;
                        }

                        EspressoShot.Type typeSelected = (EspressoShot.Type) result.getSerializable(EspressoShotControlDialogFragment.BUNDLE_KEY_TYPE);
                        int quantitySelected = result.getInt(EspressoShotControlDialogFragment.BUNDLE_KEY_QUANTITY);
                        Log.e(TAG, "type: " + typeSelected.name() + ", quantity: " + quantitySelected);

                        if (quantitySelected < 1) {
                            Log.e(TAG, "quantitySelected < 1 returning.");
                            return;
                        }

                        espressoShot.updateType(typeSelected);

                        long startDelay = 1500L * quantitySelected;
                        long duration = 1500L;
                        int repeatCountToUse = -1;
                        if (quantitySelected == 1) {
                            repeatCountToUse = REPEAT_COUNT_SINGLE;
                        } else if (quantitySelected == 2) {
                            repeatCountToUse = REPEAT_COUNT_DOUBLE;
                        } else if (quantitySelected == 3) {
                            repeatCountToUse = REPEAT_COUNT_TRIPLE;
                        } else {
                            Log.e(TAG, "quantitySelected != 1 or 2 or 3.");
                        }

                        animatorEspressoShot = ObjectAnimator.ofFloat(
                                espressoShot,
                                "y",
                                458f,
                                1200f);
                        animatorEspressoShot.setStartDelay(startDelay);
                        animatorEspressoShot.setDuration(duration);
                        animatorEspressoShot.setRepeatCount(repeatCountToUse);
                        animatorEspressoShot.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationRepeat(Animator animation) {
                                super.onAnimationRepeat(animation);
                                Log.e(TAG, "onAnimationRepeat()");

                                espressoShot.setCollided(false);
                                espressoShot.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Log.e(TAG, "onAnimationEnd()");

                                espressoShot.setY(458);
                                espressoShot.setBackgroundColor(getResources().getColor(R.color.brown));
                                espressoShot.setCollided(false);
                                espressoShot.setVisibility(View.VISIBLE);
                            }
                        });
                        animatorEspressoShot.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                                for (int i = 0; i < framelayoutLeft.getChildCount(); i++) {
                                    if (espressoShot.isCollided()) {
                                        break;
                                    }

                                    if (framelayoutLeft.getChildAt(i) instanceof CupImageView) {
                                        CupImageView ivCup = (CupImageView) framelayoutLeft.getChildAt(i);

                                        boolean colliding = isViewOverlapping(espressoShot, ivCup);
                                        ivCup.update(colliding);

                                        if (ivCup.isJustCollided()) {
                                            Log.e(TAG, "ivCup.isJustCollided()");

                                            ivCup.onCollided(espressoShot);
                                            espressoShot.setCollided(true);
                                            espressoShot.setVisibility(View.INVISIBLE);
                                        }
                                    } else if (framelayoutLeft.getChildAt(i) instanceof ShotGlass) {
                                        ShotGlass myShotGlass = (ShotGlass) framelayoutLeft.getChildAt(i);

                                        boolean colliding = isViewOverlapping(espressoShot, myShotGlass);
                                        myShotGlass.update(colliding);

                                        if (myShotGlass.isJustCollided()) {
                                            Log.e(TAG, "myShotGlass.isJustCollided()");

                                            myShotGlass.onCollided(espressoShot);
                                            espressoShot.setCollided(true);
                                            espressoShot.setVisibility(View.INVISIBLE);
                                        }
                                    } else {
                                        Log.e(TAG, "onAnimationUpdate() else-clause.");
                                    }
                                }
                            }
                        });

                        animatorEspressoShot.start();
                    }
                });
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
        framelayoutLabelStagingArea = view.findViewById(R.id.framelayout_label_staging_area);
        framelayoutEspressoStream = view.findViewById(R.id.framelayout_espresso_stream);
        framelayoutSyrupCaddy = view.findViewById(R.id.framelayout_syrup_caddy);
        framelayoutSteamingWand = view.findViewById(R.id.framelayout_steaming_wand);
        framelayoutLeft = view.findViewById(R.id.framelayout_left);
        framelayoutCenter = view.findViewById(R.id.framelayout_center);
        framelayoutRight = view.findViewById(R.id.framelayout_right);

        framelayoutLabelStagingArea.setOnDragListener(new LabelStagingAreaDragListener());

        View.OnDragListener maestranaDragListener = new MaestranaDragListener();
        framelayoutLeft.setOnDragListener(maestranaDragListener);
        framelayoutCenter.setOnDragListener(maestranaDragListener);
        framelayoutRight.setOnDragListener(maestranaDragListener);

        // STEAMING PITCHER
        steamingPitcher = new SteamingPitcher(getContext());
        steamingPitcher.setTag(TAG_STEAMING_PITCHER);
        steamingPitcher.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        steamingPitcher.setX(30);
        steamingPitcher.setY(50);
        steamingPitcher.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
        steamingPitcher.setListener(new SteamingPitcher.SteamingPitcherListener() {
            @Override
            public void showDialogFillSteamingPitcher(String contentToBeSteamed, int amount) {
                FillSteamingPitcherDialogFragment dialogFragment =
                        FillSteamingPitcherDialogFragment.newInstance(contentToBeSteamed, amount);
                dialogFragment.show(getChildFragmentManager(), FillSteamingPitcherDialogFragment.TAG);
            }
        });
        framelayoutRight.addView(steamingPitcher);

        // STEAMING WAND
        steamingWand = new SteamingWand(getContext());
        steamingWand.setTag(TAG_STEAMING_WAND);
        steamingWand.setLayoutParams(new FrameLayout.LayoutParams(32, 400));
        steamingWand.setX(128);
        steamingWand.setBackgroundColor(getResources().getColor(R.color.purple_700));
        framelayoutSteamingWand.addView(steamingWand);

        // ESPRESSO SHOT CONTROL
        espressoShotControl = new ImageView(getContext());
        espressoShotControl.setTag(TAG_ESPRESSO_SHOT_CONTROL);
        espressoShotControl.setLayoutParams(new FrameLayout.LayoutParams(128, 128));
        espressoShotControl.setX(200 - (128 / 2));
        espressoShotControl.setBackgroundColor(getResources().getColor(R.color.brown));
        espressoShotControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EspressoShotControlDialogFragment dialogFragment = new EspressoShotControlDialogFragment();
                dialogFragment.show(getChildFragmentManager(), EspressoShotControlDialogFragment.TAG);
            }
        });
        framelayoutEspressoStream.addView(espressoShotControl);

        // ESPRESSO SHOT
        espressoShot = new EspressoShot(getContext());
        espressoShot.setTag(TAG_ESPRESSO_SHOT);
        espressoShot.setLayoutParams(new FrameLayout.LayoutParams(16, 64));
        espressoShot.setX(200 - (16 / 2));
        espressoShot.setY(458);
        espressoShot.setBackgroundColor(getResources().getColor(R.color.brown));
        constraintLayoutMaestrana.addView(espressoShot);

        // SHOT GLASS
        shotGlass = new ShotGlass(getContext());
        shotGlass.setTag(TAG_SHOT_GLASS);
        shotGlass.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        shotGlass.setX(200 - (64 / 2));
        shotGlass.setBackgroundColor(getResources().getColor(R.color.cream));
        framelayoutLeft.addView(shotGlass);

        // SYRUP BOTTLE (vanilla)
        syrupBottleVanilla = new ImageView(getContext());
        syrupBottleVanilla.setTag(TAG_SYRUP_BOTTLE_VANILLA);
        syrupBottleVanilla.setLayoutParams(new FrameLayout.LayoutParams(64, 128));
        syrupBottleVanilla.setX(64 - (64 / 2));
        syrupBottleVanilla.setBackgroundColor(getResources().getColor(R.color.cream));
        syrupBottleVanilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "syrupBottleVanilla.onClick()");

                Syrup syrupVanilla = new Syrup(getContext());
                syrupVanilla.setTag(TAG_SYRUP_VANILLA);
                syrupVanilla.setLayoutParams(new FrameLayout.LayoutParams(16, 64));
                syrupVanilla.setX(344);
                syrupVanilla.setY(458);
                syrupVanilla.setBackgroundColor(getResources().getColor(R.color.cream));
                constraintLayoutMaestrana.addView(syrupVanilla);

                ObjectAnimator animatorSyrup = ObjectAnimator.ofFloat(
                        syrupVanilla,
                        "y",
                        458f,
                        1200f);
                animatorSyrup.setDuration(2000);
                animatorSyrup.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.e(TAG, "onAnimationEnd()");

                        if (syrupVanilla != null) {
                            constraintLayoutMaestrana.removeView(syrupVanilla);
                        }
                    }
                });
                animatorSyrup.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        for (int i = 0; i < framelayoutCenter.getChildCount(); i++) {
                            if (framelayoutCenter.getChildAt(i) instanceof CupImageView) {
                                CupImageView ivCup = (CupImageView) framelayoutCenter.getChildAt(i);

                                boolean colliding = isViewOverlapping(syrupVanilla, ivCup);
                                ivCup.update(colliding);

                                if (ivCup.isJustCollided()) {
                                    Log.e(TAG, "ivCup.isJustCollided()");

                                    ivCup.onCollided(syrupVanilla);
                                    constraintLayoutMaestrana.removeView(syrupVanilla);
                                    return;
                                }
                            } else {
                                Log.e(TAG, "onAnimationUpdate() else-clause.");
                            }
                        }
                    }
                });
                animatorSyrup.start();
            }
        });
        framelayoutSyrupCaddy.addView(syrupBottleVanilla);

        // CARAMEL DRIZZLE BOTTLE
        caramelDrizzleBottle = new CaramelDrizzleBottle(getContext());
        caramelDrizzleBottle.setTag(TAG_CARAMEL_DRIZZLE_BOTTLE);
        caramelDrizzleBottle.setLayoutParams(new FrameLayout.LayoutParams(48, 96));
        caramelDrizzleBottle.setX(384 - (48 + 16));
        caramelDrizzleBottle.setY(452 - (96 + 16));
        caramelDrizzleBottle.setBackgroundColor(getResources().getColor(R.color.yellow));
        framelayoutSyrupCaddy.addView(caramelDrizzleBottle);
    }

    private boolean isViewOverlapping(View firstView, View secondView) {
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

//        firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        firstView.getLocationOnScreen(firstPosition);
//        secondView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        secondView.getLocationOnScreen(secondPosition);

        return firstPosition[0] < secondPosition[0] + secondView.getWidth()
                && firstPosition[0] + firstView.getWidth() > secondPosition[0]
                && firstPosition[1] < secondPosition[1] + secondView.getHeight()
                && firstPosition[1] + firstView.getHeight() > secondPosition[1];
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated()");

        mViewModel = new ViewModelProvider(this).get(MaestranaViewModel.class);
        // TODO: Use the ViewModel
    }

    private class LabelStagingAreaDragListener
            implements View.OnDragListener {
        int resIdNormal = R.drawable.shape_maestrana;
        int resIdDropTarget = R.drawable.shape_droptarget;

        private String label;
        private LabelPrinter labelPrinter;
        private DrinkLabel drinkLabel;

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        label = dragEvent.getClipDescription().getLabel().toString();

                        if (label.equals("LabelPrinter") ||
                                label.equals("DrinkLabel")) {
                            Log.d(TAG, "label.equals(\"LabelPrinter\") || label.equals(\"DrinkLabel\")");

                            if (label.equals("LabelPrinter")) {
                                labelPrinter = (LabelPrinter) dragEvent.getLocalState();
                            } else if (label.equals("DrinkLabel")) {
                                drinkLabel = (DrinkLabel) dragEvent.getLocalState();
                            }

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
                    Log.d(TAG, "ACTION_DROP !!! LABEL PRINTER !!!");

                    xTouch = dragEvent.getX();
                    yTouch = dragEvent.getY();

                    if (label.equals("LabelPrinter")) {
                        // Instantiate DrinkLabel.
                        DrinkLabel drinkLabelNew = new DrinkLabel(getContext());
                        FrameLayout.LayoutParams layoutParams =
                                new FrameLayout.LayoutParams(labelPrinter.getWidth(), labelPrinter.getHeight());
                        drinkLabelNew.setLayoutParams(layoutParams);
                        drinkLabelNew.setBackgroundColor(getResources().getColor(R.color.purple_200));

                        // Get the item containing the dragged data.
                        ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                        // Get the text data from the item.
                        String dragData = item.getText().toString();

                        // Display a message containing the dragged data.
                        Toast.makeText(getContext(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        drinkLabelNew.setTag(dragData);
                        drinkLabelNew.setText(dragData);

                        drinkLabelNew.setX(xTouch - (labelPrinter.getWidth() / 2));
                        drinkLabelNew.setY(yTouch - (labelPrinter.getHeight() / 2));

                        // Add Textview to FrameLayout.
                        ((FrameLayout) view).addView(drinkLabelNew);

                        //////////////////////////////////////////////////////////////////////////

                        labelPrinter.generateRandomDrinkRequest();

                        labelPrinter.setVisibility(View.VISIBLE);
                        labelPrinter = null;
                    } else if (label.equals("DrinkLabel")) {
                        drinkLabel.setX(xTouch - (drinkLabel.getWidth() / 2));
                        drinkLabel.setY(yTouch - (drinkLabel.getHeight() / 2));

                        drinkLabel.setVisibility(View.VISIBLE);
                        drinkLabel = null;
                    }

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
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();

                        if (label.equals("LabelPrinter")) {
                            labelPrinter.setVisibility(View.VISIBLE);
                            labelPrinter = null;
                        } else if (label.equals("DrinkLabel")) {
                            drinkLabel.setVisibility(View.VISIBLE);
                            drinkLabel = null;
                        }
                    }

                    // TODO: override CupImageView.onDrag() to handle drop from DrinkLabel,
                    //  compare drink label and cup content, display winning dialog if same.

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by LabelPrinterDragListener.");
                    break;
            }

            return false;
        }
    }

    private String label;

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

                        label = dragEvent.getClipDescription().getLabel().toString();
                        if (label.equals("CaddyToMaestrana") ||
                                label.equals("MaestranaToCaddy") ||
                                label.equals("ShotGlass")) {
                            Log.d(TAG, "label.equals(\"CaddyToMaestrana\") || label.equals(\"MaestranaToCaddy\") || label.equals(\"ShotGlass\")");

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

                    if (label.equals("MaestranaToCaddy")) {
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

                    xTouch = dragEvent.getX();
                    yTouch = dragEvent.getY();

                    if (label.equals("CaddyToMaestrana") || label.equals("MaestranaToCaddy")) {
                        if (label.equals("CaddyToMaestrana")) {
                            Log.d(TAG, "ACTION_DROP label.equals(\"CaddyToMaestrana\")");
                            Log.d(TAG, "ACTION_DROP Instantiate ImageView for ivToBeAdded");

                            // Instantiate CupImageView.
                            ivToBeAdded = new CupImageView(getContext());
                            FrameLayout.LayoutParams layoutParams =
                                    new FrameLayout.LayoutParams(64, 64);
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
                                Log.e(TAG, "else-clause (generating ImageView to add to LinearLayout).");
                            }
                            ivToBeAdded.setBackgroundResource(resId);
                            ivToBeAdded.setTag(tag);

                            ivToBeAdded.setX(xTouch - (64 / 2));
                            ivToBeAdded.setY(yTouch - (64 / 2));
                        } else if (label.equals("MaestranaToCaddy")) {
                            Log.d(TAG, "ACTION_DROP label.equals(\"MaestranaToCaddy\")");
                            Log.d(TAG, "ACTION_DROP Derive ivToBeAdded from dragData");
                            ivToBeAdded = (CupImageView) dragEvent.getLocalState();

                            ViewGroup owner = (ViewGroup) ivToBeAdded.getParent();
                            owner.removeView(ivToBeAdded);

                            ivToBeAdded.setX(xTouch - (ivToBeAdded.getWidth() / 2));
                            ivToBeAdded.setY(yTouch - (ivToBeAdded.getHeight() / 2));
                        }

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(ivToBeAdded);
                    } else if (label.equals("ShotGlass")) {
                        Log.d(TAG, "ACTION_DROP label.equals(\"ShotGlass\")");
                        Log.d(TAG, "ACTION_DROP Derive shotGlass from dragData");
                        shotGlass = (ShotGlass) dragEvent.getLocalState();

                        ViewGroup owner = (ViewGroup) shotGlass.getParent();
                        owner.removeView(shotGlass);

                        shotGlass.setX(xTouch - (shotGlass.getWidth() / 2));
                        shotGlass.setY(yTouch - (shotGlass.getHeight() / 2));

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(shotGlass);
                    }

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED MaestranaFragment");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);
                    // Reset the background drawable to normal.
                    view.setBackgroundResource(resIdNormal);

                    if (label == null) {
                        Log.e(TAG, "label is null.");
                        return true;
                    }

                    Log.e(TAG, "label: " + label);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }

                    if (label.equals("CaddyToMaestrana") || label.equals("MaestranaToCaddy")) {
                        if (ivToBeAdded != null) {
                            ivToBeAdded.setVisibility(View.VISIBLE);

                            ivToBeAdded = null;
                        }
                    } else if (label.equals("ShotGlass")) {
                        if (shotGlass != null) {
                            shotGlass.setVisibility(View.VISIBLE);
                        }
                    }

                    xTouch = -1f;
                    yTouch = -1f;

                    Log.e(TAG, "setting label to null.");
                    label = null;

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by MaestranaDragListener.");
                    break;
            }

            return false;
        }
    }
}