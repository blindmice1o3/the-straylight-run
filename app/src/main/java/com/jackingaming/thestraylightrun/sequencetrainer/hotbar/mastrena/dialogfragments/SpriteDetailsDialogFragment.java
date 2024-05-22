package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.AdapterSpriteDetails;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CaramelDrizzleBottle;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.CinnamonDispenser;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.ClickableAndDraggableImageView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.ClickableAndDraggableTextView;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.DrinkLabel;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteEspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SpriteSyrup;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.SteamingWand;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.AdapterDrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.IceShaker;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.ShotGlass;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.SteamingPitcher;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupCold;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupHot;

import java.util.ArrayList;
import java.util.List;

public class SpriteDetailsDialogFragment extends DialogFragment {
    public static final String TAG = SpriteDetailsDialogFragment.class.getSimpleName();

    public static final String ARG_CLICKABLE_AND_DRAGGABLE_IMAGE_VIEW_SPRITE = "clickableAndDraggableImageViewSprite";
    public static final String ARG_CLICKABLE_AND_DRAGGABLE_TEXT_VIEW_SPRITE = "clickableAndDraggableTextViewSprite";

    private ClickableAndDraggableImageView clickableAndDraggableImageView;
    private ClickableAndDraggableTextView clickableAndDraggableTextView;

    public static SpriteDetailsDialogFragment newInstance(ClickableAndDraggableImageView ivSprite) {
        Log.e(TAG, "newInstance(ClickableAndDraggableImageView)");

        SpriteDetailsDialogFragment fragment = new SpriteDetailsDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKABLE_AND_DRAGGABLE_IMAGE_VIEW_SPRITE, ivSprite);
        fragment.setArguments(args);

