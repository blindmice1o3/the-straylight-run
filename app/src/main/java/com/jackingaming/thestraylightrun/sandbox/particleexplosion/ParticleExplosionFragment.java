package com.jackingaming.thestraylightrun.sandbox.particleexplosion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;
import com.jackingaming.thestraylightrun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticleExplosionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticleExplosionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ParticleExplosionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParticleExplosionFragment.
     */
    public static ParticleExplosionFragment newInstance(String param1, String param2) {
        ParticleExplosionFragment fragment = new ParticleExplosionFragment();
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
        return inflater.inflate(R.layout.fragment_particle_explosion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParticleExplosionView particleExplosionView = view.findViewById(R.id.view_particle_explosion);
        Slider slider = view.findViewById(R.id.slider_particle_explosion);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                particleExplosionView.updateProgressOfParticles(value);
            }
        });
    }
}