package com.jackingaming.thestraylightrun.sandbox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SandboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SandboxFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SandboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SandboxFragment.
     */
    public static SandboxFragment newInstance(String param1, String param2) {
        SandboxFragment fragment = new SandboxFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sandbox, container, false);
    }

    private Random random;
    private ValueAnimator animatorColor;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int colorEnd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvGreeting = view.findViewById(R.id.tv_greeting);

        // ref1: https://stackoverflow.com/questions/40422340/is-it-possible-to-change-start-end-values-of-valueanimator-on-animation-repeat
        // ref2: https://stackoverflow.com/questions/18216285/android-animate-color-change-from-color-to-color/24641977#24641977
        random = new Random();

        // start color: random.
        int colorStart = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        // end color: use HSV to get a nicer (smooth) transition.
        colorEnd = changeHueSaturationValue(colorStart, random.nextInt(256));

        animatorColor = ValueAnimator.ofArgb(colorStart, colorEnd);
        animatorColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                tvGreeting.setBackgroundColor((Integer) animatorColor.getAnimatedValue());
            }
        });
        animatorColor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

                // start color: end color of previous repetition.
                int colorStart = colorEnd;
                // end color: use HSV to get a nicer (smooth) transition.
                colorEnd = changeHueSaturationValue(colorStart, random.nextInt(256));

                animatorColor.setIntValues(colorStart, colorEnd);
                animatorColor.setEvaluator(argbEvaluator);
            }
        });
        animatorColor.setDuration(4000L);
        animatorColor.setRepeatCount(ValueAnimator.INFINITE);

        Animation hyperspaceJump = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
        View viewTweenAnimation = view.findViewById(R.id.view_tween_animation);
        viewTweenAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTweenAnimation.startAnimation(hyperspaceJump);
            }
        });

        ImageView ivScaleY = view.findViewById(R.id.iv_scaleY);
        ivScaleY.setOnClickListener(new View.OnClickListener() {

            private boolean isOriginalHeight = true;

            @Override
            public void onClick(View view) {
                float scaleX = 0;
                float scaleY = 0;
                float alpha = 0;
                if (isOriginalHeight) {
                    isOriginalHeight = false;

                    ivScaleY.animate()
                            .scaleX(-1f)
                            .scaleY(1.5f)
                            .alpha(0.25f)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(2000);
                } else {
                    isOriginalHeight = true;

                    ivScaleY.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .alpha(1f)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(2000);
                }
            }
        });
    }

    private int changeHueSaturationValue(int color, int delta) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        hsv[0] = hsv[0] + delta;
        hsv[1] = hsv[1] + delta;
        hsv[2] = hsv[2] + delta;

        hsv[0] = hsv[0] % 256;
        hsv[1] = hsv[1] % 256;
        hsv[2] = hsv[2] % 256;

        return Color.HSVToColor(Color.alpha(color), hsv);
    }

    @Override
    public void onResume() {
        super.onResume();

        animatorColor.start();
    }
}