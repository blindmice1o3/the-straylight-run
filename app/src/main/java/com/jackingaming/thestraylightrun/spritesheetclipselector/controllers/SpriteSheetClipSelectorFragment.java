package com.jackingaming.thestraylightrun.spritesheetclipselector.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.spritesheetclipselector.views.MyImageView;

public class SpriteSheetClipSelectorFragment extends Fragment
        implements FragmentResultListener {
    public static final String TAG = SpriteSheetClipSelectorFragment.class.getSimpleName();
    private static final String STATE_SCREEN_X = "screen_x";
    private static final String STATE_SCREEN_Y = "screen_y";
    private static final String STATE_DATASOURCE_X = "datasource_x";
    private static final String STATE_DATASOURCE_Y = "datasource_y";
    private static final String STATE_DATASOURCE_WIDTH = "datasource_width";
    private static final String STATE_DATASOURCE_HEIGHT = "datasource_height";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SpriteSheetClipSelectorFragment() {
        // Required empty public constructor
    }

    public static SpriteSheetClipSelectorFragment newInstance(String param1, String param2) {
        SpriteSheetClipSelectorFragment fragment = new SpriteSheetClipSelectorFragment();
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
        return inflater.inflate(R.layout.fragment_sprite_sheet_clip_selector, container, false);
    }

    private MyImageView myImageView;
    private TextView tvScreenX;
    private TextView tvScreenY;
    private TextView tvDatasourceX;
    private TextView tvDatasourceY;
    private TextView tvDatasourceWidth;
    private TextView tvDatasourceHeight;
    private Button buttonDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvScreenX = view.findViewById(R.id.tv_screen_x);
        tvScreenY = view.findViewById(R.id.tv_screen_y);
        tvDatasourceX = view.findViewById(R.id.tv_datasource_x);
        tvDatasourceY = view.findViewById(R.id.tv_datasource_y);
        tvDatasourceWidth = view.findViewById(R.id.tv_datasource_width);
        tvDatasourceHeight = view.findViewById(R.id.tv_datasource_height);


        if (savedInstanceState != null) {
            tvScreenX.setText(savedInstanceState.getString(STATE_SCREEN_X));
            tvScreenY.setText(savedInstanceState.getString(STATE_SCREEN_Y));
            tvDatasourceX.setText(savedInstanceState.getString(STATE_DATASOURCE_X));
            tvDatasourceY.setText(savedInstanceState.getString(STATE_DATASOURCE_Y));
            tvDatasourceWidth.setText(savedInstanceState.getString(STATE_DATASOURCE_WIDTH));
            tvDatasourceHeight.setText(savedInstanceState.getString(STATE_DATASOURCE_HEIGHT));
        } else {
            tvScreenX.setText("rectSelectedX: 0");
            tvScreenY.setText("rectSelectedY: 0");
            tvDatasourceX.setText("datasourceX: 0");
            tvDatasourceY.setText("datasourceY: 0");
            tvDatasourceWidth.setText("datasourceWidth: " + MyImageView.DEFAULT_WIDTH_IN_PIXEL);
            tvDatasourceHeight.setText("datasourceHeight: " + MyImageView.DEFAULT_HEIGHT_IN_PIXEL);
        }

        myImageView = view.findViewById(R.id.iv_sprite_sheet);
        myImageView.setImageResource(R.drawable.pokemon_mystery_dungeon_red_rescue_team_snorlax);
        myImageView.setListener(new MyImageView.ValueChangedListener() {
            @Override
            public void onRectSelectedXChanged(float xScreen) {
                String formatString = "rectSelectedX: %f";
                tvScreenX.setText(String.format(formatString, xScreen));
            }

            @Override
            public void onRectSelectedYChanged(float yScreen) {
                String formatString = "rectSelectedY: %f";
                tvScreenY.setText(String.format(formatString, yScreen));
            }

            @Override
            public void onDatasourceXChanged(float xDatasource) {
                String formatString = "datasourceX: %f";
                tvDatasourceX.setText(String.format(formatString, xDatasource));
            }

            @Override
            public void onDatasourceYChanged(float yDatasource) {
                String formatString = "datasourceY: %f";
                tvDatasourceY.setText(String.format(formatString, yDatasource));
            }
        });

        buttonDialog = view.findViewById(R.id.button_dialog);
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start a dialog fragment.
                SizeEdittingDialogFragment dialogFragment = SizeEdittingDialogFragment.newInstance("Unit: pixel", null);
//                dialogFragment.setCancelable(false);
                dialogFragment.show(getChildFragmentManager(), SizeEdittingDialogFragment.TAG);
            }
        });

        // Register as a listener of fragment result.
        getChildFragmentManager().setFragmentResultListener(
                SizeEdittingDialogFragment.REQUEST_KEY_SIZE_EDITTING,
                this, this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SCREEN_X, tvScreenX.getText().toString());
        outState.putString(STATE_SCREEN_Y, tvScreenY.getText().toString());
        outState.putString(STATE_DATASOURCE_X, tvDatasourceX.getText().toString());
        outState.putString(STATE_DATASOURCE_Y, tvDatasourceY.getText().toString());
        outState.putString(STATE_DATASOURCE_WIDTH, tvDatasourceWidth.getText().toString());
        outState.putString(STATE_DATASOURCE_HEIGHT, tvDatasourceHeight.getText().toString());
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        Log.i(TAG, "onFragmentResult()");

        if (requestKey.equals(SizeEdittingDialogFragment.REQUEST_KEY_SIZE_EDITTING)) {
            Log.i(TAG, "requestKey: " + requestKey);

            int widthDatasourceNew = result.getInt(SizeEdittingDialogFragment.BUNDLE_KEY_WIDTH);
            int heightDatasourceNew = result.getInt(SizeEdittingDialogFragment.BUNDLE_KEY_HEIGHT);
            Log.i(TAG, "(widthDatasourceNew: " + widthDatasourceNew + "), (heightDatasourceNew: " + heightDatasourceNew + ")");

            myImageView.setRectSelectedScreenWidth(
                    myImageView.convertToScreenCoordinateSystemHorizontally(widthDatasourceNew)
            );
            myImageView.setRectSelectedScreenHeight(
                    myImageView.convertToScreenCoordinateSystemVertically(heightDatasourceNew)
            );

            tvDatasourceWidth.setText("datasourceWidth: " + widthDatasourceNew);
            tvDatasourceHeight.setText("datasourceHeight: " + heightDatasourceNew);
        }
    }
}