package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana;

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
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.dialogfragments.EspressoShotControlDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.dialogfragments.FillSteamingPitcherDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities.SteamingWand;

public class MaestranaFragment extends Fragment {
    public static final String TAG = MaestranaFragment.class.getSimpleName();

    public static final String TAG_STEAMING_PITCHER = "SteamingPitcher";
    public static final String TAG_STEAMING_WAND = "SteamingWand";
    public static final String TAG_ESPRESSO_SHOT_CONTROL = "EspressoShotControl";
    public static final String TAG_ESPRESSO_SHOT = "EspressoShot";
    public static final String TAG_SHOT_GLASS = "ShotGlass";

    private MaestranaViewModel mViewModel;
    private ConstraintLayout constraintLayoutMaestrana;
    private FrameLayout framelayoutEspressoStreamAndSteamWand;
    private FrameLayout framelayoutLeft, framelayoutCenter, framelayoutRight;
    private SteamingPitcher steamingPitcher;
    private SteamingWand steamingWand;
    private EspressoShot espressoShot;
    private ShotGlass shotGlass;
    private ObjectAnimator animatorEspressoStream;

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

                        EspressoShot.Type typeSelected = (EspressoShot.Type) result.getSerializable(EspressoShotControlDialogFragment.BUNDLE_KEY_TYPE);
                        int quantitySelected = result.getInt(EspressoShotControlDialogFragment.BUNDLE_KEY_QUANTITY);
                        Log.e(TAG, "type: " + typeSelected.name() + ", quantity: " + quantitySelected);

                        if (quantitySelected < 1) {
                            Log.e(TAG, "quantitySelected < 1 returning.");
                            return;
                        }

                        espressoShot.updateType(typeSelected);

