package com.jackingaming.thestraylightrun.accelerometer.game.quests.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestDetailFragment extends Fragment
        implements Serializable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUEST = "quest";

    // TODO: Rename and change types of parameters
    private Quest quest;

    public QuestDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuestDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestDetailFragment newInstance(Quest quest) {
        QuestDetailFragment fragment = new QuestDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUEST, quest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quest = (Quest) getArguments().getSerializable(ARG_QUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quest_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvQuestDetails = view.findViewById(R.id.tv_quest_details);
        tvQuestDetails.setText(
                quest.getDialogueForCurrentState()
        );
    }
}