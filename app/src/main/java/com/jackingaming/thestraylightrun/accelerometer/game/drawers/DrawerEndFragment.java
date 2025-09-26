
package com.jackingaming.thestraylightrun.accelerometer.game.drawers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerEndFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerEndFragment extends Fragment {
    public static final String TAG = DrawerEndFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_DRAWER_END_LISTENER = "drawerEndListener";

    public interface DrawerEndListener extends Serializable {
        void onSubmitJournalEntry(View view, String journalEntry);
    }

    private DrawerEndListener listener;

    private LinearLayout rightDrawer;
    private TextView tvJournalPrompt;
    private EditText etJournalEntry;
    private Button buttonSubmitJournal;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DrawerEndFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerEndFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerEndFragment newInstance(String param1, String param2,
                                                DrawerEndListener drawerEndListener) {
        Log.e(TAG, "newInstance()");
        DrawerEndFragment fragment = new DrawerEndFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_DRAWER_END_LISTENER, drawerEndListener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            listener = (DrawerEndListener) getArguments().getSerializable(ARG_DRAWER_END_LISTENER);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach()");
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_end, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        rightDrawer = view.findViewById(R.id.right_drawer);
        tvJournalPrompt = view.findViewById(R.id.tv_journal_prompt);
        etJournalEntry = view.findViewById(R.id.et_journal_entry);
        buttonSubmitJournal = view.findViewById(R.id.button_submit_journal);

        buttonSubmitJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entry = etJournalEntry.getText().toString().trim();
                if (!entry.isEmpty()) {
                    listener.onSubmitJournalEntry(view, entry);
                }
            }
        });
    }

    public void updateJournalPrompt(Game.Run run) {
        String prompt = null;
        switch (run) {
            case ONE:
                prompt = "What's one way you could explain the difference between a class and an object to someone who's never coded before?";
                break;
            case TWO:
                prompt = "Think of a real-life decision you make every day. How could you turn that into an if/else statement?";
                break;
            case THREE:
                prompt = "When have you had to repeat the same task over and over? How would a for loop help in that situation?";
                break;
            case FOUR:
                prompt = "If you could make a List of anything right now - snacks, games, or even chores - what would be on it?";
                break;
            case FIVE:
                prompt = "If you had to build a simple program to take care of a farm, which parts would you use classes, loops, lists, and if/else for?";
                break;
        }
        tvJournalPrompt.setText(prompt);
    }
}