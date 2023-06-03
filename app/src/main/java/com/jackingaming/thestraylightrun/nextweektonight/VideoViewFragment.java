package com.jackingaming.thestraylightrun.nextweektonight;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.jackingaming.thestraylightrun.R;

public class VideoViewFragment extends Fragment {
    public static final String TAG = VideoViewFragment.class.getSimpleName();
    private static final String POSITION_CURRENT = "position_current";
    private static final String VIDEO_SAMPLE = "vid_20230603_145112";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private VideoView videoView;
    private int positionCurrent;

    public VideoViewFragment() {
        // Required empty public constructor
    }

    public static VideoViewFragment newInstance(String param1, String param2) {
        VideoViewFragment fragment = new VideoViewFragment();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");

        initializePlayer();
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

    private void initializePlayer() {
        Log.i(TAG, "initializePlayer()");

        Uri videoUri = getMedia((VIDEO_SAMPLE));
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