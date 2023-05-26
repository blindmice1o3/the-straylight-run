package com.jackingaming.thestraylightrun.spritesheetclipselector.controllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.jackingaming.thestraylightrun.R;

public class ConfirmationDialogFragment extends DialogFragment {
    public static final String TAG = ConfirmationDialogFragment.class.getSimpleName();

    private static final String ARG_TITLE = "title";
    private static final String ARG_PARAM2 = "param2";

    private String mTitle;
    private String mParam2;

    public ConfirmationDialogFragment() {
    }

    public static ConfirmationDialogFragment newInstance(String title, String param2) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog()");

        return new AlertDialog.Builder(getContext())
                .setTitle(mTitle)
                .setMessage("Hello, Dialog!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "positive button clicked");
                    }
                }).setPositiveButtonIcon(getResources().getDrawable(R.drawable.pokemon_mystery_dungeon_red_rescue_team_snorlax))
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "negative button clicked");
                    }
                })
                .setNegativeButtonIcon(getResources().getDrawable(R.drawable.pokemon_mystery_dungeon_red_rescue_team_snorlax))
                .setNeutralButton("neutral", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "neutral button clicked");
                    }
                })
                .setNeutralButtonIcon(getResources().getDrawable(R.drawable.pokemon_mystery_dungeon_red_rescue_team_snorlax))
                .create();

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.i(TAG, "onCancel()");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i(TAG, "onDismiss()");
    }
}