                        int repeatCountToUse = -1;
                        if (quantitySelected == 1) {
                            repeatCountToUse = REPEAT_COUNT_SINGLE;
                        } else if (quantitySelected == 2) {
                            repeatCountToUse = REPEAT_COUNT_DOUBLE;
                        } else if (quantitySelected == 3) {
                            repeatCountToUse = REPEAT_COUNT_TRIPLE;
                        } else {
                            Log.e(TAG, "repeatCountToUse != 1 or 2 or 3.");
                        }
                        animatorEspressoStream = ObjectAnimator.ofFloat(
                                espressoShot,
                                "y",
                                0f,
                                800f);
                        animatorEspressoStream.setDuration(4000);
                        animatorEspressoStream.setRepeatCount(repeatCountToUse);
//        animatorEspressoStream.setRepeatCount(ObjectAnimator.INFINITE);
//        animatorEspressoStream.setRepeatMode(ObjectAnimator.REVERSE);
                        animatorEspressoStream.setInterpolator(new AccelerateDecelerateInterpolator());
                        animatorEspressoStream.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Log.e(TAG, "onAnimationEnd()");
                                // TODO:
                                espressoShot.setY(0);
                                espressoShot.setBackgroundColor(getResources().getColor(R.color.brown));
                            }
                        });
                        animatorEspressoStream.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                                for (int i = 0; i < framelayoutLeft.getChildCount(); i++) {
                                    if (framelayoutLeft.getChildAt(i) instanceof CupImageView) {
                                        CupImageView ivCup = (CupImageView) framelayoutLeft.getChildAt(i);

                                        boolean colliding = isViewOverlapping(espressoShot, ivCup);
                                        ivCup.update(colliding);

                                        if (ivCup.isJustCollided()) {
                                            Log.e(TAG, "ivCup.isJustCollided()");

                                            ivCup.onCollided(espressoShot);
                                        }
                                    } else if (framelayoutLeft.getChildAt(i) instanceof ShotGlass) {
                                        ShotGlass myShotGlass = (ShotGlass) framelayoutLeft.getChildAt(i);

                                        boolean colliding = isViewOverlapping(espressoShot, myShotGlass);
                                        myShotGlass.update(colliding);

                                        if (myShotGlass.isJustCollided()) {
                                            Log.e(TAG, "myShotGlass.isJustCollided()");

                                            myShotGlass.onCollided(espressoShot);
                                        }
                                    } else {
                                        Log.e(TAG, "onAnimationUpdate() else-clause.");
                                    }
                                }
                            }
                        });
                        animatorEspressoStream.start();
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
        framelayoutEspressoStreamAndSteamWand = view.findViewById(R.id.framelayout_espresso_stream_and_steam_wand);
        framelayoutLeft = view.findViewById(R.id.framelayout_left);
        framelayoutCenter = view.findViewById(R.id.framelayout_center);
        framelayoutRight = view.findViewById(R.id.framelayout_right);

        View.OnDragListener maestranaDragListener = new MaestranaDragListener();
        framelayoutLeft.setOnDragListener(maestranaDragListener);
        framelayoutRight.setOnDragListener(maestranaDragListener);

        // STEAMING PITCHER
        steamingPitcher = new SteamingPitcher(getContext());
        steamingPitcher.setTag(TAG_STEAMING_PITCHER);
        steamingPitcher.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        steamingPitcher.setX(30);
        steamingPitcher.setY(50);
        steamingPitcher.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
        steamingPitcher.setOnDragListener(new ToSteamingPitcherDragListener());
        framelayoutRight.addView(steamingPitcher);

        // STEAMING WAND
        steamingWand = new SteamingWand(getContext());
        steamingWand.setTag(TAG_STEAMING_WAND);
        steamingWand.setLayoutParams(new FrameLayout.LayoutParams(32, 400));
        steamingWand.setX(800);
        steamingWand.setBackgroundColor(getResources().getColor(R.color.purple_700));
        steamingWand.setOnDragListener(new SteamingWandDragListener());
        constraintLayoutMaestrana.addView(steamingWand);

        // TODO:
        // ESPRESSO SHOT CONTROL
        ImageView espressoShotControl = new ImageView(getContext());
        espressoShotControl.setTag(TAG_ESPRESSO_SHOT_CONTROL);
        espressoShotControl.setLayoutParams(new FrameLayout.LayoutParams(128, 128));
        espressoShotControl.setX(250 - (128 / 2));
        espressoShotControl.setBackgroundColor(getResources().getColor(R.color.brown));
        espressoShotControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EspressoShotControlDialogFragment dialogFragment = new EspressoShotControlDialogFragment();
                dialogFragment.show(getChildFragmentManager(), EspressoShotControlDialogFragment.TAG);
            }
        });
        constraintLayoutMaestrana.addView(espressoShotControl);

        // ESPRESSO SHOT
        espressoShot = new EspressoShot(getContext());
        espressoShot.setTag(TAG_ESPRESSO_SHOT);
        espressoShot.setLayoutParams(new FrameLayout.LayoutParams(16, 64));
        espressoShot.setX(250 - (16 / 2));
        espressoShot.setBackgroundColor(getResources().getColor(R.color.brown));
        constraintLayoutMaestrana.addView(espressoShot);

        // SHOT GLASS
        shotGlass = new ShotGlass(getContext());
        shotGlass.setTag(TAG_SHOT_GLASS);
        shotGlass.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        shotGlass.setX(250 - (64 / 2));
        shotGlass.setBackgroundColor(getResources().getColor(R.color.cream));
        framelayoutLeft.addView(shotGlass);
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

    private class ToSteamingPitcherDragListener
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

                    String contentToBeSteamed = dragEvent.getClipData().getItemAt(0).getText().toString();
                    Log.e(TAG, contentToBeSteamed);

                    FillSteamingPitcherDialogFragment dialogFragment =
                            FillSteamingPitcherDialogFragment.newInstance(
                                    contentToBeSteamed,
                                    steamingPitcher.getAmount());
                    dialogFragment.show(getChildFragmentManager(), FillSteamingPitcherDialogFragment.TAG);

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
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

    private class SteamingWandDragListener
            implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        if (dragEvent.getClipDescription().getLabel().equals("SteamingPitcher")) {
                            Log.d(TAG, "label.equals(\"SteamingPitcher\")");

                            SteamingPitcher steamingPitcher = (SteamingPitcher) dragEvent.getLocalState();
                            if (steamingPitcher.getContent() != null &&
                                    steamingPitcher.getAmount() > 0) {
                                // Change value of alpha to indicate drop-target.
                                view.setAlpha(0.8f);

                                // Return true to indicate that the View can accept the dragged
                                // data.
                                return true;
                            } else {
                                Log.d(TAG, "steamingPitcher's content is null or amount <= 0.");
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
                    view.setAlpha(0.8f);

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "ACTION_DROP");

                    SteamingPitcher steamingPitcher = (SteamingPitcher) dragEvent.getLocalState();
                    steamingPitcher.steam();

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by SteamingWandDragListener.");
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
                                Log.e(TAG, "else-clause (generating ImageView to add to LinearLayout).");
                            }
                            ivToBeAdded.setBackgroundResource(resId);
                            ivToBeAdded.setTag(tag);
                        } else if (label.equals("MaestranaToCaddy")) {
                            Log.d(TAG, "ACTION_DROP label.equals(\"MaestranaToCaddy\")");
                            Log.d(TAG, "ACTION_DROP Derive ivToBeAdded from dragData");
                            ivToBeAdded = (CupImageView) dragEvent.getLocalState();

                            ViewGroup owner = (ViewGroup) ivToBeAdded.getParent();
                            owner.removeView(ivToBeAdded);
                        }

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(ivToBeAdded);
                        ivToBeAdded.setVisibility(View.INVISIBLE);
                    } else if (label.equals("ShotGlass")) {
                        Log.d(TAG, "ACTION_DROP label.equals(\"ShotGlass\")");
                        Log.d(TAG, "ACTION_DROP Derive shotGlass from dragData");
                        shotGlass = (ShotGlass) dragEvent.getLocalState();

                        ViewGroup owner = (ViewGroup) shotGlass.getParent();
                        owner.removeView(shotGlass);

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(shotGlass);
                        shotGlass.setVisibility(View.INVISIBLE);
                    }

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED");

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

                        if (label.equals("CaddyToMaestrana") || label.equals("MaestranaToCaddy")) {
                            if (ivToBeAdded != null) {
                                ivToBeAdded.setX(xTouch - (ivToBeAdded.getWidth() / 2));
                                ivToBeAdded.setY(yTouch - (ivToBeAdded.getHeight() / 2));
                            }
                        } else if (label.equals("ShotGlass")) {
                            if (shotGlass != null) {
                                shotGlass.setX(xTouch - (shotGlass.getWidth() / 2));
                                shotGlass.setY(yTouch - (shotGlass.getHeight() / 2));
                            }
                        }
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