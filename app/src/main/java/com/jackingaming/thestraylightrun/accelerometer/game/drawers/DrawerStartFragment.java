package com.jackingaming.thestraylightrun.accelerometer.game.drawers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerStartFragment extends Fragment {
    public static final String TAG = DrawerStartFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public interface DrawerStartListener {
        void onClickTypeWriterTextView(View view, String tag);
    }

    private DrawerStartListener listener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DrawerStartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerStartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerStartFragment newInstance(String param1, String param2) {
        Log.e(TAG, "newInstance()");
        DrawerStartFragment fragment = new DrawerStartFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");
        if (context instanceof DrawerStartListener) {
            listener = (DrawerStartListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    " must implement DrawerStartListener");
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
        return inflater.inflate(R.layout.fragment_drawer_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        view.findViewById(R.id.type_writer_tv_in_drawer_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickTypeWriterTextView(view, DrawerStartFragment.TAG);
            }
        });
    }
}