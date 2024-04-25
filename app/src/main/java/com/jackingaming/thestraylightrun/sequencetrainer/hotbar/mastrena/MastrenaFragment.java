package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupCaddyFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.EspressoShotControlDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.ExpectedVsActualDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.FillCupColdDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments.FillSteamingPitcherDialogFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CaramelDrizzleBottle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CinnamonDispenser;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteEspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteSyrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SteamingWand;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans.EspressoShotRequest;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.beans.EspressoShotRequestAdapter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.parts.Collideable;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.IceShaker;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupHot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.refrigerator.RefrigeratorFragment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MastrenaFragment extends Fragment {
    public static final String TAG = MastrenaFragment.class.getSimpleName();
    private static final long DELAY_ADD_NEW_DRINK_EASY = 60000L;
    private static final int VALUE_BRACKET_YELLOW_EASY = 20;
    private static final int VALUE_BRACKET_RED_EASY = 40;

    private static final long DELAY_ADD_NEW_DRINK_MEDIUM = 45000L;
    private static final int VALUE_BRACKET_YELLOW_MEDIUM = 15;
    private static final int VALUE_BRACKET_RED_MEDIUM = 30;

    private static final long DELAY_ADD_NEW_DRINK_HARD = 30000L;
    private static final int VALUE_BRACKET_YELLOW_HARD = 10;
    private static final int VALUE_BRACKET_RED_HARD = 20;

    private long delayAddNewDrink;
    private int valueBracketYellow;
    private int valueBracketRed;

    public static final String TAG_ICE_SHAKER = "IceShaker";
    public static final String TAG_STEAMING_PITCHER = "SteamingPitcher";
    public static final String TAG_STEAMING_WAND = "SteamingWand";
    public static final String TAG_ESPRESSO_SHOT_CONTROL = "EspressoShotControl";
    public static final String TAG_SPRITE_ESPRESSO_SHOT = "EspressoShot";
    public static final String TAG_SHOT_GLASS = "ShotGlass";
    public static final String TAG_SYRUP_BOTTLE_VANILLA = "SyrupBottleVanilla";
    public static final String TAG_SYRUP_BOTTLE_BROWN_SUGAR = "SyrupBottleBrownSugar";
    public static final String TAG_SYRUP_BOTTLE_MOCHA = "SyrupBottleMocha";
    public static final String TAG_SPRITE_SYRUP = "Syrup";
    public static final String TAG_CARAMEL_DRIZZLE_BOTTLE = "CaramelDrizzleBottle";
    public static final String TAG_CINNAMON_DISPENSER = "CinnamonDispenser";

    private MastrenaViewModel mViewModel;
    private ConstraintLayout constraintLayoutMastrena;
    private LabelPrinter labelPrinter;
    private FrameLayout framelayoutLabelStagingArea;
    private TextView tvNumberOfDrinksInQueue;
    private TextView tvClock;
    private FrameLayout framelayoutEspressoStream, framelayoutSyrupCaddy, framelayoutSteamingWand;
    private FrameLayout framelayoutLeft, framelayoutCenter, framelayoutRight;
    private IceShaker iceShaker;
    private SteamingPitcher steamingPitcher;
    private SteamingWand steamingWand;

    private ImageView espressoShotControl;
    private List<EspressoShotRequest> espressoShotRequests;
    private RecyclerView rvEspressoShotRequests;
    private ObjectAnimator animatorEspressoShot;

    private ShotGlass shotGlass;
    private ImageView syrupBottleVanilla, syrupBottleBrownSugar, syrupBottleMocha;
    private CaramelDrizzleBottle caramelDrizzleBottle;
    private CinnamonDispenser cinnamonDispenser;

    private CupImageView ivToBeAdded;
    private float xTouch, yTouch;

    private int time;

    public static MastrenaFragment newInstance() {
        Log.e(TAG, "newInstance()");
        return new MastrenaFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        espressoShotRequests = new ArrayList<>();

        // Set the listener on the child fragmentManager.
        getChildFragmentManager()
                .setFragmentResultListener(FillSteamingPitcherDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                        String content = result.getString(FillSteamingPitcherDialogFragment.BUNDLE_KEY_CONTENT);
                        int amount = result.getInt(FillSteamingPitcherDialogFragment.BUNDLE_KEY_AMOUNT);

                        // check if content is a type of Milk.
                        for (Milk.Type type : Milk.Type.values()) {
                            if (content.equals(type.name())) {
                                Log.e(TAG, "Instantiate Milk and initialize it with temperature 37, timeFrothed 0, and type (" + content + ").");
                                Milk milk = new Milk();
                                milk.init(type, amount, RefrigeratorFragment.TEMPERATURE, 0);

                                steamingPitcher.updateMilk(milk);
                            }
                        }
                        // TODO: check if content is Lemonade.
                    }
                });
        getChildFragmentManager()
                .setFragmentResultListener(EspressoShotControlDialogFragment.REQUEST_KEY, this, new FragmentResultListener() {
                    @RequiresApi(api = 33)
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Log.e(TAG, "onFragmentResult() requestKey: " + requestKey);

                        int quantitySelected = result.getInt(EspressoShotControlDialogFragment.BUNDLE_KEY_QUANTITY);
                        EspressoShot.Type typeSelected = (EspressoShot.Type) result.getSerializable(EspressoShotControlDialogFragment.BUNDLE_KEY_TYPE);
                        EspressoShot.AmountOfWater amountOfWaterSelected = (EspressoShot.AmountOfWater) result.getSerializable(EspressoShotControlDialogFragment.BUNDLE_KEY_AMOUNT_OF_WATER);
                        EspressoShot.AmountOfBean amountOfBeanSelected = (EspressoShot.AmountOfBean) result.getSerializable(EspressoShotControlDialogFragment.BUNDLE_KEY_AMOUNT_OF_BEAN);
                        Log.e(TAG, "quantity: " + quantitySelected + ", type: " + typeSelected.name());
                        Log.e(TAG, "amountOfWaterSelected: " + amountOfWaterSelected.name() + ", amountOfBeanSelected: " + amountOfBeanSelected.name());

                        if (quantitySelected < 1) {
                            Log.e(TAG, "quantitySelected < 1 returning.");
                            return;
                        }

                        EspressoShotRequest espressoShotRequest = new EspressoShotRequest(quantitySelected, typeSelected, amountOfWaterSelected, amountOfBeanSelected);

                        Log.e(TAG, "ADDING ESPRESSO SHOT REQUEST TO QUEUE.");
                        int index = espressoShotRequests.size();
                        espressoShotRequests.add(index, espressoShotRequest);
                        rvEspressoShotRequests.getAdapter().notifyItemInserted(index);

                        if (animatorEspressoShot == null) {
                            Log.e(TAG, "animatorEspressoShot == null... PULL ESPRESSO SHOT.");

                            pullEspressoShot(espressoShotRequest);
                        } else {
                            Log.e(TAG, "animatorEspressoShot != null... DO NOTHING.");
                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_mastrena, container, false);
    }

    private int quantityEspressoShotCurrent;

    private void pullEspressoShot(EspressoShotRequest espressoShotRequest) {
        int quantityEspressoShotSelected = espressoShotRequest.getQuantity();

        SpriteEspressoShot spriteEspressoShot = new SpriteEspressoShot(getContext());
        spriteEspressoShot.init(espressoShotRequest);
        spriteEspressoShot.setTag(TAG_SPRITE_ESPRESSO_SHOT);
        spriteEspressoShot.setLayoutParams(new FrameLayout.LayoutParams(16, 64));
        float xCenterOfEspressoShotControl = espressoShotControl.getX() + (espressoShotControl.getWidth() / 2) - (16 / 2);
        spriteEspressoShot.setX(xCenterOfEspressoShotControl);
//        espressoShot.setX(200 - (16 / 2));
        spriteEspressoShot.setY(458 + 64); // 64 is for espresso shot queue viewer's height.
        constraintLayoutMastrena.addView(spriteEspressoShot);

        animatorEspressoShot = ObjectAnimator.ofFloat(
                spriteEspressoShot,
                "y",
                458 + 64f,
                1200f);
        long startDelay = (quantityEspressoShotCurrent == 0) ? (1500L * quantityEspressoShotSelected) : (0);
        animatorEspressoShot.setStartDelay(startDelay);
        animatorEspressoShot.setDuration(2000L);
        animatorEspressoShot.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onAnimationEnd()");

                quantityEspressoShotCurrent++;

                if (spriteEspressoShot != null) {
                    constraintLayoutMastrena.removeView(spriteEspressoShot);
                }

                if (quantityEspressoShotCurrent < quantityEspressoShotSelected) {
                    // current request, more than one shot needed.
                    pullEspressoShot(espressoShotRequest);
                } else if (quantityEspressoShotCurrent == quantityEspressoShotSelected) {
                    // number of shots reached, request completed.
                    quantityEspressoShotCurrent = 0;

                    // remove request from queue.
                    Log.e(TAG, "REMOVING ESPRESSO SHOT REQUEST FROM QUEUE.");
                    espressoShotRequests.remove(0);
                    rvEspressoShotRequests.getAdapter().notifyItemRemoved(0);

                    // check queue.
                    if (!espressoShotRequests.isEmpty()) {
                        pullEspressoShot(
                                espressoShotRequests.get(0)
                        );
                    } else {
                        Log.e(TAG, "END OF QUEUE REACHED.");
                        animatorEspressoShot = null;
                    }
                } else {
                    Log.e(TAG, "quantityCurrentEspressoShot > quantitySelected");
                }
            }
        });
        animatorEspressoShot.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                for (int i = 0; i < framelayoutLeft.getChildCount(); i++) {
                    View view = framelayoutLeft.getChildAt(i);
                    if (view instanceof Collideable) {
                        if (view instanceof IceShaker) {
                            continue;
                        }

                        Collideable collideable = (Collideable) view;

                        boolean colliding = isViewOverlapping(spriteEspressoShot, view);
                        collideable.update(colliding);

                        if (collideable.isJustCollided()) {
                            Log.e(TAG, "collideable.isJustCollided()");

                            collideable.onCollided(spriteEspressoShot);
                            constraintLayoutMastrena.removeView(spriteEspressoShot);
                        }
                    } else {
                        Log.e(TAG, "onAnimationUpdate(): view NOT instanceof Collideable.");
                    }
                }
            }
        });
        animatorEspressoShot.start();
    }

    private void pumpSyrup(Syrup.Type type) {
        SpriteSyrup spriteSyrup = new SpriteSyrup(getContext());
        spriteSyrup.init(type);
        spriteSyrup.setTag(TAG_SPRITE_SYRUP);
        spriteSyrup.setLayoutParams(new FrameLayout.LayoutParams(16, 32));
        if (type == Syrup.Type.VANILLA) {
            spriteSyrup.setX(344);
        } else if (type == Syrup.Type.BROWN_SUGAR) {
            spriteSyrup.setX(344 + 64);
        } else if (type == Syrup.Type.MOCHA) {
            spriteSyrup.setX(344 + 64 + 64);
        }
        spriteSyrup.setY(562);
        constraintLayoutMastrena.addView(spriteSyrup);

        ObjectAnimator animatorSyrup = ObjectAnimator.ofFloat(
                spriteSyrup,
                "y",
                562f,
                1200f);
        animatorSyrup.setDuration(2000L);
        animatorSyrup.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onAnimationEnd()");

                if (spriteSyrup != null) {
                    constraintLayoutMastrena.removeView(spriteSyrup);
                }
            }
        });
        animatorSyrup.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                for (int i = 0; i < framelayoutCenter.getChildCount(); i++) {
                    View view = framelayoutCenter.getChildAt(i);
                    if (view instanceof Collideable) {
                        if (!(view instanceof CupImageView) &&
                                !(view instanceof IceShaker)) {
                            continue;
                        }

                        Collideable collideable = (Collideable) view;

                        boolean colliding = isViewOverlapping(spriteSyrup, view);
                        collideable.update(colliding);

                        if (collideable.isJustCollided()) {
                            Log.e(TAG, "collideable.isJustCollided()");

                            collideable.onCollided(spriteSyrup);
                            constraintLayoutMastrena.removeView(spriteSyrup);
                        }
                    } else {
                        Log.e(TAG, "onAnimationUpdate(): view NOT instanceof Collideable.");
                    }
                }
            }
        });
        animatorSyrup.start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        delayAddNewDrink = DELAY_ADD_NEW_DRINK_HARD;
        valueBracketYellow = VALUE_BRACKET_YELLOW_HARD;
        valueBracketRed = VALUE_BRACKET_RED_HARD;

        constraintLayoutMastrena = view.findViewById(R.id.constraintlayout_mastrena);
        framelayoutLabelStagingArea = view.findViewById(R.id.framelayout_label_staging_area);
        tvNumberOfDrinksInQueue = view.findViewById(R.id.tv_number_of_drinks_in_queue);
        tvClock = view.findViewById(R.id.tv_clock);
        framelayoutEspressoStream = view.findViewById(R.id.framelayout_espresso_stream);
        framelayoutSyrupCaddy = view.findViewById(R.id.framelayout_syrup_caddy);
        framelayoutSteamingWand = view.findViewById(R.id.framelayout_steaming_wand);
        framelayoutLeft = view.findViewById(R.id.framelayout_left);
        framelayoutCenter = view.findViewById(R.id.framelayout_center);
        framelayoutRight = view.findViewById(R.id.framelayout_right);

        framelayoutLabelStagingArea.setOnDragListener(new LabelStagingAreaDragListener());
        ObjectAnimator clockAnimator = ObjectAnimator.ofInt(this, "time", 60);
        clockAnimator.setDuration(60000L);
        clockAnimator.setRepeatCount(ValueAnimator.INFINITE);
        clockAnimator.setInterpolator(new LinearInterpolator());
        clockAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private long previousTime = System.currentTimeMillis();

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                // add new drinks to LabelPrinter's queue.
                long nowTime = System.currentTimeMillis();
                long differenceTime = nowTime - previousTime;
                if (differenceTime > delayAddNewDrink) {
                    if (labelPrinter != null) {
                        labelPrinter.generateRandomDrinkRequest();
                        labelPrinter.updateDisplay();

                        previousTime = nowTime;
                    }
                }

                // update number of drinks currently in queue.
                if (labelPrinter != null) {
                    tvNumberOfDrinksInQueue.setText(
                            Integer.toString(labelPrinter.numberOfDrinksInQueue())
                    );
                }

                // update clock
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("hh:mm:ss a");
                String timeNow = now.format(formatterTime);

                tvClock.setText(timeNow);

                // update drink labels' color.
                for (int i = 0; i < framelayoutLabelStagingArea.getChildCount(); i++) {
                    if (framelayoutLabelStagingArea.getChildAt(i) instanceof DrinkLabel) {
                        DrinkLabel drinkLabel = (DrinkLabel) framelayoutLabelStagingArea.getChildAt(i);
                        String[] drinkLabelSplitted = drinkLabel.getDrink().getTextForDrinkLabel().split("\\s+");
                        String date = drinkLabelSplitted[0];
                        String time = drinkLabelSplitted[1];
                        String amOrPm = drinkLabelSplitted[2];

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
                        LocalDateTime localDateTimeDrinkLabel = LocalDateTime.parse(date + " " + time + " " + amOrPm, formatter);

                        Duration actual = Duration.between(localDateTimeDrinkLabel, now);
                        Duration bracketYellow = Duration.ofSeconds(valueBracketYellow);
                        Duration bracketRed = Duration.ofSeconds(valueBracketRed);
                        if (actual.compareTo(bracketRed) > 0) {
                            drinkLabel.setBackgroundColor(getResources().getColor(R.color.red));
                        } else if (actual.compareTo(bracketYellow) > 0) {
                            drinkLabel.setBackgroundColor(getResources().getColor(R.color.yellow));
                        } else {
                            // keep original background color.
                        }
                    }
                }
            }
        });
        clockAnimator.start();

        View.OnDragListener mastrenaDragListener = new MastrenaDragListener();
        framelayoutLeft.setOnDragListener(mastrenaDragListener);
        framelayoutCenter.setOnDragListener(mastrenaDragListener);
        framelayoutRight.setOnDragListener(mastrenaDragListener);

        // ICE SHAKER
        iceShaker = new IceShaker(getContext());
        iceShaker.setTag(TAG_ICE_SHAKER);
        iceShaker.setLayoutParams(new FrameLayout.LayoutParams(64, 128));
        iceShaker.setX(288 - 64 - 30);
        iceShaker.setY(458 - 96 - 50);
        iceShaker.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
        framelayoutRight.addView(iceShaker);

        // STEAMING PITCHER
        steamingPitcher = new SteamingPitcher(getContext());
        steamingPitcher.setTag(TAG_STEAMING_PITCHER);
        steamingPitcher.setLayoutParams(new FrameLayout.LayoutParams(64, 64));
        steamingPitcher.setX(30);
        steamingPitcher.setY(50);
        steamingPitcher.setBackgroundColor(getResources().getColor(R.color.light_blue_A200));
        steamingPitcher.setListener(new SteamingPitcher.SteamingPitcherListener() {
            @Override
            public void showDialogFillSteamingPitcher(String contentToBeSteamed) {
                FillSteamingPitcherDialogFragment dialogFragment =
                        FillSteamingPitcherDialogFragment.newInstance(contentToBeSteamed);
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

        // ESPRESSO SHOT QUEUE VIEWER
        rvEspressoShotRequests = new RecyclerView(getContext());
        rvEspressoShotRequests.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 64, Gravity.CENTER_HORIZONTAL));
        rvEspressoShotRequests.setBackgroundColor(getResources().getColor(R.color.light_green));
        EspressoShotRequestAdapter adapter = new EspressoShotRequestAdapter(espressoShotRequests);
        rvEspressoShotRequests.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvEspressoShotRequests.setLayoutManager(linearLayoutManager);
        framelayoutEspressoStream.addView(rvEspressoShotRequests);

        // ESPRESSO SHOT CONTROL
        espressoShotControl = new ImageView(getContext());
        espressoShotControl.setTag(TAG_ESPRESSO_SHOT_CONTROL);
        espressoShotControl.setLayoutParams(new FrameLayout.LayoutParams(128, 128, Gravity.CENTER_HORIZONTAL));
//        espressoShotControl.setX(200 - (128 / 2));
        espressoShotControl.setY(64);
        espressoShotControl.setBackgroundColor(getResources().getColor(R.color.brown));
        espressoShotControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EspressoShotControlDialogFragment dialogFragment = new EspressoShotControlDialogFragment();
                dialogFragment.show(getChildFragmentManager(), EspressoShotControlDialogFragment.TAG);
            }
        });
        framelayoutEspressoStream.addView(espressoShotControl);

        // SHOT GLASS
        shotGlass = new ShotGlass(getContext());
        shotGlass.setTag(TAG_SHOT_GLASS);
        shotGlass.setLayoutParams(new FrameLayout.LayoutParams(64, 64, Gravity.CENTER_HORIZONTAL));
