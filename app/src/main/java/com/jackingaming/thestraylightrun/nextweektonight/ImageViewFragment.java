package com.jackingaming.thestraylightrun.nextweektonight;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

public class ImageViewFragment extends Fragment {
    public static final String TAG = ImageViewFragment.class.getSimpleName();

    private static final String ARG_RESOURCE_ID_IMAGE = "resourceIdImage";

    private int resourceIdImage = -1;

    private ImageView imageView;

    public ImageViewFragment() {
        // Required empty public constructor
    }

    public static ImageViewFragment newInstance(int resourceIdImage) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RESOURCE_ID_IMAGE, resourceIdImage);
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageViewFragment newInstance(String arg1, String arg2) {
        ImageViewFragment fragment = new ImageViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resourceIdImage = getArguments().getInt(ARG_RESOURCE_ID_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.image_view);

        int resourceIdToUse = (resourceIdImage != -1) ?
                resourceIdImage :
                R.drawable.harvest_moon_natsume;
        Drawable drawable = getResources().getDrawable(resourceIdToUse);
        imageView.setImageDrawable(drawable);
    }
}