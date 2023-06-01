package com.jackingaming.thestraylightrun.textviewasmarquee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackingaming.thestraylightrun.R;

public class TextViewAsMarqueeFragment extends Fragment {
    public static final String TAG = TextViewAsMarqueeFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView tvLTR;
    private TextView tvRTL;

    public TextViewAsMarqueeFragment() {
        // Required empty public constructor
    }

    public static TextViewAsMarqueeFragment newInstance(String param1, String param2) {
        TextViewAsMarqueeFragment fragment = new TextViewAsMarqueeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_text_view_as_marquee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

        tvLTR = view.findViewById(R.id.tv_ltr);
        tvLTR.setSelected(true);

        tvRTL = view.findViewById(R.id.tv_rtl);
        tvRTL.setSelected(true);
    }
}