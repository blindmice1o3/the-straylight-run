package com.jackingaming.thestraylightrun.nextweektonight;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.HomePlayerRoom01Scene;

import java.io.Serializable;

public class VideoViewFragment extends Fragment
        implements Serializable {
    public static final String TAG = VideoViewFragment.class.getSimpleName();
    private static final String POSITION_CURRENT = "position_current";

    private static final String ARG_RESOURCE_ID_VIDEO = "resourceIdVideo";
    private static final String ARG_ON_COMPLETION_LISTENER_DTO = "onCompletionListenerDTO";

    private String resourceIdVideo;
    private OnCompletionListenerDTO onCompletionListenerDTO;

    private TextView tvCloseButton;
    private VideoView videoView;
    private int positionCurrent;

    public VideoViewFragment() {
        // Required empty public constructor
    }

    public static VideoViewFragment newInstance(String resourceIdVideo,
                                                OnCompletionListenerDTO onCompletionListenerDTO) {
        VideoViewFragment fragment = new VideoViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESOURCE_ID_VIDEO, resourceIdVideo);
        args.putSerializable(ARG_ON_COMPLETION_LISTENER_DTO, onCompletionListenerDTO);
        fragment.setArguments(args);
        return fragment;
    }

    public static VideoViewFragment newInstance() {
        VideoViewFragment fragment = new VideoViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        if (getArguments() != null) {
            resourceIdVideo = getArguments().getString(ARG_RESOURCE_ID_VIDEO);
            onCompletionListenerDTO = (OnCompletionListenerDTO) getArguments().getSerializable(ARG_ON_COMPLETION_LISTENER_DTO);
        }

        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState != null");

            positionCurrent = savedInstanceState.getInt(POSITION_CURRENT);
        } else {
            Log.i(TAG, "savedInstanceState == null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        return inflater.inflate(R.layout.fragment_video_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

//        StringBuilder sb = new StringBuilder();
//        String logo = "Next Week Tonight";
//        for (int i = 0; i < 100; i++) {
//            sb.append(logo + "    ");
//        }
//        String logoRepeated100Times = sb.toString();
//        List<String> rowsOfLogoRepeated100Times = new ArrayList();
//        int counter = 0;
//        for (int i = 0; i < 100; i++) {
//            counter++;
//            if (counter > 4) {
//                counter = 1;
//            }
//
//            if (counter == 1 || counter == 2) {
//                rowsOfLogoRepeated100Times.add(logoRepeated100Times);
//            } else {
//                rowsOfLogoRepeated100Times.add("    " + logoRepeated100Times);
//            }
//        }
//        AnimatedTextViewAdapter adapter = new AnimatedTextViewAdapter(rowsOfLogoRepeated100Times);
//
//        recyclerViewMarquee = view.findViewById(R.id.rv_animated_textview);
//        recyclerViewMarquee.setAdapter(adapter);
//        recyclerViewMarquee.setLayoutManager(new LinearLayoutManager(getContext()));

        videoView = view.findViewById(R.id.video_view);
        videoView.setZOrderMediaOverlay(true);

        tvCloseButton = view.findViewById(R.id.tv_close_button);
        tvCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePlayerRoom01Scene.getInstance().closeTelevision();
            }
        });
        tvCloseButton.setZ(1f);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");

        initializePlayer(resourceIdVideo);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");

        // "If you only stop playing your video in [onStop()], as in the previous
        // step, then on older devices there may be a few seconds where even though
        // the app is no longer visible on screen, the video's audio track continues
        // to play while [onStop()] catches up. This test for older versions of
        // Android pauses the actual playback in [onPause()] to prevent the sound
        // from playing after the app has disappeared from the screen."
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
        positionCurrent = videoView.getCurrentPosition();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");

        outState.putInt(POSITION_CURRENT, positionCurrent);
    }

    private Uri getMedia(String nameMedia) {
        Log.i(TAG, "getMedia() nameMedia: " + nameMedia);

        return Uri.parse("android.resource://" + getActivity().getPackageName() +
                "/raw/" + nameMedia);
    }

    private void initializePlayer(String nameMedia) {
        Log.i(TAG, "initializePlayer()");

        Uri videoUri = (nameMedia != null) ? getMedia(nameMedia) : getMedia("pxl_20250429_193429506");
        videoView.setVideoURI(videoUri);

        if (positionCurrent > 0) {
            // Indicates that the video was playing at some point.
            videoView.seekTo(positionCurrent);
        } else {
            // Skipping to 1 shows the first frame of the video.
            videoView.seekTo(1);
        }

        videoView.setOnCompletionListener(
                onCompletionListenerDTO.getOnCompletionListener()
        );
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                Toast.makeText(getContext(), "Playback complete", Toast.LENGTH_SHORT).show();
//            }
//        });

        videoView.start();
    }

    private void releasePlayer() {
        Log.i(TAG, "releasePlayer()");

        videoView.stopPlayback();
    }
}