//        shotGlass.setX(200 - (64 / 2));
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

                pumpSyrup(Syrup.Type.VANILLA);
            }
        });
        framelayoutSyrupCaddy.addView(syrupBottleVanilla);

        // SYRUP BOTTLE (brown sugar)
        syrupBottleBrownSugar = new androidx.appcompat.widget.AppCompatImageView(getContext()) {
            private Paint paintGreen;
            private int marginHorizontal = 8;
            private int marginVertical = 24;

            {
                paintGreen = new Paint();
                paintGreen.setAntiAlias(true);
                paintGreen.setColor(getResources().getColor(R.color.green));
            }

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                canvas.drawRect(marginHorizontal, marginVertical, getWidth() - marginHorizontal, getHeight() - marginVertical, paintGreen);
            }
        };
        syrupBottleBrownSugar.setTag(TAG_SYRUP_BOTTLE_BROWN_SUGAR);
        syrupBottleBrownSugar.setLayoutParams(new FrameLayout.LayoutParams(64, 128));
        syrupBottleBrownSugar.setX(64 + 64 - (64 / 2));
        syrupBottleBrownSugar.setBackgroundColor(getResources().getColor(R.color.brown));
        syrupBottleBrownSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "syrupBottleBrownSugar.onClick()");

                pumpSyrup(Syrup.Type.BROWN_SUGAR);
            }
        });
        framelayoutSyrupCaddy.addView(syrupBottleBrownSugar);

        // SYRUP BOTTLE (mocha)
        syrupBottleMocha = new ImageView(getContext());
        syrupBottleMocha.setTag(TAG_SYRUP_BOTTLE_MOCHA);
        syrupBottleMocha.setLayoutParams(new FrameLayout.LayoutParams(64, 128));
        syrupBottleMocha.setX(64 + 64 + 64 - (64 / 2));
        syrupBottleMocha.setBackgroundColor(getResources().getColor(R.color.dark_brown));
        syrupBottleMocha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "syrupBottleMocha.onClick()");

                pumpSyrup(Syrup.Type.MOCHA);
            }
        });
        framelayoutSyrupCaddy.addView(syrupBottleMocha);

        // CARAMEL DRIZZLE BOTTLE
        caramelDrizzleBottle = new CaramelDrizzleBottle(getContext());
        caramelDrizzleBottle.setTag(TAG_CARAMEL_DRIZZLE_BOTTLE);
        caramelDrizzleBottle.setLayoutParams(new FrameLayout.LayoutParams(48, 96));
        caramelDrizzleBottle.setX(384 - (48 + 16));
        caramelDrizzleBottle.setY(452 - (96 + 16));
        caramelDrizzleBottle.setBackgroundColor(getResources().getColor(R.color.yellow));
        framelayoutSyrupCaddy.addView(caramelDrizzleBottle);

        // CINNAMON DISPENSER
        cinnamonDispenser = new CinnamonDispenser(getContext());
        cinnamonDispenser.setTag(TAG_CINNAMON_DISPENSER);
        cinnamonDispenser.setLayoutParams(new FrameLayout.LayoutParams(48, 48));
        cinnamonDispenser.setX(384 - (48 + 16));
        cinnamonDispenser.setY(452 - (48 + 16 + 96 + 16));
        cinnamonDispenser.setBackgroundColor(getResources().getColor(R.color.red));
        framelayoutSyrupCaddy.addView(cinnamonDispenser);
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

        mViewModel = new ViewModelProvider(this).get(MastrenaViewModel.class);
        // TODO: Use the ViewModel
    }

    private class LabelStagingAreaDragListener
            implements View.OnDragListener {
        int resIdNormal = R.drawable.shape_mastrena;
        int resIdDropTarget = R.drawable.shape_droptarget;

        private String label;
        private DrinkLabel drinkLabel;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        label = dragEvent.getClipDescription().getLabel().toString();
                        if (label.equals(LabelPrinter.DRAG_LABEL)) {
                            // Change background drawable to indicate drop-target.
                            view.setBackgroundResource(resIdDropTarget);

                            // Return true to indicate that the View can accept the dragged
                            // data.
                            return true;
                        } else if (label.equals(DrinkLabel.DRAG_LABEL)) {
                            drinkLabel = (DrinkLabel) dragEvent.getLocalState();

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

                    if (label.equals(LabelPrinter.DRAG_LABEL)) {
                        Drink drinkFirst = (Drink) dragEvent.getLocalState();

                        // Instantiate DrinkLabel.
                        DrinkLabel drinkLabelNew = new DrinkLabel(getContext());
                        drinkLabelNew.setDrink(drinkFirst);
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

                        labelPrinter.removeFromQueue(drinkFirst);
                        labelPrinter.updateDisplay();
                    } else if (label.equals(DrinkLabel.DRAG_LABEL)) {
                        drinkLabel.setX(xTouch - (drinkLabel.getWidth() / 2));
                        drinkLabel.setY(yTouch - (drinkLabel.getHeight() / 2));
                    }

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED");

                    // Reset value of alpha back to normal.
                    view.setAlpha(1.0f);
                    // Reset the background drawable to normal.
                    view.setBackgroundResource(resIdNormal);

                    if (label.equals(LabelPrinter.DRAG_LABEL)) {
                        labelPrinter.setVisibility(View.VISIBLE);
                    } else if (label.equals(DrinkLabel.DRAG_LABEL) && drinkLabel != null) {
                        drinkLabel.setVisibility(View.VISIBLE);
                        drinkLabel = null;
                    }

                    // Do a getResult() and displays what happens.
                    if (dragEvent.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    }

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by LabelPrinterDragListener.");
                    break;
            }

            return false;
        }
    }

    private static final int SHAKE_DETECTION_THRESHOLD = 50;
    private float yPrevious, yPeak, yTrough = -1f;
    private boolean shakeUpward, metThreshold = false;
    private int shakeCounter = 0;

    private String label;

    private class MastrenaDragListener
            implements View.OnDragListener {
        int resIdNormal = R.drawable.shape_mastrena;
        int resIdDropTarget = R.drawable.shape_droptarget;

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this View can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "ACTION_DRAG_STARTED ClipDescription.MIMETYPE_TEXT_PLAIN");

                        label = dragEvent.getClipDescription().getLabel().toString();
                        if (label.equals(CupCaddyFragment.DRAG_LABEL) ||
                                label.equals(CupHot.DRAG_LABEL) ||
                                label.equals(CupCold.DRAG_LABEL) ||
                                label.equals(ShotGlass.DRAG_LABEL) ||
                                label.equals(IceShaker.DRAG_LABEL)) {
                            Log.d(TAG, "label.equals(" + CupCaddyFragment.DRAG_LABEL + ") || label.equals(" + CupHot.DRAG_LABEL + ") || label.equals(" + CupCold.DRAG_LABEL + ") || label.equals(" + ShotGlass.DRAG_LABEL + ") || label.equals(" + IceShaker.DRAG_LABEL + ")");

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

                    if (label.equals(CupHot.DRAG_LABEL) ||
                            label.equals(CupCold.DRAG_LABEL)) {
                        ivToBeAdded = (CupImageView) dragEvent.getLocalState();
                    }

                    // Return true. The value is ignored.
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
//                    Log.d(TAG, "ACTION_DRAG_LOCATION");

                    // TODO: re-work IceShaker's shaking logic.
                    if (label.equals(IceShaker.DRAG_LABEL)) {
                        float yNow = dragEvent.getY();
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
                            Toast.makeText(getContext(), "SHAKEN", Toast.LENGTH_SHORT).show();
                            iceShaker.shake();
                        }
                    }

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

                    if (label.equals(CupCaddyFragment.DRAG_LABEL) ||
                            label.equals(CupHot.DRAG_LABEL) ||
                            label.equals(CupCold.DRAG_LABEL)) {
                        if (label.equals(CupCaddyFragment.DRAG_LABEL)) {
                            Log.d(TAG, "ACTION_DROP label.equals(" + CupCaddyFragment.DRAG_LABEL + ")");
                            Log.d(TAG, "ACTION_DROP Instantiate ImageView for ivToBeAdded");

                            // Get the item containing the dragged data.
                            ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                            // Get the text data from the item.
                            String dragData = item.getText().toString();

                            // Display a message containing the dragged data.
                            Toast.makeText(getContext(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                            // TODO: use dragData to determine instantiation of CupCold/CupHot.
                            // Set ImageView's background and tag according to dragData.
                            int resId = -1;
                            CupCold.CupColdListener cupColdListener = new CupCold.CupColdListener() {
                                @Override
                                public void showDialogFillCupCold(CupCold cupCold, String contentToBePoured) {
                                    FillCupColdDialogFragment dialogFragment =
                                            FillCupColdDialogFragment.newInstance(cupCold, contentToBePoured);
                                    dialogFragment.show(getChildFragmentManager(), FillCupColdDialogFragment.TAG);
                                }
                            };
                            CupImageView.CupImageViewListener cupImageViewListener = new CupImageView.CupImageViewListener() {
                                @Override
                                public void showExpectedVsActualDialogFragment(Drink drink, CupImageView cupImageView) {
                                    ExpectedVsActualDialogFragment dialogFragment =
                                            ExpectedVsActualDialogFragment.newInstance(drink, cupImageView);
                                    dialogFragment.show(getChildFragmentManager(), ExpectedVsActualDialogFragment.TAG);
                                }
                            };
                            if (dragData.equals(CupCaddyFragment.TAG_COLD_TALL)) {
                                resId = R.drawable.cold_drinksize_tall;
                                ivToBeAdded = new CupCold(getContext());
                                ((CupCold) ivToBeAdded).setCupColdListener(cupColdListener);
                                ((CupCold) ivToBeAdded).setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_COLD_GRANDE)) {
                                resId = R.drawable.cold_drinksize_grande;
                                ivToBeAdded = new CupCold(getContext());
                                ((CupCold) ivToBeAdded).setCupColdListener(cupColdListener);
                                ((CupCold) ivToBeAdded).setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_COLD_VENTI)) {
                                resId = R.drawable.cold_drinksize_venti;
                                ivToBeAdded = new CupCold(getContext());
                                ((CupCold) ivToBeAdded).setCupColdListener(cupColdListener);
                                ((CupCold) ivToBeAdded).setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_COLD_TRENTA)) {
                                resId = R.drawable.cold_drinksize_trenta;
                                ivToBeAdded = new CupCold(getContext());
                                ((CupCold) ivToBeAdded).setCupColdListener(cupColdListener);
                                ((CupCold) ivToBeAdded).setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_HOT_VENTI)) {
                                resId = R.drawable.hot_drinksize_venti;
                                ivToBeAdded = new CupHot(getContext());
                                ivToBeAdded.setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_HOT_GRANDE)) {
                                resId = R.drawable.hot_drinksize_grande;
                                ivToBeAdded = new CupHot(getContext());
                                ivToBeAdded.setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_HOT_TALL)) {
                                resId = R.drawable.hot_drinksize_tall;
                                ivToBeAdded = new CupHot(getContext());
                                ivToBeAdded.setCupImageViewListener(cupImageViewListener);
                            } else if (dragData.equals(CupCaddyFragment.TAG_HOT_SHORT)) {
                                resId = R.drawable.hot_drinksize_short;
                                ivToBeAdded = new CupHot(getContext());
                                ivToBeAdded.setCupImageViewListener(cupImageViewListener);
                            } else {
                                Log.e(TAG, "else-clause (selecting image resource for ivToBeAdded).");
                            }
                            FrameLayout.LayoutParams layoutParams =
                                    new FrameLayout.LayoutParams(64, 64);
                            ivToBeAdded.setLayoutParams(layoutParams);
                            ivToBeAdded.setBackgroundResource(resId);
                            ivToBeAdded.setTag(dragData);

                            ivToBeAdded.setX(xTouch - (64 / 2));
                            ivToBeAdded.setY(yTouch - (64 / 2));
                        } else if (label.equals(CupHot.DRAG_LABEL) ||
                                label.equals(CupCold.DRAG_LABEL)) {
                            Log.d(TAG, "ACTION_DROP label.equals(CupHot.DRAG_LABEL) || label.equals(CupCold.DRAG_LABEL)");
                            Log.d(TAG, "ACTION_DROP Derive ivToBeAdded from dragData");
                            ivToBeAdded = (CupImageView) dragEvent.getLocalState();

                            ViewGroup owner = (ViewGroup) ivToBeAdded.getParent();
                            owner.removeView(ivToBeAdded);

                            ivToBeAdded.setX(xTouch - (ivToBeAdded.getWidth() / 2));
                            ivToBeAdded.setY(yTouch - (ivToBeAdded.getHeight() / 2));
                        }

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(ivToBeAdded);
                    } else if (label.equals(ShotGlass.DRAG_LABEL)) {
                        Log.d(TAG, "ACTION_DROP label.equals(" + ShotGlass.DRAG_LABEL + ")");
                        Log.d(TAG, "ACTION_DROP Derive shotGlass from dragData");
                        shotGlass = (ShotGlass) dragEvent.getLocalState();

                        ViewGroup owner = (ViewGroup) shotGlass.getParent();
                        owner.removeView(shotGlass);

                        shotGlass.setX(xTouch - (shotGlass.getWidth() / 2));
                        shotGlass.setY(yTouch - (shotGlass.getHeight() / 2));

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(shotGlass);
                    } else if (label.equals(IceShaker.DRAG_LABEL)) {
                        Log.d(TAG, "ACTION_DROP label.equals(" + IceShaker.DRAG_LABEL + ")");
                        Log.d(TAG, "ACTION_DROP Derive IceShaker from dragData");
                        iceShaker = (IceShaker) dragEvent.getLocalState();

                        ViewGroup owner = (ViewGroup) iceShaker.getParent();
                        owner.removeView(iceShaker);

                        iceShaker.setX(xTouch - (iceShaker.getWidth() / 2));
                        iceShaker.setY(yTouch - (iceShaker.getHeight() / 2));

                        // Add ImageView to FrameLayout.
                        ((FrameLayout) view).addView(iceShaker);
                    }

                    // Return true. DragEvent.getResult() returns true.
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "ACTION_DRAG_ENDED MastrenaFragment");

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

                    if (label.equals(CupCaddyFragment.DRAG_LABEL) ||
                            label.equals(CupHot.DRAG_LABEL) ||
                            label.equals(CupCold.DRAG_LABEL)) {
                        if (ivToBeAdded != null) {
                            ivToBeAdded.setVisibility(View.VISIBLE);

                            ivToBeAdded = null;
                        }
                    } else if (label.equals(ShotGlass.DRAG_LABEL)) {
                        if (shotGlass != null) {
                            shotGlass.setVisibility(View.VISIBLE);
                        }
                    } else if (label.equals(IceShaker.DRAG_LABEL)) {
                        shakeUpward = false;
                        metThreshold = false;
                        shakeCounter = 0;
                        yPeak = -1;
                        yTrough = -1;

                        if (iceShaker != null) {
                            iceShaker.setVisibility(View.VISIBLE);
                        }
                    }

                    xTouch = -1f;
                    yTouch = -1f;

                    Log.e(TAG, "setting label to null.");
                    label = null;

                    // Return true. The value is ignored.
                    return true;
                default:
                    Log.e(TAG, "Unknown action type received by MastrenaDragListener.");
                    break;
            }

            return false;
        }

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public LabelPrinter getLabelPrinter() {
        return labelPrinter;
    }

    public void setLabelPrinter(LabelPrinter labelPrinter) {
        this.labelPrinter = labelPrinter;
    }
}