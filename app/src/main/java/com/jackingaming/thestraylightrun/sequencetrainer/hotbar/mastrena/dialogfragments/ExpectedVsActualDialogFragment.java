package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.dialogfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.AdapterDrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels.cups.CupImageView;

public class ExpectedVsActualDialogFragment extends DialogFragment {
    public static final String TAG = ExpectedVsActualDialogFragment.class.getSimpleName();
    public static final String ARG_DRINK = "drink";
    public static final String ARG_CUP_IMAGE_VIEW = "cupImageView";

    private RecyclerView rvExpected, rvActual;
    private AdapterDrinkComponent adapterExpected, adapterActual;

    public static ExpectedVsActualDialogFragment newInstance(Drink drink, CupImageView cupImageView) {
        ExpectedVsActualDialogFragment fragment = new ExpectedVsActualDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_DRINK, drink);
        args.putSerializable(ARG_CUP_IMAGE_VIEW, cupImageView);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Drink drink = (Drink) getArguments().getSerializable(ARG_DRINK);
        CupImageView cupImageView = (CupImageView) getArguments().getSerializable(ARG_CUP_IMAGE_VIEW);

        adapterExpected = new AdapterDrinkComponent(
                drink, null
        );
        adapterActual = new AdapterDrinkComponent(
                null, cupImageView
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG, "onCreateView()");
        return inflater.inflate(R.layout.dialogfragment_expected_vs_actual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        rvExpected = view.findViewById(R.id.rv_expected);
        rvActual = view.findViewById(R.id.rv_actual);
        // TODO:
        rvExpected.setAdapter(adapterExpected);
        rvActual.setAdapter(adapterActual);

        LinearLayoutManager linearLayoutManagerExpected = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManagerActual = new LinearLayoutManager(getContext());
        rvExpected.setLayoutManager(linearLayoutManagerExpected);
        rvActual.setLayoutManager(linearLayoutManagerActual);

        rvExpected.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvActual.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
