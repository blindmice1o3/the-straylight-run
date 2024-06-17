package com.jackingaming.thestraylightrun.sandbox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.jackingaming.thestraylightrun.R;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SandboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SandboxFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvGreeting = view.findViewById(R.id.tv_greeting);

        // ref: https://stackoverflow.com/questions/40422340/is-it-possible-to-change-start-end-values-of-valueanimator-on-animation-repeat
        random = new Random();
        float[] hsv = new float[3];
        float[] from = new float[3];
        float[] to = new float[3];

        int colorStart = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int colorNext = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        // use HSV to get a nicer (smooth) transition.
        Color.colorToHSV(colorStart, from);
        Color.colorToHSV(colorNext, to);

        animatorColor = ValueAnimator.ofObject(new ArgbEvaluatorCompat(), colorStart, colorNext);
        animatorColor.addListener(new AnimatorListenerAdapter() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

                int colorStart = Color.HSVToColor(to);
                int colorNext = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                Color.colorToHSV(colorStart, from);
                Color.colorToHSV(colorNext, to);
            }
        });
        animatorColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                hsv[0] = from[0] + (to[0] - from[0]) * valueAnimator.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1]) * valueAnimator.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2]) * valueAnimator.getAnimatedFraction();

                tvGreeting.setBackgroundColor(Color.HSVToColor(hsv));
            }
        });
        animatorColor.setDuration(8000L);
        animatorColor.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    public void onResume() {
        super.onResume();

        animatorColor.start();
    }
}