package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.AdapterDrink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;

import java.util.List;

public class LabelPrinterFragment extends Fragment {
    public static final String TAG = LabelPrinterFragment.class.getSimpleName();

    public interface Listener {
        void onInitializationCompleted(LabelPrinter labelPrinter);
    }

    private Listener listener;

    private ConstraintLayout constraintLayoutLabelPrinter;
    private LabelPrinter labelPrinter;
    private RecyclerView recyclerViewQueueDrinks;

    public LabelPrinterFragment() {
        // Required empty public constructor
    }

    public static LabelPrinterFragment newInstance() {
        Log.e(TAG, "newInstance()");
        LabelPrinterFragment fragment = new LabelPrinterFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement InitializationListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label_printer, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        constraintLayoutLabelPrinter = view.findViewById(R.id.constraintlayout_label_printer);
        labelPrinter = view.findViewById(R.id.tv_label_printer);
        recyclerViewQueueDrinks = view.findViewById(R.id.rv_queue_drinks);

        List<Drink> queueDrinks = labelPrinter.getQueueDrinks();
        AdapterDrink adapter = new AdapterDrink(queueDrinks);
        recyclerViewQueueDrinks.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewQueueDrinks.setLayoutManager(linearLayoutManager);
        recyclerViewQueueDrinks.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        labelPrinter.setListener(new LabelPrinter.LabelPrinterListener() {
            @Override
            public void onDrinkAdded(int indexToAdd) {
                adapter.notifyItemInserted(indexToAdd);
            }

            @Override
            public void onDrinkRemoved(int indexToRemove) {
                adapter.notifyItemRemoved(indexToRemove);
            }
        });

        labelPrinter.generateRandomDrinkRequest();
        labelPrinter.updateDisplay();

        listener.onInitializationCompleted(labelPrinter);
    }

    public void changeLabelPrinterMode(String modeSelected) {
        Log.e(TAG, "changeLabelPrinterMode(String) modeSelected: " + modeSelected);

//        if (labelPrinter == null) {
//            Toast.makeText(getContext(), "labelPrinter is null", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (modeSelected.equals("standard")) {
            labelPrinter.selectModeStandard();
//            delayAddNewDrink = DELAY_ADD_NEW_DRINK_EASY;
//            valueBracketYellow = VALUE_BRACKET_YELLOW_EASY;
//            valueBracketRed = VALUE_BRACKET_RED_EASY;
        } else if (modeSelected.equals("customized")) {
            labelPrinter.selectModeCustomized();
//            delayAddNewDrink = DELAY_ADD_NEW_DRINK_MEDIUM;
//            valueBracketYellow = VALUE_BRACKET_YELLOW_MEDIUM;
//            valueBracketRed = VALUE_BRACKET_RED_MEDIUM;
        } else if (modeSelected.equals("both")) {
            labelPrinter.selectModeBoth();
//            delayAddNewDrink = DELAY_ADD_NEW_DRINK_HARD;
//            valueBracketYellow = VALUE_BRACKET_YELLOW_HARD;
//            valueBracketRed = VALUE_BRACKET_RED_HARD;
        }
    }

    public LabelPrinter getLabelPrinter() {
        return labelPrinter;
    }

    public void setLabelPrinter(LabelPrinter labelPrinter) {
        this.labelPrinter = labelPrinter;
    }
}