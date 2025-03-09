package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TileSelectorView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

import java.io.Serializable;
import java.util.List;

public class TileSelectorDialogFragment extends DialogFragment {
    public static final String TAG = TileSelectorDialogFragment.class.getSimpleName();
    public static final String ARG_TILE_MANAGER = "tileManager";
    public static final String ARG_TILE_SELECTOR_LISTENER = "tileSelectorListener";
    public static final String ARG_DISMISS_LISTENER = "dismissListener";
    public static final String ARG_MODE_FOR_TILE_SELECTOR_VIEW = "modeForTileSelectorView";

    public interface TileSelectorListener extends Serializable {
        void selected(List<Tile> tiles, TileSelectorView.Mode modeForTileSelectorView);
    }

    public interface DismissListener extends Serializable {
        void onDismiss();
    }

    private TileSelectorListener tileSelectorListener;
    private DismissListener dismissListener;
    private TileManager tileManager;
    private TileSelectorView.Mode modeForTileSelectorView;

    private TileSelectorView tileSelectorView;

    public static TileSelectorDialogFragment newInstance(TileManager tileManager,
                                                         TileSelectorView.Mode modeForTileSelectorView,
                                                         TileSelectorListener tileSelectorListener,
                                                         DismissListener dismissListener) {
        TileSelectorDialogFragment fragment = new TileSelectorDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TILE_MANAGER, tileManager);
        args.putSerializable(ARG_MODE_FOR_TILE_SELECTOR_VIEW, modeForTileSelectorView);
        args.putSerializable(ARG_TILE_SELECTOR_LISTENER, tileSelectorListener);
        args.putSerializable(ARG_DISMISS_LISTENER, dismissListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tileManager = (TileManager) getArguments().getSerializable(ARG_TILE_MANAGER);
            modeForTileSelectorView = (TileSelectorView.Mode) getArguments().getSerializable(ARG_MODE_FOR_TILE_SELECTOR_VIEW);
            tileSelectorListener = (TileSelectorListener) getArguments().getSerializable(ARG_TILE_SELECTOR_LISTENER);
            dismissListener = (DismissListener) getArguments().getSerializable(ARG_DISMISS_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");

        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);

        return inflater.inflate(R.layout.dialogfragment_tile_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        tileSelectorView = (TileSelectorView) view.findViewById(R.id.view_tile_selector);
        tileSelectorView.init(tileManager);
        tileSelectorView.setMode(modeForTileSelectorView);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");

        tileSelectorListener.selected(
                tileSelectorView.getTilesSelected(),
                modeForTileSelectorView
        );
        dismissListener.onDismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}
