package com.jackingaming.thestraylightrun.accelerometer.game.drawers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerTopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerTopFragment extends Fragment {
    public static final String TAG = DrawerTopFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_RUN_SELECTION_LISTENER = "run_selection_listener";

    public interface RunSelectionListener extends Serializable {
        void onRunSelected(Game.Run run);

        void onCloseDrawerTop();
    }

    private RunSelectionListener runSelectionListener;

    private TextView tvRunOne, tvRunTwo, tvRunThree, tvRunFour, tvRunFive;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DrawerTopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerTopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerTopFragment newInstance(String param1, String param2,
                                                RunSelectionListener runSelectionListener) {
        Log.e(TAG, "newInstance()");
        DrawerTopFragment fragment = new DrawerTopFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_RUN_SELECTION_LISTENER, runSelectionListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        Bundle arguments = getArguments();
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1);
            mParam2 = arguments.getString(ARG_PARAM2);
            runSelectionListener = (RunSelectionListener) arguments.getSerializable(ARG_RUN_SELECTION_LISTENER);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tvRunOne = view.findViewById(R.id.tv_run_one);
        tvRunTwo = view.findViewById(R.id.tv_run_two);
        tvRunThree = view.findViewById(R.id.tv_run_three);
        tvRunFour = view.findViewById(R.id.tv_run_four);
        tvRunFive = view.findViewById(R.id.tv_run_five);

        tvRunOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runSelectionListener.onRunSelected(Game.Run.ONE);
                runSelectionListener.onCloseDrawerTop();
            }
        });
        tvRunTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runSelectionListener.onRunSelected(Game.Run.TWO);
                runSelectionListener.onCloseDrawerTop();
            }
        });
        tvRunThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runSelectionListener.onRunSelected(Game.Run.THREE);
                runSelectionListener.onCloseDrawerTop();
            }
        });
        tvRunFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runSelectionListener.onRunSelected(Game.Run.FOUR);
                runSelectionListener.onCloseDrawerTop();
            }
        });
        tvRunFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runSelectionListener.onRunSelected(Game.Run.FIVE);
                runSelectionListener.onCloseDrawerTop();
            }
        });
    }
}