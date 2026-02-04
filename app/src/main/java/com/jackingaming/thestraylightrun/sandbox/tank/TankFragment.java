package com.jackingaming.thestraylightrun.sandbox.tank;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TankFragment extends Fragment {
    public static final String TAG = TankFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List objectsInGame;

    public TankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TankFragment.
     */
    public static TankFragment newInstance(String param1, String param2) {
        TankFragment fragment = new TankFragment();
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
        return inflater.inflate(R.layout.fragment_tank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Tank tank = new Tank(300, 300, 100);

        // Rotate the tank clockwise
        tank.rotate(5);

        // Fire a projectile
        Projectile p = tank.fire();

        // list of game objects
        objectsInGame = new ArrayList();
        objectsInGame.add(tank);
        objectsInGame.add(p);

        // game loop
        long timePrevious = System.currentTimeMillis();
        long timeElapsed = 0L;
        while (true) {
            // calculate elapsed time
            long timeNow = System.currentTimeMillis();
            timeElapsed = timeNow - timePrevious;

            // update game objects by elapsed time
            for (Object o : objectsInGame) {
                if (o instanceof Tank) {
                    ((Tank) o).updatePosition(timeElapsed);
                } else if (o instanceof Projectile) {
                    ((Projectile) o).update(timeElapsed);
                } else {
                    Log.e(TAG, "game object not Tank and not Projectile");
                }
            }

            // TODO: render game objects.

            // set-up for next iteration
            timePrevious = timeNow;
        }
    }
}