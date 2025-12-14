package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.GrowSystemPartsDataCarrier;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.GrowSystemPart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrowSystemPartsDisplayerFragment extends DialogFragment
        implements Serializable {
    public static final String TAG = GrowSystemPartsDisplayerFragment.class.getSimpleName();
    public static final String ARG_GROW_SYSTEM_PARTS_DATA_CARRIER = "grow_system_parts_data_carrier";
    public static final String ARG_ANIMATION_LISTENER = "animation_listener";

    public interface AnimationListener extends Serializable {
        void onAnimationFinish();
    }

    private GrowSystemPartsDataCarrier growSystemPartsDataCarrier;
    private List<GrowSystemPart> growSystemParts;
    private List<ImageView> ivGrowSystemPartsList;
    private AnimationListener animationListener;

    private ImageView ivGrowSystemParts0, ivGrowSystemParts1, ivGrowSystemParts2,
            ivGrowSystemParts3, ivGrowSystemParts4, ivGrowSystemParts5;
    private TextView tvIsPoweredGrowSystemParts0, tvIsPoweredGrowSystemParts1, tvIsPoweredGrowSystemParts2,
            tvIsPoweredGrowSystemParts3, tvIsPoweredGrowSystemParts4, tvIsPoweredGrowSystemParts5;
    private TextView tvIsCalibratedGrowSystemParts0, tvIsCalibratedGrowSystemParts1, tvIsCalibratedGrowSystemParts2,
            tvIsCalibratedGrowSystemParts3, tvIsCalibratedGrowSystemParts4, tvIsCalibratedGrowSystemParts5;

    public static GrowSystemPartsDisplayerFragment newInstance(GrowSystemPartsDataCarrier growSystemPartsDataCarrier, AnimationListener animationListener) {
        GrowSystemPartsDisplayerFragment fragment = new GrowSystemPartsDisplayerFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_GROW_SYSTEM_PARTS_DATA_CARRIER, growSystemPartsDataCarrier);
        args.putSerializable(ARG_ANIMATION_LISTENER, animationListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        if (getArguments() != null) {
            growSystemPartsDataCarrier = (GrowSystemPartsDataCarrier) getArguments().getSerializable(ARG_GROW_SYSTEM_PARTS_DATA_CARRIER);
            animationListener = (AnimationListener) getArguments().getSerializable(ARG_ANIMATION_LISTENER);

            growSystemParts = growSystemPartsDataCarrier.getGrowSystemParts();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_grow_system_parts_displayer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        ivGrowSystemParts0 = view.findViewById(R.id.iv_grow_system_parts_0);
        ivGrowSystemParts1 = view.findViewById(R.id.iv_grow_system_parts_1);
        ivGrowSystemParts2 = view.findViewById(R.id.iv_grow_system_parts_2);
        ivGrowSystemParts3 = view.findViewById(R.id.iv_grow_system_parts_3);
        ivGrowSystemParts4 = view.findViewById(R.id.iv_grow_system_parts_4);
        ivGrowSystemParts5 = view.findViewById(R.id.iv_grow_system_parts_5);

        ivGrowSystemPartsList = new ArrayList<>();
        ivGrowSystemPartsList.add(ivGrowSystemParts0);
        ivGrowSystemPartsList.add(ivGrowSystemParts1);
        ivGrowSystemPartsList.add(ivGrowSystemParts2);
        ivGrowSystemPartsList.add(ivGrowSystemParts3);
        ivGrowSystemPartsList.add(ivGrowSystemParts4);
        ivGrowSystemPartsList.add(ivGrowSystemParts5);

        for (int i = 0; i < growSystemParts.size(); i++) {
            ivGrowSystemPartsList.get(i).setImageBitmap(
                    growSystemParts.get(i).getImage()
            );
        }

        tvIsPoweredGrowSystemParts0 = view.findViewById(R.id.tv_is_powered_grow_system_parts_0);
        tvIsPoweredGrowSystemParts1 = view.findViewById(R.id.tv_is_powered_grow_system_parts_1);
        tvIsPoweredGrowSystemParts2 = view.findViewById(R.id.tv_is_powered_grow_system_parts_2);
        tvIsPoweredGrowSystemParts3 = view.findViewById(R.id.tv_is_powered_grow_system_parts_3);
        tvIsPoweredGrowSystemParts4 = view.findViewById(R.id.tv_is_powered_grow_system_parts_4);
        tvIsPoweredGrowSystemParts5 = view.findViewById(R.id.tv_is_powered_grow_system_parts_5);

        tvIsCalibratedGrowSystemParts0 = view.findViewById(R.id.tv_is_calibrated_grow_system_parts_0);
        tvIsCalibratedGrowSystemParts1 = view.findViewById(R.id.tv_is_calibrated_grow_system_parts_1);
        tvIsCalibratedGrowSystemParts2 = view.findViewById(R.id.tv_is_calibrated_grow_system_parts_2);
        tvIsCalibratedGrowSystemParts3 = view.findViewById(R.id.tv_is_calibrated_grow_system_parts_3);
        tvIsCalibratedGrowSystemParts4 = view.findViewById(R.id.tv_is_calibrated_grow_system_parts_4);
        tvIsCalibratedGrowSystemParts5 = view.findViewById(R.id.tv_is_calibrated_grow_system_parts_5);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    private long elapsedOverall;
    private boolean updatedIndex0, updatedIndex1, updatedIndex2,
            updatedIndex3, updatedIndex4, updatedIndex5;

    public void update(long elapsed) {
        if (ivGrowSystemParts0 == null) {
            return;
        }

        elapsedOverall += elapsed;

        if (elapsedOverall >= 0 && elapsedOverall < 1000 && !updatedIndex0) {
            updatedIndex0 = true;

            ivGrowSystemParts0.animate().setDuration(2500L);
            ivGrowSystemParts0.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // check broken
                    if (growSystemParts.size() > 0) {
                        GrowSystemPart growSystemPart0 = growSystemParts.get(0);
                        ImageView ivGrowSystemPart0 = ivGrowSystemPartsList.get(0);
                        if (growSystemPart0.isPowered() && growSystemPart0.isCalibrated()) {
                            ivGrowSystemPart0.setBackgroundColor(Color.GREEN);
                        } else {
                            ivGrowSystemPart0.setBackgroundColor(Color.RED);
                        }

                        if (growSystemPart0.isPowered()) {
                            tvIsPoweredGrowSystemParts0.setTextColor(Color.GREEN);
                        } else {
                            tvIsPoweredGrowSystemParts0.setTextColor(Color.RED);
                        }

                        if (growSystemPart0.isCalibrated()) {
                            tvIsCalibratedGrowSystemParts0.setTextColor(Color.GREEN);
                        } else {
                            tvIsCalibratedGrowSystemParts0.setTextColor(Color.RED);
                        }
                    }
                }
            });
            ivGrowSystemParts0.animate().rotation(360);
        } else if (elapsedOverall >= 1000 && elapsedOverall < 2000 && !updatedIndex1) {
            updatedIndex1 = true;

            ivGrowSystemParts1.animate().setDuration(2500L);
            ivGrowSystemParts1.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // check broken
                    if (growSystemParts.size() > 1) {
                        GrowSystemPart growSystemPart1 = growSystemParts.get(1);
                        ImageView ivGrowSystemPart1 = ivGrowSystemPartsList.get(1);
                        if (growSystemPart1.isPowered() && growSystemPart1.isCalibrated()) {
                            ivGrowSystemPart1.setBackgroundColor(Color.GREEN);
                        } else {
                            ivGrowSystemPart1.setBackgroundColor(Color.RED);
                        }

                        if (growSystemPart1.isPowered()) {
                            tvIsPoweredGrowSystemParts1.setTextColor(Color.GREEN);
                        } else {
                            tvIsPoweredGrowSystemParts1.setTextColor(Color.RED);
                        }

                        if (growSystemPart1.isCalibrated()) {
                            tvIsCalibratedGrowSystemParts1.setTextColor(Color.GREEN);
                        } else {
                            tvIsCalibratedGrowSystemParts1.setTextColor(Color.RED);
                        }
                    }
                }
            });
            ivGrowSystemParts1.animate().rotation(360);
        } else if (elapsedOverall >= 2000 && elapsedOverall < 3000 && !updatedIndex2) {
            updatedIndex2 = true;

            ivGrowSystemParts2.animate().setDuration(2500L);
            ivGrowSystemParts2.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // check broken
                    if (growSystemParts.size() > 2) {
                        GrowSystemPart growSystemPart2 = growSystemParts.get(2);
                        ImageView ivGrowSystemPart2 = ivGrowSystemPartsList.get(2);
                        if (growSystemPart2.isPowered() && growSystemPart2.isCalibrated()) {
                            ivGrowSystemPart2.setBackgroundColor(Color.GREEN);
                        } else {
                            ivGrowSystemPart2.setBackgroundColor(Color.RED);
                        }

                        if (growSystemPart2.isPowered()) {
                            tvIsPoweredGrowSystemParts2.setTextColor(Color.GREEN);
                        } else {
                            tvIsPoweredGrowSystemParts2.setTextColor(Color.RED);
                        }

                        if (growSystemPart2.isCalibrated()) {
                            tvIsCalibratedGrowSystemParts2.setTextColor(Color.GREEN);
                        } else {
                            tvIsCalibratedGrowSystemParts2.setTextColor(Color.RED);
                        }
                    }
                }
            });
            ivGrowSystemParts2.animate().rotation(360);
        } else if (elapsedOverall >= 3000 && elapsedOverall < 4000 && !updatedIndex3) {
            updatedIndex3 = true;

            ivGrowSystemParts3.animate().setDuration(2500L);
            ivGrowSystemParts3.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // check broken
                    if (growSystemParts.size() > 3) {
                        GrowSystemPart growSystemPart3 = growSystemParts.get(3);
                        ImageView ivGrowSystemPart3 = ivGrowSystemPartsList.get(3);
                        if (growSystemPart3.isPowered() && growSystemPart3.isCalibrated()) {
                            ivGrowSystemPart3.setBackgroundColor(Color.GREEN);
                        } else {
                            ivGrowSystemPart3.setBackgroundColor(Color.RED);
                        }

                        if (growSystemPart3.isPowered()) {
                            tvIsPoweredGrowSystemParts3.setTextColor(Color.GREEN);
                        } else {
                            tvIsPoweredGrowSystemParts3.setTextColor(Color.RED);
                        }

                        if (growSystemPart3.isCalibrated()) {
                            tvIsCalibratedGrowSystemParts3.setTextColor(Color.GREEN);
                        } else {
                            tvIsCalibratedGrowSystemParts3.setTextColor(Color.RED);
                        }
                    }
                }
            });
            ivGrowSystemParts3.animate().rotation(360);
        } else if (elapsedOverall >= 4000 && elapsedOverall < 5000 && !updatedIndex4) {
            updatedIndex4 = true;

            ivGrowSystemParts4.animate().setDuration(2500L);
            ivGrowSystemParts4.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // check broken
                    if (growSystemParts.size() > 4) {
                        GrowSystemPart growSystemPart4 = growSystemParts.get(4);
                        ImageView ivGrowSystemPart4 = ivGrowSystemPartsList.get(4);
                        if (growSystemPart4.isPowered() && growSystemPart4.isCalibrated()) {
                            ivGrowSystemPart4.setBackgroundColor(Color.GREEN);
                        } else {
                            ivGrowSystemPart4.setBackgroundColor(Color.RED);
                        }

                        if (growSystemPart4.isPowered()) {
                            tvIsPoweredGrowSystemParts4.setTextColor(Color.GREEN);
                        } else {
                            tvIsPoweredGrowSystemParts4.setTextColor(Color.RED);
                        }

                        if (growSystemPart4.isCalibrated()) {
                            tvIsCalibratedGrowSystemParts4.setTextColor(Color.GREEN);
                        } else {
                            tvIsCalibratedGrowSystemParts4.setTextColor(Color.RED);
                        }
                    }
                }
            });
            ivGrowSystemParts4.animate().rotation(360);
        } else if (elapsedOverall >= 5000 && elapsedOverall < 6000 && !updatedIndex5) {
            updatedIndex5 = true;

            ivGrowSystemParts5.animate().setDuration(2500L);
            ivGrowSystemParts5.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // check broken
                    if (growSystemParts.size() > 5) {
                        GrowSystemPart growSystemPart5 = growSystemParts.get(5);
                        ImageView ivGrowSystemPart5 = ivGrowSystemPartsList.get(5);
                        if (growSystemPart5.isPowered() && growSystemPart5.isCalibrated()) {
                            ivGrowSystemPart5.setBackgroundColor(Color.GREEN);
                        } else {
                            ivGrowSystemPart5.setBackgroundColor(Color.RED);
                        }

                        if (growSystemPart5.isPowered()) {
                            tvIsPoweredGrowSystemParts5.setTextColor(Color.GREEN);
                        } else {
                            tvIsPoweredGrowSystemParts5.setTextColor(Color.RED);
                        }

                        if (growSystemPart5.isCalibrated()) {
                            tvIsCalibratedGrowSystemParts5.setTextColor(Color.GREEN);
                        } else {
                            tvIsCalibratedGrowSystemParts5.setTextColor(Color.RED);
                        }
                    }

                    //////////////////////////////////////
                    animationListener.onAnimationFinish();
                    //////////////////////////////////////
                }
            });
            ivGrowSystemParts5.animate().rotation(360);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}