        return fragment;
    }

    public static SpriteDetailsDialogFragment newInstance(ClickableAndDraggableTextView tvSprite) {
        Log.e(TAG, "newInstance(ClickableAndDraggableTextView)");

        SpriteDetailsDialogFragment fragment = new SpriteDetailsDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CLICKABLE_AND_DRAGGABLE_TEXT_VIEW_SPRITE, tvSprite);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        if (getArguments() != null) {
            clickableAndDraggableImageView = (ClickableAndDraggableImageView) getArguments().getSerializable(ARG_CLICKABLE_AND_DRAGGABLE_IMAGE_VIEW_SPRITE);
            clickableAndDraggableTextView = (ClickableAndDraggableTextView) getArguments().getSerializable(ARG_CLICKABLE_AND_DRAGGABLE_TEXT_VIEW_SPRITE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_sprite_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");
        TextView tvLabel = view.findViewById(R.id.tv_label);
        RecyclerView rvSpriteDetails = view.findViewById(R.id.rv_sprite_details);

        List<String> contentOfSprite = new ArrayList<>();
        if (clickableAndDraggableImageView != null) {
            Log.e(TAG, "clickableAndDraggableImageView != null");

            if (clickableAndDraggableImageView instanceof CupCold) {
                CupCold cupCold = (CupCold) clickableAndDraggableImageView;

                tvLabel.setText(CupCold.class.getSimpleName() + " (" + cupCold.getTag() + ")");

                List<String> drinkComponentsCupColdPrettyPrinted =
                        AdapterDrinkComponent.convertToPrettyPrint(null, cupCold);

                contentOfSprite.addAll(drinkComponentsCupColdPrettyPrinted);
            } else if (clickableAndDraggableImageView instanceof CupHot) {
                CupHot cupHot = (CupHot) clickableAndDraggableImageView;

                tvLabel.setText(CupHot.class.getSimpleName() + " (" + cupHot.getTag() + ")");

                List<String> drinkComponentsCupHotPrettyPrinted =
                        AdapterDrinkComponent.convertToPrettyPrint(null, cupHot);

                contentOfSprite.addAll(drinkComponentsCupHotPrettyPrinted);
            } else if (clickableAndDraggableImageView instanceof IceShaker) {
                IceShaker iceShaker = (IceShaker) clickableAndDraggableImageView;

                tvLabel.setText(IceShaker.class.getSimpleName());

                List<DrinkComponent> drinkComponentsIceShaker = iceShaker.getDrinkComponentsAsList();

                for (DrinkComponent drinkComponentIceShaker : drinkComponentsIceShaker) {
                    contentOfSprite.add(drinkComponentIceShaker.toString());
                }
            } else if (clickableAndDraggableImageView instanceof ShotGlass) {
                ShotGlass shotGlass = (ShotGlass) clickableAndDraggableImageView;

                tvLabel.setText(ShotGlass.class.getSimpleName());

                for (EspressoShot espressoShot : shotGlass.getEspressoShots()) {
                    contentOfSprite.add(espressoShot.toString());
                }
            } else if (clickableAndDraggableImageView instanceof SteamingPitcher) {
                SteamingPitcher steamingPitcher = (SteamingPitcher) clickableAndDraggableImageView;

                tvLabel.setText(SteamingPitcher.class.getSimpleName());

                if (steamingPitcher.getMilk() != null) {
                    contentOfSprite.add(steamingPitcher.getMilk().toString());
                }
            } else if (clickableAndDraggableImageView instanceof CaramelDrizzleBottle) {
                CaramelDrizzleBottle caramelDrizzleBottle = (CaramelDrizzleBottle) clickableAndDraggableImageView;

                tvLabel.setText(CaramelDrizzleBottle.class.getSimpleName());
                // intentionally blank.
            } else if (clickableAndDraggableImageView instanceof CinnamonDispenser) {
                CinnamonDispenser cinnamonDispenser = (CinnamonDispenser) clickableAndDraggableImageView;

                tvLabel.setText(CinnamonDispenser.class.getSimpleName());
                // intentionally blank.
            } else if (clickableAndDraggableImageView instanceof SpriteEspressoShot) {
                SpriteEspressoShot spriteEspressoShot = (SpriteEspressoShot) clickableAndDraggableImageView;

                tvLabel.setText(SpriteEspressoShot.class.getSimpleName());
                // intentionally blank.
            } else if (clickableAndDraggableImageView instanceof SpriteSyrup) {
                SpriteSyrup spriteSyrup = (SpriteSyrup) clickableAndDraggableImageView;

                tvLabel.setText(SpriteSyrup.class.getSimpleName());
                // intentionally blank.
            } else if (clickableAndDraggableImageView instanceof SteamingWand) {
                SteamingWand steamingWand = (SteamingWand) clickableAndDraggableImageView;

                tvLabel.setText(SteamingWand.class.getSimpleName());
                // intentionally blank.
            } else {
                Log.e(TAG, "else-clause: unknown class.");

                tvLabel.setText("Unknown class");
                // intentionally blank.
            }
        } else if (clickableAndDraggableTextView != null) {
            Log.e(TAG, "clickableAndDraggableTextView != null");

            if (clickableAndDraggableTextView instanceof DrinkLabel) {
                DrinkLabel drinkLabel = (DrinkLabel) clickableAndDraggableTextView;

                tvLabel.setText(DrinkLabel.class.getSimpleName());
                // intentionally blank.
            } else if (clickableAndDraggableTextView instanceof LabelPrinter) {
                LabelPrinter labelPrinter = (LabelPrinter) clickableAndDraggableTextView;

                tvLabel.setText(LabelPrinter.class.getSimpleName());
                // intentionally blank.
            } else {
                Log.e(TAG, "else-clause: unknown class.");

                tvLabel.setText("Unknown class");
                // intentionally blank.
            }
        }

        AdapterSpriteDetails adapterSpriteDetails = new AdapterSpriteDetails(contentOfSprite);
        rvSpriteDetails.setAdapter(adapterSpriteDetails);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "onCancel()");
    }
}
