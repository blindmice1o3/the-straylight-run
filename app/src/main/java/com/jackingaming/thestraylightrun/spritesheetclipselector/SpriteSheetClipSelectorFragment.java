package com.jackingaming.thestraylightrun.spritesheetclipselector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.spritesheetclipselector.views.MyImageView;

public class SpriteSheetClipSelectorFragment extends Fragment {
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

    private TextView tvXScreen;
    private TextView tvYScreen;
    private TextView tvXDatasource;
    private TextView tvYDatasource;
    private MyImageView myImageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvXScreen = view.findViewById(R.id.tv_screen_x);
        tvYScreen = view.findViewById(R.id.tv_screen_y);
        tvXDatasource = view.findViewById(R.id.tv_datasource_x);
        tvYDatasource = view.findViewById(R.id.tv_datasource_y);

        myImageView = view.findViewById(R.id.iv_sprite_sheet);
        myImageView.setImageResource(R.drawable.pokemon_mystery_dungeon_red_rescue_team_snorlax);
        myImageView.setListener(new MyImageView.ValueChangedListener() {
            @Override
            public void onXRectSelectedChanged(float xScreen) {
                String formatString = "xRectSelected: %f";
                tvXScreen.setText(String.format(formatString, xScreen));
            }

            @Override
            public void onYRectSelectedChanged(float yScreen) {
                String formatString = "yRectSelected: %f";
                tvYScreen.setText(String.format(formatString, yScreen));
            }

            @Override
            public void onXDatasourceChanged(float xDatasource) {
                String formatString = "xDatasource: %f";
                tvXDatasource.setText(String.format(formatString, xDatasource));
            }

            @Override
            public void onYDatasourceChanged(float yDatasource) {
                String formatString = "yDatasource: %f";
                tvYDatasource.setText(String.format(formatString, yDatasource));
            }
        });
    }
}