package com.jackingaming.thestraylightrun.accelerometer.game.drawers;

import android.graphics.Color;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_RUN_SELECTION_LISTENER = "run_selection_listener";
    private static final int COLOR_TEXT_LOCKED = Color.LTGRAY;
    private static final int COLOR_BACKGROUND_LOCKED = Color.DKGRAY;
    private static final int COLOR_TEXT_UNLOCKED = Color.YELLOW;
    private static final int COLOR_BACKGROUND_UNLOCKED = Color.BLUE;

    public interface SelectionListener extends Serializable {
        void onRunSelected(Game.Run run);

        void onCloseDrawerTop();
    }

    private SelectionListener selectionListener;

    private TextView tvRunOne, tvRunTwo, tvRunThree, tvRunFour, tvRunFive;

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
    public static DrawerTopFragment newInstance(String param1, String param2,
                                                SelectionListener runSelectionListener) {
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
            selectionListener = (SelectionListener) arguments.getSerializable(ARG_RUN_SELECTION_LISTENER);
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
                selectionListener.onRunSelected(Game.Run.ONE);
                selectionListener.onCloseDrawerTop();
            }
        });
        tvRunTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.onRunSelected(Game.Run.TWO);
                selectionListener.onCloseDrawerTop();
            }
        });
        tvRunThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.onRunSelected(Game.Run.THREE);
                selectionListener.onCloseDrawerTop();
            }
        });
        tvRunFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.onRunSelected(Game.Run.FOUR);
                selectionListener.onCloseDrawerTop();
            }
        });
        tvRunFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.onRunSelected(Game.Run.FIVE);
                selectionListener.onCloseDrawerTop();
            }
        });

        changeAllRunColorToLocked();
        changeRunColorToUnlocked(Game.Run.ONE);
    }

    public void changeAllRunColorToLocked() {
        tvRunOne.setTextColor(COLOR_TEXT_LOCKED);
        tvRunOne.setBackgroundColor(COLOR_BACKGROUND_LOCKED);
        tvRunTwo.setTextColor(COLOR_TEXT_LOCKED);
        tvRunTwo.setBackgroundColor(COLOR_BACKGROUND_LOCKED);
        tvRunThree.setTextColor(COLOR_TEXT_LOCKED);
        tvRunThree.setBackgroundColor(COLOR_BACKGROUND_LOCKED);
        tvRunFour.setTextColor(COLOR_TEXT_LOCKED);
        tvRunFour.setBackgroundColor(COLOR_BACKGROUND_LOCKED);
        tvRunFive.setTextColor(COLOR_TEXT_LOCKED);
        tvRunFive.setBackgroundColor(COLOR_BACKGROUND_LOCKED);
    }

    public void changeRunColorToUnlocked(Game.Run run) {
        // change PREVIOUS runs' color to be compatible with game.load(Context, String).
        switch (run) {
            case ONE:
                tvRunOne.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunOne.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                break;
            case TWO:
                tvRunOne.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunOne.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunTwo.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunTwo.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                break;
            case THREE:
                tvRunOne.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunOne.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunTwo.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunTwo.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunThree.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunThree.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                break;
            case FOUR:
                tvRunOne.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunOne.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunTwo.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunTwo.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunThree.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunThree.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunFour.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunFour.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                break;
            case FIVE:
                tvRunOne.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunOne.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunTwo.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunTwo.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunThree.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunThree.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunFour.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunFour.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                tvRunFive.setTextColor(COLOR_TEXT_UNLOCKED);
                tvRunFive.setBackgroundColor(COLOR_BACKGROUND_UNLOCKED);
                break;
        }
    }
}