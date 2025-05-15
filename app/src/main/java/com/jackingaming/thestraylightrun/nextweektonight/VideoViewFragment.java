package com.jackingaming.thestraylightrun.nextweektonight;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;

public class VideoViewFragment extends Fragment {
    public static final String TAG = VideoViewFragment.class.getSimpleName();
    private static final String POSITION_CURRENT = "position_current";

    private static final String ARG_RESOURCE_ID_VIDEO = "resourceIdVideo";

    private String resourceIdVideo;

    private VideoView videoView;
    private int positionCurrent;

    public VideoViewFragment() {
        // Required empty public constructor
    }

    public static VideoViewFragment newInstance(String resourceIdVideo) {
        VideoViewFragment fragment = new VideoViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESOURCE_ID_VIDEO, resourceIdVideo);
        fragment.setArguments(args);
        return fragment;
    }

    public static VideoViewFragment newInstance(String arg1, String arg2) {
        VideoViewFragment fragment = new VideoViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        if (getArguments() != null) {
            resourceIdVideo = getArguments().getString(ARG_RESOURCE_ID_VIDEO);
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

        videoView = view.findViewById(R.id.video_view);
        videoView.setZOrderMediaOverlay(true);
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

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(getContext(), "Playback complete", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.start();
    }

    private void releasePlayer() {
        Log.i(TAG, "releasePlayer()");

        videoView.stopPlayback();
    }
}